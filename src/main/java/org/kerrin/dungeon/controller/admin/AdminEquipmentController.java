package org.kerrin.dungeon.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.EquipmentType;
import org.kerrin.dungeon.enums.Messages;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.EquipmentForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.AccountMessage;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/admin/equipment")
public class AdminEquipmentController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminEquipmentController.class);
	
	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountMessageService accountMessageService;
	/**
	 * Look at the details of a specific equipment
	 */
	@RequestMapping(value = "/{equipmentId}", method = RequestMethod.GET)
	public String getEquipment(HttpServletRequest request, Model model, @PathVariable long equipmentId) {
		logger.debug("Get Equipment Details");
		if(!request.isUserInRole(AccountPrivilege.VIEW.getName())) {
			model.addAttribute("error", "You do not have access to view the admin pages");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		Equipment equipment = equipmentService.findById(equipmentId);
		
		if(equipment == null) {
			EquipmentForm equipmentForm = new EquipmentForm();
			model.addAttribute("equipmentForm", equipmentForm);
			model.addAttribute("equipment", new Equipment(equipmentForm));
			
			return "admin/modifyEquipment";
		}
		
		model.addAttribute("equipmentForm", new EquipmentForm(equipment));
		model.addAttribute("equipment", equipment);
		
		return "admin/modifyEquipment";
	}
	
	/**
	 * Update a equipment details
	 */
	@RequestMapping(value = "/{equipmentId}", method = RequestMethod.POST)
	public String updateEquipment(HttpServletRequest request, Model model, @PathVariable long equipmentId, 
			@ModelAttribute("equipmentForm") @Valid EquipmentForm equipmentForm, BindingResult bindingResult) {		
		logger.debug("Update Form: "+equipmentForm.toString());
		if(!request.isUserInRole(AccountPrivilege.MODIFY.getName())) {
			model.addAttribute("error", "You do not have access to modify data");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("equipmentForm", equipmentForm);
			model.addAttribute("equipment", new Equipment(equipmentForm));
            return "admin/modifyEquipment";
        }
		Equipment equipment = equipmentService.findById(equipmentForm.getId());
		equipment.setQuality(equipmentForm.getQuality());
		equipment.setLevel(equipmentForm.getLevel());
		equipment.setHardcore(equipmentForm.isHardcore());
		equipment.setIronborn(equipmentForm.isIronborn());
		equipment.setAttributes(Equipment.CURRENT_VERSION, equipmentForm.getAttributes());
		if(equipmentForm.getQuality().getId() >= EquipmentQuality.BROKEN.getId()) {
			equipment.setBaseAttribute(equipmentForm.getBaseAttributeType());
			equipment.setBaseAttributeValue(equipmentForm.getBaseAttributeValue());
		} else {
			equipment.setBaseAttribute(null);
		}
		if(equipmentForm.getQuality().getId() >= EquipmentQuality.INFERIOR.getId()) {
			equipment.setDefenceAttribute(equipmentForm.getDefenceAttributeType());
			equipment.setDefenceAttributeValue(equipmentForm.getDefenceAttributeValue());
		} else {
			equipment.setDefenceAttribute(null);
		}
		if(equipmentForm.getQuality().getId() >= EquipmentQuality.ARTIFACT.getId()) {
			equipment.setAncientAttribute(equipmentForm.getAncientAttributeType());
			equipment.setAncientAttributeValue(equipmentForm.getAncientAttributeValue());
		} else {
			equipment.setAncientAttribute(null);
		}
		equipment.setEquipmentType(equipmentForm.getEquipmentType());
		long sendToAccountId = equipmentForm.getSendToAccountId();
		if(sendToAccountId > 0) {
			equipment.setEquipmentLocation(EquipmentLocation.MESSAGE);
			Account account = accountService.findById(sendToAccountId);
			
			if(account == null) {
				model.addAttribute("error", "Error sending equipment to account "+sendToAccountId + " can't find account");
			} else {			
				String message = accountMessageService.replaceTokens(Messages.ADMIN_ITEM.getMessage(), account);
				AccountMessage accountMessage = accountMessageService.create(sendToAccountId, message, equipment);
				equipment.setEquipmentLocationId(accountMessage.getId());
			}
		}
		
		if(equipment.getId() == equipmentId) {
			try {
				equipment = equipmentService.update(equipment, true);
			} catch (EquipmentNotFound e) {
				model.addAttribute("error", "Error updating equipment: "+e.getMessage());
			}		
		} else {
			model.addAttribute("error", "Equipment ID changed from " + equipmentId + " to " + equipment.getId() + " update cancelled!");
		}
		
		
		model.addAttribute("equipmentForm", new EquipmentForm(equipment));
		model.addAttribute("equipment", equipment);
		
		return "admin/modifyEquipment";
	}
	
	/**
	 * Create a equipment
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String createEquipment(HttpServletRequest request, Model model, @ModelAttribute("equipmentForm") @Valid EquipmentForm equipmentForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			logger.debug("Has errors");
			model.addAttribute("equipmentForm", equipmentForm);
			model.addAttribute("equipment", new Equipment(equipmentForm));
            return "admin/addEquipment";
        }
		logger.debug("Create Form: "+equipmentForm.toString());
		if(!request.isUserInRole(AccountPrivilege.MODIFY.getName())) {
			model.addAttribute("error", "You do not have access to modify data");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		Equipment equipment = new Equipment(0, 
				equipmentForm.getEquipmentType(), equipmentForm.getQuality(), 
				equipmentForm.getLevel(), equipmentForm.isHardcore(), equipmentForm.isIronborn(), 
				equipmentForm.getBaseAttributeType(), equipmentForm.getBaseAttributeValue(), 
				equipmentForm.getDefenceAttributeType(), equipmentForm.getDefenceAttributeValue(), 
				equipmentForm.getAttributes(), 
				equipmentForm.getAncientAttributeType(), equipmentForm.getAncientAttributeValue(), 
				EquipmentLocation.NONE, -1);
		logger.debug("Equipment: "+equipment.toString());
		equipment = equipmentService.create(equipment);
		logger.debug("Equipment Id: "+equipment.getId());
		model.addAttribute("equipmentForm", new EquipmentForm(equipment));
		model.addAttribute("equipment", equipment);
		
		return "admin/modifyEquipment";
	}

	@ModelAttribute("equipmentType")
    public EquipmentType[] getEquipmentType() {
		EquipmentType[] equipmentType = EquipmentType.values();
		EquipmentType[] equipmentTypesNoUnknown = new EquipmentType[equipmentType.length-1];
		for(int i=1; i < equipmentType.length; i++) equipmentTypesNoUnknown[i-1] = equipmentType[i];
		return equipmentTypesNoUnknown;
    }

	@ModelAttribute("attributes")
    public EquipmentAttribute[] getAttributes() {
		return EquipmentAttribute.values();
		/*
		EquipmentAttribute[] attributes = EquipmentAttribute.values();
		EquipmentAttribute[] attributesNoSparkles = new EquipmentAttribute[attributes.length-1];
		for(int i=1; i < attributes.length; i++) attributesNoSparkles[i-1] = attributes[i];
		return attributesNoSparkles;
		*/
    }
}

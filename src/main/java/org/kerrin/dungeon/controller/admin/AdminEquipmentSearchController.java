package org.kerrin.dungeon.controller.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kerrin.dungeon.enums.AccountPrivilege;
import org.kerrin.dungeon.enums.BooleanOptions;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.EquipmentType;
import org.kerrin.dungeon.forms.AccountCreateForm;
import org.kerrin.dungeon.forms.EquipmentForm;
import org.kerrin.dungeon.forms.EquipmentSearchForm;
import org.kerrin.dungeon.forms.LoginForm;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/admin/equipment")
public class AdminEquipmentSearchController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminEquipmentSearchController.class);
	
	@Autowired
	private EquipmentService equipmentService;
	
	/**
	 * List the Equipments. Allow searches for Equipments
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String get(HttpServletRequest request, Model model, @ModelAttribute("equipmentSearchForm") @Valid EquipmentSearchForm searchForm, BindingResult bindingResult) {
		boolean restricted = false;
		logger.trace("Equipment Search: Get");
		if(!request.isUserInRole(AccountPrivilege.VIEW.getName())) {
			model.addAttribute("error", "You do not have access to view the admin pages");
			LoginForm loginForm = new LoginForm();
			model.addAttribute("loginForm", loginForm);
			AccountCreateForm accountCreateForm = new AccountCreateForm();
			model.addAttribute("accountCreateForm", accountCreateForm);
			return "index";
		}
		List<Equipment> equipments = new ArrayList<Equipment>();
		if (bindingResult.hasErrors()) {
			model.addAttribute("equipments", equipments);
			model.addAttribute("equipmentForm", new EquipmentForm());
            return "admin/searchEquipments";
        }
		if(searchForm.getId() > 0) {
			restricted = true;
			Equipment equipment = equipmentService.findById(searchForm.getId());
			if(equipment != null) {
				equipments.add(equipment);
				bindingResult.reject("id", "SearchForm.NotFound.equipmentId");
			}
		}
		if(searchForm.getQuality() != null && !searchForm.getQuality().equals(EquipmentQuality.USELESS)) {
			List<Equipment> foundEquipments = equipmentService.findAllByQualityId(searchForm.getQuality().getId());
			if(!restricted && equipments.isEmpty()) {
				equipments = foundEquipments;
			} else {
				equipments.retainAll(foundEquipments);
			}
			restricted = true;
		}
		if(searchForm.getGreaterThanLevel() >= 1 || searchForm.getLessThanLevel() >= 1) {
			List<Equipment> foundEquipments = equipmentService.findByLevelGreaterThanAndLevelLessThan(searchForm.getGreaterThanLevel()-1, searchForm.getLessThanLevel()+1);
			if(!restricted && equipments.isEmpty()) {
				equipments = foundEquipments;
			} else {
				equipments.retainAll(foundEquipments);
			}
			restricted = true;
		}
		
		if(searchForm.getHardcore() != BooleanOptions.BOTH) {			
			Iterator<Equipment> iter = equipments.iterator();
			while(iter.hasNext()) {
				Equipment thisEquipment = iter.next();
				if(thisEquipment.isHardcore() != searchForm.getHardcore().getBooleanValue()) {
					iter.remove();
				}
			}
		}
		
		if(searchForm.getIronborn() != BooleanOptions.BOTH) {			
			Iterator<Equipment> iter = equipments.iterator();
			while(iter.hasNext()) {
				Equipment thisEquipment = iter.next();
				if(thisEquipment.isIronborn() != searchForm.getIronborn().getBooleanValue()) {
					iter.remove();
				}
			}
		}
		// TODO: valid slots and attributes
		// Paging
		
		model.addAttribute("equipments", equipments);
		model.addAttribute("equipmentForm", new EquipmentForm());
		
		return "admin/searchEquipments";
	}

	@ModelAttribute("equipmentQualities")
    public EquipmentQuality[] getEquipmentQualities() {
		return EquipmentQuality.values();
    }

	@ModelAttribute("equipmentType")
    public EquipmentType[] getEquipmentType() {
		EquipmentType[] equipmentType = EquipmentType.values();
		EquipmentType[] equipmentTypesNoUnknown = new EquipmentType[equipmentType.length-1];
		for(int i=1; i < equipmentType.length; i++) equipmentTypesNoUnknown[i-1] = equipmentType[i];
		return equipmentTypesNoUnknown;
    }

	@ModelAttribute("booleanOptions")
    public BooleanOptions[] getBooleanOptions() {
		return BooleanOptions.values();
    }
}

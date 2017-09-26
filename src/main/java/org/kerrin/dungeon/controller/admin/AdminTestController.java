package org.kerrin.dungeon.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.EquipmentType;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.Spell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/admin/test")
public class AdminTestController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminTestController.class);
	
	/**
	 * Look at the details of a specific account
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getTest(HttpServletRequest request, Model model) {
		for(EquipmentType type: EquipmentType.values()) {
			List<CharSlot> slots = type.getValidSlots();
			if(slots == null) {
				logger.debug("No slots");
			} else {
				for(CharSlot slot:slots) {
					if(slot == null) {
						logger.debug("Slot null");
					} else {
						logger.debug(slot.getNiceName());
					}
				}
			}
		}
		
		for(Monster mob: Monster.values()) {
			List<Spell> spells = mob.getSpells();
			if(spells == null) {
				logger.debug("No spells");
			} else {
				for(Spell spell:spells) {
					if(spell == null) {
						logger.debug("Spell null");
					} else {
						logger.debug(spell.getNiceName());
					}
				}
			}
		}
		return "test";
	}
}

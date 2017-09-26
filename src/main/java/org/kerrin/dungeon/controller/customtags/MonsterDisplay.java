package org.kerrin.dungeon.controller.customtags;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.MonsterType;
import org.kerrin.dungeon.enums.Spell;
import org.kerrin.dungeon.model.MonsterInstance;

public class MonsterDisplay extends SimpleTagSupport {
	
	private String spanId;
	
	private Monster monster;
	
	private MonsterType monsterType;
	
	private int level = -1;
	
	private boolean fullDetails;
	
	private boolean dead = false;
	
	/** Monster hover appears above monster, instead of below */
	private boolean hoverAbove = false;

	public MonsterDisplay() {}

	public void setSpanId(String spanId) {
		this.spanId = spanId;
	}
	
	public void setMonster(Monster monster) {
		this.monster = monster;
	}

	public void setMonsterType(MonsterType monsterType) {
		this.monsterType = monsterType;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setFullDetails(boolean fullDetails) {
		this.fullDetails = fullDetails;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
	public void setHoverAbove(boolean hoverAbove) {
		this.hoverAbove = hoverAbove;
	}

	@Override
    public void doTag() throws JspException, IOException {
		StringBuilder html = new StringBuilder();
		if(monster == null || (!fullDetails && monsterType == null)) {
			html.append("None");
		} else {
			if(fullDetails) {
				if(level < 1) {
					displayAsCharClassDetails(html);
				} else {
					displayAsFullDetails(html);				
				}
			} else {
				displayAsSummaryAndPopUp(html);
			}
		}
		getJspContext().getOut().write(html.toString());
	}

	/**
	 * HTML for full details shown in HTML, no mouse over for pop up
	 * @param html
	 */
	private void displayAsCharClassDetails(StringBuilder html) {
		html.append("<span id=\"");
		html.append(spanId);
		html.append("\"class=\"monsterCharClass\" draggable=\"false\" >");
		html.append("Base Health: ");
		html.append(monster.getHealth());
		html.append("<br/>");
		if(monster.getMana() > 0) {
			html.append("Mana: ");
			html.append(monster.getMana());
			html.append("<br />");
		}
		html.append("Base Damage: ");
		html.append(monster.getBaseWeaponDamage());
		html.append(" of ");
		html.append(monster.getDamageType().getNiceName());
		html.append("<br />");
		List<Spell> spells = monster.getSpells();
		String comma;
		if(spells != null && !spells.isEmpty()) {
			html.append("Spells: ");
			comma = "";
			for(Spell spell:spells) {
				html.append(comma);
				html.append(spell.getNiceName());
				html.append(" (");
				html.append(spell.getMinimumLevel());
				html.append(")");
				// TODO: Dungeon Pedia link to spell
				comma = ", ";
			}
			html.append("<br />");
		}
		HashMap<EquipmentAttribute, Integer> strong = monster.getStrongAttributes();
		if(strong != null && !strong.isEmpty()) {
			html.append("Strengths: ");
			comma = "";
			for(EquipmentAttribute attribute:strong.keySet()) {
				html.append(comma);
				html.append(attribute.getNiceName());
				// TODO: Dungeon Pedia link to attribute
				comma = ", ";
			}
			html.append("<br />");
		}
		HashMap<EquipmentAttribute, Integer> weak = monster.getWeakAttributes();
		if(weak != null && !weak.isEmpty()) {
			html.append("Weaknesses: ");
			comma = "";
			for(EquipmentAttribute attribute:weak.keySet()) {
				html.append(comma);
				html.append(attribute.getNiceName());
				// TODO: Dungeon Pedia link to attribute
				comma = ", ";
			}
			html.append("<br />");
		}
		List<Monster> summons = monster.getSummons();
		if(summons != null && !summons.isEmpty()) {
			html.append("Summons: ");
			comma = "";
			for(Monster summon:summons) {
				html.append(comma);
				html.append(summon.getNiceName());
				// TODO: Dungeon Pedia link to monster
				comma = ", ";
			}
			html.append("<br />");
		}
		html.append("</span>");
	}
	
	/**
	 * HTML for full details shown in HTML, no mouse over for pop up
	 * @param html
	 */
	private void displayAsFullDetails(StringBuilder html) {
		html.append("<span id=\"");
		html.append(spanId);
		html.append("\"class=\"monsterFull\" draggable=\"false\" >");
		html.append(monster.getNiceName());
		html.append("<br />");
		html.append("XP: ");
		html.append(monster.getBaseXp() * level);
		html.append("<br />");
		for(MonsterType type: MonsterType.values()) {
			if(type.after(MonsterType.BOSS)) break;
			if(type.equals(MonsterType.NONE)) continue;
			MonsterInstance monsterInstance = new MonsterInstance(monster, type, level, 1);
			html.append("<span class='monsterFullHealth' data-qtiptitle='Note: Each additional party member increases health by 75%'>");
			html.append(type.getNiceName());
			html.append(" Health: ");
			html.append(monsterInstance.getMaxHealth());
			html.append("</span><br/>");
		}
		if(monster.getMana() > 0) {
			html.append("Mana: ");
			html.append(monster.getMana());
			html.append("<br />");
		}
		if(monster.getPackSize() > 1) {
			html.append("Pack Size: ");
			html.append(monster.getPackSize());
			html.append("<br />");
		}
		html.append("Damage: ");
		html.append(monster.getBaseWeaponDamage() * level);
		html.append(" of ");
		html.append(monster.getDamageType().getNiceName());
		html.append("<br />");
		List<Spell> spells = monster.getSpells(level);
		String comma;
		if(spells != null && !spells.isEmpty()) {
			html.append("Spells: ");
			comma = "";
			for(Spell spell:spells) {
				html.append(comma);
				html.append(spell.getNiceName());
				// TODO: Dungeon Pedia link to spell
				comma = ", ";
			}
			html.append("<br />");
		}
		HashMap<EquipmentAttribute, Integer> strong = monster.getStrongAttributes();
		if(strong != null && !strong.isEmpty()) {
			html.append("Strengths: ");
			comma = "";
			for(EquipmentAttribute attribute:strong.keySet()) {
				html.append(comma);
				html.append(attribute.getNiceName());
				// TODO: Dungeon Pedia link to attribute
				comma = ", ";
			}
			html.append("<br />");
		}
		HashMap<EquipmentAttribute, Integer> weak = monster.getWeakAttributes();
		if(weak != null && !weak.isEmpty()) {
			html.append("Weaknesses: ");
			comma = "";
			for(EquipmentAttribute attribute:weak.keySet()) {
				html.append(comma);
				html.append(attribute.getNiceName());
				// TODO: Dungeon Pedia link to attribute
				comma = ", ";
			}
			html.append("<br />");
		}
		List<Monster> summons = monster.getSummons();
		if(summons != null && !summons.isEmpty()) {
			html.append("Summons: ");
			comma = "";
			for(Monster summon:summons) {
				html.append(comma);
				html.append(summon.getNiceName());
				// TODO: Dungeon Pedia link to monster
				comma = ", ";
			}
			html.append("<br />");
		}
		html.append("</span>");
	}
	
	/**
	 * HTML for summary with mouse over pup up for more details
	 * @param html
	 */
	private void displayAsSummaryAndPopUp(StringBuilder html) {
		html.append("<span id=\"");
		html.append(spanId);
		html.append("\"class=\"monster");
		if(dead) {
			html.append(" deadBG");
		}
		html.append(" ");
		html.append(monsterType.getHtmlClass());	
		html.append("\" draggable=\"false\" >");
		html.append(monster.getNiceName());
		html.append("<br />");
		html.append(monsterType.getNiceName());						
		html.append("</span>");
		
		// Start Pop Up Box
		StringBuilder popupHtml = new StringBuilder();
		popupHtml.append("<span style='pointer-events: all; float: left;' id='lb_");
		popupHtml.append(spanId);
		popupHtml.append("'>");
		
		popupHtml.append("XP: ");
		popupHtml.append(monster.getBaseXp() * level);
		popupHtml.append("<br />");
		popupHtml.append("<span data-qtiptitle='Note: Each additional party member increases health by 75%'>Health: ");
		MonsterInstance monsterInstance = new MonsterInstance(monster, monsterType, level, 1);
		popupHtml.append(monsterInstance.getMaxHealth());
		popupHtml.append("</span>");
		popupHtml.append("<br />");
		if(monster.getMana() > 0) {
			popupHtml.append("Mana: ");
			popupHtml.append(monster.getMana());
			popupHtml.append("<br />");
		}
		if(monster.getPackSize() > 1) {
			popupHtml.append("Pack Size: ");
			popupHtml.append(monster.getPackSize());
			popupHtml.append("<br />");
		}
		popupHtml.append("Damage: ");
		popupHtml.append(monster.getBaseWeaponDamage() * level);
		popupHtml.append(" of ");
		popupHtml.append(monster.getDamageType().getNiceName());
		popupHtml.append("<br />");
		List<Spell> spells = monster.getSpells(level);
		String comma;
		if(spells != null && !spells.isEmpty()) {
			popupHtml.append("Spells: ");
			comma = "";
			for(Spell spell:spells) {
				popupHtml.append(comma);
				popupHtml.append(spell.getNiceName());
				comma = ", ";
			}
			popupHtml.append("<br />");
		}
		HashMap<EquipmentAttribute, Integer> strong = monster.getStrongAttributes();
		if(strong != null && !strong.isEmpty()) {
			popupHtml.append("Strengths: ");
			comma = "";
			for(EquipmentAttribute attribute:strong.keySet()) {
				popupHtml.append(comma);
				popupHtml.append(attribute.getNiceName());
				comma = ", ";
			}
			popupHtml.append("<br />");
		}
		HashMap<EquipmentAttribute, Integer> weak = monster.getWeakAttributes();
		if(weak != null && !weak.isEmpty()) {
			popupHtml.append("Weaknesses: ");
			comma = "";
			for(EquipmentAttribute attribute:weak.keySet()) {
				popupHtml.append(comma);
				popupHtml.append(attribute.getNiceName());
				comma = ", ";
			}
			popupHtml.append("<br />");
		}
		List<Monster> summons = monster.getSummons();
		if(summons != null && !summons.isEmpty()) {
			popupHtml.append("Summons: ");
			comma = "";
			for(Monster summon:summons) {
				popupHtml.append(comma);
				popupHtml.append(summon.getNiceName());
				comma = ", ";
			}
			popupHtml.append("<br />");
		}
		popupHtml.append("</span>");
		// End Pop Up Box
		
		html.append("<script>");
		html.append("$('#");
		html.append(spanId);
		html.append("').qtip({");
			html.append("show: { solo: true },");
			html.append("hide: { event: 'unfocus', distance: 100 },");
			if(hoverAbove) {
				html.append("position: { adjust: { x: -90, y: -125 } },");
			} else {
				html.append("position: { adjust: { x: -90 } },");
			}
			//html.append("prerender: true,");
			html.append("content: {");
				html.append("text: $(\"");
				html.append(popupHtml);
				html.append("\")");
			
			html.append("}");
		html.append("}, event);");

		html.append("</script>");
	}
}

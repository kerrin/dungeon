package org.kerrin.dungeon.controller.customtags;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.EquipmentType;
import org.kerrin.dungeon.model.BoostItem;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.PowerValues;
import org.kerrin.dungeon.utils.CommonTools;
import org.kerrin.dungeon.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EquipmentDisplay extends SimpleTagSupport {
	private static final Logger logger = LoggerFactory.getLogger(EquipmentDisplay.class);
	
	private Equipment equipment;
	
	private boolean adminView = false;
	private boolean enchantView = false;
	/** Equipment to show in comparisons */
	private boolean comparable = false;
	/** Equipment hover appears above equipment, instead of below */
	private boolean hoverAbove = false;
	/** Show equiped equipment */
	private boolean compare = false;
	private boolean found = false;
	private boolean dragable = false;
	private String dragableSrcFrame = null;
	private boolean dropable = false;
	private int stashId = -1;
	private int charSlotId = -1;
	private int messageId = -1;
	private String accountApiKey = null;
	private int characterId = -1;
	private boolean isTouchScreen = false;
	private int leftOffset = 0;
	private Character character = null;
	private BoostItem usableTypeBoost = null;
	private BoostItem usableRangeBoost = null;
	private BoostItem usableImproveRangeBoost = null;
	private BoostItem usableCurseBoost = null;
	private BoostItem usableQualityBoost = null;
	
	public EquipmentDisplay() {}
	
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}
	
	public void setEnchantView(boolean enchantView) {
		this.enchantView = enchantView;
	}
	
	public void setAdminView(boolean adminView) {
		this.adminView = adminView;
	}
	
	public void setComparable(boolean comparable) {
		this.comparable = comparable;
	}
	
	public void setCompare(boolean compare) {
		this.compare = compare;
	}
	
	public void setHoverAbove(boolean hoverAbove) {
		this.hoverAbove = hoverAbove;
	}
	
	public void setFound(boolean found) {
		this.found = found;
	}
	
	public void setDragable(boolean dragable) {
		this.dragable = dragable;
	}
	
	public void setDragableSrcFrame(String dragableSrcFrame) {
		this.dragableSrcFrame = dragableSrcFrame;
	}
	
	public void setDropable(boolean dropable) {
		this.dropable = dropable;
	}
	
	public void setStashId(int stashId) {
		this.stashId = stashId;
	}

	public void setCharSlotId(int charSlotId) {
		this.charSlotId = charSlotId;
	}
	
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public void setAccountApiKey(String accountApiKey) {
		this.accountApiKey = accountApiKey;
	}

	public void setCharacterId(int characterId) {
		this.characterId = characterId;
	}

	public void setLeftOffset(int leftOffset) {
		this.leftOffset = leftOffset;
	}
	
	public void setIsTouchScreen(boolean isTouchScreen) {
		this.isTouchScreen = isTouchScreen;
	}
	
	public void setCharacter(Character character) {
		this.character = character;
	}

	public void setUsableTypeBoost(BoostItem usableTypeBoost) {
		this.usableTypeBoost = usableTypeBoost;
	}

	public void setUsableRangeBoost(BoostItem usableRangeBoost) {
		this.usableRangeBoost = usableRangeBoost;
	}

	public void setUsableImproveRangeBoost(BoostItem usableImproveRangeBoost) {
		this.usableImproveRangeBoost = usableImproveRangeBoost;
	}

	public void setUsableCurseBoost(BoostItem usableCurseBoost) {
		this.usableCurseBoost = usableCurseBoost;
	}

	public void setUsableQualityBoost(BoostItem usableQualityBoost) {
		this.usableQualityBoost = usableQualityBoost;
	}

	@Override
    public void doTag() throws JspException, IOException {
		StringBuilder html;
		if(enchantView) {
			html = displayEnchantView();
		} else {
			html = displayAsSummary();
		}
		getJspContext().getOut().write(html.toString());
	}

	private StringBuilder displayEnchantView() {
		StringBuilder html = new StringBuilder();
		if(equipment == null || equipment.getId() <= 0) {
			html.append("<span id='emptyEnchantEquipment'>&nbsp;</span>");
		} else {
			html.append("<span id='enchantEquipment'>");			
			

			html.append("<table class='borderLessTable'>");
			html.append("<tr>");
			html.append("<th rowspan='2' colspan='2'>");
			html.append(addEquipmentLevel(false));
			html.append("<br />");
			html.append(equipment.getEquipmentType().getNiceName());
			html.append("<br />");
			html.append(generateQualityName());
			if(usableQualityBoost != null && equipment.getQuality() != EquipmentQuality.ARTIFACT) {
				PageContext pageContext = (PageContext) getJspContext();
				HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
				html.append("<span style='float: right;'><a href=\"javascript: jsonEnchant('");
				html.append(request.getContextPath());
				html.append("/api/account/");
				html.append(accountApiKey);
				html.append("/enchant/");
				html.append(equipment.getId());
				html.append("/quality/");
				html.append(usableQualityBoost.getId());
				html.append("','");
				html.append(request.getContextPath());
				html.append("',");
				html.append(equipment.getId());
				html.append(");\" title='Improve Quality' style='border: 1px solid green;'>");
				html.append(usableQualityBoost.getLevel());
				html.append("<span class='improveQualityBoostIcon'>&nbsp;</span>");
				html.append("</a></span>");
			} else {
				html.append("&nbsp;");
			}
			html.append("</th>");
			html.append("<th rowspan='2' align='center' valign='bottom' style='border: 1px solid black;' data-qtiptitle='The possible range of values of the attrubute type.'>Range</th>");
			html.append("<th colspan='2' align='center' valign='bottom' style='border: 1px solid black;' data-qtiptitle='Reroll the attribute type. e.g. Strength or Armour'>Reroll Type</th>");
			html.append("<th colspan='3' align='center' valign='bottom' style='border: 1px solid black;' data-qtiptitle='Reroll the attribute range value. e.g. 10'>Reroll Range</th>");
			html.append("</tr>");
			html.append("<tr>");
			html.append("<th align='center' valign='bottom' style='border: 1px solid black;'>Tokens</th>");
			html.append("<th align='center' valign='bottom' style='border: 1px solid black;'>Boost<br />Item</th>");
			html.append("<th align='center' valign='bottom' style='border: 1px solid black;'>Tokens</th>");
			html.append("<th colspan='2' align='center' valign='bottom' style='border: 1px solid black;'>Boost<br />Item</th>");
			html.append("</tr>");
			EquipmentAttribute baseAttribute = equipment.getBaseAttribute();
			if(baseAttribute != null) {				
				int baseAttributeValue = equipment.getBaseAttributeValue();
				boolean cursed = htmlEnchantAttribute(html, baseAttribute, baseAttributeValue, true);
				html.append(addEnchantLinks(true, false, baseAttribute.getId(), cursed));
			}
			EquipmentAttribute defenceAttribute = equipment.getDefenceAttribute();
			if(defenceAttribute != null) {				
				int defenceAttributeValue = equipment.getDefenceAttributeValue();
				boolean cursed = htmlEnchantAttribute(html, defenceAttribute, defenceAttributeValue, true);
				html.append(addEnchantLinks(false, true, defenceAttribute.getId(), cursed));
			}
			Map<Integer, Integer> attributes = equipment.getAttributes(false);
			for(Integer attributeId:attributes.keySet()) {
				EquipmentAttribute equipmentAttribute = EquipmentAttribute.fromId(attributeId);

				boolean cursed = htmlEnchantAttribute(html, equipmentAttribute, attributes.get(attributeId), false);
				html.append(addEnchantLinks(false, false, attributeId, cursed));
			}
			// Check for the ancient attribute
			EquipmentAttribute ancientAttribute = equipment.getAncientAttribute();
			if(ancientAttribute == null) {
				html.append("<tr><td colspan='9' style='border-top: 1px solid black;'>&nbsp;</td></tr>");
			} else {
				int minValue = 1;
				int maxValue;
				maxValue = getMaxValue(ancientAttribute);
				int attributeValue = ancientAttribute.calculateRealValue(equipment.getAncientAttributeValue(), equipment.getLevel());
				html.append("<tr><td data-qtiptitle='");
				html.append(ancientAttribute.getDescription());
				html.append("' style='border-left: 1px solid ");
				html.append(equipment.getQuality().getHtmlColour());
				html.append("; border-top: 1px solid ");
				html.append(equipment.getQuality().getHtmlColour());
				html.append("; border-bottom: 1px solid ");
				html.append(equipment.getQuality().getHtmlColour());
				html.append(";'>");
				html.append(StringTools.htmlColourfiy(ancientAttribute.getNiceName()));
				html.append("</td>");
				html.append("<td style='border-right: 1px solid ");
				html.append(equipment.getQuality().getHtmlColour());
				html.append("; border-top: 1px solid ");
				html.append(equipment.getQuality().getHtmlColour());
				html.append("; border-bottom: 1px solid ");
				html.append(equipment.getQuality().getHtmlColour());
				html.append(";'><b>");
				html.append(attributeValue);
				html.append("</b></td><td style='border-right: 1px solid black; border-bottom: 1px solid black;'>");
				html.append(ancientAttribute.calculateRealValue(minValue*2,equipment.getLevel()));
				html.append(" to ");
				html.append(ancientAttribute.calculateRealValue(maxValue*2,equipment.getLevel()));
				html.append("</td>");
				html.append("<td colspan='6' style='border-top: 1px solid black;'>&nbsp;</td></tr>");
			}
			html.append("</table>");
			html.append("</span>"); // Equipment Span
		}
		return html;
	}

	protected boolean htmlEnchantAttribute(StringBuilder html, EquipmentAttribute equipmentAttribute, int attributeValue, boolean baseOrDefense) {
		int minValue = 1;
		int maxValue = getMaxValue(equipmentAttribute);
		int realAttributeValue = equipmentAttribute.calculateRealValue(attributeValue, equipment.getLevel());
		html.append("<tr><td data-qtiptitle='");
		html.append(equipmentAttribute.getDescription());
		html.append("'");
		if(baseOrDefense) {
			html.append(" style='border-left: 1px solid green; border-top: 1px solid green; border-bottom: 1px solid green;'");
		}
		html.append(">");
		html.append(StringTools.htmlColourfiy(equipmentAttribute.getNiceName()));
		html.append("</td><td");
		if(baseOrDefense) {
			html.append(" style='border-right: 1px solid green; border-top: 1px solid green; border-bottom: 1px solid green;'");
		} else {
			html.append(" style='border-right: 1px solid black;'");
		}
		html.append("><b>");
		boolean cursed = false;
		if(realAttributeValue < 0) {
			cursed = true;
			html.append("<font color='red'>");
			html.append(realAttributeValue);
			html.append("</font>");
		} else {
			html.append(realAttributeValue);
		}
		html.append("</b></td><td style='border-right: 1px solid black;'>");
		html.append(equipmentAttribute.calculateRealValue(minValue,equipment.getLevel()));
		html.append(" to ");
		html.append(equipmentAttribute.calculateRealValue(maxValue,equipment.getLevel()));
		html.append("</td>");
		return cursed;
	}

	protected int getMaxValue(EquipmentAttribute equipmentAttribute) {
		int maxValue;
		switch(equipmentAttribute.getAttributeValueMaxType()) {
		case ONE_HUNDRED:
			maxValue=100;
			break;
		case LEVEL:
			maxValue=equipment.getLevel();
			break;
		case SLOTS:
			maxValue=10;
			break;
		case THREE:
			maxValue=3;
			break;
		default:
			logger.error("Unknown attribute value max type {}", equipmentAttribute.getAttributeValueMaxType());
			maxValue=-1;
		}
		return maxValue;
	}

	private StringBuilder addEnchantLinks(boolean isBaseStat, boolean isDefenceStat, int attributeTypeId, boolean cursed) {
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		StringBuilder html = new StringBuilder();
		html.append("<td align='center'>");
		html.append("<a href=\"javascript: jsonEnchant('");
		html.append(request.getContextPath());
		html.append("/api/account/");
		html.append(accountApiKey);
		html.append("/enchant/");
		html.append(equipment.getId());
		html.append("/enchant/");
		if(isBaseStat) html.append("base/");
		if(isDefenceStat) html.append("defence/");
		html.append(attributeTypeId);
		html.append("','");
		html.append(request.getContextPath());
		html.append("',");
		html.append(equipment.getId());
		html.append(");\">");
		int cost = equipment.getLevel();
		if(cursed) {
			cost *= 5;
		}
		if(isBaseStat || isDefenceStat) {
			cost *= 5;
		}
		html.append(cost);
		html.append(" <img src='");
		html.append(request.getContextPath());
		html.append("/images/token.png' alt='Tokens' data-qtiptitle='Dungeon Tokens' class='dungeonToken' /></a>");
		html.append("</td>");
		html.append("<td align='center' style='border-right: 1px solid black;'>");
		if(usableTypeBoost != null) {
			html.append("<a href=\"javascript: jsonEnchant('");
			html.append(request.getContextPath());
			html.append("/api/account/");
			html.append(accountApiKey);
			html.append("/enchant/");
			html.append(equipment.getId());
			html.append("/enchant/");
			if(isBaseStat) html.append("base/");
			if(isDefenceStat) html.append("defence/");
			html.append(attributeTypeId);
			html.append("/");
			html.append(usableTypeBoost.getId());
			html.append("','");
			html.append(request.getContextPath());
			html.append("',");
			html.append(equipment.getId());
			html.append(");\" title='Reroll Type' style='border: 1px solid green;'>");
			html.append(usableTypeBoost.getLevel());
			html.append("<span class='enchantTypeBoostIcon'>&nbsp;</span>");
			html.append("</a>");
		} else {
			html.append("&nbsp;");
		}
		html.append("</td>");
		
		if(cursed) {
			html.append("<td colspan='2'>&nbsp;</td>");
			html.append("<td style='border-right: 1px solid black;'>");
			if(usableCurseBoost != null) {
				html.append("<a href=\"javascript: jsonEnchant('");
				html.append(request.getContextPath());
				html.append("/api/account/");
				html.append(accountApiKey);
				html.append("/enchant/");
				html.append(equipment.getId());
				html.append("/enchant/");
				if(isBaseStat) html.append("base/");
				if(isDefenceStat) html.append("defence/");
				html.append(attributeTypeId);
				html.append("/");
				html.append(usableCurseBoost.getId());
				html.append("','");
				html.append(request.getContextPath());
				html.append("',");
				html.append(equipment.getId());
				html.append(");\" title='Remove Curse' style='border: 1px solid green;'>");
				html.append(usableCurseBoost.getLevel());
				html.append("<span class='removeCurseBoostIcon'>&nbsp;</span>");
				html.append("</a>");
			} else {
				html.append("&nbsp;");
			}
			html.append("</td>");
		} else {
			html.append("<td align='center'>");
			html.append("<a href=\"javascript: jsonEnchant('");
			html.append(request.getContextPath());
			html.append("/api/account/");
			html.append(accountApiKey);
			html.append("/enchant/");
			html.append(equipment.getId());
			html.append("/enchant/");
			html.append(attributeTypeId);
			html.append("/range");
			if(isBaseStat) html.append("/base");
			if(isDefenceStat) html.append("/defence");
			html.append("','");
			html.append(request.getContextPath());
			html.append("',");
			html.append(equipment.getId());
			html.append(");\">");
			if(isBaseStat || isDefenceStat) {
				html.append(equipment.getLevel()*20);
			} else {
				html.append(equipment.getLevel()*10);
			}
			html.append(" <img src='");
			html.append(request.getContextPath());
			html.append("/images/token.png' alt='Tokens' data-qtiptitle='Dungeon Tokens' class='dungeonToken' /></a>");
			html.append("</td>");
			html.append("<td align='center'>");
			if(usableRangeBoost != null) {
				html.append("<a href=\"javascript: jsonEnchant('");
				html.append(request.getContextPath());
				html.append("/api/account/");
				html.append(accountApiKey);
				html.append("/enchant/");
				html.append(equipment.getId());
				html.append("/enchant/");
				if(isBaseStat) html.append("base/");
				if(isDefenceStat) html.append("defence/");
				html.append(attributeTypeId);
				html.append("/");
				html.append(usableRangeBoost.getId());
				html.append("','");
				html.append(request.getContextPath());
				html.append("',");
				html.append(equipment.getId());
				html.append(");\" title='Reroll Range' style='border: 1px solid green;'>");
				html.append(usableRangeBoost.getLevel());
				html.append("<span class='enchantRangeBoostIcon'>&nbsp;</span>");
				html.append("</a>");
			} else {
				html.append("&nbsp;");
			}
			html.append("</td>");
			html.append("<td style='border-right: 1px solid black;'>");
			if(usableImproveRangeBoost != null) {
				html.append("<a href=\"javascript: jsonEnchant('");
				html.append(request.getContextPath());
				html.append("/api/account/");
				html.append(accountApiKey);
				html.append("/enchant/");
				html.append(equipment.getId());
				html.append("/enchant/");
				if(isBaseStat) html.append("base/");
				if(isDefenceStat) html.append("defence/");
				html.append(attributeTypeId);
				html.append("/");
				html.append(usableImproveRangeBoost.getId());
				html.append("','");
				html.append(request.getContextPath());
				html.append("',");
				html.append(equipment.getId());
				html.append(");\" title='Improve Range' style='border: 1px solid green;'>");
				html.append(usableImproveRangeBoost.getLevel());
				html.append("<span class='improveRangeBoostIcon'>&nbsp;</span>");
				html.append("</a>");
			} else {
				html.append("&nbsp;");
			}
			html.append("</td>");
		}
		return html;
	}

	private StringBuilder displayAsSummary() throws JspException {
		if(dragable && dragableSrcFrame == null) {
			throw new JspException("Dragable needs frame");
		}
		if(dropable) {
			if(accountApiKey == null || accountApiKey.isEmpty()) {
				throw new JspException("Dropable needs account api key");
			}
			if(stashId < 0 && charSlotId < 0) {
				throw new JspException("Dropable needs a stash or char slot id");
			}
		}
		if(comparable && charSlotId < 0) {
			throw new JspException("Comparable needs charSlotId also");
		}
		StringBuilder html = new StringBuilder();
		if(equipment == null || equipment.getId() <= 0) {
			html.append("<span id='emptyequipment");
			html.append(dragableSrcFrame);
			if(charSlotId > 0) {
				html.append(charSlotId);
			} else if(stashId > 0) {
				html.append(stashId);
			} else if(messageId > 0) {
				html.append(messageId);
			} else {
				html.append("Unknown");
			}
			html.append("' class='");
			if(dropable) {	
				html.append("itemDrop charSlotEmptyEquipment' ");
				html.append("draggable='false' ");
				html.append(generateDropableHtml());
			} else {
				html.append("itemLarge' ");
				html.append("draggable='false' ");
			}
			html.append(">");
			html.append("");
			html.append("</span>");
			// Create the empty equipment detail pop up
			html.append(createEquipmentDetailPopup(true));
			if(comparable) html.append(emptyCompareCharSlot(charSlotId));
		} else {
			// Equipment to display
			PageContext pageContext = (PageContext) getJspContext();
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest(); 
			
			if(adminView) {
				html.append("<a href='");
				html.append(request.getContextPath());
				html.append("/admin/equipment/");
				html.append(equipment.getId());
				html.append("'>");
				html.append(equipment.getId());
				html.append("</a>");
				html.append("<br />");
			}
			// Pop Up link
			html.append("<span id='equipment");
			if(!adminView) {
				html.append(dragableSrcFrame);
			}
			html.append(equipment.getId());
			html.append("' class='");
			if(adminView) {
				html.append("adminEquipment ");
			}
			
			if(found) {
				html.append("found_");
			}
			if(dragable || dropable) {				
				html.append(addDragableAndDropable(request, true));
			} else {
				html.append("itemLarge charSlotEquipment' ");
				html.append("draggable='false' ");
			}
			html.append(">");
			html.append(addEquipmentSummary(request));
			html.append("</span>");
			
			html.append("<input name='equipment' type='hidden' value='true'>");
			html.append("<input name='itemId' type='hidden' value='");
			html.append(equipment.getId());
			html.append("'>");
			
			// Create the equipment detail pop up
			html.append(createEquipmentDetailPopup(false));

			if(comparable) {
				html.append(createComparableScript());
			}
		}
		return html;
	}

	/**
	 * Id the equipment span is draggable and/or dropable, generate the appropriate attributes and class name
	 * 
	 * @param request
	 * 
	 * @return
	 */
	private StringBuilder addDragableAndDropable(HttpServletRequest request, boolean hasItem) {
		StringBuilder html = new StringBuilder();
		if(dropable) {
			html.append("itemDrop");
		} else {
			html.append("itemDrag");
		}
		if(hasItem) {
			html.append(" charSlotEquipment");
		} else {
			html.append(" charSlotEmptyEquipment");
		}
		html.append("' ");
		if(dragable) {
			html.append("draggable='true' ");
			html.append("ondragstart=\"dragEquipment(event, ");
			html.append(equipment.getId());
			html.append(",");
			html.append(equipment.getSalvageValue());
			html.append(",'");
			html.append("equipment");
			html.append(dragableSrcFrame);
			html.append(equipment.getId());
			html.append("','");
			html.append(request.getContextPath());
			html.append("')\" ");
		} else {
			html.append("draggable='false' ");
		}
		if(dropable) {
			html.append(generateDropableHtml());
		}
		
		return html;
	}

	/**
	 * Generate the equipment summary that is always displayed (level, name and quality)
	 * 
	 * @return
	 */
	private StringBuilder addEquipmentSummary(HttpServletRequest request) {
		StringBuilder html = new StringBuilder();
		html.append(addEquipmentLevel(false));
		StringBuilder appendClass = new StringBuilder();
		if(equipment.getRequiredLevel() != equipment.getLevel()) {
			appendClass.append("ReducedLevel");
		}
		
		html.append("<span class='equipmentQuality");
		html.append(appendClass);
		html.append("'>");
		html.append(generateQualityName());
		html.append("</span>");
		html.append("<span class='equipmentType");
		html.append(appendClass);
		html.append("'>");
		html.append("<span class='charSlotSpan'>");
		html.append("<img class='charSlotIcon");
		html.append(equipment.getEquipmentType().getValidSlots().get(0).getName());
		html.append("' alt='");
		html.append(equipment.getEquipmentType().getNiceName());
		html.append("' data-qtiptitle='");
		html.append(equipment.getEquipmentType().getNiceName());
		html.append("' src='");
		html.append(request.getContextPath());
		html.append("/images/1x1.png'/>");
		html.append("</span>");
		html.append("</span>");
		
		return html;
	}

	/**
	 * Generate the equipment level text (including the modified level if it has reduce level attribute)
	 * 
	 * @return
	 */
	private StringBuilder addEquipmentLevel(boolean popUp) {
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		StringBuilder appendClass = new StringBuilder();
		if(popUp) appendClass.append("Popup");
		if(equipment.getRequiredLevel() != equipment.getLevel()) {
			appendClass.append("ReducedLevel");
		}
		
		StringBuilder html = new StringBuilder();
		html.append("<img src=' ");
		html.append(request.getContextPath());
		html.append("/images/levelFrame.png' alt='Level' data-qtiptitle='Level' class='levelFrameTall");
		html.append(appendClass);
		html.append("'><span class='levelNumberTall");
		html.append(appendClass);
		html.append("'>");
		
		
		// If the usable level is different, show that too
		if(equipment.getRequiredLevel() != equipment.getLevel()) {
			html.append(String.format("%02d", equipment.getRequiredLevel()));
			//html.append("<br />");
			if(equipment.getRequiredLevel() > equipment.getLevel()) {
				html.append(" - <font color='red'>");
				html.append(String.format("%02d", equipment.getRequiredLevel()-equipment.getLevel()));
				html.append("</font>");
			} else {
				html.append(" + <font color='green'>");
				html.append(String.format("%02d", equipment.getLevel()-equipment.getRequiredLevel()));
				html.append("</font>");
			}
		} else {
			html.append(String.format("%02d", equipment.getLevel()));
		}
		html.append("</span>");
		
		return html;
	}

	/**
	 * Generate the equipment detail pop up
	 * 
	 * @param isEmptyEquipment	Is the equipment empty
	 * 
	 * @return
	 */
	private StringBuilder createEquipmentDetailPopup(boolean isEmptyEquipment) {
		StringBuilder html = new StringBuilder();
		
		html.append("<script>");
		html.append("increasePendingRequests('createEquipmentDetailPopup');");
		html.append("$('#");
		if(isEmptyEquipment) {
			html.append("empty");
		}
		html.append("equipment");
		if(!adminView) {
			html.append(dragableSrcFrame);
		}
		if(isEmptyEquipment) {
			html.append(charSlotId);
		} else {
			html.append(equipment.getId());
		}
		html.append("').qtip({");
			if(isTouchScreen) {
				html.append("show: { event: 'touchend', solo: true },");
			} else {
				html.append("show: { solo: true, delay:500 },");
			}
			html.append("hide: { event: 'unfocus', distance: 100 },");
			if(compare) {
				if(equipment.getEquipmentType().equals(EquipmentType.RING)) {
					html.append("style: { width: 358 },");
				} else {
					html.append("style: { width: 240 },");
				}
			} else {
				html.append("style: { width: 120 },");
			}
			if(compare || comparable) html.append("prerender: true,");
			if(hoverAbove) {
				html.append("position: { adjust: { x: -");
				html.append(90+leftOffset);
				html.append(", y: -");
				if(compare || dropable) {
					// Need to allow for the compare equipment to be an artifact
					// or the equipment changing and thus the hover size
					html.append("263");
				} else {
					switch (equipment.getQuality()) {
					case ARTIFACT:
						// Artifacts have an extra attribute
						html.append("263");
						break;
	
					default:
						html.append(167+(12*equipment.getQuality().getId()));
						break;
					}
				}
				html.append("} },");
			} else {
				html.append("position: { adjust: { x: -");
				html.append(90+leftOffset);
				html.append(" } },");
			}
			html.append("content: {");
				html.append("text: \"");
				if(isEmptyEquipment) {
					html.append("<span style='pointer-events: all; float: left;' class='charEquipment_charslot_none");
					html.append(charSlotId);
					html.append("' id='lb_emptyequipmentcharacterDetailsFrame");
					html.append(charSlotId);
					html.append("'></span>");
				} else {
					html.append(createPopupHtml(false, characterId > 0, null));
				}
				html.append("\"");
			
			html.append("},");
			if(compare || comparable) {
				html.append("events: { render: function(event, api) {");
					html.append("decreasePendingRequests('createEquipmentDetailPopup');");
					html.append("}");
				html.append("}");
			}
		html.append("}, event);");
		if(!compare && !comparable) {
			html.append("decreasePendingRequests('createEquipmentDetailPopup');");
		}
		html.append("</script>");
		
		return html;
	}

	/**
	 * Generate the html for the equipment popup
	 * 
	 * @param isCompare 	is the compare pop up
	 * @param isOnCharacter	is the item worn by a character
	 * @param decrementPendingText text to display in debug to decrement pending, if null will not decrement pending
	 * 
	 * @return
	 */
	private StringBuilder createPopupHtml(boolean isCompare, boolean isOnCharacter, String decrementPendingText) {
		StringBuilder appendClass = new StringBuilder();
		if(equipment.getRequiredLevel() != equipment.getLevel()) {
			appendClass.append("ReducedLevel");
		}
		StringBuilder popupHtml = new StringBuilder();
		popupHtml.append("<span style='pointer-events: all; float: left; width: 118px;");
		
		if(charSlotId > 0) {
			popupHtml.append(" border: 1px solid green;' class='charEquipment_charslot_");
			popupHtml.append(charSlotId);
		}
		popupHtml.append("' id='");
		popupHtml.append("lb_");
		if(isCompare) popupHtml.append("compare");
		popupHtml.append("equipment");
		popupHtml.append(dragableSrcFrame);
		popupHtml.append(equipment.getId());
		popupHtml.append("'>");
		
		popupHtml.append("<span style='float: left;'>");

		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		popupHtml.append("<span style='float: left;'>");			
		popupHtml.append(addEquipmentLevel(true));
		popupHtml.append("<span class='equipmentQualityPopup");
		popupHtml.append(appendClass);
		popupHtml.append("'>");
		popupHtml.append(generateQualityName());
		popupHtml.append("</span>");
		popupHtml.append("<span class='equipmentTypePopup");
		popupHtml.append(appendClass);
		popupHtml.append("'>");
		popupHtml.append("<span class='charSlotDetailsSpan'>");
		popupHtml.append(equipment.getEquipmentType().getNiceName());
		popupHtml.append("</span>");
		popupHtml.append("<span class='charSlotDetailsImageSpan'>");
		popupHtml.append("<img class='charSlotIcon");
		popupHtml.append(equipment.getEquipmentType().getValidSlots().get(0).getName());
		popupHtml.append("' alt='");
		popupHtml.append(equipment.getEquipmentType().getNiceName());
		popupHtml.append("' data-qtiptitle='");
		popupHtml.append(equipment.getEquipmentType().getNiceName());
		popupHtml.append("' src='");
		popupHtml.append(request.getContextPath());
		popupHtml.append("/images/1x1.png'/>");
		popupHtml.append("</span>");
		popupHtml.append("</span>");
		popupHtml.append("<br class='clear: both;' />");
		
		popupHtml.append("<br />");
		EquipmentAttribute baseAttribute = equipment.getBaseAttribute();
		if(baseAttribute != null) {
			popupHtml.append("<span data-qtiptitle='");
			popupHtml.append(baseAttribute.getDescription());
			popupHtml.append("'>");
			popupHtml.append(StringTools.htmlColourfiy(baseAttribute.getNiceName()));
			popupHtml.append(" ");
			int baseAttributeValue = baseAttribute.calculateRealValue(equipment.getBaseAttributeValue(), equipment.getLevel());
			if(baseAttributeValue < 0) {
				popupHtml.append("<font color='red'>");
				popupHtml.append(baseAttributeValue);
				popupHtml.append("</font>");
			} else {
				popupHtml.append(baseAttributeValue);
			}
			popupHtml.append("</span>");				
			popupHtml.append("<br />");
		}
		EquipmentAttribute defenceAttribute = equipment.getDefenceAttribute();
		if(defenceAttribute != null) {
			popupHtml.append("<span data-qtiptitle='");
			popupHtml.append(defenceAttribute.getDescription());
			popupHtml.append("'>");
			popupHtml.append(StringTools.htmlColourfiy(defenceAttribute.getNiceName()));
			popupHtml.append(" ");
			int defenceAttributeValue = defenceAttribute.calculateRealValue(equipment.getDefenceAttributeValue(), equipment.getLevel());
			if(defenceAttributeValue < 0) {
				popupHtml.append("<font color='red'>");
				popupHtml.append(defenceAttributeValue);
				popupHtml.append("</font>");
			} else {
				popupHtml.append(defenceAttributeValue);
			}
			popupHtml.append("</span>");				
			popupHtml.append("<br />");
		}
		Map<Integer, Integer> attributes = equipment.getAttributes(false);
		for(Integer typeId:attributes.keySet()) {
			EquipmentAttribute equipmentAttribute = EquipmentAttribute.fromId(typeId);
			int attributeValue = equipmentAttribute.calculateRealValue(attributes.get(typeId), equipment.getLevel());
			popupHtml.append("<span data-qtiptitle='");
			popupHtml.append(equipmentAttribute.getDescription());
			popupHtml.append("'>");
			popupHtml.append(StringTools.htmlColourfiy(equipmentAttribute.getNiceName()));
			popupHtml.append(" ");
			if(attributeValue < 0) {
				popupHtml.append("<font color='red'>");
				popupHtml.append(attributeValue);
				popupHtml.append("</font>");
			} else {
				popupHtml.append(attributeValue);
			}
			popupHtml.append("</span>");				
			popupHtml.append("<br />");
		}
		EquipmentAttribute ancientAttribute = equipment.getAncientAttribute();
		if(ancientAttribute != null) {
			popupHtml.append("<span data-qtiptitle='");
			popupHtml.append(ancientAttribute.getDescription());
			popupHtml.append("'>");
			popupHtml.append(StringTools.htmlColourfiy(ancientAttribute.getNiceName()));
			popupHtml.append(" ");
			int ancientAttributeValue = ancientAttribute.calculateRealValue(equipment.getAncientAttributeValue(), equipment.getLevel());
			if(ancientAttributeValue < 0) {
				popupHtml.append("<font color='red'>");
				popupHtml.append(ancientAttributeValue);
				popupHtml.append("</font>");
			} else {
				popupHtml.append(ancientAttributeValue);
			}
			popupHtml.append("</span>");				
			popupHtml.append("<br />");
		}
		
		PowerValues powerValues = new PowerValues();
		Map<EquipmentAttribute, Integer> characterSummary = new HashMap<EquipmentAttribute, Integer>();
		CommonTools.calculatePowerValues(powerValues, equipment, characterSummary, character);
		int level = equipment.getLevel();
		if(character != null) level = character.getLevel();
		popupHtml.append("<br />");
		popupHtml.append("A:");
		
		if(isOnCharacter && !isCompare) {
			// Just combine
			colourValue(popupHtml, powerValues.attackValue + powerValues.classSpecificAttackValue);
			popupHtml.append((powerValues.attackValue + powerValues.classSpecificAttackValue) / level);
			popupHtml.append("</font>");
		} else {
			colourValue(popupHtml, powerValues.attackValue);
			popupHtml.append(powerValues.attackValue/level);
			popupHtml.append("</font>");
			if(powerValues.classSpecificAttackValue != 0) {
				if(powerValues.classSpecificAttackValue > 0) {
					popupHtml.append("+");
				} else {
					popupHtml.append("-");
				}
				colourValue(popupHtml, powerValues.classSpecificAttackValue);
				popupHtml.append(powerValues.classSpecificAttackValue/level);
				popupHtml.append("</font>");
			}
		}
		popupHtml.append(", D:");
		if(isOnCharacter && !isCompare) {
			// Just combine
			colourValue(popupHtml, powerValues.defenceValue + powerValues.classSpecificDefenceValue);
			popupHtml.append((powerValues.defenceValue + powerValues.classSpecificDefenceValue) / level);
			popupHtml.append("</font>");
		} else {
			colourValue(popupHtml, powerValues.defenceValue);
			popupHtml.append(powerValues.defenceValue/level);
			popupHtml.append("</font>");
			if(powerValues.classSpecificDefenceValue != 0) {
				if(powerValues.classSpecificDefenceValue > 0) {
					popupHtml.append("+");
				} else {
					popupHtml.append("-");
				}
				colourValue(popupHtml, powerValues.classSpecificDefenceValue);
				popupHtml.append(powerValues.classSpecificDefenceValue/level);
				popupHtml.append("</font>");
			}
		}
		popupHtml.append(", R:");
		if(powerValues.recoveryValue >= 0) {
			popupHtml.append("<font style='color: green'>");
		} else {
			popupHtml.append("<font style='color: red'>");
		}
		popupHtml.append(powerValues.recoveryValue/level);
		popupHtml.append("</font>");
		popupHtml.append("<br />");
		
		popupHtml.append("Salvage Value:");
		popupHtml.append(equipment.getSalvageValue());
		popupHtml.append("<br />");
		popupHtml.append("</span>");

		// Add recommended classes based on highest primary and defensive attributes
		List<CharClass> recommendedClasses = equipment.getRecommendedClasses();
		if(!recommendedClasses.isEmpty()) {
			popupHtml.append("<br style='clear:both' />");
			popupHtml.append("<span style='float: left; position: relative; margin-top: -10px; width: 100px;'>");
			int count = -3;
			for(CharClass charClass:recommendedClasses) {
				if(count % 3 == 0) {
					popupHtml.append("<br />");
				}

				popupHtml.append("<span class='classIconSpan classIconRecommendedSpan'>");
				popupHtml.append("<img class='classIcon");
				popupHtml.append(charClass.getName());
				popupHtml.append("25' alt='");
				popupHtml.append(charClass.getNiceName());
				popupHtml.append("' src='");
				popupHtml.append(request.getContextPath());
				popupHtml.append("/images/1x1.png'/>");
				popupHtml.append("</span>");
				count++;
			}
			popupHtml.append("</span>");
		}
		popupHtml.append("</span>");
		popupHtml.append("</span>");
		
		if(compare) {
			List<CharSlot> charSlots = equipment.getEquipmentType().getValidSlots();
			for(CharSlot charSlot:charSlots) {
				popupHtml.append("<span class='charslot_");
				popupHtml.append(charSlot.getId());
				popupHtml.append("' id='lb_charslot_");
				popupHtml.append(charSlot.getId());
				if(isCompare) popupHtml.append("compare");
				popupHtml.append("equipment");
				popupHtml.append(dragableSrcFrame);
				popupHtml.append(equipment.getId());
				popupHtml.append("' style='float: left;'></span>");
			}
		}
		if(decrementPendingText != null) {
			popupHtml.append("\\<script\\>");
			popupHtml.append("decreasePendingRequests(pendingRequests'");
			popupHtml.append(decrementPendingText);
			popupHtml.append("');");
			popupHtml.append("\\<\\/script\\>");			
		}
		return popupHtml;
	}

	protected void colourValue(StringBuilder popupHtml, int value) {
		if(value >= 0) {
			popupHtml.append("<font style='color: green'>");
		} else {
			popupHtml.append("<font style='color: red'>");
		}
	}

	/**
	 * Create the compare popup next to the standard pop up on all the matching compare equipment
	 * 
	 * @return
	 */
	private StringBuilder createComparableScript() {
		StringBuilder html = new StringBuilder();
		if(charSlotId <= 0) return html;
		
		html.append("<script>");
		html.append("$(function() {");
		
		html.append("increasePendingRequests('createComparableScript(");
		html.append(equipment.getId());
		html.append(")');");
		
		html.append("$('.charslot_");
		html.append(charSlotId);
		html.append("').html(\"");
		html.append(createPopupHtml(true, characterId > 0, null));
		html.append("\");");
		html.append("});");
		
		html.append("decreasePendingRequests('createComparableScript(");
		html.append(equipment.getId());
		html.append(")');");

		html.append("</script>");
		
		return html;
	}

	private StringBuilder emptyCompareCharSlot(int charSlotId) {
		CharSlot charSlot = CharSlot.fromId(charSlotId);
		StringBuilder html = new StringBuilder();
		
		html.append("<script>");
		html.append("$(function() {");
		html.append("increasePendingRequests('emptyCompareCharSlot');");
		html.append("$('.charslot_");
		html.append(charSlot.getId());
		html.append("').html(\"\");");
		html.append("});");
		html.append("decreasePendingRequests('emptyCompareCharSlot');");
		html.append("</script>");
		return html;
	}

	/**
	 * Generate the Quality Name html in the correct colour
	 * 
	 * @param doubleEscape
	 * @return
	 */
	private StringBuilder generateQualityName() {
		StringBuilder html = new StringBuilder();
		EquipmentQuality quality = equipment.getQuality();
		html.append("<font color='");		
		html.append(quality.getHtmlColour());		
		html.append("'>");
		html.append(quality.getNiceName());
		html.append("</font>");
		
		return html;
	}
	
	/**
	 * Generate the 'ondrop' and 'ondragover' attributes
	 * 
	 * @return	String of attributes
	 */
	private StringBuilder generateDropableHtml() {
		StringBuilder html = new StringBuilder();
		PageContext pageContext = (PageContext) getJspContext();  
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest(); 
		if(stashId >= 0) {
			html.append("ondrop=\"dropItemStash(event, '");
			html.append(request.getContextPath());
			html.append("/api/account/");
			html.append(accountApiKey);
			html.append("/inventory/");
			html.append(stashId);
			html.append("', '");
			html.append(compare || comparable?"true":"false");
			html.append("'");
		} else if(charSlotId >= 0) {
			html.append("ondrop=\"dropEquipmentCharDetails(event, '");
			html.append(request.getContextPath());
			html.append("/play/character/");
			html.append(characterId);
			html.append("', '");
			html.append(request.getContextPath());
			html.append("/api/account/");
			html.append(accountApiKey);
			html.append("/character/");
			html.append(characterId);
			html.append("/equipment/");
			html.append("', ");
			html.append(charSlotId);
			html.append(", '");
			html.append(compare || comparable?"true":"false");
			html.append("'");
		}
		html.append(")\" ondragover=\"allowDrop(event)\" ");
		return html;
	}
}

package org.kerrin.dungeon.controller.customtags;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.CharacterEquipment;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.Equipment;

public class CharacterDisplay extends SimpleTagSupport {
	
	private Character character;
	
	private boolean adminView = false;
	private boolean localMode = false;
	private String linkDestFrame = null;
	private String linkDestUrl = null;
	private boolean dragable = false;
	private String dragableSrcFrame = null;
	private String dragableSrcUrl = null;
	private boolean dropable = false;
	private boolean isTouchScreen = false;
	private boolean filterable = false;
	private CharacterEquipment characterEquipment = null;
	
	public CharacterDisplay() {}

	public void setCharacter(Character character) {
		this.character = character;
	}
	
	public void setAdminView(boolean adminView) {
		this.adminView = adminView;
	}
	
	public void setLocalMode(boolean localMode) {
		this.localMode = localMode;
	}
	
	public void setLinkDestFrame(String linkDestFrame) {
		this.linkDestFrame = linkDestFrame;
	}
	
	public void setLinkDestUrl(String linkDestUrl) {
		this.linkDestUrl = linkDestUrl;
	}
	
	public void setDragable(boolean dragable) {
		this.dragable = dragable;
	}
	
	public void setDragableSrcFrame(String dragableSrcFrame) {
		this.dragableSrcFrame = dragableSrcFrame;
	}
	
	public void setDragableSrcUrl(String dragableSrcUrl) {
		this.dragableSrcUrl = dragableSrcUrl;
	}
	
	public void setDropable(boolean dropable) {
		this.dropable = dropable;
	}
	
	public void setIsTouchScreen(boolean isTouchScreen) {
		this.isTouchScreen = isTouchScreen;
	}
	
	public void setIsFilterable(boolean filterable) {
		this.filterable = filterable;
	}
	
	public void setCharacterEquipment(CharacterEquipment characterEquipment) {
		this.characterEquipment = characterEquipment;
	}

	@Override
    public void doTag() throws JspException, IOException {
		if(dragable && (dragableSrcFrame == null || dragableSrcUrl == null)) {
			throw new JspException("Dragable needs frame and url");
		}
		StringBuilder html = new StringBuilder();
		if(character == null || character.getId() <= 0) {
			html.append("None");
		} else {
			PageContext pageContext = (PageContext) getJspContext();
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest(); 
			if(character.isCurrentlyDead()) {
				dragable = false;
			}
			
			if(adminView) {
				html.append("<a href=\"");
				html.append(request.getContextPath());
				html.append("/admin/character/");
				html.append(character.getId());
				html.append("\">");
				html.append(character.getId());
				html.append("</a>");
				html.append("<br />");
			}
			
			html.append("<span id=\"character");
			html.append(dragableSrcFrame);
			html.append(character.getId());
			html.append("\" class=\"link ");
			if(character.isCurrentlyDead()) html.append("deadBG ");
			if(filterable) {
				html.append("CharClass");
				html.append(character.getCharClass().getName());
				html.append(" ");
			}
			setDragAndDropable(html, request);
			html.append(">");
			html.append("<span class='classIconSpan'>");
				html.append("<img class='classIcon");
				html.append(character.getCharClass().getName());
				html.append("75' alt='");
				html.append(character.getCharClass().getNiceName());
				html.append("' src='");
				html.append(request.getContextPath());
				html.append("/images/1x1.png'/>");
			html.append("</span>");

			html.append("<span class='levelXp'>");
				html.append("<img src='");
				html.append(request.getContextPath());
				html.append("/images/levelFrame.png' alt='' data-qtiptitle='Level' class='levelFrame' />");
				html.append("<span class='levelNumber'>");
				html.append(String.format("%02d", character.getLevel()));
			html.append("</span>");
			
			html.append("<span class='xp'>");
			html.append("<img src='");
				html.append(request.getContextPath());
				html.append("/images/1x1.png' alt='' data-qtiptitle='Level' class='xpBar");
				html.append(character.getXpClass());				
				html.append("' />");
			html.append("</span>");

			html.append("<span class='characterSummary'>");
			if(character.isCurrentlyDead()) html.append("<font style='text-decoration: line-through;'>");
			html.append(character.getName());
			if(character.isCurrentlyDead()) html.append("</font>"); 
			html.append("</span>");
		
			html.append("<span class='isDead'>");
			if(character.isCurrentlyDead()) {
				html.append("Died to ");
				html.append(character.getDiedTo());
			}
			html.append("</span>");
			
			Dungeon dungeon = character.getDungeon();
			if(dungeon != null) {
				html.append("<span class='inDungeon'>");
				html.append("In Dungeon");
				html.append("</span>");
			}
			html.append("</span>");
			
			html.append("</span>");
			
			createNeedEquipmentSpan(html, request);
			
			if(linkDestFrame != null && linkDestUrl != null) {
				html.append("<script>");
				if(isTouchScreen) {
					// Touch End
					html.append("$('#character");
					html.append(dragableSrcFrame);
					html.append(character.getId());
					html.append("').bind( 'touchend', function(e){");
					html.append("loadSpan('");
					html.append(linkDestFrame);
					html.append("','");
					html.append(linkDestUrl);
					html.append("');});");
				} else {
					// Mouse Up
					html.append("$('#character");
					html.append(dragableSrcFrame);
					html.append(character.getId());
					html.append("').mouseup(function() { loadSpan('");
					html.append(linkDestFrame);
					html.append("','");
					html.append(linkDestUrl);
					html.append("');});");
				}
				
				html.append("</script>");
			}
			
			// Create the equipment detail pop up
			html.append(createCharacterDetailPopup());
		}
		getJspContext().getOut().write(html.toString());
	}

	protected void setDragAndDropable(StringBuilder html, HttpServletRequest request) {
		setDragAndDropable(html, request, false);
	}
	
	protected void setDragAndDropable(StringBuilder html, HttpServletRequest request, boolean noClass) {
		if(dragable || dropable) {
			if(!noClass) {
				if(dropable) {
					html.append("charDrop\" ");
				} else {
					html.append("charDrag\" ");
				}
			}
			if(dragable) {
				html.append("draggable=\"true\" ");
				html.append("ondragstart=\"dragCharacter(event, ");
				html.append(character.getId());
				html.append(",'charactercharactersFrame");
				html.append(character.getId());
				html.append("','");
				html.append(dragableSrcFrame);
				html.append("','");
				html.append(dragableSrcUrl);
				html.append("','");
				html.append(request.getContextPath());
				html.append("')\" ");
			} else {
				html.append("draggable=\"false\" ");					
			}
			if(dropable) {
				html.append("ondrop=\"dropCharacter(event)\" ondragover=\"allowDrop(event)\" ");
			}
		} else {
			if(!noClass) {
				html.append("charLarge\" ");
			}
			html.append("draggable=\"false\" ");
		}
	}
	
	private void createNeedEquipmentSpan(StringBuilder html, HttpServletRequest request) {
		if(characterEquipment != null) {
			html.append("<span id='characterNeedEquipment");
			html.append(character.getId());
			html.append("' class='characterNeedEquipment' ");
			setDragAndDropable(html, request, true);
			html.append(">");
			Map<CharSlot, Equipment> equipmentInSlots = characterEquipment.getCharacterSlots();			
			for(int row=0; row < 5; row++) {
				for(int column=0; column < 3; column++) {
					CharSlot slot = CharSlot.fromLocation(row, column);
					html.append("<span class='needEquipmentSlot'>");
					if(slot.equals(CharSlot.UNKNOWN)) {
						// Not a equipment slot
						html.append("&nbsp;");
					} else {
						Equipment equipment = equipmentInSlots.get(slot);
						int characterLevel = character.getLevel();
						if(equipment == null) {
							// No equipment in slot
							html.append("<img class='charSlotIconNeedMissing");
							html.append(slot.getName());
							if(localMode) {
								html.append(" localModeNeedMissing");
							}
							html.append("'/>");
						} else if(equipment.getLevel() < (characterLevel - ((characterLevel/10)+3))) {
							// Low level equipment in slot
							html.append("<img class='charSlotIconNeedLow");
							html.append(slot.getName());
							html.append("'/>");
						} else {
							// Good equipment in slot
							html.append("&nbsp;");
						}
					}
					html.append("</span>");
				}
				html.append("<br style=\"clear: both\" />");
			}
			html.append("</span>");
		}
	}

	private StringBuilder createCharacterDetailPopup() {
		StringBuilder html = new StringBuilder();
		
		html.append("<script>");
		html.append("$('#");
		html.append("character");
		html.append(dragableSrcFrame);
		html.append(character.getId());
		html.append("').qtip({");
			if(isTouchScreen) {
				html.append("show: { event: 'touchend', solo: true },");
			} else {
				html.append("show: { solo: true, delay:1000 },");
			}
			//html.append("prerender: true,");
			html.append("hide: { event: 'unfocus', distance: 100 },");
			html.append("position: { adjust: { x: -100 } },");
			html.append("content: {");
				html.append("text: \"");
				html.append(createPopupHtml());
				html.append("\"");
			
			html.append("}");
		html.append("}, event);");

		html.append("</script>");
		
		return html;
	}
	
	private StringBuilder createPopupHtml() {
		StringBuilder popupHtml = new StringBuilder();
		popupHtml.append("<span style='pointer-events: all; float: left;' id='");
		popupHtml.append("lb_");
		popupHtml.append("character");
		popupHtml.append(dragableSrcFrame);
		popupHtml.append(character.getId());
		popupHtml.append("'>");
		
		popupHtml.append("<span style='float: left;'>");			
		popupHtml.append(character.getName());
		popupHtml.append("<br />Level ");
		popupHtml.append(character.getLevel());
		popupHtml.append(" ");
		popupHtml.append(character.getCharClass().getNiceName());
		popupHtml.append("<br />XP ");
		popupHtml.append(character.getXp());
		popupHtml.append(" of ");
		popupHtml.append(character.getXpNextLevel());
		if(character.isCurrentlyDead()) {
			popupHtml.append("<br />Died to ");
			popupHtml.append(character.getDiedTo());
		}
		if(character.getDungeon() != null) {
			popupHtml.append("<br />In Dungeon");
		}
		if(!character.isUsedLevelUp()) {
			popupHtml.append("<br />Only dungeon leveled");
		}
		popupHtml.append("</span>");
		popupHtml.append("</span>");
		
		return popupHtml;
	}
}

package org.kerrin.dungeon.controller.customtags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.model.DungeonEvent;
import org.kerrin.dungeon.utils.StringTools;

/**
 * This will create the dungeon action log pop up
 * It will allow the player to step through the action, they can drag a slider bar to skip ahead in the action
 * 
 * @author Kerrin
 *
 */
public class DungeonEventsDisplay extends SimpleTagSupport {
	
	private Dungeon dungeon;
	
	private List<DungeonEvent> dungeonEvents;
	
	private String parentSpan;
	
	public DungeonEventsDisplay() {}

	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}
	
	public void setDungeonEvents(List<DungeonEvent> dungeonEvents) {
		this.dungeonEvents = dungeonEvents;
	}
	
	public void setParentSpan(String parentSpan) {
		this.parentSpan = parentSpan;
	}

	@Override
    public void doTag() throws JspException, IOException {
		StringBuilder html = new StringBuilder();
		StringBuilder popupHtml = new StringBuilder();
		
		List<String> characterNames = new ArrayList<String>();
		for(Character character:dungeon.getCharacters()) {
			characterNames.add(character.getName());
		}
		
		popupHtml.append("<span id='lb_logs' class='lightbox dungeonLogs'>");
		popupHtml.append("<div style='float: left;'>");
		
		// TODO: Add stepping through actions, with nice controls and images
		for(DungeonEvent event: dungeonEvents) {
			popupHtml.append(asHtml(event, dungeon.getPartySize(), characterNames));
		};
		

		popupHtml.append("</div>");
		popupHtml.append("<div style='float: right;'>");
		popupHtml.append("<a href=\\\"javascript: hideDivById('lb_logs');\\\" style='text-decoration: none; font-weight: bold; color: red;'>X</a>");
		popupHtml.append("</div>");
		popupHtml.append("</span>");
		
		html.append("<script>$(\"#outerSpan_");
		html.append(parentSpan);
		html.append("\").append(\"");
		html.append(popupHtml);
		html.append("\");</script>");
		
		getJspContext().getOut().write(html.toString());
	}

	private String asHtml(DungeonEvent event, int partySize, List<String> characterNames) {
		StringBuilder htmlSb = new StringBuilder();
		htmlSb.append(StringTools.htmlColourfiy(event.getDescription(), characterNames));
		htmlSb.append("<br />");
		String effectDetails = event.getAffectedMobsDetails(partySize);
		if(!effectDetails.isEmpty()) {
			htmlSb.append("&nbsp;*&nbsp;");
			htmlSb.append(StringTools.htmlColourfiy(effectDetails, characterNames));
			htmlSb.append("<br />");
		}
		
		return htmlSb.toString();
	}
}

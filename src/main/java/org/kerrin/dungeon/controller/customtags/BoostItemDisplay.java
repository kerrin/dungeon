package org.kerrin.dungeon.controller.customtags;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.kerrin.dungeon.model.BoostItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoostItemDisplay extends SimpleTagSupport {
	private static final Logger logger = LoggerFactory.getLogger(BoostItemDisplay.class);
	
	private BoostItem item;
	
	private boolean adminView = false;
	/** Item hover appears above Item, instead of below */
	private boolean dragable = false;
	private String dragableSrcFrame = null;
	private boolean dropable = false;
	private int stashId = -1;
	private String accountApiKey = null;
	private String viewForm = "";
	
	public BoostItemDisplay() {}
	
	public void setItem(BoostItem item) {
		this.item = item;
	}
	
	public void setAdminView(boolean adminView) {
		this.adminView = adminView;
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

	public void setAccountApiKey(String accountApiKey) {
		this.accountApiKey = accountApiKey;
	}

	public void setViewForm(String viewForm) {
		this.viewForm = viewForm;
	}

	@Override
    public void doTag() throws JspException, IOException {
		StringBuilder html = new StringBuilder();

		if(dragable && dragableSrcFrame == null) {
			throw new JspException("Dragable needs frame");
		}
		if(dropable) {
			if(accountApiKey == null || accountApiKey.isEmpty()) {
				throw new JspException("Dropable needs account api key");
			}
			if(stashId < 0) {
				throw new JspException("Dropable needs a stash id");
			}
		}
		if(item == null || item.getId() <= 0) {
			html.append("<span id='emptyItem");
			html.append(dragableSrcFrame);
			if(stashId > 0) {
				html.append(stashId);
			} else {
				html.append("Unknown");
			}
			html.append("' class='");
			if(dropable) {	
				html.append("itemDrop emptyBoostItem' ");
				html.append("draggable='false' ");
				html.append(generateDropableHtml());
			} else {
				html.append("itemLarge' ");
				html.append("draggable='false' ");
			}
			html.append(">");
			html.append("");
			html.append("</span>");
		} else {
			PageContext pageContext = (PageContext) getJspContext();
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest(); 
			
			if(adminView) {
				html.append("<a href='");
				html.append(request.getContextPath());
				html.append("/admin/boostItem/");
				html.append(item.getId());
				html.append("'>");
				html.append(item.getId());
				html.append("</a>");
				html.append("<br />");
			}
			// Pop Up link
			html.append("<span id='boostItem");
			if(!adminView) {
				html.append(dragableSrcFrame);
			}
			html.append(item.getId());
			html.append("' class='");
			if(adminView) {
				html.append("adminBoostItem ");
			}
			
			if(dragable || dropable) {				
				html.append(addDragableAndDropable(request, true));
			} else {
				html.append("boostItemLarge BoostItem' ");
				html.append("draggable='false' ");
			}
			html.append(">");
			html.append(addItemSummary(request));
			html.append("</span>");
			
			html.append("<input name='equipment' type='hidden' value='false'>");
			html.append("<input name='itemId' type='hidden' value='");
			html.append(item.getId());
			html.append("'>");
		}
		
		getJspContext().getOut().write(html.toString());
	}

	/**
	 * Id the Item span is draggable and/or dropable, generate the appropriate attributes and class name
	 * 
	 * @param request
	 * 
	 * @return
	 */
	private StringBuilder addDragableAndDropable(HttpServletRequest request, boolean hasItem) {
		StringBuilder html = new StringBuilder();
		if(dropable) {
			html.append("boostItemDrop");
		} else {
			html.append("boostItemDrag");
		}
		if(hasItem) {
			html.append(" boostItem");
		} else {
			html.append(" boostItemEmpty");
		}
		html.append("' ");
		if(dragable) {
			html.append("draggable='true' ");
			html.append("ondragstart=\"dragBoostItem(event, ");
			html.append(item.getId());
			html.append(",");
			html.append(item.getSalvageValue());
			html.append(",'");
			html.append("boostItem");
			html.append(dragableSrcFrame);
			html.append(item.getId());
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
	 * Generate the Item summary that is always displayed (level, name and quality)
	 * 
	 * @return
	 */
	private StringBuilder addItemSummary(HttpServletRequest request) {
		StringBuilder html = new StringBuilder();
		StringBuilder appendClass = new StringBuilder();

		html.append("<span class='boostItem' title='");
		html.append(item.getBoostItemType().getDescription(item.getLevel()));
		html.append("'>");
		html.append(addItemLevel(false));
		html.append(addActionSpan(false));
		html.append("<span class='boostItemType");
		html.append(appendClass);
		html.append("'>");
		html.append("<img class='boostItemIcon");
		html.append(item.getBoostItemType().getName());
		html.append("' alt='");
		html.append(item.getBoostItemType().getNiceName());
		html.append("' data-qtiptitle='");
		html.append(item.getBoostItemType().getNiceName());
		html.append("' src='");
		html.append(request.getContextPath());
		html.append("/images/1x1.png'/>");
		html.append("</span>");
		html.append("</span>");
		
		return html;
	}

	/**
	 * Generate the Boost Item level text
	 * 
	 * @return
	 */
	private StringBuilder addItemLevel(boolean popUp) {
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		StringBuilder appendClass = new StringBuilder();
		if(popUp) appendClass.append("Popup");
		
		StringBuilder html = new StringBuilder();
		html.append("<span class='boostItemLevel'>");
		html.append("<img src='");
		html.append(request.getContextPath());
		html.append("/images/levelFrame.png' alt='Level' class='boostItemLevelFrame");
		html.append(appendClass);
		html.append("'><span class='boostItemLevelNumber");
		html.append(appendClass);
		html.append("'>");
		
		html.append(String.format("%02d", item.getLevel()));
		html.append("</span>");
		html.append("</span>");
		
		return html;
	}
	
	/**
	 * Generate the Action
	 * 
	 * @return
	 */
	private StringBuilder addActionSpan(boolean popUp) {
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		StringBuilder appendClass = new StringBuilder();
		if(popUp) appendClass.append("Popup");
		
		StringBuilder html = new StringBuilder();
		html.append("<span class='boostItemRedeem");
		html.append(appendClass);
		html.append("' title='Redeem Boost Item'><a href='");
		html.append(request.getContextPath());
		html.append(getActionUrl());
		html.append("' onclick=\"location.href=this.href+'&characterId='+charId+'&dungeonId='+dungeonId\">Use</a></span>");
				
		return html;
	}

	private String getActionUrl() {
		return "/play/boostitem/"+item.getId()+"/redeem?"+viewForm;
	}

	protected void colourValue(StringBuilder popupHtml, int value) {
		if(value >= 0) {
			popupHtml.append("<font style='color: green'>");
		} else {
			popupHtml.append("<font style='color: red'>");
		}
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
			html.append("', 'false')");
		}
		html.append(")\" ondragover=\"allowDrop(event)\" ");
		return html;
	}
}

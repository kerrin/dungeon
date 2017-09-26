package org.kerrin.dungeon.controller.customtags;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.kerrin.dungeon.utils.Facebook;

public class FBLogin extends SimpleTagSupport {
	public FBLogin() {}

	private Facebook facebook;
	
	private String buttonText = "Log In";
	
	private long characterId = -1;
	
	private long dungeonId = 0;
	
	public void setFacebook(Facebook facebook) {
		this.facebook = facebook;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public void setCharacterId(long characterId) {
		this.characterId = characterId;
	}

	public void setDungeonId(long dungeonId) {
		this.dungeonId = dungeonId;
	}

	@Override
    public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		StringBuilder returnUrl = new StringBuilder();
		returnUrl.append(facebook.getHostUrl());
		returnUrl.append(request.getContextPath());
		returnUrl.append("/login/facebook?charId=");
		returnUrl.append(characterId);
		returnUrl.append("&dungeonId=");
		returnUrl.append(dungeonId);
		String urlEncodedReturnUrl = URLEncoder.encode(returnUrl.toString(), "UTF-8");

		StringBuilder html = new StringBuilder();
		html.append("<a href='");
		html.append("https://www.facebook.com/dialog/oauth?client_id=");
		html.append(facebook.getApiId());
		html.append("&redirect_uri=");
		html.append(urlEncodedReturnUrl);
		html.append("&scope=public_profile,email'>");
		html.append("<div id='u_0_0' class='fbloginouterdiv'>");
		html.append("<div class='fbloginbutton' role='button' tabindex='0' id='u_0_1'>");
		html.append("<table class='fblogintable' cellspacing='0' cellpadding='0'>");
		html.append("<tbody>");
		html.append("<tr class='fblogintr'>");
		html.append("<td class='fblogintd'>");
		html.append("<div class='fblogodiv'>");
		html.append("<span class='fbloginspan'>");
		html.append("<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 216 216' class='fblogosvg'>");
		html.append("<path fill='white' d='");
		html.append("M204.1 0H11.9C5.3 0 0 5.3 0 11.9v192.2c0 6.6 5.3 11.9 11.9");
		html.append("11.9h103.5v-83.6H87.2V99.8h28.1v-24c0-27.9 17-43.1 41.9-43.1");
		html.append("11.9 0 22.2.9 25.2 1.3v29.2h-17.3c-13.5 0-16.2 6.4-16.2");
		html.append("15.9v20.8h32.3l-4.2 32.6h-28V216h55c6.6 0 11.9-5.3");
		html.append("11.9-11.9V11.9C216 5.3 210.7 0 204.1 0z'></path>");
		html.append("</svg>");
		html.append("</span>");
		html.append("</div>");
		html.append("</td>");
		html.append("<td class='fblogintd'><span class='fblogintext'>");
		html.append(buttonText);
		html.append("</span></td>");
		html.append("</tr>");
		html.append("</tbody>");
		html.append("</table>");
		html.append("</div>");
		html.append("</div>");
		html.append("</a>");
		getJspContext().getOut().write(html.toString());
	}
}

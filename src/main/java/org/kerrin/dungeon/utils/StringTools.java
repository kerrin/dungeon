package org.kerrin.dungeon.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kerrin.dungeon.enums.DamageType;
import org.kerrin.dungeon.enums.Monster;
import org.kerrin.dungeon.enums.Spell;

public class StringTools {

	private static final String CHARACTER_HTML_COLOUR = "#499049"; // green
	private static final String MONSTER_HTML_COLOUR = "#604020"; // brown
	private static final String SPELL_HTML_COLOUR = "#8A0868";
	private static final Map<String,String> keywords;
	private static final int RANDOM_PASSWORD_LENGTH = 12;
	static {
		keywords = new HashMap<String,String>();
		keywords.put("Dungeon Failed", "#3C191D");
		keywords.put("died.", "#3C191D");
		keywords.put("mana", "yellow");
		keywords.put("health", "red");
	};

	public static String htmlColourfiy(String text) {
		return htmlColourfiy(text, null);
	}
	
	public static String htmlColourfiy(String text, List<String> characterNames) {
		for(Monster monster:Monster.values()) {
			text = text.replaceAll("("+monster.getNiceName()+")", "<font style='color: "+MONSTER_HTML_COLOUR+";'>$1</font>");
		}

		for(Spell spell:Spell.values()) {
			text = text.replaceAll("("+spell.getNiceName()+")", "<font style='color: "+SPELL_HTML_COLOUR+";'>$1</font>");
		}
		
		for(DamageType type:DamageType.values()) {
			text = text.replaceAll("("+type.getNiceName()+" Damage)", "<font style='color: "+type.getHtmlColour()+";'>$1</font>");
			text = text.replaceAll("("+type.getNiceName()+" Resist)", "<font style='color: "+type.getHtmlColour()+";'>$1</font>");
		}
		
		for(String keyword:keywords.keySet()) {
			text = text.replaceAll("("+keyword+")", "<font style='color: "+keywords.get(keyword)+";'>$1</font>");
		}

		if(characterNames != null) {
			for(String characterName:characterNames) {
				text = text.replaceAll("("+characterName+")", "<font style='color: "+CHARACTER_HTML_COLOUR+";'>$1</font>");
			}
		}
		
		return text;
	}

	public static String generateRandomPassword() {
		String AB = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrtuxy";
		StringBuilder sb = new StringBuilder( RANDOM_PASSWORD_LENGTH );
		for( int i = 0; i < RANDOM_PASSWORD_LENGTH; i++ ) {
			sb.append( AB.charAt( (int)Math.random() * AB.length()) );
		}
		return sb.toString();
	}

	/**
	 * Replace all non-alphamumeric or spaces with underscore
	 * 
	 * @param text	Text to replace
	 * @return	Cleaned text
	 */
	public static String dbTidy(String text) {
		return text.replaceAll("[^\\p{Alnum}\\p{Blank}\\.]", "_");
	}
	
	/**
	 * Checks the email address only contains valid characters
	 * and is vaguely valid e.g.:
	 * stuff@dom.ain
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isValidEmail(String email) {
		int firstAt = email.indexOf("@");
		// Only one @ allowed
		if(email.indexOf("@", firstAt+1) > firstAt) return false;
		// Check for at least one dot, as all domains need 1
		if(email.indexOf(".", firstAt) < firstAt) return false;
		return email.equals(
				email.replaceAll(
						"[^\\p{Alnum}\\p{Blank}\\.\\@\\-\\!\\#\\$\\%\\&\\'\\*\\+\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]", "")
				);
	}
}
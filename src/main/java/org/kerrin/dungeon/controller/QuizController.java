package org.kerrin.dungeon.controller;

import java.security.Principal;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/quiz25")
public class QuizController {
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String start(Locale locale, Model model, Principal principle) {
		
		return "quiz/index";
	}
	
	@RequestMapping(value = "/step2", method = RequestMethod.GET)
	public String step2(Locale locale, Model model) {
		
		return "quiz/step2";
	}
	
	@RequestMapping(value = "/step2easy", method = RequestMethod.GET)
	public String step2Easy(Locale locale, Model model) {
		
		return "quiz/step2easy";
	}
	
	@RequestMapping(value = "/step2easiest", method = RequestMethod.GET)
	public String step2Easiest(Locale locale, Model model) {
		
		return "quiz/step2easiest";
	}
		
	@RequestMapping(value = "/result", method = RequestMethod.GET)
	public String step3(Locale locale, Model model) {
		
		return "quiz/result";
	}
}

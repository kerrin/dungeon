package org.kerrin.dungeon.forms.validator;

import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.forms.CharacterForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for the CharacterForm
 * 
 * @author Kerrin
 *
 */
@Component
public class CharacterFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return CharacterForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CharacterForm characterForm = (CharacterForm) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.characterForm.name");
		
		if(characterForm.getAccountId() < 1) {
			errors.rejectValue("accountId", "Min.characterForm.accountId");
		}
		if(characterForm.getLevel() < 1) {
			errors.rejectValue("level", "Min.characterForm.level");
		}
		if(characterForm.getXp() < 0) {
			errors.rejectValue("xp", "Min.characterForm.xp");
		}

		if(characterForm.getCharClass().equals(CharClass.ANY)) {
			errors.rejectValue("charClass", "Valid.characterForm.charClass");
		}
	}
	
}

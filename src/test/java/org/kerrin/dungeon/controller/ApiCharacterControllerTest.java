package org.kerrin.dungeon.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kerrin.dungeon.controller.api.ApiCharacterController;
import org.kerrin.dungeon.enums.CharSlot;
import org.kerrin.dungeon.enums.ModificationType;
import org.kerrin.dungeon.forms.validator.CharacterFormValidator;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.CharacterEquipment;
import org.kerrin.dungeon.model.Equipment;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class ApiCharacterControllerTest extends MockMvcTest {
	@Autowired
	private CharacterFormValidator characterFormValidator;
	
	@Before
    public void setUp() {
		super.setUp();
        mockMvc = MockMvcBuilders.standaloneSetup(new ApiCharacterController(accountServiceMock, 
        		characterServiceMock, characterEquipmentServiceMock,  
        		accountCurrencyServiceMock, characterFormValidator, equipmentServiceMock, 
        		achievementServiceMock, accountMessageServiceMock, hiscoreServiceMock))
                .setHandlerExceptionResolvers(exceptionResolver())
                .setValidator(validator())
                .setViewResolvers(viewResolver())
                .build();
    }	
	
	@Test
	public void testGetCharacter() throws Exception {
		mockMvc.perform(get("/api/account/"+testAccount.getApiKey()+"/character/"+CHAR_ID))
					.andExpect(status().isOk())
			        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
			        .andExpect(jsonPath("$.id", is((int)CHAR_ID)))
			        .andExpect(jsonPath("$.account.id", is((int)ACCOUNT_ID)))
			        .andExpect(jsonPath("$.name", is(CHAR_NAME)))
			        .andExpect(jsonPath("$.charClass", is(CHAR_CLASS.name())))
			        .andExpect(jsonPath("$.level", is(CHAR_LEVEL)))
	        		.andExpect(jsonPath("$.currentlyDead", is(false)));

		verify(accountServiceMock, times(1)).findByApiKey(eq(testAccount.getApiKey()));
		verify(characterServiceMock, times(1)).findById(eq(CHAR_ID));
		restrictMockAccess();
	}
	@Test
	public void testCreateCharacter() throws Exception {
		mockMvc.perform(post("/api/account/"+testAccount.getApiKey()+"/character")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", ""+testCharacter.getId())
				.param("name", testCharacter.getName())
            )
			.andExpect(status().isOk())
	        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
	        .andExpect(jsonPath("$.id", is((int)CHAR_ID2)))
	        .andExpect(jsonPath("$.account.id", is((int)ACCOUNT_ID)))
	        .andExpect(jsonPath("$.name", is(CHAR_NAME2)))
	        .andExpect(jsonPath("$.charClass", is(CHAR_CLASS2.name())))
	        .andExpect(jsonPath("$.level", is(LEVEL2)))
    		.andExpect(jsonPath("$.currentlyDead", is(true)));

		verify(accountServiceMock, times(1)).findByApiKey(eq(testAccount.getApiKey()));
		verify(characterServiceMock, times(1)).create(any(Character.class));
		verify(characterServiceMock, times(1)).findAllByAccountOrderByLevel(eq(testAccount), anyBoolean(), anyBoolean());
		verify(accountCurrencyServiceMock, times(1)).adjustCurrency(eq(testAccount), anyBoolean(), anyBoolean(), anyLong(), 
				any(ModificationType.class), anyString());
		verify(characterEquipmentServiceMock, times(1)).create(any(CharacterEquipment.class));
		Map<CharSlot, Equipment> emptyCharacterSlots = new HashMap<CharSlot, Equipment>();
		verify(equipmentServiceMock, times(1)).generateStarterEquipment(any(Character.class), eq(emptyCharacterSlots));
		restrictMockAccess();
	}
	
	@Test
	public void testUpdateCharacter() throws Exception {
		testCharacter.setName(CHAR_NAME2);
		mockMvc.perform(post("/api/account/"+testAccount.getApiKey()+"/character/"+CHAR_ID)
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.param("id", ""+testCharacter.getId())
					.param("name", testCharacter.getName())
                )
				.andExpect(status().isOk())
		        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
		        .andExpect(jsonPath("$.id", is((int)CHAR_ID)))
		        .andExpect(jsonPath("$.account.id", is((int)ACCOUNT_ID)))
		        .andExpect(jsonPath("$.name", is(CHAR_NAME2)))
		        .andExpect(jsonPath("$.charClass", is(CHAR_CLASS.name())))
		        .andExpect(jsonPath("$.level", is(CHAR_LEVEL)))
        		.andExpect(jsonPath("$.currentlyDead", is(false)));

		verify(accountServiceMock, times(1)).findByApiKey(eq(testAccount.getApiKey()));
		verify(characterServiceMock, times(1)).findById(eq(CHAR_ID));
		verify(characterServiceMock, times(1)).update(any(Character.class));
		restrictMockAccess();
	}
	
	@Test
	public void testGetCharacters() throws Exception {
		List<Character> characterList = new ArrayList<Character>();
		characterList.add(testCharacter);
		characterList.add(testCharacter2);
		when(characterServiceMock.findAllByAccountOrderByLevel(any(Account.class), eq(false), eq(false))).thenReturn(characterList);
		mockMvc.perform(get("/api/account/"+testAccount.getApiKey()+"/character?hardcore=false&ironborn=false"))
				.andExpect(status().isOk())
		        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
		        .andExpect(jsonPath("$", hasSize(2)))
		        .andExpect(jsonPath("$[0].id", is((int)CHAR_ID)))
		        .andExpect(jsonPath("$[0].account.id", is((int)ACCOUNT_ID)))
		        .andExpect(jsonPath("$[0].name", is(CHAR_NAME)))
		        .andExpect(jsonPath("$[0].charClass", is(CHAR_CLASS.name())))
		        .andExpect(jsonPath("$[0].level", is(CHAR_LEVEL)))
        		.andExpect(jsonPath("$[0].currentlyDead", is(false)))
		        .andExpect(jsonPath("$[1].id", is((int)CHAR_ID2)))
		        .andExpect(jsonPath("$[1].account.id", is((int)ACCOUNT_ID)))
		        .andExpect(jsonPath("$[1].name", is(CHAR_NAME2)))
		        .andExpect(jsonPath("$[1].charClass", is(CHAR_CLASS2.name())))
		        .andExpect(jsonPath("$[1].level", is(LEVEL2)))
        		.andExpect(jsonPath("$[1].currentlyDead", is(true)));

		verify(accountServiceMock, times(1)).findByApiKey(eq(testAccount.getApiKey()));
		verify(characterServiceMock, times(1)).findAllByAccountOrderByLevel(eq(testAccount), eq(false), eq(false));
		restrictMockAccess();
	}
	
	@Test
	public void testLevelUpCharacter() throws Exception {
		mockMvc.perform(post("/api/account/"+testAccount.getApiKey()+"/character/"+CHAR_ID+"/level/"+ADD_LEVELS)
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
				.andExpect(status().isOk())
		        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
		        .andExpect(jsonPath("$.id", is((int)CHAR_ID)))
		        .andExpect(jsonPath("$.account.id", is((int)ACCOUNT_ID)))
		        .andExpect(jsonPath("$.name", is(CHAR_NAME)))
		        .andExpect(jsonPath("$.charClass", is(CHAR_CLASS.name())))
		        .andExpect(jsonPath("$.level", is(CHAR_LEVEL+ADD_LEVELS)))
        		.andExpect(jsonPath("$.currentlyDead", is(false)));

		verify(accountServiceMock, times(1)).findByApiKey(eq(testAccount.getApiKey()));
		verify(characterServiceMock, times(1)).findById(eq(CHAR_ID));
		verify(characterServiceMock, times(1)).update(any(Character.class));
		verify(accountCurrencyServiceMock, times(1)).adjustCurrency(eq(testAccount), anyBoolean(), anyBoolean(), anyLong(), 
				any(ModificationType.class), anyString());
		restrictMockAccess();
	}
	
	@Test
	public void testRezrectCharacter() throws Exception {
		mockMvc.perform(post("/api/account/"+testAccount.getApiKey()+"/character/"+CHAR_ID2+"/resurrect")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
				.andExpect(status().isOk())
		        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
		        .andExpect(jsonPath("$.id", is((int)CHAR_ID2)))
		        .andExpect(jsonPath("$.account.id", is((int)ACCOUNT_ID)))
		        .andExpect(jsonPath("$.name", is(CHAR_NAME2)))
		        .andExpect(jsonPath("$.charClass", is(CHAR_CLASS2.name())))
		        .andExpect(jsonPath("$.level", is(LEVEL2)))
        		.andExpect(jsonPath("$.currentlyDead", is(false)));

		verify(accountServiceMock, times(1)).findByApiKey(eq(testAccount.getApiKey()));
		verify(accountCurrencyServiceMock, times(1)).adjustCurrency(any(Account.class), anyBoolean(), anyBoolean(), anyLong(), 
				any(ModificationType.class), anyString());
		verify(characterServiceMock, times(1)).findById(eq(CHAR_ID2));
		verify(characterServiceMock, times(1)).update(any(Character.class));
		restrictMockAccess();
	}
}

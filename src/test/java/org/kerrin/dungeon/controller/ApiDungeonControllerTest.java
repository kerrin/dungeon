package org.kerrin.dungeon.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
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
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kerrin.dungeon.controller.api.ApiDungeonController;
import org.kerrin.dungeon.model.Character;
import org.kerrin.dungeon.model.Dungeon;
import org.kerrin.dungeon.service.CharacterEquipmentService;
import org.kerrin.dungeon.service.EquipmentService;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class ApiDungeonControllerTest extends MockMvcTest {
	
	@Before
    public void setUp() {
		super.setUp();
        mockMvc = MockMvcBuilders.standaloneSetup(
        			new ApiDungeonController(
        					accountServiceMock, characterServiceMock, equipmentServiceMock, 
        					characterEquipmentServiceMock, dungeonServiceMock, 
        					accountCurrencyServiceMock, accountBoostServiceMock))
                .setHandlerExceptionResolvers(exceptionResolver())
                .setValidator(validator())
                .setViewResolvers(viewResolver())
                .build();
	}
	
	@Test
	public void testGetDungeon() throws Exception {
		mockMvc.perform(get("/api/account/"+testAccount.getApiKey()+"/dungeon/"+DUNGEON_STARTED_ID))
					.andExpect(status().isOk())
			        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
			        .andExpect(jsonPath("$.id", is((int)DUNGEON_STARTED_ID)))
			        .andExpect(jsonPath("$.account.id", is((int)ACCOUNT_ID)))
			        .andExpect(jsonPath("$.type", is(DUNGEON_TYPE_STARTED.name())))
			        .andExpect(jsonPath("$.level", is(LEVEL)));

		verify(accountServiceMock, times(1)).findByApiKey(eq(testAccount.getApiKey()));
		verify(dungeonServiceMock, times(1)).findById(eq(DUNGEON_STARTED_ID));
		restrictMockAccess();
	}
	
	@Test
	public void testGetAvailableDungeons() throws Exception {
		List<Dungeon> dungeonList = new ArrayList<Dungeon>();
		dungeonList.add(testDungeonStarted);
		dungeonList.add(testDungeon2);
		when(dungeonServiceMock.findAllByAccount(eq(testAccount), eq(false),  eq(false))).thenReturn(dungeonList);
		mockMvc.perform(get("/api/account/"+testAccount.getApiKey()+"/dungeon?hardcore=false&ironborn=false"))
			.andExpect(status().isOk())
	        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
	        .andExpect(jsonPath("$", hasSize(2)))
	        .andExpect(jsonPath("$[0].id", is((int)DUNGEON_STARTED_ID)))
	        .andExpect(jsonPath("$[0].account.id", is((int)ACCOUNT_ID)))
	        .andExpect(jsonPath("$[0].type", is(DUNGEON_TYPE_STARTED.name())))
	        .andExpect(jsonPath("$[0].level", is(LEVEL)))
	        .andExpect(jsonPath("$[1].id", is((int)DUNGEON_ID2)))
	        .andExpect(jsonPath("$[1].account.id", is((int)ACCOUNT_ID)))
	        .andExpect(jsonPath("$[1].type", is(DUNGEON_TYPE2.name())))
	        .andExpect(jsonPath("$[1].level", is(LEVEL2)));

		verify(accountServiceMock, times(1)).findByApiKey(eq(testAccount.getApiKey()));
		verify(dungeonServiceMock, times(1)).findAllByAccount(eq(testAccount), eq(false),  eq(false));
		restrictMockAccess();
	}
	
	@Test
	public void testGetActiveDungeons() throws Exception {
		List<Dungeon> dungeonList = new ArrayList<Dungeon>();
		dungeonList.add(testDungeon2);
		when(dungeonServiceMock.findAllByAccountAndActive(eq(testAccount))).thenReturn(dungeonList);
		mockMvc.perform(get("/api/account/"+testAccount.getApiKey()+"/dungeon/active"))
			.andExpect(status().isOk())
	        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
	        .andExpect(jsonPath("$", hasSize(1)))
	        .andExpect(jsonPath("$[0].id", is((int)DUNGEON_ID2)))
	        .andExpect(jsonPath("$[0].account.id", is((int)ACCOUNT_ID)))
	        .andExpect(jsonPath("$[0].type", is(DUNGEON_TYPE2.name())))
	        .andExpect(jsonPath("$[0].level", is(LEVEL2)));

		verify(accountServiceMock, times(1)).findByApiKey(eq(testAccount.getApiKey()));
		verify(dungeonServiceMock, times(1)).findAllByAccountAndActive(eq(testAccount));
		restrictMockAccess();
	}
	
	@Test
	public void testStartDungeon() throws Exception {
		List<Character> characters = new ArrayList<Character>();
		Character testCharacter = new Character(CHAR_ID, testAccount, false, false, 
				CHAR_NAME, CHAR_CLASS, CHAR_LEVEL, CHAR_XP, CHAR_PRESTIGE_LEVEL, null /* Alive */, null /* Not in a dungeon yet */);
		characters.add(testCharacter);
		testDungeon.setCharacters(characters);
		mockMvc.perform(post("/api/account/"+testAccount.getApiKey()+"/dungeon/"+DUNGEON_ID+"/start")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.param("id", ""+testDungeon.getId())
					.param("characterIds[0]", ""+testCharacter.getId())
                )
				.andExpect(status().isOk())
		        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
		        .andExpect(jsonPath("$.id", is((int)DUNGEON_STARTED_ID)))
		        .andExpect(jsonPath("$.account.id", is((int)ACCOUNT_ID)))
		        .andExpect(jsonPath("$.type", is(DUNGEON_TYPE_STARTED.name())))
		        .andExpect(jsonPath("$.level", is(LEVEL)))
        		.andExpect(jsonPath("$.started", not(nullValue())))
        		.andExpect(jsonPath("$.characters", hasSize(1)));

		verify(accountServiceMock, times(1)).findByApiKey(eq(testAccount.getApiKey()));
		verify(dungeonServiceMock, times(1)).findById(eq(DUNGEON_ID));
		verify(dungeonServiceMock, times(1)).startDungeon(eq(testDungeon), any(long[].class), anyInt(), 
				any(EquipmentService.class), any(CharacterEquipmentService.class));
		
		//verify(dungeonServiceMock, times(1)).update(any(Dungeon.class));
		//verify(characterServiceMock, times(1)).findById(eq(CHAR_ID));
		//verify(characterServiceMock, times(1)).update(any(Character.class));
		//verify(characterEquipmentServiceMock, times(1)).findById(eq(testCharacter.getId()));
		restrictMockAccess();
	}
}

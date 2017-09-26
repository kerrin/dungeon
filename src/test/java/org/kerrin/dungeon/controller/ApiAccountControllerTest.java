package org.kerrin.dungeon.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kerrin.dungeon.controller.api.ApiAccountController;
import org.kerrin.dungeon.model.Account;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class ApiAccountControllerTest extends MockMvcTest {
	
	@Before
    public void setUp() {
		super.setUp();
		mockMvc = MockMvcBuilders.standaloneSetup(new ApiAccountController(accountServiceMock))
                .setHandlerExceptionResolvers(exceptionResolver())
                .setValidator(validator())
                .setViewResolvers(viewResolver())
                .build();
	}
	
	@Test
	public void testGetAccount() throws Exception {
		mockMvc.perform(get("/api/account/"+testAccount.getApiKey()))
					.andExpect(status().isOk())
			        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
			        .andExpect(jsonPath("$.id", is((int)ACCOUNT_ID)))
			        .andExpect(jsonPath("$.username", is(USERNAME)))
			        .andExpect(jsonPath("$.displayName", is(DISPLAYNAME)))
			        .andExpect(jsonPath("$.loginTokens", is(0)))
	        		.andExpect(jsonPath("$.onHoliday", is(false)))
	        		.andExpect(jsonPath("$.previousLogin", is(previousLogin.getTime())))
	        		.andExpect(jsonPath("$.lastLogin", is(lastLogin.getTime())));

		verify(accountServiceMock, times(1)).findByApiKey(eq(testAccount.getApiKey()));
		restrictMockAccess();
	}
	
	@Test
	public void testUpdateAccount() throws Exception {
		testAccount.setPassword(PASSWORD2);
		testAccount.setDisplayName(DISPLAYNAME2);
		mockMvc.perform(post("/api/account/"+testAccount.getApiKey())
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.param("id", ""+testAccount.getId())
					.param("username", testAccount.getUsername())
	                .param("displayName", testAccount.getDisplayName())
	                .param("password", testAccount.getHashedPassword())
                )
				.andExpect(status().isOk())
		        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
		        .andExpect(jsonPath("$.id", is((int)ACCOUNT_ID)))
		        .andExpect(jsonPath("$.username", is(USERNAME)))
		        .andExpect(jsonPath("$.displayName", is(DISPLAYNAME2)))
		        .andExpect(jsonPath("$.loginTokens", is(0)))
        		.andExpect(jsonPath("$.onHoliday", is(false)))
        		.andExpect(jsonPath("$.previousLogin", is(previousLogin.getTime())))
        		.andExpect(jsonPath("$.lastLogin", is(lastLogin.getTime())));

		verify(accountServiceMock, times(1)).findByApiKey(eq(testAccount.getApiKey()));
		verify(accountServiceMock, times(1)).update(any(Account.class), anyBoolean());
		restrictMockAccess();
	}

	@Test
	public void testProcessAccount() throws Exception {
		mockMvc.perform(get("/api/account/"+testAccount.getApiKey()+"/process"))
					.andExpect(status().isOk())
			        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
			        .andExpect(jsonPath("$.id", is((int)ACCOUNT_ID)))
			        .andExpect(jsonPath("$.username", is(USERNAME)))
			        .andExpect(jsonPath("$.displayName", is(DISPLAYNAME)))
			        .andExpect(jsonPath("$.loginTokens", is(0)))
	        		.andExpect(jsonPath("$.onHoliday", is(false)))
	        		.andExpect(jsonPath("$.previousLogin", is(previousLogin.getTime())))
	        		.andExpect(jsonPath("$.lastLogin", is(lastLogin.getTime())));

		verify(accountServiceMock, times(1)).findByApiKey(eq(testAccount.getApiKey()));
		verify(accountServiceMock, times(1)).processAccount(any(Account.class));
		restrictMockAccess();
	}
}

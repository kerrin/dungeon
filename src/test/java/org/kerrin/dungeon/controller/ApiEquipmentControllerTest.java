package org.kerrin.dungeon.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kerrin.dungeon.controller.api.ApiEquipmentController;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class ApiEquipmentControllerTest extends MockMvcTest {
	
	@Before
    public void setUp() {
		super.setUp();
        mockMvc = MockMvcBuilders.standaloneSetup(new ApiEquipmentController(
        		accountServiceMock, accountCurrencyServiceMock, equipmentServiceMock, boostItemServiceMock, 
        		hiscoreServiceMock, achievementServiceMock))
                .setHandlerExceptionResolvers(exceptionResolver())
                .setValidator(validator())
                .setViewResolvers(viewResolver())
                .build();
	}
	
	@Test
	public void testGetEquipment() throws Exception {
		mockMvc.perform(get("/api/account/"+testAccount.getApiKey()+"/equipment/"+EQUIPMENT_ID))
					.andExpect(status().isOk())
			        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
			        .andExpect(jsonPath("$.id", is((int)EQUIPMENT_ID)))
			        .andExpect(jsonPath("$.quality", is(EQUIPMENT_QUALITY.name())))
			        .andExpect(jsonPath("$.equipmentType", is(EQUIPMENT_TYPE.name())))
			        .andExpect(jsonPath("$.level", is(LEVEL)));

		verify(accountServiceMock, times(1)).findByApiKey(eq(testAccount.getApiKey()));
		verify(accountServiceMock, times(1)).accountOwnsEquipment(eq(testAccount), eq(testEquipment));
		verify(equipmentServiceMock, times(1)).findById(eq(EQUIPMENT_ID));
		restrictMockAccess();
	}
}

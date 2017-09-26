package org.kerrin.dungeon.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
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
import org.kerrin.dungeon.controller.api.ApiInventoryController;
import org.kerrin.dungeon.model.Equipment;
import org.kerrin.dungeon.model.StashSlotItemSuper;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class ApiInventoryControllerTest extends MockMvcTest {
	
	@Before
    public void setUp() {
		super.setUp();
        mockMvc = MockMvcBuilders.standaloneSetup(
        		new ApiInventoryController(
        			accountServiceMock, inventoryServiceMock, equipmentServiceMock, boostItemServiceMock, 
        			stashSlotItemServiceMock)
        		)
                .setHandlerExceptionResolvers(exceptionResolver())
                .setValidator(validator())
                .setViewResolvers(viewResolver())
                .build();
	}
	
	@Test
	public void testGetInventory() throws Exception {
		mockMvc.perform(get("/api/account/"+testAccount.getApiKey()+"/inventory?hardcore=false&ironborn=false"))
					.andExpect(status().isOk())
			        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
			        .andExpect(jsonPath("$.accountId", is((int)ACCOUNT_ID)))
			        .andExpect(jsonPath("$.size", is(SLOT_NUMBER)))
			        .andExpect(jsonPath("$.inventorySlots['0'].id", is(equipmentList.get(0).getId())))
			        .andExpect(jsonPath("$.inventorySlots['0'].quality", is(equipmentList.get(0).getQuality().name())))
			        .andExpect(jsonPath("$.inventorySlots['0'].level", is(equipmentList.get(0).getLevel())))
			        .andExpect(jsonPath("$.inventorySlots['1'].id", is(equipmentList.get(1).getId())))
			        .andExpect(jsonPath("$.inventorySlots['1'].quality", is(equipmentList.get(1).getQuality().name())))
			        .andExpect(jsonPath("$.inventorySlots['1'].level", is(equipmentList.get(1).getLevel())))
			        .andExpect(jsonPath("$.inventorySlots['2'].id", is(equipmentList.get(2).getId())))
			        .andExpect(jsonPath("$.inventorySlots['2'].quality", is(equipmentList.get(2).getQuality().name())))
			        .andExpect(jsonPath("$.inventorySlots['2'].level", is(equipmentList.get(2).getLevel())))
			        .andExpect(jsonPath("$.inventorySlots['3'].id", is(equipmentList.get(3).getId())))
			        .andExpect(jsonPath("$.inventorySlots['3'].quality", is(equipmentList.get(3).getQuality().name())))
			        .andExpect(jsonPath("$.inventorySlots['3'].level", is(equipmentList.get(3).getLevel())))
			        .andExpect(jsonPath("$.inventorySlots['4'].id", is(equipmentList.get(4).getId())))
			        .andExpect(jsonPath("$.inventorySlots['4'].quality", is(equipmentList.get(4).getQuality().name())))
			        .andExpect(jsonPath("$.inventorySlots['4'].level", is(equipmentList.get(4).getLevel())))
			        .andExpect(jsonPath("$.inventorySlots['5'].id", is(equipmentList.get(5).getId())))
			        .andExpect(jsonPath("$.inventorySlots['5'].quality", is(equipmentList.get(5).getQuality().name())))
			        .andExpect(jsonPath("$.inventorySlots['5'].level", is(equipmentList.get(5).getLevel())))
			        .andExpect(jsonPath("$.inventorySlots['6'].id", is(equipmentList.get(6).getId())))
			        .andExpect(jsonPath("$.inventorySlots['6'].quality", is(equipmentList.get(6).getQuality().name())))
			        .andExpect(jsonPath("$.inventorySlots['6'].level", is(equipmentList.get(6).getLevel())))
			        .andExpect(jsonPath("$.inventorySlots['7'].id", is(equipmentList.get(7).getId())))
			        .andExpect(jsonPath("$.inventorySlots['7'].quality", is(equipmentList.get(7).getQuality().name())))
			        .andExpect(jsonPath("$.inventorySlots['7'].level", is(equipmentList.get(7).getLevel())))
			        .andExpect(jsonPath("$.inventorySlots['8'].id", is(equipmentList.get(8).getId())))
			        .andExpect(jsonPath("$.inventorySlots['8'].quality", is(equipmentList.get(8).getQuality().name())))
			        .andExpect(jsonPath("$.inventorySlots['8'].level", is(equipmentList.get(8).getLevel())))
			        .andExpect(jsonPath("$.inventorySlots['9'].id", is(equipmentList.get(9).getId())))
			        .andExpect(jsonPath("$.inventorySlots['9'].quality", is(equipmentList.get(9).getQuality().name())))
			        .andExpect(jsonPath("$.inventorySlots['9'].level", is(equipmentList.get(9).getLevel())));
			        ;

		verify(accountServiceMock, times(1)).findByApiKey(eq(testAccount.getApiKey()));
		verify(inventoryServiceMock, times(1)).findByAccount(eq(testAccount), anyBoolean(), anyBoolean());
		restrictMockAccess();
	}
	
	@Test
	public void testGetInventorySlot() throws Exception {
		for(int slotIndex=0; slotIndex < SLOT_NUMBER; slotIndex++) {
			mockMvc.perform(get("/api/account/"+testAccount.getApiKey()+"/inventory/"+slotIndex+"?hardcore=false&ironborn=false"))
						.andExpect(status().isOk())
				        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
				        .andExpect(jsonPath("$.id", is(equipmentList.get(slotIndex).getId())))
				        .andExpect(jsonPath("$.quality", is(equipmentList.get(slotIndex).getQuality().name())))
				        .andExpect(jsonPath("$.level", is(equipmentList.get(slotIndex).getLevel())));
		}

		verify(accountServiceMock, times(SLOT_NUMBER)).findByApiKey(eq(testAccount.getApiKey()));
		verify(inventoryServiceMock, times(SLOT_NUMBER)).findByAccount(eq(testAccount), anyBoolean(), anyBoolean());
		restrictMockAccess();

	}
	
	@Test
	public void testUpdateInventorySlot() throws Exception {
		for(int slotIndex=0; slotIndex < SLOT_NUMBER; slotIndex++) {
			Equipment equipment = createRandomEquipment();
			mockMvc.perform(post("/api/account/"+testAccount.getApiKey()+"/inventory/"+slotIndex+"?hardcore=false&ironborn=false")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("accountId", ""+ACCOUNT_ID)
						.param("equipmentId", ""+equipment.getId())
	                )
					.andExpect(status().isOk())
			        .andExpect(content().contentType(APPLICATION_JSON_UTF8));
		}

		verify(accountServiceMock, times(SLOT_NUMBER)).findByApiKey(eq(testAccount.getApiKey()));
		verify(accountServiceMock, times(SLOT_NUMBER)).accountOwnsEquipment(eq(testAccount), any(Equipment.class));
		verify(inventoryServiceMock, times(SLOT_NUMBER)).findByAccount(eq(testAccount), anyBoolean(), anyBoolean());
		//verify(inventoryServiceMock, times(SLOT_NUMBER)).update(eq(testInventory));
		verify(stashSlotItemServiceMock, times(SLOT_NUMBER)).swapItemWithInventory(eq(testAccount), any(StashSlotItemSuper.class), anyInt());
		verify(equipmentServiceMock, times(SLOT_NUMBER)).findById(anyInt());
		restrictMockAccess();
	}
}

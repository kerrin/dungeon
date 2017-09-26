package org.kerrin.dungeon.enums;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All the equipment slots for a character
 * 
 * @author Kerrin
 *
 */
@SuppressWarnings("serial")
public enum EquipmentType {
	UNKNOWN(0,"Unknown!", BaseStatType.NONE),
	HEAD(1, "Head", BaseStatType.ARMOUR), 
	SHOULDERS(2, "Shoulders", BaseStatType.ARMOUR), 
	CHEST(3, "Chest", BaseStatType.ARMOUR),
	LEGS(4, "Legs", BaseStatType.ARMOUR), 
	FEET(5, "Feet", BaseStatType.ARMOUR), 
	HANDS(6, "Hands", BaseStatType.ARMOUR),
	MAIN_WEAPON(7, "Weapon", BaseStatType.WEAPON),
	OFF_HAND(8, "Offhand", BaseStatType.WEAPON),
	RING(9, "Ring", BaseStatType.JEWLERY),
	AMULET(10, "Amulet", BaseStatType.JEWLERY),
	BRACERS(11, "Bracers", BaseStatType.JEWLERY),
	BROACH(12, "Broach", BaseStatType.JEWLERY)
	;
	
	private static final Logger logger = LoggerFactory.getLogger(EquipmentType.class);
	
	private int id;
	private String niceName;
	private BaseStatType baseStatType;
	private List<CharSlot> validSlots;
	
	static {
		UNKNOWN.validSlots = new ArrayList<CharSlot>() {
			{
				add(CharSlot.UNKNOWN);
			}
		};
		HEAD.validSlots = new ArrayList<CharSlot>() {
			{
				add(CharSlot.HEAD);
			}
		};
		SHOULDERS.validSlots = new ArrayList<CharSlot>() {
			{
				add(CharSlot.SHOULDERS);
			}
		};
		CHEST.validSlots = new ArrayList<CharSlot>() {
			{
				add(CharSlot.CHEST);
			}
		};
		LEGS.validSlots = new ArrayList<CharSlot>() {
			{
				add(CharSlot.LEGS);
			}
		};
		FEET.validSlots = new ArrayList<CharSlot>() {
			{
				add(CharSlot.FEET);
			}
		};
		HANDS.validSlots = new ArrayList<CharSlot>() {
			{
				add(CharSlot.HANDS);
			}
		};
		MAIN_WEAPON.validSlots = new ArrayList<CharSlot>() {
			{
				add(CharSlot.MAIN_WEAPON);
				//add(CharSlot.OFF_HAND);
			}
		};
		OFF_HAND.validSlots = new ArrayList<CharSlot>() {
			{
				add(CharSlot.OFF_HAND);
			}
		};
		RING.validSlots = new ArrayList<CharSlot>() {
			{
				add(CharSlot.RING_LEFT);
				add(CharSlot.RING_RIGHT);
			}
		};
		AMULET.validSlots = new ArrayList<CharSlot>() {
			{
				add(CharSlot.AMULET);
			}
		};
		BRACERS.validSlots = new ArrayList<CharSlot>() {
			{
				add(CharSlot.BRACERS);
			}
		};
		BROACH.validSlots = new ArrayList<CharSlot>() {
			{
				add(CharSlot.BROACH);
			}
		};
	}
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param name
	 */
	EquipmentType (int id, String niceName, BaseStatType baseStatType) {
		this.id = id;
		this.niceName = niceName;
		this.baseStatType = baseStatType;
	}
	
	/**
	 * Get the identifier
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get the nice name
	 * 
	 * @return
	 */
	public String getNiceName() {
		return niceName;
	}
	
	/**
	 * For JSTL, to get a name without spaces
	 * @return
	 */
	public String getName() {
		return name();
	}
	
	/**
	 * The type of stats that this equipment rolls always
	 * @return
	 */
	public BaseStatType getBaseStatType() {
		return baseStatType;
	}

	/**
	 * Get a character slot from its identifier
	 * 
	 * @param id
	 * @return
	 */
	public static EquipmentType fromId(int id) {
		for(EquipmentType thisSlot:values()) {
			if(thisSlot.id == id) return thisSlot;
		}
		return UNKNOWN;
	}

	public static EquipmentType getRandomType() {
		int chance = values().length - 1;
		for(EquipmentType thisSlot:values()) {
			if(thisSlot == EquipmentType.UNKNOWN) continue;
			int random = (int) (Math.random()*chance);
			if(random < 1) return thisSlot;
			chance--;
		}
		logger.error("No equipment type rolled randomly, chance is still " + chance);
		return UNKNOWN;
	}

	public List<CharSlot> getValidSlots() {
		return validSlots;
	}
	
	public List<Integer> getValidSlotIds() {
		List<Integer> ids = new ArrayList<Integer>();
		for(CharSlot slot:validSlots) {
			ids.add(slot.getId());
		}
		return ids;
	}
	
	public static List<EquipmentType> getValidTypesFromCharSlot(CharSlot slot) {
		List<EquipmentType> validEquipmentTypes = new ArrayList<EquipmentType>();
		for(EquipmentType type:values()) {
			if(type.validSlots.contains(slot)) {
				validEquipmentTypes.add(type);
			}
		}
		
		return validEquipmentTypes;
	}
}

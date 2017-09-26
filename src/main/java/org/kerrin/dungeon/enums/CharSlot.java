package org.kerrin.dungeon.enums;

/**
 * All the equipment slots for a character
 * 
 * @author Kerrin
 *
 */
public enum CharSlot {
	UNKNOWN(0,"Unknown!", new int[]{-1,-1}),
	HEAD(1, "Head", new int[]{0,1}), 
	SHOULDERS(2, "Shoulders", new int[]{0,0}), 
	CHEST(3, "Chest", new int[]{2,1}),
	LEGS(4, "Legs", new int[]{3,1}), 
	FEET(5, "Feet", new int[]{4,1}), 
	HANDS(6, "Hands", new int[]{3,2}),
	MAIN_WEAPON(7, "Weapon", new int[]{4,0}),
	OFF_HAND(8, "Offhand", new int[]{4,2}), // Dual wield weapon
	RING_LEFT(9, "Left Ring", new int[]{2,0}),
	RING_RIGHT(10, "Right Ring", new int[]{2,2}),
	AMULET(11, "Amulet", new int[]{1,1}),
	BRACERS(12, "Bracers", new int[]{3,0}),
	BROACH(13, "Broach", new int[]{1,2}),
	;
	
	// private static final Logger logger = LoggerFactory.getLogger(CharSlot.class);
	
	private int id;
	private String niceName;
	private EquipmentType validEquipment;
	/** Location on paper doll (row, column) 0 indexed from top left */
	private int[] dollLocation;
	
	// Need to statically initialise the valid equipment or the valid slots on equipment type is not defined
	static {
		UNKNOWN.validEquipment = EquipmentType.UNKNOWN;
		HEAD.validEquipment = EquipmentType.HANDS; 
		SHOULDERS.validEquipment = EquipmentType.SHOULDERS; 
		CHEST.validEquipment = EquipmentType.CHEST;
		LEGS.validEquipment = EquipmentType.LEGS; 
		FEET.validEquipment = EquipmentType.FEET; 
		HANDS.validEquipment = EquipmentType.HANDS;
		MAIN_WEAPON.validEquipment = EquipmentType.MAIN_WEAPON;
		OFF_HAND.validEquipment = EquipmentType.OFF_HAND;
		RING_LEFT.validEquipment = EquipmentType.RING;
		RING_RIGHT.validEquipment = EquipmentType.RING;
		AMULET.validEquipment = EquipmentType.AMULET;
		BRACERS.validEquipment = EquipmentType.BRACERS;
		BROACH.validEquipment = EquipmentType.BROACH;
	}
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param name
	 */
	CharSlot (int id, String niceName, int[] dollLocation) {
		this.id = id;
		this.niceName = niceName;
		this.dollLocation = dollLocation;
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
	
	public EquipmentType getValidEquipment() {
		return validEquipment;
	}

	/**
	 * Get location on paper doll (row, column) 0 indexed from top left
	 * @return
	 */
	public int[] getDollLocation() {
		return dollLocation;
	}

	/**
	 * Get a character slot from its identifier
	 * 
	 * @param id
	 * @return
	 */
	public static CharSlot fromId(int id) {
		for(CharSlot thisSlot:values()) {
			if(thisSlot.id == id) return thisSlot;
		}
		return UNKNOWN;
	}
	
	/**
	 * Get a character slot from its paper doll location
	 * 
	 * @param row
	 * @param column
	 * 
	 * @return
	 */
	public static CharSlot fromLocation(int row, int column) {
		for(CharSlot thisSlot:values()) {
			if(thisSlot.dollLocation[0] == row && thisSlot.dollLocation[1] == column) return thisSlot;
		}
		return UNKNOWN;
	}

	public static CharSlot getRandom() {
		return fromId(1 + (int)(Math.random()*values().length-1));
	}
}

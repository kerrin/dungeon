package org.kerrin.dungeon.enums;

public enum DungeonEventType {
	NONE(0),
	CHARACTER_ATTACK(1),
	MONSTER_ATTACK(2),
	CHARACTER_SPELL(3),
	CHARACTER_TAKE_DAMAGE(4),
	CHARACTER_HEAL(5),
	CHARACTER_DIED(6),
	CHARACTER_BUFF(7),
	CHARACTER_DEBUFF(8),
	
	MONSTER_SPELL(10),
	MONSTER_SUMMON(11),
	MONSTER_TAKE_DAMAGE(12),
	MONSTER_HEAL(13),
	MONSTER_DIED(14),
	MONSTER_BUFF(15),
	MONSTER_DEBUFF(16),
	//CHARACTER_SUMMON(), // Characters can't currently summon
	
	INFO(20),
	;
	
	/** The identifier of the event */
	private int id;
	
	private DungeonEventType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public DungeonEventType fromId(int id) {
		for(DungeonEventType thisType:values()) {
			if(thisType.id == id) return thisType;
		}
		return NONE;
	}
}

package org.kerrin.dungeon.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum AttackType {
	SPELL(65),
	WEAPON(5), 
	SUMMON(30);

	private static final Logger logger = LoggerFactory.getLogger(AttackType.class);
			
	private int randomChance;	
	
	private AttackType(int randomChance) {
		this.randomChance = randomChance;
	}

	public static AttackType getRandom() {
		int chance = 0;
		for(AttackType thisType:values()) {
			chance += thisType.randomChance;
		}
		for(AttackType thisType:values()) {
			int random = (int) (Math.random()*chance);
			if(random < thisType.randomChance) return thisType;
			chance -= thisType.randomChance;
		}
		logger.error("No attack type rolled randomly, chance is still " + chance);
		int typeCount = values().length;
		return values()[(int)(Math.random()*typeCount)];
	}
}

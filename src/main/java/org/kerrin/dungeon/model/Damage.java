package org.kerrin.dungeon.model;

import org.kerrin.dungeon.enums.DamageType;

public class Damage {
	private int damageAmount;
	private DamageType damageType;
	public Damage(int damageAmount, DamageType damageType) {
		super();
		this.damageAmount = damageAmount;
		this.damageType = damageType;
	}
	public int getDamageAmount() {
		return damageAmount;
	}
	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}
	public DamageType getDamageType() {
		return damageType;
	}
	public void setDamageType(DamageType damageType) {
		this.damageType = damageType;
	}
}

package org.kerrin.dungeon.forms;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Min;

import org.kerrin.dungeon.enums.BooleanOptions;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.EquipmentType;
import org.kerrin.dungeon.model.Equipment;

/**
 * Form data for a Equipment
 * 
 * @author Kerrin
 *
 */
public class EquipmentSearchForm {
	/** The Equipment identifier */
	@Min(-1)
	private long id = -1;

	private EquipmentQuality quality;
	
	@Min(-1)
	private int greaterThanLevel;
	
	@Min(-1)
	private int lessThanLevel;
	
	private BooleanOptions hardcore;
	
	private BooleanOptions ironborn;
	
	private Map<Integer,Integer> attributes;
	
	private EquipmentType equipmentType;

	public EquipmentSearchForm() {}

	public EquipmentSearchForm(Equipment equipment) {
		this(equipment.getId(), 
				equipment.getQuality(), 
				equipment.getLevel()-1,
				equipment.getLevel()+1,
				BooleanOptions.fromBoolean(equipment.isHardcore()),
				BooleanOptions.fromBoolean(equipment.isIronborn()),
				equipment.getAttributes(true),
				equipment.getEquipmentType()
				);
	}
	
	public EquipmentSearchForm(long id, EquipmentQuality quality, int greaterThanLevel, int lessThanLevel,  
			BooleanOptions hardcore, BooleanOptions ironborn,
			Map<Integer,Integer> attributes, EquipmentType equipmentType) {
		super();
		this.id = id;
		this.quality = quality;
		this.greaterThanLevel = greaterThanLevel;
		this.lessThanLevel = lessThanLevel;
		this.attributes = attributes;
		this.equipmentType = equipmentType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public EquipmentQuality getQuality() {
		return quality;
	}

	public void setQuality(EquipmentQuality quality) {
		this.quality = quality;
	}

	public int getGreaterThanLevel() {
		return greaterThanLevel;
	}

	public void setGreaterThanLevel(int greaterThanLevel) {
		this.greaterThanLevel = greaterThanLevel;
	}

	public int getLessThanLevel() {
		return lessThanLevel;
	}

	public void setLessThanLevel(int lessThanLevel) {
		this.lessThanLevel = lessThanLevel;
	}

	public BooleanOptions getHardcore() {
		return hardcore==null?BooleanOptions.BOTH:hardcore;
	}

	public void setHardcore(BooleanOptions hardcore) {
		this.hardcore = hardcore;
	}

	public BooleanOptions getIronborn() {
		return ironborn==null?BooleanOptions.BOTH:ironborn;
	}

	public void setIronborn(BooleanOptions ironborn) {
		this.ironborn = ironborn;
	}

	public Map<Integer, Integer> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<Integer, Integer> attributes) {
		this.attributes = attributes;
	}

	public EquipmentType getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

	@Override
	public String toString() {
		return "EquipmentForm [" +
				"id=" + id + ", " +
				"quality=" + quality.getNiceName() + ", " +
				"greaterThanLevel=" + greaterThanLevel + ", " +
				"lessThanLevel=" + lessThanLevel + ", " +
				"attributes=" + attributes + ", " +
				"equipmentType=" + equipmentType + 
				"]";
	}
}

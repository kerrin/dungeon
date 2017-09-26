package org.kerrin.dungeon.forms;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Transient;
import javax.validation.constraints.Min;

import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.EquipmentType;
import org.kerrin.dungeon.model.Equipment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Form data for a Equipment
 * 
 * @author Kerrin
 *
 */
public class EquipmentForm {
	@Transient
	@JsonIgnore
	private static final Logger logger = LoggerFactory.getLogger(EquipmentForm.class);
	
	@Transient
	@JsonIgnore
	private static final int QUALITY_NUMBER = 7 - 2;

	/** The Equipment identifier */
	@Min(-1)
	private long id = -1;

	private EquipmentQuality quality = EquipmentQuality.USELESS;
	
	@Min(1)
	private int level;
	
	/** Is this record for hardcore characters */
	private boolean hardcore = false;
	
	/** Is this record for ironborn characters */
	private boolean ironborn = false;
	
	private EquipmentAttribute[] equipmentAttributeTypes;
	private Integer[] equipmentAttributeValues;
	
	private EquipmentAttribute baseAttribute;
	private int baseAttributeValue;
	private EquipmentAttribute defenceAttribute;
	private int defenceAttributeValue;
	private EquipmentAttribute ancientAttribute;
	private int ancientAttributeValue;
	
	private EquipmentType equipmentType;

	/** Where is the equipment stored */
	private EquipmentLocation equipmentLocation;
	/** 
	 * What is the id of the location
	 * 	CHARACTER 	=> character id
	 *	INVENTORY 	=> inventory slot id
	 *	DUNGEON 	=> dungeon id
	 *  MESSAGE		=> Sent to account in a message
	 */
	private long equipmentLocationId;
	
	private long sendToAccountId = -1;
	
	public EquipmentForm() {
		super();
		
		this.equipmentAttributeTypes = new EquipmentAttribute[QUALITY_NUMBER];
		this.equipmentAttributeValues = new Integer[QUALITY_NUMBER];
	}

	public EquipmentForm(Equipment equipment) {
		super();
		
		this.id = equipment.getId();
		this.quality = equipment.getQuality();
		this.level = equipment.getLevel();
		this.hardcore = equipment.isHardcore();
		this.ironborn = equipment.isIronborn();
		this.equipmentType = equipment.getEquipmentType();
		this.equipmentLocation = equipment.getEquipmentLocation();
		this.equipmentLocationId = equipment.getEquipmentLocationId();
		this.baseAttribute = equipment.getBaseAttribute();
		this.baseAttributeValue = equipment.getBaseAttributeValue();
		this.defenceAttribute = equipment.getDefenceAttribute();
		this.defenceAttributeValue = equipment.getDefenceAttributeValue();
		setAttributes(equipment.getAttributes(false));
		this.ancientAttribute = equipment.getAncientAttribute();
		this.ancientAttributeValue = equipment.getAncientAttributeValue();
	}
	
	public EquipmentForm(long id, EquipmentQuality quality, int level, boolean hardcore, boolean ironborn, 
			EquipmentLocation equipmentLocation, long equipmentLocationId, 
			EquipmentAttribute ancientAttribute, int ancientAttributeValue) {
		this();
		
		this.id = id;
		this.quality = quality;
		this.level = level;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		this.equipmentLocation = equipmentLocation;
		this.equipmentLocationId = equipmentLocationId;
		this.ancientAttribute = ancientAttribute;
		this.ancientAttributeValue = ancientAttributeValue;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isHardcore() {
		return hardcore;
	}

	public void setHardcore(boolean hardcore) {
		this.hardcore = hardcore;
	}

	public boolean isIronborn() {
		return ironborn;
	}

	public void setIronborn(boolean ironborn) {
		this.ironborn = ironborn;
	}

	public EquipmentType getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

	public EquipmentLocation getEquipmentLocation() {
		return equipmentLocation;
	}

	public void setEquipmentLocation(EquipmentLocation equipmentLocation) {
		this.equipmentLocation = equipmentLocation;
	}

	public long getEquipmentLocationId() {
		return equipmentLocationId;
	}

	public void setEquipmentLocationId(long equipmentLocationId) {
		this.equipmentLocationId = equipmentLocationId;
	}

	public long getSendToAccountId() {
		return sendToAccountId;
	}

	public void setSendToAccountId(long sendToAccountId) {
		this.sendToAccountId = sendToAccountId;
	}

	public EquipmentAttribute getBaseAttributeType() {
		return baseAttribute;
	}

	public void setBaseAttributeType(EquipmentAttribute baseAttribute) {
		this.baseAttribute = baseAttribute;
	}

	public int getBaseAttributeValue() {
		return baseAttributeValue;
	}

	public void setBaseAttributeValue(int baseAttributeValue) {
		this.baseAttributeValue = baseAttributeValue;
	}

	public EquipmentAttribute getDefenceAttributeType() {
		return defenceAttribute;
	}

	public void setDefenceAttributeType(EquipmentAttribute defenceAttribute) {
		this.defenceAttribute = defenceAttribute;
	}

	public int getDefenceAttributeValue() {
		return defenceAttributeValue;
	}

	public void setDefenceAttributeValue(int defenceAttributeValue) {
		this.defenceAttributeValue = defenceAttributeValue;
	}

	public EquipmentAttribute getAncientAttributeType() {
		return ancientAttribute;
	}

	public void setAncientAttributeType(EquipmentAttribute ancientAttribute) {
		this.ancientAttribute = ancientAttribute;
	}

	public int getAncientAttributeValue() {
		return ancientAttributeValue;
	}

	public void setAncientAttributeValue(int ancientAttributeValue) {
		this.ancientAttributeValue = ancientAttributeValue;
	}

	public HashMap<Integer, Integer> getAttributes() {
		HashMap<Integer, Integer> attributes = new HashMap<Integer, Integer>();
		for(int i=0; i < QUALITY_NUMBER; i++) {
			if(equipmentAttributeTypes[i] != null) {
				attributes.put(equipmentAttributeTypes[i].getId(), equipmentAttributeValues[i]);
			}
		}
		return attributes;
	}

	public void setAttributes(Map<Integer, Integer> attributes) {
		this.equipmentAttributeTypes = new EquipmentAttribute[QUALITY_NUMBER];
		this.equipmentAttributeValues = new Integer[QUALITY_NUMBER];
		int i=0;
		for(Integer attribute:attributes.keySet()) {
			if(i >= QUALITY_NUMBER) {
				logger.error("Too many attributes for equipment form {}", this);
			} else {
				this.equipmentAttributeTypes[i] = EquipmentAttribute.fromId(attribute);
				this.equipmentAttributeValues[i] = attributes.get(attribute);
				i++;
			}
		}
	}
	
	public void setAttributeType0(EquipmentAttribute attribute) {
		this.equipmentAttributeTypes[0] = attribute;
	}
	
	public EquipmentAttribute getAttributeType0() {
		return equipmentAttributeTypes[0]==null?EquipmentAttribute.SPARKLES:equipmentAttributeTypes[0];
	}
	
	public void setAttributeType1(EquipmentAttribute attribute) {
		this.equipmentAttributeTypes[1] = attribute;
	}
	
	public EquipmentAttribute getAttributeType1() {
		return equipmentAttributeTypes[1]==null?EquipmentAttribute.SPARKLES:equipmentAttributeTypes[1];
	}
	
	public void setAttributeType2(EquipmentAttribute attribute) {
		this.equipmentAttributeTypes[2] = attribute;
	}
	
	public EquipmentAttribute getAttributeType2() {
		return equipmentAttributeTypes[2]==null?EquipmentAttribute.SPARKLES:equipmentAttributeTypes[2];
	}
	
	public void setAttributeType3(EquipmentAttribute attribute) {
		this.equipmentAttributeTypes[3] = attribute;
	}
	
	public EquipmentAttribute getAttributeType3() {
		return equipmentAttributeTypes[3]==null?EquipmentAttribute.SPARKLES:equipmentAttributeTypes[3];
	}
	
	public void setAttributeType4(EquipmentAttribute attribute) {
		this.equipmentAttributeTypes[4] = attribute;
	}
	
	public EquipmentAttribute getAttributeType4() {
		return equipmentAttributeTypes[4]==null?EquipmentAttribute.SPARKLES:equipmentAttributeTypes[4];
	}
	/*
	public void setAttributeType5(EquipmentAttribute attribute) {
		this.equipmentAttributeTypes[5] = attribute;
	}
	
	public EquipmentAttribute getAttributeType5() {
		return equipmentAttributeTypes[5]==null?EquipmentAttribute.SPARKLES:equipmentAttributeTypes[5];
	}
	
	public void setAttributeType6(EquipmentAttribute attribute) {
		this.equipmentAttributeTypes[6] = attribute;
	}
	
	public EquipmentAttribute getAttributeType6() {
		return equipmentAttributeTypes[6]==null?EquipmentAttribute.SPARKLES:equipmentAttributeTypes[6];
	}
	*/
	
	public void setAttributeValue0(int value) {
		this.equipmentAttributeValues[0] = value;
	}
	
	public int getAttributeValue0() {
		return equipmentAttributeValues[0]==null?0:equipmentAttributeValues[0];
	}
	
	public void setAttributeValue1(int value) {
		this.equipmentAttributeValues[1] = value;
	}
	
	public int getAttributeValue1() {
		return equipmentAttributeValues[1]==null?0:equipmentAttributeValues[1];
	}
	
	public void setAttributeValue2(int value) {
		this.equipmentAttributeValues[2] = value;
	}
	
	public int getAttributeValue2() {
		return equipmentAttributeValues[2]==null?0:equipmentAttributeValues[2];
	}
	
	public void setAttributeValue3(int value) {
		this.equipmentAttributeValues[3] = value;
	}
	
	public int getAttributeValue3() {
		return equipmentAttributeValues[3]==null?0:equipmentAttributeValues[3];
	}
	
	public void setAttributeValue4(int value) {
		this.equipmentAttributeValues[4] = value;
	}
	
	public int getAttributeValue4() {
		return equipmentAttributeValues[4]==null?0:equipmentAttributeValues[4];
	}
	/*
	public void setAttributeValue5(int value) {
		this.equipmentAttributeValues[5] = value;
	}
	
	public int getAttributeValue5() {
		return equipmentAttributeValues[5]==null?0:equipmentAttributeValues[5];
	}
	
	public void setAttributeValue6(int value) {
		this.equipmentAttributeValues[6] = value;
	}
	
	public int getAttributeValue6() {
		return equipmentAttributeValues[6]==null?0:equipmentAttributeValues[6];
	}
	*/
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("EquipmentForm [");
		sb.append("id=");
		sb.append(id);
		sb.append(", quality=");
		sb.append(quality.getNiceName());
		sb.append(", level=");
		sb.append(level);
		sb.append(", hardcore=");
		sb.append(hardcore?"Yes":"No");
		sb.append(", ironborn=");
		sb.append(ironborn?"Yes":"No");
		sb.append(", attributes=[");
		for(int i=0; i < equipmentAttributeTypes.length; i++) {
			sb.append(equipmentAttributeTypes[i]);
			sb.append("=>");
			sb.append(equipmentAttributeValues[i]);
			sb.append(",");
		}		
		sb.append("], equipmentType=");
		sb.append(equipmentType);
		sb.append(", sendToAccountId=");
		sb.append(sendToAccountId);
		sb.append("]]");
		
		return sb.toString();
	}
}

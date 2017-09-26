package org.kerrin.dungeon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.kerrin.dungeon.enums.CharClass;
import org.kerrin.dungeon.enums.DungeonType;
import org.kerrin.dungeon.enums.EquipmentAttribute;
import org.kerrin.dungeon.enums.EquipmentLocation;
import org.kerrin.dungeon.enums.EquipmentQuality;
import org.kerrin.dungeon.enums.EquipmentType;
import org.kerrin.dungeon.exception.EquipmentNotFound;
import org.kerrin.dungeon.forms.EquipmentForm;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.AccountService;
import org.kerrin.dungeon.service.CharacterService;
import org.kerrin.dungeon.service.DungeonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Database record for a users account
 * 
 * @author Kerrin
 *
 */
@Entity
@Table(name="equipment")
public class Equipment extends StashSlotItemSuper implements Comparable<Equipment> {
	@Transient
	@JsonIgnore
	private static final Logger logger = LoggerFactory.getLogger(Equipment.class);

	public static final short CURRENT_VERSION = 1;
	
	private short version = CURRENT_VERSION;

	/** If this equipment needs to be saved, as the version changed, etc */
	private boolean needsSave = false;
	
	@Enumerated(EnumType.ORDINAL)
	private EquipmentQuality quality;
	
	private int level;
	
	/** Is this record for a hardcore character */
	private boolean hardcore;
	
	/** Is this record for an ironborn character */
	private boolean ironborn;

	/** Map attributes to values
	 *	Note this list should be the length equal to the quality level id minus 2, e.g. Epic has 5 entries so is 3
	 */
	@ElementCollection
	@MapKeyColumn(name="type_id")
    @Column(name="value")
	private Map<Integer,Integer> attributes;
	
	/** The base attribute */
	private EquipmentAttribute baseAttribute;
	/** The base attribute value */
	private int baseAttributeValue;
	
	/** The defence attribute, if the quality is at least good enough for two attributes */
	private EquipmentAttribute defenceAttribute;
	/** The defence attribute value */
	private int defenceAttributeValue;
	
	/** If the item is ancient, then it gets an additional attribute that is double as powerful */
	private EquipmentAttribute ancientAttribute;
	private int ancientAttributeValue;

	/**
	 * What equipment type is this
	 */
	private EquipmentType equipmentType;
	
	/** Where is the equipment stored */
	private EquipmentLocation equipmentLocation;
	/** 
	 * What is the id of the location
	 * 	CHARACTER 	=> character id
	 *	INVENTORY 	=> inventory slot id
	 *	DUNGEON 	=> dungeon id
	 */
	private long equipmentLocationId;
	
	protected Equipment() {}

	public Equipment(long id, EquipmentType equipmentType, EquipmentQuality quality, int level, 
			boolean hardcore, boolean ironborn,
			EquipmentAttribute baseAttribute, int baseAttributeValue, 
			EquipmentAttribute defenceAttribute, int defenceAttributeValue, 
			Map<Integer, Integer> attributes, EquipmentAttribute ancientAttribute, int ancientAttributeValue, 
			EquipmentLocation equipmentLocation, long equipmentLocationId) {
		this(CURRENT_VERSION, id, equipmentType, quality, level, hardcore, ironborn, 
				baseAttribute, baseAttributeValue, defenceAttribute, defenceAttributeValue, 
				attributes, 
				ancientAttribute, ancientAttributeValue, 
				equipmentLocation, equipmentLocationId);
	}
	public Equipment(short version, long id, EquipmentType equipmentType, EquipmentQuality quality, int level, 
			boolean hardcore, boolean ironborn,
			EquipmentAttribute baseAttribute, int baseAttributeValue, 
			EquipmentAttribute defenceAttribute, int defenceAttributeValue, 
			Map<Integer, Integer> attributes, EquipmentAttribute ancientAttribute, int ancientAttributeValue, 
			EquipmentLocation equipmentLocation, long equipmentLocationId) {
		super(id, TYPE.EQUIPMENT);
		logger.trace("Equipment");
		this.version = version;
		this.equipmentType = equipmentType;
		this.quality = quality;
		this.level = level;
		this.hardcore = hardcore;
		this.ironborn = ironborn;
		// Set the base, defence and ancient attributes before the general ones, as setting those may change them
		// if the version has changed
		this.baseAttribute = baseAttribute;
		this.baseAttributeValue = baseAttributeValue;
		this.defenceAttribute = defenceAttribute;
		this.defenceAttributeValue = defenceAttributeValue;
		this.ancientAttribute = ancientAttribute;
		this.ancientAttributeValue = ancientAttributeValue;
		this.equipmentLocation = equipmentLocation;
		this.equipmentLocationId = equipmentLocationId;
		// Clean the attributes (i.e. remove the sparkles) and up the version
		this.needsSave = setAttributes(version, attributes);
	}
	
	public Equipment(EquipmentForm equipmentForm) {
		super(equipmentForm.getId(), TYPE.EQUIPMENT);
		this.equipmentType = equipmentForm.getEquipmentType();
		this.quality = equipmentForm.getQuality();
		this.level = equipmentForm.getLevel();
		this.attributes = equipmentForm.getAttributes();
		this.equipmentLocation = EquipmentLocation.NONE;
	}

	/**
	 * Copy constructor
	 * @param equipment
	 */
	public Equipment(Equipment equipment) {
		super(equipment.getId(), TYPE.EQUIPMENT);
		this.equipmentType = equipment.getEquipmentType();
		this.quality = equipment.getQuality();
		this.level = equipment.getLevel();
		this.baseAttribute = equipment.getBaseAttribute();
		this.baseAttributeValue = equipment.getBaseAttributeValue();
		this.defenceAttribute = equipment.getDefenceAttribute();
		this.defenceAttributeValue = equipment.getDefenceAttributeValue();
		this.attributes = equipment.getAttributes(false);
		this.ancientAttribute = equipment.getAncientAttribute();
		this.ancientAttributeValue = equipment.getAncientAttributeValue();
		this.equipmentLocation = equipment.getEquipmentLocation();
		this.equipmentLocationId = equipment.getEquipmentLocationId();
	}

	@Override
	public boolean isEquipment() {
		return true;
	}

	public short getVersion() {
		return version;
	}

	public void setVersion(short version) {
		this.version = version;
	}

	public boolean isNeedsSave() {
		return needsSave;
	}

	public void setNeedsSave(boolean needsSave) {
		this.needsSave = needsSave;
	}

	public EquipmentType getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
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
	
	/**
	 * Gets the level a character can use this, may be lower than the actual level if it has the level reduction attribute
	 * 
	 * @return
	 */
	public int getRequiredLevel() {
		return level - getAttributeValue(EquipmentAttribute.REDUCE_LEVEL);
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public boolean isHardcore() {
		return hardcore;
	}

	@Override
	public void setHardcore(boolean hardcore) {
		this.hardcore = hardcore;
	}

	@Override
	public boolean isIronborn() {
		return ironborn;
	}

	@Override
	public void setIronborn(boolean ironborn) {
		this.ironborn = ironborn;
	}
	
	public Account getAccount(
			CharacterService characterService, 
			DungeonService dungeonService, 
			AccountMessageService accountMessageService,
			AccountService accountService) {
		switch(getEquipmentLocation()) {
		case CHARACTER:
			Character character = characterService.findById(getEquipmentLocationId());
			if(character == null) return null;
			return character.getAccount();
		case DUNGEON:
			Dungeon dungeon = dungeonService.findById(getEquipmentLocationId());
			if(dungeon == null) return null;
			return dungeon.getAccount();
		case INVENTORY:
			return accountService.findById(getEquipmentLocationId());
		case MESSAGE:
			AccountMessage message = accountMessageService.findById(getEquipmentLocationId());
			if(message == null) return null;
			return accountService.findById(message.getAccountId());
		default:
			return null;
		}
	}

	/**
	 * 
	 * @param includeBasedDefenceAncient	Add the base, defence and ancient attributes to the list
	 * @return
	 */
	public Map<Integer, Integer> getAttributes(boolean includeBasedDefenceAncient) {
		Map<Integer,Integer> combinedAttributes = new HashMap<Integer, Integer>(attributes);
		if(includeBasedDefenceAncient) {
			if(quality.getId() >= EquipmentQuality.BROKEN.getId()) {
				if(baseAttribute == null) {
					logger.error("Base stat not set on {} quality equipment id {}", quality.getNiceName(), id);
					// Incase this gets saved, fix it
					baseAttribute = EquipmentAttribute.getRandomBase();
					baseAttributeValue = getRandomAttributeValue(level, baseAttribute, false);
					logger.info("Created new base attribute {} = {}", baseAttribute, baseAttributeValue);
				} else {
					combinedAttributes.put(baseAttribute.getId(), baseAttributeValue);
				}
			}
			if(quality.getId() >= EquipmentQuality.INFERIOR.getId()) {
				if(defenceAttribute == null) {
					logger.error("Defence stat not set on {} quality equipment id {}", quality.getNiceName(), id);
					// Incase this gets saved, fix it
					defenceAttribute = EquipmentAttribute.getRandomBase();
					defenceAttributeValue = getRandomAttributeValue(level, defenceAttribute, false);
					logger.info("Created new defence attribute {} = {}", defenceAttribute, defenceAttributeValue);
				} else {
					combinedAttributes.put(defenceAttribute.getId(), defenceAttributeValue);
				}
			}
			if(quality.getId() >= EquipmentQuality.ARTIFACT.getId()) {
				if(ancientAttribute == null) {
					logger.error("Base stat not set on {} quality equipment id {}", quality.getNiceName(), id);
					// Incase this gets saved, fix it
					ancientAttribute = EquipmentAttribute.getRandomBase();
					ancientAttributeValue = getRandomAttributeValue(level, ancientAttribute, false);
					logger.info("Created new ancient attribute {} = {}", ancientAttribute, ancientAttributeValue);
				} else {
					combinedAttributes.put(ancientAttribute.getId(), ancientAttributeValue);
				}
			}
		}
		return combinedAttributes;
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

	/**
	 * Add the list of attributes <EquipmentAttribute,value>
	 * @param version 
	 * @param attributes
	 * 
	 * @return needs save
	 */
	public boolean setAttributes(short version, Map<Integer, Integer> attributes) {
		if(attributes != null) {
			// We never want sparkles
			attributes.remove(EquipmentAttribute.SPARKLES.getId());
		}
		boolean needsSave = false;
		if(version == 0) {
			logger.info("Updating version from 0 for equipment {}", this);
			needsSave = true;

			Iterator<Integer> iter = attributes.keySet().iterator();
			while((baseAttribute == null || defenceAttribute == null) && iter.hasNext()) {
				Integer attributeId = iter.next();
				EquipmentAttribute thisAttribute = EquipmentAttribute.fromId(attributeId);
				if(thisAttribute.isBase() && baseAttribute == null) {
					baseAttribute = thisAttribute;
					baseAttributeValue = attributes.get(attributeId);
					iter.remove();
				} else if(thisAttribute.isDefence() && defenceAttribute == null) {
					defenceAttribute = thisAttribute;
					defenceAttributeValue = attributes.get(attributeId);
					iter.remove();
				}
			}
			if((baseAttribute == null || baseAttributeValue == 0) && 
					quality.getId() >= EquipmentQuality.BROKEN.getId()) {
				baseAttribute = EquipmentAttribute.getRandomBase();
				baseAttributeValue = getRandomAttributeValue(level, baseAttribute, false);
				// Remove an element to make up for the addition of the base attribute
				iter = attributes.keySet().iterator();
				if(iter.hasNext())
				{
					attributes.remove(iter.next());
				}
			}
			if((defenceAttribute == null || defenceAttributeValue == 0) && 
					quality.getId() >= EquipmentQuality.INFERIOR.getId()) {
				defenceAttribute = EquipmentAttribute.getRandomDefence();
				defenceAttributeValue = getRandomAttributeValue(level, defenceAttribute, false);
				// Remove an element to make up for the addition of the defence attribute
				iter = attributes.keySet().iterator();
				if(iter.hasNext())
				{
					attributes.remove(iter.next());
				}
			}
			logger.info("Updated version to 1 for equipment {}", this);
		}
		
		// Now some error checking
		List<EquipmentAttribute> otherAttributes = new ArrayList<EquipmentAttribute>();
		if((baseAttribute == null || baseAttributeValue == 0) && 
				quality.getId() >= EquipmentQuality.BROKEN.getId()) {
			baseAttribute = EquipmentAttribute.getRandomBase();
			baseAttributeValue = getRandomAttributeValue(level, baseAttribute, false);
			logger.error("Base attribute missing or 0, rerolled as {} = {}", baseAttribute, baseAttributeValue);
		}
		if(baseAttribute != null) otherAttributes.add(baseAttribute);
		if((defenceAttribute == null || defenceAttributeValue == 0) && 
				quality.getId() >= EquipmentQuality.INFERIOR.getId()) {
			defenceAttribute = EquipmentAttribute.getRandomDefence();
			defenceAttributeValue = getRandomAttributeValue(level, defenceAttribute, false);
			logger.error("Defence attribute missing or 0, rerolled as {} = {}", defenceAttribute, defenceAttributeValue);
		}
		if(defenceAttribute != null) otherAttributes.add(defenceAttribute);
		if((ancientAttribute == null || ancientAttributeValue == 0) && 
				quality.getId() >= EquipmentQuality.ARTIFACT.getId()) {
			ancientAttribute = getRandomAttribute(attributes, otherAttributes);
			while(attributes.containsKey(ancientAttribute)) {
				ancientAttribute = EquipmentAttribute.getRandom(null);
			}
			ancientAttributeValue = getRandomAttributeValue(level, ancientAttribute, false);
			logger.error("Ancient attribute missing or 0, rerolled as {} = {}", ancientAttribute, ancientAttributeValue);
		}		
		if(ancientAttribute != null) otherAttributes.add(ancientAttribute);
		
		// Check we have enough attributes
		while(attributes.size() > 0 && attributes.size() < quality.getId() - 2) {
			EquipmentAttribute attribute = getRandomAttribute(attributes, otherAttributes);
			attributes.put(attribute.getId(), getRandomAttributeValue(level, attribute, false /*Lets be nice and not curse it*/));
			logger.error("Had to add another attribute {} to equipment {}", attribute, this);
		}
		// Now check for having too many attributes
		Iterator<Integer> iter = attributes.keySet().iterator();
		while(attributes.size() > 0 && attributes.size() > quality.getId() - 2) {
			iter.next();
			logger.error("Had to remove attribute {} from equipment {}", iter, this);
			iter.remove();
		}
		
		this.attributes = attributes;
		return needsSave;
	}

	/**
	 * Get the attribute value, or 0 if not an attribute
	 * 
	 * @param attribute
	 * @return
	 */
	public int getAttributeValue(EquipmentAttribute attribute) {
		if(attribute.equals(baseAttribute)) {
			return baseAttributeValue;
		}
		if(attribute.equals(defenceAttribute)) {
			return defenceAttributeValue;
		}
		if(attribute.equals(ancientAttribute)) {
			return ancientAttributeValue;
		}
		Integer value = attributes.get(attribute.getId());
		return value==null?0:value;
	}

	public EquipmentAttribute getBaseAttribute() {
		return baseAttribute;
	}

	public void setBaseAttribute(EquipmentAttribute baseAttribute) {
		this.baseAttribute = baseAttribute;
	}

	public int getBaseAttributeValue() {
		return baseAttributeValue;
	}

	public void setBaseAttributeValue(int baseAttributeValue) {
		this.baseAttributeValue = baseAttributeValue;
	}

	public EquipmentAttribute getDefenceAttribute() {
		return defenceAttribute;
	}

	public void setDefenceAttribute(EquipmentAttribute defenceAttribute) {
		this.defenceAttribute = defenceAttribute;
	}

	public int getDefenceAttributeValue() {
		return defenceAttributeValue;
	}

	public void setDefenceAttributeValue(int defenceAttributeValue) {
		this.defenceAttributeValue = defenceAttributeValue;
	}

	public EquipmentAttribute getAncientAttribute() {
		return ancientAttribute;
	}

	public void setAncientAttribute(EquipmentAttribute ancientAttribute) {
		this.ancientAttribute = ancientAttribute;
	}

	public int getAncientAttributeValue() {
		return ancientAttributeValue;
	}

	public void setAncientAttributeValue(int ancientAttributeValue) {
		this.ancientAttributeValue = ancientAttributeValue;
	}
	
	/**
	 * Increase the equipment quality, it is does increase, the extra attributes are rolled
	 * 
	 * @param rollChance	Chance in 1000 of the increase
	 * 
	 * @return	Increased quality
	 */
	public boolean improveQuality(int rollChance) {
		int chance = 1000;
		int random = (int) (Math.random()*chance);
		EquipmentQuality previousQuality = quality;
		EquipmentQuality nextQuality = quality;
		nextQuality = nextQuality.increase();
		if(random < rollChance) {
			logger.debug("Increasing quality from {}", previousQuality);
			quality = nextQuality;
			List<EquipmentAttribute> otherAttributes = new ArrayList<EquipmentAttribute>();
			if(baseAttribute != null) otherAttributes.add(baseAttribute);
			if(defenceAttribute != null) otherAttributes.add(defenceAttribute);
			switch (quality) {
			case BROKEN:
				logger.debug("Adding base attribute");
				baseAttribute = EquipmentAttribute.getRandomBase();
				baseAttributeValue = getRandomAttributeValue(level, baseAttribute, false);
				otherAttributes.add(baseAttribute);
				break;
			case INFERIOR:
				logger.debug("Adding defence attribute");
				defenceAttribute = EquipmentAttribute.getRandomDefence();
				defenceAttributeValue = getRandomAttributeValue(level, defenceAttribute, false);
				otherAttributes.add(defenceAttribute);
				break;
			case ARTIFACT:
				logger.debug("Adding artifact attribute");
				ancientAttribute = EquipmentAttribute.SPARKLES;
				while(ancientAttribute.isCantBeAncient()) {
					ancientAttribute = getRandomAttribute(attributes, otherAttributes);
				}
				ancientAttributeValue = getRandomAttributeValue(level, ancientAttribute, false) * 2;
				otherAttributes.add(ancientAttribute);
				// No break, as it also gains a normal attribute too
			case COMMON: case SUPERIOR: case EPIC: case LEGENDARY: 
				logger.debug("Adding generic attribute");
				EquipmentAttribute attribute = getRandomAttribute(attributes, otherAttributes);
				boolean isCursed = ((int)(Math.random()*100) < attribute.getCursedChance());
				attributes.put(attribute.getId(), getRandomAttributeValue(level, attribute, isCursed));
				break;
			default:
				return false;
			}
			return true;
		} else {
			logger.debug("Quality increase failed ("+random+" of "+rollChance+")");
		}
		return false;
	}

	/**
	 * Calculate how much this item is worth to salvage
	 * 
	 * @return
	 */
	public int getSalvageValue() {
		int salvageValue = level - 1;
		salvageValue /= 2;
		
		int adjustValue;
		switch (quality) {
		case USELESS:
			salvageValue = 1;
		case BROKEN:
			adjustValue = (level/2);
			salvageValue -= (adjustValue<3?3:adjustValue);
			break;
		case INFERIOR:
			adjustValue = (level/4);
			salvageValue -= (adjustValue<2?2:adjustValue);
			break;
		case COMMON:
			adjustValue = (level/8);
			salvageValue -= (adjustValue<1?1:adjustValue);
			break;
		case SUPERIOR:
			adjustValue = (level/16);
			salvageValue -= (adjustValue<0?0:adjustValue);
			break;
		case EPIC:
			adjustValue = (level/16);
			salvageValue += (adjustValue<0?0:adjustValue);
			break;
		case LEGENDARY:
			adjustValue = (level/8);
			salvageValue += (adjustValue<1?1:adjustValue);
			break;
		case ARTIFACT:
			adjustValue = (level/4);
			salvageValue += (adjustValue<2?2:adjustValue);
			break;
		
		}
		if(salvageValue < 1) salvageValue = 1;
		if(salvageValue > 90) salvageValue = 75;
		return salvageValue;
	}

	public List<CharClass> getRecommendedClasses() {
		List<CharClass> recommendedClasses = new ArrayList<CharClass>();
		EquipmentAttribute highestPrimary = null;
		int highestPrimaryValue = 0;
		EquipmentAttribute highestDefensive = null;
		int highestDefenceValue = 0;
		Map<Integer, Integer> allAttributes = getAttributes(true);
		for(Integer attributeId:allAttributes.keySet()) {
			EquipmentAttribute attribute = EquipmentAttribute.fromId(attributeId);
			if(attribute.isBase()) {
				if(highestPrimaryValue < allAttributes.get(attributeId)) {
					highestPrimary = attribute;
					highestPrimaryValue = allAttributes.get(attributeId);
				}
			}
			if(attribute.isDefence()) {
				if(highestDefenceValue < allAttributes.get(attributeId)) {
					highestDefensive = attribute;
					highestDefenceValue = allAttributes.get(attributeId);
				}				
			}
		}
		for(CharClass charClass:CharClass.values()) {
			if(charClass == CharClass.ANY) continue;
			if((highestPrimary == null || charClass.getMainAttribute() == highestPrimary) &&
					(highestDefensive == null || highestDefensive == EquipmentAttribute.ARMOUR ||
						charClass.getDefenceAttribute() == highestDefensive)
					) {
				recommendedClasses.add(charClass);
			}
		}
		return recommendedClasses;
	}

	/**
	 * Create a new random equipment at the requested level
	 * 
	 * @param level
	 * @return
	 */
	public static Equipment createRandom(DungeonType dungeonType, int level,  boolean hardcore, boolean ironborn,
			EquipmentLocation equipmentLocation, long equipmentLocationId) {
		return createRandom(dungeonType, level, hardcore, ironborn, equipmentLocation, equipmentLocationId, false);
	}
	
	/**
	 * Create a new random equipment at the requested level
	 * 
	 * @param dungeonType
	 * @param level
	 * @param canCurse	Can item have cursed attributes
	 * @return
	 * @throws EquipmentNotFound 
	 */
	public static Equipment createRandom(DungeonType dungeonType, int level, boolean hardcore, boolean ironborn,
			EquipmentLocation equipmentLocation, long equipmentLocationId, boolean canCurse) {
		EquipmentType equipmentType = EquipmentType.getRandomType();
		EquipmentQuality quality = EquipmentQuality.getRandomReward(dungeonType);
		try {
			return createRandom(level, hardcore, ironborn, equipmentLocation, equipmentLocationId, canCurse, 
					equipmentType, quality, new ArrayList<EquipmentAttribute>(), dungeonType);
		} catch (EquipmentNotFound e) {
			logger.error("Impossible error: {}", e.getMessage());
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public static StashSlotItemSuper createRandom(EquipmentType equipmentType, EquipmentQuality quality, int level, 
			boolean hardcore, boolean ironborn, List<EquipmentAttribute> requiredGoodAttributes) 
					throws EquipmentNotFound {
		return createRandom(level, hardcore, ironborn, null, -1, false, equipmentType, quality, requiredGoodAttributes, null);
	}
	
	public static Equipment createRandom(int level, boolean hardcore, boolean ironborn,
			EquipmentLocation equipmentLocation, long equipmentLocationId, boolean canCurse, 
			EquipmentType equipmentType, EquipmentQuality quality, List<EquipmentAttribute> requiredGoodAttributes, 
			DungeonType dungeonType) 
					throws EquipmentNotFound {
		int realLevel = level > Character.MAX_LEVEL?Character.MAX_LEVEL:level;
		EquipmentQuality realQuality = quality;
		EquipmentQuality nextQuality = quality;
		nextQuality = nextQuality.increase();
		if(realLevel < level) {
			int qualityIncrease = (int)(Math.random()*(level-realLevel));
			int chance = 1000;
			while(qualityIncrease > 0) {
				int random = (int) (Math.random()*chance);
				if(random < nextQuality.getRewardRollChance(dungeonType)) {
					logger.debug("Increasing quality from {}", realQuality);
					quality = nextQuality;
					realQuality = nextQuality;
					nextQuality = nextQuality.increase();
				}
				qualityIncrease--;
			}
		}
		if(requiredGoodAttributes.size() > realQuality.getId()) {
			throw new EquipmentNotFound();
		}
		Map<Integer, Integer> attributes = new HashMap<Integer, Integer>();
		List<EquipmentAttribute> otherAttributes = new ArrayList<EquipmentAttribute>();
		
		// Make sure the base attribute is good and set
		EquipmentAttribute baseAttribute = EquipmentAttribute.getRandomBase(requiredGoodAttributes);
		int baseAttributeValue = getRandomAttributeValue(realLevel, baseAttribute, false);
		otherAttributes.add(baseAttribute);
		
		// Make sure the defence attribute is good and set
		EquipmentAttribute defenceAttribute = null;
		int defenceAttributeValue = 0;
		if(quality.getId() >= EquipmentQuality.INFERIOR.getId()) {
			defenceAttribute = EquipmentAttribute.getRandomDefence(requiredGoodAttributes);
			defenceAttributeValue = getRandomAttributeValue(realLevel, defenceAttribute, false);
			otherAttributes.add(defenceAttribute);
		}
		
		for(EquipmentAttribute requiredAttribute:requiredGoodAttributes) {
			if(requiredAttribute.equals(baseAttribute) || requiredAttribute.equals(defenceAttribute)) {
				// Already have this attribute
				continue;
			}
			attributes.put(requiredAttribute.getId(), getRandomAttributeValue(realLevel, requiredAttribute, false));
		}

		for(int attributeIndex=attributes.size()+2; attributeIndex < realQuality.getId(); attributeIndex++) {
			EquipmentAttribute attribute = getRandomAttribute(attributes, otherAttributes);
			boolean isCursed = false;
			if(canCurse) {
				isCursed = ((int)(Math.random()*100) < attribute.getCursedChance());
			}
			attributes.put(attribute.getId(), getRandomAttributeValue(realLevel, attribute, isCursed));
		}
		EquipmentAttribute ancientAttribute = null;
		int ancientAttributeValue = 0;
		if(realQuality.equals(EquipmentQuality.ARTIFACT)) {
			ancientAttribute = EquipmentAttribute.SPARKLES;
			while(ancientAttribute.isCantBeAncient()) {
				ancientAttribute = getRandomAttribute(attributes, otherAttributes);
			}
			ancientAttributeValue = getRandomAttributeValue(realLevel, ancientAttribute, false) * 2;
		}
		Equipment equipment = new Equipment(-1, equipmentType, realQuality, realLevel, hardcore, ironborn, 
				baseAttribute, baseAttributeValue, defenceAttribute, defenceAttributeValue, 
				attributes, ancientAttribute, ancientAttributeValue, equipmentLocation, equipmentLocationId);
		return equipment;
	}
	
	/**
	 * Gets a random attribute not already on item
	 * 
	 * @param attributes
	 * @param otherAttributes
	 * @return
	 */
	private static EquipmentAttribute getRandomAttribute(Map<Integer, Integer> attributes, List<EquipmentAttribute> otherAttributes) {	
		return getRandomAttribute(attributes, otherAttributes, null);
	}
	
	/**
	 * Gets a random attribute not already on item
	 * 
	 * @param attributes
	 * @param otherAttributes
	 * @param equipmentType
	 * @return
	 */
	private static EquipmentAttribute getRandomAttribute(Map<Integer, Integer> attributes, List<EquipmentAttribute> otherAttributes, EquipmentType equipmentType) {
		EquipmentAttribute attribute = EquipmentAttribute.getRandom(equipmentType);
		// No duplicate attributes
		while(attributes.containsKey(attribute.getId()) || otherAttributes.contains(attribute)) {
			attribute = EquipmentAttribute.getRandom(equipmentType);
		}
		return attribute;
	}
	
	/**
	 * Get an attribute value
	 * 
	 * @param level		Attribute level
	 * @param attribute	Attribute type
	 * @param cursed	Is it a cursed attribute
	 * @return
	 */
	public static int getRandomAttributeValue(int level, EquipmentAttribute attribute, boolean cursed) {
		return getRandomAttributeValue(level, attribute, cursed, 1);
	}
	public static int getRandomAttributeValue(int level, EquipmentAttribute attribute, boolean cursed, int minRoll) {
		if(minRoll < 1) {
			logger.error("Minimum roll was less than 1 for level "+ level + ", attribute: " + attribute);
			minRoll = 1;
		}
		int value = 0;
		switch(attribute.getAttributeValueMaxType()) {
		case ONE_HUNDRED:
			value=minRoll+(int)(Math.random()*(100-minRoll+1));
			break;
		case LEVEL:
			value=minRoll+(int)(Math.random()*(level-minRoll+1));
			break;
		case SLOTS:
			value=minRoll+(int)(Math.random()*(10/*CharSlot.values().length*/-minRoll+1));
			break;
		case THREE:
			value=minRoll+(int)(Math.random()*(3-minRoll+1));
			break;
		}
		
		if(cursed) value = -value;
		
		return value;
	}

	public boolean isAttributeCursed(int attributeTypeId) {
		if(attributes.containsKey(attributeTypeId)) {
			return attributes.get(attributeTypeId) <= 0;
		} else {
			return false;
		}
	}

	public void rerollAttribute(int attributeTypeId) {
		if(attributes.containsKey(attributeTypeId)) {
			attributes.remove(attributeTypeId);
			List<EquipmentAttribute> otherAttributes = new ArrayList<EquipmentAttribute>();
			otherAttributes.add(baseAttribute);
			otherAttributes.add(defenceAttribute);
			otherAttributes.add(ancientAttribute);
			EquipmentAttribute newAttribute = getRandomAttribute(attributes, otherAttributes);
			boolean isCursed = ((int)(Math.random()*100) < newAttribute.getCursedChance());
			attributes.put(newAttribute.getId(), getRandomAttributeValue(level, newAttribute, isCursed));
		}
	}

	/**
	 * Change the cursed attribute in to a positive attribute
	 * 
	 * @param attributeTypeId
	 * 
	 * @return Success
	 */
	public boolean removeCurse(int attributeTypeId) {
		if(attributes.containsKey(attributeTypeId)) {
			int value = attributes.get(attributeTypeId);
			if(value < 0) {
				attributes.put(attributeTypeId, -value);
				return true;
			}
		}
		return false;
	}

	public void rerollBaseAttribute() {
		List<EquipmentAttribute> otherAttributes = new ArrayList<EquipmentAttribute>();
		otherAttributes.add(defenceAttribute);
		otherAttributes.add(ancientAttribute);
		EquipmentAttribute newAttribute = EquipmentAttribute.getRandomBase();
		baseAttribute = newAttribute;
		baseAttributeValue = getRandomAttributeValue(level, newAttribute, false);
	}

	public void rerollDefenceAttribute() {
		List<EquipmentAttribute> otherAttributes = new ArrayList<EquipmentAttribute>();
		otherAttributes.add(baseAttribute);
		otherAttributes.add(ancientAttribute);
		EquipmentAttribute newAttribute = EquipmentAttribute.getRandomDefence();
		defenceAttribute = newAttribute;
		defenceAttributeValue = getRandomAttributeValue(level, newAttribute, false);
	}
	
	/**
	 * Reroll the attribute range
	 * @param attributeTypeId
	 * @param improveOnly
	 * @return Success
	 */
	public boolean rerollAttributeRange(int attributeTypeId, boolean improveOnly) {
		EquipmentAttribute attribute = EquipmentAttribute.fromId(attributeTypeId);
		if(attribute != null) {
			if(improveOnly) {
				int currentValue = attributes.get(attributeTypeId);
				if(EquipmentAttribute.fromId(attributeTypeId).isMaxValue(level, currentValue)) {
					return false;
				}
				attributes.put(attributeTypeId, getRandomAttributeValue(level, attribute, false, currentValue));
				return true;
			} else {
				boolean isCursed = ((int)(Math.random()*100) < attribute.getCursedChance());
				attributes.put(attributeTypeId, getRandomAttributeValue(level, attribute, isCursed));
				return true;
			}
		}
		return false;
	}
	
	public boolean rerollBaseAttributeRange(boolean improveOnly) {
		if(improveOnly) {
			if(baseAttribute.isMaxValue(level, baseAttributeValue)) {
				return false;
			}
			baseAttributeValue = getRandomAttributeValue(level, baseAttribute, false, baseAttributeValue);
			return true;
		} else {
			baseAttributeValue = getRandomAttributeValue(level, baseAttribute, false);
			return true;
		}
	}
	
	public boolean rerollDefenceAttributeRange(boolean improveOnly) {
		if(improveOnly) {
			if(defenceAttribute.isMaxValue(level, defenceAttributeValue)) {
				return false;
			}
			defenceAttributeValue = getRandomAttributeValue(level, defenceAttribute, false, defenceAttributeValue);
			return true;
		} else {
			defenceAttributeValue = getRandomAttributeValue(level, defenceAttribute, false);
			return true;
		}
	}

	public void checkForRerollReduceLevel() {
		int levelReduction = getAttributeValue(EquipmentAttribute.REDUCE_LEVEL);
		while(levelReduction > 0) {
			rerollAttribute(EquipmentAttribute.REDUCE_LEVEL.getId());
			levelReduction = getAttributeValue(EquipmentAttribute.REDUCE_LEVEL);
		}
	}

	/**
	 * Generate the token to insert in to a message to include this in the message as an 'attachment'
	 * 
	 * @return
	 */
	public CharSequence asMessageToken() {
		StringBuilder sb = new StringBuilder();
		sb.append("<ITEM=");
		sb.append(id);
		sb.append(">");
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Equipment other = (Equipment) obj;
		if (id != other.id)
			return false;
		if(id == -1) {
			// New equipment, so check the other fields instead
			if(!equipmentType.equals(other.equipmentType) ||
					!quality.equals(other.quality) ||
					hardcore != other.hardcore ||
					ironborn != other.ironborn ||
					level != other.level) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int compareTo(Equipment compareTo)
	{
		// Level 1 to 60
		// Quality 0 to 7
		// Equipment Type 0 to 13
	    return ((level - compareTo.level)*200)+
	    		((quality.getId()-compareTo.quality.getId())*20)+
	    		(equipmentType.getId()-compareTo.equipmentType.getId());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Equipment [");
		sb.append("id=");
		sb.append(id);
		sb.append(",version=");
		sb.append(version);
		sb.append(",equipmentType=");
		sb.append(equipmentType==null?"No type":equipmentType.getName());
		sb.append(", quality=");
		sb.append(quality);
		sb.append(", level=");
		sb.append(level);
		sb.append(", location=");
		sb.append(equipmentLocation==null?"No Location":equipmentLocation.name());
		sb.append(", locationId=");
		sb.append(equipmentLocationId);
		sb.append(", baseAttribute=");
		sb.append(baseAttribute);
		sb.append(", baseAttributeValue=");
		sb.append(baseAttributeValue);
		sb.append(", defenceAttribute=");
		sb.append(defenceAttribute);
		sb.append(", defenceAttributeValue=");
		sb.append(defenceAttributeValue);
		sb.append(", ancientAttribute=");
		sb.append(ancientAttribute);
		sb.append(", ancientAttributeValue=");
		sb.append(ancientAttributeValue);
		sb.append(", attributes=[");
		if(attributes != null) {
			for(Integer attribute:attributes.keySet()) {
				sb.append(EquipmentAttribute.fromId(attribute));
				sb.append("=>");
				sb.append(attributes.get(attribute));
				sb.append(",");
			}
		} else {
			sb.append("null");
		}
		sb.append("]]");
		return sb.toString();
	}
	
	public String toStringNoAttributes() {
		StringBuilder sb = new StringBuilder("Equipment [");
		sb.append("id=");
		sb.append(id);
		sb.append(",version=");
		sb.append(version);
		sb.append(",equipmentType=");
		sb.append(equipmentType.getName());
		sb.append(", quality=");
		sb.append(quality);
		sb.append(", level=");
		sb.append(level);
		sb.append(", location=");
		sb.append(equipmentLocation.name());
		sb.append(", locationId=");
		sb.append(equipmentLocationId);
		sb.append("]");
		return sb.toString();
	}
}
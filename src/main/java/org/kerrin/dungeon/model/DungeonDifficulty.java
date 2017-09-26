package org.kerrin.dungeon.model;

public class DungeonDifficulty {
	protected int difficultyModifier = 0;
	protected int cost = 0;
	protected int levelAdjustment = 0;
	protected long difficultyScore = 0;

	public int getDifficultyModifier() {
		return difficultyModifier;
	}
	public void setDifficultyModifier(int difficultyModifier) {
		this.difficultyModifier = difficultyModifier;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public int getLevelAdjustment() {
		return levelAdjustment;
	}
	public void setLevelAdjustment(int levelAdjustment) {
		this.levelAdjustment = levelAdjustment;
	}
	public long getDifficultyScore() {
		return difficultyScore;
	}
	public void setDifficultyScore(long difficultyScore) {
		this.difficultyScore = difficultyScore;
	}
}

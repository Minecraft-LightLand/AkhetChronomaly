package dev.xkmc.akhet_chronomaly.content.core.item;

import dev.xkmc.akhet_chronomaly.init.data.ACModConfig;

public class ArtifactUpgradeManager {

	public static int getExpForLevel(int rank, int level) {
		double rank_factor = ACModConfig.SERVER.expConsumptionRankFactor.get();
		double level_factor = ACModConfig.SERVER.expLevelFactor.get();
		double base = ACModConfig.SERVER.baseExpConsumption.get();
		return (int) Math.round(base * Math.pow(level_factor, level) * Math.pow(rank_factor, rank - 1));
	}

	public static int getMaxLevel(int rank) {
		return rank * ACModConfig.SERVER.maxLevelPerRank.get();
	}

}

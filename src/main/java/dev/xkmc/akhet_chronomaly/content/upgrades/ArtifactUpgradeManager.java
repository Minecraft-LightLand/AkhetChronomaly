package dev.xkmc.akhet_chronomaly.content.upgrades;

import dev.xkmc.akhet_chronomaly.content.core.ArtifactStats;
import dev.xkmc.akhet_chronomaly.init.data.ACModConfig;

import javax.annotation.Nullable;

public class ArtifactUpgradeManager {

	public static int getExpForLevel(int rank, int level) {
		double rank_factor = ACModConfig.SERVER.expConsumptionRankFactor.get();
		double level_factor = ACModConfig.SERVER.expLevelFactor.get();
		double base = ACModConfig.SERVER.baseExpConsumption.get();
		return (int) Math.round(base * Math.pow(level_factor, level) * Math.pow(rank_factor, rank - 1));
	}

	public static int getExpForConversion(int rank, @Nullable ArtifactStats stat) {
		int base = ACModConfig.SERVER.baseExpConversion.get();
		double base_factor = ACModConfig.SERVER.expConversionRankFactor.get();
		double retention = ACModConfig.SERVER.expRetention.get();
		double base_exp = base * Math.pow(base_factor, rank - 1);
		if (stat == null) {
			return (int) Math.round(base_exp);
		}
		double used_exp = stat.exp();
		for (int i = 0; i < stat.level(); i++) {
			used_exp += getExpForLevel(stat.rank(), i);
		}
		return (int) Math.round(base_exp + used_exp * retention);
	}

	public static int getMaxLevel(int rank) {
		return rank * ACModConfig.SERVER.maxLevelPerRank.get();
	}

}

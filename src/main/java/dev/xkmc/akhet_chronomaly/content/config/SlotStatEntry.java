package dev.xkmc.akhet_chronomaly.content.config;

public record SlotStatEntry(int weight, double scale) {

	public double getScale(SlotStatMap map, int scaleRank) {
		return map.overallScale() * scaleRank * (2 + scaleRank) * 0.5;//TODO
	}

}

package dev.xkmc.akhet_chronomaly.content.config;

import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;

public record SlotStatMap(
		double overallScale, int refineChance, boolean mayReforge, boolean mayAscend,
		LinkedHashMap<ResourceLocation, SlotStatEntry> map
) {
}

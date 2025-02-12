package dev.xkmc.akhet_chronomaly.content.core.data;

import dev.xkmc.akhet_chronomaly.content.core.bonus.BonusHolder;
import dev.xkmc.akhet_chronomaly.content.core.bonus.BonusModifier;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

@SerialClass
public class AkhetSetData {

	@SerialField
	int[] flags;

	@SerialField
	private final Map<String, BonusHolder> bonuses = new LinkedHashMap<>();

	public void tick() {
		for (var ent : bonuses.values()) {
			ent.tick();
		}
	}

	public BonusHolder getBonus(String type) {
		return bonuses.getOrDefault(type, new BonusHolder());
	}

	public void addModifier(String type, ResourceLocation id, BonusModifier modifier) {
		bonuses.computeIfAbsent(type, k -> new BonusHolder()).addModifier(id, modifier);
	}

	public void removeModifier(String type, ResourceLocation id) {
		var holder = bonuses.get(type);
		if (holder != null) holder.removeModifier(id);
	}

}

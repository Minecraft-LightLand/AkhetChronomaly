package dev.xkmc.akhet_chronomaly.content.core.bonus;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

@SerialClass
public class BonusHolder {

	@SerialField
	private final Map<ResourceLocation, BonusModifier> modifiers = new LinkedHashMap<>();

	@SerialField
	private double add, base, total;

	public void addModifier(ResourceLocation id, BonusModifier mod) {
		modifiers.put(id, mod);
		update();
	}

	public void removeModifier(ResourceLocation id) {
		if (modifiers.remove(id) != null) update();
	}

	public double get() {
		return add * (1 + base) * total;
	}

	public void tick() {
		if (modifiers.values().removeIf(BonusModifier::tickAndShouldRemove)) {
			update();
		}
	}

	private void update() {
		add = 0;
		base = 0;
		total = 1;
		for (var e : modifiers.values()) {
			add += e.add;
			base += e.base;
			total *= e.total;
		}
	}

}

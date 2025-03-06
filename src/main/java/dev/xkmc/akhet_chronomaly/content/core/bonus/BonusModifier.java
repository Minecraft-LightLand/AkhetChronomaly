package dev.xkmc.akhet_chronomaly.content.core.bonus;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

@SerialClass
public class BonusModifier {

	public static BonusModifier of(double value, AttributeModifier.Operation operation) {
		BonusModifier ans = new BonusModifier();
		switch (operation) {
			case ADD_VALUE -> ans.add = value;
			case ADD_MULTIPLIED_BASE -> ans.base = value;
			case ADD_MULTIPLIED_TOTAL -> ans.total = value;
		}
		return ans;
	}

	public static BonusModifier of(double value, AttributeModifier.Operation operation, int duration) {
		BonusModifier ans = of(value, operation);
		ans.duration = duration;
		return ans;
	}

	@SerialField
	private int duration = -1;

	@SerialField
	protected double add = 0, base = 0, total = 1;


	public boolean tickAndShouldRemove() {
		if (duration < 0) return false;
		return --duration <= 0;
	}

	public void update(BonusModifier mod) {
		if (mod.duration < 0) duration = -1;
		else if (duration > 0) duration = Math.max(duration, mod.duration);
	}

}

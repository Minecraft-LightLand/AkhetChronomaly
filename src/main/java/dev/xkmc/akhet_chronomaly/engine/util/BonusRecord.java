package dev.xkmc.akhet_chronomaly.engine.util;

import dev.xkmc.akhet_chronomaly.content.core.bonus.BonusModifier;
import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.codec.IComponentCodec;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record BonusRecord(
		String id,
		int duration,
		double amount,
		AttributeModifier.Operation operation
) implements IComponentCodec {

	public static BonusRecord add(String id, int duration, double amount) {
		return new BonusRecord(id, duration, amount, AttributeModifier.Operation.ADD_VALUE);
	}

	public static BonusRecord base(String id, int duration, double amount) {
		return new BonusRecord(id, duration, amount, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	}

	public static BonusRecord total(String id, int duration, double amount) {
		return new BonusRecord(id, duration, amount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	public void trigger(SetEffectContext ctx) {
		ctx.data().addModifier(id, ctx.path(), BonusModifier.of(amount, operation, duration));
	}

}

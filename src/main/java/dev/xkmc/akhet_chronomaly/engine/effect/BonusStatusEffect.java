package dev.xkmc.akhet_chronomaly.engine.effect;

import dev.xkmc.akhet_chronomaly.content.core.bonus.BonusModifier;
import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.type.IStatusEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record BonusStatusEffect(
		String attribute,
		double value,
		AttributeModifier.Operation operation
) implements IStatusEffect<BonusStatusEffect> {

	public static BonusStatusEffect add(String attribute, double value) {
		return new BonusStatusEffect(attribute, value, AttributeModifier.Operation.ADD_VALUE);
	}

	public static BonusStatusEffect base(String attribute, double value) {
		return new BonusStatusEffect(attribute, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	}

	public static BonusStatusEffect total(String attribute, double value) {
		return new BonusStatusEffect(attribute, value, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	@Override
	public void start(SetEffectContext ctx) {
		ctx.data().addModifier(attribute, ctx.path(), BonusModifier.of(value, operation));
	}

	@Override
	public void stop(SetEffectContext ctx) {
		ctx.data().removeModifier(attribute, ctx.path());
	}

}

package dev.xkmc.akhet_chronomaly.engine.effect;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.type.IStatusEffect;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttributeStatusEffect(
		Holder<Attribute> attribute,
		double value,
		AttributeModifier.Operation operation
) implements IStatusEffect<AttributeStatusEffect> {

	public static AttributeStatusEffect add(Holder<Attribute> attribute, double value) {
		return new AttributeStatusEffect(attribute, value, AttributeModifier.Operation.ADD_VALUE);
	}

	public static AttributeStatusEffect base(Holder<Attribute> attribute, double value) {
		return new AttributeStatusEffect(attribute, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	}

	public static AttributeStatusEffect total(Holder<Attribute> attribute, double value) {
		return new AttributeStatusEffect(attribute, value, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	@Override
	public void start(SetEffectContext ctx) {
		var ins = ctx.player().getAttribute(attribute);
		if (ins == null) return;
		ins.removeModifier(ctx.path());
		ins.addPermanentModifier(new AttributeModifier(ctx.path(), value, operation));
	}

	@Override
	public void stop(SetEffectContext ctx) {
		var ins = ctx.player().getAttribute(attribute);
		if (ins == null) return;
		ins.removeModifier(ctx.path());
	}

}

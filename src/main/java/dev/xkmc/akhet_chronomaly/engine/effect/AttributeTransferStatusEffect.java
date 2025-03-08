package dev.xkmc.akhet_chronomaly.engine.effect;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.DescElementCollector;
import dev.xkmc.akhet_chronomaly.engine.core.type.IStatusEffect;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public record AttributeTransferStatusEffect(
		Holder<Attribute> attribute,
		double factor, Type source,
		AttributeModifier.Operation operation
) implements IStatusEffect<AttributeTransferStatusEffect> {

	public enum Type {
		MAX_HEALTH(LivingEntity::getMaxHealth),
		HEALTH(LivingEntity::getHealth);

		private final Function<Player, Float> pred;

		Type(Function<Player, Float> pred) {
			this.pred = pred;
		}

		public float get(Player player) {
			return pred.apply(player);
		}

		public AttributeTransferStatusEffect add(Holder<Attribute> attribute, double factor) {
			return new AttributeTransferStatusEffect(attribute, factor, this, AttributeModifier.Operation.ADD_VALUE);
		}

		public AttributeTransferStatusEffect base(Holder<Attribute> attribute, double factor) {
			return new AttributeTransferStatusEffect(attribute, factor, this, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
		}

		public AttributeTransferStatusEffect total(Holder<Attribute> attribute, double factor) {
			return new AttributeTransferStatusEffect(attribute, factor, this, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		}

	}

	@Override
	public void start(SetEffectContext ctx) {
		var ins = ctx.player().getAttribute(attribute);
		if (ins == null) return;
		ins.removeModifier(ctx.path());
		double val = source.get(ctx.player()) * factor;
		if (val != 0) {
			ins.addPermanentModifier(new AttributeModifier(ctx.path(), val, operation));
		}
	}

	@Override
	public void tick(SetEffectContext ctx) {
		var ins = ctx.player().getAttribute(attribute);
		if (ins == null) return;
		var mod = ins.getModifier(ctx.path());
		double val = source.get(ctx.player()) * factor;
		if (mod == null || mod.amount() != val) {
			ins.removeModifier(ctx.path());
			ins.addPermanentModifier(new AttributeModifier(ctx.path(), val, operation));
		}
	}

	@Override
	public void stop(SetEffectContext ctx) {
		var ins = ctx.player().getAttribute(attribute);
		if (ins == null) return;
		ins.removeModifier(ctx.path());
	}

	@Override
	public void getDescElements(DescElementCollector list) {
		list.add(ACLang.perc(factor));
	}

}

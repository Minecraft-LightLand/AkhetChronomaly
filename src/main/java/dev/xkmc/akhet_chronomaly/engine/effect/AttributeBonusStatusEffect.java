package dev.xkmc.akhet_chronomaly.engine.effect;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.DescElementCollector;
import dev.xkmc.akhet_chronomaly.engine.core.type.IStatusEffect;
import dev.xkmc.akhet_chronomaly.engine.util.BonusRecord;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.TooltipFlag;

public record AttributeBonusStatusEffect(
		Holder<Attribute> attribute,
		String id,
		AttributeModifier.Operation operation
) implements IStatusEffect<AttributeBonusStatusEffect> {

	public static AttributeBonusStatusEffect add(Holder<Attribute> attribute, String id) {
		return new AttributeBonusStatusEffect(attribute, id, AttributeModifier.Operation.ADD_VALUE);
	}

	public static AttributeBonusStatusEffect base(Holder<Attribute> attribute, String id) {
		return new AttributeBonusStatusEffect(attribute, id, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	}

	public static AttributeBonusStatusEffect total(Holder<Attribute> attribute, String id) {
		return new AttributeBonusStatusEffect(attribute, id, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	@Override
	public void start(SetEffectContext ctx) {
		var ins = ctx.player().getAttribute(attribute);
		if (ins == null) return;
		ins.removeModifier(ctx.path());
		double val = ctx.data().getBonus(id).get();
		if (val != 0) {
			ins.addPermanentModifier(new AttributeModifier(ctx.path(), val, operation));
		}
	}

	@Override
	public void tick(SetEffectContext ctx) {
		var ins = ctx.player().getAttribute(attribute);
		if (ins == null) return;
		var mod = ins.getModifier(ctx.path());
		double val = ctx.data().getBonus(id).get();
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
		var e = list.fetch(BonusRecord.class);
		if (e == null || e.operation() != AttributeModifier.Operation.ADD_VALUE) return;
		list.add(attribute.value().toComponent(new AttributeModifier(AttributeStatusEffect.DUMMY, e.amount(), operation), TooltipFlag.NORMAL));
		if (e.duration() > 0) {
			list.add(ACLang.decimal(e.duration() / 20d));
		}
	}

}

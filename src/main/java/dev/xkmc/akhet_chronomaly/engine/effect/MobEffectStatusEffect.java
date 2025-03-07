package dev.xkmc.akhet_chronomaly.engine.effect;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.DescProvider;
import dev.xkmc.akhet_chronomaly.engine.core.type.IStatusEffect;
import dev.xkmc.akhet_chronomaly.engine.util.EffectRecord;
import dev.xkmc.l2core.base.effects.EffectUtil;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public record MobEffectStatusEffect(
		Holder<MobEffect> effect,
		int amplifier
) implements IStatusEffect<MobEffectStatusEffect>, DescProvider {

	@Override
	public void tick(SetEffectContext ctx) {
		var old = ctx.player().getEffect(effect);
		if (old == null || old.getAmplifier() < amplifier || old.getDuration() < 20) {
			EffectUtil.addEffect(ctx.player(), new MobEffectInstance(effect, 20, amplifier, true, false, true), ctx.player());
		}
	}

	@Override
	public MutableComponent getDesc() {
		return EffectRecord.getTooltip(effect.value(), amplifier, 0);
	}

}

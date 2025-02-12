package dev.xkmc.akhet_chronomaly.content.engine.effect;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.content.engine.core.effect.IStatusEffect;
import dev.xkmc.l2core.base.effects.EffectUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public record MobEffectStatusEffect(
		Holder<MobEffect> effect,
		int amplifier
) implements IStatusEffect<MobEffectStatusEffect> {

	@Override
	public void tick(SetEffectContext ctx) {
		var old = ctx.player().getEffect(effect);
		if (old == null || old.getAmplifier() < amplifier || old.getDuration() < 20) {
			EffectUtil.addEffect(ctx.player(), new MobEffectInstance(effect, 20, amplifier, true, false, true), ctx.player());
		}
	}

}

package dev.xkmc.akhet_chronomaly.content.engine.util;

import dev.xkmc.l2core.base.effects.EffectUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public record EffectRecord(
		Holder<MobEffect> effect,
		int amplifier,
		int duration,
		double chance,
		boolean stack,
		int maxStack
) {

	public void trigger(LivingEntity player, LivingEntity target) {
		if (player.getRandom().nextDouble() < chance) {
			int dur = duration;
			int amp = amplifier + 1;
			if (stack) {
				var ins = target.getEffect(effect);
				if (ins != null) {
					dur += ins.getDuration();
					amp += ins.getAmplifier() + 1;
					amp = Math.min(amp, maxStack());
				}
			}
			EffectUtil.addEffect(target, new MobEffectInstance(
					effect, dur, amp - 1
			), player);
		}
	}

}

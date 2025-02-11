package dev.xkmc.akhet_chronomaly.content.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.engine.core.effect.EffectContext;
import dev.xkmc.akhet_chronomaly.content.engine.core.trigger.OnTargetKillEffect;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public record HealOnKill(
		float healFactor,
		int nutrition,
		float saturation,
		float overflowFactor
) implements OnTargetKillEffect<HealOnKill> {

	public HealOnKill(float healFactor) {
		this(healFactor, 0, 0, 0);
	}

	@Override
	public void trigger(EffectContext ctx, LivingDeathEvent event) {
		var mhp = event.getEntity().getMaxHealth();
		ctx.player().heal(mhp * healFactor);
		if (nutrition > 0) {
			int nut = ctx.player().getFoodData().getFoodLevel() + nutrition;
			ctx.player().getFoodData().eat(nutrition, saturation);
			int overflow = nut - ctx.player().getFoodData().getFoodLevel();
			if (overflow > 0 && overflowFactor > 0) {
				ctx.player().heal(overflowFactor * overflow * ctx.player().getMaxHealth());
			}
		}
	}

}

package dev.xkmc.akhet_chronomaly.content.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.engine.core.effect.EffectContext;
import dev.xkmc.akhet_chronomaly.content.engine.core.trigger.OnTargetKillEffect;
import dev.xkmc.akhet_chronomaly.content.engine.util.EffectRecord;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public record KillTargetSelfGainEffect(
		EffectRecord component
) implements OnTargetKillEffect<KillTargetSelfGainEffect> {

	@Override
	public void trigger(EffectContext ctx, LivingDeathEvent event) {
		component.trigger(ctx.player(), ctx.player());
	}

}

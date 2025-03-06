package dev.xkmc.akhet_chronomaly.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.OnTargetKillEffect;
import dev.xkmc.akhet_chronomaly.engine.util.EffectRecord;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public record KillTargetGainEffect(
		EffectRecord component
) implements OnTargetKillEffect<KillTargetGainEffect> {

	@Override
	public void trigger(SetEffectContext ctx, LivingDeathEvent event) {
		component.trigger(ctx.player(), ctx.player());
	}

}

package dev.xkmc.akhet_chronomaly.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.OnPlayerHealEffect;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.OnTargetKillEffect;
import dev.xkmc.akhet_chronomaly.engine.util.EffectRecord;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

public record HealGainEffect(
		EffectRecord component
) implements OnPlayerHealEffect<HealGainEffect> {

	@Override
	public void trigger(SetEffectContext ctx, LivingHealEvent event) {
		component.trigger(ctx.player(), ctx.player());
	}

}

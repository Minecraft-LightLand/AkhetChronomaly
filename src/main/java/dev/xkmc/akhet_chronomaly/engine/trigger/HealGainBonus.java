package dev.xkmc.akhet_chronomaly.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.OnPlayerHealEffect;
import dev.xkmc.akhet_chronomaly.engine.util.BonusRecord;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

public record HealGainBonus(
		BonusRecord component
) implements OnPlayerHealEffect<HealGainBonus> {

	@Override
	public void trigger(SetEffectContext ctx, LivingHealEvent event) {
		component.trigger(ctx);
	}

}

package dev.xkmc.akhet_chronomaly.engine.core.trigger;

import dev.xkmc.akhet_chronomaly.engine.core.type.ITriggerEffect;
import dev.xkmc.akhet_chronomaly.engine.core.type.AutoReg;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

public interface OnPlayerHealEffect<T extends Record & OnPlayerHealEffect<T>> extends ITriggerEffect<T, LivingHealEvent> {

	@Override
	default TriggerType<LivingHealEvent> triggerType() {
		return AutoReg.ON_HEAL.get();
	}

}

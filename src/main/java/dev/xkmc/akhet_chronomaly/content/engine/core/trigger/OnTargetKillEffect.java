package dev.xkmc.akhet_chronomaly.content.engine.core.trigger;

import dev.xkmc.akhet_chronomaly.content.engine.core.effect.ITriggerEffect;
import dev.xkmc.akhet_chronomaly.content.engine.core.type.AutoReg;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public interface OnTargetKillEffect<T extends Record & OnTargetKillEffect<T>> extends ITriggerEffect<T, LivingDeathEvent> {

	@Override
	default TriggerType<LivingDeathEvent> triggerType() {
		return AutoReg.ON_KILL_TARGET.get();
	}

}

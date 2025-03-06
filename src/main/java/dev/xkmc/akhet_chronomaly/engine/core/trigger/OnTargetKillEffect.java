package dev.xkmc.akhet_chronomaly.engine.core.trigger;

import dev.xkmc.akhet_chronomaly.engine.core.type.ITriggerEffect;
import dev.xkmc.akhet_chronomaly.engine.core.type.AutoReg;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public interface OnTargetKillEffect<T extends Record & OnTargetKillEffect<T>> extends ITriggerEffect<T, LivingDeathEvent> {

	@Override
	default TriggerType<LivingDeathEvent> triggerType() {
		return AutoReg.ON_KILL_TARGET.get();
	}

}

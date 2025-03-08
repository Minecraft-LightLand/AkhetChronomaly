package dev.xkmc.akhet_chronomaly.engine.core.trigger;

import dev.xkmc.akhet_chronomaly.engine.core.type.AutoReg;
import dev.xkmc.akhet_chronomaly.engine.core.type.ITriggerEffect;
import dev.xkmc.akhet_chronomaly.engine.util.CritTest;

public interface CritTestEffect<T extends Record & CritTestEffect<T>> extends ITriggerEffect<T, CritTest> {

	@Override
	default TriggerType<CritTest> triggerType() {
		return AutoReg.ON_CRIT_TEST.get();
	}

}

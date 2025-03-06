package dev.xkmc.akhet_chronomaly.engine.core.trigger;

import dev.xkmc.akhet_chronomaly.engine.core.type.ITriggerEffect;
import dev.xkmc.akhet_chronomaly.engine.core.type.AutoReg;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;

public interface OnPlayerDamageEffect<T extends Record & OnPlayerDamageEffect<T>> extends ITriggerEffect<T, DamageData.Defence> {

	@Override
	default TriggerType<DamageData.Defence> triggerType() {
		return AutoReg.ON_DMG_SELF.get();
	}

}

package dev.xkmc.akhet_chronomaly.engine.core.trigger;

import dev.xkmc.akhet_chronomaly.engine.core.type.ITriggerEffect;
import dev.xkmc.akhet_chronomaly.engine.core.type.AutoReg;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;

public interface AfterPlayerDamagedEffect<T extends Record & AfterPlayerDamagedEffect<T>> extends ITriggerEffect<T, DamageData.DefenceMax> {

	@Override
	default TriggerType<DamageData.DefenceMax> triggerType() {
		return AutoReg.AFT_DMG_SELF.get();
	}

}

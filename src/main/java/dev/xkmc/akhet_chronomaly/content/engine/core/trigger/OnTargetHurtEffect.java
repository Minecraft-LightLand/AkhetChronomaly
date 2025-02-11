package dev.xkmc.akhet_chronomaly.content.engine.core.trigger;

import dev.xkmc.akhet_chronomaly.content.engine.core.effect.ITriggerEffect;
import dev.xkmc.akhet_chronomaly.content.engine.core.type.AutoReg;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;

public interface OnTargetHurtEffect<T extends Record & OnTargetHurtEffect<T>> extends ITriggerEffect<T, DamageData.Offence> {

	@Override
	default TriggerType<DamageData.Offence> triggerType() {
		return AutoReg.ON_HURT_TARGET.get();
	}

}

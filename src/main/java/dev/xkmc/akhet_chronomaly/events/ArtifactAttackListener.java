package dev.xkmc.akhet_chronomaly.events;

import dev.xkmc.akhet_chronomaly.content.engine.core.type.AutoReg;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;

public class ArtifactAttackListener implements AttackListener {

	@Override
	public void onHurt(DamageData.Offence cache) {
		if (cache.getAttacker() != null) {
			AutoReg.ON_HURT_TARGET.get().trigger(cache.getAttacker(), cache);
		}
	}

	@Override
	public void onDamage(DamageData.Defence cache) {
		AutoReg.ON_DMG_SELF.get().trigger(cache.getTarget(), cache);
	}

	@Override
	public void onDamageFinalized(DamageData.DefenceMax cache) {
		if (cache.getAttacker() != null)
			AutoReg.AFT_DMG_TARGET.get().trigger(cache.getAttacker(), cache);
		AutoReg.AFT_DMG_SELF.get().trigger(cache.getTarget(), cache);
	}

}

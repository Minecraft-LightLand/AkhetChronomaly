package dev.xkmc.akhet_chronomaly.events;

import dev.xkmc.akhet_chronomaly.engine.core.type.AutoReg;
import dev.xkmc.akhet_chronomaly.engine.util.CritTest;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.PlayerAttackCache;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

public class ArtifactAttackListener implements AttackListener {

	@Override
	public boolean onCriticalHit(PlayerAttackCache cache, CriticalHitEvent event) {
		CritTest cont = new CritTest(cache, event);
		AutoReg.ON_CRIT_TEST.get().trigger(cache.getAttacker(), cont);
		return cont.shouldCrit();
	}

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

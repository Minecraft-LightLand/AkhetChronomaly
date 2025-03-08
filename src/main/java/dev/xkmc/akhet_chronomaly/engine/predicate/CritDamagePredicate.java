package dev.xkmc.akhet_chronomaly.engine.predicate;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.type.IUserPredicate;

public record CritDamagePredicate() implements IUserPredicate<CritDamagePredicate> {

	@Override
	public boolean isValid(SetEffectContext ctx) {
		var source = ctx.getDamageSource();
		var attack = ctx.getAttackTrigger();
		if (source.isEmpty() || attack.isEmpty()) return false;
		var player = attack.get().getPlayerData();
		if (player == null) return false;
		var crit = player.getCriticalHitEvent();
		return crit != null && crit.isCriticalHit();
	}

}

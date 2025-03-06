package dev.xkmc.akhet_chronomaly.engine.predicate;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.type.IUserPredicate;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import net.minecraft.world.phys.Vec3;

public record BehindDamagePredicate(
		boolean direct, boolean checkStrength
) implements IUserPredicate<BehindDamagePredicate> {

	@Override
	public boolean isValid(SetEffectContext ctx) {
		var source = ctx.getDamageSource();
		var attack = ctx.getAttackTrigger();
		if (source.isEmpty() || attack.isEmpty()) return false;
		if (direct && !source.get().is(L2DamageTypes.DIRECT)) return false;
		if (checkStrength && attack.get().getStrength() < 0.95f) return false;
		Vec3 src = source.get().getSourcePosition();
		var target = attack.get().getTarget();
		if (src == null) return false;
		Vec3 view = target.calculateViewVector(0.0F, target.getYHeadRot());
		Vec3 dir = src.vectorTo(target.position()).multiply(1, 0, 1).normalize();
		return dir.dot(view) < 0;
	}

}

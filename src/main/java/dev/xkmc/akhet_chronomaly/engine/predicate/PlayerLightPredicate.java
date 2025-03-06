package dev.xkmc.akhet_chronomaly.engine.predicate;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.util.PlayerLight;
import dev.xkmc.akhet_chronomaly.engine.core.type.IUserPredicate;

public record PlayerLightPredicate(
		int min, int max, boolean skyOnly
) implements IUserPredicate<PlayerLightPredicate> {

	@Override
	public boolean isValid(SetEffectContext ctx) {
		int light = skyOnly ? PlayerLight.playerUnderSun(ctx.player()) : PlayerLight.playerLight(ctx.player());
		return light > min && light <= max;
	}


}

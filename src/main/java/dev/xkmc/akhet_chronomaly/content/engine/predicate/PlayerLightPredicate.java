package dev.xkmc.akhet_chronomaly.content.engine.predicate;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.content.effects.core.PlayerLight;
import dev.xkmc.akhet_chronomaly.content.engine.core.type.IUserPredicate;

public record PlayerLightPredicate(
		int min, int max, boolean skyOnly
) implements IUserPredicate<PlayerLightPredicate> {

	@Override
	public boolean isValid(SetEffectContext ctx) {
		int light = skyOnly ? PlayerLight.playerUnderSun(ctx.player()) : PlayerLight.playerLight(ctx.player());
		return light > min && light <= max;
	}


}

package dev.xkmc.akhet_chronomaly.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.DescElementCollector;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.OnTargetKillEffect;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public record HealOnKill(
		float healFactor,
		int nutrition,
		float saturation,
		float overflowFactor
) implements OnTargetKillEffect<HealOnKill> {

	public HealOnKill(float healFactor) {
		this(healFactor, 0, 0, 0);
	}

	@Override
	public void trigger(SetEffectContext ctx, LivingDeathEvent event) {
		var mhp = event.getEntity().getMaxHealth();
		ctx.player().heal(mhp * healFactor);
		if (nutrition > 0) {
			int nut = ctx.player().getFoodData().getFoodLevel() + nutrition;
			ctx.player().getFoodData().eat(nutrition, saturation);
			int overflow = nut - ctx.player().getFoodData().getFoodLevel();
			if (overflow > 0 && overflowFactor > 0) {
				ctx.player().heal(overflowFactor * overflow * ctx.player().getMaxHealth());
			}
		}
	}

	@Override
	public void getDescElements(DescElementCollector list) {
		list.add(ACLang.perc(healFactor));
	}

}

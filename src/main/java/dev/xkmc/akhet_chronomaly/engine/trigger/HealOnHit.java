package dev.xkmc.akhet_chronomaly.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.DescElementCollector;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.DescProvider;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.AfterPlayerHurtTargetEffect;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public record HealOnHit(
		float healFactor
) implements AfterPlayerHurtTargetEffect<HealOnHit>, DescProvider {

	@Override
	public void trigger(SetEffectContext ctx, DamageData.DefenceMax event) {
		ctx.player().heal(event.getDamageFinal() * healFactor);
	}

	@Override
	public MutableComponent getDesc() {
		return ACLang.HEAL_HIT.get(ACLang.perc(healFactor));
	}

}

package dev.xkmc.akhet_chronomaly.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.DescElementCollector;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.OnTargetHurtEffect;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;

public record ConvertAbsorptionToDamage(
		float factor
) implements OnTargetHurtEffect<ConvertAbsorptionToDamage> {

	@Override
	public void trigger(SetEffectContext ctx, DamageData.Offence event) {
		var abs = ctx.player().getAbsorptionAmount();
		if (abs <= 0) return;
		ctx.player().setAbsorptionAmount(abs * (1 - factor));
		event.addHurtModifier(DamageModifier.addExtra(abs * factor, ctx.path()));
	}

	@Override
	public void getDescElements(DescElementCollector list) {
		list.add(ACLang.perc(factor));
	}
}

package dev.xkmc.akhet_chronomaly.engine.predicate;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.DescProvider;
import dev.xkmc.akhet_chronomaly.engine.core.type.IUserPredicate;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import net.minecraft.network.chat.MutableComponent;

public record SelfHealthPredicate(
		double min, double max
) implements IUserPredicate<SelfHealthPredicate>, DescProvider {

	@Override
	public boolean isValid(SetEffectContext ctx) {
		var e = ctx.player();
		var php = e.getHealth() / e.getMaxHealth();
		return (min <= 0 || php >= min) && (max >= 1 || php <= max);
	}

	@Override
	public MutableComponent getDesc() {
		if (max >= 1) {
			return ACLang.PHP_HIGH.get(ACLang.perc(min));
		} else if (min <= 0) {
			return ACLang.PHP_LOW.get(ACLang.perc(max));
		} else {
			return ACLang.PHP_MID.get(ACLang.perc(min), ACLang.perc(max));
		}
	}

}

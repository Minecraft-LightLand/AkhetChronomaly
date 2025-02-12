package dev.xkmc.akhet_chronomaly.content.core.data;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.Optional;

public record SetEffectContext(
		Player player,
		Holder<SetEffect> set,
		AkhetSetData data,
		ResourceLocation path,
		Optional<?> trigger
) {


	public Optional<DamageSource> getDamageSource() {
		if (trigger.isEmpty()) return Optional.empty();
		if (trigger.get() instanceof DamageData.PostSetup p) {
			return Optional.of(p.getSource());
		}
		if (trigger.get() instanceof LivingDeathEvent e) {
			return Optional.of(e.getSource());
		}
		return Optional.empty();
	}

	public Optional<DamageData.PostSetup> getAttackTrigger() {
		return trigger().map(e -> e instanceof DamageData.PostSetup p ? p : null);
	}

	public SetEffectContext sub(String s) {
		return new SetEffectContext(player, set, data, path.withSuffix(s), trigger);
	}

}

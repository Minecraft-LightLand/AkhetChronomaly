package dev.xkmc.akhet_chronomaly.content.set.core;

import dev.xkmc.akhet_chronomaly.content.config.ArtifactSetConfig;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.l2core.capability.conditionals.TokenKey;
import dev.xkmc.l2core.init.L2LibReg;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public abstract class PersistentDataSetEffect<T extends SetEffectData> extends PlayerOnlySetEffect {

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, boolean enabled) {
		if (!enabled) return;
		fetch(player, ent).update(2);
	}

	public @Nullable T fetchNullable(Player player) {
		return L2LibReg.CONDITIONAL.type().getExisting(player).map(e -> e.getData(getKey())).orElse(null);
	}

	public T fetch(Player player, ArtifactSetConfig.Entry ent) {
		return L2LibReg.CONDITIONAL.type().getOrCreate(player).getOrCreateData(getKey(), () -> getData(ent));
	}

	public abstract T getData(ArtifactSetConfig.Entry ent);

	public TokenKey<T> getKey() {
		return new TokenKey<>(AkhetChronomaly.MODID, getRegistryName().toString());
	}

}

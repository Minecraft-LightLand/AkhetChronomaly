package dev.xkmc.akhet_chronomaly.init.registrate;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.world.effect.MobEffect;

public class ArtifactEffects {

	private static <T extends MobEffect> SimpleEntry<MobEffect> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return new SimpleEntry<>(AkhetChronomaly.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register());
	}

	public static void register() {
	}

}

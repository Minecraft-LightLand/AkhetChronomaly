package dev.xkmc.akhet_chronomaly.init.registrate.entries;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.akhet_chronomaly.content.core.ArtifactSet;
import dev.xkmc.akhet_chronomaly.content.core.BaseArtifact;
import dev.xkmc.akhet_chronomaly.content.set.core.SetEffect;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ACRegistrate extends L2Registrate {

	public ACRegistrate() {
		super(AkhetChronomaly.MODID);
	}

	public final TreeMap<ResourceLocation, SetEntry<?>> SET_MAP = new TreeMap<>();
	public final List<SetEntry<?>> SET_LIST = new ArrayList<>();

	final <T extends ArtifactSet> SetBuilder<T, BaseArtifact, ACRegistrate> regSet(String id, NonNullSupplier<T> sup, String name) {
		return this.entry(id, (cb) -> new SetBuilder<>(this, this, id, cb, sup)).lang(name);
	}

	<T extends SetEffect> SetEffectBuilder<T, ACRegistrate> setEffect(String id, NonNullSupplier<T> sup) {
		return this.entry(id, cb -> new SetEffectBuilder<>(this, this, id, cb, sup));
	}

	public SetRegHelper getSetHelper(String id) {
		return new SetRegHelper(this, id);
	}

}

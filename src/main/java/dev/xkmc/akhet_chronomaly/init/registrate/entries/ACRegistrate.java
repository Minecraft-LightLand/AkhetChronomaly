package dev.xkmc.akhet_chronomaly.init.registrate.entries;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.akhet_chronomaly.content.core.ArtifactSet;
import dev.xkmc.akhet_chronomaly.content.core.BaseArtifact;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;

public class ACRegistrate extends L2Registrate {

	public ACRegistrate() {
		super(AkhetChronomaly.MODID);
	}

	final <T extends ArtifactSet> SetBuilder<T, BaseArtifact, ACRegistrate> regSet(String id, NonNullSupplier<T> sup, String name) {
		return this.entry(id, (cb) -> new SetBuilder<>(this, this, id, cb, sup)).lang(name);
	}

	public SetRegHelper getSetHelper(String id) {
		return new SetRegHelper(this, id);
	}

}

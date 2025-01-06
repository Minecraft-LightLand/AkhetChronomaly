package dev.xkmc.akhet_chronomaly.init.registrate.entries;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.akhet_chronomaly.content.set.core.SetEffect;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SetEffectEntry<T extends SetEffect> extends RegistryEntry<SetEffect, T> {

	public SetEffectEntry(AbstractRegistrate<?> owner, DeferredHolder<SetEffect, T> key) {
		super(owner, key);
	}

}

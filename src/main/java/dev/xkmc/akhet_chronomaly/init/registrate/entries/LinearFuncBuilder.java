package dev.xkmc.akhet_chronomaly.init.registrate.entries;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import dev.xkmc.akhet_chronomaly.content.core.LinearFunc;
import dev.xkmc.akhet_chronomaly.init.registrate.ACTypeRegistry;
import dev.xkmc.l2serial.util.Wrappers;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class LinearFuncBuilder<P> extends AbstractBuilder<LinearFunc, LinearFunc, P, LinearFuncBuilder<P>> {

	private final NonNullSupplier<LinearFunc> sup;
	private final SetRegHelper set;

	private final double base, slope;

	public LinearFuncBuilder(ACRegistrate owner, P parent, String name, BuilderCallback callback,
							 SetRegHelper set,
							 NonNullSupplier<LinearFunc> sup,
							 double base, double slope) {
		super(owner, parent, name, callback, ACTypeRegistry.LINEAR.key());
		this.sup = sup;
		this.base = base;
		this.slope = slope;
		this.set = set;
	}

	@Override
	protected RegistryEntry<LinearFunc, LinearFunc> createEntryWrapper(DeferredHolder<LinearFunc, LinearFunc> delegate) {
		return new LinearFuncEntry(Wrappers.cast(this.getOwner()), set, delegate, base, slope);
	}

	@NonnullType
	@NotNull
	protected LinearFunc createEntry() {
		return this.sup.get();
	}

}

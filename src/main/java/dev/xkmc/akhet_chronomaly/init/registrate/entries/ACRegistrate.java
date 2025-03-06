package dev.xkmc.akhet_chronomaly.init.registrate.entries;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.akhet_chronomaly.content.core.data.SetEffect;
import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactSet;
import dev.xkmc.akhet_chronomaly.content.core.item.BaseArtifact;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ACRegistrate extends L2Registrate {

	private final List<Pair<ResourceKey<SetEffect>, Supplier<SetEffect>>> pending = new ArrayList<>();

	public ACRegistrate() {
		super(AkhetChronomaly.MODID);
	}

	final <T extends ArtifactSet> SetBuilder<T, BaseArtifact, ACRegistrate> regSet(String id, NonNullSupplier<T> sup, String name) {
		return this.entry(id, (cb) -> new SetBuilder<>(this, this, id, cb, sup)).lang(name);
	}

	public SetRegHelper getSetHelper(String id) {
		return new SetRegHelper(this, id);
	}

	public void addData(ResourceKey<SetEffect> key, Supplier<SetEffect> data) {
		pending.add(Pair.of(key, data));
	}

	public void genSetEffect(BootstrapContext<SetEffect> ctx) {
		for (var e : pending) {
			ctx.register(e.getFirst(), e.getSecond().get());
		}
	}

}

package dev.xkmc.akhet_chronomaly.content.config;

import dev.xkmc.akhet_chronomaly.content.core.ArtifactSet;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.akhet_chronomaly.init.registrate.entries.SetEntry;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record SetGroup(ArrayList<ArtifactSet> set) {

	public static SetGroup of(Collection<SetEntry<?>> sets) {
		return new SetGroup(new ArrayList<>(sets.stream().map(DeferredHolder::get).toList()));
	}

	public static SetGroup ofGroup(Collection<SetEntry<?>> sets) {
		return new SetGroup(new ArrayList<>(sets.stream().map(DeferredHolder::get).toList()));
	}

	public List<SetEntry<?>> getSets(boolean full) {//TODO
		ArrayList<SetEntry<?>> ans = new ArrayList<>();
		for (var e : set) {
			var x = AkhetChronomaly.REGISTRATE.SET_MAP.get(e.getRegistryName());
			if (x == null) continue;
			ans.add(x);
		}
		return ans;
	}

}

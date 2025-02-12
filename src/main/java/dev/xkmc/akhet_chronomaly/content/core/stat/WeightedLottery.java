package dev.xkmc.akhet_chronomaly.content.core.stat;

import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactSlot;
import dev.xkmc.akhet_chronomaly.init.registrate.ACTypeRegistry;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.RandomSource;

import java.util.LinkedList;
import java.util.List;

public class WeightedLottery {

	private final List<Pair<Holder<StatType>, SlotStatEntry>> list = new LinkedList<>();
	private final RandomSource source;
	private int total = 0;

	public WeightedLottery(ArtifactSlot slot, RegistryAccess access, RandomSource source) {
		this.source = source;
		var map = ACTypeRegistry.STAT_MAP.get(access, slot.holder());
		if (map == null) return;
		map.map().entrySet().stream()
				.map(e -> Pair.of(ACTypeRegistry.STAT_TYPE.get(access, e.getKey()), e.getValue()))
				.filter(e -> e.first() != null && e.second().weight() > 0).forEach(list::add);
		for (var e : list) {
			total += e.second().weight();
		}
	}

	public Pair<Holder<StatType>, SlotStatEntry> poll() {
		var itr = list.iterator();
		int sel = source.nextInt(total);
		while (true) {
			var e = itr.next();
			sel -= e.second().weight();
			if (sel < 0 || !itr.hasNext()) {
				itr.remove();
				total -= e.second().weight();
				return e;
			}
		}
	}

	public boolean isEmpty() {
		return total == 0 || list.isEmpty();
	}

	public void remove(Holder<StatType> sub) {
		var itr = list.iterator();
		while (itr.hasNext()) {
			var e = itr.next();
			if (e.equals(sub)) {
				total -= e.second().weight();
				itr.remove();
				return;
			}
		}
	}

}

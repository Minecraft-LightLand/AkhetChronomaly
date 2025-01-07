package dev.xkmc.akhet_chronomaly.content.core;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.akhet_chronomaly.content.config.StatType;
import dev.xkmc.akhet_chronomaly.content.config.WeightedLottery;
import dev.xkmc.akhet_chronomaly.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.akhet_chronomaly.init.data.ACModConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public record ArtifactStats(
		ArtifactSlot slot, int rank, int level, int exp, int old_level,
		ArrayList<StatEntry> stats
) {

	public static ArtifactStats.Mutable of(ArtifactSlot slot, int rank) {
		return new Mutable(new ArtifactStats(slot, rank, 0, 0, 0, new ArrayList<>()));
	}

	public static ArtifactStats generate(RegistryAccess access, ArtifactSlot slot, int rank, RandomSource random) {
		var ans = ArtifactStats.of(slot, rank);
		ans.generate(access, random);
		return ans.immutable();
	}

	public Multimap<Holder<Attribute>, AttributeModifier> buildAttributes(ResourceLocation base) {
		ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();
		for (StatEntry ent : stats) {
			ent.toModifier(builder, base);
		}
		return builder.build();
	}

	public ArtifactStats addExp(int gain) {
		int nexp = exp + gain;
		int nlevel = level;
		int max_level = ArtifactUpgradeManager.getMaxLevel(rank);
		while (true) {
			if (nlevel >= max_level) {
				break;
			}
			int max_exp = ArtifactUpgradeManager.getExpForLevel(rank, nlevel);
			if (nexp < max_exp) {
				break;
			}
			nexp -= max_exp;
			nlevel++;
		}
		if (nlevel == max_level) {
			nexp = 0;
		}
		return new ArtifactStats(slot, rank, nlevel, nexp, old_level, stats);
	}

	public ArtifactStats upgrade(RandomSource random) {
		var mutable = new Mutable(this);
		for (int i = old_level + 1; i <= level; i++) {
			mutable.onUpgrade(i, random);
		}
		return mutable.flush();
	}

	public boolean containsKey(Holder<StatType> astat) {
		for (var e : stats) {
			if (e.type().equals(astat)) return true;
		}
		return false;
	}

	@Nullable
	public StatEntry get(Holder<StatType> astat) {
		for (var e : stats) {
			if (e.type().equals(astat)) return e;
		}
		return null;
	}

	public static class Mutable {

		private final ArtifactStats ref;
		private final ArrayList<StatEntry.Mutable> stats;
		private final Map<Holder<StatType>, StatEntry.Mutable> map;

		private Mutable(ArtifactStats self) {
			this.ref = self;
			this.stats = new ArrayList<>(self.stats.stream()
					.map(StatEntry::mutable).toList());
			map = new LinkedHashMap<>();
			for (var ent : stats) {
				map.put(ent.type(), ent);
			}
		}

		private void add(StatEntry entry) {
			if (map.containsKey(entry.type()))
				return;
			var m = entry.mutable();
			stats.add(m);
			map.put(entry.type(), m);
		}

		public void add(Holder<StatType> type, double value) {
			if (map.containsKey(type)) {
				map.get(type).addMultiplier(value);
			} else {
				add(new StatEntry(type, value));
			}
		}

		private void generate(RegistryAccess access, RandomSource random) {
			var main_list = new WeightedLottery(access, random, true);
			var main = main_list.poll();
			var sub_list = new WeightedLottery(access, random, false);
			sub_list.remove(main);
			add(main, main.value().getInitialValue(random));
			int roll = ref.rank() - 1;
			for (int i = 0; i < roll; i++) {
				if (sub_list.isEmpty()) break;
				Holder<StatType> sub = sub_list.poll();
				add(sub, sub.value().getSubValue(random));
			}
		}

		private void onUpgrade(int lv, RandomSource random) {
			int gate = ACModConfig.SERVER.levelPerSubStat.get();
			if (lv % gate == 0 && !stats.isEmpty()) {
				for (var e : stats)
					add(e.type(), e.type().value().getSubValue(random));
			}
		}

		public ArtifactStats immutable() {
			var sub = new ArrayList<>(stats.stream().map(StatEntry.Mutable::immutable).toList());
			return new ArtifactStats(ref.slot, ref.rank, ref.level, ref.exp, ref.old_level, sub);
		}

		private ArtifactStats flush() {
			var sub = new ArrayList<>(stats.stream().map(StatEntry.Mutable::immutable).toList());
			return new ArtifactStats(ref.slot, ref.rank, ref.level, ref.exp, ref.level, sub);
		}

	}

}

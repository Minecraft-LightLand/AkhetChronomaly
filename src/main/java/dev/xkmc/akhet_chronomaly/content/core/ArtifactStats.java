package dev.xkmc.akhet_chronomaly.content.core;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.akhet_chronomaly.content.config.StatType;
import dev.xkmc.akhet_chronomaly.content.config.WeightedLottery;
import dev.xkmc.akhet_chronomaly.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import dev.xkmc.akhet_chronomaly.init.data.ACModConfig;
import dev.xkmc.akhet_chronomaly.init.registrate.ACTypeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public record ArtifactStats(
		ArtifactSlot slot, int rank, int level, int exp, int old_level,
		int scale, int chance, int setCount, @Nullable AscendType ascend, ArrayList<StatEntry> stats
) {

	public static ArtifactStats.Mutable of(ArtifactSlot slot, int rank, int chance) {
		return new Mutable(new ArtifactStats(slot, rank, 0, 0, 0, rank, chance, 1, null, new ArrayList<>()));
	}

	public static ArtifactStats withRank(RegistryAccess access, ArtifactSlot slot, int rank) {
		var data = ACTypeRegistry.STAT_MAP.get(access, slot.holder());
		int chance = data == null ? 0 : data.refineChance();
		var ans = ArtifactStats.of(slot, rank, chance);
		return ans.immutable();
	}

	public static ArtifactStats generate(RegistryAccess access, ArtifactSlot slot, int rank, RandomSource random) {
		var data = ACTypeRegistry.STAT_MAP.get(access, slot.holder());
		int chance = data == null ? 0 : data.refineChance();
		var ans = ArtifactStats.of(slot, rank, chance);
		ans.generate(access, random, 1);
		return ans.immutable();
	}

	public void gatherAttributeIds(Consumer<ResourceLocation> cons, ResourceLocation base) {
		for (StatEntry ent : stats) {
			cons.accept(ent.attributeId(base));
		}
	}

	public Multimap<Holder<Attribute>, AttributeModifier> buildAttributes(RegistryAccess access, ResourceLocation base) {
		ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();
		var map = ACTypeRegistry.STAT_MAP.get(access, slot().holder());
		for (StatEntry ent : stats) {
			Optional.ofNullable(map).map(e -> e.map().get(ent.getID()))
					.ifPresent(e -> ent.toModifier(e.getScale(map, scale), builder, base));
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
		return new ArtifactStats(slot, rank, nlevel, nexp, old_level, scale, chance, setCount, ascend, stats);
	}

	public ArtifactStats upgrade(RandomSource random) {
		var mutable = new Mutable(this);
		for (int i = old_level + 1; i <= level; i++) {
			mutable.onUpgrade(i, random);
		}
		return mutable.immutable();
	}

	public ArtifactStats generate(RegistryAccess access, RandomSource random) {
		var mutable = new Mutable(this);
		mutable.generate(access, random, 1);
		return mutable.immutable();
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

	public void buildTooltip(Item.TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		boolean shift = flag.hasShiftDown();
		var world = ctx.level();
		boolean max = level() == ArtifactUpgradeManager.getMaxLevel(rank());
		list.add(ACLang.ARTIFACT_LEVEL.get(level()).withStyle(max ? ChatFormatting.GOLD : ChatFormatting.WHITE));
		if (level() < ArtifactUpgradeManager.getMaxLevel(rank())) {
			if (shift)
				list.add(ACLang.ARTIFACT_EXP.get(exp(), ArtifactUpgradeManager.getExpForLevel(rank(), level())));
		}
		if (level() > old_level()) {
			list.add(ACLang.UPGRADE.get());
		} else if (!shift && world != null && !stats().isEmpty()) {
			var map = ACTypeRegistry.STAT_MAP.get(world.registryAccess(), slot().holder());
			boolean first = true;
			if (map != null) {
				for (StatEntry ent : stats) {
					var e = map.map().get(ent.getID());
					if (e != null && ent.scale() != 0) {
						if (first) {
							list.add(ACLang.STAT.get());
							first = false;
						}
						list.add(ent.getTooltip(e.getScale(map, scale), null));
					}
				}
			}
		}
	}

	public static class Mutable {

		private final ArtifactStats ref;
		private final ArrayList<StatEntry.Mutable> stats;
		private final Map<Holder<StatType>, StatEntry.Mutable> map;
		private int rank;
		private int scale;
		private int chance;
		private int level;
		private int old_level;
		private int exp;
		private final int setCount;
		private AscendType ascend;

		private Mutable(ArtifactStats self) {
			this.ref = self;
			this.stats = new ArrayList<>(self.stats.stream()
					.map(StatEntry::mutable).toList());
			map = new LinkedHashMap<>();
			for (var ent : stats) {
				map.put(ent.type(), ent);
			}
			this.scale = self.scale;
			this.rank = self.rank;
			this.level = self.level;
			this.chance = self.chance;
			this.old_level = self.old_level;
			this.exp = self.exp;
			this.setCount = self.setCount;
			this.ascend = self.ascend;
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
				add(new StatEntry(type, value, 1));
			}
		}

		public void generate(RegistryAccess access, RandomSource random, int count) {
			stats.clear();
			var list = new WeightedLottery(ref.slot(), access, random);
			for (int i = 0; i < count; i++) {
				if (list.isEmpty()) break;
				var sub = list.poll().first();
				add(sub, sub.value().getLevelingValue(random));
			}
		}

		private void onUpgrade(int lv, RandomSource random) {
			int gate = ACModConfig.SERVER.levelPerSubStat.get();
			if (lv % gate == 0 && !stats.isEmpty()) {
				for (var e : stats)
					add(e.type(), e.type().value().getLevelingValue(random));
			}
		}

		public void reforge(RegistryAccess access, RandomSource random) {
			var list = new WeightedLottery(ref.slot(), access, random);
			for (var e : stats) {
				list.remove(e.type());
			}
			if (list.isEmpty()) return;
			var sub = list.poll().first();
			add(sub, sub.value().getLevelingValue(random));
			chance--;
		}

		public void refine() {
			rank++;
			scale++;
			chance--;
		}

		public void ascend(AscendType type) {
			if (stats.size() != 3 || ascend != null || chance != 1) return;
			switch (type) {
				case MYSTIC -> {
					boolean first = true;
					for (var e : stats) {
						if (first) first = false;
						else e.scale(0);
					}
				}
				case FORBIDDEN -> {
					boolean first = true;
					for (var e : stats) {
						if (first) {
							first = false;
							e.scale(-1);
						} else e.scale(2);
					}
				}
				case CONDENSE -> scale += 2;
				case BIND -> scale += 4;
			}
			ascend = type;
			chance = 0;
		}

		private void clear() {
			old_level = 0;
			level = 0;
			exp = 0;
		}

		public void refresh(RandomSource random) {
			clear();
			for (var ent : stats) {
				ent.refresh(random);
			}
		}

		public void reroll(RegistryAccess access, RandomSource random) {
			clear();
			generate(access, random, stats.size());//TODO
		}

		public ArtifactStats immutable() {
			var sub = new ArrayList<>(stats.stream().map(StatEntry.Mutable::immutable).toList());
			return new ArtifactStats(ref.slot, rank, level, exp, old_level, scale, chance, setCount, ascend, sub);
		}

	}

}

package dev.xkmc.akhet_chronomaly.content.core;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.akhet_chronomaly.content.client.tab.DarkTextColorRanks;
import dev.xkmc.akhet_chronomaly.content.config.ArtifactSetConfig;
import dev.xkmc.akhet_chronomaly.content.set.core.PlayerOnlySetEffect;
import dev.xkmc.akhet_chronomaly.events.ArtifactEffectEvents;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import dev.xkmc.akhet_chronomaly.init.data.ACModConfig;
import dev.xkmc.akhet_chronomaly.init.registrate.ACTypeRegistry;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import dev.xkmc.l2core.util.Proxy;
import dev.xkmc.l2core.util.ServerProxy;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArtifactSet extends NamedEntry<ArtifactSet> {

	public record SetContext(int count, int[] ranks, int current_index) {

	}

	private static int[] remapRanks(int count, int[] rank) {
		for (int i = rank.length - 2; i >= 0; i--) {
			rank[i] += rank[i + 1];
		}
		// now rank[i] means how many items are equal or above this rank
		// ranks[i] means the maximum rank for count of i
		int[] ranks = new int[count + 1];
		for (int i = 0; i <= count; i++) { // count
			for (int j = 0; j < rank.length; j++) { // rank
				if (rank[j] >= i) {
					ranks[i] = Math.max(ranks[i], j);
				}
			}
		}
		return ranks;
	}

	private MutableComponent getCountDesc(int count) {
		int max = AkhetChronomaly.REGISTRATE.SET_MAP.get(getRegistryName()).size();
		return ACLang.getTranslate("set." + count, max);
	}

	public ArtifactSet() {
		super(ACTypeRegistry.SET);
	}

	public Optional<SetContext> getCountAndIndex(@Nullable SlotContext context) {
		LivingEntity e = context == null ? Proxy.getPlayer() : context.entity();
		var list = CuriosUtils.find(e, this);
		if (list.isEmpty()) return Optional.empty();
		int[] rank = new int[ACModConfig.SERVER.maxRank.get() + 1];
		int index = -1;
		int count = 0;
		for (SlotResult result : list) {
			if (result.stack().getItem() instanceof BaseArtifact) {
				rank[BaseArtifact.getRank(result.stack())]++;
				if (context != null && context.identifier().equals(result.slotContext().identifier()) &&
						context.index() == result.slotContext().index())
					index = count;
				count++;
			}
		}
		return Optional.of(new SetContext(count, remapRanks(count, rank), index));
	}


	public SetContext getSetCount(List<SlotResult> list) {
		int[] rank = new int[ACModConfig.SERVER.maxRank.get() + 1];
		int count = 0;
		for (SlotResult result : list) {
			if (result.stack().getItem() instanceof BaseArtifact) {
				rank[BaseArtifact.getRank(result.stack())]++;
				count++;
			}
		}
		return new SetContext(count, remapRanks(count, rank), -1);
	}

	public Optional<SetContext> getSetCount(LivingEntity e) {
		var list = CuriosUtils.find(e, this);
		if (list.isEmpty()) return Optional.empty();
		return Optional.of(getSetCount(list));
	}

	private List<ArtifactSetConfig.Entry> getConfig(LivingEntity e) {
		return getConfig(e.level().registryAccess());
	}

	private List<ArtifactSetConfig.Entry> getConfig(@Nullable RegistryAccess ctx) {
		if (ctx == null) return List.of();
		var ans = ACTypeRegistry.ARTIFACT_SETS.get(ctx, holder());
		return ans == null ? List.of() : ans.entries();
	}

	public void update(SlotContext context) {
		LivingEntity e = context.entity();
		Optional<SetContext> result = getCountAndIndex(context);
		if (result.isPresent()) {
			for (ArtifactSetConfig.Entry ent : getConfig(context.entity())) {
				ent.effect().update(e, ent, result.get().ranks()[ent.count()], result.get().count() >= ent.count());
			}
		}
	}

	public void tick(SlotContext context) {
		LivingEntity e = context.entity();
		Optional<SetContext> result = getCountAndIndex(context);
		if (result.isPresent() && result.get().current_index() == 0) {
			for (ArtifactSetConfig.Entry ent : getConfig(context.entity())) {
				ent.effect().tick(e, ent, result.get().ranks()[ent.count()], result.get().count() >= ent.count());
			}
		}
	}

	public <T> void propagateEvent(SetContext ctx, LivingEntity e, T event, ArtifactEffectEvents.EventConsumer<T> cons) {
		for (ArtifactSetConfig.Entry ent : getConfig(e)) {
			if (ctx.count() >= ent.count()) {
				cons.apply(ent.effect(), e, ent, ctx.ranks()[ent.count()], event);
			}
		}
	}

	public <T> boolean propagateEvent(SetContext ctx, LivingEntity e, T event, ArtifactEffectEvents.EventPredicate<T> cons) {
		boolean ans = false;
		for (ArtifactSetConfig.Entry ent : getConfig(e)) {
			if (ctx.count() >= ent.count()) {
				ans |= cons.apply(ent.effect(), e, ent, ctx.ranks()[ent.count()], event);
			}

		}
		return ans;
	}

	public List<MutableComponent> getAllDescs(ItemStack stack, boolean show) {
		List<MutableComponent> ans = new ArrayList<>();
		var list = getConfig(ServerProxy.getRegistryAccess());
		if (Proxy.getPlayer() != null) {
			Optional<SetContext> opt = getSetCount(Proxy.getPlayer());
			if (opt.isPresent()) {
				int rank = BaseArtifact.getRank(stack);
				SetContext ctx = opt.get();
				ans.add(ACLang.SET.get(Component.translatable(getDescriptionId()).withStyle(ChatFormatting.YELLOW)));
				if (show) {
					for (ArtifactSetConfig.Entry ent : list) {
						ChatFormatting color_count = ctx.count() < ent.count() ?
								ChatFormatting.GRAY : ChatFormatting.GREEN;
						ChatFormatting color_title = ctx.count() < ent.count() || ctx.ranks()[ent.count()] < rank ?
								ChatFormatting.GRAY : ChatFormatting.GREEN;
						ChatFormatting color_desc = ctx.count() < ent.count() || ctx.ranks()[ent.count()] < rank ?
								ChatFormatting.DARK_GRAY : ChatFormatting.DARK_GREEN;
						ans.add(getCountDesc(ent.count()).withStyle(color_count).append(ent.effect().getDesc().withStyle(color_title)));
						List<MutableComponent> desc = ent.effect().getDetailedDescription(rank);
						for (MutableComponent comp : desc) {
							ans.add(comp.withStyle(color_desc));
						}
					}
				}
			}
		}
		if (!show) {
			boolean playerOnly = false;
			for (var e : list) {
				playerOnly |= e.effect() instanceof PlayerOnlySetEffect;
			}
			if (playerOnly) {
				ans.add(ACLang.PLAYER_ONLY.get().withStyle(ChatFormatting.DARK_GRAY));
			}
		}
		return ans;
	}

	public List<Pair<List<Component>, List<Component>>> addComponents(SetContext ctx) {
		List<Pair<List<Component>, List<Component>>> ans = new ArrayList<>();//创建一个空list
		//获取一个SetEffect，List.of()把SetEffect对象转为只有一个元素的List，Pair.of()把两个List封装成一个Pair，Pair添加到ans
		ans.add(Pair.of(List.of(ACLang.ALL_SET_EFFECTS.get(getDesc(), ctx.count())), List.of()));
		var list = getConfig(ServerProxy.getRegistryAccess());
		for (ArtifactSetConfig.Entry ent : list) {
			//判断符合数量条件的效果与描述加入ans
			if (ctx.count() >= ent.count()) {//若数量符合
				int rank = ctx.ranks[ent.count()];//根据不同数量获取不同等级
				List<Component> a = List.of(getCountDesc(ent.count()), ent.effect().getDesc()
						.withStyle(DarkTextColorRanks.getDark(rank)));
				List<Component> b = new ArrayList<>();
				b.add(getCountDesc(ent.count()).append(ent.effect().getDesc()
						.withStyle(DarkTextColorRanks.getLight(rank))));
				b.addAll(ent.effect().getDetailedDescription(rank).stream()
						.map(comp -> (Component) comp.withStyle(DarkTextColorRanks.getDark(rank))).toList());
				ans.add(Pair.of(a, b));
			}
		}
		return ans;
	}

	public Item getItemIcon() {
		var arr = AkhetChronomaly.REGISTRATE.SET_MAP.get(getRegistryName());
		return arr.getItem(0, -1).getItem();
	}

	@Nullable
	public NonNullList<ItemStack> getTooltipItems() {
		var arr = AkhetChronomaly.REGISTRATE.SET_MAP.get(getRegistryName());
		NonNullList<ItemStack> ans = NonNullList.create();
		for (int i = 0; i < arr.size(); i++) {
			ans.add(arr.getItem(i, -1));
		}
		return ans;
	}
}

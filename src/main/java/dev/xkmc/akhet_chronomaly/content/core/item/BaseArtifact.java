package dev.xkmc.akhet_chronomaly.content.core.item;

import com.google.common.collect.Multimap;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import dev.xkmc.akhet_chronomaly.init.data.ACModConfig;
import dev.xkmc.akhet_chronomaly.init.registrate.ACItems;
import dev.xkmc.l2core.util.Proxy;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class BaseArtifact extends Item implements ICurioItem, IArtifact {

	public static final List<BaseArtifact> LIST = new ArrayList<>();

	public static void upgrade(ItemStack stack, int exp) {
		var stats = ACItems.STATS.get(stack);
		if (stats == null) return;
		ACItems.STATS.set(stack, stats.addExp(exp));
	}

	public static Optional<ArtifactStats> getStats(ItemStack stack) {
		return Optional.ofNullable(ACItems.STATS.get(stack));
	}

	public static ItemStack complete(ItemStack stack, ArtifactStats stats) {
		ACItems.STATS.set(stack, stats);
		return stack;
	}

	public static int getRank(ItemStack stack) {
		return Math.min(ACModConfig.SERVER.maxRank.get(), getStats(stack).map(ArtifactStats::rank).orElse(0));
	}

	public static int maxRank() {
		return 5;//TODO
	}

	public final Supplier<ArtifactSet> set;
	public final Supplier<ArtifactSlot> slot;

	public BaseArtifact(Properties properties, Supplier<ArtifactSet> set, Supplier<ArtifactSlot> slot) {
		super(properties.stacksTo(1));
		this.set = set;
		this.slot = slot;
		LIST.add(this);
	}

	@Override
	public ArtifactSet getSet() {
		return set.get();
	}

	public ItemStack withRank(RegistryAccess access, int rank) {
		return ACItems.STATS.set(getDefaultInstance(), ArtifactStats.withRank(access, slot.get(), rank));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		return resolve(level.registryAccess(), stack, level.isClientSide(), player.getRandom());
	}

	public InteractionResultHolder<ItemStack> resolve(RegistryAccess access, ItemStack stack, boolean isClient, RandomSource random) {
		var optStats = getStats(stack);
		if (optStats.isEmpty() || optStats.get().stats().isEmpty()) {
			if (!isClient) {
				ArtifactStats stats = optStats.map(e -> e.generate(access, random))
						.orElseGet(() -> ArtifactStats.generate(access, slot.get(), 0, random));
				complete(stack, stats);
			}
			return InteractionResultHolder.success(stack);
		} else {
			ArtifactStats stats = optStats.get();
			if (stats.level() > stats.old_level()) {
				if (!isClient) {
					stats = stats.upgrade(random);
					complete(stack, stats);
				}
				return InteractionResultHolder.success(stack);
			}
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		boolean shift = flag.hasShiftDown();
		if (Proxy.getClientPlayer() != null) {
			var stats = getStats(stack);
			if (stats.isEmpty() || stats.get().stats().isEmpty()) {
				list.add(ACLang.RAW_ARTIFACT.get());
			} else {
				stats.get().buildTooltip(ctx, list, flag);
			}
			set.get().getAllDescs(list, ctx, shift);
		}
		if (!shift) {
			list.add(ACLang.SHIFT_TEXT.get());
		}
	}

	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext ctx, ResourceLocation id, ItemStack stack) {
		var stats = getStats(stack);
		if (stats.isPresent()) {
			return stats.get().buildAttributes(ctx.entity().registryAccess(), id);
		}
		return ICurioItem.super.getAttributeModifiers(ctx, id, stack);
	}

}

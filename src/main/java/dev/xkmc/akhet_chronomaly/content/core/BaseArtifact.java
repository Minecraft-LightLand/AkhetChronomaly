package dev.xkmc.akhet_chronomaly.content.core;

import com.google.common.collect.Multimap;
import dev.xkmc.akhet_chronomaly.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import dev.xkmc.akhet_chronomaly.init.registrate.ACItems;
import dev.xkmc.l2core.init.L2LibReg;
import dev.xkmc.l2core.util.Proxy;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class BaseArtifact extends RankedItem implements ICurioItem {

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

	public final Supplier<ArtifactSet> set;
	public final Supplier<ArtifactSlot> slot;

	public BaseArtifact(Properties properties, Supplier<ArtifactSet> set, Supplier<ArtifactSlot> slot, int rank) {
		super(properties.stacksTo(1), rank);
		this.set = set;
		this.slot = slot;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		return resolve(level.registryAccess(), stack, level.isClientSide(), player.getRandom());
	}

	public InteractionResultHolder<ItemStack> resolve(RegistryAccess access, ItemStack stack, boolean isClient, RandomSource random) {
		var optStats = getStats(stack);
		if (optStats.isEmpty()) {
			if (!isClient) {
				ArtifactStats stats = ArtifactStats.generate(access, slot.get(), rank, random);
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
			if (stats.isEmpty()) {
				list.add(ACLang.RAW_ARTIFACT.get());
			} else {
				var s = stats.get();
				boolean max = s.level() == ArtifactUpgradeManager.getMaxLevel(s.rank());
				list.add(ACLang.ARTIFACT_LEVEL.get(s.level()).withStyle(max ? ChatFormatting.GOLD : ChatFormatting.WHITE));
				if (s.level() < ArtifactUpgradeManager.getMaxLevel(s.rank())) {
					if (shift)
						list.add(ACLang.ARTIFACT_EXP.get(s.exp(), ArtifactUpgradeManager.getExpForLevel(s.rank(), s.level())));
				}
				if (s.level() > s.old_level()) {
					list.add(ACLang.UPGRADE.get());
				} else if (!shift) {
					if (!s.stats().isEmpty()) {
						list.add(ACLang.STAT.get());
						for (StatEntry ent : s.stats()) {
							list.add(ent.getTooltip(null));
						}
					}
				}
			}
			list.addAll(set.get().getAllDescs(stack, shift));
			if (!shift)
				list.add(ACLang.EXP_CONVERSION.get(ArtifactUpgradeManager.getExpForConversion(rank, getStats(stack).orElse(null))));
		}
		if (!shift) {
			list.add(ACLang.SHIFT_TEXT.get());
		}
	}

	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext ctx, ResourceLocation id, ItemStack stack) {
		var stats = getStats(stack);
		if (stats.isPresent()) {
			return stats.get().buildAttributes(id);
		}
		return ICurioItem.super.getAttributeModifiers(ctx, id, stack);
	}

	@Override
	public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
		if (stack.getItem() instanceof BaseArtifact base) {
			base.set.get().update(slotContext);
		}
	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
		if (stack.getItem() instanceof BaseArtifact base) {
			base.set.get().update(slotContext);
		}
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		try {
			if (stack.getItem() instanceof BaseArtifact base) {
				base.set.get().tick(slotContext);
			}
		} catch (Exception e) {
			if (slotContext.entity() instanceof Player player) {
				L2LibReg.CONDITIONAL.type().getOrCreate(player).data.entrySet().removeIf(x -> x.getKey().type().equals(AkhetChronomaly.MODID));
				AkhetChronomaly.LOGGER.error("Player " + player + " has invalid artifact data for " + stack.getItem() + ". This could be a bug.");
			}
		}
	}

}

package dev.xkmc.akhet_chronomaly.content.misc;

import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactSet;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import dev.xkmc.akhet_chronomaly.init.registrate.ACItems;
import dev.xkmc.akhet_chronomaly.init.registrate.entries.SetEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class RandomArtifactItem extends RankedItem {

	public RandomArtifactItem(Properties props, int rank) {
		super(props, rank);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		player.awardStat(Stats.ITEM_USED.get(this));
		if (!level.isClientSide) {
			player.getInventory().placeItemBackInInventory(getRandomArtifact(player.level().registryAccess(), stack, rank, player.getRandom()));
		}
		if (!player.getAbilities().instabuild) {
			stack.shrink(1);
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	public static ItemStack setList(int rank, Collection<SetEntry<?>> sets) {
		ItemStack stack = ACItems.RANDOM[rank].asStack();
		return ACItems.GROUP.set(stack, SetGroup.of(sets));
	}

	@Nullable
	private static Collection<ArtifactSet> getList(ItemStack stack) {
		var group = ACItems.GROUP.get(stack);
		if (group == null) return null;
		return group.getSets(false);
	}

	public static ItemStack getRandomArtifact(RegistryAccess access, ItemStack stack, int rank, RandomSource random) {
		var list = getList(stack);
		if (list == null) list = ArtifactSet.getAll();
		var sets = list.stream()
				.flatMap(e -> e.getLink().getItems().stream()).toList();
		var arr = sets.get(random.nextInt(sets.size()));
		return arr.withRank(access, rank);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		var sets = getList(stack);
		if (sets == null) {
			list.add(ACLang.LOOT_POOL_ALL.get());
		} else {
			list.add(ACLang.LOOT_POOL.get());
			for (var e : sets) {
				list.add(e.getDesc().withStyle(ChatFormatting.GRAY));
			}
		}
	}
}

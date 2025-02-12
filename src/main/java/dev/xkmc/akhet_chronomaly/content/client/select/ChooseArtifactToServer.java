package dev.xkmc.akhet_chronomaly.content.client.select;

import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactSet;
import dev.xkmc.akhet_chronomaly.content.misc.SelectArtifactItem;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


public record ChooseArtifactToServer(
		ArtifactSet set, int slot, int rank
) implements SerialPacketBase<ChooseArtifactToServer> {

	@Override
	public void handle(Player pl) {
		if (!(pl instanceof ServerPlayer player)) return;
		ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (!(stack.getItem() instanceof SelectArtifactItem)) {
			stack = player.getItemInHand(InteractionHand.OFF_HAND);
		}
		if (!(stack.getItem() instanceof SelectArtifactItem)) {
			return;
		}
		if (slot >= set.getLink().size()) return;
		if (!player.getAbilities().instabuild)
			stack.shrink(1);
		ItemStack artifact = set.getLink().getItem(slot, rank);
		player.getInventory().placeItemBackInInventory(artifact);
	}
}

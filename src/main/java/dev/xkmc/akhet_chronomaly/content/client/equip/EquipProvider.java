package dev.xkmc.akhet_chronomaly.content.client.equip;

import dev.xkmc.akhet_chronomaly.init.registrate.ACMenuRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record EquipProvider(ServerPlayer sp, EquipHandler handler) implements MenuProvider {

	@Override
	public Component getDisplayName() {
		return Component.translatable(ACMenuRegistry.getLangKey(ACMenuRegistry.EQUIP.get()));
	}

	@Override
	public AbstractContainerMenu createMenu(int wid, Inventory inv, Player player) {
		return new EquipMenu(ACMenuRegistry.EQUIP.get(), wid, inv, handler);
	}

}

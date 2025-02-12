package dev.xkmc.akhet_chronomaly.content.client.equip;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public class EquipSlot extends SlotItemHandler {

	private final EquipHandler handler;
	private final EquipHandler.SlotHolder holder;

	public EquipSlot(EquipHandler handler, int index, int x, int y) {
		super(handler, index, x, y);
		this.handler = handler;
		this.holder = handler.holders[index];
		holder.slot = this;
	}

	@Override
	public boolean isActive() {
		return holder.isEnabled();
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		return 1;
	}

	@Override
	public boolean mayPickup(Player playerIn) {
		return true;
	}

	@Override
	public void setChanged() {
		handler.verify();
	}

}

package dev.xkmc.akhet_chronomaly.content.client.equip;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class EquipMenu extends AbstractContainerMenu {

	public static EquipMenu fromNetwork(MenuType<?> type, int wid, Inventory inv) {
		return new EquipMenu(type, wid, inv, new EquipHandler.DummyHandler());
	}

	protected final EquipHandler handler;

	public EquipMenu(MenuType<?> type, int wid, Inventory inv, EquipHandler handler) {
		super(type, wid);
		this.handler = handler;
		bindPlayerInventory(inv, 32, 133);
		addSlots(handler);
	}

	private void addSlots(EquipHandler handler) {
		// head
		addSlot(new EquipSlot(handler, 0, 61, 16));
		// body
		addSlot(new EquipSlot(handler, 1, 47, 44));
		// cape
		addSlot(new EquipSlot(handler, 2, 75, 44));
		// belt
		addSlot(new EquipSlot(handler, 3, 47, 72));
		// pants
		addSlot(new EquipSlot(handler, 4, 75, 72));
		// boots
		addSlot(new EquipSlot(handler, 5, 61, 100));
		// left pauldron
		addSlot(new EquipSlot(handler, 6, 19, 30));
		// right pauldron
		addSlot(new EquipSlot(handler, 7, 103, 30));
		// left bracer
		addSlot(new EquipSlot(handler, 8, 19, 58));
		// right bracer
		addSlot(new EquipSlot(handler, 9, 103, 58));
		// left glove
		addSlot(new EquipSlot(handler, 10, 19, 86));
		// right glove
		addSlot(new EquipSlot(handler, 11, 103, 86));
		// necklace
		addSlot(new EquipSlot(handler, 12, 131, 30));
		// left ring
		addSlot(new EquipSlot(handler, 13, 131, 58));
		// right ring
		addSlot(new EquipSlot(handler, 14, 131, 86));
		// charm
		addSlot(new EquipSlot(handler, 15, 163, 30));
		// charm
		addSlot(new EquipSlot(handler, 16, 163, 58));
		// charm
		addSlot(new EquipSlot(handler, 17, 163, 86));
	}

	protected void bindPlayerInventory(Inventory plInv, int x, int y) {
		int k;
		for (k = 0; k < 3; ++k) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(plInv, j + k * 9 + 9, x + j * 18, y + k * 18));
			}
		}
		for (k = 0; k < 9; ++k) {
			this.addSlot(new Slot(plInv, k, x + k * 18, y + 58));
		}

	}

	public ItemStack quickMoveStack(Player pl, int id) {
		ItemStack stack = this.slots.get(id).getItem();
		int n = handler.getSlots();
		boolean moved;
		if (id >= 36) {
			moved = this.moveItemStackTo(stack, 0, 36, true);
		} else {
			moved = this.moveItemStackTo(stack, 36, 36 + n, false);
		}
		if (moved) {
			this.slots.get(id).setChanged();
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

}

package dev.xkmc.akhet_chronomaly.content.client.equip;

import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactSet;
import dev.xkmc.akhet_chronomaly.content.core.item.BaseArtifact;
import dev.xkmc.akhet_chronomaly.content.core.item.BaseCharmArtifact;
import dev.xkmc.akhet_chronomaly.content.core.item.IArtifact;
import dev.xkmc.akhet_chronomaly.init.data.ACSlotCuriosType;
import dev.xkmc.akhet_chronomaly.init.registrate.ACItems;
import net.minecraft.util.Unit;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.ArrayList;
import java.util.List;

public class EquipHandler implements IItemHandlerModifiable {

	protected final SlotHolder[] holders;

	protected EquipHandler() {
		List<SlotHolder> list = new ArrayList<>();
		for (var e : ACSlotCuriosType.values()) {
			for (int i = 0; i < e.count; i++) {
				list.add(new SlotHolder(this, e, i, list.size()));
			}
		}
		holders = list.toArray(SlotHolder[]::new);
	}

	@Nullable
	protected ArtifactSet getSet() {
		for (int i = 0; i < 6; i++) {
			ItemStack stack = getStackInSlot(i);
			if (stack.isEmpty()) continue;
			if (stack.getItem() instanceof BaseArtifact item) {
				return item.set.get();
			}
		}
		return null;
	}

	protected void verify() {
	}

	@Override
	public int getSlots() {
		return holders.length;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot < 0 || slot >= holders.length) return ItemStack.EMPTY;
		var holder = holders[slot];
		if (holder.access == null) return ItemStack.EMPTY;
		return holder.access.getItem();
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		if (slot < 0 || slot >= holders.length) return;
		var holder = holders[slot];
		if (holder.access == null) return;
		holder.access.setItem(stack);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		if (slot < 0 || slot >= holders.length) return false;
		return holders[slot].isValid(stack);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (slot < 0 || slot >= holders.length) return stack;
		var holder = holders[slot];
		if (holder.access == null || !holder.isValid(stack)) return stack;
		return holder.access.insertItem(stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (slot < 0 || slot >= holders.length || amount <= 0) return ItemStack.EMPTY;
		var holder = holders[slot];
		if (holder.access == null) return ItemStack.EMPTY;
		return holder.access.extractItem(simulate);
	}

	public interface SlotAccess {

		boolean isValid(ItemStack stack);

		ItemStack insertItem(ItemStack stack, boolean simulate);

		ItemStack extractItem(boolean simulate);

		ItemStack getItem();

		void setItem(ItemStack stack);
	}

	public static class SlotHolder {

		private final EquipHandler parent;
		protected final ACSlotCuriosType type;
		protected final int slotIndex, menuIndex;

		@Nullable
		protected EquipSlot slot;

		@Nullable
		protected SlotAccess access;

		public SlotHolder(EquipHandler parent, ACSlotCuriosType e, int slotIndex, int menuIndex) {
			this.parent = parent;
			this.type = e;
			this.slotIndex = slotIndex;
			this.menuIndex = menuIndex;
		}

		public boolean isEnabled() {
			if (access == null) return false;
			ArtifactSet set = parent.getSet();
			return set != null || menuIndex < 6;
		}

		public boolean isValid(ItemStack stack) {
			if (access == null || !access.isValid(stack)) return false;
			ArtifactSet set = parent.getSet();
			if (set == null && menuIndex >= 6) return false;
			if (!(stack.getItem() instanceof IArtifact item)) return false;
			if (set != null && item.getSet() != set) return false;
			if (type.slot != null) {
				return stack.getItem() instanceof BaseArtifact art && art.slot.get() == type.slot;
			}
			return stack.getItem() instanceof BaseCharmArtifact;
		}

	}

	public record CurioSlot(
			SlotHolder holder,
			boolean canFlip,
			IDynamicStackHandler handler,
			int index
	) implements SlotAccess {

		@Override
		public boolean isValid(ItemStack stack) {
			return index < handler.getSlots() && handler.isItemValid(index, stack);
		}

		@Override
		public ItemStack insertItem(ItemStack stack, boolean simulate) {
			if (index >= handler.getSlots()) return stack;
			return handler.insertItem(index, stack, simulate);
		}

		@Override
		public void setItem(ItemStack stack) {
			if (index >= handler.getSlots()) return;
			if (canFlip && !stack.isEmpty()) {
				if (index == 0) stack.remove(ACItems.FLIP);
				else ACItems.FLIP.set(stack, Unit.INSTANCE);
				handler.setStackInSlot(index, stack);
			} else {
				handler.setStackInSlot(index, stack);
			}
		}

		@Override
		public ItemStack extractItem(boolean simulate) {
			return index >= handler.getSlots() ? ItemStack.EMPTY : handler.extractItem(index, 1, simulate);
		}

		@Override
		public ItemStack getItem() {
			return index >= handler.getSlots() ? ItemStack.EMPTY : handler.getStackInSlot(index);
		}
	}

	public record DummySlot(IItemHandlerModifiable container, int index) implements SlotAccess {

		@Override
		public boolean isValid(ItemStack stack) {
			return true;
		}

		@Override
		public ItemStack insertItem(ItemStack stack, boolean simulate) {
			return container.insertItem(index, stack, simulate);
		}

		@Override
		public ItemStack extractItem(boolean simulate) {
			return container.extractItem(index, 1, simulate);
		}

		@Override
		public void setItem(ItemStack stack) {
			container.setStackInSlot(index, stack);
		}

		@Override
		public ItemStack getItem() {
			return container.getStackInSlot(index);
		}
	}

	public static class CurioHandler extends EquipHandler {

		private final Player player;

		public CurioHandler(Player player) {
			super();
			this.player = player;
			var opt = CuriosApi.getCuriosInventory(player);
			if (opt.isPresent()) {
				for (var e : holders) {
					String id = e.type.getIdentifier();
					var stacks = opt.get().getStacksHandler(id);
					if (stacks.isPresent()) {
						var handler = stacks.get().getStacks();
						if (handler.getSlots() > e.slotIndex)
							e.access = new CurioSlot(e, e.type.count == 2, handler, e.slotIndex);
					}
				}
			}
		}

		@Override
		protected void verify() {
			super.verify();
			if (getSet() != null) return;
			for (var e : holders) {
				if (e.access == null) continue;
				if (e.access.getItem().isEmpty()) continue;
				ItemStack stack = e.access.extractItem(false);
				if (stack.isEmpty()) continue;
				player.getInventory().placeItemBackInInventory(stack);
			}
		}
	}

	public static class DummyHandler extends EquipHandler {

		public DummyHandler() {
			super();
			var container = new InvWrapper(new SimpleContainer(holders.length));
			for (var e : holders) {
				e.access = new DummySlot(container, e.menuIndex);
			}
		}

	}

}

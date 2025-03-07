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

	@Nullable
	protected ArtifactSet set;
	protected int a, l, r, c, total;

	private boolean doUpdate = false, updating = false;
	private volatile boolean shouldUpdate = false;

	protected EquipHandler() {
		List<SlotHolder> list = new ArrayList<>();
		for (var e : ACSlotCuriosType.values()) {
			for (int i = 0; i < e.count; i++) {
				list.add(new SlotHolder(this, e, i, list.size()));
			}
		}
		holders = list.toArray(SlotHolder[]::new);
	}


	public void updating(boolean b) {
		doUpdate = b;
		if (doUpdate) {
			verifyImpl();
		}
	}

	public final void verify() {
		if (!doUpdate) return;
		if (updating) {
			shouldUpdate = true;
			return;
		}
		updating = true;
		shouldUpdate = true;
		while (shouldUpdate) {
			shouldUpdate = false;
			verifyImpl();
		}
		updating = false;
	}

	protected void verifyImpl() {
		set = null;
		a = l = r = c = 0;
		for (int i = 0; i < 6; i++) {
			ItemStack stack = getStackInSlot(i);
			if (stack.isEmpty()) continue;
			a++;
			if (set == null && stack.getItem() instanceof BaseArtifact item) {
				set = item.getSet().root();
			}
		}
		for (int i = 6; i < 12; i++) {
			ItemStack stack = getStackInSlot(i);
			if (stack.isEmpty()) continue;
			if (i % 2 == 0) l++;
			else r++;
		}
		for (int i = 12; i < 15; i++) {
			ItemStack stack = getStackInSlot(i);
			if (stack.isEmpty()) continue;
			c++;
		}
		total = a + l + r + c;
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
			if (menuIndex < 6) return true;
			if (menuIndex < 12) return parent.a > 0;
			return switch (menuIndex) {
				case 12 -> parent.a >= 3;
				case 13 -> parent.l > 0;
				case 14 -> parent.r > 0;
				case 15 -> parent.total >= 9;
				case 16 -> parent.total >= 12;
				case 17 -> parent.total >= 15;
				default -> true;
			};
		}

		public boolean isValid(ItemStack stack) {
			if (access == null || !access.isValid(stack)) return false;
			if (!isEnabled()) return false;
			ArtifactSet set = parent.set;
			if (!(stack.getItem() instanceof IArtifact item)) return false;
			if (set != null && item.getSet().root() != set) return false;
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
		protected void verifyImpl() {
			super.verifyImpl();
			for (var e : holders) {
				if (e.access == null) continue;
				if (e.isEnabled()) continue;
				ItemStack stack = e.access.getItem();
				if (stack.isEmpty()) continue;
				player.getInventory().placeItemBackInInventory(stack);
				e.access.setItem(ItemStack.EMPTY);
				verify();
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

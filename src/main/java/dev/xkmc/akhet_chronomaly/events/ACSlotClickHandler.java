package dev.xkmc.akhet_chronomaly.events;

import dev.xkmc.akhet_chronomaly.content.core.BaseArtifact;
import dev.xkmc.akhet_chronomaly.init.registrate.ACItems;
import dev.xkmc.l2menustacker.click.writable.ClickedPlayerSlotResult;
import dev.xkmc.l2menustacker.click.writable.WritableStackClickHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;

public class ACSlotClickHandler extends WritableStackClickHandler {

	public static void flip(ItemStack stack) {
		if (ACItems.FLIP.get(stack) == null)
			ACItems.FLIP.set(stack, Unit.INSTANCE);
		else stack.remove(ACItems.FLIP);
	}

	public ACSlotClickHandler(ResourceLocation rl) {
		super(rl);
	}

	@Override
	protected void handle(ServerPlayer sp, ClickedPlayerSlotResult slot) {
		flip(slot.stack());
		slot.container().update();
	}

	@Override
	public boolean isAllowed(ItemStack stack) {
		return stack.getItem() instanceof BaseArtifact item && item.slot.get().mayFlip();
	}
}

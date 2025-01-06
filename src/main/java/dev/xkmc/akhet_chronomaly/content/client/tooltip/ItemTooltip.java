package dev.xkmc.akhet_chronomaly.content.client.tooltip;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record ItemTooltip(NonNullList<ItemStack> list) implements TooltipComponent {

}

package dev.xkmc.akhet_chronomaly.content.client.equip;

import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EquipScreen extends AbstractContainerScreen<EquipMenu> {

	private static final ResourceLocation TEX = AkhetChronomaly.loc("textures/gui/container/equip.png");

	public EquipScreen(EquipMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		imageWidth = 238;
		imageHeight = 216;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float pt) {
		super.render(g, mx, my, pt);
		this.renderTooltip(g, mx, my);
	}

	@Override
	protected void renderLabels(GuiGraphics g, int mx, int my) {

	}

	@Override
	protected void renderBg(GuiGraphics g, float pt, int mx, int my) {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		g.blit(TEX, i, j, 0, 0, this.imageWidth, this.imageHeight);

	}

}

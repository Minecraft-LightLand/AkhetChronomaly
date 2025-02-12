package dev.xkmc.akhet_chronomaly.content.client.equip;

import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EquipScreen extends AbstractContainerScreen<EquipMenu> {

	private static final ResourceLocation TEX = AkhetChronomaly.loc("textures/gui/container/equip.png");
	private static final ResourceLocation SLOT = AkhetChronomaly.loc("equip_slot");
	private static final ResourceLocation CANDLE_L = AkhetChronomaly.loc("candle_left");
	private static final ResourceLocation CANDLE_R = AkhetChronomaly.loc("candle_right");
	private static final ResourceLocation FLAG_L = AkhetChronomaly.loc("flag_left");
	private static final ResourceLocation FLAG_R = AkhetChronomaly.loc("flag_right");
	private static final ResourceLocation FACE_UP = AkhetChronomaly.loc("face_up");
	private static final ResourceLocation FACE_DOWN = AkhetChronomaly.loc("face_down");

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
		for (var e : menu.handler.holders) {
			if (e.slot != null && e.slot.isActive()) {
				g.blitSprite(SLOT, i + e.slot.x - 5, j + e.slot.y - 5, 26, 26);
			}
		}
		g.blitSprite(CANDLE_L, i, j + 104, 24, 24);
		g.blitSprite(CANDLE_R, i + 200, j + 104, 24, 24);
		g.blitSprite(FLAG_L, i, j + 128, 24, 24);
		g.blitSprite(FLAG_R, i + 200, j + 128, 24, 24);
		g.blitSprite(FACE_UP, i + 186, j + 13, 35, 50);
		g.blitSprite(FACE_DOWN, i + 186, j + 69, 35, 50);
	}

}

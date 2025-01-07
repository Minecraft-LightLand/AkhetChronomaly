package dev.xkmc.akhet_chronomaly.content.client.select;

import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import dev.xkmc.l2core.base.menu.base.SpriteManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class SetSelectScreen extends AbstractSelectScreen {

	private static final SpriteManager MANAGER = new SpriteManager(AkhetChronomaly.MODID, "set_select");

	public SetSelectScreen() {
		super(ACLang.TITLE_SELECT_SET.get(), MANAGER, "grid");
	}

	@Override
	protected void renderLabels(GuiGraphics g, int mx, int my) {
		g.drawString(font, ACLang.TITLE_SELECT_SET.get(), 8, 6, 4210752, false);
	}

	@Override
	protected ItemStack getStack(String comp, int x, int y) {
		int ind = x + y * 9;
		if (ind >= AkhetChronomaly.REGISTRATE.SET_LIST.size()) return ItemStack.EMPTY;
		var arr = AkhetChronomaly.REGISTRATE.SET_LIST.get(ind).items[0];
		return arr[arr.length - 1].asStack();
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (super.mouseClicked(mx, my, button)) {
			return true;
		}
		SlotResult result = findSlot(mx, my);
		if (result == null) return false;
		int ind = result.x() + result.y() * 9;
		if (ind >= AkhetChronomaly.REGISTRATE.SET_LIST.size()) return false;
		Minecraft.getInstance().setScreen(new SlotSelectScreen(ind));
		return true;
	}
}

package dev.xkmc.akhet_chronomaly.content.client.select;

import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import dev.xkmc.l2core.base.menu.base.SpriteManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class SlotSelectScreen extends AbstractSelectScreen {

	private static final SpriteManager MANAGER = new SpriteManager(AkhetChronomaly.MODID, "slot_select");

	private final int set;

	protected SlotSelectScreen(int set) {
		super(ACLang.TITLE_SELECT_SLOT.get(), MANAGER, "set", "slot");
		this.set = set;
	}

	@Override
	protected void renderLabels(GuiGraphics g, int mx, int my) {
		g.drawString(font, ACLang.TITLE_SELECT_SET.get(), 8, 6, 4210752, false);
		g.drawString(font, ACLang.TITLE_SELECT_SLOT.get(), 8, 6 + 13 + 18, 4210752, false);
	}

	@Override
	protected ItemStack getStack(String comp, int x, int y) {
		var setEntry = AkhetChronomaly.REGISTRATE.SET_LIST.get(set);
		if (comp.equals("set")) return setEntry.getItem(0, -1);
		return setEntry.getItem(x, -1);
	}


	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (super.mouseClicked(mx, my, button)) {
			return true;
		}
		SlotResult result = findSlot(mx, my);
		if (result == null) return false;
		if (result.name().equals("set")) {
			Minecraft.getInstance().setScreen(new SetSelectScreen());
			return true;
		}
		int ind = result.x();
		var setEntry = AkhetChronomaly.REGISTRATE.SET_LIST.get(set);
		if (ind >= setEntry.size()) return false;
		Minecraft.getInstance().setScreen(new RankSelectScreen(set, ind));
		return true;
	}

}

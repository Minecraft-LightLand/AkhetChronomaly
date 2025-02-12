package dev.xkmc.akhet_chronomaly.content.client.select;

import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactSet;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import dev.xkmc.l2core.base.menu.base.SpriteManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SetSelectScreen extends AbstractSelectScreen {

	private static final SpriteManager MANAGER = new SpriteManager(AkhetChronomaly.MODID, "set_select");

	private final List<ArtifactSet> sets = ArtifactSet.getAll();

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
		if (ind >= sets.size()) return ItemStack.EMPTY;
		return sets.get(ind).getLink().getItem(0, -1);
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (super.mouseClicked(mx, my, button)) {
			return true;
		}
		SlotResult result = findSlot(mx, my);
		if (result == null) return false;
		int ind = result.x() + result.y() * 9;
		if (ind >= sets.size()) return false;
		Minecraft.getInstance().setScreen(new SlotSelectScreen(sets.get(ind)));
		return true;
	}
}

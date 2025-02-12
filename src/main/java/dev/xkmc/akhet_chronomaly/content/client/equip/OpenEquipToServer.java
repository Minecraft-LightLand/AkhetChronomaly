package dev.xkmc.akhet_chronomaly.content.client.equip;

import dev.xkmc.l2menustacker.screen.base.ScreenTracker;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record OpenEquipToServer(

) implements SerialPacketBase<OpenEquipToServer> {

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		ScreenTracker.onServerOpen(sp);
		sp.openMenu(new EquipProvider(sp, new EquipHandler.CurioHandler(sp)));
	}

}

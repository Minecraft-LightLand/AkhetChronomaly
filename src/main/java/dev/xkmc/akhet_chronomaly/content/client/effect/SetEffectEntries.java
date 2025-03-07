package dev.xkmc.akhet_chronomaly.content.client.effect;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.akhet_chronomaly.init.registrate.ACTypeRegistry;
import dev.xkmc.l2library.util.TextWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public record SetEffectEntries(
		int count, int size,
		List<Pair<List<FormattedCharSequence>, List<Component>>> text
) {

	public static List<List<SetEffectEntries>> aggregate(Player player, int width, int linePerPage) {
		var map = ACTypeRegistry.CAP.type().getOrCreate(player).getSets();
		if (map.isEmpty()) return List.of();
		List<SetEffectEntries> raw = new ArrayList<>();
		for (var ent : map.entrySet()) {
			List<Pair<List<FormattedCharSequence>, List<Component>>> ans = new ArrayList<>();
			var list = ent.getKey().addComponents(player.level().registryAccess(), ent.getValue().count());
			int size = 0;
			for (var pair : list) {
				var lines = TextWrapper.wrapText(Minecraft.getInstance().font, pair.getFirst(), width - 16);
				size += lines.size();
				ans.add(Pair.of(lines, pair.getSecond()));
			}
			raw.add(new SetEffectEntries(ent.getValue().count(), size, ans));
		}
		List<List<SetEffectEntries>> ans = new ArrayList<>();
		List<SetEffectEntries> current = new ArrayList<>();
		int size = 0;
		for (var e : raw) {
			if (size > 0 && size + e.size() > linePerPage) {
				ans.add(current);
				current = new ArrayList<>();
				size = 0;
			}
			size += e.size();
			current.add(e);
		}
		ans.add(current);
		return ans;
	}

}

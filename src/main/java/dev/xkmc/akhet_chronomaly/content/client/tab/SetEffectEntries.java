package dev.xkmc.akhet_chronomaly.content.client.tab;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.akhet_chronomaly.content.core.ArtifactSet;
import dev.xkmc.akhet_chronomaly.content.core.BaseArtifact;
import dev.xkmc.akhet_chronomaly.content.core.CuriosUtils;
import dev.xkmc.l2library.util.TextWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public record SetEffectEntries(
		ArtifactSet.SetContext set, int size,
		List<Pair<List<FormattedCharSequence>, List<Component>>> text
) {

	public static List<List<SetEffectEntries>> aggregate(@Nullable LivingEntity le, int width, int linePerPage) {
		var slots = CuriosUtils.findAll(le);
		if (slots.isEmpty()) return List.of();
		List<SetEffectEntries> raw = new ArrayList<>();
		for (SlotResult sr : slots) {
			ItemStack stack = sr.stack();//玩家身上的饰品
			BaseArtifact base = (BaseArtifact) stack.getItem();
			var optSet = base.set.get().getCountAndIndex(sr.slotContext());
			if (optSet.isEmpty()) continue;
			var result = optSet.get();
			if (result.current_index() > 0) continue;
			List<Pair<List<FormattedCharSequence>, List<Component>>> ans = new ArrayList<>();
			var list = base.set.get().addComponents(result);
			int size = 0;
			for (var pair : list) {
				var lines = TextWrapper.wrapText(Minecraft.getInstance().font, pair.getFirst(), width - 16);
				size += lines.size();
				ans.add(Pair.of(lines, pair.getSecond()));
			}
			raw.add(new SetEffectEntries(result, size, ans));
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
		if (!current.isEmpty()) ans.add(current);
		return ans;
	}

}

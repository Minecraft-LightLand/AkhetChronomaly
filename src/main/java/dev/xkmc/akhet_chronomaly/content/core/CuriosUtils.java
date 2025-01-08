package dev.xkmc.akhet_chronomaly.content.core;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CuriosUtils {

	public static Map<ArtifactSet, ArtifactSet.SetContext> findAll(@Nullable LivingEntity le) {
		if (le == null) return Map.of();
		var opt = CuriosApi.getCuriosInventory(le);
		if (opt.isEmpty()) return Map.of();
		var list = opt.get().findCurios(stack -> stack.getItem() instanceof BaseArtifact);
		Map<ArtifactSet, List<SlotResult>> map = new LinkedHashMap<>();
		for (var e : list) {
			if (e.stack().getItem() instanceof BaseArtifact art) {
				map.computeIfAbsent(art.set.get(), k -> new ArrayList<>()).add(e);
			}
		}
		Map<ArtifactSet, ArtifactSet.SetContext> ans = new LinkedHashMap<>();
		for (var ent : map.entrySet()) {
			ans.put(ent.getKey(), ent.getKey().getSetCount(ent.getValue()));
		}
		return ans;
	}

	public static List<SlotResult> find(@Nullable LivingEntity le, ArtifactSet set) {
		if (le == null) return List.of();
		var opt = CuriosApi.getCuriosInventory(le);
		if (opt.isEmpty()) return List.of();
		return opt.get().findCurios(stack -> stack.getItem() instanceof BaseArtifact art && art.set.get() == set);
	}

}

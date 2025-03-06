package dev.xkmc.akhet_chronomaly.content.core.data;

import dev.xkmc.akhet_chronomaly.content.core.bonus.BonusHolder;
import dev.xkmc.akhet_chronomaly.content.core.bonus.BonusModifier;
import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactSet;
import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactSlot;
import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactStats;
import dev.xkmc.akhet_chronomaly.content.core.item.BaseArtifact;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.TriggerType;
import dev.xkmc.akhet_chronomaly.init.registrate.ACTypeRegistry;
import dev.xkmc.l2core.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2library.util.GenericItemStack;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.*;

@SerialClass
public class AkhetCapability extends PlayerCapabilityTemplate<AkhetCapability> {

	@SerialField
	private final Map<ArtifactSet, SetStat> setMap = new LinkedHashMap<>();
	@SerialField
	private final Map<ArtifactSet, AkhetSetData> dataMap = new LinkedHashMap<>();
	@SerialField
	private final Map<String, BonusHolder> bonuses = new LinkedHashMap<>();

	@Override
	public void tick(Player player) {
		Set<ArtifactSet> total = new LinkedHashSet<>(setMap.keySet());
		updateSet(player);
		total.addAll(setMap.keySet());
		for (var set : total) {
			SetStat newStat = setMap.getOrDefault(set, SetStat.EMPTY);
			AkhetSetData data = dataMap.computeIfAbsent(set, k -> new AkhetSetData());
			var effects = set.getConfig(player.level().registryAccess());
			List<Holder<SetEffect>> list = effects == null ? List.of() : effects.list();
			if (data.flags == null || data.flags.length != list.size()) {
				data.flags = new int[list.size()];
			}
			for (int i = 0; i < list.size(); i++) {
				var holder = list.get(i);
				var id = holder.unwrapKey().orElseThrow().location();
				SetEffectContext ctx = new SetEffectContext(player, holder, this, id, Optional.empty());
				data.flags[i] = holder.value().tick(ctx, data.flags[i], holder.value().count() >= newStat.count());
			}
			if (!setMap.containsKey(set)) {
				dataMap.remove(set);
			}
		}
		for (var ent : bonuses.values()) {
			ent.tick();
		}
	}

	public <R> void trigger(Player player, TriggerType<R> type, R event) {
		for (var ent : setMap.entrySet()) {
			var effects = ent.getKey().getConfig(player.level().registryAccess());
			List<Holder<SetEffect>> list = effects == null ? List.of() : effects.list();
			for (Holder<SetEffect> holder : list) {
				var id = holder.unwrapKey().orElseThrow().location();
				SetEffectContext ctx = new SetEffectContext(player, holder, this, id, Optional.of(event));
				holder.value().trigger(ctx, type, event);
			}
		}
	}

	private void updateSet(Player player) {
		var curio = CuriosApi.getCuriosInventory(player);
		if (curio.isEmpty()) {
			setMap.clear();
			return;
		}
		List<GenericItemStack<BaseArtifact>> list = new ArrayList<>();
		for (var slot : ACTypeRegistry.SLOT.reg()) {
			var handler = curio.get().getStacksHandler(slot.getCurioIdentifier());
			if (handler.isEmpty()) continue;
			for (int i = 0; i < handler.get().getSlots(); i++) {
				ItemStack stack = handler.get().getStacks().getStackInSlot(i);
				if (stack.getItem() instanceof BaseArtifact) {
					list.add(GenericItemStack.of(stack));
				}
			}
		}
		Map<ArtifactSet, SetStatBuilder> map = new LinkedHashMap<>();
		for (var e : list) {
			var set = e.item().set.get();
			map.computeIfAbsent(set, k -> new SetStatBuilder()).add(e);
		}
		for (var ent : map.entrySet()) {
			setMap.put(ent.getKey(), ent.getValue().toImmutable());
		}
	}

	public Map<ArtifactSet, SetStat> getSets() {
		return setMap;
	}

	public BonusHolder getBonus(String type) {
		return bonuses.getOrDefault(type, new BonusHolder());
	}

	public void addModifier(String type, ResourceLocation id, BonusModifier modifier) {
		bonuses.computeIfAbsent(type, k -> new BonusHolder()).addModifier(id, modifier);
	}

	public void removeModifier(String type, ResourceLocation id) {
		var holder = bonuses.get(type);
		if (holder != null) holder.removeModifier(id);
	}

	public static class SetStatBuilder {

		private int count;

		private final ArrayList<ItemStack> stacks = new ArrayList<>();

		private final LinkedHashSet<ArtifactSlot> slots = new LinkedHashSet<>();

		protected void add(GenericItemStack<BaseArtifact> e) {
			count += BaseArtifact.getStats(e.stack()).map(ArtifactStats::setCount).orElse(1);
			stacks.add(e.stack());
			slots.add(e.item().slot.get());
		}

		public SetStat toImmutable() {
			return new SetStat(count, stacks, slots);
		}

	}

	public record SetStat(int count, ArrayList<ItemStack> stacks, LinkedHashSet<ArtifactSlot> slots) {

		public static final SetStat EMPTY = new SetStat(0, new ArrayList<>(), new LinkedHashSet<>());

	}

}

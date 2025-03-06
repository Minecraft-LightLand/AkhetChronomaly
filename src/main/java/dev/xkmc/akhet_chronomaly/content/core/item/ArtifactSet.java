package dev.xkmc.akhet_chronomaly.content.core.item;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.akhet_chronomaly.content.core.data.SetConfig;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.TooltipConsumer;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import dev.xkmc.akhet_chronomaly.init.registrate.ACTypeRegistry;
import dev.xkmc.akhet_chronomaly.init.registrate.entries.SetEntry;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ArtifactSet extends NamedEntry<ArtifactSet> {

	private static final List<ArtifactSet> SETS = new ArrayList<>();

	public static void load() {
		for (var e : BaseArtifact.LIST) {
			e.set.get().link.add(e);
		}
		for (var e : ACTypeRegistry.SET.get()) {
			SETS.add(e);
		}
	}

	public static List<ArtifactSet> getAll() {
		return SETS;
	}

	private final SetLink link = new SetLink();
	private SetEntry<?> root = null;

	public ArtifactSet() {
		super(ACTypeRegistry.SET);
	}

	public ArtifactSet(@Nullable SetEntry<?> root) {
		this();
		this.root = root;
	}

	public SetLink getLink() {
		return link;
	}

	@Nullable
	public SetConfig getConfig(RegistryAccess access) {
		return ACTypeRegistry.SET_CONFIG.get(access, holder());
	}

	public Item getItemIcon() {
		return link.getItem(0, -1).getItem();
	}

	@Nullable
	public NonNullList<ItemStack> getTooltipItems() {
		NonNullList<ItemStack> ans = NonNullList.create();
		for (int i = 0; i < link.size(); i++) {
			ans.add(link.getItem(i, -1));
		}
		return ans;
	}

	public List<Pair<List<Component>, List<Component>>> addComponents(int count) {
		List<Pair<List<Component>, List<Component>>> ans = new ArrayList<>();//创建一个空list
		return ans;
	}

	public ArtifactSet root() {
		return root == null ? this : root.get();
	}

	public void getAllDescs(List<Component> list, Item.TooltipContext ctx, boolean shift) {
		list.add(ACLang.SET.get(getDesc().withStyle(ChatFormatting.YELLOW)));
		var level = ctx.level();
		if (level == null) return;
		var config = getConfig(level.registryAccess());
		if (config == null) return;
		var consumer = new TooltipConsumer(list);
		for (var eff : config.list()) {
			var e = eff.value();
			var id = eff.unwrapKey().orElseThrow().location();
			consumer.add(ACLang.EFFECT_COUNT.get(e.getName(id), e.count()));
			if (shift) {
				consumer.push();
				e.getDetailedDescription(id, consumer);
				consumer.pop();
			}
		}
	}

}

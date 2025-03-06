package dev.xkmc.akhet_chronomaly.content.core.item;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.akhet_chronomaly.content.core.data.SetConfig;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import dev.xkmc.akhet_chronomaly.init.registrate.ACTypeRegistry;
import dev.xkmc.akhet_chronomaly.init.registrate.entries.SetEntry;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import dev.xkmc.l2core.util.ServerProxy;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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


	private MutableComponent getCountDesc(int count) {
		return ACLang.getTranslate("set." + count, getLink().size());//TODO
	}

	public List<Pair<List<Component>, List<Component>>> addComponents(int count) {
		List<Pair<List<Component>, List<Component>>> ans = new ArrayList<>();//创建一个空list
		//获取一个SetEffect，List.of()把SetEffect对象转为只有一个元素的List，Pair.of()把两个List封装成一个Pair，Pair添加到ans
		ans.add(Pair.of(List.of(ACLang.ALL_SET_EFFECTS.get(getDesc(), count)), List.of()));
		var access = ServerProxy.getRegistryAccess();
		if (access == null) return ans;
		var list = getConfig(access);
		if (list == null) return ans;
		for (var ent : list.list()) {
			//判断符合数量条件的效果与描述加入ans
			if (count >= ent.value().count()) {//若数量符合
				var id = ent.unwrapKey().orElseThrow().location();
				var desc = Component.translatable("set_effect." + id.getNamespace() + "." + id.getPath());
				List<Component> a = List.of(getCountDesc(ent.value().count()), desc.withStyle(ChatFormatting.BLACK));
				List<Component> b = new ArrayList<>();
				b.add(getCountDesc(ent.value().count()).append(desc.withStyle(ChatFormatting.GRAY)));
				b.addAll(ent.value().getDetailedDescription().stream()
						.map(comp -> (Component) comp.withStyle(ChatFormatting.BLACK)).toList());
				ans.add(Pair.of(a, b));
			}
		}
		return ans;
	}

	public ArtifactSet root() {
		return root == null ? this : root.get();
	}

}

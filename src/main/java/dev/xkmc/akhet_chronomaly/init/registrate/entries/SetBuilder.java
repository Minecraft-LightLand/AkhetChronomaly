package dev.xkmc.akhet_chronomaly.init.registrate.entries;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactSet;
import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactSlot;
import dev.xkmc.akhet_chronomaly.content.core.item.BaseArtifact;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.akhet_chronomaly.init.registrate.ACTypeRegistry;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2menustacker.init.L2MSTagGen;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import static com.tterrag.registrate.providers.RegistrateLangProvider.toEnglishName;

public class SetBuilder<T extends ArtifactSet, I extends BaseArtifact, P> extends AbstractBuilder<ArtifactSet, T, P, SetBuilder<T, I, P>> {

	private final NonNullSupplier<T> sup;

	public SetBuilder(ACRegistrate owner, P parent, String name, BuilderCallback callback, NonNullSupplier<T> sup) {
		super(owner, parent, name, callback, ACTypeRegistry.SET.key());
		this.sup = sup;
	}

	public final ItemBuilder<BaseArtifact, SetBuilder<T, I, P>> addSlotImpl(SimpleEntry<ArtifactSlot> slot, String partName) {
		TagKey<Item> artifact = ItemTags.create(AkhetChronomaly.loc("artifact"));
		String slot_name = slot.key().location().getPath();
		TagKey<Item> curios_tag = ItemTags.create(ResourceLocation.fromNamespaceAndPath("curios", "akhet_" + slot_name));
		TagKey<Item> slot_tag = ItemTags.create(AkhetChronomaly.loc(slot_name));
		return AkhetChronomaly.REGISTRATE.item(this, getName() + "_" + partName, p -> new BaseArtifact(p, asSupplier()::get, slot))
				.tag(curios_tag, slot_tag, artifact).lang(toEnglishName(partName) + " of " + toEnglishName(getName()));
	}

	public final ItemBuilder<BaseArtifact, SetBuilder<T, I, P>> addSlot(SimpleEntry<ArtifactSlot> slot, String partName) {
		var ans = addSlotImpl(slot, partName);
		return ans.model((ctx, pvd) -> pvd.getBuilder(getName() + "_" + partName)
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0", AkhetChronomaly.loc("item/" + getName() + "/" + partName)));
	}

	public final ItemBuilder<BaseArtifact, SetBuilder<T, I, P>> addSymmetricSlot(SimpleEntry<ArtifactSlot> slot, String partName) {
		var ans = addSlotImpl(slot, partName);
		ans.tag(L2MSTagGen.QUICK_ACCESS);
		return ans.model((ctx, pvd) -> {
			var left = pvd.getBuilder(getName() + "_" + partName + "_left")
					.parent(new ModelFile.UncheckedModelFile("item/generated"))
					.texture("layer0", AkhetChronomaly.loc("item/" + getName() + "/" + partName + "_left"));
			pvd.getBuilder(getName() + "_" + partName)
					.parent(new ModelFile.UncheckedModelFile("item/generated"))
					.texture("layer0", AkhetChronomaly.loc("item/" + getName() + "/" + partName + "_right"))
					.override().predicate(AkhetChronomaly.loc("flip"), 0.5f).model(left).end();
		});
	}

	@Override
	protected RegistryEntry<ArtifactSet, T> createEntryWrapper(DeferredHolder<ArtifactSet, T> delegate) {
		return new SetEntry<>(Wrappers.cast(this.getOwner()), delegate);
	}

	@NonnullType
	@NotNull
	protected T createEntry() {
		return this.sup.get();
	}

	public SetBuilder<T, I, P> lang(String name) {
		return this.lang(NamedEntry::getDescriptionId, name);
	}

	@Override
	public SetEntry<T> register() {
		return Wrappers.cast(super.register());
	}
}

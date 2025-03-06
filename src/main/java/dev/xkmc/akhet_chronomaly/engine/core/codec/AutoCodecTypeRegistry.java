package dev.xkmc.akhet_chronomaly.engine.core.codec;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public record AutoCodecTypeRegistry<T extends IAutoCodec<?>>(
		ResourceKey<Registry<WrappedCodec<T, ?>>> key,
		Supplier<Registry<WrappedCodec<T, ?>>> registry
) {

	private static final ConcurrentHashMap<Class<?>, AutoCodecTypeRegistry<?>> MAP = new ConcurrentHashMap<>();

	public static Collection<AutoCodecTypeRegistry<?>> registries() {
		return MAP.values();
	}

	public static <E extends IAutoCodec<?>> AutoCodecTypeRegistry<E> of(String id, Class<?> type) {
		ResourceKey<Registry<WrappedCodec<E, ?>>> key = ResourceKey.createRegistryKey(AkhetChronomaly.loc(id));
		RegistryBuilder<WrappedCodec<E, ?>> builder = new RegistryBuilder<>(key);
		Registry<WrappedCodec<E, ?>> reg = builder.create();
		OneTimeEventReceiver.addModListener(AkhetChronomaly.REGISTRATE, NewRegistryEvent.class, (e) -> e.register(reg));
		var ans = new AutoCodecTypeRegistry<>(key, () -> reg);
		MAP.put(type, ans);
		return ans;
	}

	public static AutoCodecTypeRegistry<?> get(Class<?> cls) {
		var hit = MAP.get(cls);
		if (hit == null) throw new IllegalStateException("Failed to get registry for type " + cls);
		return hit;
	}

	public Codec<WrappedCodec<T, ?>> codec() {
		return registry.get().byNameCodec();
	}

	public Codec<?> directCodec() {
		return codec().dispatch(IAutoCodec::type, WrappedCodec::mapCodec);
	}

}

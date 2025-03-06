package dev.xkmc.akhet_chronomaly.engine.core.codec;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.xkmc.akhet_chronomaly.content.core.bonus.BonusRef;
import dev.xkmc.akhet_chronomaly.content.core.bonus.Constant;
import dev.xkmc.akhet_chronomaly.content.core.bonus.NumberValue;
import dev.xkmc.l2serial.serialization.custom_handler.Handlers;
import dev.xkmc.l2serial.serialization.type_cache.RecordCache;
import dev.xkmc.l2serial.serialization.type_cache.TypeInfo;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CodecHelper {

	private static final ConcurrentHashMap<Class<?>, Codec<?>> PARAM_CACHE = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Class<?>, MapCodec<?>> RESULT_CACHE = new ConcurrentHashMap<>();

	static {
		PARAM_CACHE.put(boolean.class, Codec.BOOL);
		PARAM_CACHE.put(int.class, Codec.INT);
		PARAM_CACHE.put(float.class, Codec.FLOAT);
		PARAM_CACHE.put(double.class, Codec.DOUBLE);
		PARAM_CACHE.put(String.class, Codec.STRING);
		PARAM_CACHE.put(ResourceLocation.class, Codec.STRING.xmap(ResourceLocation::parse, ResourceLocation::toString));

		Codec<NumberValue> numCodec = Codec.either(
				Codec.STRING.xmap(BonusRef::new, BonusRef::type),
				Codec.DOUBLE.xmap(Constant::new, Constant::val)
		).xmap(
				e -> e.map(l -> l, r -> r),
				e -> e instanceof BonusRef ref ? Either.left(ref) : Either.right((Constant) e)
		);
		PARAM_CACHE.put(NumberValue.class, numCodec);
	}

	public static <T extends Record> MapCodec<T> getAutoCodec(Class<T> cls) {
		var hit = RESULT_CACHE.get(cls);
		if (hit != null) return Wrappers.cast(hit);
		try {
			var ans = getEntryCodec(cls);
			RESULT_CACHE.put(cls, ans);
			return ans;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private static <T extends Record> MapCodec<T> getEntryCodec(Class<T> cls) throws Exception {
		var cache = RecordCache.get(cls);
		if (IComponentCodec.class.isAssignableFrom(cache.getComponents()[0].getType())) {
			var subType = cache.getComponents()[0].getType();
			var sub = RecordCache.get(subType);
			List<MapCodec<?>> list = new ArrayList<>();
			for (var e : sub.getComponents()) {
				list.add(getCodec(e));
			}
			return Wrappers.cast(sub.getCodec(list).xmap(
					e -> Wrappers.get(() -> cache.create(new Object[]{e})),
					e -> Wrappers.get(() -> cache.get(e, 0))
			));
		}
		List<MapCodec<?>> list = new ArrayList<>();
		for (var e : cache.getComponents()) {
			list.add(getCodec(e));
		}
		return Wrappers.cast(cache.getCodec(list));
	}

	private static MapCodec<?> getCodec(RecordComponent comp) {
		var info = TypeInfo.of(comp);
		if (info.getAsClass() == Optional.class) {
			return getCodec(info.getGenericType(0)).optionalFieldOf(comp.getName());
		}
		return getCodec(info).fieldOf(comp.getName());
	}

	private static Codec<?> getCodec(TypeInfo info) {
		if (info.getAsClass() == List.class) {
			var codec = getCodec(info.getGenericType(0));
			return maybeList(codec);
		}
		if (info.getAsClass() == Holder.class) {
			return Handlers.getReg(info.getGenericType(0).getAsClass()).holder().codec();
		}
		return getCodec(info.getAsClass());
	}

	private static <T> Codec<List<T>> maybeList(Codec<T> codec) {
		return Codec.either(codec, codec.listOf()).xmap(
				e -> e.map(List::of, r -> r),
				list -> list.size() == 1 ? Either.left(list.getFirst()) : Either.right(list)
		);
	}

	private static <T> Codec<T> getCodec(Class<T> cls) {
		var hit = PARAM_CACHE.get(cls);
		if (hit != null) return Wrappers.cast(hit);
		var ans = getCodecRaw(cls);
		PARAM_CACHE.put(cls, ans);
		return Wrappers.cast(ans);
	}

	private static Codec<?> getCodecRaw(Class<?> cls) {
		if (cls.isEnum()) {
			return getEnumCodec(Wrappers.cast(cls));
		}
		if (IAutoCodec.class.isAssignableFrom(cls)) {
			return AutoCodecTypeRegistry.get(cls).directCodec();
		}
		throw new IllegalStateException("Class " + cls + " is not auto-serializable");
	}

	private static <T extends Enum<T>> Codec<T> getEnumCodec(Class<T> cls) {
		return Codec.STRING.xmap(e -> Enum.valueOf(cls, e), Enum::name);
	}

}

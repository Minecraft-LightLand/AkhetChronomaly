package dev.xkmc.akhet_chronomaly.content.core;

import com.google.common.collect.ImmutableMultimap;
import dev.xkmc.akhet_chronomaly.content.config.StatType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

public record StatEntry(Holder<StatType> type, double value, double scale) {

	public ResourceLocation getID() {
		return type.unwrapKey().orElseThrow().location();
	}

	public Component getTooltip(double factor, @Nullable TooltipFlag flag) {
		return type.value().getTooltip(getVal(factor), flag);
	}

	public double getVal(double factor) {
		return factor * factor * value * type().value().getBaseValue();
	}

	public ResourceLocation attributeId(ResourceLocation base) {
		return base.withSuffix("_" + getID().getPath());
	}

	public void toModifier(double factor, ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder, ResourceLocation base) {
		if (scale() == 0) return;
		var e = type().value();
		var id = base.withSuffix("_" + getID().getPath());
		builder.put(e.attr(), new AttributeModifier(id, getVal(factor), e.op()));
	}

	public Mutable mutable() {
		return new Mutable(type, value, scale);
	}

	public static class Mutable {

		private final Holder<StatType> type;
		private double value, factor;

		private Mutable(Holder<StatType> type, double value, double factor) {
			this.type = type;
			this.value = value;
		}


		public void addMultiplier(double val) {
			value += val;
		}

		public StatEntry immutable() {
			return new StatEntry(type, value, factor);
		}

		public Holder<StatType> type() {
			return type;
		}

		public void refresh(RandomSource random) {
			value = type.value().getInitialValue(random);
		}

		public void scale(int factor) {
			this.factor *= factor;
		}
	}

}

package dev.xkmc.akhet_chronomaly.content.engine.core.codec;

import dev.xkmc.l2core.init.reg.simple.Reg;
import net.neoforged.neoforge.registries.DeferredRegister;

public record WR<T extends IAutoCodec<?>>(DeferredRegister<WrappedCodec<T, ?>> reg) {

	public WR(DeferredRegister<WrappedCodec<T, ?>> reg) {
		this.reg = reg;
	}

	public static <T extends IAutoCodec<?>> WR<T> of(Reg parent, AutoCodecTypeRegistry<T> reg) {
		return new WR<>(parent.make(reg.key()));
	}

	public <H extends T> void reg(String id, Class<H> cls) {
		this.reg.register(id, () -> new WrappedCodec(cls));
	}

}

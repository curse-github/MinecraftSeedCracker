package net.minecraft.network.protocol;

import io.netty.buffer.ByteBuf;
import java.util.function.Function;
import net.minecraft.network.PacketListener;
import net.minecraft.network.ProtocolInfo;

public interface UnboundProtocol<T extends PacketListener, B extends ByteBuf, C> extends ProtocolInfo.DetailsProvider {
  ProtocolInfo<T> bind(Function<ByteBuf, B> paramFunction, C paramC);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\UnboundProtocol.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
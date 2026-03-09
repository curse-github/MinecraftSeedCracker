/*   */ package net.minecraft.network;
/*   */ 
/*   */ import net.minecraft.network.protocol.PacketFlow;
/*   */ 
/*   */ public interface ClientboundPacketListener
/*   */   extends PacketListener
/*   */ {
/* 8 */   default PacketFlow flow() { return PacketFlow.CLIENTBOUND; }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\ClientboundPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
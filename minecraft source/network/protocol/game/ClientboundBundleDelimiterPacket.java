/*   */ package net.minecraft.network.protocol.game;
/*   */ 
/*   */ import net.minecraft.network.protocol.BundleDelimiterPacket;
/*   */ import net.minecraft.network.protocol.PacketType;
/*   */ 
/*   */ public class ClientboundBundleDelimiterPacket
/*   */   extends BundleDelimiterPacket<ClientGamePacketListener>
/*   */ {
/* 9 */   public PacketType<ClientboundBundleDelimiterPacket> type() { return GamePacketTypes.CLIENTBOUND_BUNDLE_DELIMITER; }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBundleDelimiterPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
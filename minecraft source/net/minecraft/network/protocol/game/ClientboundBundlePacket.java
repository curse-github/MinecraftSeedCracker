/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.BundlePacket;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundBundlePacket extends BundlePacket<ClientGamePacketListener> {
/*  9 */   public ClientboundBundlePacket(Iterable<Packet<? super ClientGamePacketListener>> packets) { super(packets); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   public PacketType<ClientboundBundlePacket> type() { return GamePacketTypes.CLIENTBOUND_BUNDLE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public void handle(ClientGamePacketListener listener) { listener.handleBundlePacket(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBundlePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
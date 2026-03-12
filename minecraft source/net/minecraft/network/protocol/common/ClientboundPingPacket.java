/*    */ package net.minecraft.network.protocol.common;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundPingPacket extends Object implements Packet<ClientCommonPacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundPingPacket> STREAM_CODEC = Packet.codec(ClientboundPingPacket::write, ClientboundPingPacket::new);
/*    */   
/*    */   private final int id;
/*    */ 
/*    */   
/* 15 */   public ClientboundPingPacket(int id) { this.id = id; }
/*    */ 
/*    */ 
/*    */   
/* 19 */   private ClientboundPingPacket(FriendlyByteBuf input) { this.id = input.readInt(); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   private void write(FriendlyByteBuf output) { output.writeInt(this.id); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public PacketType<ClientboundPingPacket> type() { return CommonPacketTypes.CLIENTBOUND_PING; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public void handle(ClientCommonPacketListener listener) { listener.handlePing(this); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public int getId() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ClientboundPingPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
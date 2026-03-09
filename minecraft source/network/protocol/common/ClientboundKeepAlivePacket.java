/*    */ package net.minecraft.network.protocol.common;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundKeepAlivePacket extends Object implements Packet<ClientCommonPacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundKeepAlivePacket> STREAM_CODEC = Packet.codec(ClientboundKeepAlivePacket::write, ClientboundKeepAlivePacket::new);
/*    */   
/*    */   private final long id;
/*    */ 
/*    */   
/* 14 */   public ClientboundKeepAlivePacket(long id) { this.id = id; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private ClientboundKeepAlivePacket(FriendlyByteBuf input) { this.id = input.readLong(); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private void write(FriendlyByteBuf output) { output.writeLong(this.id); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ClientboundKeepAlivePacket> type() { return CommonPacketTypes.CLIENTBOUND_KEEP_ALIVE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ClientCommonPacketListener listener) { listener.handleKeepAlive(this); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public long getId() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ClientboundKeepAlivePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
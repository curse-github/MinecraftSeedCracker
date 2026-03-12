/*    */ package net.minecraft.network.protocol.ping;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundPingRequestPacket extends Object implements Packet<ServerPingPacketListener> {
/*  9 */   public static final StreamCodec<ByteBuf, ServerboundPingRequestPacket> STREAM_CODEC = Packet.codec(ServerboundPingRequestPacket::write, ServerboundPingRequestPacket::new);
/*    */   
/*    */   private final long time;
/*    */ 
/*    */   
/* 14 */   public ServerboundPingRequestPacket(long time) { this.time = time; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private ServerboundPingRequestPacket(ByteBuf input) { this.time = input.readLong(); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private void write(ByteBuf output) { output.writeLong(this.time); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ServerboundPingRequestPacket> type() { return PingPacketTypes.SERVERBOUND_PING_REQUEST; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ServerPingPacketListener listener) { listener.handlePingRequest(this); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public long getTime() { return this.time; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\ping\ServerboundPingRequestPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
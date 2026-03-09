/*    */ package net.minecraft.network.protocol.common;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundPongPacket extends Object implements Packet<ServerCommonPacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundPongPacket> STREAM_CODEC = Packet.codec(ServerboundPongPacket::write, ServerboundPongPacket::new);
/*    */   
/*    */   private final int id;
/*    */ 
/*    */   
/* 15 */   public ServerboundPongPacket(int id) { this.id = id; }
/*    */ 
/*    */ 
/*    */   
/* 19 */   private ServerboundPongPacket(FriendlyByteBuf input) { this.id = input.readInt(); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   private void write(FriendlyByteBuf output) { output.writeInt(this.id); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public PacketType<ServerboundPongPacket> type() { return CommonPacketTypes.SERVERBOUND_PONG; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public void handle(ServerCommonPacketListener listener) { listener.handlePong(this); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public int getId() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ServerboundPongPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundPaddleBoatPacket extends Object implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundPaddleBoatPacket> STREAM_CODEC = Packet.codec(ServerboundPaddleBoatPacket::write, ServerboundPaddleBoatPacket::new);
/*    */   
/*    */   private final boolean left;
/*    */   private final boolean right;
/*    */   
/*    */   public ServerboundPaddleBoatPacket(boolean left, boolean right) {
/* 15 */     this.left = left;
/* 16 */     this.right = right;
/*    */   }
/*    */   
/*    */   private ServerboundPaddleBoatPacket(FriendlyByteBuf input) {
/* 20 */     this.left = input.readBoolean();
/* 21 */     this.right = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 25 */     output.writeBoolean(this.left);
/* 26 */     output.writeBoolean(this.right);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public void handle(ServerGamePacketListener listener) { listener.handlePaddleBoat(this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public PacketType<ServerboundPaddleBoatPacket> type() { return GamePacketTypes.SERVERBOUND_PADDLE_BOAT; }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public boolean getLeft() { return this.left; }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public boolean getRight() { return this.right; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundPaddleBoatPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
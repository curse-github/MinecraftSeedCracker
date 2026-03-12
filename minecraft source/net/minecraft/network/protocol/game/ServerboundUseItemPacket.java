/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ 
/*    */ public class ServerboundUseItemPacket extends Object implements Packet<ServerGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundUseItemPacket> STREAM_CODEC = Packet.codec(ServerboundUseItemPacket::write, ServerboundUseItemPacket::new);
/*    */   
/*    */   private final InteractionHand hand;
/*    */   private final int sequence;
/*    */   private final float yRot;
/*    */   private final float xRot;
/*    */   
/*    */   public ServerboundUseItemPacket(InteractionHand hand, int sequence, float yRot, float xRot) {
/* 18 */     this.hand = hand;
/* 19 */     this.sequence = sequence;
/* 20 */     this.yRot = yRot;
/* 21 */     this.xRot = xRot;
/*    */   }
/*    */   
/*    */   private ServerboundUseItemPacket(FriendlyByteBuf input) {
/* 25 */     this.hand = (InteractionHand)input.readEnum(InteractionHand.class);
/* 26 */     this.sequence = input.readVarInt();
/* 27 */     this.yRot = input.readFloat();
/* 28 */     this.xRot = input.readFloat();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 32 */     output.writeEnum(this.hand);
/* 33 */     output.writeVarInt(this.sequence);
/* 34 */     output.writeFloat(this.yRot);
/* 35 */     output.writeFloat(this.xRot);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public PacketType<ServerboundUseItemPacket> type() { return GamePacketTypes.SERVERBOUND_USE_ITEM; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public void handle(ServerGamePacketListener listener) { listener.handleUseItem(this); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public InteractionHand getHand() { return this.hand; }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public int getSequence() { return this.sequence; }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public float getYRot() { return this.yRot; }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public float getXRot() { return this.xRot; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundUseItemPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class ServerboundUseItemOnPacket extends Object implements Packet<ServerGamePacketListener> {
/* 11 */   public static final StreamCodec<FriendlyByteBuf, ServerboundUseItemOnPacket> STREAM_CODEC = Packet.codec(ServerboundUseItemOnPacket::write, ServerboundUseItemOnPacket::new);
/*    */   
/*    */   private final BlockHitResult blockHit;
/*    */   private final InteractionHand hand;
/*    */   private final int sequence;
/*    */   
/*    */   public ServerboundUseItemOnPacket(InteractionHand hand, BlockHitResult blockHit, int sequence) {
/* 18 */     this.hand = hand;
/* 19 */     this.blockHit = blockHit;
/* 20 */     this.sequence = sequence;
/*    */   }
/*    */   
/*    */   private ServerboundUseItemOnPacket(FriendlyByteBuf input) {
/* 24 */     this.hand = (InteractionHand)input.readEnum(InteractionHand.class);
/* 25 */     this.blockHit = input.readBlockHitResult();
/* 26 */     this.sequence = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 30 */     output.writeEnum(this.hand);
/* 31 */     output.writeBlockHitResult(this.blockHit);
/* 32 */     output.writeVarInt(this.sequence);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public PacketType<ServerboundUseItemOnPacket> type() { return GamePacketTypes.SERVERBOUND_USE_ITEM_ON; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public void handle(ServerGamePacketListener listener) { listener.handleUseItemOn(this); }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public InteractionHand getHand() { return this.hand; }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public BlockHitResult getHitResult() { return this.blockHit; }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public int getSequence() { return this.sequence; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundUseItemOnPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
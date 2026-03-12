/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundBlockEntityTagQueryPacket extends Object implements Packet<ServerGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundBlockEntityTagQueryPacket> STREAM_CODEC = Packet.codec(ServerboundBlockEntityTagQueryPacket::write, ServerboundBlockEntityTagQueryPacket::new);
/*    */   
/*    */   private final int transactionId;
/*    */   private final BlockPos pos;
/*    */   
/*    */   public ServerboundBlockEntityTagQueryPacket(int transactionId, BlockPos pos) {
/* 16 */     this.transactionId = transactionId;
/* 17 */     this.pos = pos;
/*    */   }
/*    */   
/*    */   private ServerboundBlockEntityTagQueryPacket(FriendlyByteBuf input) {
/* 21 */     this.transactionId = input.readVarInt();
/* 22 */     this.pos = input.readBlockPos();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 26 */     output.writeVarInt(this.transactionId);
/* 27 */     output.writeBlockPos(this.pos);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public PacketType<ServerboundBlockEntityTagQueryPacket> type() { return GamePacketTypes.SERVERBOUND_BLOCK_ENTITY_TAG_QUERY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public void handle(ServerGamePacketListener listener) { listener.handleBlockEntityTagQuery(this); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public int getTransactionId() { return this.transactionId; }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public BlockPos getPos() { return this.pos; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundBlockEntityTagQueryPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
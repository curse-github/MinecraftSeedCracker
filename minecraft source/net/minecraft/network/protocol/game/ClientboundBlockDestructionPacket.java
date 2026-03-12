/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundBlockDestructionPacket extends Object implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundBlockDestructionPacket> STREAM_CODEC = Packet.codec(ClientboundBlockDestructionPacket::write, ClientboundBlockDestructionPacket::new);
/*    */   
/*    */   private final int id;
/*    */   private final BlockPos pos;
/*    */   private final int progress;
/*    */   
/*    */   public ClientboundBlockDestructionPacket(int id, BlockPos pos, int progress) {
/* 17 */     this.id = id;
/* 18 */     this.pos = pos;
/* 19 */     this.progress = progress;
/*    */   }
/*    */   
/*    */   private ClientboundBlockDestructionPacket(FriendlyByteBuf input) {
/* 23 */     this.id = input.readVarInt();
/* 24 */     this.pos = input.readBlockPos();
/* 25 */     this.progress = input.readUnsignedByte();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 29 */     output.writeVarInt(this.id);
/* 30 */     output.writeBlockPos(this.pos);
/* 31 */     output.writeByte(this.progress);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public PacketType<ClientboundBlockDestructionPacket> type() { return GamePacketTypes.CLIENTBOUND_BLOCK_DESTRUCTION; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public void handle(ClientGamePacketListener listener) { listener.handleBlockDestruction(this); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public int getId() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public BlockPos getPos() { return this.pos; }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public int getProgress() { return this.progress; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBlockDestructionPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
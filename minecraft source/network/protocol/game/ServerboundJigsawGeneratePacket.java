/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundJigsawGeneratePacket extends Object implements Packet<ServerGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundJigsawGeneratePacket> STREAM_CODEC = Packet.codec(ServerboundJigsawGeneratePacket::write, ServerboundJigsawGeneratePacket::new);
/*    */   
/*    */   private final BlockPos pos;
/*    */   private final int levels;
/*    */   private final boolean keepJigsaws;
/*    */   
/*    */   public ServerboundJigsawGeneratePacket(BlockPos blockPos, int levels, boolean keepJigsaws) {
/* 17 */     this.pos = blockPos;
/* 18 */     this.levels = levels;
/* 19 */     this.keepJigsaws = keepJigsaws;
/*    */   }
/*    */   
/*    */   private ServerboundJigsawGeneratePacket(FriendlyByteBuf input) {
/* 23 */     this.pos = input.readBlockPos();
/* 24 */     this.levels = input.readVarInt();
/* 25 */     this.keepJigsaws = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 29 */     output.writeBlockPos(this.pos);
/* 30 */     output.writeVarInt(this.levels);
/* 31 */     output.writeBoolean(this.keepJigsaws);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public PacketType<ServerboundJigsawGeneratePacket> type() { return GamePacketTypes.SERVERBOUND_JIGSAW_GENERATE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public void handle(ServerGamePacketListener listener) { listener.handleJigsawGenerate(this); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public BlockPos getPos() { return this.pos; }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public int levels() { return this.levels; }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public boolean keepJigsaws() { return this.keepJigsaws; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundJigsawGeneratePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
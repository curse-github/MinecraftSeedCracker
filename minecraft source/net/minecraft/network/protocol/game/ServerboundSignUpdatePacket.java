/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundSignUpdatePacket extends Object implements Packet<ServerGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSignUpdatePacket> STREAM_CODEC = Packet.codec(ServerboundSignUpdatePacket::write, ServerboundSignUpdatePacket::new);
/*    */   
/*    */   private static final int MAX_STRING_LENGTH = 384;
/*    */   private final BlockPos pos;
/*    */   private final String[] lines;
/*    */   private final boolean isFrontText;
/*    */   
/*    */   public ServerboundSignUpdatePacket(BlockPos pos, boolean isFrontText, String line0, String line1, String line2, String line3) {
/* 18 */     this.pos = pos;
/* 19 */     this.isFrontText = isFrontText;
/* 20 */     this.lines = new String[] { line0, line1, line2, line3 };
/*    */   }
/*    */   
/*    */   private ServerboundSignUpdatePacket(FriendlyByteBuf input) {
/* 24 */     this.pos = input.readBlockPos();
/* 25 */     this.isFrontText = input.readBoolean();
/* 26 */     this.lines = new String[4];
/* 27 */     for (int i = 0; i < 4; i++) {
/* 28 */       this.lines[i] = input.readUtf(384);
/*    */     }
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 33 */     output.writeBlockPos(this.pos);
/* 34 */     output.writeBoolean(this.isFrontText);
/* 35 */     for (int i = 0; i < 4; i++) {
/* 36 */       output.writeUtf(this.lines[i]);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public PacketType<ServerboundSignUpdatePacket> type() { return GamePacketTypes.SERVERBOUND_SIGN_UPDATE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public void handle(ServerGamePacketListener listener) { listener.handleSignUpdate(this); }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public BlockPos getPos() { return this.pos; }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public boolean isFrontText() { return this.isFrontText; }
/*    */ 
/*    */ 
/*    */   
/* 59 */   public String[] getLines() { return this.lines; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSignUpdatePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
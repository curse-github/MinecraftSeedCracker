/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundOpenSignEditorPacket extends Object implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundOpenSignEditorPacket> STREAM_CODEC = Packet.codec(ClientboundOpenSignEditorPacket::write, ClientboundOpenSignEditorPacket::new);
/*    */   
/*    */   private final BlockPos pos;
/*    */   private final boolean isFrontText;
/*    */   
/*    */   public ClientboundOpenSignEditorPacket(BlockPos pos, boolean isFrontText) {
/* 16 */     this.pos = pos;
/* 17 */     this.isFrontText = isFrontText;
/*    */   }
/*    */   
/*    */   private ClientboundOpenSignEditorPacket(FriendlyByteBuf input) {
/* 21 */     this.pos = input.readBlockPos();
/* 22 */     this.isFrontText = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 26 */     output.writeBlockPos(this.pos);
/* 27 */     output.writeBoolean(this.isFrontText);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public PacketType<ClientboundOpenSignEditorPacket> type() { return GamePacketTypes.CLIENTBOUND_OPEN_SIGN_EDITOR; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public void handle(ClientGamePacketListener listener) { listener.handleOpenSignEditor(this); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public BlockPos getPos() { return this.pos; }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public boolean isFrontText() { return this.isFrontText; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundOpenSignEditorPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
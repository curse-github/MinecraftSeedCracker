/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundLevelEventPacket extends Object implements Packet<ClientGamePacketListener> {
/* 11 */   public static final StreamCodec<FriendlyByteBuf, ClientboundLevelEventPacket> STREAM_CODEC = Packet.codec(ClientboundLevelEventPacket::write, ClientboundLevelEventPacket::new);
/*    */   
/*    */   private final int type;
/*    */   private final BlockPos pos;
/*    */   private final int data;
/*    */   private final boolean globalEvent;
/*    */   
/*    */   public ClientboundLevelEventPacket(int type, BlockPos pos, int data, boolean globalEvent) {
/* 19 */     this.type = type;
/* 20 */     this.pos = pos.immutable();
/* 21 */     this.data = data;
/* 22 */     this.globalEvent = globalEvent;
/*    */   }
/*    */   
/*    */   private ClientboundLevelEventPacket(FriendlyByteBuf input) {
/* 26 */     this.type = input.readInt();
/* 27 */     this.pos = input.readBlockPos();
/* 28 */     this.data = input.readInt();
/* 29 */     this.globalEvent = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 33 */     output.writeInt(this.type);
/* 34 */     output.writeBlockPos(this.pos);
/* 35 */     output.writeInt(this.data);
/* 36 */     output.writeBoolean(this.globalEvent);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public PacketType<ClientboundLevelEventPacket> type() { return GamePacketTypes.CLIENTBOUND_LEVEL_EVENT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public void handle(ClientGamePacketListener listener) { listener.handleLevelEvent(this); }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public boolean isGlobalEvent() { return this.globalEvent; }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public int getType() { return this.type; }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public int getData() { return this.data; }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public BlockPos getPos() { return this.pos; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundLevelEventPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ 
/*    */ public class ClientboundSetBorderLerpSizePacket extends Object implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetBorderLerpSizePacket> STREAM_CODEC = Packet.codec(ClientboundSetBorderLerpSizePacket::write, ClientboundSetBorderLerpSizePacket::new);
/*    */   
/*    */   private final double oldSize;
/*    */   private final double newSize;
/*    */   private final long lerpTime;
/*    */   
/*    */   public ClientboundSetBorderLerpSizePacket(WorldBorder border) {
/* 17 */     this.oldSize = border.getSize();
/* 18 */     this.newSize = border.getLerpTarget();
/* 19 */     this.lerpTime = border.getLerpTime();
/*    */   }
/*    */   
/*    */   private ClientboundSetBorderLerpSizePacket(FriendlyByteBuf input) {
/* 23 */     this.oldSize = input.readDouble();
/* 24 */     this.newSize = input.readDouble();
/* 25 */     this.lerpTime = input.readVarLong();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 29 */     output.writeDouble(this.oldSize);
/* 30 */     output.writeDouble(this.newSize);
/* 31 */     output.writeVarLong(this.lerpTime);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public PacketType<ClientboundSetBorderLerpSizePacket> type() { return GamePacketTypes.CLIENTBOUND_SET_BORDER_LERP_SIZE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public void handle(ClientGamePacketListener listener) { listener.handleSetBorderLerpSize(this); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public double getOldSize() { return this.oldSize; }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public double getNewSize() { return this.newSize; }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public long getLerpTime() { return this.lerpTime; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetBorderLerpSizePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
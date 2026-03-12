/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ 
/*    */ public class ClientboundSetBorderCenterPacket extends Object implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetBorderCenterPacket> STREAM_CODEC = Packet.codec(ClientboundSetBorderCenterPacket::write, ClientboundSetBorderCenterPacket::new);
/*    */   
/*    */   private final double newCenterX;
/*    */   private final double newCenterZ;
/*    */   
/*    */   public ClientboundSetBorderCenterPacket(WorldBorder border) {
/* 16 */     this.newCenterX = border.getCenterX();
/* 17 */     this.newCenterZ = border.getCenterZ();
/*    */   }
/*    */   
/*    */   private ClientboundSetBorderCenterPacket(FriendlyByteBuf input) {
/* 21 */     this.newCenterX = input.readDouble();
/* 22 */     this.newCenterZ = input.readDouble();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 26 */     output.writeDouble(this.newCenterX);
/* 27 */     output.writeDouble(this.newCenterZ);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public PacketType<ClientboundSetBorderCenterPacket> type() { return GamePacketTypes.CLIENTBOUND_SET_BORDER_CENTER; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public void handle(ClientGamePacketListener listener) { listener.handleSetBorderCenter(this); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public double getNewCenterZ() { return this.newCenterZ; }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public double getNewCenterX() { return this.newCenterX; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetBorderCenterPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
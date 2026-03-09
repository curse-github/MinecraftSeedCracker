/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ClientboundRotateHeadPacket extends Object implements Packet<ClientGamePacketListener> {
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ClientboundRotateHeadPacket> STREAM_CODEC = Packet.codec(ClientboundRotateHeadPacket::write, ClientboundRotateHeadPacket::new);
/*    */   
/*    */   private final int entityId;
/*    */   private final byte yHeadRot;
/*    */   
/*    */   public ClientboundRotateHeadPacket(Entity entity, byte yHeadRot) {
/* 20 */     this.entityId = entity.getId();
/* 21 */     this.yHeadRot = yHeadRot;
/*    */   }
/*    */   
/*    */   private ClientboundRotateHeadPacket(FriendlyByteBuf input) {
/* 25 */     this.entityId = input.readVarInt();
/* 26 */     this.yHeadRot = input.readByte();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 30 */     output.writeVarInt(this.entityId);
/* 31 */     output.writeByte(this.yHeadRot);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public PacketType<ClientboundRotateHeadPacket> type() { return GamePacketTypes.CLIENTBOUND_ROTATE_HEAD; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public void handle(ClientGamePacketListener listener) { listener.handleRotateMob(this); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public Entity getEntity(Level level) { return level.getEntity(this.entityId); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public float getYHeadRot() { return Mth.unpackDegrees(this.yHeadRot); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundRotateHeadPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
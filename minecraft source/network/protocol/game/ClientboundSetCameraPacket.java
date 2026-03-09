/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ClientboundSetCameraPacket extends Object implements Packet<ClientGamePacketListener> {
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetCameraPacket> STREAM_CODEC = Packet.codec(ClientboundSetCameraPacket::write, ClientboundSetCameraPacket::new);
/*    */   
/*    */   private final int cameraId;
/*    */ 
/*    */   
/* 17 */   public ClientboundSetCameraPacket(Entity camera) { this.cameraId = camera.getId(); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   private ClientboundSetCameraPacket(FriendlyByteBuf input) { this.cameraId = input.readVarInt(); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   private void write(FriendlyByteBuf output) { output.writeVarInt(this.cameraId); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public PacketType<ClientboundSetCameraPacket> type() { return GamePacketTypes.CLIENTBOUND_SET_CAMERA; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public void handle(ClientGamePacketListener listener) { listener.handleSetCamera(this); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public Entity getEntity(Level level) { return level.getEntity(this.cameraId); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetCameraPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
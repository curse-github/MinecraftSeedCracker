/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ClientboundEntityEventPacket extends Object implements Packet<ClientGamePacketListener> {
/* 13 */   public static final StreamCodec<FriendlyByteBuf, ClientboundEntityEventPacket> STREAM_CODEC = Packet.codec(ClientboundEntityEventPacket::write, ClientboundEntityEventPacket::new);
/*    */   
/*    */   private final int entityId;
/*    */   private final byte eventId;
/*    */   
/*    */   public ClientboundEntityEventPacket(Entity entity, byte eventId) {
/* 19 */     this.entityId = entity.getId();
/* 20 */     this.eventId = eventId;
/*    */   }
/*    */   
/*    */   private ClientboundEntityEventPacket(FriendlyByteBuf input) {
/* 24 */     this.entityId = input.readInt();
/* 25 */     this.eventId = input.readByte();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 29 */     output.writeInt(this.entityId);
/* 30 */     output.writeByte(this.eventId);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public PacketType<ClientboundEntityEventPacket> type() { return GamePacketTypes.CLIENTBOUND_ENTITY_EVENT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public void handle(ClientGamePacketListener listener) { listener.handleEntityEvent(this); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public Entity getEntity(Level level) { return level.getEntity(this.entityId); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public byte getEventId() { return this.eventId; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundEntityEventPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
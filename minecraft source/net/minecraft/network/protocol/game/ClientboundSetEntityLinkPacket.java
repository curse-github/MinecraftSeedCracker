/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ClientboundSetEntityLinkPacket extends Object implements Packet<ClientGamePacketListener> {
/* 11 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetEntityLinkPacket> STREAM_CODEC = Packet.codec(ClientboundSetEntityLinkPacket::write, ClientboundSetEntityLinkPacket::new);
/*    */   
/*    */   private final int sourceId;
/*    */   private final int destId;
/*    */   
/*    */   public ClientboundSetEntityLinkPacket(Entity sourceEntity, Entity destEntity) {
/* 17 */     this.sourceId = sourceEntity.getId();
/* 18 */     this.destId = (destEntity != null) ? destEntity.getId() : 0;
/*    */   }
/*    */   
/*    */   private ClientboundSetEntityLinkPacket(FriendlyByteBuf input) {
/* 22 */     this.sourceId = input.readInt();
/* 23 */     this.destId = input.readInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 27 */     output.writeInt(this.sourceId);
/* 28 */     output.writeInt(this.destId);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public PacketType<ClientboundSetEntityLinkPacket> type() { return GamePacketTypes.CLIENTBOUND_SET_ENTITY_LINK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public void handle(ClientGamePacketListener listener) { listener.handleEntityLinkPacket(this); }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public int getSourceId() { return this.sourceId; }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public int getDestId() { return this.destId; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetEntityLinkPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
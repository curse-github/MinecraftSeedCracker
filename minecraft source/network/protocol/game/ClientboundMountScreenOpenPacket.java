/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundMountScreenOpenPacket extends Object implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundMountScreenOpenPacket> STREAM_CODEC = Packet.codec(ClientboundMountScreenOpenPacket::write, ClientboundMountScreenOpenPacket::new);
/*    */   
/*    */   private final int containerId;
/*    */   private final int inventoryColumns;
/*    */   private final int entityId;
/*    */   
/*    */   public ClientboundMountScreenOpenPacket(int containerId, int inventoryColumns, int entityId) {
/* 16 */     this.containerId = containerId;
/* 17 */     this.inventoryColumns = inventoryColumns;
/* 18 */     this.entityId = entityId;
/*    */   }
/*    */   
/*    */   private ClientboundMountScreenOpenPacket(FriendlyByteBuf input) {
/* 22 */     this.containerId = input.readContainerId();
/* 23 */     this.inventoryColumns = input.readVarInt();
/* 24 */     this.entityId = input.readInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 28 */     output.writeContainerId(this.containerId);
/* 29 */     output.writeVarInt(this.inventoryColumns);
/* 30 */     output.writeInt(this.entityId);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public PacketType<ClientboundMountScreenOpenPacket> type() { return GamePacketTypes.CLIENTBOUND_MOUNT_SCREEN_OPEN; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public void handle(ClientGamePacketListener listener) { listener.handleMountScreenOpen(this); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public int getContainerId() { return this.containerId; }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public int getInventoryColumns() { return this.inventoryColumns; }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public int getEntityId() { return this.entityId; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundMountScreenOpenPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
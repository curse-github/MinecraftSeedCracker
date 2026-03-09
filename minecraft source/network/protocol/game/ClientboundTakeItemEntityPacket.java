/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundTakeItemEntityPacket extends Object implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundTakeItemEntityPacket> STREAM_CODEC = Packet.codec(ClientboundTakeItemEntityPacket::write, ClientboundTakeItemEntityPacket::new);
/*    */   
/*    */   private final int itemId;
/*    */   private final int playerId;
/*    */   private final int amount;
/*    */   
/*    */   public ClientboundTakeItemEntityPacket(int itemId, int playerId, int amount) {
/* 16 */     this.itemId = itemId;
/* 17 */     this.playerId = playerId;
/* 18 */     this.amount = amount;
/*    */   }
/*    */   
/*    */   private ClientboundTakeItemEntityPacket(FriendlyByteBuf input) {
/* 22 */     this.itemId = input.readVarInt();
/* 23 */     this.playerId = input.readVarInt();
/* 24 */     this.amount = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 28 */     output.writeVarInt(this.itemId);
/* 29 */     output.writeVarInt(this.playerId);
/* 30 */     output.writeVarInt(this.amount);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public PacketType<ClientboundTakeItemEntityPacket> type() { return GamePacketTypes.CLIENTBOUND_TAKE_ITEM_ENTITY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public void handle(ClientGamePacketListener listener) { listener.handleTakeItemEntity(this); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public int getItemId() { return this.itemId; }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public int getPlayerId() { return this.playerId; }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public int getAmount() { return this.amount; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundTakeItemEntityPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
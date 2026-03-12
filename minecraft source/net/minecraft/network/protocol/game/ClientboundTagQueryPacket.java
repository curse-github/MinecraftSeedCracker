/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundTagQueryPacket extends Object implements Packet<ClientGamePacketListener> {
/* 11 */   public static final StreamCodec<FriendlyByteBuf, ClientboundTagQueryPacket> STREAM_CODEC = Packet.codec(ClientboundTagQueryPacket::write, ClientboundTagQueryPacket::new);
/*    */   
/*    */   private final int transactionId;
/*    */   
/*    */   private final CompoundTag tag;
/*    */   
/*    */   public ClientboundTagQueryPacket(int transactionId, CompoundTag tag) {
/* 18 */     this.transactionId = transactionId;
/* 19 */     this.tag = tag;
/*    */   }
/*    */   
/*    */   private ClientboundTagQueryPacket(FriendlyByteBuf input) {
/* 23 */     this.transactionId = input.readVarInt();
/* 24 */     this.tag = input.readNbt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 28 */     output.writeVarInt(this.transactionId);
/* 29 */     output.writeNbt(this.tag);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public PacketType<ClientboundTagQueryPacket> type() { return GamePacketTypes.CLIENTBOUND_TAG_QUERY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public void handle(ClientGamePacketListener listener) { listener.handleTagQueryPacket(this); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public int getTransactionId() { return this.transactionId; }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public CompoundTag getTag() { return this.tag; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public boolean isSkippable() { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundTagQueryPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
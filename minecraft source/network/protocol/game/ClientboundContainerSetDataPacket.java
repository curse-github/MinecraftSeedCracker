/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundContainerSetDataPacket extends Object implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundContainerSetDataPacket> STREAM_CODEC = Packet.codec(ClientboundContainerSetDataPacket::write, ClientboundContainerSetDataPacket::new);
/*    */   
/*    */   private final int containerId;
/*    */   private final int id;
/*    */   private final int value;
/*    */   
/*    */   public ClientboundContainerSetDataPacket(int containerId, int id, int value) {
/* 16 */     this.containerId = containerId;
/* 17 */     this.id = id;
/* 18 */     this.value = value;
/*    */   }
/*    */   
/*    */   private ClientboundContainerSetDataPacket(FriendlyByteBuf input) {
/* 22 */     this.containerId = input.readContainerId();
/* 23 */     this.id = input.readShort();
/* 24 */     this.value = input.readShort();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 28 */     output.writeContainerId(this.containerId);
/* 29 */     output.writeShort(this.id);
/* 30 */     output.writeShort(this.value);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public PacketType<ClientboundContainerSetDataPacket> type() { return GamePacketTypes.CLIENTBOUND_CONTAINER_SET_DATA; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public void handle(ClientGamePacketListener listener) { listener.handleContainerSetData(this); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public int getContainerId() { return this.containerId; }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public int getId() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public int getValue() { return this.value; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundContainerSetDataPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
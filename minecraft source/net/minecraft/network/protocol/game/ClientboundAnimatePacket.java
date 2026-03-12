/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ClientboundAnimatePacket extends Object implements Packet<ClientGamePacketListener> {
/* 11 */   public static final StreamCodec<FriendlyByteBuf, ClientboundAnimatePacket> STREAM_CODEC = Packet.codec(ClientboundAnimatePacket::write, ClientboundAnimatePacket::new);
/*    */   
/*    */   public static final int SWING_MAIN_HAND = 0;
/*    */   
/*    */   public static final int WAKE_UP = 2;
/*    */   public static final int SWING_OFF_HAND = 3;
/*    */   public static final int CRITICAL_HIT = 4;
/*    */   public static final int MAGIC_CRITICAL_HIT = 5;
/*    */   private final int id;
/*    */   private final int action;
/*    */   
/*    */   public ClientboundAnimatePacket(Entity entity, int action) {
/* 23 */     this.id = entity.getId();
/* 24 */     this.action = action;
/*    */   }
/*    */   
/*    */   private ClientboundAnimatePacket(FriendlyByteBuf input) {
/* 28 */     this.id = input.readVarInt();
/* 29 */     this.action = input.readUnsignedByte();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 33 */     output.writeVarInt(this.id);
/* 34 */     output.writeByte(this.action);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public PacketType<ClientboundAnimatePacket> type() { return GamePacketTypes.CLIENTBOUND_ANIMATE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public void handle(ClientGamePacketListener listener) { listener.handleAnimate(this); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public int getId() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public int getAction() { return this.action; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundAnimatePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
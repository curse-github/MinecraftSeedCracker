/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundSetHealthPacket extends Object implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetHealthPacket> STREAM_CODEC = Packet.codec(ClientboundSetHealthPacket::write, ClientboundSetHealthPacket::new);
/*    */   
/*    */   private final float health;
/*    */   private final int food;
/*    */   private final float saturation;
/*    */   
/*    */   public ClientboundSetHealthPacket(float health, int food, float saturation) {
/* 16 */     this.health = health;
/* 17 */     this.food = food;
/* 18 */     this.saturation = saturation;
/*    */   }
/*    */   
/*    */   private ClientboundSetHealthPacket(FriendlyByteBuf input) {
/* 22 */     this.health = input.readFloat();
/* 23 */     this.food = input.readVarInt();
/* 24 */     this.saturation = input.readFloat();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 28 */     output.writeFloat(this.health);
/* 29 */     output.writeVarInt(this.food);
/* 30 */     output.writeFloat(this.saturation);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public PacketType<ClientboundSetHealthPacket> type() { return GamePacketTypes.CLIENTBOUND_SET_HEALTH; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public void handle(ClientGamePacketListener listener) { listener.handleSetHealth(this); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public float getHealth() { return this.health; }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public int getFood() { return this.food; }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public float getSaturation() { return this.saturation; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetHealthPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
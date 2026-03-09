/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundProjectilePowerPacket extends Object implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundProjectilePowerPacket> STREAM_CODEC = Packet.codec(ClientboundProjectilePowerPacket::write, ClientboundProjectilePowerPacket::new);
/*    */   
/*    */   private final int id;
/*    */   private final double accelerationPower;
/*    */   
/*    */   public ClientboundProjectilePowerPacket(int id, double accelerationPower) {
/* 15 */     this.id = id;
/* 16 */     this.accelerationPower = accelerationPower;
/*    */   }
/*    */   
/*    */   private ClientboundProjectilePowerPacket(FriendlyByteBuf input) {
/* 20 */     this.id = input.readVarInt();
/* 21 */     this.accelerationPower = input.readDouble();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 25 */     output.writeVarInt(this.id);
/* 26 */     output.writeDouble(this.accelerationPower);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public PacketType<ClientboundProjectilePowerPacket> type() { return GamePacketTypes.CLIENTBOUND_PROJECTILE_POWER; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public void handle(ClientGamePacketListener listener) { listener.handleProjectilePowerPacket(this); }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public int getId() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public double getAccelerationPower() { return this.accelerationPower; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundProjectilePowerPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
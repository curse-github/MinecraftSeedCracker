/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ClientboundSetPassengersPacket extends Object implements Packet<ClientGamePacketListener> {
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetPassengersPacket> STREAM_CODEC = Packet.codec(ClientboundSetPassengersPacket::write, ClientboundSetPassengersPacket::new);
/*    */   
/*    */   private final int vehicle;
/*    */   private final int[] passengers;
/*    */   
/*    */   public ClientboundSetPassengersPacket(Entity vehicle) {
/* 18 */     this.vehicle = vehicle.getId();
/* 19 */     List<Entity> entities = vehicle.getPassengers();
/* 20 */     this.passengers = new int[entities.size()];
/*    */     
/* 22 */     for (int i = 0; i < entities.size(); i++) {
/* 23 */       this.passengers[i] = ((Entity)entities.get(i)).getId();
/*    */     }
/*    */   }
/*    */   
/*    */   private ClientboundSetPassengersPacket(FriendlyByteBuf input) {
/* 28 */     this.vehicle = input.readVarInt();
/* 29 */     this.passengers = input.readVarIntArray();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 33 */     output.writeVarInt(this.vehicle);
/* 34 */     output.writeVarIntArray(this.passengers);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public PacketType<ClientboundSetPassengersPacket> type() { return GamePacketTypes.CLIENTBOUND_SET_PASSENGERS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public void handle(ClientGamePacketListener listener) { listener.handleSetEntityPassengersPacket(this); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public int[] getPassengers() { return this.passengers; }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public int getVehicle() { return this.vehicle; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetPassengersPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
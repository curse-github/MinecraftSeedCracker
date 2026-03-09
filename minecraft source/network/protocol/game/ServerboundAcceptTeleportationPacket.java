/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundAcceptTeleportationPacket extends Object implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundAcceptTeleportationPacket> STREAM_CODEC = Packet.codec(ServerboundAcceptTeleportationPacket::write, ServerboundAcceptTeleportationPacket::new);
/*    */   
/*    */   private final int id;
/*    */ 
/*    */   
/* 14 */   public ServerboundAcceptTeleportationPacket(int id) { this.id = id; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private ServerboundAcceptTeleportationPacket(FriendlyByteBuf input) { this.id = input.readVarInt(); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private void write(FriendlyByteBuf output) { output.writeVarInt(this.id); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ServerboundAcceptTeleportationPacket> type() { return GamePacketTypes.SERVERBOUND_ACCEPT_TELEPORTATION; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ServerGamePacketListener listener) { listener.handleAcceptTeleportPacket(this); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public int getId() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundAcceptTeleportationPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
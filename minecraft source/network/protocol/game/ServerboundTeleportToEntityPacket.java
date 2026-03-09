/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ServerboundTeleportToEntityPacket extends Object implements Packet<ServerGamePacketListener> {
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ServerboundTeleportToEntityPacket> STREAM_CODEC = Packet.codec(ServerboundTeleportToEntityPacket::write, ServerboundTeleportToEntityPacket::new);
/*    */   
/*    */   private final UUID uuid;
/*    */ 
/*    */   
/* 19 */   public ServerboundTeleportToEntityPacket(UUID uuid) { this.uuid = uuid; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   private ServerboundTeleportToEntityPacket(FriendlyByteBuf input) { this.uuid = input.readUUID(); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   private void write(FriendlyByteBuf output) { output.writeUUID(this.uuid); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public PacketType<ServerboundTeleportToEntityPacket> type() { return GamePacketTypes.SERVERBOUND_TELEPORT_TO_ENTITY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public void handle(ServerGamePacketListener listener) { listener.handleTeleportToEntityPacket(this); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public Entity getEntity(ServerLevel level) { return level.getEntity(this.uuid); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundTeleportToEntityPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
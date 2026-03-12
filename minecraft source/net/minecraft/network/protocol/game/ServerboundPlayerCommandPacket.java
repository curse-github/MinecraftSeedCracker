/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ServerboundPlayerCommandPacket extends Object implements Packet<ServerGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundPlayerCommandPacket> STREAM_CODEC = Packet.codec(ServerboundPlayerCommandPacket::write, ServerboundPlayerCommandPacket::new);
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final Action action;
/*    */   private final int data;
/*    */   
/* 17 */   public ServerboundPlayerCommandPacket(Entity entity, Action action) { this(entity, action, 0); }
/*    */ 
/*    */   
/*    */   public ServerboundPlayerCommandPacket(Entity entity, Action action, int data) {
/* 21 */     this.id = entity.getId();
/* 22 */     this.action = action;
/* 23 */     this.data = data;
/*    */   }
/*    */   
/*    */   private ServerboundPlayerCommandPacket(FriendlyByteBuf input) {
/* 27 */     this.id = input.readVarInt();
/* 28 */     this.action = (Action)input.readEnum(Action.class);
/* 29 */     this.data = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 33 */     output.writeVarInt(this.id);
/* 34 */     output.writeEnum(this.action);
/* 35 */     output.writeVarInt(this.data);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public PacketType<ServerboundPlayerCommandPacket> type() { return GamePacketTypes.SERVERBOUND_PLAYER_COMMAND; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public void handle(ServerGamePacketListener listener) { listener.handlePlayerCommand(this); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public int getId() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public Action getAction() { return this.action; }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public int getData() { return this.data; }
/*    */   
/*    */   public enum Action
/*    */   {
/* 61 */     STOP_SLEEPING,
/* 62 */     START_SPRINTING,
/* 63 */     STOP_SPRINTING,
/* 64 */     START_RIDING_JUMP,
/* 65 */     STOP_RIDING_JUMP,
/* 66 */     OPEN_INVENTORY,
/* 67 */     START_FALL_FLYING;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundPlayerCommandPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
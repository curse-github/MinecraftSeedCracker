/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.vehicle.minecart.MinecartCommandBlock;
/*    */ import net.minecraft.world.level.BaseCommandBlock;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ServerboundSetCommandMinecartPacket extends Object implements Packet<ServerGamePacketListener> {
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSetCommandMinecartPacket> STREAM_CODEC = Packet.codec(ServerboundSetCommandMinecartPacket::write, ServerboundSetCommandMinecartPacket::new);
/*    */   
/*    */   private final int entity;
/*    */   private final String command;
/*    */   private final boolean trackOutput;
/*    */   
/*    */   public ServerboundSetCommandMinecartPacket(int entity, String command, boolean trackOutput) {
/* 21 */     this.entity = entity;
/* 22 */     this.command = command;
/* 23 */     this.trackOutput = trackOutput;
/*    */   }
/*    */   
/*    */   private ServerboundSetCommandMinecartPacket(FriendlyByteBuf input) {
/* 27 */     this.entity = input.readVarInt();
/* 28 */     this.command = input.readUtf();
/* 29 */     this.trackOutput = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 33 */     output.writeVarInt(this.entity);
/* 34 */     output.writeUtf(this.command);
/* 35 */     output.writeBoolean(this.trackOutput);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public PacketType<ServerboundSetCommandMinecartPacket> type() { return GamePacketTypes.SERVERBOUND_SET_COMMAND_MINECART; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public void handle(ServerGamePacketListener listener) { listener.handleSetCommandMinecart(this); }
/*    */ 
/*    */   
/*    */   public BaseCommandBlock getCommandBlock(Level level) {
/* 49 */     Entity entity = level.getEntity(this.entity);
/* 50 */     if (entity instanceof MinecartCommandBlock) {
/* 51 */       return ((MinecartCommandBlock)entity).getCommandBlock();
/*    */     }
/* 53 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public String getCommand() { return this.command; }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public boolean isTrackOutput() { return this.trackOutput; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSetCommandMinecartPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
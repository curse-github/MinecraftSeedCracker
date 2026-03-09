/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StatusOnly
/*     */   extends ServerboundMovePlayerPacket
/*     */ {
/* 143 */   public static final StreamCodec<FriendlyByteBuf, StatusOnly> STREAM_CODEC = Packet.codec(StatusOnly::write, StatusOnly::read);
/*     */ 
/*     */   
/* 146 */   public StatusOnly(boolean onGround, boolean horizontalCollision) { super(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, onGround, horizontalCollision, false, false); }
/*     */ 
/*     */   
/*     */   private static StatusOnly read(FriendlyByteBuf input) {
/* 150 */     short flags = input.readUnsignedByte();
/* 151 */     boolean onGround = ServerboundMovePlayerPacket.unpackOnGround(flags);
/* 152 */     boolean horizontalCollision = ServerboundMovePlayerPacket.unpackHorizontalCollision(flags);
/* 153 */     return new StatusOnly(onGround, horizontalCollision);
/*     */   }
/*     */ 
/*     */   
/* 157 */   private void write(FriendlyByteBuf output) { output.writeByte(ServerboundMovePlayerPacket.packFlags(this.onGround, this.horizontalCollision)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   public PacketType<StatusOnly> type() { return GamePacketTypes.SERVERBOUND_MOVE_PLAYER_STATUS_ONLY; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundMovePlayerPacket$StatusOnly.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
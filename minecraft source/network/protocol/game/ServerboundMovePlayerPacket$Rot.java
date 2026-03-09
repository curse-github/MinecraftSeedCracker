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
/*     */ public class Rot
/*     */   extends ServerboundMovePlayerPacket
/*     */ {
/* 115 */   public static final StreamCodec<FriendlyByteBuf, Rot> STREAM_CODEC = Packet.codec(Rot::write, Rot::read);
/*     */ 
/*     */   
/* 118 */   public Rot(float yRot, float xRot, boolean onGround, boolean horizontalCollision) { super(0.0D, 0.0D, 0.0D, yRot, xRot, onGround, horizontalCollision, false, true); }
/*     */ 
/*     */   
/*     */   private static Rot read(FriendlyByteBuf input) {
/* 122 */     float yRot = input.readFloat();
/* 123 */     float xRot = input.readFloat();
/* 124 */     short flags = input.readUnsignedByte();
/* 125 */     boolean onGround = ServerboundMovePlayerPacket.unpackOnGround(flags);
/* 126 */     boolean horizontalCollision = ServerboundMovePlayerPacket.unpackHorizontalCollision(flags);
/* 127 */     return new Rot(yRot, xRot, onGround, horizontalCollision);
/*     */   }
/*     */   
/*     */   private void write(FriendlyByteBuf output) {
/* 131 */     output.writeFloat(this.yRot);
/* 132 */     output.writeFloat(this.xRot);
/* 133 */     output.writeByte(ServerboundMovePlayerPacket.packFlags(this.onGround, this.horizontalCollision));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 138 */   public PacketType<Rot> type() { return GamePacketTypes.SERVERBOUND_MOVE_PLAYER_ROT; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundMovePlayerPacket$Rot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
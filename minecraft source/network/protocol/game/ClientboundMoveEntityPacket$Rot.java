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
/*     */ public class Rot
/*     */   extends ClientboundMoveEntityPacket
/*     */ {
/*  93 */   public static final StreamCodec<FriendlyByteBuf, Rot> STREAM_CODEC = Packet.codec(Rot::write, Rot::read);
/*     */ 
/*     */   
/*  96 */   public Rot(int id, byte yRot, byte xRot, boolean onGround) { super(id, (short)0, (short)0, (short)0, yRot, xRot, onGround, true, false); }
/*     */ 
/*     */   
/*     */   private static Rot read(FriendlyByteBuf input) {
/* 100 */     int entityId = input.readVarInt();
/* 101 */     byte yRot = input.readByte();
/* 102 */     byte xRot = input.readByte();
/* 103 */     boolean onGround = input.readBoolean();
/*     */     
/* 105 */     return new Rot(entityId, yRot, xRot, onGround);
/*     */   }
/*     */   
/*     */   private void write(FriendlyByteBuf output) {
/* 109 */     output.writeVarInt(this.entityId);
/* 110 */     output.writeByte(this.yRot);
/* 111 */     output.writeByte(this.xRot);
/* 112 */     output.writeBoolean(this.onGround);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 117 */   public PacketType<Rot> type() { return GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_ROT; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundMoveEntityPacket$Rot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
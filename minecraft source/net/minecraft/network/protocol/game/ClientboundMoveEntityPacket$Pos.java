/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Pos
/*    */   extends ClientboundMoveEntityPacket
/*    */ {
/* 62 */   public static final StreamCodec<FriendlyByteBuf, Pos> STREAM_CODEC = Packet.codec(Pos::write, Pos::read);
/*    */ 
/*    */   
/* 65 */   public Pos(int id, short xa, short ya, short za, boolean onGround) { super(id, xa, ya, za, (byte)0, (byte)0, onGround, false, true); }
/*    */ 
/*    */   
/*    */   private static Pos read(FriendlyByteBuf input) {
/* 69 */     int entityId = input.readVarInt();
/* 70 */     short xa = input.readShort();
/* 71 */     short ya = input.readShort();
/* 72 */     short za = input.readShort();
/* 73 */     boolean onGround = input.readBoolean();
/*    */     
/* 75 */     return new Pos(entityId, xa, ya, za, onGround);
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 79 */     output.writeVarInt(this.entityId);
/* 80 */     output.writeShort(this.xa);
/* 81 */     output.writeShort(this.ya);
/* 82 */     output.writeShort(this.za);
/* 83 */     output.writeBoolean(this.onGround);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 88 */   public PacketType<Pos> type() { return GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_POS; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundMoveEntityPacket$Pos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
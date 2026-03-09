/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public abstract class ClientboundMoveEntityPacket
/*     */   extends Object
/*     */   implements Packet<ClientGamePacketListener> {
/*     */   protected final int entityId;
/*     */   protected final short xa;
/*     */   protected final short ya;
/*     */   protected final short za;
/*     */   protected final byte yRot;
/*     */   protected final byte xRot;
/*     */   protected final boolean onGround;
/*     */   protected final boolean hasRot;
/*     */   protected final boolean hasPos;
/*     */   
/*     */   public static class PosRot
/*     */     extends ClientboundMoveEntityPacket {
/*  27 */     public static final StreamCodec<FriendlyByteBuf, PosRot> STREAM_CODEC = Packet.codec(PosRot::write, PosRot::read);
/*     */ 
/*     */     
/*  30 */     public PosRot(int id, short xa, short ya, short za, byte yRot, byte xRot, boolean onGround) { super(id, xa, ya, za, yRot, xRot, onGround, true, true); }
/*     */ 
/*     */     
/*     */     private static PosRot read(FriendlyByteBuf input) {
/*  34 */       int entityId = input.readVarInt();
/*  35 */       short xa = input.readShort();
/*  36 */       short ya = input.readShort();
/*  37 */       short za = input.readShort();
/*  38 */       byte yRot = input.readByte();
/*  39 */       byte xRot = input.readByte();
/*  40 */       boolean onGround = input.readBoolean();
/*     */       
/*  42 */       return new PosRot(entityId, xa, ya, za, yRot, xRot, onGround);
/*     */     }
/*     */     
/*     */     private void write(FriendlyByteBuf output) {
/*  46 */       output.writeVarInt(this.entityId);
/*  47 */       output.writeShort(this.xa);
/*  48 */       output.writeShort(this.ya);
/*  49 */       output.writeShort(this.za);
/*  50 */       output.writeByte(this.yRot);
/*  51 */       output.writeByte(this.xRot);
/*  52 */       output.writeBoolean(this.onGround);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  57 */     public PacketType<PosRot> type() { return GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_POS_ROT; }
/*     */   }
/*     */   
/*     */   public static class Pos
/*     */     extends ClientboundMoveEntityPacket {
/*  62 */     public static final StreamCodec<FriendlyByteBuf, Pos> STREAM_CODEC = Packet.codec(Pos::write, Pos::read);
/*     */ 
/*     */     
/*  65 */     public Pos(int id, short xa, short ya, short za, boolean onGround) { super(id, xa, ya, za, (byte)0, (byte)0, onGround, false, true); }
/*     */ 
/*     */     
/*     */     private static Pos read(FriendlyByteBuf input) {
/*  69 */       int entityId = input.readVarInt();
/*  70 */       short xa = input.readShort();
/*  71 */       short ya = input.readShort();
/*  72 */       short za = input.readShort();
/*  73 */       boolean onGround = input.readBoolean();
/*     */       
/*  75 */       return new Pos(entityId, xa, ya, za, onGround);
/*     */     }
/*     */     
/*     */     private void write(FriendlyByteBuf output) {
/*  79 */       output.writeVarInt(this.entityId);
/*  80 */       output.writeShort(this.xa);
/*  81 */       output.writeShort(this.ya);
/*  82 */       output.writeShort(this.za);
/*  83 */       output.writeBoolean(this.onGround);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  88 */     public PacketType<Pos> type() { return GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_POS; }
/*     */   }
/*     */   
/*     */   public static class Rot
/*     */     extends ClientboundMoveEntityPacket {
/*  93 */     public static final StreamCodec<FriendlyByteBuf, Rot> STREAM_CODEC = Packet.codec(Rot::write, Rot::read);
/*     */ 
/*     */     
/*  96 */     public Rot(int id, byte yRot, byte xRot, boolean onGround) { super(id, (short)0, (short)0, (short)0, yRot, xRot, onGround, true, false); }
/*     */ 
/*     */     
/*     */     private static Rot read(FriendlyByteBuf input) {
/* 100 */       int entityId = input.readVarInt();
/* 101 */       byte yRot = input.readByte();
/* 102 */       byte xRot = input.readByte();
/* 103 */       boolean onGround = input.readBoolean();
/*     */       
/* 105 */       return new Rot(entityId, yRot, xRot, onGround);
/*     */     }
/*     */     
/*     */     private void write(FriendlyByteBuf output) {
/* 109 */       output.writeVarInt(this.entityId);
/* 110 */       output.writeByte(this.yRot);
/* 111 */       output.writeByte(this.xRot);
/* 112 */       output.writeBoolean(this.onGround);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 117 */     public PacketType<Rot> type() { return GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_ROT; }
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClientboundMoveEntityPacket(int entityId, short xa, short ya, short za, byte yRot, byte xRot, boolean onGround, boolean hasRot, boolean hasPos) {
/* 122 */     this.entityId = entityId;
/* 123 */     this.xa = xa;
/* 124 */     this.ya = ya;
/* 125 */     this.za = za;
/* 126 */     this.yRot = yRot;
/* 127 */     this.xRot = xRot;
/* 128 */     this.onGround = onGround;
/* 129 */     this.hasRot = hasRot;
/* 130 */     this.hasPos = hasPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   public void handle(ClientGamePacketListener listener) { listener.handleMoveEntity(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   public String toString() { return "Entity_" + super.toString(); }
/*     */ 
/*     */ 
/*     */   
/* 147 */   public Entity getEntity(Level level) { return level.getEntity(this.entityId); }
/*     */ 
/*     */ 
/*     */   
/* 151 */   public short getXa() { return this.xa; }
/*     */ 
/*     */ 
/*     */   
/* 155 */   public short getYa() { return this.ya; }
/*     */ 
/*     */ 
/*     */   
/* 159 */   public short getZa() { return this.za; }
/*     */ 
/*     */ 
/*     */   
/* 163 */   public float getYRot() { return Mth.unpackDegrees(this.yRot); }
/*     */ 
/*     */ 
/*     */   
/* 167 */   public float getXRot() { return Mth.unpackDegrees(this.xRot); }
/*     */ 
/*     */ 
/*     */   
/* 171 */   public boolean hasRotation() { return this.hasRot; }
/*     */ 
/*     */ 
/*     */   
/* 175 */   public boolean hasPosition() { return this.hasPos; }
/*     */ 
/*     */ 
/*     */   
/* 179 */   public boolean isOnGround() { return this.onGround; }
/*     */   
/*     */   public abstract PacketType<? extends ClientboundMoveEntityPacket> type();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundMoveEntityPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
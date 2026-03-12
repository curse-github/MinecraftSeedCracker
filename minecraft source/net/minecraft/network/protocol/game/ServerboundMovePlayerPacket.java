/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class ServerboundMovePlayerPacket extends Object implements Packet<ServerGamePacketListener> {
/*     */   private static final int FLAG_ON_GROUND = 1;
/*     */   private static final int FLAG_HORIZONTAL_COLLISION = 2;
/*     */   protected final double x;
/*     */   protected final double y;
/*     */   protected final double z;
/*     */   protected final float yRot;
/*     */   protected final float xRot;
/*     */   protected final boolean onGround;
/*     */   protected final boolean horizontalCollision;
/*     */   protected final boolean hasPos;
/*     */   protected final boolean hasRot;
/*     */   
/*     */   private static int packFlags(boolean onGround, boolean horizontalCollision) {
/*  24 */     int flags = 0;
/*  25 */     if (onGround) {
/*  26 */       flags |= 0x1;
/*     */     }
/*  28 */     if (horizontalCollision) {
/*  29 */       flags |= 0x2;
/*     */     }
/*  31 */     return flags;
/*     */   }
/*     */ 
/*     */   
/*  35 */   private static boolean unpackOnGround(int flags) { return ((flags & true) != 0); }
/*     */ 
/*     */ 
/*     */   
/*  39 */   private static boolean unpackHorizontalCollision(int flags) { return ((flags & 0x2) != 0); }
/*     */   
/*     */   public static class PosRot
/*     */     extends ServerboundMovePlayerPacket {
/*  43 */     public static final StreamCodec<FriendlyByteBuf, PosRot> STREAM_CODEC = Packet.codec(PosRot::write, PosRot::read);
/*     */ 
/*     */     
/*  46 */     public PosRot(Vec3 pos, float yRot, float xRot, boolean onGround, boolean horizontalCollision) { super(pos.x, pos.y, pos.z, yRot, xRot, onGround, horizontalCollision, true, true); }
/*     */ 
/*     */ 
/*     */     
/*  50 */     public PosRot(double x, double y, double z, float yRot, float xRot, boolean onGround, boolean horizontalCollision) { super(x, y, z, yRot, xRot, onGround, horizontalCollision, true, true); }
/*     */ 
/*     */     
/*     */     private static PosRot read(FriendlyByteBuf input) {
/*  54 */       double x = input.readDouble();
/*  55 */       double y = input.readDouble();
/*  56 */       double z = input.readDouble();
/*  57 */       float yRot = input.readFloat();
/*  58 */       float xRot = input.readFloat();
/*  59 */       short flags = input.readUnsignedByte();
/*  60 */       boolean onGround = ServerboundMovePlayerPacket.unpackOnGround(flags);
/*  61 */       boolean horizontalCollision = ServerboundMovePlayerPacket.unpackHorizontalCollision(flags);
/*  62 */       return new PosRot(x, y, z, yRot, xRot, onGround, horizontalCollision);
/*     */     }
/*     */     
/*     */     private void write(FriendlyByteBuf output) {
/*  66 */       output.writeDouble(this.x);
/*  67 */       output.writeDouble(this.y);
/*  68 */       output.writeDouble(this.z);
/*  69 */       output.writeFloat(this.yRot);
/*  70 */       output.writeFloat(this.xRot);
/*  71 */       output.writeByte(ServerboundMovePlayerPacket.packFlags(this.onGround, this.horizontalCollision));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  76 */     public PacketType<PosRot> type() { return GamePacketTypes.SERVERBOUND_MOVE_PLAYER_POS_ROT; }
/*     */   }
/*     */   
/*     */   public static class Pos
/*     */     extends ServerboundMovePlayerPacket {
/*  81 */     public static final StreamCodec<FriendlyByteBuf, Pos> STREAM_CODEC = Packet.codec(Pos::write, Pos::read);
/*     */ 
/*     */     
/*  84 */     public Pos(Vec3 pos, boolean onGround, boolean horizontalCollision) { super(pos.x, pos.y, pos.z, 0.0F, 0.0F, onGround, horizontalCollision, true, false); }
/*     */ 
/*     */ 
/*     */     
/*  88 */     public Pos(double x, double y, double z, boolean onGround, boolean horizontalCollision) { super(x, y, z, 0.0F, 0.0F, onGround, horizontalCollision, true, false); }
/*     */ 
/*     */     
/*     */     private static Pos read(FriendlyByteBuf input) {
/*  92 */       double x = input.readDouble();
/*  93 */       double y = input.readDouble();
/*  94 */       double z = input.readDouble();
/*  95 */       short flags = input.readUnsignedByte();
/*  96 */       boolean onGround = ServerboundMovePlayerPacket.unpackOnGround(flags);
/*  97 */       boolean horizontalCollision = ServerboundMovePlayerPacket.unpackHorizontalCollision(flags);
/*  98 */       return new Pos(x, y, z, onGround, horizontalCollision);
/*     */     }
/*     */     
/*     */     private void write(FriendlyByteBuf output) {
/* 102 */       output.writeDouble(this.x);
/* 103 */       output.writeDouble(this.y);
/* 104 */       output.writeDouble(this.z);
/* 105 */       output.writeByte(ServerboundMovePlayerPacket.packFlags(this.onGround, this.horizontalCollision));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 110 */     public PacketType<Pos> type() { return GamePacketTypes.SERVERBOUND_MOVE_PLAYER_POS; }
/*     */   }
/*     */   
/*     */   public static class Rot
/*     */     extends ServerboundMovePlayerPacket {
/* 115 */     public static final StreamCodec<FriendlyByteBuf, Rot> STREAM_CODEC = Packet.codec(Rot::write, Rot::read);
/*     */ 
/*     */     
/* 118 */     public Rot(float yRot, float xRot, boolean onGround, boolean horizontalCollision) { super(0.0D, 0.0D, 0.0D, yRot, xRot, onGround, horizontalCollision, false, true); }
/*     */ 
/*     */     
/*     */     private static Rot read(FriendlyByteBuf input) {
/* 122 */       float yRot = input.readFloat();
/* 123 */       float xRot = input.readFloat();
/* 124 */       short flags = input.readUnsignedByte();
/* 125 */       boolean onGround = ServerboundMovePlayerPacket.unpackOnGround(flags);
/* 126 */       boolean horizontalCollision = ServerboundMovePlayerPacket.unpackHorizontalCollision(flags);
/* 127 */       return new Rot(yRot, xRot, onGround, horizontalCollision);
/*     */     }
/*     */     
/*     */     private void write(FriendlyByteBuf output) {
/* 131 */       output.writeFloat(this.yRot);
/* 132 */       output.writeFloat(this.xRot);
/* 133 */       output.writeByte(ServerboundMovePlayerPacket.packFlags(this.onGround, this.horizontalCollision));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 138 */     public PacketType<Rot> type() { return GamePacketTypes.SERVERBOUND_MOVE_PLAYER_ROT; }
/*     */   }
/*     */   
/*     */   public static class StatusOnly
/*     */     extends ServerboundMovePlayerPacket {
/* 143 */     public static final StreamCodec<FriendlyByteBuf, StatusOnly> STREAM_CODEC = Packet.codec(StatusOnly::write, StatusOnly::read);
/*     */ 
/*     */     
/* 146 */     public StatusOnly(boolean onGround, boolean horizontalCollision) { super(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, onGround, horizontalCollision, false, false); }
/*     */ 
/*     */     
/*     */     private static StatusOnly read(FriendlyByteBuf input) {
/* 150 */       short flags = input.readUnsignedByte();
/* 151 */       boolean onGround = ServerboundMovePlayerPacket.unpackOnGround(flags);
/* 152 */       boolean horizontalCollision = ServerboundMovePlayerPacket.unpackHorizontalCollision(flags);
/* 153 */       return new StatusOnly(onGround, horizontalCollision);
/*     */     }
/*     */ 
/*     */     
/* 157 */     private void write(FriendlyByteBuf output) { output.writeByte(ServerboundMovePlayerPacket.packFlags(this.onGround, this.horizontalCollision)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     public PacketType<StatusOnly> type() { return GamePacketTypes.SERVERBOUND_MOVE_PLAYER_STATUS_ONLY; }
/*     */   }
/*     */ 
/*     */   
/*     */   protected ServerboundMovePlayerPacket(double x, double y, double z, float yRot, float xRot, boolean onGround, boolean horizontalCollision, boolean hasPos, boolean hasRot) {
/* 167 */     this.x = x;
/* 168 */     this.y = y;
/* 169 */     this.z = z;
/* 170 */     this.yRot = yRot;
/* 171 */     this.xRot = xRot;
/* 172 */     this.onGround = onGround;
/* 173 */     this.horizontalCollision = horizontalCollision;
/* 174 */     this.hasPos = hasPos;
/* 175 */     this.hasRot = hasRot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   public void handle(ServerGamePacketListener listener) { listener.handleMovePlayer(this); }
/*     */ 
/*     */ 
/*     */   
/* 187 */   public double getX(double fallback) { return this.hasPos ? this.x : fallback; }
/*     */ 
/*     */ 
/*     */   
/* 191 */   public double getY(double fallback) { return this.hasPos ? this.y : fallback; }
/*     */ 
/*     */ 
/*     */   
/* 195 */   public double getZ(double fallback) { return this.hasPos ? this.z : fallback; }
/*     */ 
/*     */ 
/*     */   
/* 199 */   public float getYRot(float fallback) { return this.hasRot ? this.yRot : fallback; }
/*     */ 
/*     */ 
/*     */   
/* 203 */   public float getXRot(float fallback) { return this.hasRot ? this.xRot : fallback; }
/*     */ 
/*     */ 
/*     */   
/* 207 */   public boolean isOnGround() { return this.onGround; }
/*     */ 
/*     */ 
/*     */   
/* 211 */   public boolean horizontalCollision() { return this.horizontalCollision; }
/*     */ 
/*     */ 
/*     */   
/* 215 */   public boolean hasPosition() { return this.hasPos; }
/*     */ 
/*     */ 
/*     */   
/* 219 */   public boolean hasRotation() { return this.hasRot; }
/*     */   
/*     */   public abstract PacketType<? extends ServerboundMovePlayerPacket> type();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundMovePlayerPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
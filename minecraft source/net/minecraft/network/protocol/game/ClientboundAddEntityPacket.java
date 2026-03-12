/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ClientboundAddEntityPacket extends Object implements Packet<ClientGamePacketListener> {
/*  20 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundAddEntityPacket> STREAM_CODEC = Packet.codec(ClientboundAddEntityPacket::write, ClientboundAddEntityPacket::new);
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final UUID uuid;
/*     */   
/*     */   private final EntityType<?> type;
/*     */   private final double x;
/*     */   private final double y;
/*     */   private final double z;
/*     */   private final Vec3 movement;
/*     */   private final byte xRot;
/*     */   private final byte yRot;
/*     */   private final byte yHeadRot;
/*     */   private final int data;
/*     */   
/*  36 */   public ClientboundAddEntityPacket(Entity entity, ServerEntity serverEntity) { this(entity, serverEntity, 0); }
/*     */ 
/*     */ 
/*     */   
/*  40 */   public ClientboundAddEntityPacket(Entity entity, ServerEntity serverEntity, int data) { this(entity.getId(), entity.getUUID(), serverEntity.getPositionBase().x(), serverEntity.getPositionBase().y(), serverEntity.getPositionBase().z(), serverEntity.getLastSentXRot(), serverEntity.getLastSentYRot(), entity.getType(), data, serverEntity.getLastSentMovement(), serverEntity.getLastSentYHeadRot()); }
/*     */ 
/*     */ 
/*     */   
/*  44 */   public ClientboundAddEntityPacket(Entity entity, int data, BlockPos pos) { this(entity.getId(), entity.getUUID(), pos.getX(), pos.getY(), pos.getZ(), entity.getXRot(), entity.getYRot(), entity.getType(), data, entity.getDeltaMovement(), entity.getYHeadRot()); }
/*     */ 
/*     */   
/*     */   public ClientboundAddEntityPacket(int id, UUID uuid, double x, double y, double z, float xRot, float yRot, EntityType<?> type, int data, Vec3 movement, double yHeadRot) {
/*  48 */     this.id = id;
/*  49 */     this.uuid = uuid;
/*  50 */     this.x = x;
/*  51 */     this.y = y;
/*  52 */     this.z = z;
/*  53 */     this.movement = movement;
/*  54 */     this.xRot = Mth.packDegrees(xRot);
/*  55 */     this.yRot = Mth.packDegrees(yRot);
/*  56 */     this.yHeadRot = Mth.packDegrees((float)yHeadRot);
/*  57 */     this.type = type;
/*  58 */     this.data = data;
/*     */   }
/*     */   
/*     */   private ClientboundAddEntityPacket(RegistryFriendlyByteBuf input) {
/*  62 */     this.id = input.readVarInt();
/*  63 */     this.uuid = input.readUUID();
/*  64 */     this.type = (EntityType)ByteBufCodecs.registry(Registries.ENTITY_TYPE).decode(input);
/*  65 */     this.x = input.readDouble();
/*  66 */     this.y = input.readDouble();
/*  67 */     this.z = input.readDouble();
/*  68 */     this.movement = input.readLpVec3();
/*  69 */     this.xRot = input.readByte();
/*  70 */     this.yRot = input.readByte();
/*  71 */     this.yHeadRot = input.readByte();
/*  72 */     this.data = input.readVarInt();
/*     */   }
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf output) {
/*  76 */     output.writeVarInt(this.id);
/*  77 */     output.writeUUID(this.uuid);
/*  78 */     ByteBufCodecs.registry(Registries.ENTITY_TYPE).encode(output, this.type);
/*  79 */     output.writeDouble(this.x);
/*  80 */     output.writeDouble(this.y);
/*  81 */     output.writeDouble(this.z);
/*  82 */     output.writeLpVec3(this.movement);
/*  83 */     output.writeByte(this.xRot);
/*  84 */     output.writeByte(this.yRot);
/*  85 */     output.writeByte(this.yHeadRot);
/*  86 */     output.writeVarInt(this.data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public PacketType<ClientboundAddEntityPacket> type() { return GamePacketTypes.CLIENTBOUND_ADD_ENTITY; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public void handle(ClientGamePacketListener listener) { listener.handleAddEntity(this); }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public int getId() { return this.id; }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public UUID getUUID() { return this.uuid; }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public EntityType<?> getType() { return this.type; }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public double getX() { return this.x; }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public double getY() { return this.y; }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public double getZ() { return this.z; }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public Vec3 getMovement() { return this.movement; }
/*     */ 
/*     */ 
/*     */   
/* 128 */   public float getXRot() { return Mth.unpackDegrees(this.xRot); }
/*     */ 
/*     */ 
/*     */   
/* 132 */   public float getYRot() { return Mth.unpackDegrees(this.yRot); }
/*     */ 
/*     */ 
/*     */   
/* 136 */   public float getYHeadRot() { return Mth.unpackDegrees(this.yHeadRot); }
/*     */ 
/*     */ 
/*     */   
/* 140 */   public int getData() { return this.data; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundAddEntityPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.network.protocol.game;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ 
/*     */ public class ClientboundLevelParticlesPacket extends Object implements Packet<ClientGamePacketListener> {
/*  12 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLevelParticlesPacket> STREAM_CODEC = Packet.codec(ClientboundLevelParticlesPacket::write, ClientboundLevelParticlesPacket::new);
/*     */   
/*     */   private final double x;
/*     */   private final double y;
/*     */   private final double z;
/*     */   private final float xDist;
/*     */   private final float yDist;
/*     */   private final float zDist;
/*     */   private final float maxSpeed;
/*     */   private final int count;
/*     */   private final boolean overrideLimiter;
/*     */   private final boolean alwaysShow;
/*     */   private final ParticleOptions particle;
/*     */   
/*     */   public <T extends ParticleOptions> ClientboundLevelParticlesPacket(T particle, boolean overrideLimiter, boolean alwaysShow, double x, double y, double z, float xDist, float yDist, float zDist, float maxSpeed, int count) {
/*  27 */     this.particle = particle;
/*  28 */     this.overrideLimiter = overrideLimiter;
/*  29 */     this.alwaysShow = alwaysShow;
/*  30 */     this.x = x;
/*  31 */     this.y = y;
/*  32 */     this.z = z;
/*  33 */     this.xDist = xDist;
/*  34 */     this.yDist = yDist;
/*  35 */     this.zDist = zDist;
/*  36 */     this.maxSpeed = maxSpeed;
/*  37 */     this.count = count;
/*     */   }
/*     */   
/*     */   private ClientboundLevelParticlesPacket(RegistryFriendlyByteBuf input) {
/*  41 */     this.overrideLimiter = input.readBoolean();
/*  42 */     this.alwaysShow = input.readBoolean();
/*  43 */     this.x = input.readDouble();
/*  44 */     this.y = input.readDouble();
/*  45 */     this.z = input.readDouble();
/*  46 */     this.xDist = input.readFloat();
/*  47 */     this.yDist = input.readFloat();
/*  48 */     this.zDist = input.readFloat();
/*  49 */     this.maxSpeed = input.readFloat();
/*  50 */     this.count = input.readInt();
/*  51 */     this.particle = (ParticleOptions)ParticleTypes.STREAM_CODEC.decode(input);
/*     */   }
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf output) {
/*  55 */     output.writeBoolean(this.overrideLimiter);
/*  56 */     output.writeBoolean(this.alwaysShow);
/*  57 */     output.writeDouble(this.x);
/*  58 */     output.writeDouble(this.y);
/*  59 */     output.writeDouble(this.z);
/*  60 */     output.writeFloat(this.xDist);
/*  61 */     output.writeFloat(this.yDist);
/*  62 */     output.writeFloat(this.zDist);
/*  63 */     output.writeFloat(this.maxSpeed);
/*  64 */     output.writeInt(this.count);
/*  65 */     ParticleTypes.STREAM_CODEC.encode(output, this.particle);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  70 */   public PacketType<ClientboundLevelParticlesPacket> type() { return GamePacketTypes.CLIENTBOUND_LEVEL_PARTICLES; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public void handle(ClientGamePacketListener listener) { listener.handleParticleEvent(this); }
/*     */ 
/*     */ 
/*     */   
/*  79 */   public boolean isOverrideLimiter() { return this.overrideLimiter; }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public boolean alwaysShow() { return this.alwaysShow; }
/*     */ 
/*     */ 
/*     */   
/*  87 */   public double getX() { return this.x; }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public double getY() { return this.y; }
/*     */ 
/*     */ 
/*     */   
/*  95 */   public double getZ() { return this.z; }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public float getXDist() { return this.xDist; }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public float getYDist() { return this.yDist; }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public float getZDist() { return this.zDist; }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public float getMaxSpeed() { return this.maxSpeed; }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public int getCount() { return this.count; }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public ParticleOptions getParticle() { return this.particle; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundLevelParticlesPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
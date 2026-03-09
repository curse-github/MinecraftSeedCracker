/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.TraceableEntity;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EvokerFangs
/*     */   extends Entity
/*     */   implements TraceableEntity
/*     */ {
/*     */   public static final int ATTACK_DURATION = 20;
/*     */   public static final int LIFE_OFFSET = 2;
/*     */   public static final int ATTACK_TRIGGER_TICKS = 14;
/*     */   private static final int DEFAULT_WARMUP_DELAY = 0;
/*  30 */   private int warmupDelayTicks = 0;
/*     */   private boolean sentSpikeEvent;
/*  32 */   private int lifeTicks = 22;
/*     */   
/*     */   private boolean clientSideAttackStarted;
/*     */   
/*     */   private EntityReference<LivingEntity> owner;
/*     */   
/*  38 */   public EvokerFangs(EntityType<? extends EvokerFangs> type, Level level) { super(type, level); }
/*     */ 
/*     */   
/*     */   public EvokerFangs(Level level, double x, double y, double z, float rotaionRadians, int warmupDelayTicks, LivingEntity owner) {
/*  42 */     this(EntityType.EVOKER_FANGS, level);
/*  43 */     this.warmupDelayTicks = warmupDelayTicks;
/*  44 */     setOwner(owner);
/*  45 */     setYRot(rotaionRadians * 57.295776F);
/*  46 */     setPos(x, y, z);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {}
/*     */ 
/*     */   
/*  54 */   public void setOwner(LivingEntity owner) { this.owner = EntityReference.of(owner); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   public LivingEntity getOwner() { return EntityReference.getLivingEntity(this.owner, level()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  64 */     this.warmupDelayTicks = input.getIntOr("Warmup", 0);
/*  65 */     this.owner = EntityReference.read(input, "Owner");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  70 */     output.putInt("Warmup", this.warmupDelayTicks);
/*  71 */     EntityReference.store(this.owner, output, "Owner");
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  76 */     super.tick();
/*     */     
/*  78 */     if (level().isClientSide()) {
/*  79 */       if (this.clientSideAttackStarted) {
/*  80 */         this.lifeTicks--;
/*  81 */         if (this.lifeTicks == 14) {
/*  82 */           for (int i = 0; i < 12; i++) {
/*  83 */             double x = getX() + (this.random.nextDouble() * 2.0D - 1.0D) * getBbWidth() * 0.5D;
/*  84 */             double y = getY() + 0.05D + this.random.nextDouble();
/*  85 */             double z = getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * getBbWidth() * 0.5D;
/*  86 */             double xd = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
/*  87 */             double yd = 0.3D + this.random.nextDouble() * 0.3D;
/*  88 */             double zd = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
/*  89 */             level().addParticle(ParticleTypes.CRIT, x, y + 1.0D, z, xd, yd, zd);
/*     */           }
/*     */         
/*     */         }
/*     */       } 
/*  94 */     } else if (--this.warmupDelayTicks < 0) {
/*  95 */       if (this.warmupDelayTicks == -8) {
/*     */         
/*  97 */         List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(0.2D, 0.0D, 0.2D));
/*  98 */         for (LivingEntity entity : entities) {
/*  99 */           dealDamageTo(entity);
/*     */         }
/*     */       } 
/* 102 */       if (!this.sentSpikeEvent) {
/* 103 */         level().broadcastEntityEvent(this, (byte)4);
/* 104 */         this.sentSpikeEvent = true;
/*     */       } 
/* 106 */       if (--this.lifeTicks < 0) {
/* 107 */         discard();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void dealDamageTo(LivingEntity entity) {
/* 114 */     LivingEntity currentOwner = getOwner();
/* 115 */     if (!entity.isAlive() || entity.isInvulnerable() || entity == currentOwner) {
/*     */       return;
/*     */     }
/* 118 */     if (currentOwner == null) {
/* 119 */       entity.hurt(damageSources().magic(), 6.0F);
/*     */     } else {
/* 121 */       if (currentOwner.isAlliedTo(entity)) {
/*     */         return;
/*     */       }
/* 124 */       DamageSource damageSource = damageSources().indirectMagic(this, currentOwner);
/* 125 */       Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (entity.hurtServer(serverLevel, damageSource, 6.0F)) {
/* 126 */           EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
/*     */         } }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 133 */     super.handleEntityEvent(id);
/*     */     
/* 135 */     if (id == 4) {
/* 136 */       this.clientSideAttackStarted = true;
/* 137 */       if (!isSilent()) {
/* 138 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.EVOKER_FANGS_ATTACK, getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getAnimationProgress(float a) {
/* 144 */     if (!this.clientSideAttackStarted) {
/* 145 */       return 0.0F;
/*     */     }
/* 147 */     int remainingLife = this.lifeTicks - 2;
/* 148 */     if (remainingLife <= 0) {
/* 149 */       return 1.0F;
/*     */     }
/* 151 */     return 1.0F - (remainingLife - a) / 20.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\EvokerFangs.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
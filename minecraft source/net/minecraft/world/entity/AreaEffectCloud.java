/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class AreaEffectCloud
/*     */   extends Entity
/*     */   implements TraceableEntity
/*     */ {
/*     */   private static final int TIME_BETWEEN_APPLICATIONS = 5;
/*  34 */   private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.FLOAT);
/*  35 */   private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.BOOLEAN);
/*  36 */   private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.PARTICLE);
/*     */   
/*     */   private static final float MAX_RADIUS = 32.0F;
/*     */   
/*     */   private static final int DEFAULT_AGE = 0;
/*     */   
/*     */   private static final int DEFAULT_DURATION_ON_USE = 0;
/*     */   
/*     */   private static final float DEFAULT_RADIUS_ON_USE = 0.0F;
/*     */   
/*     */   private static final float DEFAULT_RADIUS_PER_TICK = 0.0F;
/*     */   private static final float DEFAULT_POTION_DURATION_SCALE = 1.0F;
/*     */   private static final float MINIMAL_RADIUS = 0.5F;
/*     */   private static final float DEFAULT_RADIUS = 3.0F;
/*     */   public static final float DEFAULT_WIDTH = 6.0F;
/*     */   public static final float HEIGHT = 0.5F;
/*     */   public static final int INFINITE_DURATION = -1;
/*     */   public static final int DEFAULT_LINGERING_DURATION = 600;
/*     */   private static final int DEFAULT_WAIT_TIME = 20;
/*     */   private static final int DEFAULT_REAPPLICATION_DELAY = 20;
/*  56 */   private static final ColorParticleOption DEFAULT_PARTICLE = ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, -1);
/*     */   
/*     */   private ParticleOptions customParticle;
/*  59 */   private PotionContents potionContents = PotionContents.EMPTY;
/*  60 */   private float potionDurationScale = 1.0F;
/*  61 */   private final Map<Entity, Integer> victims = Maps.newHashMap();
/*  62 */   private int duration = -1;
/*  63 */   private int waitTime = 20;
/*  64 */   private int reapplicationDelay = 20;
/*  65 */   private int durationOnUse = 0;
/*  66 */   private float radiusOnUse = 0.0F;
/*  67 */   private float radiusPerTick = 0.0F;
/*     */   private EntityReference<LivingEntity> owner;
/*     */   
/*     */   public AreaEffectCloud(EntityType<? extends AreaEffectCloud> type, Level level) {
/*  71 */     super(type, level);
/*  72 */     this.noPhysics = true;
/*     */   }
/*     */   
/*     */   public AreaEffectCloud(Level level, double x, double y, double z) {
/*  76 */     this(EntityType.AREA_EFFECT_CLOUD, level);
/*  77 */     setPos(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  82 */     entityData.define(DATA_RADIUS, Float.valueOf(3.0F));
/*  83 */     entityData.define(DATA_WAITING, Boolean.valueOf(false));
/*  84 */     entityData.define(DATA_PARTICLE, DEFAULT_PARTICLE);
/*     */   }
/*     */   
/*     */   public void setRadius(float radius) {
/*  88 */     if (!level().isClientSide()) {
/*  89 */       getEntityData().set(DATA_RADIUS, Float.valueOf(Mth.clamp(radius, 0.0F, 32.0F)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void refreshDimensions() {
/*  95 */     double x = getX();
/*  96 */     double y = getY();
/*  97 */     double z = getZ();
/*  98 */     super.refreshDimensions();
/*  99 */     setPos(x, y, z);
/*     */   }
/*     */ 
/*     */   
/* 103 */   public float getRadius() { return ((Float)getEntityData().get(DATA_RADIUS)).floatValue(); }
/*     */ 
/*     */   
/*     */   public void setPotionContents(PotionContents contents) {
/* 107 */     this.potionContents = contents;
/* 108 */     updateParticle();
/*     */   }
/*     */   
/*     */   public void setCustomParticle(ParticleOptions customParticle) {
/* 112 */     this.customParticle = customParticle;
/* 113 */     updateParticle();
/*     */   }
/*     */ 
/*     */   
/* 117 */   public void setPotionDurationScale(float scale) { this.potionDurationScale = scale; }
/*     */ 
/*     */   
/*     */   private void updateParticle() {
/* 121 */     if (this.customParticle != null) {
/* 122 */       this.entityData.set(DATA_PARTICLE, this.customParticle);
/*     */     } else {
/* 124 */       int color = ARGB.opaque(this.potionContents.getColor());
/* 125 */       this.entityData.set(DATA_PARTICLE, ColorParticleOption.create(DEFAULT_PARTICLE.getType(), color));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 130 */   public void addEffect(MobEffectInstance effect) { setPotionContents(this.potionContents.withEffectAdded(effect)); }
/*     */ 
/*     */ 
/*     */   
/* 134 */   public ParticleOptions getParticle() { return (ParticleOptions)getEntityData().get(DATA_PARTICLE); }
/*     */ 
/*     */ 
/*     */   
/* 138 */   protected void setWaiting(boolean waiting) { getEntityData().set(DATA_WAITING, Boolean.valueOf(waiting)); }
/*     */ 
/*     */ 
/*     */   
/* 142 */   public boolean isWaiting() { return ((Boolean)getEntityData().get(DATA_WAITING)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 146 */   public int getDuration() { return this.duration; }
/*     */ 
/*     */ 
/*     */   
/* 150 */   public void setDuration(int duration) { this.duration = duration; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 155 */     super.tick();
/* 156 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 157 */       serverTick(serverLevel); }
/*     */     else
/* 159 */     { clientTick(); }
/*     */   
/*     */   } private void clientTick() {
/*     */     float particleRadius;
/*     */     int particleCount;
/* 164 */     boolean isWaiting = isWaiting();
/* 165 */     float radius = getRadius();
/*     */     
/* 167 */     if (isWaiting && this.random.nextBoolean()) {
/*     */       return;
/*     */     }
/* 170 */     ParticleOptions particle = getParticle();
/*     */ 
/*     */ 
/*     */     
/* 174 */     if (isWaiting) {
/* 175 */       particleCount = 2;
/* 176 */       particleRadius = 0.2F;
/*     */     } else {
/* 178 */       particleCount = Mth.ceil(3.1415927F * radius * radius);
/* 179 */       particleRadius = radius;
/*     */     } 
/*     */     
/* 182 */     for (int i = 0; i < particleCount; i++) {
/* 183 */       float angle = this.random.nextFloat() * 6.2831855F;
/* 184 */       float distance = Mth.sqrt(this.random.nextFloat()) * particleRadius;
/* 185 */       double x = getX() + (Mth.cos(angle) * distance);
/* 186 */       double y = getY();
/* 187 */       double z = getZ() + (Mth.sin(angle) * distance);
/*     */       
/* 189 */       if (particle.getType() == ParticleTypes.ENTITY_EFFECT) {
/* 190 */         if (isWaiting && this.random.nextBoolean()) {
/* 191 */           level().addAlwaysVisibleParticle(DEFAULT_PARTICLE, x, y, z, 0.0D, 0.0D, 0.0D);
/*     */         } else {
/* 193 */           level().addAlwaysVisibleParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
/*     */         }
/*     */       
/* 196 */       } else if (isWaiting) {
/* 197 */         level().addAlwaysVisibleParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
/*     */       } else {
/* 199 */         level().addAlwaysVisibleParticle(particle, x, y, z, (0.5D - this.random.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - this.random.nextDouble()) * 0.15D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void serverTick(ServerLevel serverLevel) {
/* 206 */     if (this.duration != -1 && this.tickCount - this.waitTime >= this.duration) {
/* 207 */       discard();
/*     */       
/*     */       return;
/*     */     } 
/* 211 */     boolean isWaiting = isWaiting();
/* 212 */     boolean shouldWait = (this.tickCount < this.waitTime);
/* 213 */     if (isWaiting != shouldWait) {
/* 214 */       setWaiting(shouldWait);
/*     */     }
/* 216 */     if (shouldWait) {
/*     */       return;
/*     */     }
/*     */     
/* 220 */     float radius = getRadius();
/* 221 */     if (this.radiusPerTick != 0.0F) {
/* 222 */       radius += this.radiusPerTick;
/* 223 */       if (radius < 0.5F) {
/* 224 */         discard();
/*     */         return;
/*     */       } 
/* 227 */       setRadius(radius);
/*     */     } 
/*     */     
/* 230 */     if (this.tickCount % 5 == 0) {
/* 231 */       this.victims.entrySet().removeIf(entry -> (this.tickCount >= ((Integer)entry.getValue()).intValue()));
/*     */       
/* 233 */       if (!this.potionContents.hasEffects()) {
/* 234 */         this.victims.clear();
/*     */       } else {
/* 236 */         List<MobEffectInstance> allEffects = new ArrayList<MobEffectInstance>();
/* 237 */         Objects.requireNonNull(allEffects); this.potionContents.forEachEffect(allEffects::add, this.potionDurationScale);
/*     */         
/* 239 */         List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox());
/* 240 */         if (!entities.isEmpty()) {
/* 241 */           for (LivingEntity entity : entities) {
/* 242 */             Objects.requireNonNull(entity); if (this.victims.containsKey(entity) || !entity.isAffectedByPotions() || allEffects.stream().noneMatch(entity::canBeAffected)) {
/*     */               continue;
/*     */             }
/* 245 */             double xd = entity.getX() - getX();
/* 246 */             double zd = entity.getZ() - getZ();
/* 247 */             double dist = xd * xd + zd * zd;
/* 248 */             if (dist <= (radius * radius)) {
/* 249 */               this.victims.put(entity, Integer.valueOf(this.tickCount + this.reapplicationDelay));
/* 250 */               for (MobEffectInstance effect : allEffects) {
/* 251 */                 if (((MobEffect)effect.getEffect().value()).isInstantenous()) {
/* 252 */                   ((MobEffect)effect.getEffect().value()).applyInstantenousEffect(serverLevel, this, getOwner(), entity, effect.getAmplifier(), 0.5D); continue;
/*     */                 } 
/* 254 */                 entity.addEffect(new MobEffectInstance(effect), this);
/*     */               } 
/*     */               
/* 257 */               if (this.radiusOnUse != 0.0F) {
/* 258 */                 radius += this.radiusOnUse;
/* 259 */                 if (radius < 0.5F) {
/* 260 */                   discard();
/*     */                   return;
/*     */                 } 
/* 263 */                 setRadius(radius);
/*     */               } 
/* 265 */               if (this.durationOnUse != 0 && this.duration != -1) {
/* 266 */                 this.duration += this.durationOnUse;
/* 267 */                 if (this.duration <= 0) {
/* 268 */                   discard();
/*     */                   return;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 280 */   public float getRadiusOnUse() { return this.radiusOnUse; }
/*     */ 
/*     */ 
/*     */   
/* 284 */   public void setRadiusOnUse(float radiusOnUse) { this.radiusOnUse = radiusOnUse; }
/*     */ 
/*     */ 
/*     */   
/* 288 */   public float getRadiusPerTick() { return this.radiusPerTick; }
/*     */ 
/*     */ 
/*     */   
/* 292 */   public void setRadiusPerTick(float radiusPerTick) { this.radiusPerTick = radiusPerTick; }
/*     */ 
/*     */ 
/*     */   
/* 296 */   public int getDurationOnUse() { return this.durationOnUse; }
/*     */ 
/*     */ 
/*     */   
/* 300 */   public void setDurationOnUse(int durationOnUse) { this.durationOnUse = durationOnUse; }
/*     */ 
/*     */ 
/*     */   
/* 304 */   public int getWaitTime() { return this.waitTime; }
/*     */ 
/*     */ 
/*     */   
/* 308 */   public void setWaitTime(int waitTime) { this.waitTime = waitTime; }
/*     */ 
/*     */ 
/*     */   
/* 312 */   public void setOwner(LivingEntity owner) { this.owner = EntityReference.of(owner); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 317 */   public LivingEntity getOwner() { return EntityReference.getLivingEntity(this.owner, level()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 322 */     this.tickCount = input.getIntOr("Age", 0);
/* 323 */     this.duration = input.getIntOr("Duration", -1);
/* 324 */     this.waitTime = input.getIntOr("WaitTime", 20);
/* 325 */     this.reapplicationDelay = input.getIntOr("ReapplicationDelay", 20);
/* 326 */     this.durationOnUse = input.getIntOr("DurationOnUse", 0);
/* 327 */     this.radiusOnUse = input.getFloatOr("RadiusOnUse", 0.0F);
/* 328 */     this.radiusPerTick = input.getFloatOr("RadiusPerTick", 0.0F);
/* 329 */     setRadius(input.getFloatOr("Radius", 3.0F));
/* 330 */     this.owner = EntityReference.read(input, "Owner");
/*     */     
/* 332 */     setCustomParticle((ParticleOptions)input.read("custom_particle", ParticleTypes.CODEC).orElse(null));
/* 333 */     setPotionContents((PotionContents)input.read("potion_contents", PotionContents.CODEC).orElse(PotionContents.EMPTY));
/*     */     
/* 335 */     this.potionDurationScale = input.getFloatOr("potion_duration_scale", 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 340 */     output.putInt("Age", this.tickCount);
/* 341 */     output.putInt("Duration", this.duration);
/* 342 */     output.putInt("WaitTime", this.waitTime);
/* 343 */     output.putInt("ReapplicationDelay", this.reapplicationDelay);
/* 344 */     output.putInt("DurationOnUse", this.durationOnUse);
/* 345 */     output.putFloat("RadiusOnUse", this.radiusOnUse);
/* 346 */     output.putFloat("RadiusPerTick", this.radiusPerTick);
/* 347 */     output.putFloat("Radius", getRadius());
/*     */     
/* 349 */     output.storeNullable("custom_particle", ParticleTypes.CODEC, this.customParticle);
/*     */     
/* 351 */     EntityReference.store(this.owner, output, "Owner");
/*     */     
/* 353 */     if (!this.potionContents.equals(PotionContents.EMPTY)) {
/* 354 */       output.store("potion_contents", PotionContents.CODEC, this.potionContents);
/*     */     }
/* 356 */     if (this.potionDurationScale != 1.0F) {
/* 357 */       output.putFloat("potion_duration_scale", this.potionDurationScale);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 363 */     if (DATA_RADIUS.equals(accessor)) {
/* 364 */       refreshDimensions();
/*     */     }
/* 366 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 371 */   public PushReaction getPistonPushReaction() { return PushReaction.IGNORE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 376 */   public EntityDimensions getDimensions(Pose pose) { return EntityDimensions.scalable(getRadius() * 2.0F, 0.5F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 381 */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 386 */     if (type == DataComponents.POTION_CONTENTS) {
/* 387 */       return (T)castComponentValue(type, this.potionContents);
/*     */     }
/* 389 */     if (type == DataComponents.POTION_DURATION_SCALE) {
/* 390 */       return (T)castComponentValue(type, Float.valueOf(this.potionDurationScale));
/*     */     }
/*     */     
/* 393 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 398 */     applyImplicitComponentIfPresent(components, DataComponents.POTION_CONTENTS);
/* 399 */     applyImplicitComponentIfPresent(components, DataComponents.POTION_DURATION_SCALE);
/* 400 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 405 */     if (type == DataComponents.POTION_CONTENTS) {
/* 406 */       setPotionContents((PotionContents)castComponentValue(DataComponents.POTION_CONTENTS, value));
/* 407 */       return true;
/*     */     } 
/* 409 */     if (type == DataComponents.POTION_DURATION_SCALE) {
/* 410 */       setPotionDurationScale(((Float)castComponentValue(DataComponents.POTION_DURATION_SCALE, value)).floatValue());
/* 411 */       return true;
/*     */     } 
/*     */     
/* 414 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\AreaEffectCloud.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
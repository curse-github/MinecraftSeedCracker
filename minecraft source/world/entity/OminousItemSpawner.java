/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ProjectileItem;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class OminousItemSpawner
/*     */   extends Entity
/*     */ {
/*     */   private static final int SPAWN_ITEM_DELAY_MIN = 60;
/*     */   private static final int SPAWN_ITEM_DELAY_MAX = 120;
/*     */   private static final String TAG_SPAWN_ITEM_AFTER_TICKS = "spawn_item_after_ticks";
/*     */   private static final String TAG_ITEM = "item";
/*  31 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(OminousItemSpawner.class, EntityDataSerializers.ITEM_STACK);
/*     */   public static final int TICKS_BEFORE_ABOUT_TO_SPAWN_SOUND = 36;
/*     */   private long spawnItemAfterTicks;
/*     */   
/*     */   public OminousItemSpawner(EntityType<? extends OminousItemSpawner> type, Level level) {
/*  36 */     super(type, level);
/*  37 */     this.noPhysics = true;
/*     */   }
/*     */   
/*     */   public static OminousItemSpawner create(Level level, ItemStack item) {
/*  41 */     OminousItemSpawner itemSpawner = new OminousItemSpawner(EntityType.OMINOUS_ITEM_SPAWNER, level);
/*  42 */     itemSpawner.spawnItemAfterTicks = level.random.nextIntBetweenInclusive(60, 120);
/*  43 */     itemSpawner.setItem(item);
/*  44 */     return itemSpawner;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  49 */     super.tick();
/*  50 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  51 */       tickServer(serverLevel); }
/*     */     else
/*  53 */     { tickClient(); }
/*     */   
/*     */   }
/*     */   
/*     */   private void tickServer(ServerLevel level) {
/*  58 */     if (this.tickCount == this.spawnItemAfterTicks - 36L) {
/*  59 */       level.playSound(null, blockPosition(), SoundEvents.TRIAL_SPAWNER_ABOUT_TO_SPAWN_ITEM, SoundSource.NEUTRAL);
/*     */     }
/*  61 */     if (this.tickCount >= this.spawnItemAfterTicks) {
/*  62 */       spawnItem();
/*  63 */       kill(level);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tickClient() {
/*  68 */     if (level().getGameTime() % 5L == 0L)
/*  69 */       addParticles(); 
/*     */   }
/*     */   private void spawnItem() {
/*     */     ItemEntity itemEntity;
/*     */     ServerLevel level;
/*  74 */     Level level1 = level(); if (level1 instanceof ServerLevel) { level = (ServerLevel)level1; }
/*     */     else
/*     */     { return; }
/*     */     
/*  78 */     ItemStack item = getItem();
/*  79 */     if (item.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  84 */     Item item1 = item.getItem(); if (item1 instanceof ProjectileItem) { ProjectileItem projectileItem = (ProjectileItem)item1;
/*  85 */       itemEntity = spawnProjectile(level, projectileItem, item); }
/*     */     else
/*  87 */     { itemEntity = new ItemEntity(level, getX(), getY(), getZ(), item);
/*  88 */       level.addFreshEntity(itemEntity); }
/*     */ 
/*     */     
/*  91 */     level.levelEvent(3021, blockPosition(), 1);
/*  92 */     level.gameEvent(itemEntity, GameEvent.ENTITY_PLACE, position());
/*  93 */     setItem(ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   private Entity spawnProjectile(ServerLevel level, ProjectileItem projectileItem, ItemStack item) {
/*  97 */     ProjectileItem.DispenseConfig dispenseConfig = projectileItem.createDispenseConfig();
/*  98 */     dispenseConfig.overrideDispenseEvent().ifPresent(event -> level.levelEvent(event, blockPosition(), 0));
/*  99 */     Direction direction = Direction.DOWN;
/* 100 */     Projectile projectile = Projectile.spawnProjectileUsingShoot(projectileItem
/* 101 */         .asProjectile(level, position(), item, direction), level, item, direction
/*     */         
/* 103 */         .getStepX(), direction.getStepY(), direction.getStepZ(), dispenseConfig
/* 104 */         .power(), dispenseConfig.uncertainty());
/*     */     
/* 106 */     projectile.setOwner(this);
/* 107 */     return projectile;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 112 */   protected void defineSynchedData(SynchedEntityData.Builder entityData) { entityData.define(DATA_ITEM, ItemStack.EMPTY); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 117 */     setItem((ItemStack)input.read("item", ItemStack.CODEC).orElse(ItemStack.EMPTY));
/* 118 */     this.spawnItemAfterTicks = input.getLongOr("spawn_item_after_ticks", 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 123 */     if (!getItem().isEmpty()) {
/* 124 */       output.store("item", ItemStack.CODEC, getItem());
/*     */     }
/* 126 */     output.putLong("spawn_item_after_ticks", this.spawnItemAfterTicks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 131 */   protected boolean canAddPassenger(Entity passenger) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   protected boolean couldAcceptPassenger() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   protected void addPassenger(Entity passenger) { throw new IllegalStateException("Should never addPassenger without checking couldAcceptPassenger()"); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public PushReaction getPistonPushReaction() { return PushReaction.IGNORE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   public boolean isIgnoringBlockTriggers() { return true; }
/*     */ 
/*     */   
/*     */   public void addParticles() {
/* 155 */     Vec3 flyTowards = position();
/* 156 */     int particleCount = this.random.nextIntBetweenInclusive(1, 3);
/* 157 */     for (int i = 0; i < particleCount; i++) {
/* 158 */       double radius = 0.4D;
/*     */ 
/*     */ 
/*     */       
/* 162 */       Vec3 flyFrom = new Vec3(getX() + 0.4D * (this.random.nextGaussian() - this.random.nextGaussian()), getY() + 0.4D * (this.random.nextGaussian() - this.random.nextGaussian()), getZ() + 0.4D * (this.random.nextGaussian() - this.random.nextGaussian()));
/*     */       
/* 164 */       Vec3 randomDirection = flyTowards.vectorTo(flyFrom);
/* 165 */       level().addParticle(ParticleTypes.OMINOUS_SPAWNING, flyTowards.x(), flyTowards.y(), flyTowards.z(), randomDirection.x(), randomDirection.y(), randomDirection.z());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 170 */   public ItemStack getItem() { return (ItemStack)getEntityData().get(DATA_ITEM); }
/*     */ 
/*     */ 
/*     */   
/* 174 */   private void setItem(ItemStack itemStack) { getEntityData().set(DATA_ITEM, itemStack); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\OminousItemSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
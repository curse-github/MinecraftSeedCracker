/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableWitchTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestHealableRaiderTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Witch
/*     */   extends Raider
/*     */   implements RangedAttackMob
/*     */ {
/*  48 */   private static final Identifier SPEED_MODIFIER_DRINKING_ID = Identifier.withDefaultNamespace("drinking");
/*  49 */   private static final AttributeModifier SPEED_MODIFIER_DRINKING = new AttributeModifier(SPEED_MODIFIER_DRINKING_ID, -0.25D, AttributeModifier.Operation.ADD_VALUE);
/*     */   
/*  51 */   private static final EntityDataAccessor<Boolean> DATA_USING_ITEM = SynchedEntityData.defineId(Witch.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private int usingTime;
/*     */   
/*     */   private NearestHealableRaiderTargetGoal<Raider> healRaidersGoal;
/*     */   
/*     */   private NearestAttackableWitchTargetGoal<Player> attackPlayersGoal;
/*     */   
/*  59 */   public Witch(EntityType<? extends Witch> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  64 */     super.registerGoals();
/*     */ 
/*     */     
/*  67 */     this.healRaidersGoal = new NearestHealableRaiderTargetGoal(this, Raider.class, true, (target, level) -> (hasActiveRaid() && target.getType() != EntityType.WITCH));
/*  68 */     this.attackPlayersGoal = new NearestAttackableWitchTargetGoal(this, Player.class, 10, true, false, null);
/*     */     
/*  70 */     this.goalSelector.addGoal(1, new FloatGoal(this));
/*  71 */     this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 60, 10.0F));
/*  72 */     this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
/*  73 */     this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
/*  74 */     this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
/*     */     
/*  76 */     this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[] { Raider.class }));
/*  77 */     this.targetSelector.addGoal(2, this.healRaidersGoal);
/*  78 */     this.targetSelector.addGoal(3, this.attackPlayersGoal);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  83 */     super.defineSynchedData(entityData);
/*     */     
/*  85 */     entityData.define(DATA_USING_ITEM, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  90 */   protected SoundEvent getAmbientSound() { return SoundEvents.WITCH_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.WITCH_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   protected SoundEvent getDeathSound() { return SoundEvents.WITCH_DEATH; }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public void setUsingItem(boolean using) { getEntityData().set(DATA_USING_ITEM, Boolean.valueOf(using)); }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public boolean isDrinkingPotion() { return ((Boolean)getEntityData().get(DATA_USING_ITEM)).booleanValue(); }
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 112 */     return Monster.createMonsterAttributes()
/* 113 */       .add(Attributes.MAX_HEALTH, 26.0D)
/* 114 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 119 */     if (!level().isClientSide() && isAlive()) {
/* 120 */       this.healRaidersGoal.decrementCooldown();
/*     */       
/* 122 */       if (this.healRaidersGoal.getCooldown() <= 0) {
/* 123 */         this.attackPlayersGoal.setCanAttack(true);
/*     */       } else {
/* 125 */         this.attackPlayersGoal.setCanAttack(false);
/*     */       } 
/*     */       
/* 128 */       if (isDrinkingPotion()) {
/* 129 */         if (this.usingTime-- <= 0) {
/* 130 */           setUsingItem(false);
/* 131 */           ItemStack itemStack = getMainHandItem();
/* 132 */           setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*     */           
/* 134 */           PotionContents potion = (PotionContents)itemStack.get(DataComponents.POTION_CONTENTS);
/* 135 */           if (itemStack.is(Items.POTION) && potion != null) {
/* 136 */             potion.forEachEffect(this::addEffect, ((Float)itemStack.getOrDefault(DataComponents.POTION_DURATION_SCALE, Float.valueOf(1.0F))).floatValue());
/*     */           }
/* 138 */           gameEvent(GameEvent.DRINK);
/* 139 */           getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_DRINKING.id());
/*     */         } 
/*     */       } else {
/* 142 */         Holder<Potion> potion = null;
/*     */         
/* 144 */         if (this.random.nextFloat() < 0.15F && isEyeInFluid(FluidTags.WATER) && !hasEffect(MobEffects.WATER_BREATHING)) {
/* 145 */           potion = Potions.WATER_BREATHING;
/* 146 */         } else if (this.random.nextFloat() < 0.15F && (isOnFire() || (getLastDamageSource() != null && getLastDamageSource().is(DamageTypeTags.IS_FIRE))) && !hasEffect(MobEffects.FIRE_RESISTANCE)) {
/* 147 */           potion = Potions.FIRE_RESISTANCE;
/* 148 */         } else if (this.random.nextFloat() < 0.05F && getHealth() < getMaxHealth()) {
/* 149 */           potion = Potions.HEALING;
/* 150 */         } else if (this.random.nextFloat() < 0.5F && getTarget() != null && !hasEffect(MobEffects.SPEED) && getTarget().distanceToSqr(this) > 121.0D) {
/* 151 */           potion = Potions.SWIFTNESS;
/*     */         } 
/*     */         
/* 154 */         if (potion != null) {
/* 155 */           setItemSlot(EquipmentSlot.MAINHAND, PotionContents.createItemStack(Items.POTION, potion));
/* 156 */           this.usingTime = getMainHandItem().getUseDuration(this);
/* 157 */           setUsingItem(true);
/* 158 */           if (!isSilent()) {
/* 159 */             level().playSound(null, getX(), getY(), getZ(), SoundEvents.WITCH_DRINK, getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
/*     */           }
/* 161 */           AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
/* 162 */           speed.removeModifier(SPEED_MODIFIER_DRINKING_ID);
/* 163 */           speed.addTransientModifier(SPEED_MODIFIER_DRINKING);
/*     */         } 
/*     */       } 
/*     */       
/* 167 */       if (this.random.nextFloat() < 7.5E-4F) {
/* 168 */         level().broadcastEntityEvent(this, (byte)15);
/*     */       }
/*     */     } 
/*     */     
/* 172 */     super.aiStep();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 177 */   public SoundEvent getCelebrateSound() { return SoundEvents.WITCH_CELEBRATE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 182 */     if (id == 15) {
/* 183 */       for (int i = 0; i < this.random.nextInt(35) + 10; i++) {
/* 184 */         level().addParticle(ParticleTypes.WITCH, getX() + this.random.nextGaussian() * 0.12999999523162842D, (getBoundingBox()).maxY + 0.5D + this.random.nextGaussian() * 0.12999999523162842D, getZ() + this.random.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D);
/*     */       }
/*     */     } else {
/* 187 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getDamageAfterMagicAbsorb(DamageSource damageSource, float damage) {
/* 193 */     damage = super.getDamageAfterMagicAbsorb(damageSource, damage);
/*     */     
/* 195 */     if (damageSource.getEntity() == this) {
/* 196 */       damage = 0.0F;
/*     */     }
/* 198 */     if (damageSource.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
/* 199 */       damage *= 0.15F;
/*     */     }
/*     */     
/* 202 */     return damage;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity target, float power) {
/* 207 */     if (isDrinkingPotion()) {
/*     */       return;
/*     */     }
/*     */     
/* 211 */     Vec3 targetMovement = target.getDeltaMovement();
/* 212 */     double xd = target.getX() + targetMovement.x - getX();
/* 213 */     double yd = target.getEyeY() - 1.100000023841858D - getY();
/* 214 */     double zd = target.getZ() + targetMovement.z - getZ();
/* 215 */     double dist = Math.sqrt(xd * xd + zd * zd);
/* 216 */     Holder<Potion> potion = Potions.HARMING;
/*     */ 
/*     */     
/* 219 */     if (target instanceof Raider) {
/* 220 */       if (target.getHealth() <= 4.0F) {
/* 221 */         potion = Potions.HEALING;
/*     */       } else {
/* 223 */         potion = Potions.REGENERATION;
/*     */       } 
/* 225 */       setTarget(null);
/*     */     }
/* 227 */     else if (dist >= 8.0D && !target.hasEffect(MobEffects.SLOWNESS)) {
/* 228 */       potion = Potions.SLOWNESS;
/* 229 */     } else if (target.getHealth() >= 8.0F && !target.hasEffect(MobEffects.POISON)) {
/* 230 */       potion = Potions.POISON;
/* 231 */     } else if (dist <= 3.0D && !target.hasEffect(MobEffects.WEAKNESS) && this.random.nextFloat() < 0.25F) {
/* 232 */       potion = Potions.WEAKNESS;
/*     */     } 
/*     */ 
/*     */     
/* 236 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 237 */       ItemStack itemStack = PotionContents.createItemStack(Items.SPLASH_POTION, potion);
/* 238 */       Projectile.spawnProjectileUsingShoot(net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion::new, serverLevel, itemStack, this, xd, yd + dist * 0.2D, zd, 0.75F, 8.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 245 */     if (!isSilent()) {
/* 246 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.WITCH_THROW, getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyRaidBuffs(ServerLevel level, int wave, boolean isCaptain) {}
/*     */ 
/*     */ 
/*     */   
/* 256 */   public boolean canBeLeader() { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Witch.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
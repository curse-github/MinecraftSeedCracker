/*     */ package net.minecraft.world.entity.animal.golem;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Crackiness;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.GolemRandomStrollInVillageGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.OfferFlowerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.DefendVillageTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IronGolem
/*     */   extends AbstractGolem
/*     */   implements NeutralMob
/*     */ {
/*  54 */   protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(IronGolem.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   private static final int IRON_INGOT_HEAL_AMOUNT = 25;
/*     */   
/*     */   private static final boolean DEFAULT_PLAYER_CREATED = false;
/*     */   private int attackAnimationTick;
/*     */   private int offerFlowerTick;
/*  61 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   
/*     */   private long persistentAngerEndTime;
/*     */   private EntityReference<LivingEntity> persistentAngerTarget;
/*     */   
/*  66 */   public IronGolem(EntityType<? extends IronGolem> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  71 */     this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
/*  72 */     this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
/*  73 */     this.goalSelector.addGoal(2, new MoveBackToVillageGoal(this, 0.6D, false));
/*  74 */     this.goalSelector.addGoal(4, new GolemRandomStrollInVillageGoal(this, 0.6D));
/*  75 */     this.goalSelector.addGoal(5, new OfferFlowerGoal(this));
/*  76 */     this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
/*  77 */     this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
/*     */     
/*  79 */     this.targetSelector.addGoal(1, new DefendVillageTargetGoal(this));
/*  80 */     this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
/*  81 */     this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::isAngryAt));
/*  82 */     this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, 5, false, false, (target, level) -> (target instanceof net.minecraft.world.entity.monster.Enemy && !(target instanceof net.minecraft.world.entity.monster.Creeper))));
/*  83 */     this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal(this, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  88 */     super.defineSynchedData(entityData);
/*  89 */     entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  93 */     return Mob.createMobAttributes()
/*  94 */       .add(Attributes.MAX_HEALTH, 100.0D)
/*  95 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/*  96 */       .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
/*  97 */       .add(Attributes.ATTACK_DAMAGE, 15.0D)
/*  98 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   protected int decreaseAirSupply(int currentSupply) { return currentSupply; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doPush(Entity entity) {
/* 109 */     if (entity instanceof net.minecraft.world.entity.monster.Enemy && !(entity instanceof net.minecraft.world.entity.monster.Creeper) && 
/* 110 */       getRandom().nextInt(20) == 0) {
/* 111 */       setTarget((LivingEntity)entity);
/*     */     }
/*     */     
/* 114 */     super.doPush(entity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 119 */     super.aiStep();
/*     */     
/* 121 */     if (this.attackAnimationTick > 0) {
/* 122 */       this.attackAnimationTick--;
/*     */     }
/* 124 */     if (this.offerFlowerTick > 0) {
/* 125 */       this.offerFlowerTick--;
/*     */     }
/*     */     
/* 128 */     if (!level().isClientSide()) {
/* 129 */       updatePersistentAnger((ServerLevel)level(), true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public boolean canSpawnSprintParticle() { return (getDeltaMovement().horizontalDistanceSqr() > 2.500000277905201E-7D && this.random.nextInt(5) == 0); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canAttackType(EntityType<?> targetType) {
/* 140 */     if (isPlayerCreated() && targetType == EntityType.PLAYER) {
/* 141 */       return false;
/*     */     }
/* 143 */     if (targetType == EntityType.CREEPER) {
/* 144 */       return false;
/*     */     }
/* 146 */     return super.canAttackType(targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 151 */     super.addAdditionalSaveData(output);
/* 152 */     output.putBoolean("PlayerCreated", isPlayerCreated());
/* 153 */     addPersistentAngerSaveData(output);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 158 */     super.readAdditionalSaveData(input);
/* 159 */     setPlayerCreated(input.getBooleanOr("PlayerCreated", false));
/* 160 */     readPersistentAngerSaveData(level(), input);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 165 */   public void startPersistentAngerTimer() { setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   public void setPersistentAngerEndTime(long endTime) { this.persistentAngerEndTime = endTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 175 */   public long getPersistentAngerEndTime() { return this.persistentAngerEndTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 180 */   public void setPersistentAngerTarget(EntityReference<LivingEntity> persistentAngerTarget) { this.persistentAngerTarget = persistentAngerTarget; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 185 */   public EntityReference<LivingEntity> getPersistentAngerTarget() { return this.persistentAngerTarget; }
/*     */ 
/*     */ 
/*     */   
/* 189 */   private float getAttackDamage() { return (float)getAttributeValue(Attributes.ATTACK_DAMAGE); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 194 */     this.attackAnimationTick = 10;
/* 195 */     level.broadcastEntityEvent(this, (byte)4);
/* 196 */     float attackDamage = getAttackDamage();
/* 197 */     float damage = ((int)attackDamage > 0) ? (attackDamage / 2.0F + this.random.nextInt((int)attackDamage)) : attackDamage;
/* 198 */     DamageSource damageSource = damageSources().mobAttack(this);
/* 199 */     boolean hurt = target.hurtServer(level, damageSource, damage);
/* 200 */     if (hurt) {
/* 201 */       LivingEntity livingEntity = (LivingEntity)target; double knockbackResistance = (target instanceof LivingEntity) ? livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) : 0.0D;
/* 202 */       double scale = Math.max(0.0D, 1.0D - knockbackResistance);
/*     */       
/* 204 */       target.setDeltaMovement(target.getDeltaMovement().add(0.0D, 0.4000000059604645D * scale, 0.0D));
/* 205 */       EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
/*     */     } 
/* 207 */     playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
/* 208 */     return hurt;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 213 */     Crackiness.Level previousCrackiness = getCrackiness();
/* 214 */     boolean wasHurt = super.hurtServer(level, source, damage);
/* 215 */     if (wasHurt && getCrackiness() != previousCrackiness) {
/* 216 */       playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
/*     */     }
/* 218 */     return wasHurt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 225 */   public Crackiness.Level getCrackiness() { return Crackiness.GOLEM.byFraction(getHealth() / getMaxHealth()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 230 */     if (id == 4) {
/* 231 */       this.attackAnimationTick = 10;
/* 232 */       playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
/* 233 */     } else if (id == 11) {
/* 234 */       this.offerFlowerTick = 400;
/* 235 */     } else if (id == 34) {
/* 236 */       this.offerFlowerTick = 0;
/*     */     } else {
/* 238 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 243 */   public int getAttackAnimationTick() { return this.attackAnimationTick; }
/*     */ 
/*     */   
/*     */   public void offerFlower(boolean offer) {
/* 247 */     if (offer) {
/* 248 */       this.offerFlowerTick = 400;
/* 249 */       level().broadcastEntityEvent(this, (byte)11);
/*     */     } else {
/* 251 */       this.offerFlowerTick = 0;
/* 252 */       level().broadcastEntityEvent(this, (byte)34);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 258 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.IRON_GOLEM_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 263 */   protected SoundEvent getDeathSound() { return SoundEvents.IRON_GOLEM_DEATH; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 268 */     ItemStack itemStack = player.getItemInHand(hand);
/* 269 */     if (!itemStack.is(Items.IRON_INGOT)) {
/* 270 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 273 */     float healthBefore = getHealth();
/* 274 */     heal(25.0F);
/* 275 */     if (getHealth() == healthBefore) {
/* 276 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 279 */     float pitch = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
/* 280 */     playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, pitch);
/*     */     
/* 282 */     itemStack.consume(1, player);
/* 283 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 288 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/* 292 */   public int getOfferFlowerTick() { return this.offerFlowerTick; }
/*     */ 
/*     */ 
/*     */   
/* 296 */   public boolean isPlayerCreated() { return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & true) != 0); }
/*     */ 
/*     */   
/*     */   public void setPlayerCreated(boolean value) {
/* 300 */     byte current = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 301 */     if (value) {
/* 302 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(current | true)));
/*     */     } else {
/* 304 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(current & 0xFFFFFFFE)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 311 */   public void die(DamageSource source) { super.die(source); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader level) {
/* 317 */     BlockPos pos = blockPosition();
/* 318 */     BlockPos belowPos = pos.below();
/* 319 */     BlockState below = level.getBlockState(belowPos);
/* 320 */     if (below.entityCanStandOn(level, belowPos, this)) {
/* 321 */       for (int i = 1; i < 3; i++) {
/* 322 */         BlockPos abovePos = pos.above(i);
/* 323 */         BlockState above = level.getBlockState(abovePos);
/* 324 */         if (!NaturalSpawner.isValidEmptySpawnBlock(level, abovePos, above, above.getFluidState(), EntityType.IRON_GOLEM)) {
/* 325 */           return false;
/*     */         }
/*     */       } 
/* 328 */       return (NaturalSpawner.isValidEmptySpawnBlock(level, pos, level.getBlockState(pos), Fluids.EMPTY.defaultFluidState(), EntityType.IRON_GOLEM) && level
/* 329 */         .isUnobstructed(this));
/*     */     } 
/* 331 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 336 */   public Vec3 getLeashOffset() { return new Vec3(0.0D, (0.875F * getEyeHeight()), (getBbWidth() * 0.4F)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\golem\IronGolem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
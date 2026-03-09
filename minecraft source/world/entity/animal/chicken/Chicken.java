/*     */ package net.minecraft.world.entity.animal.chicken;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.variant.SpawnContext;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.item.EitherHolder;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Chicken
/*     */   extends Animal
/*     */ {
/*  59 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.CHICKEN.getDimensions().scale(0.5F).withEyeHeight(0.2975F);
/*  60 */   private static final EntityDataAccessor<Holder<ChickenVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.CHICKEN_VARIANT);
/*     */   
/*     */   private static final boolean DEFAULT_CHICKEN_JOCKEY = false;
/*     */   public float flap;
/*     */   public float flapSpeed;
/*     */   public float oFlapSpeed;
/*     */   public float oFlap;
/*  67 */   public float flapping = 1.0F;
/*  68 */   private float nextFlap = 1.0F;
/*     */   public int eggTime;
/*     */   public boolean isChickenJockey = false;
/*     */   
/*     */   public Chicken(EntityType<? extends Chicken> type, Level level) {
/*  73 */     super(type, level);
/*     */     
/*  75 */     this.eggTime = this.random.nextInt(6000) + 6000;
/*     */     
/*  77 */     setPathfindingMalus(PathType.WATER, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  82 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/*  83 */     this.goalSelector.addGoal(1, new PanicGoal(this, 1.4D));
/*  84 */     this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
/*  85 */     this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, i -> i.is(ItemTags.CHICKEN_FOOD), false));
/*  86 */     this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
/*  87 */     this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
/*  88 */     this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 6.0F));
/*  89 */     this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  94 */   public EntityDimensions getDefaultDimensions(Pose pose) { return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose); }
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  98 */     return Animal.createAnimalAttributes()
/*  99 */       .add(Attributes.MAX_HEALTH, 4.0D)
/* 100 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 105 */     super.aiStep();
/*     */     
/* 107 */     this.oFlap = this.flap;
/* 108 */     this.oFlapSpeed = this.flapSpeed;
/*     */     
/* 110 */     this.flapSpeed += (onGround() ? -1.0F : 4.0F) * 0.3F;
/* 111 */     this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
/*     */     
/* 113 */     if (!onGround() && this.flapping < 1.0F) {
/* 114 */       this.flapping = 1.0F;
/*     */     }
/* 116 */     this.flapping *= 0.9F;
/*     */     
/* 118 */     Vec3 movement = getDeltaMovement();
/* 119 */     if (!onGround() && movement.y < 0.0D) {
/* 120 */       setDeltaMovement(movement.multiply(1.0D, 0.6D, 1.0D));
/*     */     }
/*     */     
/* 123 */     this.flap += this.flapping * 2.0F;
/*     */     
/* 125 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1; if (isAlive() && !isBaby() && !isChickenJockey() && --this.eggTime <= 0) {
/* 126 */         if (dropFromGiftLootTable(level, BuiltInLootTables.CHICKEN_LAY, this::spawnAtLocation)) {
/* 127 */           playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/* 128 */           gameEvent(GameEvent.ENTITY_PLACE);
/*     */         } 
/* 130 */         this.eggTime = this.random.nextInt(6000) + 6000;
/*     */       }  }
/*     */   
/*     */   }
/*     */ 
/*     */   
/* 136 */   protected boolean isFlapping() { return (this.flyDist > this.nextFlap); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   protected void onFlap() { this.nextFlap = this.flyDist + this.flapSpeed / 2.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   protected SoundEvent getAmbientSound() { return SoundEvents.CHICKEN_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.CHICKEN_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   protected SoundEvent getDeathSound() { return SoundEvents.CHICKEN_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Chicken getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 166 */     Chicken baby = (Chicken)EntityType.CHICKEN.create(level, EntitySpawnReason.BREEDING);
/* 167 */     if (baby != null && partner instanceof Chicken) { Chicken partnerChicken = (Chicken)partner;
/* 168 */       baby.setVariant(this.random.nextBoolean() ? getVariant() : partnerChicken.getVariant()); }
/*     */     
/* 170 */     return baby;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 175 */     VariantUtils.selectVariantToSpawn(SpawnContext.create(level, blockPosition()), Registries.CHICKEN_VARIANT).ifPresent(this::setVariant);
/* 176 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 181 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.CHICKEN_FOOD); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getBaseExperienceReward(ServerLevel level) {
/* 186 */     if (isChickenJockey()) {
/* 187 */       return 10;
/*     */     }
/* 189 */     return super.getBaseExperienceReward(level);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 194 */     super.defineSynchedData(entityData);
/* 195 */     entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), ChickenVariants.TEMPERATE));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 200 */     super.readAdditionalSaveData(input);
/* 201 */     this.isChickenJockey = input.getBooleanOr("IsChickenJockey", false);
/*     */     
/* 203 */     input.getInt("EggLayTime").ifPresent(time -> this.eggTime = time.intValue());
/* 204 */     VariantUtils.readVariant(input, Registries.CHICKEN_VARIANT).ifPresent(this::setVariant);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 209 */     super.addAdditionalSaveData(output);
/* 210 */     output.putBoolean("IsChickenJockey", this.isChickenJockey);
/* 211 */     output.putInt("EggLayTime", this.eggTime);
/* 212 */     VariantUtils.writeVariant(output, getVariant());
/*     */   }
/*     */ 
/*     */   
/* 216 */   public void setVariant(Holder<ChickenVariant> variant) { this.entityData.set(DATA_VARIANT_ID, variant); }
/*     */ 
/*     */ 
/*     */   
/* 220 */   public Holder<ChickenVariant> getVariant() { return (Holder)this.entityData.get(DATA_VARIANT_ID); }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 225 */     if (type == DataComponents.CHICKEN_VARIANT) {
/* 226 */       return (T)castComponentValue(type, new EitherHolder(getVariant()));
/*     */     }
/*     */     
/* 229 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 234 */     applyImplicitComponentIfPresent(components, DataComponents.CHICKEN_VARIANT);
/* 235 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 240 */     if (type == DataComponents.CHICKEN_VARIANT) {
/* 241 */       Optional<Holder<ChickenVariant>> variant = ((EitherHolder)castComponentValue(DataComponents.CHICKEN_VARIANT, value)).unwrap(registryAccess());
/* 242 */       if (variant.isPresent()) {
/* 243 */         setVariant((Holder)variant.get());
/* 244 */         return true;
/*     */       } 
/* 246 */       return false;
/*     */     } 
/*     */     
/* 249 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 254 */   public boolean removeWhenFarAway(double distSqr) { return isChickenJockey(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
/* 259 */     super.positionRider(passenger, moveFunction);
/* 260 */     if (passenger instanceof LivingEntity) {
/* 261 */       ((LivingEntity)passenger).yBodyRot = this.yBodyRot;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 266 */   public boolean isChickenJockey() { return this.isChickenJockey; }
/*     */ 
/*     */ 
/*     */   
/* 270 */   public void setChickenJockey(boolean isChickenJockey) { this.isChickenJockey = isChickenJockey; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\chicken\Chicken.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
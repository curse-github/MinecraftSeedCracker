/*     */ package net.minecraft.world.entity.animal.feline;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Ocelot
/*     */   extends Animal
/*     */ {
/*     */   public static final double CROUCH_SPEED_MOD = 0.6D;
/*     */   public static final double WALK_SPEED_MOD = 0.8D;
/*     */   public static final double SPRINT_SPEED_MOD = 1.33D;
/*  62 */   private static final EntityDataAccessor<Boolean> DATA_TRUSTING = SynchedEntityData.defineId(Ocelot.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final boolean DEFAULT_TRUSTING = false;
/*     */   private OcelotAvoidEntityGoal<Player> ocelotAvoidPlayersGoal;
/*     */   private OcelotTemptGoal temptGoal;
/*     */   
/*     */   public Ocelot(EntityType<? extends Ocelot> type, Level level) {
/*  69 */     super(type, level);
/*     */     
/*  71 */     reassessTrustingGoals();
/*     */   }
/*     */ 
/*     */   
/*  75 */   private boolean isTrusting() { return ((Boolean)this.entityData.get(DATA_TRUSTING)).booleanValue(); }
/*     */ 
/*     */   
/*     */   private void setTrusting(boolean trusting) {
/*  79 */     this.entityData.set(DATA_TRUSTING, Boolean.valueOf(trusting));
/*     */     
/*  81 */     reassessTrustingGoals();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  86 */     super.addAdditionalSaveData(output);
/*     */     
/*  88 */     output.putBoolean("Trusting", isTrusting());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  93 */     super.readAdditionalSaveData(input);
/*     */     
/*  95 */     setTrusting(input.getBooleanOr("Trusting", false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 100 */     super.defineSynchedData(entityData);
/*     */     
/* 102 */     entityData.define(DATA_TRUSTING, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 107 */     this.temptGoal = new OcelotTemptGoal(this, 0.6D, i -> i.is(ItemTags.OCELOT_FOOD), true);
/* 108 */     this.goalSelector.addGoal(1, new FloatGoal(this));
/* 109 */     this.goalSelector.addGoal(3, this.temptGoal);
/* 110 */     this.goalSelector.addGoal(7, new LeapAtTargetGoal(this, 0.3F));
/* 111 */     this.goalSelector.addGoal(8, new OcelotAttackGoal(this));
/* 112 */     this.goalSelector.addGoal(9, new BreedGoal(this, 0.8D));
/* 113 */     this.goalSelector.addGoal(10, new WaterAvoidingRandomStrollGoal(this, 0.8D, 1.0000001E-5F));
/* 114 */     this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0F));
/*     */     
/* 116 */     this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.animal.chicken.Chicken.class, false));
/* 117 */     this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Turtle.class, 10, false, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */ 
/*     */   
/*     */   public void customServerAiStep(ServerLevel level) {
/* 122 */     if (getMoveControl().hasWanted()) {
/* 123 */       double speed = getMoveControl().getSpeedModifier();
/* 124 */       if (speed == 0.6D) {
/* 125 */         setPose(Pose.CROUCHING);
/* 126 */         setSprinting(false);
/* 127 */       } else if (speed == 1.33D) {
/* 128 */         setPose(Pose.STANDING);
/* 129 */         setSprinting(true);
/*     */       } else {
/* 131 */         setPose(Pose.STANDING);
/* 132 */         setSprinting(false);
/*     */       } 
/*     */     } else {
/* 135 */       setPose(Pose.STANDING);
/* 136 */       setSprinting(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 142 */   public boolean removeWhenFarAway(double distSqr) { return (!isTrusting() && this.tickCount > 2400); }
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 146 */     return Animal.createAnimalAttributes()
/* 147 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 148 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 149 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 154 */   protected SoundEvent getAmbientSound() { return SoundEvents.OCELOT_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   public int getAmbientSoundInterval() { return 900; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.OCELOT_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   protected SoundEvent getDeathSound() { return SoundEvents.OCELOT_DEATH; }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 174 */     ItemStack itemStack = player.getItemInHand(hand);
/* 175 */     if ((this.temptGoal == null || this.temptGoal.isRunning()) && !isTrusting() && isFood(itemStack) && player.distanceToSqr(this) < 9.0D) {
/* 176 */       usePlayerItem(player, hand, itemStack);
/*     */       
/* 178 */       if (!level().isClientSide()) {
/* 179 */         if (this.random.nextInt(3) == 0) {
/* 180 */           setTrusting(true);
/* 181 */           spawnTrustingParticles(true);
/* 182 */           level().broadcastEntityEvent(this, (byte)41);
/*     */         } else {
/* 184 */           spawnTrustingParticles(false);
/* 185 */           level().broadcastEntityEvent(this, (byte)40);
/*     */         } 
/*     */       }
/*     */       
/* 189 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 192 */     return super.mobInteract(player, hand);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 197 */     if (id == 41) {
/* 198 */       spawnTrustingParticles(true);
/* 199 */     } else if (id == 40) {
/* 200 */       spawnTrustingParticles(false);
/*     */     } else {
/* 202 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnTrustingParticles(boolean success) {
/* 207 */     SimpleParticleType simpleParticleType = ParticleTypes.HEART;
/* 208 */     if (!success) {
/* 209 */       simpleParticleType = ParticleTypes.SMOKE;
/*     */     }
/* 211 */     for (int i = 0; i < 7; i++) {
/* 212 */       double xa = this.random.nextGaussian() * 0.02D;
/* 213 */       double ya = this.random.nextGaussian() * 0.02D;
/* 214 */       double za = this.random.nextGaussian() * 0.02D;
/* 215 */       level().addParticle(simpleParticleType, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), xa, ya, za);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void reassessTrustingGoals() {
/* 220 */     if (this.ocelotAvoidPlayersGoal == null) {
/* 221 */       this.ocelotAvoidPlayersGoal = new OcelotAvoidEntityGoal(this, Player.class, 16.0F, 0.8D, 1.33D);
/*     */     }
/*     */     
/* 224 */     this.goalSelector.removeGoal(this.ocelotAvoidPlayersGoal);
/*     */     
/* 226 */     if (!isTrusting()) {
/* 227 */       this.goalSelector.addGoal(4, this.ocelotAvoidPlayersGoal);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 233 */   public Ocelot getBreedOffspring(ServerLevel level, AgeableMob partner) { return (Ocelot)EntityType.OCELOT.create(level, EntitySpawnReason.BREEDING); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 238 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.OCELOT_FOOD); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 244 */   public static boolean checkOcelotSpawnRules(EntityType<Ocelot> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) { return (random.nextInt(3) != 0); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader level) {
/* 249 */     if (level.isUnobstructed(this) && !level.containsAnyLiquid(getBoundingBox())) {
/* 250 */       BlockPos pos = blockPosition();
/* 251 */       if (pos.getY() < level.getSeaLevel()) {
/* 252 */         return false;
/*     */       }
/*     */       
/* 255 */       BlockState state = level.getBlockState(pos.below());
/* 256 */       if (state.is(Blocks.GRASS_BLOCK) || state.is(BlockTags.LEAVES)) {
/* 257 */         return true;
/*     */       }
/*     */     } 
/* 260 */     return false;
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 265 */     if (groupData == null)
/*     */     {
/* 267 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(1.0F);
/*     */     }
/*     */     
/* 270 */     return super.finalizeSpawn(level, difficulty, spawnReason, ageableMobGroupData);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 275 */   public Vec3 getLeashOffset() { return new Vec3(0.0D, (0.5F * getEyeHeight()), (getBbWidth() * 0.4F)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 280 */   public boolean isSteppingCarefully() { return (isCrouching() || super.isSteppingCarefully()); }
/*     */   
/*     */   private static class OcelotAvoidEntityGoal<T extends LivingEntity>
/*     */     extends AvoidEntityGoal<T> {
/*     */     private final Ocelot ocelot;
/*     */     
/*     */     public OcelotAvoidEntityGoal(Ocelot ocelot, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
/* 287 */       super(ocelot, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
/* 288 */       this.ocelot = ocelot;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 293 */     public boolean canUse() { return (!this.ocelot.isTrusting() && super.canUse()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 298 */     public boolean canContinueToUse() { return (!this.ocelot.isTrusting() && super.canContinueToUse()); }
/*     */   }
/*     */   
/*     */   private static class OcelotTemptGoal
/*     */     extends TemptGoal {
/*     */     private final Ocelot ocelot;
/*     */     
/*     */     public OcelotTemptGoal(Ocelot ocelot, double speedModifier, Predicate<ItemStack> items, boolean canScare) {
/* 306 */       super(ocelot, speedModifier, items, canScare);
/* 307 */       this.ocelot = ocelot;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 312 */     protected boolean canScare() { return (super.canScare() && !this.ocelot.isTrusting()); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\feline\Ocelot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.SpearUseGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class ZombifiedPiglin
/*     */   extends Zombie implements NeutralMob {
/*  47 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.ZOMBIFIED_PIGLIN.getDimensions().scale(0.5F).withEyeHeight(0.97F);
/*     */   
/*  49 */   private static final Identifier SPEED_MODIFIER_ATTACKING_ID = Identifier.withDefaultNamespace("attacking");
/*  50 */   private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_ID, 0.05D, AttributeModifier.Operation.ADD_VALUE);
/*     */   
/*  52 */   private static final UniformInt FIRST_ANGER_SOUND_DELAY = TimeUtil.rangeOfSeconds(0, 1);
/*     */   
/*     */   private int playFirstAngerSoundIn;
/*  55 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   
/*     */   private long persistentAngerEndTime;
/*     */   private EntityReference<LivingEntity> persistentAngerTarget;
/*     */   private static final int ALERT_RANGE_Y = 10;
/*  60 */   private static final UniformInt ALERT_INTERVAL = TimeUtil.rangeOfSeconds(4, 6);
/*     */   private int ticksUntilNextAlert;
/*     */   
/*     */   public ZombifiedPiglin(EntityType<? extends ZombifiedPiglin> type, Level level) {
/*  64 */     super(type, level);
/*  65 */     setPathfindingMalus(PathType.LAVA, 8.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addBehaviourGoals() {
/*  70 */     this.goalSelector.addGoal(1, new SpearUseGoal(this, 1.0D, 1.0D, 10.0F, 2.0F));
/*  71 */     this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0D, false));
/*  72 */     this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
/*     */     
/*  74 */     this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
/*  75 */     this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::isAngryAt));
/*  76 */     this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal(this, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  80 */     return Zombie.createAttributes()
/*  81 */       .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0D)
/*  82 */       .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
/*  83 */       .add(Attributes.ATTACK_DAMAGE, 5.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public EntityDimensions getDefaultDimensions(Pose pose) { return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   protected boolean convertsInWater() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/*  98 */     AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
/*  99 */     if (isAngry()) {
/* 100 */       if (!isBaby() && !speed.hasModifier(SPEED_MODIFIER_ATTACKING_ID)) {
/* 101 */         speed.addTransientModifier(SPEED_MODIFIER_ATTACKING);
/*     */       }
/* 103 */       maybePlayFirstAngerSound();
/* 104 */     } else if (speed.hasModifier(SPEED_MODIFIER_ATTACKING_ID)) {
/* 105 */       speed.removeModifier(SPEED_MODIFIER_ATTACKING_ID);
/*     */     } 
/*     */     
/* 108 */     updatePersistentAnger(level, true);
/* 109 */     if (getTarget() != null) {
/* 110 */       maybeAlertOthers();
/*     */     }
/*     */     
/* 113 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   private void maybePlayFirstAngerSound() {
/* 117 */     if (this.playFirstAngerSoundIn > 0) {
/* 118 */       this.playFirstAngerSoundIn--;
/* 119 */       if (this.playFirstAngerSoundIn == 0) {
/* 120 */         playAngerSound();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeAlertOthers() {
/* 130 */     if (this.ticksUntilNextAlert > 0) {
/* 131 */       this.ticksUntilNextAlert--;
/*     */       return;
/*     */     } 
/* 134 */     if (getSensing().hasLineOfSight(getTarget())) {
/* 135 */       alertOthers();
/*     */     }
/* 137 */     this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.random);
/*     */   }
/*     */   
/*     */   private void alertOthers() {
/* 141 */     double within = getAttributeValue(Attributes.FOLLOW_RANGE);
/* 142 */     AABB searchAabb = AABB.unitCubeFromLowerCorner(position()).inflate(within, 10.0D, within);
/* 143 */     level().getEntitiesOfClass(ZombifiedPiglin.class, searchAabb, EntitySelector.NO_SPECTATORS).stream()
/* 144 */       .filter(other -> (other != this))
/* 145 */       .filter(other -> (other.getTarget() == null))
/* 146 */       .filter(other -> !other.isAlliedTo(getTarget()))
/* 147 */       .forEach(other -> other.setTarget(getTarget()));
/*     */   }
/*     */ 
/*     */   
/* 151 */   private void playAngerSound() { playSound(SoundEvents.ZOMBIFIED_PIGLIN_ANGRY, getSoundVolume() * 2.0F, getVoicePitch() * 1.8F); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(LivingEntity target) {
/* 156 */     if (getTarget() == null && target != null) {
/*     */ 
/*     */       
/* 159 */       this.playFirstAngerSoundIn = FIRST_ANGER_SOUND_DELAY.sample(this.random);
/* 160 */       this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.random);
/*     */     } 
/* 162 */     super.setTarget(target);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 167 */   public void startPersistentAngerTimer() { setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random)); }
/*     */ 
/*     */ 
/*     */   
/* 171 */   public static boolean checkZombifiedPiglinSpawnRules(EntityType<ZombifiedPiglin> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) { return (level.getDifficulty() != Difficulty.PEACEFUL && !level.getBlockState(pos.below()).is(Blocks.NETHER_WART_BLOCK)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   public boolean checkSpawnObstruction(LevelReader level) { return (level.isUnobstructed(this) && !level.containsAnyLiquid(getBoundingBox())); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 181 */     super.addAdditionalSaveData(output);
/* 182 */     addPersistentAngerSaveData(output);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 187 */     super.readAdditionalSaveData(input);
/* 188 */     readPersistentAngerSaveData(level(), input);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 193 */   public void setPersistentAngerEndTime(long endTime) { this.persistentAngerEndTime = endTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 198 */   public long getPersistentAngerEndTime() { return this.persistentAngerEndTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   public void setPersistentAngerTarget(EntityReference<LivingEntity> persistentAngerTarget) { this.persistentAngerTarget = persistentAngerTarget; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 208 */   protected SoundEvent getAmbientSound() { return isAngry() ? SoundEvents.ZOMBIFIED_PIGLIN_ANGRY : SoundEvents.ZOMBIFIED_PIGLIN_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 213 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ZOMBIFIED_PIGLIN_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 218 */   protected SoundEvent getDeathSound() { return SoundEvents.ZOMBIFIED_PIGLIN_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 223 */   public void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) { setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((random.nextInt(20) == 0) ? Items.GOLDEN_SPEAR : Items.GOLDEN_SWORD)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 228 */   protected void randomizeReinforcementsChance() { getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0D); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 233 */   public EntityReference<LivingEntity> getPersistentAngerTarget() { return this.persistentAngerTarget; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 238 */   public boolean isPreventingPlayerRest(ServerLevel level, Player player) { return isAngryAt(player, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 243 */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) { return canHoldItem(itemStack); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\zombie\ZombifiedPiglin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
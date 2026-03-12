/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.function.DoubleSupplier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ZombieHorse
/*     */   extends AbstractHorse
/*     */ {
/*     */   private static final float SPEED_FACTOR = 42.16F;
/*     */   private static final double BASE_JUMP_STRENGTH = 0.5D;
/*     */   private static final double PER_RANDOM_JUMP_STRENGTH = 0.06666666666666667D;
/*     */   private static final double BASE_SPEED = 9.0D;
/*     */   private static final double PER_RANDOM_SPEED = 1.0D;
/*  47 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.ZOMBIE_HORSE.getDimensions()
/*  48 */     .withAttachments(EntityAttachments.builder()
/*  49 */       .attach(EntityAttachment.PASSENGER, 0.0F, EntityType.ZOMBIE_HORSE.getHeight() - 0.03125F, 0.0F))
/*     */     
/*  51 */     .scale(0.5F);
/*     */   
/*     */   public ZombieHorse(EntityType<? extends ZombieHorse> type, Level level) {
/*  54 */     super(type, level);
/*  55 */     setPathfindingMalus(PathType.DANGER_OTHER, -1.0F);
/*  56 */     setPathfindingMalus(PathType.DAMAGE_OTHER, -1.0F);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  60 */     return createBaseHorseAttributes()
/*  61 */       .add(Attributes.MAX_HEALTH, 25.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand) {
/*  66 */     setPersistenceRequired();
/*  67 */     return super.interact(player, hand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public boolean removeWhenFarAway(double distSqr) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public boolean isMobControlled() { return getFirstPassenger() instanceof net.minecraft.world.entity.Mob; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void randomizeAttributes(RandomSource random) {
/*  82 */     Objects.requireNonNull(random); getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(generateZombieHorseJumpStrength(random::nextDouble));
/*  83 */     Objects.requireNonNull(random); getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(generateZombieHorseSpeed(random::nextDouble));
/*     */   }
/*     */ 
/*     */   
/*  87 */   private static double generateZombieHorseJumpStrength(DoubleSupplier probabilityProvider) { return 0.5D + probabilityProvider.getAsDouble() * 0.06666666666666667D + probabilityProvider.getAsDouble() * 0.06666666666666667D + probabilityProvider.getAsDouble() * 0.06666666666666667D; }
/*     */ 
/*     */ 
/*     */   
/*  91 */   private static double generateZombieHorseSpeed(DoubleSupplier probabilityProvider) { return (9.0D + probabilityProvider.getAsDouble() * 1.0D + probabilityProvider.getAsDouble() * 1.0D + probabilityProvider.getAsDouble() * 1.0D) / 42.15999984741211D; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   protected SoundEvent getAmbientSound() { return SoundEvents.ZOMBIE_HORSE_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   protected SoundEvent getDeathSound() { return SoundEvents.ZOMBIE_HORSE_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ZOMBIE_HORSE_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   protected SoundEvent getAngrySound() { return SoundEvents.ZOMBIE_HORSE_ANGRY; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   protected SoundEvent getEatingSound() { return SoundEvents.ZOMBIE_HORSE_EAT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   public boolean canFallInLove() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addBehaviourGoals() {
/* 131 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/* 132 */     this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, i -> i.is(ItemTags.ZOMBIE_HORSE_FOOD), false));
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 137 */     if (spawnReason == EntitySpawnReason.NATURAL) {
/* 138 */       Zombie zombie = (Zombie)EntityType.ZOMBIE.create(level(), EntitySpawnReason.JOCKEY);
/* 139 */       if (zombie != null) {
/* 140 */         zombie.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 141 */         zombie.finalizeSpawn(level, difficulty, spawnReason, null);
/* 142 */         zombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SPEAR));
/* 143 */         zombie.startRiding(this, false, false);
/*     */       } 
/*     */     } 
/* 146 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 151 */     boolean shouldOpenInventory = (!isBaby() && isTamed() && player.isSecondaryUseActive());
/* 152 */     if (isVehicle() || shouldOpenInventory) {
/* 153 */       return super.mobInteract(player, hand);
/*     */     }
/*     */     
/* 156 */     ItemStack itemStack = player.getItemInHand(hand);
/*     */     
/* 158 */     if (!itemStack.isEmpty()) {
/* 159 */       if (isFood(itemStack)) {
/* 160 */         return fedFood(player, itemStack);
/*     */       }
/*     */       
/* 163 */       if (!isTamed()) {
/* 164 */         makeMad();
/* 165 */         return InteractionResult.SUCCESS;
/*     */       } 
/*     */     } 
/* 168 */     return super.mobInteract(player, hand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 173 */   public boolean canUseSlot(EquipmentSlot slot) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   public boolean canBeLeashed() { return (isTamed() || !isMobControlled()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.ZOMBIE_HORSE_FOOD); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 188 */   protected EquipmentSlot sunProtectionSlot() { return EquipmentSlot.BODY; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 193 */   public Vec3[] getQuadLeashOffsets() { return Leashable.createQuadLeashOffsets(this, 0.04D, 0.41D, 0.18D, 0.73D); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 198 */   public EntityDimensions getDefaultDimensions(Pose pose) { return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   public float chargeSpeedModifier() { return 1.4F; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\equine\ZombieHorse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
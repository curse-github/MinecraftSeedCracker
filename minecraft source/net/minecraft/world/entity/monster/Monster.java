/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.ProjectileWeaponItem;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public abstract class Monster extends PathfinderMob implements Enemy {
/*     */   protected Monster(EntityType<? extends Monster> type, Level level) {
/*  33 */     super(type, level);
/*  34 */     this.xpReward = 5;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  39 */   public SoundSource getSoundSource() { return SoundSource.HOSTILE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  44 */     updateSwingTime();
/*  45 */     updateNoActionTime();
/*  46 */     super.aiStep();
/*     */   }
/*     */   
/*     */   protected void updateNoActionTime() {
/*  50 */     float br = getLightLevelDependentMagicValue();
/*  51 */     if (br > 0.5F) {
/*  52 */       this.noActionTime += 2;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  58 */   protected SoundEvent getSwimSound() { return SoundEvents.HOSTILE_SWIM; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   protected SoundEvent getSwimSplashSound() { return SoundEvents.HOSTILE_SPLASH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.HOSTILE_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   protected SoundEvent getDeathSound() { return SoundEvents.HOSTILE_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public LivingEntity.Fallsounds getFallSounds() { return new LivingEntity.Fallsounds(SoundEvents.HOSTILE_SMALL_FALL, SoundEvents.HOSTILE_BIG_FALL); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public float getWalkTargetValue(BlockPos pos, LevelReader level) { return -level.getPathfindingCostFromLightLevels(pos); }
/*     */ 
/*     */   
/*     */   public static boolean isDarkEnoughToSpawn(ServerLevelAccessor level, BlockPos pos, RandomSource random) {
/*  87 */     if (level.getBrightness(LightLayer.SKY, pos) > random.nextInt(32)) {
/*  88 */       return false;
/*     */     }
/*     */     
/*  91 */     DimensionType dimensionType = level.dimensionType();
/*  92 */     int blockLightLimit = dimensionType.monsterSpawnBlockLightLimit();
/*  93 */     if (blockLightLimit < 15 && level.getBrightness(LightLayer.BLOCK, pos) > blockLightLimit) {
/*  94 */       return false;
/*     */     }
/*     */     
/*  97 */     int brightness = level.getLevel().isThundering() ? level.getMaxLocalRawBrightness(pos, 10) : level.getMaxLocalRawBrightness(pos);
/*  98 */     return (brightness <= dimensionType.monsterSpawnLightTest().sample(random));
/*     */   }
/*     */   
/*     */   public static boolean checkMonsterSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 102 */     return (level.getDifficulty() != Difficulty.PEACEFUL && (
/* 103 */       EntitySpawnReason.ignoresLightRequirements(spawnReason) || isDarkEnoughToSpawn(level, pos, random)) && 
/* 104 */       checkMobSpawnRules(type, level, spawnReason, pos, random));
/*     */   }
/*     */   
/*     */   public static boolean checkAnyLightMonsterSpawnRules(EntityType<? extends Monster> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 108 */     return (level.getDifficulty() != Difficulty.PEACEFUL && 
/* 109 */       checkMobSpawnRules(type, level, spawnReason, pos, random));
/*     */   }
/*     */   
/*     */   public static boolean checkSurfaceMonstersSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 113 */     return (checkMonsterSpawnRules(type, level, spawnReason, pos, random) && (
/* 114 */       EntitySpawnReason.isSpawner(spawnReason) || level.canSeeSky(pos)));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createMonsterAttributes() {
/* 118 */     return Mob.createMobAttributes()
/* 119 */       .add(Attributes.ATTACK_DAMAGE);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public boolean shouldDropExperience() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   protected boolean shouldDropLoot(ServerLevel level) { return ((Boolean)level.getGameRules().get(GameRules.MOB_DROPS)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 133 */   public boolean isPreventingPlayerRest(ServerLevel level, Player player) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getProjectile(ItemStack heldWeapon) {
/* 138 */     if (heldWeapon.getItem() instanceof ProjectileWeaponItem) {
/* 139 */       Predicate<ItemStack> supportedProjectiles = ((ProjectileWeaponItem)heldWeapon.getItem()).getSupportedHeldProjectiles();
/* 140 */       ItemStack heldProjectile = ProjectileWeaponItem.getHeldProjectile(this, supportedProjectiles);
/* 141 */       return heldProjectile.isEmpty() ? new ItemStack(Items.ARROW) : heldProjectile;
/*     */     } 
/* 143 */     return ItemStack.EMPTY;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Monster.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
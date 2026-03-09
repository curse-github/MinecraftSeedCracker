/*     */ package net.minecraft.world.entity.monster.skeleton;
/*     */ 
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ 
/*     */ 
/*     */ public class WitherSkeleton
/*     */   extends AbstractSkeleton
/*     */ {
/*     */   public WitherSkeleton(EntityType<? extends WitherSkeleton> type, Level level) {
/*  34 */     super(type, level);
/*     */     
/*  36 */     setPathfindingMalus(PathType.LAVA, 8.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  41 */     this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.monster.piglin.AbstractPiglin.class, true));
/*  42 */     super.registerGoals();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  47 */   protected SoundEvent getAmbientSound() { return SoundEvents.WITHER_SKELETON_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.WITHER_SKELETON_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   protected SoundEvent getDeathSound() { return SoundEvents.WITHER_SKELETON_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   SoundEvent getStepSound() { return SoundEvents.WITHER_SKELETON_STEP; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   public TagKey<Item> getPreferredWeaponType() { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public boolean canHoldItem(ItemStack itemStack) { return (!itemStack.is(ItemTags.WITHER_SKELETON_DISLIKED_WEAPONS) && super.canHoldItem(itemStack)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) { setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentEnchantments(ServerLevelAccessor level, RandomSource random, DifficultyInstance localDifficulty) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*  86 */     SpawnGroupData spawnGroupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */     
/*  88 */     getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0D);
/*     */     
/*  90 */     reassessWeaponGoal();
/*     */     
/*  92 */     return spawnGroupData;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/*  97 */     if (!super.doHurtTarget(level, target)) {
/*  98 */       return false;
/*     */     }
/*     */     
/* 101 */     if (target instanceof LivingEntity) {
/* 102 */       ((LivingEntity)target).addEffect(new MobEffectInstance(MobEffects.WITHER, 200), this);
/*     */     }
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractArrow getArrow(ItemStack projectile, float power, ItemStack firingWeapon) {
/* 109 */     AbstractArrow arrow = super.getArrow(projectile, power, firingWeapon);
/* 110 */     arrow.igniteForSeconds(100.0F);
/* 111 */     return arrow;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeAffected(MobEffectInstance newEffect) {
/* 116 */     if (newEffect.is(MobEffects.WITHER)) {
/* 117 */       return false;
/*     */     }
/* 119 */     return super.canBeAffected(newEffect);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\skeleton\WitherSkeleton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
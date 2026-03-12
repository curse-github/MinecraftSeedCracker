/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
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
/*     */ import net.minecraft.world.entity.animal.camel.CamelHusk;
/*     */ import net.minecraft.world.entity.monster.skeleton.Parched;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Husk
/*     */   extends Zombie
/*     */ {
/*  30 */   public Husk(EntityType<? extends Husk> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  35 */   protected boolean isSunSensitive() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   protected SoundEvent getAmbientSound() { return SoundEvents.HUSK_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.HUSK_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   protected SoundEvent getDeathSound() { return SoundEvents.HUSK_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   protected SoundEvent getStepSound() { return SoundEvents.HUSK_STEP; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/*  60 */     boolean result = super.doHurtTarget(level, target);
/*  61 */     if (result && getMainHandItem().isEmpty() && target instanceof LivingEntity) {
/*  62 */       float difficulty = level.getCurrentDifficultyAt(blockPosition()).getEffectiveDifficulty();
/*  63 */       ((LivingEntity)target).addEffect(new MobEffectInstance(MobEffects.HUNGER, 140 * (int)difficulty), this);
/*     */     } 
/*     */     
/*  66 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  71 */   protected boolean convertsInWater() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doUnderWaterConversion(ServerLevel level) {
/*  76 */     convertToZombieType(level, EntityType.ZOMBIE);
/*  77 */     if (!isSilent()) {
/*  78 */       level.levelEvent(null, 1041, blockPosition(), 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*  84 */     RandomSource random = level.getRandom();
/*     */     
/*  86 */     groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*  87 */     float difficultyModifier = difficulty.getSpecialMultiplier();
/*     */     
/*  89 */     if (spawnReason != EntitySpawnReason.CONVERSION) {
/*  90 */       setCanPickUpLoot((random.nextFloat() < 0.55F * difficultyModifier));
/*     */     }
/*     */     
/*  93 */     if (groupData != null) {
/*  94 */       groupData = new HuskGroupData((Zombie.ZombieGroupData)groupData);
/*  95 */       ((HuskGroupData)groupData).triedToSpawnCamelHusk = (spawnReason != EntitySpawnReason.NATURAL);
/*     */     } 
/*  97 */     if (groupData instanceof HuskGroupData) { HuskGroupData huskGroupData = (HuskGroupData)groupData;
/*  98 */       if (!huskGroupData.triedToSpawnCamelHusk) {
/*     */         
/* 100 */         BlockPos pos = blockPosition();
/* 101 */         if (level.noCollision(EntityType.CAMEL_HUSK.getSpawnAABB(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D))) {
/*     */           
/* 103 */           huskGroupData.triedToSpawnCamelHusk = true;
/* 104 */           if (random.nextFloat() < 0.1F) {
/*     */             
/* 106 */             setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SPEAR));
/*     */             
/* 108 */             CamelHusk camelHusk = (CamelHusk)EntityType.CAMEL_HUSK.create(level(), EntitySpawnReason.NATURAL);
/* 109 */             if (camelHusk != null) {
/* 110 */               camelHusk.setPos(getX(), getY(), getZ());
/* 111 */               camelHusk.finalizeSpawn(level, difficulty, spawnReason, null);
/* 112 */               startRiding(camelHusk, true, true);
/* 113 */               level.addFreshEntity(camelHusk);
/*     */               
/* 115 */               Parched parched = (Parched)EntityType.PARCHED.create(level(), EntitySpawnReason.NATURAL);
/* 116 */               if (parched != null) {
/* 117 */                 parched.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 118 */                 parched.finalizeSpawn(level, difficulty, spawnReason, null);
/* 119 */                 parched.startRiding(camelHusk, false, false);
/* 120 */                 level.addFreshEntityWithPassengers(parched);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }  }
/*     */     
/* 127 */     return groupData;
/*     */   }
/*     */   
/*     */   public static class HuskGroupData
/*     */     extends Zombie.ZombieGroupData {
/*     */     public boolean triedToSpawnCamelHusk = false;
/*     */     
/* 134 */     public HuskGroupData(Zombie.ZombieGroupData groupData) { super(groupData.isBaby, groupData.canSpawnJockey); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\zombie\Husk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
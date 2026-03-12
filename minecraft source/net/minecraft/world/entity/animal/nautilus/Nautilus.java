/*     */ package net.minecraft.world.entity.animal.nautilus;
/*     */ 
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ 
/*     */ public class Nautilus
/*     */   extends AbstractNautilus
/*     */ {
/*     */   private static final int NAUTILUS_TOTAL_AIR_SUPPLY = 300;
/*     */   
/*  22 */   public Nautilus(EntityType<? extends Nautilus> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  27 */   protected Brain.Provider<Nautilus> brainProvider() { return NautilusAi.brainProvider(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  32 */   protected Brain<?> makeBrain(Dynamic<?> input) { return NautilusAi.makeBrain(brainProvider().makeBrain(input)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   public Brain<Nautilus> getBrain() { return super.getBrain(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Nautilus getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  43 */     Nautilus baby = (Nautilus)EntityType.NAUTILUS.create(level, EntitySpawnReason.BREEDING);
/*  44 */     if (baby != null && 
/*  45 */       isTame()) {
/*  46 */       baby.setOwnerReference(getOwnerReference());
/*  47 */       baby.setTame(true, true);
/*     */     } 
/*     */     
/*  50 */     return baby;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/*  55 */     ProfilerFiller profiler = Profiler.get();
/*  56 */     profiler.push("nautilusBrain");
/*  57 */     getBrain().tick(level, this);
/*  58 */     profiler.pop();
/*     */     
/*  60 */     profiler.push("nautilusActivityUpdate");
/*  61 */     NautilusAi.updateActivity(this);
/*  62 */     profiler.pop();
/*  63 */     super.customServerAiStep(level);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  68 */     if (isBaby()) {
/*  69 */       return isUnderWater() ? SoundEvents.BABY_NAUTILUS_AMBIENT : SoundEvents.BABY_NAUTILUS_AMBIENT_ON_LAND;
/*     */     }
/*  71 */     return isUnderWater() ? SoundEvents.NAUTILUS_AMBIENT : SoundEvents.NAUTILUS_AMBIENT_ON_LAND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/*  76 */     if (isBaby()) {
/*  77 */       return isUnderWater() ? SoundEvents.BABY_NAUTILUS_HURT : SoundEvents.BABY_NAUTILUS_HURT_ON_LAND;
/*     */     }
/*  79 */     return isUnderWater() ? SoundEvents.NAUTILUS_HURT : SoundEvents.NAUTILUS_HURT_ON_LAND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  84 */     if (isBaby()) {
/*  85 */       return isUnderWater() ? SoundEvents.BABY_NAUTILUS_DEATH : SoundEvents.BABY_NAUTILUS_DEATH_ON_LAND;
/*     */     }
/*  87 */     return isUnderWater() ? SoundEvents.NAUTILUS_DEATH : SoundEvents.NAUTILUS_DEATH_ON_LAND;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  92 */   protected SoundEvent getDashSound() { return isUnderWater() ? SoundEvents.NAUTILUS_DASH : SoundEvents.NAUTILUS_DASH_ON_LAND; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   protected SoundEvent getDashReadySound() { return isUnderWater() ? SoundEvents.NAUTILUS_DASH_READY : SoundEvents.NAUTILUS_DASH_READY_ON_LAND; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void playEatingSound() {
/* 102 */     SoundEvent nautilusEatSound = isBaby() ? SoundEvents.BABY_NAUTILUS_EAT : SoundEvents.NAUTILUS_EAT;
/* 103 */     makeSound(nautilusEatSound);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 108 */   protected SoundEvent getSwimSound() { return isBaby() ? SoundEvents.BABY_NAUTILUS_SWIM : SoundEvents.NAUTILUS_SWIM; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public int getMaxAirSupply() { return 300; }
/*     */ 
/*     */   
/*     */   protected void handleAirSupply(ServerLevel level, int preTickAirSupply) {
/* 117 */     if (isAlive() && !isInWater()) {
/* 118 */       setAirSupply(preTickAirSupply - 1);
/* 119 */       if (getAirSupply() <= -20) {
/* 120 */         setAirSupply(0);
/* 121 */         hurtServer(level, damageSources().dryOut(), 2.0F);
/*     */       } 
/*     */     } else {
/* 124 */       setAirSupply(300);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void baseTick() {
/* 130 */     int airSupply = getAirSupply();
/* 131 */     super.baseTick();
/* 132 */     if (!isNoAi()) { Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 133 */         handleAirSupply(serverLevel, airSupply); }
/*     */        }
/*     */   
/*     */   }
/*     */ 
/*     */   
/* 139 */   public boolean canBeLeashed() { return !isAggravated(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\nautilus\Nautilus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
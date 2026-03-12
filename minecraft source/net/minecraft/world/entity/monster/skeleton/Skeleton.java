/*     */ package net.minecraft.world.entity.monster.skeleton;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ public class Skeleton
/*     */   extends AbstractSkeleton
/*     */ {
/*     */   private static final int TOTAL_CONVERSION_TIME = 300;
/*  21 */   private static final EntityDataAccessor<Boolean> DATA_STRAY_CONVERSION_ID = SynchedEntityData.defineId(Skeleton.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   public static final String CONVERSION_TAG = "StrayConversionTime";
/*     */   
/*     */   private static final int NOT_CONVERTING = -1;
/*     */   private int inPowderSnowTime;
/*     */   private int conversionTime;
/*     */   
/*  29 */   public Skeleton(EntityType<? extends Skeleton> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  34 */     super.defineSynchedData(entityData);
/*     */     
/*  36 */     entityData.define(DATA_STRAY_CONVERSION_ID, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*  40 */   public boolean isFreezeConverting() { return ((Boolean)getEntityData().get(DATA_STRAY_CONVERSION_ID)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/*  44 */   public void setFreezeConverting(boolean isConverting) { this.entityData.set(DATA_STRAY_CONVERSION_ID, Boolean.valueOf(isConverting)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public boolean isShaking() { return isFreezeConverting(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  54 */     if (!level().isClientSide() && isAlive() && !isNoAi()) {
/*  55 */       if (this.isInPowderSnow) {
/*  56 */         if (isFreezeConverting()) {
/*  57 */           this.conversionTime--;
/*     */           
/*  59 */           if (this.conversionTime < 0) {
/*  60 */             doFreezeConversion();
/*     */           }
/*     */         } else {
/*  63 */           this.inPowderSnowTime++;
/*     */           
/*  65 */           if (this.inPowderSnowTime >= 140) {
/*  66 */             startFreezeConversion(300);
/*     */           }
/*     */         } 
/*     */       } else {
/*  70 */         this.inPowderSnowTime = -1;
/*  71 */         setFreezeConverting(false);
/*     */       } 
/*     */     }
/*     */     
/*  75 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  80 */     super.addAdditionalSaveData(output);
/*     */     
/*  82 */     output.putInt("StrayConversionTime", isFreezeConverting() ? this.conversionTime : -1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  87 */     super.readAdditionalSaveData(input);
/*     */     
/*  89 */     int conversionTime = input.getIntOr("StrayConversionTime", -1);
/*  90 */     if (conversionTime != -1) {
/*  91 */       startFreezeConversion(conversionTime);
/*     */     } else {
/*  93 */       setFreezeConverting(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void startFreezeConversion(int time) {
/*  99 */     this.conversionTime = time;
/* 100 */     setFreezeConverting(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doFreezeConversion() {
/* 105 */     convertTo(EntityType.STRAY, ConversionParams.single(this, true, true), stray -> {
/* 106 */           if (!isSilent()) {
/* 107 */             level().levelEvent(null, 1048, blockPosition(), 0);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public boolean canFreeze() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   protected SoundEvent getAmbientSound() { return SoundEvents.SKELETON_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.SKELETON_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   protected SoundEvent getDeathSound() { return SoundEvents.SKELETON_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   SoundEvent getStepSound() { return SoundEvents.SKELETON_STEP; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\skeleton\Skeleton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
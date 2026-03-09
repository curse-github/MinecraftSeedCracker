/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public abstract class AbstractPiglin
/*     */   extends Monster {
/*  25 */   protected static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(AbstractPiglin.class, EntityDataSerializers.BOOLEAN);
/*     */   public static final int CONVERSION_TIME = 300;
/*     */   private static final boolean DEFAULT_IMMUNE_TO_ZOMBIFICATION = false;
/*     */   private static final boolean DEFAULT_PICK_UP_LOOT = true;
/*     */   private static final int DEFAULT_TIME_IN_OVERWORLD = 0;
/*  30 */   protected int timeInOverworld = 0;
/*     */   
/*     */   public AbstractPiglin(EntityType<? extends AbstractPiglin> type, Level level) {
/*  33 */     super(type, level);
/*  34 */     setCanPickUpLoot(true);
/*  35 */     applyOpenDoorsAbility();
/*  36 */     setPathfindingMalus(PathType.DANGER_FIRE, 16.0F);
/*  37 */     setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
/*     */   }
/*     */   
/*     */   private void applyOpenDoorsAbility() {
/*  41 */     if (GoalUtils.hasGroundPathNavigation(this)) {
/*  42 */       getNavigation().setCanOpenDoors(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public void setImmuneToZombification(boolean isImmuneToZombification) { getEntityData().set(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(isImmuneToZombification)); }
/*     */ 
/*     */ 
/*     */   
/*  53 */   protected boolean isImmuneToZombification() { return ((Boolean)getEntityData().get(DATA_IMMUNE_TO_ZOMBIFICATION)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  58 */     super.defineSynchedData(entityData);
/*  59 */     entityData.define(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  64 */     super.addAdditionalSaveData(output);
/*     */     
/*  66 */     output.putBoolean("IsImmuneToZombification", isImmuneToZombification());
/*  67 */     output.putInt("TimeInOverworld", this.timeInOverworld);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  72 */     super.readAdditionalSaveData(input);
/*     */ 
/*     */     
/*  75 */     setCanPickUpLoot(input.getBooleanOr("CanPickUpLoot", true));
/*     */     
/*  77 */     setImmuneToZombification(input.getBooleanOr("IsImmuneToZombification", false));
/*  78 */     this.timeInOverworld = input.getIntOr("TimeInOverworld", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/*  83 */     super.customServerAiStep(level);
/*     */     
/*  85 */     if (isConverting()) {
/*  86 */       this.timeInOverworld++;
/*     */     } else {
/*  88 */       this.timeInOverworld = 0;
/*     */     } 
/*  90 */     if (this.timeInOverworld > 300) {
/*  91 */       playConvertedSound();
/*  92 */       finishConversion(level);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*  98 */   public void setTimeInOverworld(int timeInOverworld) { this.timeInOverworld = timeInOverworld; }
/*     */ 
/*     */ 
/*     */   
/* 102 */   public boolean isConverting() { return (!isImmuneToZombification() && !isNoAi() && ((Boolean)level().environmentAttributes().getValue(EnvironmentAttributes.PIGLINS_ZOMBIFY, position())).booleanValue()); }
/*     */ 
/*     */   
/*     */   protected void finishConversion(ServerLevel level) {
/* 106 */     convertTo(EntityType.ZOMBIFIED_PIGLIN, 
/*     */         
/* 108 */         ConversionParams.single(this, true, true), zombified -> 
/* 109 */         zombified.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 0)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public boolean isAdult() { return !isBaby(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public LivingEntity getTarget() { return getTargetFromBrain(); }
/*     */ 
/*     */ 
/*     */   
/* 125 */   protected boolean isHoldingMeleeWeapon() { return getMainHandItem().has(DataComponents.TOOL); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playAmbientSound() {
/* 130 */     if (PiglinAi.isIdle(this))
/* 131 */       super.playAmbientSound(); 
/*     */   }
/*     */   
/*     */   protected abstract boolean canHunt();
/*     */   
/*     */   public abstract PiglinArmPose getArmPose();
/*     */   
/*     */   protected abstract void playConvertedSound();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\piglin\AbstractPiglin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
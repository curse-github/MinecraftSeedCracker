/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.monster.CrossbowAttackMob;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.item.CrossbowItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.ChargedProjectiles;
/*     */ 
/*     */ public class CrossbowAttack<E extends Mob & CrossbowAttackMob, T extends LivingEntity>
/*     */   extends Behavior<E>
/*     */ {
/*     */   private static final int TIMEOUT = 1200;
/*     */   private int attackDelay;
/*     */   
/*     */   private enum CrossbowState
/*     */   {
/*  26 */     UNCHARGED,
/*  27 */     CHARGING,
/*  28 */     CHARGED,
/*  29 */     READY_TO_ATTACK;
/*     */   }
/*     */ 
/*     */   
/*  33 */   private CrossbowState crossbowState = CrossbowState.UNCHARGED;
/*     */ 
/*     */   
/*  36 */   public CrossbowAttack() { super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, E body) {
/*  44 */     LivingEntity attackTarget = getAttackTarget(body);
/*  45 */     return (body.isHolding(Items.CROSSBOW) && BehaviorUtils.canSee(body, attackTarget) && BehaviorUtils.isWithinAttackRange(body, attackTarget, 0));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  50 */   protected boolean canStillUse(ServerLevel level, E body, long timestamp) { return (body.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && checkExtraStartConditions(level, body)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, E body, long timestamp) {
/*  56 */     LivingEntity target = getAttackTarget(body);
/*  57 */     lookAtTarget(body, target);
/*  58 */     crossbowAttack(body, target);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, E body, long timestamp) {
/*  63 */     if (body.isUsingItem()) {
/*  64 */       body.stopUsingItem();
/*     */     }
/*  66 */     if (body.isHolding(Items.CROSSBOW)) {
/*  67 */       ((CrossbowAttackMob)body).setChargingCrossbow(false);
/*  68 */       body.getUseItem().set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void crossbowAttack(E body, LivingEntity target) {
/*  74 */     if (this.crossbowState == CrossbowState.UNCHARGED) {
/*  75 */       body.startUsingItem(ProjectileUtil.getWeaponHoldingHand(body, Items.CROSSBOW));
/*  76 */       this.crossbowState = CrossbowState.CHARGING;
/*  77 */       ((CrossbowAttackMob)body).setChargingCrossbow(true);
/*  78 */     } else if (this.crossbowState == CrossbowState.CHARGING) {
/*  79 */       if (!body.isUsingItem()) {
/*  80 */         this.crossbowState = CrossbowState.UNCHARGED;
/*     */       }
/*  82 */       int pullTime = body.getTicksUsingItem();
/*  83 */       ItemStack useItem = body.getUseItem();
/*  84 */       if (pullTime >= CrossbowItem.getChargeDuration(useItem, body)) {
/*  85 */         body.releaseUsingItem();
/*  86 */         this.crossbowState = CrossbowState.CHARGED;
/*  87 */         this.attackDelay = 20 + body.getRandom().nextInt(20);
/*  88 */         ((CrossbowAttackMob)body).setChargingCrossbow(false);
/*     */       } 
/*  90 */     } else if (this.crossbowState == CrossbowState.CHARGED) {
/*  91 */       this.attackDelay--;
/*  92 */       if (this.attackDelay == 0) {
/*  93 */         this.crossbowState = CrossbowState.READY_TO_ATTACK;
/*     */       }
/*  95 */     } else if (this.crossbowState == CrossbowState.READY_TO_ATTACK) {
/*  96 */       ((RangedAttackMob)body).performRangedAttack(target, 1.0F);
/*  97 */       this.crossbowState = CrossbowState.UNCHARGED;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 102 */   private void lookAtTarget(Mob body, LivingEntity target) { body.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true)); }
/*     */ 
/*     */ 
/*     */   
/* 106 */   private static LivingEntity getAttackTarget(LivingEntity body) { return (LivingEntity)body.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\CrossbowAttack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
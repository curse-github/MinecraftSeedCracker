/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.animal.allay.Allay;
/*     */ import net.minecraft.world.entity.animal.allay.AllayAi;
/*     */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GoAndGiveItemsToTarget<E extends LivingEntity & InventoryCarrier>
/*     */   extends Behavior<E>
/*     */ {
/*     */   private static final int CLOSE_ENOUGH_DISTANCE_TO_TARGET = 3;
/*     */   private static final int ITEM_PICKUP_COOLDOWN_AFTER_THROWING = 60;
/*     */   private final Function<LivingEntity, Optional<PositionTracker>> targetPositionGetter;
/*     */   private final float speedModifier;
/*     */   
/*     */   public GoAndGiveItemsToTarget(Function<LivingEntity, Optional<PositionTracker>> targetPositionGetter, float speedModifier, int timeoutDuration) {
/*  34 */     super(Map.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryStatus.REGISTERED), timeoutDuration);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  39 */     this.targetPositionGetter = targetPositionGetter;
/*  40 */     this.speedModifier = speedModifier;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  45 */   protected boolean checkExtraStartConditions(ServerLevel level, E body) { return canThrowItemToTarget(body); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   protected boolean canStillUse(ServerLevel level, E body, long timestamp) { return canThrowItemToTarget(body); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, E body, long timestamp) {
/*  55 */     ((Optional)this.targetPositionGetter.apply(body)).ifPresent(positionTracker -> 
/*     */         
/*  57 */         BehaviorUtils.setWalkAndLookTargetMemories(body, positionTracker, this.speedModifier, 3));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, E body, long timestamp) {
/*  62 */     Optional<PositionTracker> targetPosition = (Optional)this.targetPositionGetter.apply(body);
/*  63 */     if (targetPosition.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  67 */     PositionTracker depositTarget = (PositionTracker)targetPosition.get();
/*  68 */     double distanceToTarget = depositTarget.currentPosition().distanceTo(body.getEyePosition());
/*  69 */     if (distanceToTarget < 3.0D) {
/*  70 */       ItemStack item = ((InventoryCarrier)body).getInventory().removeItem(0, 1);
/*  71 */       if (!item.isEmpty()) {
/*  72 */         throwItem(body, item, getThrowPosition(depositTarget));
/*  73 */         if (body instanceof Allay) { Allay allay = (Allay)body;
/*  74 */           AllayAi.getLikedPlayer(allay).ifPresent(player -> triggerDropItemOnBlock(depositTarget, item, player)); }
/*     */         
/*  76 */         body.getBrain().setMemory(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, Integer.valueOf(60));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void triggerDropItemOnBlock(PositionTracker depositTarget, ItemStack item, ServerPlayer player) {
/*  82 */     BlockPos belowPos = depositTarget.currentBlockPosition().below();
/*  83 */     CriteriaTriggers.ALLAY_DROP_ITEM_ON_BLOCK.trigger(player, belowPos, item);
/*     */   }
/*     */   
/*     */   private boolean canThrowItemToTarget(E body) {
/*  87 */     if (((InventoryCarrier)body).getInventory().isEmpty()) {
/*  88 */       return false;
/*     */     }
/*  90 */     Optional<PositionTracker> positionTracker = (Optional)this.targetPositionGetter.apply(body);
/*  91 */     return positionTracker.isPresent();
/*     */   }
/*     */ 
/*     */   
/*  95 */   private static Vec3 getThrowPosition(PositionTracker depositTarget) { return depositTarget.currentPosition().add(0.0D, 1.0D, 0.0D); }
/*     */ 
/*     */   
/*     */   public static void throwItem(LivingEntity thrower, ItemStack item, Vec3 targetPos) {
/*  99 */     Vec3 throwVelocity = new Vec3(0.20000000298023224D, 0.30000001192092896D, 0.20000000298023224D);
/* 100 */     BehaviorUtils.throwItem(thrower, item, targetPos, throwVelocity, 0.2F);
/*     */     
/* 102 */     Level level = thrower.level();
/* 103 */     if (level.getGameTime() % 7L == 0L && level.random.nextDouble() < 0.9D) {
/* 104 */       float pitch = ((Float)Util.getRandom(Allay.THROW_SOUND_PITCHES, level.getRandom())).floatValue();
/* 105 */       level.playSound(null, thrower, SoundEvents.ALLAY_THROW, SoundSource.NEUTRAL, 1.0F, pitch);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\GoAndGiveItemsToTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
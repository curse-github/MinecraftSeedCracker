/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.monster.warden.Warden;
/*    */ import net.minecraft.world.entity.monster.warden.WardenAi;
/*    */ 
/*    */ public class Roar extends Behavior<Warden> {
/*    */   private static final int TICKS_BEFORE_PLAYING_ROAR_SOUND = 25;
/*    */   private static final int ROAR_ANGER_INCREASE = 20;
/*    */   
/* 22 */   public Roar() { super(ImmutableMap.of(MemoryModuleType.ROAR_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.ROAR_SOUND_COOLDOWN, MemoryStatus.REGISTERED, MemoryModuleType.ROAR_SOUND_DELAY, MemoryStatus.REGISTERED), WardenAi.ROAR_DURATION); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, Warden body, long timestamp) {
/* 32 */     Brain<Warden> brain = body.getBrain();
/* 33 */     brain.setMemoryWithExpiry(MemoryModuleType.ROAR_SOUND_DELAY, Unit.INSTANCE, 25L);
/* 34 */     brain.eraseMemory(MemoryModuleType.WALK_TARGET);
/* 35 */     LivingEntity target = (LivingEntity)body.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).get();
/* 36 */     BehaviorUtils.lookAtEntity(body, target);
/* 37 */     body.setPose(Pose.ROARING);
/* 38 */     body.increaseAngerAt(target, 20, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 43 */   protected boolean canStillUse(ServerLevel level, Warden body, long timestamp) { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel level, Warden body, long timestamp) {
/* 48 */     if (body.getBrain().hasMemoryValue(MemoryModuleType.ROAR_SOUND_DELAY) || body.getBrain().hasMemoryValue(MemoryModuleType.ROAR_SOUND_COOLDOWN)) {
/*    */       return;
/*    */     }
/*    */     
/* 52 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.ROAR_SOUND_COOLDOWN, Unit.INSTANCE, (WardenAi.ROAR_DURATION - 25));
/*    */     
/* 54 */     body.playSound(SoundEvents.WARDEN_ROAR, 3.0F, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel level, Warden body, long timestamp) {
/* 59 */     if (body.hasPose(Pose.ROARING)) {
/* 60 */       body.setPose(Pose.STANDING);
/*    */     }
/*    */     
/* 63 */     Objects.requireNonNull(body); body.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).ifPresent(body::setAttackTarget);
/* 64 */     body.getBrain().eraseMemory(MemoryModuleType.ROAR_TARGET);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\warden\Roar.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.monster.warden.Warden;
/*    */ import net.minecraft.world.entity.monster.warden.WardenAi;
/*    */ 
/*    */ public class Sniffing<E extends Warden> extends Behavior<E> {
/*    */   private static final double ANGER_FROM_SNIFFING_MAX_DISTANCE_XZ = 6.0D;
/*    */   private static final double ANGER_FROM_SNIFFING_MAX_DISTANCE_Y = 20.0D;
/*    */   
/* 19 */   public Sniffing(int ticks) { super(ImmutableMap.of(MemoryModuleType.IS_SNIFFING, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.NEAREST_ATTACKABLE, MemoryStatus.REGISTERED, MemoryModuleType.DISTURBANCE_LOCATION, MemoryStatus.REGISTERED, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.REGISTERED), ticks); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   protected boolean canStillUse(ServerLevel level, E body, long timestamp) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   protected void start(ServerLevel level, E body, long timestamp) { body.playSound(SoundEvents.WARDEN_SNIFF, 5.0F, 1.0F); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel level, E body, long timestamp) {
/* 42 */     if (body.hasPose(Pose.SNIFFING)) {
/* 43 */       body.setPose(Pose.STANDING);
/*    */     }
/*    */     
/* 46 */     body.getBrain().eraseMemory(MemoryModuleType.IS_SNIFFING);
/* 47 */     Objects.requireNonNull(body); body.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE).filter(body::canTargetEntity).ifPresent(entity -> {
/* 48 */           if (body.closerThan(entity, 6.0D, 20.0D)) {
/* 49 */             body.increaseAngerAt(entity);
/*    */           }
/*    */ 
/*    */           
/* 53 */           if (!body.getBrain().hasMemoryValue(MemoryModuleType.DISTURBANCE_LOCATION))
/* 54 */             WardenAi.setDisturbanceLocation(body, entity.blockPosition()); 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\warden\Sniffing.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
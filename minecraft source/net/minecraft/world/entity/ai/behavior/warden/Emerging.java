/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.monster.warden.Warden;
/*    */ 
/*    */ public class Emerging<E extends Warden> extends Behavior<E> {
/* 14 */   public Emerging(int ticks) { super(ImmutableMap.of(MemoryModuleType.IS_EMERGING, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), ticks); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   protected boolean canStillUse(ServerLevel level, E body, long timestamp) { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, E body, long timestamp) {
/* 28 */     body.setPose(Pose.EMERGING);
/* 29 */     body.playSound(SoundEvents.WARDEN_EMERGE, 5.0F, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel level, E body, long timestamp) {
/* 34 */     if (body.hasPose(Pose.EMERGING))
/* 35 */       body.setPose(Pose.STANDING); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\warden\Emerging.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
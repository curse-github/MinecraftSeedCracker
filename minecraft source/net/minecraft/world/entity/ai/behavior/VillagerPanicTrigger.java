/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillagerPanicTrigger
/*    */   extends Behavior<Villager>
/*    */ {
/* 16 */   public VillagerPanicTrigger() { super(ImmutableMap.of()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) { return (isHurt(body) || hasHostile(body)); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, Villager body, long timestamp) {
/* 26 */     if (isHurt(body) || hasHostile(body)) {
/* 27 */       Brain<?> brain = body.getBrain();
/*    */ 
/*    */       
/* 30 */       if (!brain.isActive(Activity.PANIC)) {
/* 31 */         brain.eraseMemory(MemoryModuleType.PATH);
/* 32 */         brain.eraseMemory(MemoryModuleType.WALK_TARGET);
/* 33 */         brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
/* 34 */         brain.eraseMemory(MemoryModuleType.BREED_TARGET);
/* 35 */         brain.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
/*    */       } 
/* 37 */       brain.setActiveActivityIfPossible(Activity.PANIC);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel level, Villager body, long timestamp) {
/* 43 */     if (timestamp % 100L == 0L) {
/* 44 */       body.spawnGolemIfNeeded(level, timestamp, 3);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 49 */   public static boolean hasHostile(LivingEntity myBody) { return myBody.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE); }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public static boolean isHurt(LivingEntity myBody) { return myBody.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\VillagerPanicTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
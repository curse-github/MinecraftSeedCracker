/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WorkAtPoi
/*    */   extends Behavior<Villager>
/*    */ {
/*    */   private static final int CHECK_COOLDOWN = 300;
/*    */   private static final double DISTANCE = 1.73D;
/*    */   private long lastCheck;
/*    */   
/* 25 */   public WorkAtPoi() { super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
/* 33 */     if (level.getGameTime() - this.lastCheck < 300L) {
/* 34 */       return false;
/*    */     }
/*    */     
/* 37 */     if (level.random.nextInt(2) != 0) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     this.lastCheck = level.getGameTime();
/*    */     
/* 43 */     GlobalPos target = (GlobalPos)body.getBrain().getMemory(MemoryModuleType.JOB_SITE).get();
/* 44 */     return (target.dimension() == level.dimension() && target.pos().closerToCenterThan(body.position(), 1.73D));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, Villager body, long timestamp) {
/* 49 */     Brain<Villager> brain = body.getBrain();
/* 50 */     brain.setMemory(MemoryModuleType.LAST_WORKED_AT_POI, Long.valueOf(timestamp));
/* 51 */     brain.getMemory(MemoryModuleType.JOB_SITE).ifPresent(globalPos -> 
/* 52 */         brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(globalPos.pos())));
/*    */ 
/*    */     
/* 55 */     body.playWorkSound();
/* 56 */     useWorkstation(level, body);
/*    */     
/* 58 */     if (body.shouldRestock(level)) {
/* 59 */       body.restock();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void useWorkstation(ServerLevel level, Villager body) {}
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) {
/* 68 */     Optional<GlobalPos> jobSiteMemory = body.getBrain().getMemory(MemoryModuleType.JOB_SITE);
/* 69 */     if (jobSiteMemory.isEmpty()) {
/* 70 */       return false;
/*    */     }
/*    */     
/* 73 */     GlobalPos target = (GlobalPos)jobSiteMemory.get();
/* 74 */     return (target.dimension() == level.dimension() && target
/* 75 */       .pos().closerToCenterThan(body.position(), 1.73D));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\WorkAtPoi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
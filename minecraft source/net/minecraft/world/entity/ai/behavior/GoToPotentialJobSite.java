/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GoToPotentialJobSite
/*    */   extends Behavior<Villager>
/*    */ {
/*    */   private static final int TICKS_UNTIL_TIMEOUT = 1200;
/*    */   final float speedModifier;
/*    */   
/*    */   public GoToPotentialJobSite(float speedModifier) {
/* 28 */     super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryStatus.VALUE_PRESENT), 1200);
/*    */ 
/*    */     
/* 31 */     this.speedModifier = speedModifier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 36 */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) { return ((Boolean)body.getBrain().getActiveNonCoreActivity().map(activity -> Boolean.valueOf((activity == Activity.IDLE || activity == Activity.WORK || activity == Activity.PLAY))).orElse(Boolean.valueOf(true))).booleanValue(); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) { return body.getBrain().hasMemoryValue(MemoryModuleType.POTENTIAL_JOB_SITE); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   protected void tick(ServerLevel level, Villager body, long timestamp) { BehaviorUtils.setWalkAndLookTargetMemories(body, ((GlobalPos)body.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get()).pos(), this.speedModifier, 1); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel level, Villager body, long timestamp) {
/* 52 */     Optional<GlobalPos> potentialJobSitePos = body.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
/* 53 */     potentialJobSitePos.ifPresent(globalPos -> {
/* 54 */           BlockPos pos = globalPos.pos();
/* 55 */           ServerLevel serverLevel = level.getServer().getLevel(globalPos.dimension());
/* 56 */           if (serverLevel == null) {
/*    */             return;
/*    */           }
/* 59 */           PoiManager manager = serverLevel.getPoiManager();
/* 60 */           if (manager.exists(pos, ())) {
/* 61 */             manager.release(pos);
/*    */           }
/* 63 */           level.debugSynchronizers().updatePoi(pos);
/*    */         });
/* 65 */     body.getBrain().eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\GoToPotentialJobSite.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
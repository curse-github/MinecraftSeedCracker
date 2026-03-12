/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class LookAndFollowTradingPlayerSink
/*    */   extends Behavior<Villager> {
/*    */   public LookAndFollowTradingPlayerSink(float speedModifier) {
/* 16 */     super(
/* 17 */         ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), 2147483647);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 23 */     this.speedModifier = speedModifier;
/*    */   }
/*    */   private final float speedModifier;
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
/* 28 */     Player tradingPlayer = body.getTradingPlayer();
/*    */     
/* 30 */     return (body.isAlive() && tradingPlayer != null && 
/*    */       
/* 32 */       !body.isInWater() && !body.hurtMarked && body
/*    */       
/* 34 */       .distanceToSqr(tradingPlayer) <= 16.0D);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) { return checkExtraStartConditions(level, body); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   protected void start(ServerLevel level, Villager body, long timestamp) { followPlayer(body); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel level, Villager body, long timestamp) {
/* 50 */     Brain<?> brain = body.getBrain();
/* 51 */     brain.eraseMemory(MemoryModuleType.WALK_TARGET);
/* 52 */     brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 57 */   protected void tick(ServerLevel level, Villager body, long timestamp) { followPlayer(body); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   protected boolean timedOut(long timestamp) { return false; }
/*    */ 
/*    */   
/*    */   private void followPlayer(Villager body) {
/* 66 */     Brain<?> brain = body.getBrain();
/* 67 */     brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(body.getTradingPlayer(), false), this.speedModifier, 2));
/* 68 */     brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(body.getTradingPlayer(), true));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\LookAndFollowTradingPlayerSink.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
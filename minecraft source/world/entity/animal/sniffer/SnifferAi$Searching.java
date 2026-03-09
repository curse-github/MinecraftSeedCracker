/*     */ package net.minecraft.world.entity.animal.sniffer;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.behavior.PositionTracker;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Searching
/*     */   extends Behavior<Sniffer>
/*     */ {
/* 221 */   private Searching() { super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_PRESENT), 600); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 232 */   protected boolean checkExtraStartConditions(ServerLevel level, Sniffer sniffer) { return sniffer.canSniff(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 237 */     if (!sniffer.canSniff()) {
/* 238 */       sniffer.transitionTo(Sniffer.State.IDLING);
/* 239 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 245 */     Optional<BlockPos> walkTarget = sniffer.getBrain().getMemory(MemoryModuleType.WALK_TARGET).map(WalkTarget::getTarget).map(PositionTracker::currentBlockPosition);
/*     */     
/* 247 */     Optional<BlockPos> sniffingTarget = sniffer.getBrain().getMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
/*     */     
/* 249 */     if (walkTarget.isEmpty() || sniffingTarget.isEmpty()) {
/* 250 */       return false;
/*     */     }
/*     */     
/* 253 */     return ((BlockPos)sniffingTarget.get()).equals(walkTarget.get());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 258 */   protected void start(ServerLevel level, Sniffer sniffer, long timestamp) { sniffer.transitionTo(Sniffer.State.SEARCHING); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 264 */     if (sniffer.canDig() && sniffer.canSniff()) {
/* 265 */       sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_DIGGING, Boolean.valueOf(true));
/*     */     }
/*     */ 
/*     */     
/* 269 */     sniffer.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 270 */     sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\sniffer\SnifferAi$Searching.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
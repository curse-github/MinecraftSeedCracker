/*     */ package net.minecraft.world.entity.animal.sniffer;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
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
/*     */ class Sniffing
/*     */   extends Behavior<Sniffer>
/*     */ {
/* 182 */   private Sniffing(int min, int max) { super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.VALUE_ABSENT), min, max); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 192 */   protected boolean checkExtraStartConditions(ServerLevel level, Sniffer body) { return (!body.isBaby() && body.canSniff()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   protected boolean canStillUse(ServerLevel level, Sniffer body, long timestamp) { return body.canSniff(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   protected void start(ServerLevel level, Sniffer sniffer, long timestamp) { sniffer.transitionTo(Sniffer.State.SNIFFING); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 207 */     boolean finished = timedOut(timestamp);
/* 208 */     sniffer.transitionTo(Sniffer.State.IDLING);
/*     */     
/* 210 */     if (finished)
/* 211 */       sniffer.calculateDigPosition().ifPresent(position -> {
/* 212 */             sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET, position);
/* 213 */             sniffer.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(position, 1.25F, 0));
/*     */           }); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\sniffer\SnifferAi$Sniffing.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
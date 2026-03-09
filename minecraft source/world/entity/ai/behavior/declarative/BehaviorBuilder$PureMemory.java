/*     */ package net.minecraft.world.entity.ai.behavior.declarative;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PureMemory<E extends LivingEntity, F extends K1, Value>
/*     */   extends BehaviorBuilder<E, MemoryAccessor<F, Value>>
/*     */ {
/*     */   private PureMemory(final MemoryCondition<F, Value> condition) {
/* 118 */     super(new BehaviorBuilder.TriggerWithResult<E, MemoryAccessor<F, Value>>()
/*     */         {
/*     */           public MemoryAccessor<F, Value> tryTrigger(ServerLevel level, E body, long timestamp)
/*     */           {
/* 122 */             Brain<?> brain = body.getBrain();
/* 123 */             Optional<Value> value = brain.getMemoryInternal(condition.memory());
/* 124 */             if (value == null) {
/* 125 */               return null;
/*     */             }
/* 127 */             return condition.createAccessor(brain, value);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 132 */           public String debugString() { return "M[" + String.valueOf(condition) + "]"; }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 137 */           public String toString() { return debugString(); }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$PureMemory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
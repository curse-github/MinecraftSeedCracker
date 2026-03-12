/*    */ package net.minecraft.world.entity.animal.axolotl;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class PlayDead
/*    */   extends Behavior<Axolotl> {
/* 15 */   public PlayDead() { super(ImmutableMap.of(MemoryModuleType.PLAY_DEAD_TICKS, MemoryStatus.VALUE_PRESENT, MemoryModuleType.HURT_BY_ENTITY, MemoryStatus.VALUE_PRESENT), 200); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   protected boolean checkExtraStartConditions(ServerLevel level, Axolotl body) { return body.isInWater(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   protected boolean canStillUse(ServerLevel level, Axolotl body, long timestamp) { return (body.isInWater() && body.getBrain().hasMemoryValue(MemoryModuleType.PLAY_DEAD_TICKS)); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, Axolotl body, long timestamp) {
/* 34 */     Brain<Axolotl> brain = body.getBrain();
/*    */     
/* 36 */     brain.eraseMemory(MemoryModuleType.WALK_TARGET);
/* 37 */     brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
/*    */     
/* 39 */     body.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\axolotl\PlayDead.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
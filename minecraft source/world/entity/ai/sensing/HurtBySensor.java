/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HurtBySensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/* 21 */   public Set<MemoryModuleType<?>> requires() { return ImmutableSet.of(MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 26 */     Brain<?> brain = body.getBrain();
/* 27 */     DamageSource damageSource = body.getLastDamageSource();
/* 28 */     if (damageSource != null) {
/* 29 */       brain.setMemory(MemoryModuleType.HURT_BY, body.getLastDamageSource());
/* 30 */       Entity entitySource = damageSource.getEntity();
/* 31 */       if (entitySource instanceof LivingEntity) {
/* 32 */         brain.setMemory(MemoryModuleType.HURT_BY_ENTITY, (LivingEntity)entitySource);
/*    */       }
/*    */     } else {
/* 35 */       brain.eraseMemory(MemoryModuleType.HURT_BY);
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     brain.getMemory(MemoryModuleType.HURT_BY_ENTITY).ifPresent(hurtByEntity -> {
/* 42 */           if (!hurtByEntity.isAlive() || hurtByEntity.level() != level)
/* 43 */             brain.eraseMemory(MemoryModuleType.HURT_BY_ENTITY); 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\HurtBySensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
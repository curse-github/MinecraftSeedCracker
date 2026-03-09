/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Iterables;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.monster.breeze.Breeze;
/*    */ 
/*    */ public class BreezeAttackEntitySensor
/*    */   extends NearestLivingEntitySensor<Breeze>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 18 */     return ImmutableSet.copyOf(Iterables.concat(super
/* 19 */           .requires(), 
/* 20 */           List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel level, Breeze breeze) {
/* 26 */     super.doTick(level, breeze);
/*    */     
/* 28 */     breeze.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream()
/* 29 */       .flatMap(Collection::stream)
/* 30 */       .filter(EntitySelector.NO_CREATIVE_OR_SPECTATOR)
/* 31 */       .filter(entity -> Sensor.isEntityAttackable(level, breeze, entity)).findFirst()
/* 32 */       .ifPresentOrElse(entity -> 
/* 33 */         breeze.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, entity), () -> 
/* 34 */         breeze.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\BreezeAttackEntitySensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
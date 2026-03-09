/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Iterables;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.monster.warden.Warden;
/*    */ 
/*    */ public class WardenEntitySensor
/*    */   extends NearestLivingEntitySensor<Warden> {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 20 */     return ImmutableSet.copyOf(Iterables.concat(super
/* 21 */           .requires(), 
/* 22 */           List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel level, Warden body) {
/* 28 */     super.doTick(level, body);
/*    */     
/* 30 */     getClosest(body, e -> (e.getType() == EntityType.PLAYER))
/* 31 */       .or(() -> getClosest(body, ()))
/* 32 */       .ifPresentOrElse(entity -> 
/* 33 */         body.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, entity), () -> 
/* 34 */         body.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Optional<LivingEntity> getClosest(Warden body, Predicate<LivingEntity> test) {
/* 41 */     Objects.requireNonNull(body); return body.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream).filter(body::canTargetEntity)
/* 42 */       .filter(test)
/* 43 */       .findFirst();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\WardenEntitySensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
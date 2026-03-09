/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PiglinBruteSpecificSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/* 23 */   public Set<MemoryModuleType<?>> requires() { return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEARBY_ADULT_PIGLINS); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 35 */     Brain<?> brain = body.getBrain();
/*    */     
/* 37 */     List<AbstractPiglin> adultPiglins = Lists.newArrayList();
/*    */ 
/*    */     
/* 40 */     NearestVisibleLivingEntities visibleLivingEntities = (NearestVisibleLivingEntities)brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
/* 41 */     Objects.requireNonNull(Mob.class); Optional<Mob> nemesis = visibleLivingEntities.findClosest(entity -> (entity instanceof net.minecraft.world.entity.monster.skeleton.WitherSkeleton || entity instanceof net.minecraft.world.entity.boss.wither.WitherBoss)).map(Mob.class::cast);
/*    */     
/* 43 */     List<LivingEntity> livingEntities = (List)brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(ImmutableList.of());
/* 44 */     for (LivingEntity entity : livingEntities) {
/* 45 */       if (entity instanceof AbstractPiglin && ((AbstractPiglin)entity).isAdult()) {
/* 46 */         adultPiglins.add((AbstractPiglin)entity);
/*    */       }
/*    */     } 
/*    */     
/* 50 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, nemesis);
/* 51 */     brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, adultPiglins);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\PiglinBruteSpecificSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
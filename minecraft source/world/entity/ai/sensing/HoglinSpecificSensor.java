/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*    */ import net.minecraft.world.entity.monster.piglin.Piglin;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HoglinSpecificSensor
/*    */   extends Sensor<Hoglin>
/*    */ {
/* 26 */   public Set<MemoryModuleType<?>> requires() { return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, new MemoryModuleType[0]); }
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
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel level, Hoglin body) {
/* 41 */     Brain<?> brain = body.getBrain();
/*    */     
/* 43 */     brain.setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(level, body));
/*    */     
/* 45 */     Optional<Piglin> adultPiglin = Optional.empty();
/* 46 */     int adultPiglinCount = 0;
/*    */     
/* 48 */     List<Hoglin> adultHoglins = Lists.newArrayList();
/*    */ 
/*    */     
/* 51 */     NearestVisibleLivingEntities visibleLivingEntities = (NearestVisibleLivingEntities)brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
/* 52 */     for (LivingEntity entity : visibleLivingEntities.findAll(entity -> (!entity.isBaby() && (entity instanceof Piglin || entity instanceof Hoglin)))) {
/* 53 */       if (entity instanceof Piglin) { Piglin piglin = (Piglin)entity;
/* 54 */         adultPiglinCount++;
/* 55 */         if (adultPiglin.isEmpty()) {
/* 56 */           adultPiglin = Optional.of(piglin);
/*    */         } }
/*    */ 
/*    */       
/* 60 */       if (entity instanceof Hoglin) { Hoglin hoglin = (Hoglin)entity;
/* 61 */         adultHoglins.add(hoglin); }
/*    */     
/*    */     } 
/*    */     
/* 65 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, adultPiglin);
/* 66 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, adultHoglins);
/* 67 */     brain.setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, Integer.valueOf(adultPiglinCount));
/* 68 */     brain.setMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, Integer.valueOf(adultHoglins.size()));
/*    */   }
/*    */   
/*    */   private Optional<BlockPos> findNearestRepellent(ServerLevel level, Hoglin body) {
/* 72 */     return BlockPos.findClosestMatch(body
/* 73 */         .blockPosition(), 8, 4, pos -> 
/*    */ 
/*    */         
/* 76 */         level.getBlockState(pos).is(BlockTags.HOGLIN_REPELLENTS));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\HoglinSpecificSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
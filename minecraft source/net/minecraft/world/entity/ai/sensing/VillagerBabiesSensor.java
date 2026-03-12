/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillagerBabiesSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/* 21 */   public Set<MemoryModuleType<?>> requires() { return ImmutableSet.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected void doTick(ServerLevel level, LivingEntity body) { body.getBrain().setMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES, getNearestVillagerBabies(body)); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   private List<LivingEntity> getNearestVillagerBabies(LivingEntity myBody) { return ImmutableList.copyOf(getVisibleEntities(myBody).findAll(this::isVillagerBaby)); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   private boolean isVillagerBaby(LivingEntity entity) { return (entity.getType() == EntityType.VILLAGER && entity.isBaby()); }
/*    */ 
/*    */   
/*    */   private NearestVisibleLivingEntities getVisibleEntities(LivingEntity myBody) {
/* 38 */     return (NearestVisibleLivingEntities)myBody.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
/* 39 */       .orElse(NearestVisibleLivingEntities.empty());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\VillagerBabiesSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
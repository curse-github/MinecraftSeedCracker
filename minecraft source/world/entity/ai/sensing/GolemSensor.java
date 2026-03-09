/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GolemSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   private static final int GOLEM_SCAN_RATE = 200;
/*    */   private static final int MEMORY_TIME_TO_LIVE = 599;
/*    */   
/* 22 */   public GolemSensor() { this(200); }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public GolemSensor(int scanRate) { super(scanRate); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   protected void doTick(ServerLevel level, LivingEntity body) { checkForNearbyGolem(body); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public Set<MemoryModuleType<?>> requires() { return ImmutableSet.of(MemoryModuleType.NEAREST_LIVING_ENTITIES); }
/*    */ 
/*    */   
/*    */   public static void checkForNearbyGolem(LivingEntity body) {
/* 40 */     Optional<List<LivingEntity>> livingEntitiesMemory = body.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);
/* 41 */     if (livingEntitiesMemory.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 45 */     boolean golemPresent = ((List)livingEntitiesMemory.get()).stream().anyMatch(entity -> entity.getType().equals(EntityType.IRON_GOLEM));
/*    */     
/* 47 */     if (golemPresent) {
/* 48 */       golemDetected(body);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 53 */   public static void golemDetected(LivingEntity body) { body.getBrain().setMemoryWithExpiry(MemoryModuleType.GOLEM_DETECTED_RECENTLY, Boolean.valueOf(true), 599L); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\GolemSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
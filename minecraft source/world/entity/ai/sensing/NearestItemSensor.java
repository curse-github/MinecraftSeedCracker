/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ 
/*    */ public class NearestItemSensor
/*    */   extends Sensor<Mob> {
/*    */   private static final long XZ_RANGE = 32L;
/*    */   private static final long Y_RANGE = 16L;
/*    */   public static final int MAX_DISTANCE_TO_WANTED_ITEM = 32;
/*    */   
/* 22 */   public Set<MemoryModuleType<?>> requires() { return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel level, Mob body) {
/* 28 */     Brain<?> brain = body.getBrain();
/*    */     
/* 30 */     List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, body.getBoundingBox().inflate(32.0D, 16.0D, 32.0D), item -> true);
/* 31 */     Objects.requireNonNull(body); items.sort(Comparator.comparingDouble(body::distanceToSqr));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 36 */     Objects.requireNonNull(body);
/* 37 */     Optional<ItemEntity> nearestVisibleLovedItem = items.stream().filter(itemEntity -> body.wantsToPickUp(level, itemEntity.getItem())).filter(itemEntity -> itemEntity.closerThan(body, 32.0D)).filter(body::hasLineOfSight).findFirst();
/* 38 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, nearestVisibleLovedItem);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\NearestItemSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
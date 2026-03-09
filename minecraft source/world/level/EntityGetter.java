/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public interface EntityGetter
/*     */ {
/*     */   List<Entity> getEntities(Entity paramEntity, AABB paramAABB, Predicate<? super Entity> paramPredicate);
/*     */   
/*     */   <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> paramEntityTypeTest, AABB paramAABB, Predicate<? super T> paramPredicate);
/*     */   
/*  24 */   default <T extends Entity> List<T> getEntitiesOfClass(Class<T> baseClass, AABB bb, Predicate<? super T> selector) { return getEntities(EntityTypeTest.forClass(baseClass), bb, selector); }
/*     */ 
/*     */   
/*     */   List<? extends Player> players();
/*     */ 
/*     */   
/*  30 */   default List<Entity> getEntities(Entity except, AABB bb) { return getEntities(except, bb, EntitySelector.NO_SPECTATORS); }
/*     */ 
/*     */   
/*     */   default boolean isUnobstructed(Entity source, VoxelShape shape) {
/*  34 */     if (shape.isEmpty()) {
/*  35 */       return true;
/*     */     }
/*     */     
/*  38 */     for (Entity entity : getEntities(source, shape.bounds())) {
/*  39 */       if (!entity.isRemoved() && entity.blocksBuilding && (source == null || !entity.isPassengerOfSameVehicle(source)) && 
/*  40 */         Shapes.joinIsNotEmpty(shape, Shapes.create(entity.getBoundingBox()), BooleanOp.AND)) {
/*  41 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  45 */     return true;
/*     */   }
/*     */ 
/*     */   
/*  49 */   default <T extends Entity> List<T> getEntitiesOfClass(Class<T> baseClass, AABB bb) { return getEntitiesOfClass(baseClass, bb, EntitySelector.NO_SPECTATORS); }
/*     */ 
/*     */ 
/*     */   
/*     */   default List<VoxelShape> getEntityCollisions(Entity source, AABB testArea) {
/*  54 */     if (testArea.getSize() < 1.0E-7D) {
/*  55 */       return List.of();
/*     */     }
/*     */     
/*  58 */     Objects.requireNonNull(source); Predicate<Entity> canCollide = (source == null) ? EntitySelector.CAN_BE_COLLIDED_WITH : EntitySelector.NO_SPECTATORS.and(source::canCollideWith);
/*  59 */     List<Entity> collidingEntities = getEntities(source, testArea.inflate(1.0E-7D), canCollide);
/*     */     
/*  61 */     if (collidingEntities.isEmpty()) {
/*  62 */       return List.of();
/*     */     }
/*     */     
/*  65 */     ImmutableList.Builder<VoxelShape> shapes = ImmutableList.builderWithExpectedSize(collidingEntities.size());
/*  66 */     for (Entity entity : collidingEntities) {
/*  67 */       shapes.add(Shapes.create(entity.getBoundingBox()));
/*     */     }
/*     */     
/*  70 */     return shapes.build();
/*     */   }
/*     */   
/*     */   default Player getNearestPlayer(double x, double y, double z, double range, Predicate<Entity> predicate) {
/*  74 */     double best = -1.0D;
/*  75 */     Player result = null;
/*     */     
/*  77 */     for (Player player : players()) {
/*  78 */       if (predicate != null && !predicate.test(player)) {
/*     */         continue;
/*     */       }
/*     */       
/*  82 */       double dist = player.distanceToSqr(x, y, z);
/*  83 */       if ((range < 0.0D || dist < range * range) && (best == -1.0D || dist < best)) {
/*  84 */         best = dist;
/*  85 */         result = player;
/*     */       } 
/*     */     } 
/*  88 */     return result;
/*     */   }
/*     */ 
/*     */   
/*  92 */   default Player getNearestPlayer(Entity source, double maxDist) { return getNearestPlayer(source.getX(), source.getY(), source.getZ(), maxDist, false); }
/*     */ 
/*     */   
/*     */   default Player getNearestPlayer(double x, double y, double z, double maxDist, boolean filterOutCreative) {
/*  96 */     Predicate<Entity> predicate = filterOutCreative ? EntitySelector.NO_CREATIVE_OR_SPECTATOR : EntitySelector.NO_SPECTATORS;
/*  97 */     return getNearestPlayer(x, y, z, maxDist, predicate);
/*     */   }
/*     */   
/*     */   default boolean hasNearbyAlivePlayer(double x, double y, double z, double range) {
/* 101 */     for (Player player : players()) {
/* 102 */       if (!EntitySelector.NO_SPECTATORS.test(player) || !EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(player)) {
/*     */         continue;
/*     */       }
/* 105 */       double playerDist = player.distanceToSqr(x, y, z);
/* 106 */       if (range < 0.0D || playerDist < range * range) {
/* 107 */         return true;
/*     */       }
/*     */     } 
/* 110 */     return false;
/*     */   }
/*     */   
/*     */   default Player getPlayerByUUID(UUID uuid) {
/* 114 */     for (int i = 0; i < players().size(); i++) {
/* 115 */       Player player = (Player)players().get(i);
/* 116 */       if (uuid.equals(player.getUUID())) {
/* 117 */         return player;
/*     */       }
/*     */     } 
/* 120 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\EntityGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
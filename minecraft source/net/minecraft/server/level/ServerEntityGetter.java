/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.EntityGetter;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ServerEntityGetter
/*    */   extends EntityGetter
/*    */ {
/* 19 */   default Player getNearestPlayer(TargetingConditions targetConditions, LivingEntity source) { return (Player)getNearestEntity(players(), targetConditions, source, source.getX(), source.getY(), source.getZ()); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   default Player getNearestPlayer(TargetingConditions targetConditions, LivingEntity source, double x, double y, double z) { return (Player)getNearestEntity(players(), targetConditions, source, x, y, z); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   default Player getNearestPlayer(TargetingConditions targetConditions, double x, double y, double z) { return (Player)getNearestEntity(players(), targetConditions, null, x, y, z); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   default <T extends LivingEntity> T getNearestEntity(Class<? extends T> type, TargetingConditions targetConditions, LivingEntity source, double x, double y, double z, AABB bb) { return (T)getNearestEntity(getEntitiesOfClass(type, bb, entity -> true), targetConditions, source, x, y, z); }
/*    */ 
/*    */   
/*    */   default LivingEntity getNearestEntity(TagKey<EntityType<?>> tag, TargetingConditions targetConditions, LivingEntity source, double x, double y, double z, AABB bb) {
/* 35 */     double bestDistance = Double.MAX_VALUE;
/* 36 */     LivingEntity nearestEntity = null;
/* 37 */     for (LivingEntity entity : getEntitiesOfClass(LivingEntity.class, bb, e -> e.getType().is(tag))) {
/* 38 */       if (!targetConditions.test(getLevel(), source, entity)) {
/*    */         continue;
/*    */       }
/*    */       
/* 42 */       double distance = entity.distanceToSqr(x, y, z);
/* 43 */       if (distance < bestDistance) {
/* 44 */         bestDistance = distance;
/* 45 */         nearestEntity = entity;
/*    */       } 
/*    */     } 
/* 48 */     return nearestEntity;
/*    */   }
/*    */   
/*    */   default <T extends LivingEntity> T getNearestEntity(List<? extends T> entities, TargetingConditions targetConditions, LivingEntity source, double x, double y, double z) {
/* 52 */     double best = -1.0D;
/* 53 */     T result = null;
/* 54 */     for (Iterator iterator = entities.iterator(); iterator.hasNext(); ) { T entity = (T)(LivingEntity)iterator.next();
/* 55 */       if (!targetConditions.test(getLevel(), source, entity)) {
/*    */         continue;
/*    */       }
/*    */       
/* 59 */       double dist = entity.distanceToSqr(x, y, z);
/* 60 */       if (best == -1.0D || dist < best) {
/* 61 */         best = dist;
/* 62 */         result = entity;
/*    */       }  }
/*    */ 
/*    */     
/* 66 */     return result;
/*    */   }
/*    */   
/*    */   default List<Player> getNearbyPlayers(TargetingConditions targetConditions, LivingEntity source, AABB bb) {
/* 70 */     List<Player> foundPlayers = new ArrayList<Player>();
/* 71 */     for (Player player : players()) {
/* 72 */       if (bb.contains(player.getX(), player.getY(), player.getZ()) && targetConditions.test(getLevel(), source, player)) {
/* 73 */         foundPlayers.add(player);
/*    */       }
/*    */     } 
/*    */     
/* 77 */     return foundPlayers;
/*    */   }
/*    */   
/*    */   default <T extends LivingEntity> List<T> getNearbyEntities(Class<T> type, TargetingConditions targetConditions, LivingEntity source, AABB bb) {
/* 81 */     List<T> nearby = getEntitiesOfClass(type, bb, entity -> true);
/* 82 */     List<T> entities = new ArrayList<T>();
/*    */     
/* 84 */     for (Iterator iterator = nearby.iterator(); iterator.hasNext(); ) { T entity = (T)(LivingEntity)iterator.next();
/* 85 */       if (targetConditions.test(getLevel(), source, entity)) {
/* 86 */         entities.add(entity);
/*    */       } }
/*    */ 
/*    */     
/* 90 */     return entities;
/*    */   }
/*    */   
/*    */   ServerLevel getLevel();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ServerEntityGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
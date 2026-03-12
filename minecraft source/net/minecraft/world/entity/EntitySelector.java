/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.google.common.base.Predicates;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.scores.PlayerTeam;
/*    */ import net.minecraft.world.scores.Team;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class EntitySelector
/*    */ {
/* 14 */   public static final Predicate<Entity> ENTITY_STILL_ALIVE = Entity::isAlive;
/* 15 */   public static final Predicate<Entity> LIVING_ENTITY_STILL_ALIVE = entity -> (entity.isAlive() && entity instanceof LivingEntity);
/* 16 */   public static final Predicate<Entity> ENTITY_NOT_BEING_RIDDEN = entity -> (entity.isAlive() && !entity.isVehicle() && !entity.isPassenger());
/* 17 */   public static final Predicate<Entity> CONTAINER_ENTITY_SELECTOR = entity -> (entity instanceof net.minecraft.world.Container && entity.isAlive()); public static final Predicate<Entity> NO_CREATIVE_OR_SPECTATOR = entity -> {
/* 18 */       if (entity instanceof Player) { Player player = (Player)entity; if (!entity.isSpectator() && !player.isCreative()); return false; } 
/* 19 */     }; public static final Predicate<Entity> NO_SPECTATORS = entity -> !entity.isSpectator();
/* 20 */   public static final Predicate<Entity> CAN_BE_COLLIDED_WITH = NO_SPECTATORS.and(entity -> entity.canBeCollidedWith(null));
/* 21 */   public static final Predicate<Entity> CAN_BE_PICKED = NO_SPECTATORS.and(Entity::isPickable);
/*    */   
/*    */   public static Predicate<Entity> withinDistance(double centerX, double centerY, double centerZ, double distance) {
/* 24 */     double distanceSqr = distance * distance;
/* 25 */     return input -> (input.distanceToSqr(centerX, centerY, centerZ) <= distanceSqr);
/*    */   }
/*    */   
/*    */   public static Predicate<Entity> pushableBy(Entity entity) {
/* 29 */     PlayerTeam playerTeam = entity.getTeam();
/* 30 */     Team.CollisionRule ownCollisionRule = (playerTeam == null) ? Team.CollisionRule.ALWAYS : playerTeam.getCollisionRule();
/* 31 */     if (ownCollisionRule == Team.CollisionRule.NEVER) {
/* 32 */       return Predicates.alwaysFalse();
/*    */     }
/* 34 */     return NO_SPECTATORS.and(input -> {
/* 35 */           if (!input.isPushable()) {
/* 36 */             return false;
/*    */           }
/* 38 */           if (entity.level().isClientSide()) if (input instanceof Player) { Player player = (Player)input; if (!player.isLocalPlayer())
/* 39 */                 return false;  } else { return false; }
/*    */              
/* 41 */           PlayerTeam playerTeam = input.getTeam();
/* 42 */           Team.CollisionRule theirCollisionRule = (playerTeam == null) ? Team.CollisionRule.ALWAYS : playerTeam.getCollisionRule();
/* 43 */           if (theirCollisionRule == Team.CollisionRule.NEVER) {
/* 44 */             return false;
/*    */           }
/* 46 */           boolean sameTeam = (ownTeam != null && ownTeam.isAlliedTo(playerTeam));
/* 47 */           if ((ownCollisionRule == Team.CollisionRule.PUSH_OWN_TEAM || theirCollisionRule == Team.CollisionRule.PUSH_OWN_TEAM) && sameTeam) {
/* 48 */             return false;
/*    */           }
/* 50 */           if ((ownCollisionRule == Team.CollisionRule.PUSH_OTHER_TEAMS || theirCollisionRule == Team.CollisionRule.PUSH_OTHER_TEAMS) && !sameTeam) {
/* 51 */             return false;
/*    */           }
/* 53 */           return true;
/*    */         });
/*    */   }
/*    */   
/*    */   public static Predicate<Entity> notRiding(Entity entity) {
/* 58 */     return input -> {
/* 59 */         while (input.isPassenger()) {
/* 60 */           input = input.getVehicle();
/* 61 */           if (input == entity) {
/* 62 */             return false;
/*    */           }
/*    */         } 
/* 65 */         return true;
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\EntitySelector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.block.entity.trialspawner;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.entity.EntityTypeTest;
/*    */ import net.minecraft.world.phys.AABB;
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
/*    */ 
/*    */ 
/*    */ public interface EntitySelector
/*    */ {
/* 55 */   public static final EntitySelector SELECT_FROM_LEVEL = new EntitySelector()
/*    */     {
/*    */       public List<ServerPlayer> getPlayers(ServerLevel level, Predicate<? super Player> selector) {
/* 58 */         return level.getPlayers(selector);
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 63 */       public <T extends Entity> List<T> getEntities(ServerLevel level, EntityTypeTest<Entity, T> type, AABB aabb, Predicate<? super T> selector) { return level.getEntities(type, aabb, selector); }
/*    */     };
/*    */   
/*    */   List<? extends Player> getPlayers(ServerLevel paramServerLevel, Predicate<? super Player> paramPredicate);
/*    */   
/* 68 */   static EntitySelector onlySelectPlayer(Player player) { return onlySelectPlayers(List.of(player)); }
/*    */ 
/*    */   
/*    */   static EntitySelector onlySelectPlayers(final List<Player> players) {
/* 72 */     return new EntitySelector()
/*    */       {
/*    */         public List<Player> getPlayers(ServerLevel level, Predicate<? super Player> selector) {
/* 75 */           return players.stream()
/* 76 */             .filter(selector)
/* 77 */             .toList();
/*    */         }
/*    */ 
/*    */ 
/*    */         
/*    */         public <T extends Entity> List<T> getEntities(ServerLevel level, EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) {
/* 83 */           Objects.requireNonNull(type); return players.stream().map(type::tryCast)
/* 84 */             .filter(Objects::nonNull)
/* 85 */             .filter(selector)
/* 86 */             .toList();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   <T extends Entity> List<T> getEntities(ServerLevel paramServerLevel, EntityTypeTest<Entity, T> paramEntityTypeTest, AABB paramAABB, Predicate<? super T> paramPredicate);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\trialspawner\PlayerDetector$EntitySelector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
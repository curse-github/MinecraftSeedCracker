/*    */ package net.minecraft.world.level.block.entity.trialspawner;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
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
/*    */ class null
/*    */   implements PlayerDetector.EntitySelector
/*    */ {
/* 75 */   public List<Player> getPlayers(ServerLevel level, Predicate<? super Player> selector) { return players.stream()
/* 76 */       .filter(selector)
/* 77 */       .toList(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T extends Entity> List<T> getEntities(ServerLevel level, EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) {
/* 83 */     Objects.requireNonNull(type); return players.stream().map(type::tryCast)
/* 84 */       .filter(Objects::nonNull)
/* 85 */       .filter(selector)
/* 86 */       .toList();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\trialspawner\PlayerDetector$EntitySelector$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
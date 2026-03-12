/*    */ package net.minecraft.world.level.block.entity.trialspawner;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.entity.EntityTypeTest;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ 
/*    */ public interface PlayerDetector {
/* 25 */   public static final PlayerDetector NO_CREATIVE_PLAYERS = (level, selector, pos, requiredPlayerRange, requireLineOfSight) -> selector.getPlayers(level, ()).stream()
/* 26 */     .filter(())
/* 27 */     .map(Entity::getUUID)
/* 28 */     .toList();
/*    */   
/* 30 */   public static final PlayerDetector INCLUDING_CREATIVE_PLAYERS = (level, selector, pos, requiredPlayerRange, requireLineOfSight) -> selector.getPlayers(level, ()).stream()
/* 31 */     .filter(())
/* 32 */     .map(Entity::getUUID)
/* 33 */     .toList();
/*    */   
/*    */   public static final PlayerDetector SHEEP = (level, selector, pos, requiredPlayerRange, requireLineOfSight) -> {
/* 36 */       AABB area = (new AABB(pos)).inflate(requiredPlayerRange);
/* 37 */       return selector.getEntities(level, EntityType.SHEEP, area, LivingEntity::isAlive).stream()
/* 38 */         .filter(())
/* 39 */         .map(Entity::getUUID)
/* 40 */         .toList();
/*    */     };
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean inLineOfSight(Level level, Vec3 origin, Vec3 dest) {
/* 46 */     BlockHitResult hitResult = level.clip(new ClipContext(dest, origin, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, CollisionContext.empty()));
/* 47 */     return (hitResult.getBlockPos().equals(BlockPos.containing(origin)) || hitResult.getType() == HitResult.Type.MISS);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   List<UUID> detect(ServerLevel paramServerLevel, EntitySelector paramEntitySelector, BlockPos paramBlockPos, double paramDouble, boolean paramBoolean);
/*    */ 
/*    */   
/*    */   class null
/*    */     implements EntitySelector
/*    */   {
/* 58 */     public List<ServerPlayer> getPlayers(ServerLevel level, Predicate<? super Player> selector) { return level.getPlayers(selector); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 63 */     public <T extends Entity> List<T> getEntities(ServerLevel level, EntityTypeTest<Entity, T> type, AABB aabb, Predicate<? super T> selector) { return level.getEntities(type, aabb, selector); } } public static interface EntitySelector { public static final EntitySelector SELECT_FROM_LEVEL = new EntitySelector() { public List<ServerPlayer> getPlayers(ServerLevel level, Predicate<? super Player> selector) { return level.getPlayers(selector); } public <T extends Entity> List<T> getEntities(ServerLevel level, EntityTypeTest<Entity, T> type, AABB aabb, Predicate<? super T> selector) { return level.getEntities(type, aabb, selector); } }
/*    */     ;
/*    */     List<? extends Player> getPlayers(ServerLevel param1ServerLevel, Predicate<? super Player> param1Predicate);
/*    */     <T extends Entity> List<T> getEntities(ServerLevel param1ServerLevel, EntityTypeTest<Entity, T> param1EntityTypeTest, AABB param1AABB, Predicate<? super T> param1Predicate);
/*    */     
/* 68 */     static EntitySelector onlySelectPlayer(Player player) { return onlySelectPlayers(List.of(player)); }
/*    */     
/*    */     static EntitySelector onlySelectPlayers(final List<Player> players)
/*    */     {
/* 72 */       return new EntitySelector()
/*    */         {
/*    */           public List<Player> getPlayers(ServerLevel level, Predicate<? super Player> selector) {
/* 75 */             return players.stream()
/* 76 */               .filter(selector)
/* 77 */               .toList();
/*    */           }
/*    */ 
/*    */           
/*    */           public <T extends Entity> List<T> getEntities(ServerLevel level, EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector)
/*    */           {
/* 83 */             Objects.requireNonNull(type); return players.stream().map(type::tryCast)
/* 84 */               .filter(Objects::nonNull)
/* 85 */               .filter(selector)
/* 86 */               .toList(); } }; } } class null implements EntitySelector { public <T extends Entity> List<T> getEntities(ServerLevel level, EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) { Objects.requireNonNull(type); return players.stream().map(type::tryCast).filter(Objects::nonNull).filter(selector).toList(); }
/*    */ 
/*    */     
/*    */     public List<Player> getPlayers(ServerLevel level, Predicate<? super Player> selector) { return players.stream().filter(selector).toList(); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\trialspawner\PlayerDetector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
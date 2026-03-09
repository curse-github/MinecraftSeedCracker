/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ChunkMap;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ 
/*    */ public class LocalMobCapCalculator
/*    */ {
/*    */   private final Long2ObjectMap<List<ServerPlayer>> playersNearChunk;
/*    */   
/*    */   public LocalMobCapCalculator(ChunkMap chunkMap) {
/* 19 */     this.playersNearChunk = new Long2ObjectOpenHashMap();
/* 20 */     this.playerMobCounts = Maps.newHashMap();
/*    */ 
/*    */ 
/*    */     
/* 24 */     this.chunkMap = chunkMap;
/*    */   }
/*    */   private final Map<ServerPlayer, MobCounts> playerMobCounts; private final ChunkMap chunkMap;
/*    */   
/* 28 */   private List<ServerPlayer> getPlayersNear(ChunkPos pos) { return (List)this.playersNearChunk.computeIfAbsent(pos.toLong(), key -> this.chunkMap.getPlayersCloseForSpawning(pos)); }
/*    */ 
/*    */   
/*    */   public void addMob(ChunkPos pos, MobCategory category) {
/* 32 */     for (ServerPlayer player : getPlayersNear(pos)) {
/* 33 */       ((MobCounts)this.playerMobCounts.computeIfAbsent(player, key -> new MobCounts())).add(category);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean canSpawn(MobCategory mobCategory, ChunkPos pos) {
/* 38 */     for (ServerPlayer serverPlayer : getPlayersNear(pos)) {
/* 39 */       MobCounts mobCounts = (MobCounts)this.playerMobCounts.get(serverPlayer);
/* 40 */       if (mobCounts == null || mobCounts.canSpawn(mobCategory)) {
/* 41 */         return true;
/*    */       }
/*    */     } 
/* 44 */     return false;
/*    */   }
/*    */   
/*    */   private static class MobCounts {
/* 48 */     private final Object2IntMap<MobCategory> counts = new Object2IntOpenHashMap(MobCategory.values().length);
/*    */ 
/*    */     
/* 51 */     public void add(MobCategory category) { this.counts.computeInt(category, (k, count) -> Integer.valueOf((count == null) ? 1 : (count.intValue() + 1))); }
/*    */ 
/*    */ 
/*    */     
/* 55 */     public boolean canSpawn(MobCategory category) { return (this.counts.getOrDefault(category, 0) < category.getMaxInstancesPerChunk()); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\LocalMobCapCalculator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
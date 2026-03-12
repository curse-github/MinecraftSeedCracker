/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*    */ import java.util.Set;
/*    */ 
/*    */ public final class PlayerMap
/*    */ {
/*  9 */   private final Object2BooleanMap<ServerPlayer> players = new Object2BooleanOpenHashMap();
/*    */ 
/*    */   
/* 12 */   public Set<ServerPlayer> getAllPlayers() { return this.players.keySet(); }
/*    */ 
/*    */ 
/*    */   
/* 16 */   public void addPlayer(ServerPlayer player, boolean ignored) { this.players.put(player, ignored); }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public void removePlayer(ServerPlayer player) { this.players.removeBoolean(player); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public void ignorePlayer(ServerPlayer player) { this.players.replace(player, true); }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public void unIgnorePlayer(ServerPlayer player) { this.players.replace(player, false); }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public boolean ignoredOrUnknown(ServerPlayer player) { return this.players.getOrDefault(player, true); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public boolean ignored(ServerPlayer player) { return this.players.getBoolean(player); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\PlayerMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.stats;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class StatsCounter {
/*    */   public StatsCounter() {
/*  9 */     this.stats = Object2IntMaps.synchronize(new Object2IntOpenHashMap());
/*    */ 
/*    */     
/* 12 */     this.stats.defaultReturnValue(0);
/*    */   }
/*    */   protected final Object2IntMap<Stat<?>> stats;
/*    */   public void increment(Player player, Stat<?> stat, int count) {
/* 16 */     int result = (int)Math.min(getValue(stat) + count, 2147483647L);
/* 17 */     setValue(player, stat, result);
/*    */   }
/*    */ 
/*    */   
/* 21 */   public void setValue(Player player, Stat<?> stat, int count) { this.stats.put(stat, count); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public <T> int getValue(StatType<T> type, T key) { return type.contains(key) ? getValue(type.get(key)) : 0; }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public int getValue(Stat<?> stat) { return this.stats.getInt(stat); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\stats\StatsCounter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
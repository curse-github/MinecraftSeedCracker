/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import net.minecraft.world.entity.MobCategory;
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
/*    */ class MobCounts
/*    */ {
/* 48 */   private final Object2IntMap<MobCategory> counts = new Object2IntOpenHashMap(MobCategory.values().length);
/*    */ 
/*    */   
/* 51 */   public void add(MobCategory category) { this.counts.computeInt(category, (k, count) -> Integer.valueOf((count == null) ? 1 : (count.intValue() + 1))); }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public boolean canSpawn(MobCategory category) { return (this.counts.getOrDefault(category, 0) < category.getMaxInstancesPerChunk()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\LocalMobCapCalculator$MobCounts.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
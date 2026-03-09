/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import org.apache.commons.lang3.mutable.MutableInt;
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
/*    */   extends CacheLoader<ServerLevel, FeatureCountTracker.LevelData>
/*    */ {
/* 32 */   public FeatureCountTracker.LevelData load(ServerLevel level) { return new FeatureCountTracker.LevelData(Object2IntMaps.synchronize(new Object2IntOpenHashMap()), new MutableInt(0)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\FeatureCountTracker$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
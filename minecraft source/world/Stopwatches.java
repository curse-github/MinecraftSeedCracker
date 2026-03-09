/*    */ package net.minecraft.world;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.TreeMap;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.saveddata.SavedData;
/*    */ import net.minecraft.world.level.saveddata.SavedDataType;
/*    */ 
/*    */ public class Stopwatches
/*    */   extends SavedData {
/* 18 */   private static final Codec<Stopwatches> CODEC = Codec.unboundedMap(Identifier.CODEC, Codec.LONG).fieldOf("stopwatches").codec()
/* 19 */     .xmap(Stopwatches::unpack, Stopwatches::pack);
/* 20 */   public static final SavedDataType<Stopwatches> TYPE = new SavedDataType("stopwatches", Stopwatches::new, CODEC, DataFixTypes.SAVED_DATA_STOPWATCHES);
/* 21 */   private final Map<Identifier, Stopwatch> stopwatches = new Object2ObjectOpenHashMap();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Stopwatches unpack(Map<Identifier, Long> stopwatches) {
/* 27 */     Stopwatches result = new Stopwatches();
/* 28 */     long currentTime = currentTime();
/* 29 */     stopwatches.forEach((id, accumulatedElapsedTime) -> result.stopwatches.put(id, new Stopwatch(currentTime, accumulatedElapsedTime.longValue())));
/* 30 */     return result;
/*    */   }
/*    */   
/*    */   private Map<Identifier, Long> pack() {
/* 34 */     long currentTime = currentTime();
/* 35 */     Map<Identifier, Long> result = new TreeMap<Identifier, Long>();
/* 36 */     this.stopwatches.forEach((id, stopwatch) -> result.put(id, Long.valueOf(stopwatch.elapsedMilliseconds(currentTime))));
/* 37 */     return result;
/*    */   }
/*    */ 
/*    */   
/* 41 */   public Stopwatch get(Identifier id) { return (Stopwatch)this.stopwatches.get(id); }
/*    */ 
/*    */   
/*    */   public boolean add(Identifier id, Stopwatch stopwatch) {
/* 45 */     if (this.stopwatches.putIfAbsent(id, stopwatch) == null) {
/* 46 */       setDirty();
/* 47 */       return true;
/*    */     } 
/* 49 */     return false;
/*    */   }
/*    */   
/*    */   public boolean update(Identifier id, UnaryOperator<Stopwatch> update) {
/* 53 */     if (this.stopwatches.computeIfPresent(id, (key, value) -> (Stopwatch)update.apply(value)) != null) {
/* 54 */       setDirty();
/* 55 */       return true;
/*    */     } 
/* 57 */     return false;
/*    */   }
/*    */   
/*    */   public boolean remove(Identifier id) {
/* 61 */     boolean removed = (this.stopwatches.remove(id) != null);
/* 62 */     if (removed) {
/* 63 */       setDirty();
/*    */     }
/* 65 */     return removed;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 70 */   public boolean isDirty() { return (super.isDirty() || !this.stopwatches.isEmpty()); }
/*    */ 
/*    */ 
/*    */   
/* 74 */   public List<Identifier> ids() { return List.copyOf(this.stopwatches.keySet()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 79 */   public static long currentTime() { return Util.getMillis(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\Stopwatches.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
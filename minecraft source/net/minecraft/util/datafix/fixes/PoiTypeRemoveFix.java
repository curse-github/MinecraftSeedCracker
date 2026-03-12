/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class PoiTypeRemoveFix
/*    */   extends AbstractPoiSectionFix {
/*    */   private final Predicate<String> typesToKeep;
/*    */   
/*    */   public PoiTypeRemoveFix(Schema outputSchema, String name, Predicate<String> typesToRemove) {
/* 13 */     super(outputSchema, name);
/* 14 */     this.typesToKeep = typesToRemove.negate();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 19 */   protected <T> Stream<Dynamic<T>> processRecords(Stream<Dynamic<T>> records) { return records.filter(this::shouldKeepRecord); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   private <T> boolean shouldKeepRecord(Dynamic<T> record) { return record.get("type").asString().result().filter(this.typesToKeep).isPresent(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\PoiTypeRemoveFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
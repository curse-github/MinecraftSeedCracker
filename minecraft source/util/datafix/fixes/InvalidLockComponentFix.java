/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ 
/*    */ public class InvalidLockComponentFix
/*    */   extends DataComponentRemainderFix
/*    */ {
/* 13 */   private static final Optional<String> INVALID_LOCK_CUSTOM_NAME = Optional.of("\"\"");
/*    */ 
/*    */   
/* 16 */   public InvalidLockComponentFix(Schema outputSchema) { super(outputSchema, "InvalidLockComponentPredicateFix", "minecraft:lock"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   protected <T> Dynamic<T> fixComponent(Dynamic<T> input) { return fixLock(input); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static <T> Dynamic<T> fixLock(Dynamic<T> input) { return isBrokenLock(input) ? null : input; }
/*    */ 
/*    */   
/*    */   private static <T> boolean isBrokenLock(Dynamic<T> input) {
/* 29 */     return isMapWithOneField(input, "components", components -> 
/* 30 */         isMapWithOneField(components, "minecraft:custom_name", ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> boolean isMapWithOneField(Dynamic<T> input, String fieldName, Predicate<Dynamic<T>> predicate) {
/* 37 */     Optional<Map<Dynamic<T>, Dynamic<T>>> map = input.getMapValues().result();
/* 38 */     if (map.isEmpty() || ((Map)map.get()).size() != 1) {
/* 39 */       return false;
/*    */     }
/* 41 */     return input.get(fieldName).result().filter(predicate).isPresent();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\InvalidLockComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
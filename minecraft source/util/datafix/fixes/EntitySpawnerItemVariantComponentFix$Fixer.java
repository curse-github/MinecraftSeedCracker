/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Function;
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
/*    */ @FunctionalInterface
/*    */ interface Fixer
/*    */   extends Function<Typed<?>, Typed<?>>
/*    */ {
/* 55 */   default Typed<?> apply(Typed<?> components) { return components.update(DSL.remainderFinder(), this::fixRemainder); }
/*    */ 
/*    */ 
/*    */   
/* 59 */   default <T> Dynamic<T> fixRemainder(Dynamic<T> remainder) { return (Dynamic)remainder.get("minecraft:bucket_entity_data").result().map(bucketData -> fixRemainder(remainder, bucketData)).orElse(remainder); }
/*    */   
/*    */   <T> Dynamic<T> fixRemainder(Dynamic<T> paramDynamic1, Dynamic<T> paramDynamic2);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntitySpawnerItemVariantComponentFix$Fixer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
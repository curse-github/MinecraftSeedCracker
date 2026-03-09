/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class FeatureFlagRemoveFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public FeatureFlagRemoveFix(Schema outputSchema, String name, Set<String> flagsToRemove) {
/* 21 */     super(outputSchema, false);
/* 22 */     this.name = name;
/* 23 */     this.flagsToRemove = flagsToRemove;
/*    */   }
/*    */   private final Set<String> flagsToRemove;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 28 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(References.LIGHTWEIGHT_LEVEL), input -> 
/* 29 */         input.update(DSL.remainderFinder(), this::fixTag));
/*    */   }
/*    */ 
/*    */   
/*    */   private <T> Dynamic<T> fixTag(Dynamic<T> tag) {
/* 34 */     List<Dynamic<T>> inactiveFeatures = (List)tag.get("removed_features").asStream().collect(Collectors.toCollection(java.util.ArrayList::new));
/* 35 */     Dynamic<T> result = tag.update("enabled_features", features -> {
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
/* 46 */           Objects.requireNonNull(tag); return (Dynamic)DataFixUtils.orElse(features.asStreamOpt().result().map(()).map(tag::createList), features);
/*    */         });
/*    */     
/* 49 */     if (!inactiveFeatures.isEmpty()) {
/* 50 */       result = result.set("removed_features", tag.createList(inactiveFeatures.stream()));
/*    */     }
/* 52 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\FeatureFlagRemoveFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
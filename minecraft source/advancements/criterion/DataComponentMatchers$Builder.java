/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.core.component.DataComponentExactPredicate;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.predicates.DataComponentPredicate;
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
/*    */ public class Builder
/*    */ {
/* 53 */   private DataComponentExactPredicate exact = DataComponentExactPredicate.EMPTY;
/* 54 */   private final ImmutableMap.Builder<DataComponentPredicate.Type<?>, DataComponentPredicate> partial = ImmutableMap.builder();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public static Builder components() { return new Builder(); }
/*    */ 
/*    */   
/*    */   public <T extends DataComponentType<?>> Builder any(DataComponentType<?> type) {
/* 64 */     DataComponentPredicate.AnyValueType predicateType = DataComponentPredicate.AnyValueType.create(type);
/* 65 */     this.partial.put(predicateType, predicateType.predicate());
/* 66 */     return this;
/*    */   }
/*    */   
/*    */   public <T extends DataComponentPredicate> Builder partial(DataComponentPredicate.Type<T> type, T predicate) {
/* 70 */     this.partial.put(type, predicate);
/* 71 */     return this;
/*    */   }
/*    */   
/*    */   public Builder exact(DataComponentExactPredicate exact) {
/* 75 */     this.exact = exact;
/* 76 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 80 */   public DataComponentMatchers build() { return new DataComponentMatchers(this.exact, this.partial.buildOrThrow()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\DataComponentMatchers$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
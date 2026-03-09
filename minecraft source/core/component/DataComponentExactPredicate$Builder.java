/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*  99 */   private final List<TypedDataComponent<?>> expectedComponents = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public <T> Builder expect(TypedDataComponent<T> value) { return expect(value.type(), value.value()); }
/*     */ 
/*     */   
/*     */   public <T> Builder expect(DataComponentType<? super T> type, T value) {
/* 109 */     for (TypedDataComponent<?> component : this.expectedComponents) {
/* 110 */       if (component.type() == type) {
/* 111 */         throw new IllegalArgumentException("Predicate already has component of type: '" + String.valueOf(type) + "'");
/*     */       }
/*     */     } 
/* 114 */     this.expectedComponents.add(new TypedDataComponent(type, value));
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 119 */   public DataComponentExactPredicate build() { return new DataComponentExactPredicate(List.copyOf(this.expectedComponents)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\DataComponentExactPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
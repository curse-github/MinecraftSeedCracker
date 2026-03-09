/*    */ package net.minecraft.util.context;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
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
/* 32 */   private final Set<ContextKey<?>> required = Sets.newIdentityHashSet();
/* 33 */   private final Set<ContextKey<?>> optional = Sets.newIdentityHashSet();
/*    */   
/*    */   public Builder required(ContextKey<?> param) {
/* 36 */     if (this.optional.contains(param)) {
/* 37 */       throw new IllegalArgumentException("Parameter " + String.valueOf(param.name()) + " is already optional");
/*    */     }
/* 39 */     this.required.add(param);
/* 40 */     return this;
/*    */   }
/*    */   
/*    */   public Builder optional(ContextKey<?> param) {
/* 44 */     if (this.required.contains(param)) {
/* 45 */       throw new IllegalArgumentException("Parameter " + String.valueOf(param.name()) + " is already required");
/*    */     }
/* 47 */     this.optional.add(param);
/* 48 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 52 */   public ContextKeySet build() { return new ContextKeySet(this.required, this.optional); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\context\ContextKeySet$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
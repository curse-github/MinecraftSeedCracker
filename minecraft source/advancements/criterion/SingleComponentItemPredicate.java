/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*    */ 
/*    */ public interface SingleComponentItemPredicate<T>
/*    */   extends DataComponentPredicate {
/*    */   default boolean matches(DataComponentGetter components) {
/* 10 */     T value = (T)components.get(componentType());
/* 11 */     return (value != null && matches(value));
/*    */   }
/*    */   
/*    */   DataComponentType<T> componentType();
/*    */   
/*    */   boolean matches(T paramT);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\SingleComponentItemPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.core.component;
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
/*    */ public interface DataComponentGetter
/*    */ {
/*    */   <T> T get(DataComponentType<? extends T> paramDataComponentType);
/*    */   
/*    */   default <T> T getOrDefault(DataComponentType<? extends T> type, T defaultValue) {
/* 28 */     T value = (T)get(type);
/* 29 */     return (value != null) ? value : defaultValue;
/*    */   }
/*    */   
/*    */   default <T> TypedDataComponent<T> getTyped(DataComponentType<T> type) {
/* 33 */     T value = (T)get(type);
/* 34 */     return (value != null) ? new TypedDataComponent(type, value) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\DataComponentGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
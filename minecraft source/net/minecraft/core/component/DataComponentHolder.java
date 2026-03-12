/*    */ package net.minecraft.core.component;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface DataComponentHolder
/*    */   extends DataComponentGetter
/*    */ {
/* 12 */   default <T> T get(DataComponentType<? extends T> type) { return (T)getComponents().get(type); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   default <T> Stream<T> getAllOfType(Class<? extends T> valueClass) { return getComponents().stream().map(TypedDataComponent::value).filter(value -> valueClass.isAssignableFrom(value.getClass())).map(value -> value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   default <T> T getOrDefault(DataComponentType<? extends T> type, T defaultValue) { return (T)getComponents().getOrDefault(type, defaultValue); }
/*    */ 
/*    */ 
/*    */   
/* 26 */   default boolean has(DataComponentType<?> type) { return getComponents().has(type); }
/*    */   
/*    */   DataComponentMap getComponents();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\DataComponentHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
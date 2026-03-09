/*    */ package net.minecraft.network.syncher;
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
/*    */ public interface ForValueType<T>
/*    */   extends EntityDataSerializer<T>
/*    */ {
/* 18 */   default T copy(T value) { return value; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\syncher\EntityDataSerializer$ForValueType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
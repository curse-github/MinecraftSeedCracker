/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import net.minecraft.util.context.ContextKey;
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
/*    */ public interface Getter<T, R>
/*    */   extends LootContextArg<R>
/*    */ {
/*    */   R get(T paramT);
/*    */   
/*    */   ContextKey<? extends T> contextParam();
/*    */   
/*    */   default R get(LootContext context) {
/* 43 */     T value = (T)context.getOptionalParameter(contextParam());
/* 44 */     return (R)((value != null) ? get(value) : null);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\LootContextArg$Getter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
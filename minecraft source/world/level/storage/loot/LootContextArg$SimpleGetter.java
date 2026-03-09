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
/*    */ public interface SimpleGetter<T>
/*    */   extends LootContextArg<T>
/*    */ {
/*    */   ContextKey<? extends T> contextParam();
/*    */   
/* 54 */   default T get(LootContext context) { return (T)context.getOptionalParameter(contextParam()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\LootContextArg$SimpleGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
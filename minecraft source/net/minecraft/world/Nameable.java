/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ 
/*    */ public interface Nameable
/*    */ {
/*    */   Component getName();
/*    */   
/* 10 */   default String getPlainTextName() { return getName().getString(); }
/*    */ 
/*    */ 
/*    */   
/* 14 */   default boolean hasCustomName() { return (getCustomName() != null); }
/*    */ 
/*    */ 
/*    */   
/* 18 */   default Component getDisplayName() { return getName(); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   default Component getCustomName() { return null; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\Nameable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*   */ package net.minecraft.core;
/*   */ 
/*   */ public interface HolderOwner<T>
/*   */ {
/* 5 */   default boolean canSerializeIn(HolderOwner<T> context) { return (context == this); }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\HolderOwner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
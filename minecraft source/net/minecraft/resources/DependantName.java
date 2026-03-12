/*   */ package net.minecraft.resources;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface DependantName<T, V>
/*   */ {
/*   */   V get(ResourceKey<T> paramResourceKey);
/*   */   
/* 8 */   static <T, V> DependantName<T, V> fixed(V value) { return id -> value; }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\resources\DependantName.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
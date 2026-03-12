/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class RegistryEntry<T>
/*    */   extends Record
/*    */ {
/*    */   private final ResourceKey<? extends Registry<T>> key;
/*    */   private final Registry<T> value;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistryAccess$RegistryEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/RegistryAccess$RegistryEntry;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/core/RegistryAccess$RegistryEntry<TT;>; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistryAccess$RegistryEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/RegistryAccess$RegistryEntry;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/core/RegistryAccess$RegistryEntry<TT;>; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistryAccess$RegistryEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/RegistryAccess$RegistryEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/core/RegistryAccess$RegistryEntry<TT;>; }
/*    */   
/* 25 */   public RegistryEntry(ResourceKey<? extends Registry<T>> key, Registry<T> value) { this.key = key; this.value = value; } public ResourceKey<? extends Registry<T>> key() { return this.key; } public Registry<T> value() { return this.value; }
/*    */   
/* 27 */   private static <T, R extends Registry<? extends T>> RegistryEntry<T> fromMapEntry(Map.Entry<? extends ResourceKey<? extends Registry<?>>, R> e) { return fromUntyped((ResourceKey)e.getKey(), (Registry)e.getValue()); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   private static <T> RegistryEntry<T> fromUntyped(ResourceKey<? extends Registry<?>> key, Registry<?> value) { return new RegistryEntry(key, value); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   private RegistryEntry<T> freeze() { return new RegistryEntry(this.key, this.value.freeze()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\RegistryAccess$RegistryEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
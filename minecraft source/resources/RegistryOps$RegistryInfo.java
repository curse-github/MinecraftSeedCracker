/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.HolderOwner;
/*    */ 
/*    */ public final class RegistryInfo<T>
/*    */   extends Record {
/*    */   private final HolderOwner<T> owner;
/*    */   private final HolderGetter<T> getter;
/*    */   private final Lifecycle elementsLifecycle;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/resources/RegistryOps$RegistryInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo<TT;>; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/RegistryOps$RegistryInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo<TT;>; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/resources/RegistryOps$RegistryInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo<TT;>; }
/*    */   
/* 20 */   public RegistryInfo(HolderOwner<T> owner, HolderGetter<T> getter, Lifecycle elementsLifecycle) { this.owner = owner; this.getter = getter; this.elementsLifecycle = elementsLifecycle; } public HolderOwner<T> owner() { return this.owner; } public HolderGetter<T> getter() { return this.getter; } public Lifecycle elementsLifecycle() { return this.elementsLifecycle; }
/*    */   
/* 22 */   public static <T> RegistryInfo<T> fromRegistryLookup(HolderLookup.RegistryLookup<T> registry) { return new RegistryInfo(registry, registry, registry.registryLifecycle()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\resources\RegistryOps$RegistryInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
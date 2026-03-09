/*    */ package net.minecraft.server.packs.metadata;
/*    */ 
/*    */ public final class MetadataSectionType<T> extends Record {
/*    */   private final String name;
/*    */   private final Codec<T> codec;
/*    */   
/*  7 */   public MetadataSectionType(String name, Codec<T> codec) { this.name = name; this.codec = codec; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/metadata/MetadataSectionType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  7 */     //   0	7	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType<TT;>; } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/metadata/MetadataSectionType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/metadata/MetadataSectionType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  7 */     //   0	8	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType<TT;>; } public Codec<T> codec() { return this.codec; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 13 */   public WithValue<T> withValue(T value) { return new WithValue(this, value); }
/*    */   public static final class WithValue<T> extends Record { private final MetadataSectionType<T> type; private final T value;
/*    */     
/* 16 */     public WithValue(MetadataSectionType<T> type, T value) { this.type = type; this.value = value; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue<TT;>; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 16 */       //   0	8	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue<TT;>; } public MetadataSectionType<T> type() { return this.type; } public T value() { return (T)this.value; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 22 */     public <U> Optional<U> unwrapToType(MetadataSectionType<U> type) { return (type == this.type) ? Optional.of(this.value) : Optional.empty(); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\metadata\MetadataSectionType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
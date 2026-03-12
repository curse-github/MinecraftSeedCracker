/*    */ package net.minecraft.server.jsonrpc.api;public final class SchemaComponent<T> extends Record {
/*    */   private final String name;
/*    */   private final URI ref;
/*    */   private final Schema<T> schema;
/*    */   
/*  6 */   public SchemaComponent(String name, URI ref, Schema<T> schema) { this.name = name; this.ref = ref; this.schema = schema; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/api/SchemaComponent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/SchemaComponent;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  6 */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/SchemaComponent<TT;>; } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/api/SchemaComponent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/SchemaComponent;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/SchemaComponent<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/api/SchemaComponent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/SchemaComponent;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  6 */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/SchemaComponent<TT;>; } public URI ref() { return this.ref; } public Schema<T> schema() { return this.schema; }
/*    */   
/*  8 */   public Schema<T> asRef() { return Schema.ofRef(this.ref, this.schema.codec()); }
/*    */ 
/*    */ 
/*    */   
/* 12 */   public Schema<List<T>> asArray() { return Schema.arrayOf(asRef(), this.schema.codec()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\api\SchemaComponent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
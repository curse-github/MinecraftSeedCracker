/*    */ package net.minecraft.server.jsonrpc.api;
/*    */ public final class ParamInfo<Param> extends Record {
/*    */   private final String name;
/*    */   private final Schema<Param> schema;
/*    */   private final boolean required;
/*    */   
/*  7 */   public ParamInfo(String name, Schema<Param> schema, boolean required) { this.name = name; this.schema = schema; this.required = required; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/api/ParamInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  7 */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo<TParam;>; } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/api/ParamInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo<TParam;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/api/ParamInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  7 */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo<TParam;>; } public Schema<Param> schema() { return this.schema; } public boolean required() { return this.required; }
/*    */   
/*  9 */   public static <Param> MapCodec<ParamInfo<Param>> typedCodec() { return RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING
/* 10 */           .fieldOf("name").forGetter(ParamInfo::name), 
/* 11 */           Schema.typedCodec().fieldOf("schema").forGetter(ParamInfo::schema), Codec.BOOL
/* 12 */           .fieldOf("required").forGetter(ParamInfo::required))
/* 13 */         .apply(i, ParamInfo::new)); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public ParamInfo(String name, Schema<Param> schema) { this(name, schema, true); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\api\ParamInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
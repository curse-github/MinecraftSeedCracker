/*    */ package net.minecraft.server.jsonrpc.api;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class MethodInfo<Params, Result> extends Record {
/*    */   private final String description;
/*    */   private final Optional<ParamInfo<Params>> params;
/*    */   private final Optional<ResultInfo<Result>> result;
/*    */   
/* 12 */   public MethodInfo(String description, Optional<ParamInfo<Params>> params, Optional<ResultInfo<Result>> result) { this.description = description; this.params = params; this.result = result; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/api/MethodInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo<TParams;TResult;>; } public String description() { return this.description; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/api/MethodInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo<TParams;TResult;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/api/MethodInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo<TParams;TResult;>; } public Optional<ParamInfo<Params>> params() { return this.params; } public Optional<ResultInfo<Result>> result() { return this.result; }
/*    */ 
/*    */   
/* 15 */   private static <Params> Optional<ParamInfo<Params>> toOptional(List<ParamInfo<Params>> list) { return list.isEmpty() ? Optional.empty() : Optional.of((ParamInfo)list.getFirst()); }
/*    */ 
/*    */ 
/*    */   
/*    */   private static <Params> List<ParamInfo<Params>> toList(Optional<ParamInfo<Params>> opt) {
/* 20 */     if (opt.isPresent()) {
/* 21 */       return List.of((ParamInfo)opt.get());
/*    */     }
/* 23 */     return List.of();
/*    */   }
/*    */ 
/*    */   
/* 27 */   private static <Params> Codec<Optional<ParamInfo<Params>>> paramsTypedCodec() { return ParamInfo.typedCodec().codec().listOf().xmap(MethodInfo::toOptional, MethodInfo::toList); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   private static <Params, Result> MapCodec<MethodInfo<Params, Result>> typedCodec() { return RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING
/* 35 */           .fieldOf("description").forGetter(MethodInfo::description), 
/* 36 */           paramsTypedCodec().fieldOf("params").forGetter(MethodInfo::params), 
/* 37 */           ResultInfo.typedCodec().optionalFieldOf("result").forGetter(MethodInfo::result))
/* 38 */         .apply(i, MethodInfo::new)); }
/*    */ 
/*    */   
/*    */   public MethodInfo(String description, ParamInfo<Params> paramInfo, ResultInfo<Result> resultInfo) {
/* 42 */     this(description, 
/*    */         
/* 44 */         Optional.ofNullable(paramInfo), 
/* 45 */         Optional.ofNullable(resultInfo));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public Named<Params, Result> named(Identifier name) { return new Named(name, this); }
/*    */   public static final class Named<Params, Result> extends Record { private final Identifier name; private final MethodInfo<Params, Result> contents;
/*    */     
/* 53 */     public Named(Identifier name, MethodInfo<Params, Result> contents) { this.name = name; this.contents = contents; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named<TParams;TResult;>; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named<TParams;TResult;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 53 */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named<TParams;TResult;>; } public Identifier name() { return this.name; } public MethodInfo<Params, Result> contents() { return this.contents; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 58 */     public static <Params, Result> Codec<Named<Params, Result>> typedCodec() { return RecordCodecBuilder.create(i -> i.group(Identifier.CODEC
/* 59 */             .fieldOf("name").forGetter(Named::name), 
/* 60 */             MethodInfo.typedCodec().forGetter(Named::contents))
/* 61 */           .apply(i, Named::new)); }
/*    */ 
/*    */ 
/*    */     
/* 65 */     public static final Codec<Named<?, ?>> CODEC = typedCodec(); }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\api\MethodInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
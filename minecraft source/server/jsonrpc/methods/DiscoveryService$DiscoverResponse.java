/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.jsonrpc.api.MethodInfo;
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
/*    */ public final class DiscoverResponse
/*    */   extends Record
/*    */ {
/*    */   private final String jsonRpcProtocolVersion;
/*    */   private final DiscoveryService.DiscoverInfo discoverInfo;
/*    */   private final List<MethodInfo.Named<?, ?>> methods;
/*    */   private final DiscoveryService.DiscoverComponents components;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 42 */   public DiscoverResponse(String jsonRpcProtocolVersion, DiscoveryService.DiscoverInfo discoverInfo, List<MethodInfo.Named<?, ?>> methods, DiscoveryService.DiscoverComponents components) { this.jsonRpcProtocolVersion = jsonRpcProtocolVersion; this.discoverInfo = discoverInfo; this.methods = methods; this.components = components; } public String jsonRpcProtocolVersion() { return this.jsonRpcProtocolVersion; } public DiscoveryService.DiscoverInfo discoverInfo() { return this.discoverInfo; } public List<MethodInfo.Named<?, ?>> methods() { return this.methods; } public DiscoveryService.DiscoverComponents components() { return this.components; }
/* 43 */   public static final MapCodec<DiscoverResponse> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING
/* 44 */         .fieldOf("openrpc").forGetter(DiscoverResponse::jsonRpcProtocolVersion), DiscoveryService.DiscoverInfo.CODEC
/* 45 */         .codec().fieldOf("info").forGetter(DiscoverResponse::discoverInfo), 
/* 46 */         Codec.list(MethodInfo.Named.CODEC).fieldOf("methods").forGetter(DiscoverResponse::methods), DiscoveryService.DiscoverComponents.CODEC
/* 47 */         .codec().fieldOf("components").forGetter(DiscoverResponse::components))
/* 48 */       .apply(i, DiscoverResponse::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\methods\DiscoveryService$DiscoverResponse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
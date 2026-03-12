/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.time.Instant;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.players.IpBanListEntry;
/*    */ import net.minecraft.util.ExtraCodecs;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class IpBanDto
/*    */   extends Record
/*    */ {
/*    */   private final String ip;
/*    */   private final Optional<String> reason;
/*    */   private final Optional<String> source;
/*    */   private final Optional<Instant> expires;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 48 */   public IpBanDto(String ip, Optional<String> reason, Optional<String> source, Optional<Instant> expires) { this.ip = ip; this.reason = reason; this.source = source; this.expires = expires; } public String ip() { return this.ip; } public Optional<String> reason() { return this.reason; } public Optional<String> source() { return this.source; } public Optional<Instant> expires() { return this.expires; }
/* 49 */   public static final MapCodec<IpBanDto> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING
/* 50 */         .fieldOf("ip").forGetter(IpBanDto::ip), Codec.STRING
/* 51 */         .optionalFieldOf("reason").forGetter(IpBanDto::reason), Codec.STRING
/* 52 */         .optionalFieldOf("source").forGetter(IpBanDto::source), ExtraCodecs.INSTANT_ISO8601
/* 53 */         .optionalFieldOf("expires").forGetter(IpBanDto::expires))
/* 54 */       .apply(i, IpBanDto::new));
/*    */ 
/*    */   
/* 57 */   private static IpBanDto from(IpBanlistService.IpBan ban) { return new IpBanDto(ban.ip(), Optional.ofNullable(ban.reason()), Optional.of(ban.source()), ban.expires()); }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public static IpBanDto from(IpBanListEntry ban) { return from(IpBanlistService.IpBan.from(ban)); }
/*    */ 
/*    */ 
/*    */   
/* 65 */   private IpBanlistService.IpBan toIpBan() { return new IpBanlistService.IpBan(ip(), (String)reason().orElse(null), (String)source().orElse("Management server"), expires()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\methods\IpBanlistService$IpBanDto.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
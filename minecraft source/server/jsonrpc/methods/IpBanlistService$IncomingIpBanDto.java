/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ import com.google.common.net.InetAddresses;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.time.Instant;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class IncomingIpBanDto extends Record {
/*    */   private final Optional<PlayerDto> player;
/*    */   private final Optional<String> ip;
/*    */   private final Optional<String> reason;
/*    */   private final Optional<String> source;
/*    */   private final Optional<Instant> expires;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 27 */   public IncomingIpBanDto(Optional<PlayerDto> player, Optional<String> ip, Optional<String> reason, Optional<String> source, Optional<Instant> expires) { this.player = player; this.ip = ip; this.reason = reason; this.source = source; this.expires = expires; } public Optional<PlayerDto> player() { return this.player; } public Optional<String> ip() { return this.ip; } public Optional<String> reason() { return this.reason; } public Optional<String> source() { return this.source; } public Optional<Instant> expires() { return this.expires; }
/* 28 */   public static final MapCodec<IncomingIpBanDto> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(PlayerDto.CODEC
/* 29 */         .codec().optionalFieldOf("player").forGetter(IncomingIpBanDto::player), Codec.STRING
/* 30 */         .optionalFieldOf("ip").forGetter(IncomingIpBanDto::ip), Codec.STRING
/* 31 */         .optionalFieldOf("reason").forGetter(IncomingIpBanDto::reason), Codec.STRING
/* 32 */         .optionalFieldOf("source").forGetter(IncomingIpBanDto::source), ExtraCodecs.INSTANT_ISO8601
/* 33 */         .optionalFieldOf("expires").forGetter(IncomingIpBanDto::expires))
/* 34 */       .apply(i, IncomingIpBanDto::new));
/*    */ 
/*    */   
/* 37 */   private IpBanlistService.IpBan toIpBan(ServerPlayer player) { return new IpBanlistService.IpBan(player.getIpAddress(), (String)reason().orElse(null), (String)source().orElse("Management server"), expires()); }
/*    */ 
/*    */   
/*    */   private IpBanlistService.IpBan toIpBan() {
/* 41 */     if (ip().isEmpty() || !InetAddresses.isInetAddress((String)ip().get())) {
/* 42 */       return null;
/*    */     }
/* 44 */     return new IpBanlistService.IpBan((String)ip().get(), (String)reason().orElse(null), (String)source().orElse("Management server"), expires());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\methods\IpBanlistService$IncomingIpBanDto.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
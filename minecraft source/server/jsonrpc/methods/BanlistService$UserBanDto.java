/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.time.Instant;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.UserBanListEntry;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class UserBanDto
/*    */   extends Record {
/*    */   private final PlayerDto player;
/*    */   private final Optional<String> reason;
/*    */   private final Optional<String> source;
/*    */   private final Optional<Instant> expires;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 28 */   public UserBanDto(PlayerDto player, Optional<String> reason, Optional<String> source, Optional<Instant> expires) { this.player = player; this.reason = reason; this.source = source; this.expires = expires; } public PlayerDto player() { return this.player; } public Optional<String> reason() { return this.reason; } public Optional<String> source() { return this.source; } public Optional<Instant> expires() { return this.expires; }
/* 29 */   public static final MapCodec<UserBanDto> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(PlayerDto.CODEC
/* 30 */         .codec().fieldOf("player").forGetter(UserBanDto::player), Codec.STRING
/* 31 */         .optionalFieldOf("reason").forGetter(UserBanDto::reason), Codec.STRING
/* 32 */         .optionalFieldOf("source").forGetter(UserBanDto::source), ExtraCodecs.INSTANT_ISO8601
/* 33 */         .optionalFieldOf("expires").forGetter(UserBanDto::expires))
/* 34 */       .apply(i, UserBanDto::new));
/*    */ 
/*    */   
/* 37 */   private static UserBanDto from(BanlistService.UserBan ban) { return new UserBanDto(PlayerDto.from(ban.player()), Optional.ofNullable(ban.reason()), Optional.of(ban.source()), ban.expires()); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static UserBanDto from(UserBanListEntry entry) { return from(BanlistService.UserBan.from(entry)); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   private BanlistService.UserBan toUserBan(NameAndId nameAndId) { return new BanlistService.UserBan(nameAndId, (String)reason().orElse(null), (String)source().orElse("Management server"), expires()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\methods\BanlistService$UserBanDto.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
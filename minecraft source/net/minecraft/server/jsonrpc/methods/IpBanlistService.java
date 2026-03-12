/*     */ package net.minecraft.server.jsonrpc.methods;
/*     */ import com.google.common.net.InetAddresses;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.time.Instant;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.players.IpBanListEntry;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public class IpBanlistService {
/*     */   private static final String BAN_SOURCE = "Management server";
/*     */   
/*     */   public static final class IncomingIpBanDto extends Record {
/*     */     private final Optional<PlayerDto> player;
/*     */     private final Optional<String> ip;
/*     */     
/*  27 */     public IncomingIpBanDto(Optional<PlayerDto> player, Optional<String> ip, Optional<String> reason, Optional<String> source, Optional<Instant> expires) { this.player = player; this.ip = ip; this.reason = reason; this.source = source; this.expires = expires; } private final Optional<String> reason; private final Optional<String> source; private final Optional<Instant> expires; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto;
/*  27 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<PlayerDto> player() { return this.player; } public Optional<String> ip() { return this.ip; } public Optional<String> reason() { return this.reason; } public Optional<String> source() { return this.source; } public Optional<Instant> expires() { return this.expires; }
/*  28 */     public static final MapCodec<IncomingIpBanDto> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(PlayerDto.CODEC
/*  29 */           .codec().optionalFieldOf("player").forGetter(IncomingIpBanDto::player), Codec.STRING
/*  30 */           .optionalFieldOf("ip").forGetter(IncomingIpBanDto::ip), Codec.STRING
/*  31 */           .optionalFieldOf("reason").forGetter(IncomingIpBanDto::reason), Codec.STRING
/*  32 */           .optionalFieldOf("source").forGetter(IncomingIpBanDto::source), ExtraCodecs.INSTANT_ISO8601
/*  33 */           .optionalFieldOf("expires").forGetter(IncomingIpBanDto::expires))
/*  34 */         .apply(i, IncomingIpBanDto::new));
/*     */ 
/*     */     
/*  37 */     private IpBanlistService.IpBan toIpBan(ServerPlayer player) { return new IpBanlistService.IpBan(player.getIpAddress(), (String)reason().orElse(null), (String)source().orElse("Management server"), expires()); }
/*     */ 
/*     */     
/*     */     private IpBanlistService.IpBan toIpBan() {
/*  41 */       if (ip().isEmpty() || !InetAddresses.isInetAddress((String)ip().get())) {
/*  42 */         return null;
/*     */       }
/*  44 */       return new IpBanlistService.IpBan((String)ip().get(), (String)reason().orElse(null), (String)source().orElse("Management server"), expires());
/*     */     } }
/*     */   public static final class IpBanDto extends Record { private final String ip; private final Optional<String> reason; private final Optional<String> source; private final Optional<Instant> expires;
/*     */     
/*  48 */     public IpBanDto(String ip, Optional<String> reason, Optional<String> source, Optional<Instant> expires) { this.ip = ip; this.reason = reason; this.source = source; this.expires = expires; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto;
/*  48 */       //   0	8	1	o	Ljava/lang/Object; } public String ip() { return this.ip; } public Optional<String> reason() { return this.reason; } public Optional<String> source() { return this.source; } public Optional<Instant> expires() { return this.expires; }
/*  49 */     public static final MapCodec<IpBanDto> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING
/*  50 */           .fieldOf("ip").forGetter(IpBanDto::ip), Codec.STRING
/*  51 */           .optionalFieldOf("reason").forGetter(IpBanDto::reason), Codec.STRING
/*  52 */           .optionalFieldOf("source").forGetter(IpBanDto::source), ExtraCodecs.INSTANT_ISO8601
/*  53 */           .optionalFieldOf("expires").forGetter(IpBanDto::expires))
/*  54 */         .apply(i, IpBanDto::new));
/*     */ 
/*     */     
/*  57 */     private static IpBanDto from(IpBanlistService.IpBan ban) { return new IpBanDto(ban.ip(), Optional.ofNullable(ban.reason()), Optional.of(ban.source()), ban.expires()); }
/*     */ 
/*     */ 
/*     */     
/*  61 */     public static IpBanDto from(IpBanListEntry ban) { return from(IpBanlistService.IpBan.from(ban)); }
/*     */ 
/*     */ 
/*     */     
/*  65 */     private IpBanlistService.IpBan toIpBan() { return new IpBanlistService.IpBan(ip(), (String)reason().orElse(null), (String)source().orElse("Management server"), expires()); } }
/*     */   private static final class IpBan extends Record { private final String ip; private final String reason; private final String source;
/*     */     private final Optional<Instant> expires;
/*     */     
/*  69 */     private IpBan(String ip, String reason, String source, Optional<Instant> expires) { this.ip = ip; this.reason = reason; this.source = source; this.expires = expires; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBan;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBan; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBan;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBan; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBan;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBan;
/*  69 */       //   0	8	1	o	Ljava/lang/Object; } public String ip() { return this.ip; } public String reason() { return this.reason; } public String source() { return this.source; } public Optional<Instant> expires() { return this.expires; }
/*     */ 
/*     */     
/*  72 */     private static IpBan from(IpBanListEntry entry) { return new IpBan((String)Objects.requireNonNull((String)entry.getUser()), entry.getReason(), entry.getSource(), Optional.ofNullable(entry.getExpires()).map(Date::toInstant)); }
/*     */ 
/*     */ 
/*     */     
/*  76 */     private IpBanListEntry toIpBanEntry() { return new IpBanListEntry(ip(), null, source(), (Date)expires().map(Date::from).orElse(null), reason()); } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public static List<IpBanDto> get(MinecraftApi minecraftApi) { return minecraftApi.banListService().getIpBanEntries().stream()
/*  82 */       .map(IpBan::from)
/*  83 */       .map(IpBanDto::from)
/*  84 */       .toList(); }
/*     */ 
/*     */   
/*     */   public static List<IpBanDto> add(MinecraftApi minecraftApi, List<IncomingIpBanDto> bans, ClientInfo clientInfo) {
/*  88 */     bans.stream()
/*  89 */       .map(ban -> banIp(minecraftApi, ban, clientInfo))
/*  90 */       .flatMap(Collection::stream)
/*  91 */       .forEach(player -> player.connection.disconnect(Component.translatable("multiplayer.disconnect.ip_banned")));
/*  92 */     return get(minecraftApi);
/*     */   }
/*     */   
/*     */   private static List<ServerPlayer> banIp(MinecraftApi minecraftApi, IncomingIpBanDto ban, ClientInfo clientInfo) {
/*  96 */     IpBan ipBan = ban.toIpBan();
/*  97 */     if (ipBan != null)
/*  98 */       return banIp(minecraftApi, ipBan, clientInfo); 
/*  99 */     if (ban.player().isPresent()) {
/* 100 */       Optional<ServerPlayer> player = minecraftApi.playerListService().getPlayer(((PlayerDto)ban.player().get()).id(), ((PlayerDto)ban.player().get()).name());
/* 101 */       if (player.isPresent()) {
/* 102 */         return banIp(minecraftApi, ban.toIpBan((ServerPlayer)player.get()), clientInfo);
/*     */       }
/*     */     } 
/* 105 */     return List.of();
/*     */   }
/*     */   
/*     */   private static List<ServerPlayer> banIp(MinecraftApi minecraftApi, IpBan ban, ClientInfo clientInfo) {
/* 109 */     minecraftApi.banListService().addIpBan(ban.toIpBanEntry(), clientInfo);
/* 110 */     return minecraftApi.playerListService().getPlayersWithAddress(ban.ip());
/*     */   }
/*     */   
/*     */   public static List<IpBanDto> clear(MinecraftApi minecraftApi, ClientInfo clientInfo) {
/* 114 */     minecraftApi.banListService().clearIpBans(clientInfo);
/* 115 */     return get(minecraftApi);
/*     */   }
/*     */   
/*     */   public static List<IpBanDto> remove(MinecraftApi minecraftApi, List<String> ban, ClientInfo clientInfo) {
/* 119 */     ban.forEach(ip -> minecraftApi.banListService().removeIpBan(ip, clientInfo));
/* 120 */     return get(minecraftApi);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<IpBanDto> set(MinecraftApi minecraftApi, List<IpBanDto> ips, ClientInfo clientInfo) {
/* 127 */     Set<IpBan> finalBanlist = (Set)ips.stream().filter(ban -> InetAddresses.isInetAddress(ban.ip())).map(IpBanDto::toIpBan).collect(Collectors.toSet());
/*     */ 
/*     */ 
/*     */     
/* 131 */     Set<IpBan> currentBans = (Set)minecraftApi.banListService().getIpBanEntries().stream().map(IpBan::from).collect(Collectors.toSet());
/*     */     
/* 133 */     currentBans.stream()
/* 134 */       .filter(ban -> !finalBanlist.contains(ban))
/* 135 */       .forEach(ban -> minecraftApi.banListService().removeIpBan(ban.ip(), clientInfo));
/*     */     
/* 137 */     finalBanlist.stream()
/* 138 */       .filter(ban -> !currentBans.contains(ban))
/* 139 */       .forEach(ban -> minecraftApi.banListService().addIpBan(ban.toIpBanEntry(), clientInfo));
/*     */     
/* 141 */     finalBanlist.stream()
/* 142 */       .filter(ban -> !currentBans.contains(ban))
/* 143 */       .flatMap(ban -> minecraftApi.playerListService().getPlayersWithAddress(ban.ip()).stream())
/* 144 */       .forEach(player -> player.connection.disconnect(Component.translatable("multiplayer.disconnect.ip_banned")));
/*     */     
/* 146 */     return get(minecraftApi);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\methods\IpBanlistService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
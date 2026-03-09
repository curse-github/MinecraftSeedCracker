/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*    */ import net.minecraft.server.permissions.PermissionLevel;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.ServerOpListEntry;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class OperatorService {
/*    */   public static final class OperatorDto extends Record {
/*    */     private final PlayerDto player;
/*    */     private final Optional<PermissionLevel> permissionLevel;
/*    */     private final Optional<Boolean> bypassesPlayerLimit;
/*    */     
/* 22 */     public OperatorDto(PlayerDto player, Optional<PermissionLevel> permissionLevel, Optional<Boolean> bypassesPlayerLimit) { this.player = player; this.permissionLevel = permissionLevel; this.bypassesPlayerLimit = bypassesPlayerLimit; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 22 */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto; } public PlayerDto player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto;
/* 22 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<PermissionLevel> permissionLevel() { return this.permissionLevel; } public Optional<Boolean> bypassesPlayerLimit() { return this.bypassesPlayerLimit; }
/* 23 */     public static final MapCodec<OperatorDto> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(PlayerDto.CODEC
/* 24 */           .codec().fieldOf("player").forGetter(OperatorDto::player), PermissionLevel.INT_CODEC
/* 25 */           .optionalFieldOf("permissionLevel").forGetter(OperatorDto::permissionLevel), Codec.BOOL
/* 26 */           .optionalFieldOf("bypassesPlayerLimit").forGetter(OperatorDto::bypassesPlayerLimit))
/* 27 */         .apply(i, OperatorDto::new));
/*    */     
/*    */     public static OperatorDto from(ServerOpListEntry serverOpListEntry) {
/* 30 */       return new OperatorDto(PlayerDto.from(
/* 31 */             (NameAndId)Objects.requireNonNull((NameAndId)serverOpListEntry.getUser())), 
/* 32 */           Optional.of(serverOpListEntry.permissions().level()), 
/* 33 */           Optional.of(Boolean.valueOf(serverOpListEntry.getBypassesPlayerLimit())));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 38 */   public static List<OperatorDto> get(MinecraftApi minecraftApi) { return minecraftApi.operatorListService().getEntries().stream()
/* 39 */       .filter(u -> (u.getUser() != null))
/* 40 */       .map(OperatorDto::from)
/* 41 */       .toList(); }
/*    */ 
/*    */   
/*    */   public static List<OperatorDto> clear(MinecraftApi minecraftApi, ClientInfo clientInfo) {
/* 45 */     minecraftApi.operatorListService().clear(clientInfo);
/* 46 */     return get(minecraftApi);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<OperatorDto> remove(MinecraftApi minecraftApi, List<PlayerDto> playerDtos, ClientInfo clientInfo) {
/* 52 */     List<CompletableFuture<Optional<NameAndId>>> fetch = playerDtos.stream().map(playerDto -> minecraftApi.playerListService().getUser(playerDto.id(), playerDto.name())).toList();
/*    */     
/* 54 */     for (Optional<NameAndId> user : (List)Util.sequence(fetch).join()) {
/* 55 */       user.ifPresent(nameAndId -> minecraftApi.operatorListService().deop(nameAndId, clientInfo));
/*    */     }
/* 57 */     return get(minecraftApi);
/*    */   }
/*    */   static final class Op extends Record { private final NameAndId user; private final Optional<PermissionLevel> permissionLevel; private final Optional<Boolean> bypassesPlayerLimit;
/* 60 */     Op(NameAndId user, Optional<PermissionLevel> permissionLevel, Optional<Boolean> bypassesPlayerLimit) { this.user = user; this.permissionLevel = permissionLevel; this.bypassesPlayerLimit = bypassesPlayerLimit; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/OperatorService$Op;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #60	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/OperatorService$Op; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/OperatorService$Op;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #60	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/OperatorService$Op; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/OperatorService$Op;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #60	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/OperatorService$Op;
/* 60 */       //   0	8	1	o	Ljava/lang/Object; } public NameAndId user() { return this.user; } public Optional<PermissionLevel> permissionLevel() { return this.permissionLevel; } public Optional<Boolean> bypassesPlayerLimit() { return this.bypassesPlayerLimit; } }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<OperatorDto> add(MinecraftApi minecraftApi, List<OperatorDto> operators, ClientInfo clientInfo) {
/* 66 */     List<CompletableFuture<Optional<Op>>> fetch = operators.stream().map(operator -> minecraftApi.playerListService().getUser(operator.player().id(), operator.player().name()).thenApply(())).toList();
/*    */     
/* 68 */     for (Optional<Op> op : (List)Util.sequence(fetch).join()) {
/* 69 */       op.ifPresent(operator -> minecraftApi.operatorListService().op(operator.user(), operator.permissionLevel(), operator.bypassesPlayerLimit(), clientInfo));
/*    */     }
/* 71 */     return get(minecraftApi);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<OperatorDto> set(MinecraftApi minecraftApi, List<OperatorDto> operators, ClientInfo clientInfo) {
/* 78 */     List<CompletableFuture<Optional<Op>>> fetch = operators.stream().map(operator -> minecraftApi.playerListService().getUser(operator.player().id(), operator.player().name()).thenApply(())).toList();
/*    */ 
/*    */ 
/*    */     
/* 82 */     Set<Op> finalOperators = (Set)((List)Util.sequence(fetch).join()).stream().flatMap(Optional::stream).collect(Collectors.toSet());
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 87 */     Set<Op> currentOperators = (Set)minecraftApi.operatorListService().getEntries().stream().filter(entry -> (entry.getUser() != null)).map(entry -> new Op((NameAndId)entry.getUser(), Optional.of(entry.permissions().level()), Optional.of(Boolean.valueOf(entry.getBypassesPlayerLimit())))).collect(Collectors.toSet());
/*    */     
/* 89 */     currentOperators.stream()
/* 90 */       .filter(operator -> !finalOperators.contains(operator))
/* 91 */       .forEach(operator -> minecraftApi.operatorListService().deop(operator.user(), clientInfo));
/*    */     
/* 93 */     finalOperators.stream()
/* 94 */       .filter(operator -> !currentOperators.contains(operator))
/* 95 */       .forEach(operator -> minecraftApi.operatorListService().op(operator.user(), operator.permissionLevel(), operator.bypassesPlayerLimit(), clientInfo));
/*    */     
/* 97 */     return get(minecraftApi);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\methods\OperatorService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
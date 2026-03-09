/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.StoredUserEntry;
/*    */ import net.minecraft.server.players.UserWhiteListEntry;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ 
/*    */ public class AllowlistService
/*    */ {
/* 18 */   public static List<PlayerDto> get(MinecraftApi minecraftApi) { return minecraftApi.allowListService().getEntries().stream()
/* 19 */       .filter(p -> (p.getUser() != null))
/* 20 */       .map(u -> PlayerDto.from((NameAndId)u.getUser()))
/* 21 */       .toList(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<PlayerDto> add(MinecraftApi minecraftApi, List<PlayerDto> playerDtos, ClientInfo clientInfo) {
/* 27 */     List<CompletableFuture<Optional<NameAndId>>> fetch = playerDtos.stream().map(playerDto -> minecraftApi.playerListService().getUser(playerDto.id(), playerDto.name())).toList();
/*    */     
/* 29 */     for (Optional<NameAndId> user : (List)Util.sequence(fetch).join()) {
/* 30 */       user.ifPresent(nameAndId -> minecraftApi.allowListService().add(new UserWhiteListEntry(nameAndId), clientInfo));
/*    */     }
/*    */     
/* 33 */     return get(minecraftApi);
/*    */   }
/*    */   
/*    */   public static List<PlayerDto> clear(MinecraftApi minecraftApi, ClientInfo clientInfo) {
/* 37 */     minecraftApi.allowListService().clear(clientInfo);
/* 38 */     return get(minecraftApi);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<PlayerDto> remove(MinecraftApi minecraftApi, List<PlayerDto> playerDtos, ClientInfo clientInfo) {
/* 44 */     List<CompletableFuture<Optional<NameAndId>>> fetch = playerDtos.stream().map(playerDto -> minecraftApi.playerListService().getUser(playerDto.id(), playerDto.name())).toList();
/*    */     
/* 46 */     for (Optional<NameAndId> user : (List)Util.sequence(fetch).join()) {
/* 47 */       user.ifPresent(nameAndId -> minecraftApi.allowListService().remove(nameAndId, clientInfo));
/*    */     }
/* 49 */     minecraftApi.allowListService().kickUnlistedPlayers(clientInfo);
/* 50 */     return get(minecraftApi);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<PlayerDto> set(MinecraftApi minecraftApi, List<PlayerDto> playerDtos, ClientInfo clientInfo) {
/* 56 */     List<CompletableFuture<Optional<NameAndId>>> fetch = playerDtos.stream().map(playerDto -> minecraftApi.playerListService().getUser(playerDto.id(), playerDto.name())).toList();
/*    */ 
/*    */ 
/*    */     
/* 60 */     Set<NameAndId> finalAllowList = (Set)((List)Util.sequence(fetch).join()).stream().flatMap(Optional::stream).collect(Collectors.toSet());
/*    */ 
/*    */ 
/*    */     
/* 64 */     Set<NameAndId> currentAllowList = (Set)minecraftApi.allowListService().getEntries().stream().map(StoredUserEntry::getUser).collect(Collectors.toSet());
/*    */     
/* 66 */     currentAllowList.stream()
/* 67 */       .filter(user -> !finalAllowList.contains(user))
/* 68 */       .forEach(user -> minecraftApi.allowListService().remove(user, clientInfo));
/*    */     
/* 70 */     finalAllowList.stream()
/* 71 */       .filter(user -> !currentAllowList.contains(user))
/* 72 */       .forEach(user -> minecraftApi.allowListService().add(new UserWhiteListEntry(user), clientInfo));
/*    */     
/* 74 */     minecraftApi.allowListService().kickUnlistedPlayers(clientInfo);
/* 75 */     return get(minecraftApi);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\methods\AllowlistService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
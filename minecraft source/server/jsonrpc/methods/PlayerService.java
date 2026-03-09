/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class PlayerService
/*    */ {
/* 18 */   public static List<PlayerDto> get(MinecraftApi minecraftApi) { return minecraftApi.playerListService().getPlayers().stream().map(PlayerDto::from).toList(); }
/*    */   public static final class KickDto extends Record { private final PlayerDto player; private final Optional<Message> message;
/*    */     
/* 21 */     public KickDto(PlayerDto player, Optional<Message> message) { this.player = player; this.message = message; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/PlayerService$KickDto;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 21 */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/PlayerService$KickDto; } public PlayerDto player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/PlayerService$KickDto;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/PlayerService$KickDto; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/PlayerService$KickDto;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/PlayerService$KickDto;
/* 21 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Message> message() { return this.message; }
/* 22 */     public static final MapCodec<KickDto> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(PlayerDto.CODEC
/* 23 */           .codec().fieldOf("player").forGetter(KickDto::player), Message.CODEC
/* 24 */           .optionalFieldOf("message").forGetter(KickDto::message))
/* 25 */         .apply(i, KickDto::new)); }
/*    */ 
/*    */   
/* 28 */   private static final Component DEFAULT_KICK_MESSAGE = Component.translatable("multiplayer.disconnect.kicked");
/*    */   
/*    */   public static List<PlayerDto> kick(MinecraftApi minecraftApi, List<KickDto> kick, ClientInfo clientInfo) {
/* 31 */     List<PlayerDto> kicked = new ArrayList<PlayerDto>();
/* 32 */     for (KickDto kickDto : kick) {
/* 33 */       ServerPlayer serverPlayer = getServerPlayer(minecraftApi, kickDto.player());
/* 34 */       if (serverPlayer != null) {
/* 35 */         minecraftApi.playerListService().remove(serverPlayer, clientInfo);
/* 36 */         serverPlayer.connection.disconnect((Component)kickDto.message.flatMap(Message::asComponent).orElse(DEFAULT_KICK_MESSAGE));
/* 37 */         kicked.add(kickDto.player());
/*    */       } 
/*    */     } 
/* 40 */     return kicked;
/*    */   }
/*    */   
/*    */   private static ServerPlayer getServerPlayer(MinecraftApi minecraftApi, PlayerDto playerDto) {
/* 44 */     if (playerDto.id().isPresent())
/* 45 */       return minecraftApi.playerListService().getPlayer((UUID)playerDto.id().get()); 
/* 46 */     if (playerDto.name().isPresent()) {
/* 47 */       return minecraftApi.playerListService().getPlayerByName((String)playerDto.name().get());
/*    */     }
/* 49 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\methods\PlayerService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
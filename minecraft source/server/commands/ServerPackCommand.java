/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.UuidArgument;
/*    */ import net.minecraft.network.Connection;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket;
/*    */ import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
/*    */ 
/*    */ 
/*    */ public class ServerPackCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 24 */     dispatcher.register(
/* 25 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("serverpack")
/* 26 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 27 */         .then(
/* 28 */           Commands.literal("push")
/* 29 */           .then((
/* 30 */             (RequiredArgumentBuilder)Commands.argument("url", StringArgumentType.string())
/* 31 */             .then((
/* 32 */               (RequiredArgumentBuilder)Commands.argument("uuid", UuidArgument.uuid())
/* 33 */               .then(
/* 34 */                 Commands.argument("hash", StringArgumentType.word())
/* 35 */                 .executes(c -> pushPack((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "url"), Optional.of(UuidArgument.getUuid(c, "uuid")), Optional.of(StringArgumentType.getString(c, "hash"))))))
/*    */               
/* 37 */               .executes(c -> pushPack((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "url"), Optional.of(UuidArgument.getUuid(c, "uuid")), Optional.empty()))))
/*    */             
/* 39 */             .executes(c -> pushPack((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "url"), Optional.empty(), Optional.empty())))))
/*    */ 
/*    */         
/* 42 */         .then(
/* 43 */           Commands.literal("pop")
/* 44 */           .then(
/* 45 */             Commands.argument("uuid", UuidArgument.uuid())
/* 46 */             .executes(c -> popPack((CommandSourceStack)c.getSource(), UuidArgument.getUuid(c, "uuid"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   private static void sendToAllConnections(CommandSourceStack source, Packet<?> packet) { source.getServer().getConnection().getConnections().forEach(connection -> connection.send(packet)); }
/*    */ 
/*    */   
/*    */   private static int pushPack(CommandSourceStack source, String url, Optional<UUID> maybeId, Optional<String> maybeHash) {
/* 58 */     UUID id = (UUID)maybeId.orElseGet(() -> UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8)));
/* 59 */     String hash = (String)maybeHash.orElse("");
/*    */     
/* 61 */     ClientboundResourcePackPushPacket packet = new ClientboundResourcePackPushPacket(id, url, hash, false, null);
/* 62 */     sendToAllConnections(source, packet);
/* 63 */     return 0;
/*    */   }
/*    */   
/*    */   private static int popPack(CommandSourceStack source, UUID uuid) {
/* 67 */     ClientboundResourcePackPopPacket packet = new ClientboundResourcePackPopPacket(Optional.of(uuid));
/* 68 */     sendToAllConnections(source, packet);
/* 69 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\ServerPackCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
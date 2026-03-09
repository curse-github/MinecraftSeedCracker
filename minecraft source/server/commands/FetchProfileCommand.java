/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.UuidArgument;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.chat.contents.objects.PlayerSprite;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.players.ProfileResolver;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ 
/*     */ public class FetchProfileCommand {
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  34 */     dispatcher.register(
/*  35 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("fetchprofile")
/*  36 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  37 */         .then(
/*  38 */           Commands.literal("name")
/*  39 */           .then(
/*  40 */             Commands.argument("name", StringArgumentType.greedyString())
/*  41 */             .executes(c -> resolveName((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "name"))))))
/*     */ 
/*     */         
/*  44 */         .then(
/*  45 */           Commands.literal("id")
/*  46 */           .then(
/*  47 */             Commands.argument("id", UuidArgument.uuid())
/*  48 */             .executes(c -> resolveId((CommandSourceStack)c.getSource(), UuidArgument.getUuid(c, "id"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void reportResolvedProfile(CommandSourceStack sender, GameProfile gameProfile, String messageId, Component argument) {
/*  55 */     ResolvableProfile componentToWrite = ResolvableProfile.createResolved(gameProfile);
/*  56 */     ResolvableProfile.CODEC.encodeStart(NbtOps.INSTANCE, componentToWrite)
/*  57 */       .ifSuccess(encodedProfile -> {
/*  58 */           String encodedProfileAsString = encodedProfile.toString();
/*  59 */           MutableComponent headComponent = Component.object(new PlayerSprite(componentToWrite, true));
/*  60 */           ComponentSerialization.CODEC.encodeStart(NbtOps.INSTANCE, headComponent).ifSuccess(())
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  77 */             .ifError(());
/*     */ 
/*     */ 
/*     */         
/*  81 */         }).ifError(error -> 
/*  82 */         sender.sendFailure(Component.translatable("commands.fetchprofile.failed_to_serialize", new Object[] { error.message() })));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int resolveName(CommandSourceStack source, String name) {
/*  87 */     MinecraftServer server = source.getServer();
/*  88 */     ProfileResolver resolver = server.services().profileResolver();
/*     */     
/*  90 */     Util.nonCriticalIoPool().execute(() -> {
/*  91 */           MutableComponent mutableComponent = Component.literal(name);
/*  92 */           Optional<GameProfile> result = resolver.fetchByName(name);
/*  93 */           server.execute(());
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     return 1;
/*     */   }
/*     */   
/*     */   private static int resolveId(CommandSourceStack source, UUID id) {
/* 104 */     MinecraftServer server = source.getServer();
/* 105 */     ProfileResolver resolver = server.services().profileResolver();
/*     */     
/* 107 */     Util.nonCriticalIoPool().execute(() -> {
/* 108 */           Component idComponent = Component.translationArg(id);
/* 109 */           Optional<GameProfile> result = resolver.fetchById(id);
/* 110 */           server.execute(());
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\FetchProfileCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
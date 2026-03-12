/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*     */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.game.ClientboundSoundPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlaySoundCommand
/*     */ {
/*  39 */   private static final SimpleCommandExceptionType ERROR_TOO_FAR = new SimpleCommandExceptionType(Component.translatable("commands.playsound.failed"));
/*     */ 
/*     */ 
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  44 */     RequiredArgumentBuilder<CommandSourceStack, Identifier> name = (RequiredArgumentBuilder)Commands.argument("sound", IdentifierArgument.id()).suggests(SuggestionProviders.cast(SuggestionProviders.AVAILABLE_SOUNDS)).executes(c -> playSound((CommandSourceStack)c.getSource(), getCallingPlayerAsCollection(((CommandSourceStack)c.getSource()).getPlayer()), IdentifierArgument.getId(c, "sound"), SoundSource.MASTER, ((CommandSourceStack)c.getSource()).getPosition(), 1.0F, 1.0F, 0.0F));
/*     */     
/*  46 */     for (SoundSource source : SoundSource.values()) {
/*  47 */       name.then(source(source));
/*     */     }
/*     */     
/*  50 */     dispatcher.register(
/*  51 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("playsound")
/*  52 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  53 */         .then(name));
/*     */   }
/*     */ 
/*     */   
/*     */   private static LiteralArgumentBuilder<CommandSourceStack> source(SoundSource source) {
/*  58 */     return (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal(source.getName())
/*  59 */       .executes(c -> playSound((CommandSourceStack)c.getSource(), getCallingPlayerAsCollection(((CommandSourceStack)c.getSource()).getPlayer()), IdentifierArgument.getId(c, "sound"), source, ((CommandSourceStack)c.getSource()).getPosition(), 1.0F, 1.0F, 0.0F)))
/*  60 */       .then((
/*  61 */         (RequiredArgumentBuilder)Commands.argument("targets", EntityArgument.players())
/*  62 */         .executes(c -> playSound((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), IdentifierArgument.getId(c, "sound"), source, ((CommandSourceStack)c.getSource()).getPosition(), 1.0F, 1.0F, 0.0F)))
/*  63 */         .then((
/*  64 */           (RequiredArgumentBuilder)Commands.argument("pos", Vec3Argument.vec3())
/*  65 */           .executes(c -> playSound((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), IdentifierArgument.getId(c, "sound"), source, Vec3Argument.getVec3(c, "pos"), 1.0F, 1.0F, 0.0F)))
/*  66 */           .then((
/*  67 */             (RequiredArgumentBuilder)Commands.argument("volume", FloatArgumentType.floatArg(0.0F))
/*  68 */             .executes(c -> playSound((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), IdentifierArgument.getId(c, "sound"), source, Vec3Argument.getVec3(c, "pos"), ((Float)c.getArgument("volume", Float.class)).floatValue(), 1.0F, 0.0F)))
/*  69 */             .then((
/*  70 */               (RequiredArgumentBuilder)Commands.argument("pitch", FloatArgumentType.floatArg(0.0F, 2.0F))
/*  71 */               .executes(c -> playSound((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), IdentifierArgument.getId(c, "sound"), source, Vec3Argument.getVec3(c, "pos"), ((Float)c.getArgument("volume", Float.class)).floatValue(), ((Float)c.getArgument("pitch", Float.class)).floatValue(), 0.0F)))
/*  72 */               .then(
/*  73 */                 Commands.argument("minVolume", FloatArgumentType.floatArg(0.0F, 1.0F))
/*  74 */                 .executes(c -> playSound((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), IdentifierArgument.getId(c, "sound"), source, Vec3Argument.getVec3(c, "pos"), ((Float)c.getArgument("volume", Float.class)).floatValue(), ((Float)c.getArgument("pitch", Float.class)).floatValue(), ((Float)c.getArgument("minVolume", Float.class)).floatValue())))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   private static Collection<ServerPlayer> getCallingPlayerAsCollection(ServerPlayer player) { return (player != null) ? List.of(player) : List.of(); }
/*     */ 
/*     */   
/*     */   private static int playSound(CommandSourceStack source, Collection<ServerPlayer> players, Identifier sound, SoundSource soundSource, Vec3 position, float volume, float pitch, float minVolume) throws CommandSyntaxException {
/*  87 */     Holder<SoundEvent> soundHolder = Holder.direct(SoundEvent.createVariableRangeEvent(sound));
/*  88 */     double maxDistSqr = Mth.square(((SoundEvent)soundHolder.value()).getRange(volume));
/*  89 */     ServerLevel level = source.getLevel();
/*  90 */     long seed = level.getRandom().nextLong();
/*     */     
/*  92 */     List<ServerPlayer> playedFor = new ArrayList<ServerPlayer>();
/*     */     
/*  94 */     for (ServerPlayer player : players) {
/*  95 */       if (player.level() != level) {
/*     */         continue;
/*     */       }
/*  98 */       double deltaX = position.x - player.getX();
/*  99 */       double deltaY = position.y - player.getY();
/* 100 */       double deltaZ = position.z - player.getZ();
/* 101 */       double distSqr = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
/* 102 */       Vec3 localPosition = position;
/* 103 */       float localVolume = volume;
/*     */       
/* 105 */       if (distSqr > maxDistSqr) {
/* 106 */         if (minVolume <= 0.0F) {
/*     */           continue;
/*     */         }
/*     */         
/* 110 */         double distance = Math.sqrt(distSqr);
/* 111 */         localPosition = new Vec3(player.getX() + deltaX / distance * 2.0D, player.getY() + deltaY / distance * 2.0D, player.getZ() + deltaZ / distance * 2.0D);
/* 112 */         localVolume = minVolume;
/*     */       } 
/*     */       
/* 115 */       player.connection.send(new ClientboundSoundPacket(soundHolder, soundSource, localPosition.x(), localPosition.y(), localPosition.z(), localVolume, pitch, seed));
/* 116 */       playedFor.add(player);
/*     */     } 
/*     */     
/* 119 */     int count = playedFor.size();
/* 120 */     if (count == 0)
/* 121 */       throw ERROR_TOO_FAR.create(); 
/* 122 */     if (count == 1) {
/* 123 */       source.sendSuccess(() -> Component.translatable("commands.playsound.success.single", new Object[] { Component.translationArg(sound), ((ServerPlayer)playedFor.getFirst()).getDisplayName() }), true);
/*     */     } else {
/* 125 */       source.sendSuccess(() -> Component.translatable("commands.playsound.success.multiple", new Object[] { Component.translationArg(sound), Integer.valueOf(count) }), true);
/*     */     } 
/*     */     
/* 128 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\PlaySoundCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
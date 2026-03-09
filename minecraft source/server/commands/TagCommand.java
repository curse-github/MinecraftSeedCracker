/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class TagCommand
/*     */ {
/*  25 */   private static final SimpleCommandExceptionType ERROR_ADD_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.tag.add.failed"));
/*  26 */   private static final SimpleCommandExceptionType ERROR_REMOVE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.tag.remove.failed"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  29 */     dispatcher.register(
/*  30 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tag")
/*  31 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  32 */         .then((
/*  33 */           (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", EntityArgument.entities())
/*  34 */           .then(
/*  35 */             Commands.literal("add")
/*  36 */             .then(
/*  37 */               Commands.argument("name", StringArgumentType.word())
/*  38 */               .executes(c -> addTag((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), StringArgumentType.getString(c, "name"))))))
/*     */ 
/*     */           
/*  41 */           .then(
/*  42 */             Commands.literal("remove")
/*  43 */             .then(
/*  44 */               Commands.argument("name", StringArgumentType.word())
/*  45 */               .suggests((c, p) -> SharedSuggestionProvider.suggest(getTags(EntityArgument.getEntities(c, "targets")), p))
/*  46 */               .executes(c -> removeTag((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), StringArgumentType.getString(c, "name"))))))
/*     */ 
/*     */           
/*  49 */           .then(
/*  50 */             Commands.literal("list")
/*  51 */             .executes(c -> listTags((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Collection<String> getTags(Collection<? extends Entity> entities) {
/*  58 */     Set<String> result = Sets.newHashSet();
/*  59 */     for (Entity entity : entities) {
/*  60 */       result.addAll(entity.getTags());
/*     */     }
/*  62 */     return result;
/*     */   }
/*     */   
/*     */   private static int addTag(CommandSourceStack source, Collection<? extends Entity> targets, String name) throws CommandSyntaxException {
/*  66 */     int count = 0;
/*     */     
/*  68 */     for (Entity entity : targets) {
/*  69 */       if (entity.addTag(name)) {
/*  70 */         count++;
/*     */       }
/*     */     } 
/*     */     
/*  74 */     if (count == 0) {
/*  75 */       throw ERROR_ADD_FAILED.create();
/*     */     }
/*     */     
/*  78 */     if (targets.size() == 1) {
/*  79 */       source.sendSuccess(() -> Component.translatable("commands.tag.add.success.single", new Object[] { name, ((Entity)targets.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/*  81 */       source.sendSuccess(() -> Component.translatable("commands.tag.add.success.multiple", new Object[] { name, Integer.valueOf(targets.size()) }), true);
/*     */     } 
/*     */     
/*  84 */     return count;
/*     */   }
/*     */   
/*     */   private static int removeTag(CommandSourceStack source, Collection<? extends Entity> targets, String name) throws CommandSyntaxException {
/*  88 */     int count = 0;
/*     */     
/*  90 */     for (Entity entity : targets) {
/*  91 */       if (entity.removeTag(name)) {
/*  92 */         count++;
/*     */       }
/*     */     } 
/*     */     
/*  96 */     if (count == 0) {
/*  97 */       throw ERROR_REMOVE_FAILED.create();
/*     */     }
/*     */     
/* 100 */     if (targets.size() == 1) {
/* 101 */       source.sendSuccess(() -> Component.translatable("commands.tag.remove.success.single", new Object[] { name, ((Entity)targets.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 103 */       source.sendSuccess(() -> Component.translatable("commands.tag.remove.success.multiple", new Object[] { name, Integer.valueOf(targets.size()) }), true);
/*     */     } 
/*     */     
/* 106 */     return count;
/*     */   }
/*     */   
/*     */   private static int listTags(CommandSourceStack source, Collection<? extends Entity> targets) {
/* 110 */     Set<String> tags = Sets.newHashSet();
/*     */     
/* 112 */     for (Entity entity : targets) {
/* 113 */       tags.addAll(entity.getTags());
/*     */     }
/*     */     
/* 116 */     if (targets.size() == 1) {
/* 117 */       Entity entity = (Entity)targets.iterator().next();
/*     */       
/* 119 */       if (tags.isEmpty()) {
/* 120 */         source.sendSuccess(() -> Component.translatable("commands.tag.list.single.empty", new Object[] { entity.getDisplayName() }), false);
/*     */       } else {
/* 122 */         source.sendSuccess(() -> Component.translatable("commands.tag.list.single.success", new Object[] { entity.getDisplayName(), Integer.valueOf(tags.size()), ComponentUtils.formatList(tags) }), false);
/*     */       }
/*     */     
/* 125 */     } else if (tags.isEmpty()) {
/* 126 */       source.sendSuccess(() -> Component.translatable("commands.tag.list.multiple.empty", new Object[] { Integer.valueOf(targets.size()) }), false);
/*     */     } else {
/* 128 */       source.sendSuccess(() -> Component.translatable("commands.tag.list.multiple.success", new Object[] { Integer.valueOf(targets.size()), Integer.valueOf(tags.size()), ComponentUtils.formatList(tags) }), false);
/*     */     } 
/*     */ 
/*     */     
/* 132 */     return tags.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\TagCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
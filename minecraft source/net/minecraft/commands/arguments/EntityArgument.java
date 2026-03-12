/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class EntityArgument
/*     */   extends Object implements ArgumentType<EntitySelector> {
/*  30 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "Player", "0123", "@e", "@e[type=foo]", "dd12be42-52a9-4a91-a8a1-11c01849e498" });
/*  31 */   public static final SimpleCommandExceptionType ERROR_NOT_SINGLE_ENTITY = new SimpleCommandExceptionType(Component.translatable("argument.entity.toomany"));
/*  32 */   public static final SimpleCommandExceptionType ERROR_NOT_SINGLE_PLAYER = new SimpleCommandExceptionType(Component.translatable("argument.player.toomany"));
/*  33 */   public static final SimpleCommandExceptionType ERROR_ONLY_PLAYERS_ALLOWED = new SimpleCommandExceptionType(Component.translatable("argument.player.entities"));
/*  34 */   public static final SimpleCommandExceptionType NO_ENTITIES_FOUND = new SimpleCommandExceptionType(Component.translatable("argument.entity.notfound.entity"));
/*  35 */   public static final SimpleCommandExceptionType NO_PLAYERS_FOUND = new SimpleCommandExceptionType(Component.translatable("argument.entity.notfound.player"));
/*  36 */   public static final SimpleCommandExceptionType ERROR_SELECTORS_NOT_ALLOWED = new SimpleCommandExceptionType(Component.translatable("argument.entity.selector.not_allowed"));
/*     */   
/*     */   private final boolean single;
/*     */   private final boolean playersOnly;
/*     */   
/*     */   protected EntityArgument(boolean single, boolean playersOnly) {
/*  42 */     this.single = single;
/*  43 */     this.playersOnly = playersOnly;
/*     */   }
/*     */ 
/*     */   
/*  47 */   public static EntityArgument entity() { return new EntityArgument(true, false); }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static Entity getEntity(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return ((EntitySelector)context.getArgument(name, EntitySelector.class)).findSingleEntity((CommandSourceStack)context.getSource()); }
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static EntityArgument entities() { return new EntityArgument(false, false); }
/*     */ 
/*     */   
/*     */   public static Collection<? extends Entity> getEntities(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  59 */     Collection<? extends Entity> result = getOptionalEntities(context, name);
/*  60 */     if (result.isEmpty()) {
/*  61 */       throw NO_ENTITIES_FOUND.create();
/*     */     }
/*  63 */     return result;
/*     */   }
/*     */ 
/*     */   
/*  67 */   public static Collection<? extends Entity> getOptionalEntities(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return ((EntitySelector)context.getArgument(name, EntitySelector.class)).findEntities((CommandSourceStack)context.getSource()); }
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static Collection<ServerPlayer> getOptionalPlayers(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return ((EntitySelector)context.getArgument(name, EntitySelector.class)).findPlayers((CommandSourceStack)context.getSource()); }
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static EntityArgument player() { return new EntityArgument(true, true); }
/*     */ 
/*     */ 
/*     */   
/*  79 */   public static ServerPlayer getPlayer(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return ((EntitySelector)context.getArgument(name, EntitySelector.class)).findSinglePlayer((CommandSourceStack)context.getSource()); }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public static EntityArgument players() { return new EntityArgument(false, true); }
/*     */ 
/*     */   
/*     */   public static Collection<ServerPlayer> getPlayers(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  87 */     List<ServerPlayer> players = ((EntitySelector)context.getArgument(name, EntitySelector.class)).findPlayers((CommandSourceStack)context.getSource());
/*  88 */     if (players.isEmpty()) {
/*  89 */       throw NO_PLAYERS_FOUND.create();
/*     */     }
/*  91 */     return players;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public EntitySelector parse(StringReader reader) throws CommandSyntaxException { return parse(reader, true); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public <S> EntitySelector parse(StringReader reader, S source) throws CommandSyntaxException { return parse(reader, EntitySelectorParser.allowSelectors(source)); }
/*     */ 
/*     */   
/*     */   private EntitySelector parse(StringReader reader, boolean allowSelectors) throws CommandSyntaxException {
/* 106 */     int start = 0;
/* 107 */     EntitySelectorParser parser = new EntitySelectorParser(reader, allowSelectors);
/* 108 */     EntitySelector selector = parser.parse();
/* 109 */     if (selector.getMaxResults() > 1 && this.single) {
/* 110 */       if (this.playersOnly) {
/* 111 */         reader.setCursor(0);
/* 112 */         throw ERROR_NOT_SINGLE_PLAYER.createWithContext(reader);
/*     */       } 
/* 114 */       reader.setCursor(0);
/* 115 */       throw ERROR_NOT_SINGLE_ENTITY.createWithContext(reader);
/*     */     } 
/*     */     
/* 118 */     if (selector.includesEntities() && this.playersOnly && !selector.isSelfSelector()) {
/* 119 */       reader.setCursor(0);
/* 120 */       throw ERROR_ONLY_PLAYERS_ALLOWED.createWithContext(reader);
/*     */     } 
/*     */     
/* 123 */     return selector;
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> contextBuilder, SuggestionsBuilder builder) {
/* 128 */     Object object = contextBuilder.getSource(); if (object instanceof SharedSuggestionProvider) { SharedSuggestionProvider source = (SharedSuggestionProvider)object;
/* 129 */       StringReader reader = new StringReader(builder.getInput());
/* 130 */       reader.setCursor(builder.getStart());
/* 131 */       EntitySelectorParser parser = new EntitySelectorParser(reader, source.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS));
/*     */       try {
/* 133 */         parser.parse();
/* 134 */       } catch (CommandSyntaxException commandSyntaxException) {}
/*     */       
/* 136 */       return parser.fillSuggestions(builder, suggestions -> {
/* 137 */             Collection<String> onlinePlayerNames = source.getOnlinePlayerNames();
/* 138 */             Iterable<String> suggestedNames = this.playersOnly ? onlinePlayerNames : Iterables.concat(onlinePlayerNames, source.getSelectedEntities());
/* 139 */             SharedSuggestionProvider.suggest(suggestedNames, suggestions);
/*     */           }); }
/*     */     
/* 142 */     return Suggestions.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   public Collection<String> getExamples() { return EXAMPLES; }
/*     */   
/*     */   public static class Info
/*     */     extends Object implements ArgumentTypeInfo<EntityArgument, Info.Template> {
/*     */     private static final byte FLAG_SINGLE = 1;
/*     */     private static final byte FLAG_PLAYERS_ONLY = 2;
/*     */     
/*     */     public final class Template extends Object implements ArgumentTypeInfo.Template<EntityArgument> {
/*     */       private final boolean single;
/*     */       private final boolean playersOnly;
/*     */       
/*     */       private Template(boolean single, boolean playersOnly) {
/* 160 */         this.single = single;
/* 161 */         this.playersOnly = playersOnly;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 166 */       public EntityArgument instantiate(CommandBuildContext context) { return new EntityArgument(this.single, this.playersOnly); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 171 */       public ArgumentTypeInfo<EntityArgument, ?> type() { return EntityArgument.Info.this; }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 177 */       int flags = 0;
/* 178 */       if (template.single) {
/* 179 */         flags |= 0x1;
/*     */       }
/* 181 */       if (template.playersOnly) {
/* 182 */         flags |= 0x2;
/*     */       }
/* 184 */       out.writeByte(flags);
/*     */     }
/*     */ 
/*     */     
/*     */     public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 189 */       byte flags = in.readByte();
/* 190 */       return new Template(((flags & true) != 0), ((flags & 0x2) != 0));
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeToJson(Template template, JsonObject out) {
/* 195 */       out.addProperty("amount", template.single ? "single" : "multiple");
/* 196 */       out.addProperty("type", template.playersOnly ? "players" : "entities");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 201 */     public Template unpack(EntityArgument argument) { return new Template(argument.single, argument.playersOnly); }
/*     */   }
/*     */   
/*     */   public final class Template extends Object implements ArgumentTypeInfo.Template<EntityArgument> {
/*     */     private final boolean single;
/*     */     private final boolean playersOnly;
/*     */     
/*     */     private Template(boolean single, boolean playersOnly) {
/*     */       this.single = single;
/*     */       this.playersOnly = playersOnly;
/*     */     }
/*     */     
/*     */     public EntityArgument instantiate(CommandBuildContext context) { return new EntityArgument(this.single, this.playersOnly); }
/*     */     
/*     */     public ArgumentTypeInfo<EntityArgument, ?> type() { return EntityArgument.Info.this; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\EntityArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
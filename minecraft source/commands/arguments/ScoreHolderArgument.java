/*     */ package net.minecraft.commands.arguments;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ 
/*     */ public class ScoreHolderArgument extends Object implements ArgumentType<ScoreHolderArgument.Result> {
/*     */   public static final SuggestionProvider<CommandSourceStack> SUGGEST_SCORE_HOLDERS = (context, builder) -> {
/*  36 */       StringReader reader = new StringReader(builder.getInput());
/*  37 */       reader.setCursor(builder.getStart());
/*  38 */       EntitySelectorParser parser = new EntitySelectorParser(reader, ((CommandSourceStack)context.getSource()).permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS));
/*     */       try {
/*  40 */         parser.parse();
/*  41 */       } catch (CommandSyntaxException commandSyntaxException) {}
/*     */       
/*  43 */       return parser.fillSuggestions(builder, ());
/*     */     };
/*     */   
/*  46 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "Player", "0123", "*", "@e" });
/*  47 */   private static final SimpleCommandExceptionType ERROR_NO_RESULTS = new SimpleCommandExceptionType(Component.translatable("argument.scoreHolder.empty"));
/*     */   
/*     */   private final boolean multiple;
/*     */ 
/*     */   
/*  52 */   public ScoreHolderArgument(boolean multiple) { this.multiple = multiple; }
/*     */ 
/*     */ 
/*     */   
/*  56 */   public static ScoreHolder getName(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return (ScoreHolder)getNames(context, name).iterator().next(); }
/*     */ 
/*     */ 
/*     */   
/*  60 */   public static Collection<ScoreHolder> getNames(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return getNames(context, name, Collections::emptyList); }
/*     */ 
/*     */ 
/*     */   
/*  64 */   public static Collection<ScoreHolder> getNamesWithDefaultWildcard(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { Objects.requireNonNull(((CommandSourceStack)context.getSource()).getServer().getScoreboard()); return getNames(context, name, ((CommandSourceStack)context.getSource()).getServer().getScoreboard()::getTrackedPlayers); }
/*     */ 
/*     */   
/*     */   public static Collection<ScoreHolder> getNames(CommandContext<CommandSourceStack> context, String name, Supplier<Collection<ScoreHolder>> wildcard) throws CommandSyntaxException {
/*  68 */     Collection<ScoreHolder> result = ((Result)context.getArgument(name, Result.class)).getNames((CommandSourceStack)context.getSource(), wildcard);
/*  69 */     if (result.isEmpty()) {
/*  70 */       throw EntityArgument.NO_ENTITIES_FOUND.create();
/*     */     }
/*  72 */     return result;
/*     */   }
/*     */ 
/*     */   
/*  76 */   public static ScoreHolderArgument scoreHolder() { return new ScoreHolderArgument(false); }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public static ScoreHolderArgument scoreHolders() { return new ScoreHolderArgument(true); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public Result parse(StringReader reader) throws CommandSyntaxException { return parse(reader, true); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public <S> Result parse(StringReader reader, S source) throws CommandSyntaxException { return parse(reader, EntitySelectorParser.allowSelectors(source)); }
/*     */ 
/*     */   
/*     */   private Result parse(StringReader reader, boolean allowSelectors) throws CommandSyntaxException {
/*  95 */     if (reader.canRead() && reader.peek() == '@') {
/*  96 */       EntitySelectorParser parser = new EntitySelectorParser(reader, allowSelectors);
/*  97 */       EntitySelector selector = parser.parse();
/*  98 */       if (!this.multiple && selector.getMaxResults() > 1) {
/*  99 */         throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.createWithContext(reader);
/*     */       }
/* 101 */       return new SelectorResult(selector);
/*     */     } 
/* 103 */     int start = reader.getCursor();
/* 104 */     while (reader.canRead() && reader.peek() != ' ') {
/* 105 */       reader.skip();
/*     */     }
/* 107 */     String text = reader.getString().substring(start, reader.getCursor());
/* 108 */     if (text.equals("*")) {
/* 109 */       return (sender, wildcard) -> {
/* 110 */           Collection<ScoreHolder> results = (Collection)wildcard.get();
/* 111 */           if (results.isEmpty()) {
/* 112 */             throw ERROR_NO_RESULTS.create();
/*     */           }
/* 114 */           return results;
/*     */         };
/*     */     }
/*     */     
/* 118 */     List<ScoreHolder> nameOnlyHolder = List.of(ScoreHolder.forNameOnly(text));
/*     */ 
/*     */     
/* 121 */     if (text.startsWith("#")) {
/* 122 */       return (sender, wildcard) -> nameOnlyHolder;
/*     */     }
/*     */     
/*     */     try {
/* 126 */       UUID uuid = UUID.fromString(text);
/*     */       
/* 128 */       return (sender, wildcard) -> {
/* 129 */           MinecraftServer server = sender.getServer();
/*     */           
/* 131 */           Entity entity1 = null;
/* 132 */           List<ScoreHolder> moreResults = null;
/* 133 */           for (ServerLevel level : server.getAllLevels()) {
/* 134 */             Entity entity = level.getEntity(uuid);
/* 135 */             if (entity != null) {
/* 136 */               if (entity1 == null) {
/* 137 */                 entity1 = entity;
/*     */                 continue;
/*     */               } 
/* 140 */               if (moreResults == null) {
/* 141 */                 moreResults = new ArrayList<ScoreHolder>();
/* 142 */                 moreResults.add(entity1);
/*     */               } 
/* 144 */               moreResults.add(entity);
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 149 */           if (moreResults != null) {
/* 150 */             return moreResults;
/*     */           }
/* 152 */           if (entity1 != null) {
/* 153 */             return List.of(entity1);
/*     */           }
/* 155 */           return nameOnlyHolder;
/*     */         };
/* 157 */     } catch (IllegalArgumentException illegalArgumentException) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 162 */       return (sender, wildcard) -> {
/*     */           
/* 164 */           MinecraftServer server = sender.getServer();
/* 165 */           ServerPlayer player = server.getPlayerList().getPlayerByName(text);
/*     */           
/* 167 */           if (player != null) {
/* 168 */             return List.of(player);
/*     */           }
/*     */           
/* 171 */           return nameOnlyHolder;
/*     */         };
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 177 */   public Collection<String> getExamples() { return EXAMPLES; }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Result
/*     */   {
/*     */     Collection<ScoreHolder> getNames(CommandSourceStack param1CommandSourceStack, Supplier<Collection<ScoreHolder>> param1Supplier) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   public static class SelectorResult
/*     */     implements Result {
/*     */     private final EntitySelector selector;
/*     */     
/* 189 */     public SelectorResult(EntitySelector selector) { this.selector = selector; }
/*     */ 
/*     */ 
/*     */     
/*     */     public Collection<ScoreHolder> getNames(CommandSourceStack sender, Supplier<Collection<ScoreHolder>> wildcard) throws CommandSyntaxException {
/* 194 */       List<? extends Entity> entities = this.selector.findEntities(sender);
/* 195 */       if (entities.isEmpty()) {
/* 196 */         throw EntityArgument.NO_ENTITIES_FOUND.create();
/*     */       }
/* 198 */       return List.copyOf(entities);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Info
/*     */     extends Object implements ArgumentTypeInfo<ScoreHolderArgument, Info.Template> {
/*     */     private static final byte FLAG_MULTIPLE = 1;
/*     */     
/*     */     public final class Template extends Object implements ArgumentTypeInfo.Template<ScoreHolderArgument> {
/*     */       private final boolean multiple;
/*     */       
/* 209 */       private Template(boolean multiple) { this.multiple = multiple; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 214 */       public ScoreHolderArgument instantiate(CommandBuildContext context) { return new ScoreHolderArgument(this.multiple); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 219 */       public ArgumentTypeInfo<ScoreHolderArgument, ?> type() { return ScoreHolderArgument.Info.this; }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 225 */       int flags = 0;
/* 226 */       if (template.multiple) {
/* 227 */         flags |= 0x1;
/*     */       }
/* 229 */       out.writeByte(flags);
/*     */     }
/*     */ 
/*     */     
/*     */     public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 234 */       byte flags = in.readByte();
/* 235 */       boolean multiple = ((flags & true) != 0);
/* 236 */       return new Template(multiple);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 241 */     public void serializeToJson(Template template, JsonObject out) { out.addProperty("amount", template.multiple ? "multiple" : "single"); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 246 */     public Template unpack(ScoreHolderArgument argument) { return new Template(argument.multiple); }
/*     */   }
/*     */   
/*     */   public final class Template extends Object implements ArgumentTypeInfo.Template<ScoreHolderArgument> {
/*     */     private final boolean multiple;
/*     */     
/*     */     private Template(boolean multiple) { this.multiple = multiple; }
/*     */     
/*     */     public ScoreHolderArgument instantiate(CommandBuildContext context) { return new ScoreHolderArgument(this.multiple); }
/*     */     
/*     */     public ArgumentTypeInfo<ScoreHolderArgument, ?> type() { return ScoreHolderArgument.Info.this; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ScoreHolderArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
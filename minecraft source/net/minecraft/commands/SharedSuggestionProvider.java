/*     */ package net.minecraft.commands;
/*     */ 
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.permissions.PermissionSetSupplier;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public interface SharedSuggestionProvider extends PermissionSetSupplier {
/*  32 */   public static final CharMatcher MATCH_SPLITTER = CharMatcher.anyOf("._/");
/*     */   
/*     */   public static class TextCoordinates {
/*  35 */     public static final TextCoordinates DEFAULT_LOCAL = new TextCoordinates("^", "^", "^");
/*     */     
/*  37 */     public static final TextCoordinates DEFAULT_GLOBAL = new TextCoordinates("~", "~", "~");
/*     */     
/*     */     public final String x;
/*     */     
/*     */     public final String y;
/*     */     
/*     */     public final String z;
/*     */     
/*     */     public TextCoordinates(String x, String y, String z) {
/*  46 */       this.x = x;
/*  47 */       this.y = y;
/*  48 */       this.z = z;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   default Collection<String> getCustomTabSugggestions() { return getOnlinePlayerNames(); }
/*     */ 
/*     */ 
/*     */   
/*  59 */   default Collection<String> getSelectedEntities() { return Collections.emptyList(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   default Collection<TextCoordinates> getRelevantCoordinates() { return Collections.singleton(TextCoordinates.DEFAULT_GLOBAL); }
/*     */ 
/*     */ 
/*     */   
/*  73 */   default Collection<TextCoordinates> getAbsoluteCoordinates() { return Collections.singleton(TextCoordinates.DEFAULT_GLOBAL); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum ElementSuggestionType
/*     */   {
/*  83 */     TAGS,
/*  84 */     ELEMENTS,
/*  85 */     ALL;
/*     */ 
/*     */ 
/*     */     
/*  89 */     public boolean shouldSuggestTags() { return (this == TAGS || this == ALL); }
/*     */ 
/*     */ 
/*     */     
/*  93 */     public boolean shouldSuggestElements() { return (this == ELEMENTS || this == ALL); }
/*     */   }
/*     */ 
/*     */   
/*     */   default void suggestRegistryElements(HolderLookup<?> registry, ElementSuggestionType elements, SuggestionsBuilder builder) {
/*  98 */     if (elements.shouldSuggestTags()) {
/*  99 */       suggestResource(registry.listTagIds().map(TagKey::location), builder, "#");
/*     */     }
/* 101 */     if (elements.shouldSuggestElements()) {
/* 102 */       suggestResource(registry.listElementIds().map(ResourceKey::identifier), builder);
/*     */     }
/*     */   }
/*     */   
/*     */   static <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder, ResourceKey<? extends Registry<?>> registryKey, ElementSuggestionType type) {
/* 107 */     Object object = context.getSource(); if (object instanceof SharedSuggestionProvider) { SharedSuggestionProvider suggestionProvider = (SharedSuggestionProvider)object;
/* 108 */       return suggestionProvider.suggestRegistryElements(registryKey, type, builder, context); }
/*     */     
/* 110 */     return builder.buildFuture();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> void filterResources(Iterable<T> values, String contents, Function<T, Identifier> converter, Consumer<T> consumer) {
/* 116 */     boolean hasNamespace = (contents.indexOf(':') > -1);
/* 117 */     for (T value : values) {
/* 118 */       Identifier id = (Identifier)converter.apply(value);
/* 119 */       if (hasNamespace) {
/* 120 */         String name = id.toString();
/* 121 */         if (matchesSubStr(contents, name))
/* 122 */           consumer.accept(value); 
/*     */         continue;
/*     */       } 
/* 125 */       if (matchesSubStr(contents, id.getNamespace()) || matchesSubStr(contents, id.getPath())) {
/* 126 */         consumer.accept(value);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> void filterResources(Iterable<T> values, String contents, String prefix, Function<T, Identifier> converter, Consumer<T> consumer) {
/* 133 */     if (contents.isEmpty()) {
/* 134 */       values.forEach(consumer);
/*     */     } else {
/* 136 */       String commonPrefix = Strings.commonPrefix(contents, prefix);
/* 137 */       if (!commonPrefix.isEmpty()) {
/* 138 */         String strippedContents = contents.substring(commonPrefix.length());
/* 139 */         filterResources(values, strippedContents, converter, consumer);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static CompletableFuture<Suggestions> suggestResource(Iterable<Identifier> values, SuggestionsBuilder builder, String prefix) {
/* 145 */     String contents = builder.getRemaining().toLowerCase(Locale.ROOT);
/* 146 */     filterResources(values, contents, prefix, t -> t, v -> builder.suggest(prefix + prefix));
/* 147 */     return builder.buildFuture();
/*     */   }
/*     */ 
/*     */   
/* 151 */   static CompletableFuture<Suggestions> suggestResource(Stream<Identifier> values, SuggestionsBuilder builder, String prefix) { Objects.requireNonNull(values); return suggestResource(values::iterator, builder, prefix); }
/*     */ 
/*     */   
/*     */   static CompletableFuture<Suggestions> suggestResource(Iterable<Identifier> values, SuggestionsBuilder builder) {
/* 155 */     String contents = builder.getRemaining().toLowerCase(Locale.ROOT);
/* 156 */     filterResources(values, contents, t -> t, v -> builder.suggest(v.toString()));
/* 157 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   static <T> CompletableFuture<Suggestions> suggestResource(Iterable<T> values, SuggestionsBuilder builder, Function<T, Identifier> id, Function<T, Message> tooltip) {
/* 161 */     String contents = builder.getRemaining().toLowerCase(Locale.ROOT);
/* 162 */     filterResources(values, contents, id, v -> builder.suggest(((Identifier)id.apply(v)).toString(), (Message)tooltip.apply(v)));
/* 163 */     return builder.buildFuture();
/*     */   }
/*     */ 
/*     */   
/* 167 */   static CompletableFuture<Suggestions> suggestResource(Stream<Identifier> values, SuggestionsBuilder builder) { Objects.requireNonNull(values); return suggestResource(values::iterator, builder); }
/*     */ 
/*     */ 
/*     */   
/* 171 */   static <T> CompletableFuture<Suggestions> suggestResource(Stream<T> values, SuggestionsBuilder builder, Function<T, Identifier> id, Function<T, Message> tooltip) { Objects.requireNonNull(values); return suggestResource(values::iterator, builder, id, tooltip); }
/*     */ 
/*     */   
/*     */   static CompletableFuture<Suggestions> suggestCoordinates(String currentInput, Collection<TextCoordinates> allSuggestions, SuggestionsBuilder builder, Predicate<String> validator) {
/* 175 */     List<String> result = Lists.newArrayList();
/*     */     
/* 177 */     if (Strings.isNullOrEmpty(currentInput)) {
/* 178 */       for (TextCoordinates coordinate : allSuggestions) {
/* 179 */         String fullValue = coordinate.x + " " + coordinate.x + " " + coordinate.y;
/* 180 */         if (validator.test(fullValue)) {
/* 181 */           result.add(coordinate.x);
/* 182 */           result.add(coordinate.x + " " + coordinate.x);
/* 183 */           result.add(fullValue);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 187 */       String[] fields = currentInput.split(" ");
/*     */       
/* 189 */       if (fields.length == 1) {
/* 190 */         for (TextCoordinates coordinate : allSuggestions) {
/* 191 */           String fullValue = fields[0] + " " + fields[0] + " " + coordinate.y;
/* 192 */           if (validator.test(fullValue)) {
/* 193 */             result.add(fields[0] + " " + fields[0]);
/* 194 */             result.add(fullValue);
/*     */           } 
/*     */         } 
/* 197 */       } else if (fields.length == 2) {
/* 198 */         for (TextCoordinates coordinate : allSuggestions) {
/* 199 */           String fullValue = fields[0] + " " + fields[0] + " " + fields[1];
/* 200 */           if (validator.test(fullValue)) {
/* 201 */             result.add(fullValue);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 206 */     return suggest(result, builder);
/*     */   }
/*     */   
/*     */   static CompletableFuture<Suggestions> suggest2DCoordinates(String currentInput, Collection<TextCoordinates> allSuggestions, SuggestionsBuilder builder, Predicate<String> validator) {
/* 210 */     List<String> result = Lists.newArrayList();
/*     */     
/* 212 */     if (Strings.isNullOrEmpty(currentInput)) {
/* 213 */       for (TextCoordinates coordinate : allSuggestions) {
/* 214 */         String fullValue = coordinate.x + " " + coordinate.x;
/* 215 */         if (validator.test(fullValue)) {
/* 216 */           result.add(coordinate.x);
/* 217 */           result.add(fullValue);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 221 */       String[] fields = currentInput.split(" ");
/* 222 */       if (fields.length == 1) {
/* 223 */         for (TextCoordinates coordinate : allSuggestions) {
/* 224 */           String fullValue = fields[0] + " " + fields[0];
/* 225 */           if (validator.test(fullValue)) {
/* 226 */             result.add(fullValue);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 231 */     return suggest(result, builder);
/*     */   }
/*     */   
/*     */   static CompletableFuture<Suggestions> suggest(Iterable<String> values, SuggestionsBuilder builder) {
/* 235 */     String lowerPrefix = builder.getRemaining().toLowerCase(Locale.ROOT);
/* 236 */     for (String name : values) {
/* 237 */       if (matchesSubStr(lowerPrefix, name.toLowerCase(Locale.ROOT))) {
/* 238 */         builder.suggest(name);
/*     */       }
/*     */     } 
/* 241 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   static CompletableFuture<Suggestions> suggest(Stream<String> values, SuggestionsBuilder builder) {
/* 245 */     String lowerPrefix = builder.getRemaining().toLowerCase(Locale.ROOT);
/* 246 */     Objects.requireNonNull(builder); values.filter(v -> matchesSubStr(lowerPrefix, v.toLowerCase(Locale.ROOT))).forEach(builder::suggest);
/* 247 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   static CompletableFuture<Suggestions> suggest(String[] values, SuggestionsBuilder builder) {
/* 251 */     String lowerPrefix = builder.getRemaining().toLowerCase(Locale.ROOT);
/* 252 */     for (String name : values) {
/* 253 */       if (matchesSubStr(lowerPrefix, name.toLowerCase(Locale.ROOT))) {
/* 254 */         builder.suggest(name);
/*     */       }
/*     */     } 
/* 257 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   static <T> CompletableFuture<Suggestions> suggest(Iterable<T> values, SuggestionsBuilder builder, Function<T, String> toString, Function<T, Message> tooltip) {
/* 261 */     String lowerPrefix = builder.getRemaining().toLowerCase(Locale.ROOT);
/* 262 */     for (T value : values) {
/* 263 */       String name = (String)toString.apply(value);
/* 264 */       if (matchesSubStr(lowerPrefix, name.toLowerCase(Locale.ROOT))) {
/* 265 */         builder.suggest(name, (Message)tooltip.apply(value));
/*     */       }
/*     */     } 
/* 268 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   static boolean matchesSubStr(String pattern, String input) {
/* 272 */     int index = 0;
/* 273 */     while (!input.startsWith(pattern, index)) {
/* 274 */       int indexOfSplitter = MATCH_SPLITTER.indexIn(input, index);
/* 275 */       if (indexOfSplitter < 0) {
/* 276 */         return false;
/*     */       }
/* 278 */       index = indexOfSplitter + 1;
/*     */     } 
/* 280 */     return true;
/*     */   }
/*     */   
/*     */   Collection<String> getOnlinePlayerNames();
/*     */   
/*     */   Collection<String> getAllTeams();
/*     */   
/*     */   Stream<Identifier> getAvailableSounds();
/*     */   
/*     */   CompletableFuture<Suggestions> customSuggestion(CommandContext<?> paramCommandContext);
/*     */   
/*     */   Set<ResourceKey<Level>> levels();
/*     */   
/*     */   RegistryAccess registryAccess();
/*     */   
/*     */   FeatureFlagSet enabledFeatures();
/*     */   
/*     */   CompletableFuture<Suggestions> suggestRegistryElements(ResourceKey<? extends Registry<?>> paramResourceKey, ElementSuggestionType paramElementSuggestionType, SuggestionsBuilder paramSuggestionsBuilder, CommandContext<?> paramCommandContext);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\SharedSuggestionProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
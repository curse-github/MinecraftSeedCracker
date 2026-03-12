/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ 
/*     */ public class ResourceSelectorArgument<T> extends Object implements ArgumentType<Collection<Holder.Reference<T>>> {
/*  29 */   private static final Collection<String> EXAMPLES = List.of("minecraft:*", "*:asset", "*");
/*     */   
/*  31 */   public static final Dynamic2CommandExceptionType ERROR_NO_MATCHES = new Dynamic2CommandExceptionType((selector, registry) -> Component.translatableEscape("argument.resource_selector.not_found", new Object[] { selector, registry }));
/*     */   
/*     */   private final ResourceKey<? extends Registry<T>> registryKey;
/*     */   private final HolderLookup<T> registryLookup;
/*     */   
/*     */   private ResourceSelectorArgument(CommandBuildContext context, ResourceKey<? extends Registry<T>> registryKey) {
/*  37 */     this.registryKey = registryKey;
/*  38 */     this.registryLookup = context.lookupOrThrow(registryKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<Holder.Reference<T>> parse(StringReader reader) throws CommandSyntaxException {
/*  43 */     String pattern = ensureNamespaced(readPattern(reader));
/*  44 */     List<Holder.Reference<T>> results = this.registryLookup.listElements().filter(element -> matches(pattern, element.key().identifier())).toList();
/*  45 */     if (results.isEmpty()) {
/*  46 */       throw ERROR_NO_MATCHES.createWithContext(reader, pattern, this.registryKey.identifier());
/*     */     }
/*  48 */     return results;
/*     */   }
/*     */   
/*     */   public static <T> Collection<Holder.Reference<T>> parse(StringReader reader, HolderLookup<T> registry) {
/*  52 */     String pattern = ensureNamespaced(readPattern(reader));
/*  53 */     return registry.listElements().filter(element -> matches(pattern, element.key().identifier())).toList();
/*     */   }
/*     */   
/*     */   private static String readPattern(StringReader reader) {
/*  57 */     int start = reader.getCursor();
/*     */     
/*  59 */     while (reader.canRead() && isAllowedPatternCharacter(reader.peek())) {
/*  60 */       reader.skip();
/*     */     }
/*     */     
/*  63 */     return reader.getString().substring(start, reader.getCursor());
/*     */   }
/*     */ 
/*     */   
/*  67 */   private static boolean isAllowedPatternCharacter(char character) { return (Identifier.isAllowedInIdentifier(character) || character == '*' || character == '?'); }
/*     */ 
/*     */   
/*     */   private static String ensureNamespaced(String input) {
/*  71 */     if (!input.contains(":")) {
/*  72 */       return "minecraft:" + input;
/*     */     }
/*  74 */     return input;
/*     */   }
/*     */ 
/*     */   
/*  78 */   private static boolean matches(String pattern, Identifier key) { return FilenameUtils.wildcardMatch(key.toString(), pattern); }
/*     */ 
/*     */ 
/*     */   
/*  82 */   public static <T> ResourceSelectorArgument<T> resourceSelector(CommandBuildContext context, ResourceKey<? extends Registry<T>> registry) { return new ResourceSelectorArgument(context, registry); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public static <T> Collection<Holder.Reference<T>> getSelectedResources(CommandContext<CommandSourceStack> context, String name) { return (Collection)context.getArgument(name, Collection.class); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) { return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ELEMENTS); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public Collection<String> getExamples() { return EXAMPLES; }
/*     */   
/*     */   public static class Info<T>
/*     */     extends Object
/*     */     implements ArgumentTypeInfo<ResourceSelectorArgument<T>, Info<T>.Template> {
/*     */     public final class Template extends Object implements ArgumentTypeInfo.Template<ResourceSelectorArgument<T>> {
/*     */       private final ResourceKey<? extends Registry<T>> registryKey;
/*     */       
/* 105 */       private Template(ResourceKey<? extends Registry<T>> registryKey) { this.registryKey = registryKey; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 110 */       public ResourceSelectorArgument<T> instantiate(CommandBuildContext context) { return new ResourceSelectorArgument(context, this.registryKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 115 */       public ArgumentTypeInfo<ResourceSelectorArgument<T>, ?> type() { return ResourceSelectorArgument.Info.this; }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     public void serializeToNetwork(Template template, FriendlyByteBuf out) { out.writeResourceKey(template.registryKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     public Template deserializeFromNetwork(FriendlyByteBuf in) { return new Template(in.readRegistryKey()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     public void serializeToJson(Template template, JsonObject out) { out.addProperty("registry", template.registryKey.identifier().toString()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     public Template unpack(ResourceSelectorArgument<T> argument) { return new Template(argument.registryKey); }
/*     */   }
/*     */   
/*     */   public final class Template extends Object implements ArgumentTypeInfo.Template<ResourceSelectorArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) { this.registryKey = registryKey; }
/*     */     
/*     */     public ResourceSelectorArgument<T> instantiate(CommandBuildContext context) { return new ResourceSelectorArgument(context, this.registryKey); }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceSelectorArgument<T>, ?> type() { return ResourceSelectorArgument.Info.this; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ResourceSelectorArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
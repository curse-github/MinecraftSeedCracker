/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeManager;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ 
/*     */ 
/*     */ public class ResourceKeyArgument<T>
/*     */   extends Object
/*     */   implements ArgumentType<ResourceKey<T>>
/*     */ {
/*  41 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "012" });
/*     */   
/*  43 */   private static final DynamicCommandExceptionType ERROR_INVALID_FEATURE = new DynamicCommandExceptionType(value -> Component.translatableEscape("commands.place.feature.invalid", new Object[] { value }));
/*  44 */   private static final DynamicCommandExceptionType ERROR_INVALID_STRUCTURE = new DynamicCommandExceptionType(value -> Component.translatableEscape("commands.place.structure.invalid", new Object[] { value }));
/*  45 */   private static final DynamicCommandExceptionType ERROR_INVALID_TEMPLATE_POOL = new DynamicCommandExceptionType(value -> Component.translatableEscape("commands.place.jigsaw.invalid", new Object[] { value }));
/*  46 */   private static final DynamicCommandExceptionType ERROR_INVALID_RECIPE = new DynamicCommandExceptionType(value -> Component.translatableEscape("recipe.notFound", new Object[] { value }));
/*  47 */   private static final DynamicCommandExceptionType ERROR_INVALID_ADVANCEMENT = new DynamicCommandExceptionType(value -> Component.translatableEscape("advancement.advancementNotFound", new Object[] { value }));
/*     */   
/*     */   private final ResourceKey<? extends Registry<T>> registryKey;
/*     */ 
/*     */   
/*  52 */   public ResourceKeyArgument(ResourceKey<? extends Registry<T>> registryKey) { this.registryKey = registryKey; }
/*     */ 
/*     */ 
/*     */   
/*  56 */   public static <T> ResourceKeyArgument<T> key(ResourceKey<? extends Registry<T>> key) { return new ResourceKeyArgument(key); }
/*     */ 
/*     */   
/*     */   public static <T> ResourceKey<T> getRegistryKey(CommandContext<CommandSourceStack> context, String name, ResourceKey<Registry<T>> registryKey, DynamicCommandExceptionType exceptionType) throws CommandSyntaxException {
/*  60 */     ResourceKey<?> argument = (ResourceKey)context.getArgument(name, ResourceKey.class);
/*     */     
/*  62 */     Optional<ResourceKey<T>> value = argument.cast(registryKey);
/*  63 */     return (ResourceKey)value.orElseThrow(() -> exceptionType.create(argument.identifier()));
/*     */   }
/*     */ 
/*     */   
/*  67 */   private static <T> Registry<T> getRegistry(CommandContext<CommandSourceStack> context, ResourceKey<? extends Registry<T>> registryKey) { return ((CommandSourceStack)context.getSource()).getServer().registryAccess().lookupOrThrow(registryKey); }
/*     */ 
/*     */   
/*     */   private static <T> Holder.Reference<T> resolveKey(CommandContext<CommandSourceStack> context, String name, ResourceKey<Registry<T>> registryKey, DynamicCommandExceptionType exception) throws CommandSyntaxException {
/*  71 */     ResourceKey<T> key = getRegistryKey(context, name, registryKey, exception);
/*  72 */     return (Holder.Reference)getRegistry(context, registryKey).get(key).orElseThrow(() -> exception.create(key.identifier()));
/*     */   }
/*     */ 
/*     */   
/*  76 */   public static Holder.Reference<ConfiguredFeature<?, ?>> getConfiguredFeature(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return resolveKey(context, name, Registries.CONFIGURED_FEATURE, ERROR_INVALID_FEATURE); }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public static Holder.Reference<Structure> getStructure(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return resolveKey(context, name, Registries.STRUCTURE, ERROR_INVALID_STRUCTURE); }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public static Holder.Reference<StructureTemplatePool> getStructureTemplatePool(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return resolveKey(context, name, Registries.TEMPLATE_POOL, ERROR_INVALID_TEMPLATE_POOL); }
/*     */ 
/*     */   
/*     */   public static RecipeHolder<?> getRecipe(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  88 */     RecipeManager recipeManager = ((CommandSourceStack)context.getSource()).getServer().getRecipeManager();
/*  89 */     ResourceKey<Recipe<?>> key = getRegistryKey(context, name, Registries.RECIPE, ERROR_INVALID_RECIPE);
/*  90 */     return (RecipeHolder)recipeManager.byKey(key).orElseThrow(() -> ERROR_INVALID_RECIPE.create(key.identifier()));
/*     */   }
/*     */   
/*     */   public static AdvancementHolder getAdvancement(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  94 */     ResourceKey<Advancement> key = getRegistryKey(context, name, Registries.ADVANCEMENT, ERROR_INVALID_ADVANCEMENT);
/*  95 */     AdvancementHolder advancement = ((CommandSourceStack)context.getSource()).getServer().getAdvancements().get(key.identifier());
/*  96 */     if (advancement == null) {
/*  97 */       throw ERROR_INVALID_ADVANCEMENT.create(key.identifier());
/*     */     }
/*  99 */     return advancement;
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceKey<T> parse(StringReader reader) throws CommandSyntaxException {
/* 104 */     Identifier resourceId = Identifier.read(reader);
/* 105 */     return ResourceKey.create(this.registryKey, resourceId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) { return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ELEMENTS); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public Collection<String> getExamples() { return EXAMPLES; }
/*     */   
/*     */   public static class Info<T>
/*     */     extends Object
/*     */     implements ArgumentTypeInfo<ResourceKeyArgument<T>, Info<T>.Template> {
/*     */     public final class Template extends Object implements ArgumentTypeInfo.Template<ResourceKeyArgument<T>> {
/*     */       private final ResourceKey<? extends Registry<T>> registryKey;
/*     */       
/* 123 */       private Template(ResourceKey<? extends Registry<T>> registryKey) { this.registryKey = registryKey; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 128 */       public ResourceKeyArgument<T> instantiate(CommandBuildContext context) { return new ResourceKeyArgument(this.registryKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 133 */       public ArgumentTypeInfo<ResourceKeyArgument<T>, ?> type() { return ResourceKeyArgument.Info.this; }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     public void serializeToNetwork(Template template, FriendlyByteBuf out) { out.writeResourceKey(template.registryKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     public Template deserializeFromNetwork(FriendlyByteBuf in) { return new Template(in.readRegistryKey()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     public void serializeToJson(Template template, JsonObject out) { out.addProperty("registry", template.registryKey.identifier().toString()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     public Template unpack(ResourceKeyArgument<T> argument) { return new Template(argument.registryKey); }
/*     */   }
/*     */   
/*     */   public final class Template extends Object implements ArgumentTypeInfo.Template<ResourceKeyArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) { this.registryKey = registryKey; }
/*     */     
/*     */     public ResourceKeyArgument<T> instantiate(CommandBuildContext context) { return new ResourceKeyArgument(this.registryKey); }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceKeyArgument<T>, ?> type() { return ResourceKeyArgument.Info.this; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ResourceKeyArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
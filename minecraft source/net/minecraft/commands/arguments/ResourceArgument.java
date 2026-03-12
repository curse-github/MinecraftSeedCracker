/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ 
/*     */ public class ResourceArgument<T> extends Object implements ArgumentType<Holder.Reference<T>> {
/*  37 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "012" });
/*     */   
/*  39 */   private static final DynamicCommandExceptionType ERROR_NOT_SUMMONABLE_ENTITY = new DynamicCommandExceptionType(value -> Component.translatableEscape("entity.not_summonable", new Object[] { value }));
/*     */   
/*  41 */   public static final Dynamic2CommandExceptionType ERROR_UNKNOWN_RESOURCE = new Dynamic2CommandExceptionType((id, registry) -> Component.translatableEscape("argument.resource.not_found", new Object[] { id, registry }));
/*  42 */   public static final Dynamic3CommandExceptionType ERROR_INVALID_RESOURCE_TYPE = new Dynamic3CommandExceptionType((id, actualRegistry, expectedRegistry) -> Component.translatableEscape("argument.resource.invalid_type", new Object[] { id, actualRegistry, expectedRegistry }));
/*     */   
/*     */   private final ResourceKey<? extends Registry<T>> registryKey;
/*     */   private final HolderLookup<T> registryLookup;
/*     */   
/*     */   public ResourceArgument(CommandBuildContext context, ResourceKey<? extends Registry<T>> registryKey) {
/*  48 */     this.registryKey = registryKey;
/*  49 */     this.registryLookup = context.lookupOrThrow(registryKey);
/*     */   }
/*     */ 
/*     */   
/*  53 */   public static <T> ResourceArgument<T> resource(CommandBuildContext context, ResourceKey<? extends Registry<T>> key) { return new ResourceArgument(context, key); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Holder.Reference<T> getResource(CommandContext<CommandSourceStack> context, String name, ResourceKey<Registry<T>> registryKey) throws CommandSyntaxException {
/*  58 */     Holder.Reference<T> argument = (Holder.Reference)context.getArgument(name, Holder.Reference.class);
/*     */     
/*  60 */     ResourceKey<?> argumentKey = argument.key();
/*  61 */     if (argumentKey.isFor(registryKey)) {
/*  62 */       return argument;
/*     */     }
/*     */     
/*  65 */     throw ERROR_INVALID_RESOURCE_TYPE.create(argumentKey.identifier(), argumentKey.registry(), registryKey.identifier());
/*     */   }
/*     */ 
/*     */   
/*  69 */   public static Holder.Reference<Attribute> getAttribute(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return getResource(context, name, Registries.ATTRIBUTE); }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static Holder.Reference<ConfiguredFeature<?, ?>> getConfiguredFeature(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return getResource(context, name, Registries.CONFIGURED_FEATURE); }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public static Holder.Reference<Structure> getStructure(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return getResource(context, name, Registries.STRUCTURE); }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public static Holder.Reference<EntityType<?>> getEntityType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return getResource(context, name, Registries.ENTITY_TYPE); }
/*     */ 
/*     */   
/*     */   public static Holder.Reference<EntityType<?>> getSummonableEntityType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  85 */     Holder.Reference<EntityType<?>> result = getResource(context, name, Registries.ENTITY_TYPE);
/*  86 */     if (!((EntityType)result.value()).canSummon()) {
/*  87 */       throw ERROR_NOT_SUMMONABLE_ENTITY.create(result.key().identifier().toString());
/*     */     }
/*  89 */     return result;
/*     */   }
/*     */ 
/*     */   
/*  93 */   public static Holder.Reference<MobEffect> getMobEffect(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return getResource(context, name, Registries.MOB_EFFECT); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public static Holder.Reference<Enchantment> getEnchantment(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return getResource(context, name, Registries.ENCHANTMENT); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Holder.Reference<T> parse(StringReader reader) throws CommandSyntaxException {
/* 102 */     Identifier resourceId = Identifier.read(reader);
/* 103 */     ResourceKey<T> keyInRegistry = ResourceKey.create(this.registryKey, resourceId);
/* 104 */     return (Holder.Reference)this.registryLookup.get(keyInRegistry).orElseThrow(() -> ERROR_UNKNOWN_RESOURCE.createWithContext(reader, resourceId, this.registryKey.identifier()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 109 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) { return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ELEMENTS); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   public Collection<String> getExamples() { return EXAMPLES; }
/*     */   
/*     */   public static class Info<T>
/*     */     extends Object
/*     */     implements ArgumentTypeInfo<ResourceArgument<T>, Info<T>.Template> {
/*     */     public final class Template extends Object implements ArgumentTypeInfo.Template<ResourceArgument<T>> {
/*     */       private final ResourceKey<? extends Registry<T>> registryKey;
/*     */       
/* 122 */       private Template(ResourceKey<? extends Registry<T>> registryKey) { this.registryKey = registryKey; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 127 */       public ResourceArgument<T> instantiate(CommandBuildContext context) { return new ResourceArgument(context, this.registryKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 132 */       public ArgumentTypeInfo<ResourceArgument<T>, ?> type() { return ResourceArgument.Info.this; }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     public void serializeToNetwork(Template template, FriendlyByteBuf out) { out.writeResourceKey(template.registryKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     public Template deserializeFromNetwork(FriendlyByteBuf in) { return new Template(in.readRegistryKey()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 148 */     public void serializeToJson(Template template, JsonObject out) { out.addProperty("registry", template.registryKey.identifier().toString()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     public Template unpack(ResourceArgument<T> argument) { return new Template(argument.registryKey); }
/*     */   }
/*     */   
/*     */   public final class Template extends Object implements ArgumentTypeInfo.Template<ResourceArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) { this.registryKey = registryKey; }
/*     */     
/*     */     public ResourceArgument<T> instantiate(CommandBuildContext context) { return new ResourceArgument(context, this.registryKey); }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceArgument<T>, ?> type() { return ResourceArgument.Info.this; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ResourceArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
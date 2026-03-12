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
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ public class ResourceOrTagArgument<T> extends Object implements ArgumentType<ResourceOrTagArgument.Result<T>> {
/*  34 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "012", "#skeletons", "#minecraft:skeletons" });
/*     */   
/*  36 */   private static final Dynamic2CommandExceptionType ERROR_UNKNOWN_TAG = new Dynamic2CommandExceptionType((id, registry) -> Component.translatableEscape("argument.resource_tag.not_found", new Object[] { id, registry }));
/*  37 */   private static final Dynamic3CommandExceptionType ERROR_INVALID_TAG_TYPE = new Dynamic3CommandExceptionType((id, actualRegistry, expectedRegistry) -> Component.translatableEscape("argument.resource_tag.invalid_type", new Object[] { id, actualRegistry, expectedRegistry }));
/*     */   private final HolderLookup<T> registryLookup;
/*     */   private final ResourceKey<? extends Registry<T>> registryKey;
/*     */   
/*     */   private static final class ResourceResult<T>
/*     */     extends Record implements Result<T> {
/*     */     private final Holder.Reference<T> value;
/*     */     
/*     */     public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult<TT;>; }
/*     */     
/*  47 */     private ResourceResult(Holder.Reference<T> value) { this.value = value; } public Holder.Reference<T> value() { return this.value; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult<TT;>; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$ResourceResult<TT;>; }
/*  50 */     public Either<Holder.Reference<T>, HolderSet.Named<T>> unwrap() { return Either.left(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     public <E> Optional<ResourceOrTagArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> registryKey) { return this.value.key().isFor(registryKey) ? Optional.of(this) : Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     public boolean test(Holder<T> holder) { return holder.equals(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     public String asPrintable() { return this.value.key().identifier().toString(); } }
/*     */   private static final class TagResult<T> extends Record implements Result<T> { private final HolderSet.Named<T> tag;
/*     */     public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult<TT;>; }
/*     */     
/*  70 */     private TagResult(HolderSet.Named<T> tag) { this.tag = tag; } public HolderSet.Named<T> tag() { return this.tag; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult<TT;>; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagArgument$TagResult<TT;>; }
/*  73 */     public Either<Holder.Reference<T>, HolderSet.Named<T>> unwrap() { return Either.right(this.tag); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     public <E> Optional<ResourceOrTagArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> registryKey) { return this.tag.key().isFor(registryKey) ? Optional.of(this) : Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     public boolean test(Holder<T> holder) { return this.tag.contains(holder); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     public String asPrintable() { return "#" + String.valueOf(this.tag.key().location()); } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceOrTagArgument(CommandBuildContext context, ResourceKey<? extends Registry<T>> registryKey) {
/*  97 */     this.registryKey = registryKey;
/*  98 */     this.registryLookup = context.lookupOrThrow(registryKey);
/*     */   }
/*     */ 
/*     */   
/* 102 */   public static <T> ResourceOrTagArgument<T> resourceOrTag(CommandBuildContext context, ResourceKey<? extends Registry<T>> key) { return new ResourceOrTagArgument(context, key); }
/*     */ 
/*     */   
/*     */   public static <T> Result<T> getResourceOrTag(CommandContext<CommandSourceStack> context, String name, ResourceKey<Registry<T>> registryKey) throws CommandSyntaxException {
/* 106 */     Result<?> argument = (Result)context.getArgument(name, Result.class);
/*     */     
/* 108 */     Optional<Result<T>> value = argument.cast(registryKey);
/* 109 */     return (Result)value.orElseThrow(() -> (CommandSyntaxException)argument.unwrap().map((), ()));
/*     */   }
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
/*     */   public Result<T> parse(StringReader reader) throws CommandSyntaxException {
/* 123 */     if (reader.canRead() && reader.peek() == '#') {
/* 124 */       int cursor = reader.getCursor();
/*     */       try {
/* 126 */         reader.skip();
/* 127 */         Identifier tagId = Identifier.read(reader);
/* 128 */         TagKey<T> tagKey = TagKey.create(this.registryKey, tagId);
/* 129 */         HolderSet.Named<T> holderSet = (HolderSet.Named)this.registryLookup.get(tagKey).orElseThrow(() -> ERROR_UNKNOWN_TAG.createWithContext(reader, tagId, this.registryKey.identifier()));
/* 130 */         return new TagResult(holderSet);
/* 131 */       } catch (CommandSyntaxException e) {
/* 132 */         reader.setCursor(cursor);
/* 133 */         throw e;
/*     */       } 
/*     */     } 
/* 136 */     Identifier resourceId = Identifier.read(reader);
/* 137 */     ResourceKey<T> resourceKey = ResourceKey.create(this.registryKey, resourceId);
/* 138 */     Holder.Reference<T> holder = (Holder.Reference)this.registryLookup.get(resourceKey).orElseThrow(() -> ResourceArgument.ERROR_UNKNOWN_RESOURCE.createWithContext(reader, resourceId, this.registryKey.identifier()));
/* 139 */     return new ResourceResult(holder);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 144 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) { return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ALL); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 149 */   public Collection<String> getExamples() { return EXAMPLES; }
/*     */   
/*     */   public static class Info<T>
/*     */     extends Object
/*     */     implements ArgumentTypeInfo<ResourceOrTagArgument<T>, Info<T>.Template> {
/*     */     public final class Template extends Object implements ArgumentTypeInfo.Template<ResourceOrTagArgument<T>> {
/*     */       private final ResourceKey<? extends Registry<T>> registryKey;
/*     */       
/* 157 */       private Template(ResourceKey<? extends Registry<T>> registryKey) { this.registryKey = registryKey; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 162 */       public ResourceOrTagArgument<T> instantiate(CommandBuildContext context) { return new ResourceOrTagArgument(context, this.registryKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 167 */       public ArgumentTypeInfo<ResourceOrTagArgument<T>, ?> type() { return ResourceOrTagArgument.Info.this; }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     public void serializeToNetwork(Template template, FriendlyByteBuf out) { out.writeResourceKey(template.registryKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     public Template deserializeFromNetwork(FriendlyByteBuf in) { return new Template(in.readRegistryKey()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     public void serializeToJson(Template template, JsonObject out) { out.addProperty("registry", template.registryKey.identifier().toString()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     public Template unpack(ResourceOrTagArgument<T> argument) { return new Template(argument.registryKey); }
/*     */   }
/*     */   
/*     */   public final class Template extends Object implements ArgumentTypeInfo.Template<ResourceOrTagArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) { this.registryKey = registryKey; }
/*     */     
/*     */     public ResourceOrTagArgument<T> instantiate(CommandBuildContext context) { return new ResourceOrTagArgument(context, this.registryKey); }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceOrTagArgument<T>, ?> type() { return ResourceOrTagArgument.Info.this; }
/*     */   }
/*     */   
/*     */   public static interface Result<T> extends Predicate<Holder<T>> {
/*     */     Either<Holder.Reference<T>, HolderSet.Named<T>> unwrap();
/*     */     
/*     */     <E> Optional<Result<E>> cast(ResourceKey<? extends Registry<E>> param1ResourceKey);
/*     */     
/*     */     String asPrintable();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ResourceOrTagArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
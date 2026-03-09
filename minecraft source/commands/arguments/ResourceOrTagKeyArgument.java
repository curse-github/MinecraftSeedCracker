/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ 
/*     */ public class ResourceOrTagKeyArgument<T>
/*     */   extends Object
/*     */   implements ArgumentType<ResourceOrTagKeyArgument.Result<T>>
/*     */ {
/*  34 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "012", "#skeletons", "#minecraft:skeletons" });
/*     */   private final ResourceKey<? extends Registry<T>> registryKey;
/*     */   
/*     */   private static final class ResourceResult<T>
/*     */     extends Record
/*     */     implements Result<T> {
/*     */     private final ResourceKey<T> key;
/*     */     
/*     */     public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #44	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult<TT;>; }
/*     */     
/*  44 */     private ResourceResult(ResourceKey<T> key) { this.key = key; } public ResourceKey<T> key() { return this.key; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #44	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult<TT;>; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #44	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult<TT;>; }
/*  47 */     public Either<ResourceKey<T>, TagKey<T>> unwrap() { return Either.left(this.key); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     public <E> Optional<ResourceOrTagKeyArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> registryKey) { return this.key.cast(registryKey).map(ResourceResult::new); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  57 */     public boolean test(Holder<T> holder) { return holder.is(this.key); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     public String asPrintable() { return this.key.identifier().toString(); } }
/*     */   private static final class TagResult<T> extends Record implements Result<T> { private final TagKey<T> key;
/*     */     public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult<TT;>; }
/*     */     
/*  66 */     private TagResult(TagKey<T> key) { this.key = key; } public TagKey<T> key() { return this.key; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult<TT;>; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult<TT;>; }
/*  69 */     public Either<ResourceKey<T>, TagKey<T>> unwrap() { return Either.right(this.key); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     public <E> Optional<ResourceOrTagKeyArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> registryKey) { return this.key.cast(registryKey).map(TagResult::new); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     public boolean test(Holder<T> holder) { return holder.is(this.key); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     public String asPrintable() { return "#" + String.valueOf(this.key.location()); } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public ResourceOrTagKeyArgument(ResourceKey<? extends Registry<T>> registryKey) { this.registryKey = registryKey; }
/*     */ 
/*     */ 
/*     */   
/*  95 */   public static <T> ResourceOrTagKeyArgument<T> resourceOrTagKey(ResourceKey<? extends Registry<T>> key) { return new ResourceOrTagKeyArgument(key); }
/*     */ 
/*     */   
/*     */   public static <T> Result<T> getResourceOrTagKey(CommandContext<CommandSourceStack> context, String name, ResourceKey<Registry<T>> registryKey, DynamicCommandExceptionType exceptionType) throws CommandSyntaxException {
/*  99 */     Result<?> argument = (Result)context.getArgument(name, Result.class);
/*     */     
/* 101 */     Optional<Result<T>> value = argument.cast(registryKey);
/* 102 */     return (Result)value.orElseThrow(() -> exceptionType.create(argument));
/*     */   }
/*     */ 
/*     */   
/*     */   public Result<T> parse(StringReader reader) throws CommandSyntaxException {
/* 107 */     if (reader.canRead() && reader.peek() == '#') {
/* 108 */       int cursor = reader.getCursor();
/*     */       try {
/* 110 */         reader.skip();
/* 111 */         Identifier tagId = Identifier.read(reader);
/* 112 */         return new TagResult(TagKey.create(this.registryKey, tagId));
/* 113 */       } catch (CommandSyntaxException e) {
/* 114 */         reader.setCursor(cursor);
/* 115 */         throw e;
/*     */       } 
/*     */     } 
/* 118 */     Identifier resourceId = Identifier.read(reader);
/* 119 */     return new ResourceResult(ResourceKey.create(this.registryKey, resourceId));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) { return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ALL); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public Collection<String> getExamples() { return EXAMPLES; }
/*     */   
/*     */   public static class Info<T>
/*     */     extends Object
/*     */     implements ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, Info<T>.Template> {
/*     */     public final class Template extends Object implements ArgumentTypeInfo.Template<ResourceOrTagKeyArgument<T>> {
/*     */       private final ResourceKey<? extends Registry<T>> registryKey;
/*     */       
/* 137 */       private Template(ResourceKey<? extends Registry<T>> registryKey) { this.registryKey = registryKey; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 142 */       public ResourceOrTagKeyArgument<T> instantiate(CommandBuildContext context) { return new ResourceOrTagKeyArgument(this.registryKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 147 */       public ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, ?> type() { return ResourceOrTagKeyArgument.Info.this; }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     public void serializeToNetwork(Template template, FriendlyByteBuf out) { out.writeResourceKey(template.registryKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     public Template deserializeFromNetwork(FriendlyByteBuf in) { return new Template(in.readRegistryKey()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     public void serializeToJson(Template template, JsonObject out) { out.addProperty("registry", template.registryKey.identifier().toString()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 168 */     public Template unpack(ResourceOrTagKeyArgument<T> argument) { return new Template(argument.registryKey); }
/*     */   }
/*     */   
/*     */   public final class Template extends Object implements ArgumentTypeInfo.Template<ResourceOrTagKeyArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) { this.registryKey = registryKey; }
/*     */     
/*     */     public ResourceOrTagKeyArgument<T> instantiate(CommandBuildContext context) { return new ResourceOrTagKeyArgument(this.registryKey); }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, ?> type() { return ResourceOrTagKeyArgument.Info.this; }
/*     */   }
/*     */   
/*     */   public static interface Result<T> extends Predicate<Holder<T>> {
/*     */     Either<ResourceKey<T>, TagKey<T>> unwrap();
/*     */     
/*     */     <E> Optional<Result<E>> cast(ResourceKey<? extends Registry<E>> param1ResourceKey);
/*     */     
/*     */     String asPrintable();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ResourceOrTagKeyArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
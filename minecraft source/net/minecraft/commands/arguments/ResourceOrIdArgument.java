/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.SnbtGrammar;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.util.parsing.packrat.Atom;
/*     */ import net.minecraft.util.parsing.packrat.Dictionary;
/*     */ import net.minecraft.util.parsing.packrat.NamedRule;
/*     */ import net.minecraft.util.parsing.packrat.Scope;
/*     */ import net.minecraft.util.parsing.packrat.Term;
/*     */ import net.minecraft.util.parsing.packrat.commands.Grammar;
/*     */ import net.minecraft.util.parsing.packrat.commands.IdentifierParseRule;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class ResourceOrIdArgument<T>
/*     */   extends Object implements ArgumentType<Holder<T>> {
/*  47 */   private static final Collection<String> EXAMPLES = List.of("foo", "foo:bar", "012", "{}", "true");
/*     */   
/*  49 */   public static final DynamicCommandExceptionType ERROR_FAILED_TO_PARSE = new DynamicCommandExceptionType(error -> Component.translatableEscape("argument.resource_or_id.failed_to_parse", new Object[] { error }));
/*  50 */   public static final Dynamic2CommandExceptionType ERROR_NO_SUCH_ELEMENT = new Dynamic2CommandExceptionType((id, registry) -> Component.translatableEscape("argument.resource_or_id.no_such_element", new Object[] { id, registry }));
/*     */   
/*  52 */   public static final DynamicOps<Tag> OPS = NbtOps.INSTANCE;
/*     */   
/*     */   private final HolderLookup.Provider registryLookup;
/*     */   private final Optional<? extends HolderLookup.RegistryLookup<T>> elementLookup;
/*     */   private final Codec<T> codec;
/*     */   private final Grammar<Result<T, Tag>> grammar;
/*     */   private final ResourceKey<? extends Registry<T>> registryKey;
/*     */   
/*     */   protected ResourceOrIdArgument(CommandBuildContext context, ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec) {
/*  61 */     this.registryLookup = context;
/*  62 */     this.elementLookup = context.lookup(registryKey);
/*  63 */     this.registryKey = registryKey;
/*  64 */     this.codec = codec;
/*     */     
/*  66 */     this.grammar = createGrammar(registryKey, OPS);
/*     */   }
/*     */   
/*     */   public static <T, O> Grammar<Result<T, O>> createGrammar(ResourceKey<? extends Registry<T>> registryKey, DynamicOps<O> ops) {
/*  70 */     Grammar<O> inlineValueGrammar = SnbtGrammar.createParser(ops);
/*     */     
/*  72 */     Dictionary<StringReader> rules = new Dictionary<StringReader>();
/*  73 */     Atom<Result<T, O>> result = Atom.of("result");
/*  74 */     Atom<Identifier> id = Atom.of("id");
/*  75 */     Atom<O> value = Atom.of("value");
/*     */     
/*  77 */     rules.put(id, IdentifierParseRule.INSTANCE);
/*  78 */     rules.put(value, inlineValueGrammar.top().value());
/*     */     
/*  80 */     NamedRule<StringReader, Result<T, O>> topRule = rules.put(result, 
/*  81 */         Term.alternative(new Term[] {
/*     */ 
/*     */             
/*  84 */             rules.named(id), rules
/*  85 */             .named(value)
/*     */           }, ), scope -> {
/*  87 */           Identifier parsedId = (Identifier)scope.get(id);
/*  88 */           if (parsedId != null) {
/*  89 */             return new ReferenceResult(ResourceKey.create(registryKey, parsedId));
/*     */           }
/*     */           
/*  92 */           O parsedInline = (O)scope.getOrThrow(value);
/*  93 */           return new InlineResult(parsedInline);
/*     */         });
/*     */ 
/*     */     
/*  97 */     return new Grammar(rules, topRule);
/*     */   }
/*     */   
/*     */   public static class LootTableArgument
/*     */     extends ResourceOrIdArgument<LootTable> {
/* 102 */     protected LootTableArgument(CommandBuildContext context) { super(context, Registries.LOOT_TABLE, LootTable.DIRECT_CODEC); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public static LootTableArgument lootTable(CommandBuildContext context) { return new LootTableArgument(context); }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public static Holder<LootTable> getLootTable(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return getResource(context, name); }
/*     */   
/*     */   public static class LootModifierArgument
/*     */     extends ResourceOrIdArgument<LootItemFunction>
/*     */   {
/* 116 */     protected LootModifierArgument(CommandBuildContext context) { super(context, Registries.ITEM_MODIFIER, LootItemFunctions.ROOT_CODEC); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 121 */   public static LootModifierArgument lootModifier(CommandBuildContext context) { return new LootModifierArgument(context); }
/*     */ 
/*     */ 
/*     */   
/* 125 */   public static Holder<LootItemFunction> getLootModifier(CommandContext<CommandSourceStack> context, String name) { return getResource(context, name); }
/*     */   
/*     */   public static class LootPredicateArgument
/*     */     extends ResourceOrIdArgument<LootItemCondition>
/*     */   {
/* 130 */     protected LootPredicateArgument(CommandBuildContext context) { super(context, Registries.PREDICATE, LootItemCondition.DIRECT_CODEC); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public static LootPredicateArgument lootPredicate(CommandBuildContext context) { return new LootPredicateArgument(context); }
/*     */ 
/*     */ 
/*     */   
/* 139 */   public static Holder<LootItemCondition> getLootPredicate(CommandContext<CommandSourceStack> context, String name) { return getResource(context, name); }
/*     */   
/*     */   public static class DialogArgument
/*     */     extends ResourceOrIdArgument<Dialog>
/*     */   {
/* 144 */     protected DialogArgument(CommandBuildContext context) { super(context, Registries.DIALOG, Dialog.DIRECT_CODEC); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 149 */   public static DialogArgument dialog(CommandBuildContext context) { return new DialogArgument(context); }
/*     */ 
/*     */ 
/*     */   
/* 153 */   public static Holder<Dialog> getDialog(CommandContext<CommandSourceStack> context, String name) { return getResource(context, name); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   private static <T> Holder<T> getResource(CommandContext<CommandSourceStack> context, String name) { return (Holder)context.getArgument(name, Holder.class); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   public Holder<T> parse(StringReader reader) throws CommandSyntaxException { return parse(reader, this.grammar, OPS); }
/*     */ 
/*     */   
/*     */   private <O> Holder<T> parse(StringReader reader, Grammar<Result<T, O>> grammar, DynamicOps<O> ops) throws CommandSyntaxException {
/* 167 */     Result<T, O> contents = (Result)grammar.parseForCommands(reader);
/*     */     
/* 169 */     if (this.elementLookup.isEmpty())
/*     */     {
/* 171 */       return null;
/*     */     }
/*     */     
/* 174 */     return contents.parse(reader, this.registryLookup, ops, this.codec, (HolderLookup.RegistryLookup)this.elementLookup.get());
/*     */   }
/*     */   
/*     */   public static final class InlineResult<T, O>
/*     */     extends Record implements Result<T, O> {
/*     */     private final O value;
/*     */     
/* 181 */     public InlineResult(O value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #181	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 181 */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult<TT;TO;>; } public O value() { return (O)this.value; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #181	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult<TT;TO;>; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #181	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$InlineResult<TT;TO;>; }
/*     */     public Holder<T> parse(ImmutableStringReader reader, HolderLookup.Provider lookup, DynamicOps<O> ops, Codec<T> codec, HolderLookup.RegistryLookup<T> elementLookup) throws CommandSyntaxException {
/* 184 */       return Holder.direct(codec.parse(lookup.createSerializationContext(ops), this.value)
/* 185 */           .getOrThrow(msg -> ResourceOrIdArgument.ERROR_FAILED_TO_PARSE.createWithContext(reader, msg)));
/*     */     } }
/*     */   public static final class ReferenceResult<T, O> extends Record implements Result<T, O> { private final ResourceKey<T> key;
/*     */     
/* 189 */     public ReferenceResult(ResourceKey<T> key) { this.key = key; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #189	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult<TT;TO;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #189	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult<TT;TO;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #189	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 189 */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrIdArgument$ReferenceResult<TT;TO;>; } public ResourceKey<T> key() { return this.key; }
/*     */ 
/*     */     
/* 192 */     public Holder<T> parse(ImmutableStringReader reader, HolderLookup.Provider lookup, DynamicOps<O> ops, Codec<T> codec, HolderLookup.RegistryLookup<T> elementLookup) throws CommandSyntaxException { return (Holder)elementLookup.get(this.key).orElseThrow(() -> ResourceOrIdArgument.ERROR_NO_SUCH_ELEMENT.createWithContext(reader, this.key.identifier(), this.key.registry())); } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 198 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) { return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ELEMENTS); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   public Collection<String> getExamples() { return EXAMPLES; }
/*     */   
/*     */   public static interface Result<T, O> {
/*     */     Holder<T> parse(ImmutableStringReader param1ImmutableStringReader, HolderLookup.Provider param1Provider, DynamicOps<O> param1DynamicOps, Codec<T> param1Codec, HolderLookup.RegistryLookup<T> param1RegistryLookup) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ResourceOrIdArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
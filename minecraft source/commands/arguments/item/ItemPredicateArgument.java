/*     */ package net.minecraft.commands.arguments.item;
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.parsing.packrat.commands.ParserBasedArgument;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class ItemPredicateArgument extends ParserBasedArgument<ItemPredicateArgument.Result> {
/*  42 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "stick", "minecraft:stick", "#stick", "#stick{foo:'bar'}" });
/*     */   
/*  44 */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_ITEM = new DynamicCommandExceptionType(id -> Component.translatableEscape("argument.item.id.invalid", new Object[] { id }));
/*  45 */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_TAG = new DynamicCommandExceptionType(tag -> Component.translatableEscape("arguments.item.tag.unknown", new Object[] { tag }));
/*  46 */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_COMPONENT = new DynamicCommandExceptionType(id -> Component.translatableEscape("arguments.item.component.unknown", new Object[] { id }));
/*  47 */   private static final Dynamic2CommandExceptionType ERROR_MALFORMED_COMPONENT = new Dynamic2CommandExceptionType((type, message) -> Component.translatableEscape("arguments.item.component.malformed", new Object[] { type, message }));
/*  48 */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_PREDICATE = new DynamicCommandExceptionType(id -> Component.translatableEscape("arguments.item.predicate.unknown", new Object[] { id }));
/*  49 */   private static final Dynamic2CommandExceptionType ERROR_MALFORMED_PREDICATE = new Dynamic2CommandExceptionType((type, message) -> Component.translatableEscape("arguments.item.predicate.malformed", new Object[] { type, message }));
/*     */   private static final class ComponentWrapper extends Record { private final Identifier id; private final Predicate<ItemStack> presenceChecker; private final Decoder<? extends Predicate<ItemStack>> valueChecker;
/*  51 */     private ComponentWrapper(Identifier id, Predicate<ItemStack> presenceChecker, Decoder<? extends Predicate<ItemStack>> valueChecker) { this.id = id; this.presenceChecker = presenceChecker; this.valueChecker = valueChecker; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #51	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  51 */       //   0	7	0	this	Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #51	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #51	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper;
/*  51 */       //   0	8	1	o	Ljava/lang/Object; } public Predicate<ItemStack> presenceChecker() { return this.presenceChecker; } public Decoder<? extends Predicate<ItemStack>> valueChecker() { return this.valueChecker; }
/*     */     public static <T> ComponentWrapper create(ImmutableStringReader reader, Identifier id, DataComponentType<T> type) throws CommandSyntaxException {
/*  53 */       Codec<T> codec = type.codec();
/*  54 */       if (codec == null) {
/*  55 */         throw ItemPredicateArgument.ERROR_UNKNOWN_COMPONENT.createWithContext(reader, id);
/*     */       }
/*     */       
/*  58 */       return new ComponentWrapper(id, itemStack -> 
/*     */           
/*  60 */           itemStack.has(type), codec
/*  61 */           .map(expected -> ()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Predicate<ItemStack> decode(ImmutableStringReader reader, Dynamic<?> value) throws CommandSyntaxException {
/*  69 */       DataResult<? extends Predicate<ItemStack>> result = this.valueChecker.parse(value);
/*  70 */       return (Predicate)result.getOrThrow(message -> ItemPredicateArgument.ERROR_MALFORMED_COMPONENT.createWithContext(reader, this.id.toString(), message));
/*     */     } }
/*     */   private static final class PredicateWrapper extends Record { private final Identifier id; private final Decoder<? extends Predicate<ItemStack>> type;
/*     */     
/*  74 */     private PredicateWrapper(Identifier id, Decoder<? extends Predicate<ItemStack>> type) { this.id = id; this.type = type; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$PredicateWrapper;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #74	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$PredicateWrapper; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$PredicateWrapper;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #74	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$PredicateWrapper; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$PredicateWrapper;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #74	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/item/ItemPredicateArgument$PredicateWrapper;
/*  74 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier id() { return this.id; } public Decoder<? extends Predicate<ItemStack>> type() { return this.type; }
/*     */     public PredicateWrapper(Holder.Reference<DataComponentPredicate.Type<?>> holder) {
/*  76 */       this(holder
/*  77 */           .key().identifier(), ((DataComponentPredicate.Type)holder
/*  78 */           .value()).codec().map(v -> { Objects.requireNonNull(v); return v::matches;
/*     */             }));
/*     */     }
/*     */     
/*     */     public Predicate<ItemStack> decode(ImmutableStringReader reader, Dynamic<?> value) throws CommandSyntaxException {
/*  83 */       DataResult<? extends Predicate<ItemStack>> result = this.type.parse(value);
/*  84 */       return (Predicate)result.getOrThrow(message -> ItemPredicateArgument.ERROR_MALFORMED_PREDICATE.createWithContext(reader, this.id.toString(), message));
/*     */     } }
/*     */ 
/*     */   
/*     */   private static PredicateWrapper createComponentExistencePredicate(Holder.Reference<DataComponentType<?>> componentId) {
/*  89 */     Predicate<ItemStack> componentExists = itemStack -> itemStack.has((DataComponentType)componentId.value());
/*  90 */     return new PredicateWrapper(componentId.key().identifier(), Unit.CODEC.map(unit -> componentExists));
/*     */   }
/*     */   
/*  93 */   private static final Identifier COUNT_ID = Identifier.withDefaultNamespace("count");
/*     */   
/*  95 */   private static final Map<Identifier, ComponentWrapper> PSEUDO_COMPONENTS = (Map)Stream.of(new ComponentWrapper(COUNT_ID, itemStack -> true, MinMaxBounds.Ints.CODEC
/*  96 */         .map(range -> ())))
/*  97 */     .collect(Collectors.toUnmodifiableMap(ComponentWrapper::id, e -> e));
/*     */   
/*  99 */   private static final Map<Identifier, PredicateWrapper> PSEUDO_PREDICATES = (Map)Stream.of(new PredicateWrapper(COUNT_ID, MinMaxBounds.Ints.CODEC
/* 100 */         .map(range -> ())))
/* 101 */     .collect(Collectors.toUnmodifiableMap(PredicateWrapper::id, e -> e));
/*     */   
/*     */   public ItemPredicateArgument(CommandBuildContext registries) {
/* 104 */     super(
/* 105 */         ComponentPredicateParser.createGrammar(new Context(registries))
/* 106 */         .mapResult(predicates -> { Objects.requireNonNull(Util.allOf(predicates)); return Util.allOf(predicates)::test;
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/* 111 */   public static ItemPredicateArgument itemPredicate(CommandBuildContext context) { return new ItemPredicateArgument(context); }
/*     */   
/*     */   private static class Context
/*     */     extends Object implements ComponentPredicateParser.Context<Predicate<ItemStack>, ComponentWrapper, PredicateWrapper> {
/*     */     private final HolderLookup.Provider registries;
/*     */     private final HolderLookup.RegistryLookup<Item> items;
/*     */     private final HolderLookup.RegistryLookup<DataComponentType<?>> components;
/*     */     private final HolderLookup.RegistryLookup<DataComponentPredicate.Type<?>> predicates;
/*     */     
/*     */     private Context(HolderLookup.Provider registries) {
/* 121 */       this.registries = registries;
/* 122 */       this.items = registries.lookupOrThrow(Registries.ITEM);
/* 123 */       this.components = registries.lookupOrThrow(Registries.DATA_COMPONENT_TYPE);
/* 124 */       this.predicates = registries.lookupOrThrow(Registries.DATA_COMPONENT_PREDICATE_TYPE);
/*     */     }
/*     */ 
/*     */     
/*     */     public Predicate<ItemStack> forElementType(ImmutableStringReader reader, Identifier id) throws CommandSyntaxException {
/* 129 */       Holder.Reference<Item> item = (Holder.Reference)this.items.get(ResourceKey.create(Registries.ITEM, id)).orElseThrow(() -> ItemPredicateArgument.ERROR_UNKNOWN_ITEM.createWithContext(reader, id));
/* 130 */       return itemStack -> itemStack.is(item);
/*     */     }
/*     */ 
/*     */     
/*     */     public Predicate<ItemStack> forTagType(ImmutableStringReader reader, Identifier id) throws CommandSyntaxException {
/* 135 */       HolderSet<Item> tag = (HolderSet)this.items.get(TagKey.create(Registries.ITEM, id)).orElseThrow(() -> ItemPredicateArgument.ERROR_UNKNOWN_TAG.createWithContext(reader, id));
/* 136 */       return itemStack -> itemStack.is(tag);
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemPredicateArgument.ComponentWrapper lookupComponentType(ImmutableStringReader reader, Identifier componentId) throws CommandSyntaxException {
/* 141 */       ItemPredicateArgument.ComponentWrapper wrapper = (ItemPredicateArgument.ComponentWrapper)ItemPredicateArgument.PSEUDO_COMPONENTS.get(componentId);
/* 142 */       if (wrapper != null) {
/* 143 */         return wrapper;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 148 */       DataComponentType<?> componentType = (DataComponentType)this.components.get(ResourceKey.create(Registries.DATA_COMPONENT_TYPE, componentId)).map(Holder::value).orElseThrow(() -> ItemPredicateArgument.ERROR_UNKNOWN_COMPONENT.createWithContext(reader, componentId));
/* 149 */       return ItemPredicateArgument.ComponentWrapper.create(reader, componentId, componentType);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 154 */     public Predicate<ItemStack> createComponentTest(ImmutableStringReader reader, ItemPredicateArgument.ComponentWrapper componentType, Dynamic<?> value) throws CommandSyntaxException { return componentType.decode(reader, RegistryOps.injectRegistryContext(value, this.registries)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     public Predicate<ItemStack> createComponentTest(ImmutableStringReader reader, ItemPredicateArgument.ComponentWrapper componentType) { return componentType.presenceChecker; }
/*     */ 
/*     */ 
/*     */     
/*     */     public ItemPredicateArgument.PredicateWrapper lookupPredicateType(ImmutableStringReader reader, Identifier componentId) throws CommandSyntaxException {
/* 164 */       ItemPredicateArgument.PredicateWrapper wrapper = (ItemPredicateArgument.PredicateWrapper)ItemPredicateArgument.PSEUDO_PREDICATES.get(componentId);
/* 165 */       if (wrapper != null) {
/* 166 */         return wrapper;
/*     */       }
/*     */       
/* 169 */       return (ItemPredicateArgument.PredicateWrapper)this.predicates.get(ResourceKey.create(Registries.DATA_COMPONENT_PREDICATE_TYPE, componentId))
/* 170 */         .map(PredicateWrapper::new)
/* 171 */         .or(() -> 
/* 172 */           this.components.get(ResourceKey.create(Registries.DATA_COMPONENT_TYPE, componentId))
/* 173 */           .map(ItemPredicateArgument::createComponentExistencePredicate))
/*     */         
/* 175 */         .orElseThrow(() -> ItemPredicateArgument.ERROR_UNKNOWN_PREDICATE.createWithContext(reader, componentId));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 180 */     public Predicate<ItemStack> createPredicateTest(ImmutableStringReader reader, ItemPredicateArgument.PredicateWrapper predicateType, Dynamic<?> value) throws CommandSyntaxException { return predicateType.decode(reader, RegistryOps.injectRegistryContext(value, this.registries)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     public Stream<Identifier> listElementTypes() { return this.items.listElementIds().map(ResourceKey::identifier); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     public Stream<Identifier> listTagTypes() { return this.items.listTagIds().map(TagKey::location); }
/*     */ 
/*     */ 
/*     */     
/*     */     public Stream<Identifier> listComponentTypes() {
/* 195 */       return Stream.concat(ItemPredicateArgument.PSEUDO_COMPONENTS
/* 196 */           .keySet().stream(), this.components
/* 197 */           .listElements().filter(e -> !((DataComponentType)e.value()).isTransient()).map(e -> e.key().identifier()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Stream<Identifier> listPredicateTypes() {
/* 203 */       return Stream.concat(ItemPredicateArgument.PSEUDO_PREDICATES
/* 204 */           .keySet().stream(), this.predicates
/* 205 */           .listElementIds().map(ResourceKey::identifier));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     public Predicate<ItemStack> negate(Predicate<ItemStack> value) { return value.negate(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     public Predicate<ItemStack> anyOf(List<Predicate<ItemStack>> alternatives) { return Util.anyOf(alternatives); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 221 */   public static Result getItemPredicate(CommandContext<CommandSourceStack> context, String name) { return (Result)context.getArgument(name, Result.class); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 226 */   public Collection<String> getExamples() { return EXAMPLES; }
/*     */   
/*     */   public static interface Result extends Predicate<ItemStack> {}
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\item\ItemPredicateArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.commands.arguments.item;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.parsing.packrat.Atom;
/*     */ import net.minecraft.util.parsing.packrat.Dictionary;
/*     */ import net.minecraft.util.parsing.packrat.NamedRule;
/*     */ import net.minecraft.util.parsing.packrat.ParseState;
/*     */ import net.minecraft.util.parsing.packrat.Scope;
/*     */ import net.minecraft.util.parsing.packrat.Term;
/*     */ import net.minecraft.util.parsing.packrat.commands.Grammar;
/*     */ import net.minecraft.util.parsing.packrat.commands.IdentifierParseRule;
/*     */ import net.minecraft.util.parsing.packrat.commands.ResourceLookupRule;
/*     */ import net.minecraft.util.parsing.packrat.commands.StringReaderTerms;
/*     */ import net.minecraft.util.parsing.packrat.commands.TagParseRule;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ComponentPredicateParser
/*     */ {
/*     */   public static <T, C, P> Grammar<List<T>> createGrammar(Context<T, C, P> context) {
/*  34 */     Atom<List<T>> top = Atom.of("top");
/*  35 */     Atom<Optional<T>> type = Atom.of("type");
/*  36 */     Atom<Unit> anyType = Atom.of("any_type");
/*  37 */     Atom<T> elementType = Atom.of("element_type");
/*  38 */     Atom<T> tagType = Atom.of("tag_type");
/*  39 */     Atom<List<T>> conditions = Atom.of("conditions");
/*  40 */     Atom<List<T>> alternatives = Atom.of("alternatives");
/*  41 */     Atom<T> term = Atom.of("term");
/*  42 */     Atom<T> negation = Atom.of("negation");
/*  43 */     Atom<T> test = Atom.of("test");
/*  44 */     Atom<C> componentType = Atom.of("component_type");
/*  45 */     Atom<P> predicateType = Atom.of("predicate_type");
/*  46 */     Atom<Identifier> id = Atom.of("id");
/*  47 */     Atom<Dynamic<?>> tag = Atom.of("tag");
/*     */     
/*  49 */     Dictionary<StringReader> rules = new Dictionary<StringReader>();
/*     */     
/*  51 */     NamedRule<StringReader, Identifier> idRule = rules.put(id, IdentifierParseRule.INSTANCE);
/*     */     
/*  53 */     NamedRule<StringReader, List<T>> topRule = rules.put(top, 
/*  54 */         Term.alternative(new Term[] {
/*  55 */             Term.sequence(new Term[] { rules.named(type), (new Term[5][2] = (new Term[5][1] = StringReaderTerms.character('[')).cut()).optional(rules.named(conditions)), StringReaderTerms.character(']') }, ), rules
/*  56 */             .named(type)
/*     */           }, ), scope -> {
/*  58 */           ImmutableList.Builder<T> builder = ImmutableList.builder();
/*  59 */           Objects.requireNonNull(builder); ((Optional)scope.getOrThrow(type)).ifPresent(builder::add);
/*  60 */           List<T> parsedConditions = (List)scope.get(conditions);
/*  61 */           if (parsedConditions != null) {
/*  62 */             builder.addAll(parsedConditions);
/*     */           }
/*  64 */           return builder.build();
/*     */         });
/*     */     
/*  67 */     rules.put(type, Term.alternative(new Term[] { rules
/*  68 */             .named(elementType), 
/*  69 */             Term.sequence(new Term[] { null, (new Term[3][0] = StringReaderTerms.character('#')).cut(), rules.named(tagType) }, ), rules
/*  70 */             .named(anyType)
/*  71 */           }, ), scope -> Optional.ofNullable(scope.getAny(new Atom[] { elementType, tagType })));
/*     */     
/*  73 */     rules.put(anyType, StringReaderTerms.character('*'), s -> Unit.INSTANCE);
/*  74 */     rules.put(elementType, new ElementLookupRule(idRule, context));
/*  75 */     rules.put(tagType, new TagLookupRule(idRule, context));
/*     */     
/*  77 */     rules.put(conditions, 
/*  78 */         Term.sequence(new Term[] {
/*  79 */             rules.named(alternatives), 
/*  80 */             Term.optional(Term.sequence(new Term[] { StringReaderTerms.character(','), rules.named(conditions) }, ))
/*     */           }, ), scope -> {
/*     */           
/*  83 */           T parsedCondition = (T)context.anyOf((List)scope.getOrThrow(alternatives));
/*  84 */           return (List)Optional.ofNullable((List)scope.get(conditions))
/*  85 */             .map(())
/*  86 */             .orElse(List.of(parsedCondition));
/*     */         });
/*     */ 
/*     */     
/*  90 */     rules.put(alternatives, 
/*  91 */         Term.sequence(new Term[] {
/*  92 */             rules.named(term), 
/*  93 */             Term.optional(Term.sequence(new Term[] { StringReaderTerms.character('|'), rules.named(alternatives) }, ))
/*     */           }, ), scope -> {
/*     */           
/*  96 */           T alternative = (T)scope.getOrThrow(term);
/*  97 */           return (List)Optional.ofNullable((List)scope.get(alternatives))
/*  98 */             .map(())
/*  99 */             .orElse(List.of(alternative));
/*     */         });
/*     */ 
/*     */     
/* 103 */     rules.put(term, 
/* 104 */         Term.alternative(new Term[] {
/* 105 */             rules.named(test), 
/* 106 */             Term.sequence(new Term[] { StringReaderTerms.character('!'), rules.named(negation)
/*     */               }, )
/* 108 */           }, ), scope -> scope.getAnyOrThrow(new Atom[] { test, negation }));
/*     */ 
/*     */     
/* 111 */     rules.put(negation, rules
/* 112 */         .named(test), scope -> 
/* 113 */         context.negate(scope.getOrThrow(test)));
/*     */ 
/*     */     
/* 116 */     rules.putComplex(test, 
/* 117 */         Term.alternative(new Term[] {
/* 118 */             Term.sequence(new Term[] { rules.named(componentType), (new Term[4][1] = StringReaderTerms.character('=')).cut(), rules.named(tag)
/* 119 */               }, ), Term.sequence(new Term[] { rules.named(predicateType), (new Term[4][1] = StringReaderTerms.character('~')).cut(), rules.named(tag) }, ), rules
/* 120 */             .named(componentType)
/*     */           }, ), state -> {
/*     */           
/* 123 */           Scope scope = state.scope();
/* 124 */           P predicate = (P)scope.get(predicateType);
/*     */           try {
/* 126 */             if (predicate != null) {
/* 127 */               Dynamic<?> value = (Dynamic)scope.getOrThrow(tag);
/* 128 */               return context.createPredicateTest((ImmutableStringReader)state.input(), predicate, value);
/*     */             } 
/* 130 */             C component = (C)scope.getOrThrow(componentType);
/* 131 */             Dynamic<?> value = (Dynamic)scope.get(tag);
/* 132 */             return (value != null) ? 
/* 133 */               context.createComponentTest((ImmutableStringReader)state.input(), component, value) : 
/* 134 */               context.createComponentTest((ImmutableStringReader)state.input(), component);
/*     */           }
/* 136 */           catch (CommandSyntaxException e) {
/* 137 */             state.errorCollector().store(state.mark(), e);
/* 138 */             return null;
/*     */           } 
/*     */         });
/*     */ 
/*     */     
/* 143 */     rules.put(componentType, new ComponentLookupRule(idRule, context));
/* 144 */     rules.put(predicateType, new PredicateLookupRule(idRule, context));
/* 145 */     rules.put(tag, new TagParseRule(NbtOps.INSTANCE));
/*     */     
/* 147 */     return new Grammar(rules, topRule);
/*     */   }
/*     */   
/*     */   private static class ElementLookupRule<T, C, P>
/*     */     extends ResourceLookupRule<Context<T, C, P>, T> {
/* 152 */     private ElementLookupRule(NamedRule<StringReader, Identifier> idParser, ComponentPredicateParser.Context<T, C, P> context) { super(idParser, context); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     protected T validateElement(ImmutableStringReader reader, Identifier id) throws Exception { return (T)((ComponentPredicateParser.Context)this.context).forElementType(reader, id); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     public Stream<Identifier> possibleResources() { return ((ComponentPredicateParser.Context)this.context).listElementTypes(); }
/*     */   } public static interface Context<T, C, P> { T forElementType(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws Exception; Stream<Identifier> listElementTypes(); T forTagType(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws Exception; Stream<Identifier> listTagTypes(); C lookupComponentType(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws CommandSyntaxException; Stream<Identifier> listComponentTypes(); T createComponentTest(ImmutableStringReader param1ImmutableStringReader, C param1C, Dynamic<?> param1Dynamic) throws CommandSyntaxException; T createComponentTest(ImmutableStringReader param1ImmutableStringReader, C param1C); P lookupPredicateType(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws CommandSyntaxException;
/*     */     Stream<Identifier> listPredicateTypes();
/*     */     T createPredicateTest(ImmutableStringReader param1ImmutableStringReader, P param1P, Dynamic<?> param1Dynamic) throws CommandSyntaxException;
/*     */     T negate(T param1T);
/*     */     T anyOf(List<T> param1List); }
/* 168 */   private static class TagLookupRule<T, C, P> extends ResourceLookupRule<Context<T, C, P>, T> { private TagLookupRule(NamedRule<StringReader, Identifier> idParser, ComponentPredicateParser.Context<T, C, P> context) { super(idParser, context); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     protected T validateElement(ImmutableStringReader reader, Identifier id) throws Exception { return (T)((ComponentPredicateParser.Context)this.context).forTagType(reader, id); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     public Stream<Identifier> possibleResources() { return ((ComponentPredicateParser.Context)this.context).listTagTypes(); } }
/*     */ 
/*     */   
/*     */   private static class ComponentLookupRule<T, C, P>
/*     */     extends ResourceLookupRule<Context<T, C, P>, C>
/*     */   {
/* 184 */     private ComponentLookupRule(NamedRule<StringReader, Identifier> idParser, ComponentPredicateParser.Context<T, C, P> context) { super(idParser, context); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 189 */     protected C validateElement(ImmutableStringReader reader, Identifier id) throws CommandSyntaxException { return (C)((ComponentPredicateParser.Context)this.context).lookupComponentType(reader, id); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     public Stream<Identifier> possibleResources() { return ((ComponentPredicateParser.Context)this.context).listComponentTypes(); }
/*     */   }
/*     */   
/*     */   private static class PredicateLookupRule<T, C, P>
/*     */     extends ResourceLookupRule<Context<T, C, P>, P>
/*     */   {
/* 200 */     private PredicateLookupRule(NamedRule<StringReader, Identifier> idParser, ComponentPredicateParser.Context<T, C, P> context) { super(idParser, context); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     protected P validateElement(ImmutableStringReader reader, Identifier id) throws CommandSyntaxException { return (P)((ComponentPredicateParser.Context)this.context).lookupPredicateType(reader, id); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     public Stream<Identifier> possibleResources() { return ((ComponentPredicateParser.Context)this.context).listPredicateTypes(); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\item\ComponentPredicateParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.commands.arguments.item;
/*     */ 
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.parsing.packrat.NamedRule;
/*     */ import net.minecraft.util.parsing.packrat.commands.ResourceLookupRule;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ComponentLookupRule<T, C, P>
/*     */   extends ResourceLookupRule<ComponentPredicateParser.Context<T, C, P>, C>
/*     */ {
/* 184 */   private ComponentLookupRule(NamedRule<StringReader, Identifier> idParser, ComponentPredicateParser.Context<T, C, P> context) { super(idParser, context); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 189 */   protected C validateElement(ImmutableStringReader reader, Identifier id) throws Exception { return (C)((ComponentPredicateParser.Context)this.context).lookupComponentType(reader, id); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   public Stream<Identifier> possibleResources() { return ((ComponentPredicateParser.Context)this.context).listComponentTypes(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\item\ComponentPredicateParser$ComponentLookupRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
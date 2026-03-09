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
/*     */ class TagLookupRule<T, C, P>
/*     */   extends ResourceLookupRule<ComponentPredicateParser.Context<T, C, P>, T>
/*     */ {
/* 168 */   private TagLookupRule(NamedRule<StringReader, Identifier> idParser, ComponentPredicateParser.Context<T, C, P> context) { super(idParser, context); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 173 */   protected T validateElement(ImmutableStringReader reader, Identifier id) throws Exception { return (T)((ComponentPredicateParser.Context)this.context).forTagType(reader, id); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   public Stream<Identifier> possibleResources() { return ((ComponentPredicateParser.Context)this.context).listTagTypes(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\item\ComponentPredicateParser$TagLookupRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
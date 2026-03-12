/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public abstract class CompositeLootItemCondition
/*    */   implements LootItemCondition {
/*    */   protected final List<LootItemCondition> terms;
/*    */   private final Predicate<LootContext> composedPredicate;
/*    */   
/*    */   protected CompositeLootItemCondition(List<LootItemCondition> terms, Predicate<LootContext> composedPredicate) {
/* 21 */     this.terms = terms;
/* 22 */     this.composedPredicate = composedPredicate;
/*    */   }
/*    */ 
/*    */   
/* 26 */   protected static <T extends CompositeLootItemCondition> MapCodec<T> createCodec(Function<List<LootItemCondition>, T> factory) { return RecordCodecBuilder.mapCodec(i -> i.group(LootItemCondition.DIRECT_CODEC
/* 27 */           .listOf().fieldOf("terms").forGetter(()))
/* 28 */         .apply(i, factory)); }
/*    */ 
/*    */ 
/*    */   
/* 32 */   protected static <T extends CompositeLootItemCondition> Codec<T> createInlineCodec(Function<List<LootItemCondition>, T> factory) { return LootItemCondition.DIRECT_CODEC.listOf().xmap(factory, condition -> condition.terms); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public final boolean test(LootContext context) { return this.composedPredicate.test(context); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext output) {
/* 42 */     super.validate(output);
/*    */     
/* 44 */     for (int i = 0; i < this.terms.size(); i++)
/* 45 */       ((LootItemCondition)this.terms.get(i)).validate(output.forChild(new ProblemReporter.IndexedFieldPathElement("terms", i))); 
/*    */   }
/*    */   
/*    */   public static abstract class Builder
/*    */     implements LootItemCondition.Builder {
/* 50 */     private final ImmutableList.Builder<LootItemCondition> terms = ImmutableList.builder();
/*    */     
/*    */     protected Builder(Builder... terms) {
/* 53 */       for (LootItemCondition.Builder term : terms) {
/* 54 */         this.terms.add(term.build());
/*    */       }
/*    */     }
/*    */ 
/*    */     
/* 59 */     public void addTerm(LootItemCondition.Builder term) { this.terms.add(term.build()); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 64 */     public LootItemCondition build() { return create(this.terms.build()); }
/*    */     
/*    */     protected abstract LootItemCondition create(List<LootItemCondition> param1List);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\CompositeLootItemCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public abstract class LootItemConditionalFunction
/*    */   implements LootItemFunction {
/*    */   protected final List<LootItemCondition> predicates;
/*    */   private final Predicate<LootContext> compositePredicates;
/*    */   
/*    */   protected LootItemConditionalFunction(List<LootItemCondition> predicates) {
/* 23 */     this.predicates = predicates;
/* 24 */     this.compositePredicates = Util.allOf(predicates);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected static <T extends LootItemConditionalFunction> Products.P1<RecordCodecBuilder.Mu<T>, List<LootItemCondition>> commonFields(RecordCodecBuilder.Instance<T> i) {
/* 31 */     return i.group(LootItemCondition.DIRECT_CODEC
/* 32 */         .listOf().optionalFieldOf("conditions", List.of()).forGetter(f -> f.predicates));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public final ItemStack apply(ItemStack itemStack, LootContext context) { return this.compositePredicates.test(context) ? run(itemStack, context) : itemStack; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 45 */     super.validate(context);
/*    */     
/* 47 */     for (int i = 0; i < this.predicates.size(); i++)
/* 48 */       ((LootItemCondition)this.predicates.get(i)).validate(context.forChild(new ProblemReporter.IndexedFieldPathElement("conditions", i))); 
/*    */   }
/*    */   
/*    */   public static abstract class Builder<T extends Builder<T>>
/*    */     extends Object implements LootItemFunction.Builder, ConditionUserBuilder<T> {
/* 53 */     private final ImmutableList.Builder<LootItemCondition> conditions = ImmutableList.builder();
/*    */ 
/*    */     
/*    */     public T when(LootItemCondition.Builder condition) {
/* 57 */       this.conditions.add(condition.build());
/* 58 */       return (T)getThis();
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 63 */     public final T unwrap() { return (T)getThis(); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 69 */     protected List<LootItemCondition> getConditions() { return this.conditions.build(); }
/*    */     
/*    */     protected abstract T getThis();
/*    */   }
/*    */   
/*    */   private static final class DummyBuilder extends Builder<DummyBuilder> {
/*    */     private final Function<List<LootItemCondition>, LootItemFunction> constructor;
/*    */     
/* 77 */     public DummyBuilder(Function<List<LootItemCondition>, LootItemFunction> constructor) { this.constructor = constructor; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 82 */     protected DummyBuilder getThis() { return this; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 87 */     public LootItemFunction build() { return (LootItemFunction)this.constructor.apply(getConditions()); }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 92 */   protected static Builder<?> simpleBuilder(Function<List<LootItemCondition>, LootItemFunction> constructor) { return new DummyBuilder(constructor); }
/*    */   
/*    */   public abstract LootItemFunctionType<? extends LootItemConditionalFunction> getType();
/*    */   
/*    */   protected abstract ItemStack run(ItemStack paramItemStack, LootContext paramLootContext);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\LootItemConditionalFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
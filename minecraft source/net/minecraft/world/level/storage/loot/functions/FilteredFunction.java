/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.criterion.ItemPredicate;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class FilteredFunction extends LootItemConditionalFunction {
/* 16 */   public static final MapCodec<FilteredFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(ItemPredicate.CODEC
/* 17 */           .fieldOf("item_filter").forGetter(()), LootItemFunctions.ROOT_CODEC
/* 18 */           .optionalFieldOf("on_pass").forGetter(()), LootItemFunctions.ROOT_CODEC
/* 19 */           .optionalFieldOf("on_fail").forGetter(())))
/* 20 */       .apply(i, FilteredFunction::new));
/*    */   
/*    */   private final ItemPredicate filter;
/*    */   private final Optional<LootItemFunction> onPass;
/*    */   private final Optional<LootItemFunction> onFail;
/*    */   
/*    */   private FilteredFunction(List<LootItemCondition> predicates, ItemPredicate filter, Optional<LootItemFunction> onPass, Optional<LootItemFunction> onFail) {
/* 27 */     super(predicates);
/* 28 */     this.filter = filter;
/* 29 */     this.onPass = onPass;
/* 30 */     this.onFail = onFail;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public LootItemFunctionType<FilteredFunction> getType() { return LootItemFunctions.FILTERED; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 40 */     Optional<LootItemFunction> function = this.filter.test(itemStack) ? this.onPass : this.onFail;
/* 41 */     if (function.isPresent()) {
/* 42 */       return (ItemStack)((LootItemFunction)function.get()).apply(itemStack, context);
/*    */     }
/* 44 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 49 */     super.validate(context);
/* 50 */     this.onPass.ifPresent(f -> f.validate(context.forChild(new ProblemReporter.FieldPathElement("on_pass"))));
/* 51 */     this.onFail.ifPresent(f -> f.validate(context.forChild(new ProblemReporter.FieldPathElement("on_fail"))));
/*    */   }
/*    */ 
/*    */   
/* 55 */   public static Builder filtered(ItemPredicate predicate) { return new Builder(predicate); }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> { private final ItemPredicate itemPredicate;
/*    */     
/*    */     private Builder(ItemPredicate itemPredicate) {
/* 60 */       this.onPass = Optional.empty();
/* 61 */       this.onFail = Optional.empty();
/*    */ 
/*    */       
/* 64 */       this.itemPredicate = itemPredicate;
/*    */     }
/*    */     private Optional<LootItemFunction> onPass;
/*    */     private Optional<LootItemFunction> onFail;
/*    */     
/* 69 */     protected Builder getThis() { return this; }
/*    */ 
/*    */     
/*    */     public Builder onPass(Optional<LootItemFunction> onPass) {
/* 73 */       this.onPass = onPass;
/* 74 */       return this;
/*    */     }
/*    */     
/*    */     public Builder onFail(Optional<LootItemFunction> onFail) {
/* 78 */       this.onFail = onFail;
/* 79 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 84 */     public LootItemFunction build() { return new FilteredFunction(getConditions(), this.itemPredicate, this.onPass, this.onFail); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\FilteredFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
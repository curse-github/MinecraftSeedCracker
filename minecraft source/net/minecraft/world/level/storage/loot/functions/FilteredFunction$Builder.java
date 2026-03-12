/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.criterion.ItemPredicate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */   extends LootItemConditionalFunction.Builder<FilteredFunction.Builder>
/*    */ {
/*    */   private final ItemPredicate itemPredicate;
/*    */   private Optional<LootItemFunction> onPass;
/*    */   private Optional<LootItemFunction> onFail;
/*    */   
/*    */   private Builder(ItemPredicate itemPredicate) {
/* 60 */     this.onPass = Optional.empty();
/* 61 */     this.onFail = Optional.empty();
/*    */ 
/*    */     
/* 64 */     this.itemPredicate = itemPredicate;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 69 */   protected Builder getThis() { return this; }
/*    */ 
/*    */   
/*    */   public Builder onPass(Optional<LootItemFunction> onPass) {
/* 73 */     this.onPass = onPass;
/* 74 */     return this;
/*    */   }
/*    */   
/*    */   public Builder onFail(Optional<LootItemFunction> onFail) {
/* 78 */     this.onFail = onFail;
/* 79 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 84 */   public LootItemFunction build() { return new FilteredFunction(getConditions(), this.itemPredicate, this.onPass, this.onFail); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\FilteredFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
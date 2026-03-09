/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
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
/*    */   extends LootItemConditionalFunction.Builder<EnchantWithLevelsFunction.Builder>
/*    */ {
/*    */   private final NumberProvider levels;
/*    */   private Optional<HolderSet<Enchantment>> options;
/*    */   
/*    */   public Builder(NumberProvider levels) {
/* 59 */     this.options = Optional.empty();
/*    */ 
/*    */     
/* 62 */     this.levels = levels;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 67 */   protected Builder getThis() { return this; }
/*    */ 
/*    */   
/*    */   public Builder fromOptions(HolderSet<Enchantment> tag) {
/* 71 */     this.options = Optional.of(tag);
/* 72 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public LootItemFunction build() { return new EnchantWithLevelsFunction(getConditions(), this.levels, this.options); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\EnchantWithLevelsFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.core.Holder;
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
/*    */   extends LootItemConditionalFunction.Builder<SetEnchantmentsFunction.Builder>
/*    */ {
/*    */   private final ImmutableMap.Builder<Holder<Enchantment>, NumberProvider> enchantments;
/*    */   private final boolean add;
/*    */   
/* 70 */   public Builder() { this(false); }
/*    */   
/*    */   public Builder(boolean add) {
/*    */     this.enchantments = ImmutableMap.builder();
/* 74 */     this.add = add;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 79 */   protected Builder getThis() { return this; }
/*    */ 
/*    */   
/*    */   public Builder withEnchantment(Holder<Enchantment> enchantment, NumberProvider levelProvider) {
/* 83 */     this.enchantments.put(enchantment, levelProvider);
/* 84 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 89 */   public LootItemFunction build() { return new SetEnchantmentsFunction(getConditions(), this.enchantments.build(), this.add); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetEnchantmentsFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
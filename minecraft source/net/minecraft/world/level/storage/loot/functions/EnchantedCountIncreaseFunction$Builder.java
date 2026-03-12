/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */   extends LootItemConditionalFunction.Builder<EnchantedCountIncreaseFunction.Builder>
/*     */ {
/*     */   private final Holder<Enchantment> enchantment;
/*     */   private final NumberProvider count;
/*     */   private int limit;
/*     */   
/*     */   public Builder(Holder<Enchantment> enchantment, NumberProvider count) {
/*  84 */     this.limit = 0;
/*     */ 
/*     */     
/*  87 */     this.enchantment = enchantment;
/*  88 */     this.count = count;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  93 */   protected Builder getThis() { return this; }
/*     */ 
/*     */   
/*     */   public Builder setLimit(int limit) {
/*  97 */     this.limit = limit;
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public LootItemFunction build() { return new EnchantedCountIncreaseFunction(getConditions(), this.enchantment, this.count, this.limit); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\EnchantedCountIncreaseFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
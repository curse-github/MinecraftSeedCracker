/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   extends LootItemConditionalFunction.Builder<EnchantRandomlyFunction.Builder>
/*     */ {
/*  82 */   private Optional<HolderSet<Enchantment>> options = Optional.empty();
/*     */   
/*     */   private boolean onlyCompatible = true;
/*     */ 
/*     */   
/*  87 */   protected Builder getThis() { return this; }
/*     */ 
/*     */   
/*     */   public Builder withEnchantment(Holder<Enchantment> enchantment) {
/*  91 */     this.options = Optional.of(HolderSet.direct(new Holder[] { enchantment }));
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withOneOf(HolderSet<Enchantment> enchantments) {
/*  96 */     this.options = Optional.of(enchantments);
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public Builder allowingIncompatibleEnchantments() {
/* 101 */     this.onlyCompatible = false;
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public LootItemFunction build() { return new EnchantRandomlyFunction(getConditions(), this.options, this.onlyCompatible); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\EnchantRandomlyFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
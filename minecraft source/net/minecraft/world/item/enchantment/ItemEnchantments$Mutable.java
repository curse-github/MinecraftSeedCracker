/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Holder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Mutable
/*     */ {
/*     */   private final Object2IntOpenHashMap<Holder<Enchantment>> enchantments;
/*     */   
/*     */   public Mutable(ItemEnchantments enchantments) {
/* 128 */     this.enchantments = new Object2IntOpenHashMap();
/*     */ 
/*     */     
/* 131 */     this.enchantments.putAll(enchantments.enchantments);
/*     */   }
/*     */   
/*     */   public void set(Holder<Enchantment> enchantment, int level) {
/* 135 */     if (level <= 0) {
/* 136 */       this.enchantments.removeInt(enchantment);
/*     */     } else {
/* 138 */       this.enchantments.put(enchantment, Math.min(level, 255));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void upgrade(Holder<Enchantment> enchantment, int level) {
/* 143 */     if (level > 0) {
/* 144 */       this.enchantments.merge(enchantment, Math.min(level, 255), Integer::max);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 149 */   public void removeIf(Predicate<Holder<Enchantment>> predicate) { this.enchantments.keySet().removeIf(predicate); }
/*     */ 
/*     */ 
/*     */   
/* 153 */   public int getLevel(Holder<Enchantment> enchantment) { return this.enchantments.getOrDefault(enchantment, 0); }
/*     */ 
/*     */ 
/*     */   
/* 157 */   public Set<Holder<Enchantment>> keySet() { return this.enchantments.keySet(); }
/*     */ 
/*     */ 
/*     */   
/* 161 */   public ItemEnchantments toImmutable() { return new ItemEnchantments(this.enchantments); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\ItemEnchantments$Mutable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
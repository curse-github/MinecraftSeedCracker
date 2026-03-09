/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.criterion.EnchantmentPredicate;
/*    */ import net.minecraft.advancements.criterion.SingleComponentItemPredicate;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ 
/*    */ public abstract class EnchantmentsPredicate
/*    */   extends Object
/*    */   implements SingleComponentItemPredicate<ItemEnchantments> {
/*    */   private final List<EnchantmentPredicate> enchantments;
/*    */   
/* 17 */   protected EnchantmentsPredicate(List<EnchantmentPredicate> enchantments) { this.enchantments = enchantments; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static <T extends EnchantmentsPredicate> Codec<T> codec(Function<List<EnchantmentPredicate>, T> constructor) { return EnchantmentPredicate.CODEC.listOf().xmap(constructor, EnchantmentsPredicate::enchantments); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected List<EnchantmentPredicate> enchantments() { return this.enchantments; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(ItemEnchantments appliedEnchantments) {
/* 30 */     for (EnchantmentPredicate enchantment : this.enchantments) {
/* 31 */       if (!enchantment.containedIn(appliedEnchantments)) {
/* 32 */         return false;
/*    */       }
/*    */     } 
/* 35 */     return true;
/*    */   }
/*    */ 
/*    */   
/* 39 */   public static Enchantments enchantments(List<EnchantmentPredicate> predicates) { return new Enchantments(predicates); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public static StoredEnchantments storedEnchantments(List<EnchantmentPredicate> predicates) { return new StoredEnchantments(predicates); }
/*    */   
/*    */   public static class Enchantments
/*    */     extends EnchantmentsPredicate {
/* 47 */     public static final Codec<Enchantments> CODEC = codec(Enchantments::new);
/*    */ 
/*    */     
/* 50 */     protected Enchantments(List<EnchantmentPredicate> enchantments) { super(enchantments); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 55 */     public DataComponentType<ItemEnchantments> componentType() { return DataComponents.ENCHANTMENTS; }
/*    */   }
/*    */   
/*    */   public static class StoredEnchantments
/*    */     extends EnchantmentsPredicate {
/* 60 */     public static final Codec<StoredEnchantments> CODEC = codec(StoredEnchantments::new);
/*    */ 
/*    */     
/* 63 */     protected StoredEnchantments(List<EnchantmentPredicate> enchantments) { super(enchantments); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 68 */     public DataComponentType<ItemEnchantments> componentType() { return DataComponents.STORED_ENCHANTMENTS; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\predicates\EnchantmentsPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
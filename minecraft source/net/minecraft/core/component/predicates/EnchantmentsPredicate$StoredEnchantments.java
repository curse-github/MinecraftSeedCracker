/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.criterion.EnchantmentPredicate;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
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
/*    */ public class StoredEnchantments
/*    */   extends EnchantmentsPredicate
/*    */ {
/* 60 */   public static final Codec<StoredEnchantments> CODEC = codec(StoredEnchantments::new);
/*    */ 
/*    */   
/* 63 */   protected StoredEnchantments(List<EnchantmentPredicate> enchantments) { super(enchantments); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 68 */   public DataComponentType<ItemEnchantments> componentType() { return DataComponents.STORED_ENCHANTMENTS; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\predicates\EnchantmentsPredicate$StoredEnchantments.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
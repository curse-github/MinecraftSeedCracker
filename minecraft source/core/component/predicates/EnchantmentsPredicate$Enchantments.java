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
/*    */ public class Enchantments
/*    */   extends EnchantmentsPredicate
/*    */ {
/* 47 */   public static final Codec<Enchantments> CODEC = codec(Enchantments::new);
/*    */ 
/*    */   
/* 50 */   protected Enchantments(List<EnchantmentPredicate> enchantments) { super(enchantments); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public DataComponentType<ItemEnchantments> componentType() { return DataComponents.ENCHANTMENTS; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\predicates\EnchantmentsPredicate$Enchantments.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
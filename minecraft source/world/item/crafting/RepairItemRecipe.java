/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.EnchantmentTags;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class RepairItemRecipe
/*    */   extends CustomRecipe {
/* 16 */   public RepairItemRecipe(CraftingBookCategory category) { super(category); }
/*    */ 
/*    */   
/*    */   private static Pair<ItemStack, ItemStack> getItemsToCombine(CraftingInput input) {
/* 20 */     if (input.ingredientCount() != 2) {
/* 21 */       return null;
/*    */     }
/* 23 */     ItemStack first = null;
/* 24 */     for (int i = 0; i < input.size(); i++) {
/* 25 */       ItemStack itemStack = input.getItem(i);
/* 26 */       if (!itemStack.isEmpty())
/*    */       {
/*    */         
/* 29 */         if (first == null) {
/* 30 */           first = itemStack;
/*    */         } else {
/* 32 */           return canCombine(first, itemStack) ? Pair.of(first, itemStack) : null;
/*    */         }  } 
/*    */     } 
/* 35 */     return null;
/*    */   }
/*    */   
/*    */   private static boolean canCombine(ItemStack first, ItemStack second) {
/* 39 */     return (second.is(first.getItem()) && first
/* 40 */       .getCount() == 1 && second.getCount() == 1 && first
/* 41 */       .has(DataComponents.MAX_DAMAGE) && second.has(DataComponents.MAX_DAMAGE) && first
/* 42 */       .has(DataComponents.DAMAGE) && second.has(DataComponents.DAMAGE));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public boolean matches(CraftingInput input, Level level) { return (getItemsToCombine(input) != null); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/* 52 */     Pair<ItemStack, ItemStack> itemsToCombine = getItemsToCombine(input);
/* 53 */     if (itemsToCombine == null) {
/* 54 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 57 */     ItemStack first = (ItemStack)itemsToCombine.getFirst();
/* 58 */     ItemStack second = (ItemStack)itemsToCombine.getSecond();
/*    */ 
/*    */     
/* 61 */     int durability = Math.max(first.getMaxDamage(), second.getMaxDamage());
/*    */     
/* 63 */     int remaining1 = first.getMaxDamage() - first.getDamageValue();
/* 64 */     int remaining2 = second.getMaxDamage() - second.getDamageValue();
/* 65 */     int remaining = remaining1 + remaining2 + durability * 5 / 100;
/*    */     
/* 67 */     ItemStack itemStack = new ItemStack(first.getItem());
/* 68 */     itemStack.set(DataComponents.MAX_DAMAGE, Integer.valueOf(durability));
/* 69 */     itemStack.setDamageValue(Math.max(durability - remaining, 0));
/*    */     
/* 71 */     ItemEnchantments firstEnchants = EnchantmentHelper.getEnchantmentsForCrafting(first);
/* 72 */     ItemEnchantments secondEnchants = EnchantmentHelper.getEnchantmentsForCrafting(second);
/* 73 */     EnchantmentHelper.updateEnchantments(itemStack, enchantments -> 
/* 74 */         registries.lookupOrThrow(Registries.ENCHANTMENT).listElements().filter(()).forEach(()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 82 */     return itemStack;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 87 */   public RecipeSerializer<RepairItemRecipe> getSerializer() { return RecipeSerializer.REPAIR_ITEM; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\RepairItemRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
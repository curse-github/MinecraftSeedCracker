/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.world.item.DyeItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.DyedItemColor;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ArmorDyeRecipe
/*    */   extends CustomRecipe {
/* 15 */   public ArmorDyeRecipe(CraftingBookCategory category) { super(category); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingInput input, Level level) {
/* 21 */     if (input.ingredientCount() < 2) {
/* 22 */       return false;
/*    */     }
/*    */     
/* 25 */     boolean hasArmor = false;
/* 26 */     boolean hasDyes = false;
/*    */     
/* 28 */     for (int slot = 0; slot < input.size(); slot++) {
/* 29 */       ItemStack itemStack = input.getItem(slot);
/* 30 */       if (!itemStack.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 34 */         if (itemStack.is(ItemTags.DYEABLE)) {
/* 35 */           if (hasArmor) {
/* 36 */             return false;
/*    */           }
/* 38 */           hasArmor = true;
/* 39 */         } else if (itemStack.getItem() instanceof DyeItem) {
/* 40 */           hasDyes = true;
/*    */         } else {
/* 42 */           return false;
/*    */         } 
/*    */       }
/*    */     } 
/* 46 */     return (hasDyes && hasArmor);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/* 51 */     List<DyeItem> dyes = new ArrayList<DyeItem>();
/* 52 */     ItemStack armorItemStack = ItemStack.EMPTY;
/*    */     
/* 54 */     for (int slot = 0; slot < input.size(); slot++) {
/* 55 */       ItemStack itemStack = input.getItem(slot);
/* 56 */       if (!itemStack.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 60 */         if (itemStack.is(ItemTags.DYEABLE))
/* 61 */         { if (!armorItemStack.isEmpty()) {
/* 62 */             return ItemStack.EMPTY;
/*    */           }
/*    */           
/* 65 */           armorItemStack = itemStack.copy(); }
/* 66 */         else { Item item = itemStack.getItem(); if (item instanceof DyeItem) { DyeItem dye = (DyeItem)item;
/* 67 */             dyes.add(dye); }
/*    */           else
/* 69 */           { return ItemStack.EMPTY; }
/*    */            }
/*    */          } 
/*    */     } 
/* 73 */     if (armorItemStack.isEmpty() || dyes.isEmpty()) {
/* 74 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 77 */     return DyedItemColor.applyDyes(armorItemStack, dyes);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 82 */   public RecipeSerializer<ArmorDyeRecipe> getSerializer() { return RecipeSerializer.ARMOR_DYE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\ArmorDyeRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class TippedArrowRecipe extends CustomRecipe {
/* 11 */   public TippedArrowRecipe(CraftingBookCategory category) { super(category); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingInput input, Level level) {
/* 17 */     if (input.width() != 3 || input.height() != 3 || input.ingredientCount() != 9) {
/* 18 */       return false;
/*    */     }
/*    */     
/* 21 */     for (int y = 0; y < input.height(); y++) {
/* 22 */       for (int x = 0; x < input.width(); x++) {
/* 23 */         ItemStack ingredient = input.getItem(x, y);
/*    */         
/* 25 */         if (ingredient.isEmpty()) {
/* 26 */           return false;
/*    */         }
/*    */         
/* 29 */         if (x == 1 && y == 1) {
/* 30 */           if (!ingredient.is(Items.LINGERING_POTION)) {
/* 31 */             return false;
/*    */           }
/* 33 */         } else if (!ingredient.is(Items.ARROW)) {
/* 34 */           return false;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 39 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/* 44 */     ItemStack potion = input.getItem(1, 1);
/* 45 */     if (!potion.is(Items.LINGERING_POTION)) {
/* 46 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 49 */     ItemStack result = new ItemStack(Items.TIPPED_ARROW, 8);
/* 50 */     result.set(DataComponents.POTION_CONTENTS, (PotionContents)potion.get(DataComponents.POTION_CONTENTS));
/*    */     
/* 52 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public RecipeSerializer<TippedArrowRecipe> getSerializer() { return RecipeSerializer.TIPPED_ARROW; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\TippedArrowRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
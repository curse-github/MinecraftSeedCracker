/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.PotDecorations;
/*    */ 
/*    */ public class DecoratedPotRecipe
/*    */   extends CustomRecipe {
/* 12 */   public DecoratedPotRecipe(CraftingBookCategory category) { super(category); }
/*    */ 
/*    */ 
/*    */   
/* 16 */   private static ItemStack back(CraftingInput input) { return input.getItem(1, 0); }
/*    */ 
/*    */ 
/*    */   
/* 20 */   private static ItemStack left(CraftingInput input) { return input.getItem(0, 1); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   private static ItemStack right(CraftingInput input) { return input.getItem(2, 1); }
/*    */ 
/*    */ 
/*    */   
/* 28 */   private static ItemStack front(CraftingInput input) { return input.getItem(1, 2); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingInput input, Level level) {
/* 34 */     if (input.width() != 3 || input.height() != 3 || input.ingredientCount() != 4) {
/* 35 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 39 */     return (back(input).is(ItemTags.DECORATED_POT_INGREDIENTS) && 
/* 40 */       left(input).is(ItemTags.DECORATED_POT_INGREDIENTS) && 
/* 41 */       right(input).is(ItemTags.DECORATED_POT_INGREDIENTS) && 
/* 42 */       front(input).is(ItemTags.DECORATED_POT_INGREDIENTS));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/* 51 */     PotDecorations decorations = new PotDecorations(back(input).getItem(), left(input).getItem(), right(input).getItem(), front(input).getItem());
/*    */     
/* 53 */     return DecoratedPotBlockEntity.createDecoratedPotItem(decorations);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public RecipeSerializer<DecoratedPotRecipe> getSerializer() { return RecipeSerializer.DECORATED_POT_RECIPE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\DecoratedPotRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
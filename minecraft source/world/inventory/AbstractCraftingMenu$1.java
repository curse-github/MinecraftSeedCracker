/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.recipebook.ServerPlaceRecipe;
/*    */ import net.minecraft.world.entity.player.StackedItemContents;
/*    */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
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
/*    */ class null
/*    */   extends Object
/*    */   implements ServerPlaceRecipe.CraftingMenuAccess<CraftingRecipe>
/*    */ {
/* 50 */   public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) { AbstractCraftingMenu.this.fillCraftSlotsStackedContents(stackedContents); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void clearCraftingContent() {
/* 55 */     AbstractCraftingMenu.this.resultSlots.clearContent();
/* 56 */     AbstractCraftingMenu.this.craftSlots.clearContent();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public boolean recipeMatches(RecipeHolder<CraftingRecipe> recipe) { return ((CraftingRecipe)recipe.value()).matches(AbstractCraftingMenu.this.craftSlots.asCraftInput(), AbstractCraftingMenu.this.owner().level()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\AbstractCraftingMenu$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
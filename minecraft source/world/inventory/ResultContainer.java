/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.ContainerHelper;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ 
/*    */ public class ResultContainer
/*    */   implements Container, RecipeCraftingHolder {
/* 12 */   private final NonNullList<ItemStack> itemStacks = NonNullList.withSize(1, ItemStack.EMPTY);
/*    */   
/*    */   private RecipeHolder<?> recipeUsed;
/*    */ 
/*    */   
/* 17 */   public int getContainerSize() { return 1; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 22 */     for (ItemStack itemStack : this.itemStacks) {
/* 23 */       if (!itemStack.isEmpty()) {
/* 24 */         return false;
/*    */       }
/*    */     } 
/* 27 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public ItemStack getItem(int slot) { return (ItemStack)this.itemStacks.get(0); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public ItemStack removeItem(int slot, int count) { return ContainerHelper.takeItem(this.itemStacks, 0); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public ItemStack removeItemNoUpdate(int slot) { return ContainerHelper.takeItem(this.itemStacks, 0); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public void setItem(int slot, ItemStack itemStack) { this.itemStacks.set(0, itemStack); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setChanged() {}
/*    */ 
/*    */ 
/*    */   
/* 56 */   public boolean stillValid(Player player) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public void clearContent() { this.itemStacks.clear(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 66 */   public void setRecipeUsed(RecipeHolder<?> recipeUsed) { this.recipeUsed = recipeUsed; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public RecipeHolder<?> getRecipeUsed() { return this.recipeUsed; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\ResultContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
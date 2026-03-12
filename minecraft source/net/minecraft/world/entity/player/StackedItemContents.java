/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.PlacementInfo;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ 
/*    */ public class StackedItemContents
/*    */ {
/* 13 */   private final StackedContents<Holder<Item>> raw = new StackedContents();
/*    */   
/*    */   public void accountSimpleStack(ItemStack itemStack) {
/* 16 */     if (Inventory.isUsableForCrafting(itemStack)) {
/* 17 */       accountStack(itemStack);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 22 */   public void accountStack(ItemStack itemStack) { accountStack(itemStack, itemStack.getMaxStackSize()); }
/*    */ 
/*    */   
/*    */   public void accountStack(ItemStack itemStack, int maxCount) {
/* 26 */     if (!itemStack.isEmpty()) {
/* 27 */       int count = Math.min(maxCount, itemStack.getCount());
/* 28 */       this.raw.account(itemStack.getItemHolder(), count);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 33 */   public boolean canCraft(Recipe<?> recipe, StackedContents.Output<Holder<Item>> output) { return canCraft(recipe, 1, output); }
/*    */ 
/*    */   
/*    */   public boolean canCraft(Recipe<?> recipe, int amount, StackedContents.Output<Holder<Item>> output) {
/* 37 */     PlacementInfo placementInfo = recipe.placementInfo();
/* 38 */     if (placementInfo.isImpossibleToPlace()) {
/* 39 */       return false;
/*    */     }
/* 41 */     return canCraft(placementInfo.ingredients(), amount, output);
/*    */   }
/*    */ 
/*    */   
/* 45 */   public boolean canCraft(List<? extends StackedContents.IngredientInfo<Holder<Item>>> contents, StackedContents.Output<Holder<Item>> output) { return canCraft(contents, 1, output); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   private boolean canCraft(List<? extends StackedContents.IngredientInfo<Holder<Item>>> contents, int amount, StackedContents.Output<Holder<Item>> output) { return this.raw.tryPick(contents, amount, output); }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public int getBiggestCraftableStack(Recipe<?> recipe, StackedContents.Output<Holder<Item>> output) { return getBiggestCraftableStack(recipe, 2147483647, output); }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public int getBiggestCraftableStack(Recipe<?> recipe, int maxSize, StackedContents.Output<Holder<Item>> output) { return this.raw.tryPickAll(recipe.placementInfo().ingredients(), maxSize, output); }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public void clear() { this.raw.clear(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\player\StackedItemContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
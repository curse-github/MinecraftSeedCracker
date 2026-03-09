/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*    */ import net.minecraft.world.item.crafting.display.StonecutterRecipeDisplay;
/*    */ 
/*    */ public class StonecutterRecipe
/*    */   extends SingleItemRecipe
/*    */ {
/* 13 */   public StonecutterRecipe(String group, Ingredient ingredient, ItemStack result) { super(group, ingredient, result); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public RecipeType<StonecutterRecipe> getType() { return RecipeType.STONECUTTING; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public RecipeSerializer<StonecutterRecipe> getSerializer() { return RecipeSerializer.STONECUTTER; }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<RecipeDisplay> display() {
/* 28 */     return List.of(new StonecutterRecipeDisplay(
/* 29 */           input().display(), 
/* 30 */           resultDisplay(), new SlotDisplay.ItemSlotDisplay(Items.STONECUTTER)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public SlotDisplay resultDisplay() { return new SlotDisplay.ItemStackSlotDisplay(result()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.STONECUTTER; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\StonecutterRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
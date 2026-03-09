/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ 
/*    */ public interface RecipeCraftingHolder
/*    */ {
/*    */   void setRecipeUsed(RecipeHolder<?> paramRecipeHolder);
/*    */   
/*    */   RecipeHolder<?> getRecipeUsed();
/*    */   
/*    */   default void awardUsedRecipes(Player player, List<ItemStack> itemStacks) {
/* 19 */     RecipeHolder<?> recipeUsed = getRecipeUsed();
/* 20 */     if (recipeUsed != null) {
/* 21 */       player.triggerRecipeCrafted(recipeUsed, itemStacks);
/* 22 */       if (!recipeUsed.value().isSpecial()) {
/* 23 */         player.awardRecipes(Collections.singleton(recipeUsed));
/* 24 */         setRecipeUsed(null);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   default boolean setRecipeUsed(ServerPlayer player, RecipeHolder<?> recipe) {
/* 30 */     if (recipe.value().isSpecial() || !((Boolean)player.level().getGameRules().get(GameRules.LIMITED_CRAFTING)).booleanValue() || player.getRecipeBook().contains(recipe.id())) {
/* 31 */       setRecipeUsed(recipe);
/* 32 */       return true;
/*    */     } 
/*    */     
/* 35 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\RecipeCraftingHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.StackedItemContents;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ 
/*    */ public abstract class RecipeBookMenu
/*    */   extends AbstractContainerMenu {
/* 10 */   public RecipeBookMenu(MenuType<?> menuType, int containerId) { super(menuType, containerId); }
/*    */   
/*    */   public abstract PostPlaceAction handlePlacement(boolean paramBoolean1, boolean paramBoolean2, RecipeHolder<?> paramRecipeHolder, ServerLevel paramServerLevel, Inventory paramInventory);
/*    */   
/*    */   public abstract void fillCraftSlotsStackedContents(StackedItemContents paramStackedItemContents);
/*    */   
/*    */   public abstract RecipeBookType getRecipeBookType();
/*    */   
/*    */   public enum PostPlaceAction
/*    */   {
/* 20 */     NOTHING,
/* 21 */     PLACE_GHOST_RECIPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\RecipeBookMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
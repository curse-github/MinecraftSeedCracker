/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.recipebook.ServerPlaceRecipe;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.player.StackedItemContents;
/*    */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ 
/*    */ public abstract class AbstractCraftingMenu
/*    */   extends RecipeBookMenu
/*    */ {
/*    */   private final int width;
/*    */   private final int height;
/*    */   protected final CraftingContainer craftSlots;
/* 18 */   protected final ResultContainer resultSlots = new ResultContainer();
/*    */   
/*    */   public AbstractCraftingMenu(MenuType<?> menuType, int containerId, int width, int height) {
/* 21 */     super(menuType, containerId);
/* 22 */     this.width = width;
/* 23 */     this.height = height;
/*    */     
/* 25 */     this.craftSlots = new TransientCraftingContainer(this, width, height);
/*    */   }
/*    */ 
/*    */   
/* 29 */   protected Slot addResultSlot(Player player, int x, int y) { return addSlot(new ResultSlot(player, this.craftSlots, this.resultSlots, 0, x, y)); }
/*    */ 
/*    */   
/*    */   protected void addCraftingGridSlots(int left, int top) {
/* 33 */     for (int y = 0; y < this.width; y++) {
/* 34 */       for (int x = 0; x < this.height; x++) {
/* 35 */         addSlot(new Slot(this.craftSlots, x + y * this.width, left + x * 18, top + y * 18));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeBookMenu.PostPlaceAction handlePlacement(boolean useMaxItems, boolean allowDroppingItemsToClear, RecipeHolder<?> recipe, ServerLevel level, Inventory inventory) {
/* 42 */     typedRecipe = recipe;
/* 43 */     beginPlacingRecipe();
/*    */     try {
/* 45 */       List<Slot> inputSlots = getInputGridSlots();
/* 46 */       return ServerPlaceRecipe.placeRecipe(new ServerPlaceRecipe.CraftingMenuAccess<CraftingRecipe>()
/*    */           {
/*    */             public void fillCraftSlotsStackedContents(StackedItemContents stackedContents)
/*    */             {
/* 50 */               AbstractCraftingMenu.this.fillCraftSlotsStackedContents(stackedContents);
/*    */             }
/*    */ 
/*    */             
/*    */             public void clearCraftingContent() {
/* 55 */               AbstractCraftingMenu.this.resultSlots.clearContent();
/* 56 */               AbstractCraftingMenu.this.craftSlots.clearContent();
/*    */             }
/*    */ 
/*    */             
/*    */             public boolean recipeMatches(RecipeHolder<CraftingRecipe> recipe) {
/* 61 */               return ((CraftingRecipe)recipe.value()).matches(AbstractCraftingMenu.this.craftSlots.asCraftInput(), AbstractCraftingMenu.this.owner().level());
/*    */             }
/*    */           }this.width, this.height, inputSlots, inputSlots, inventory, typedRecipe, useMaxItems, allowDroppingItemsToClear);
/*    */ 
/*    */ 
/*    */     
/*    */     }
/*    */     finally {
/*    */ 
/*    */ 
/*    */       
/* 72 */       finishPlacingRecipe(level, typedRecipe);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void beginPlacingRecipe() {}
/*    */ 
/*    */   
/*    */   protected void finishPlacingRecipe(ServerLevel level, RecipeHolder<CraftingRecipe> recipe) {}
/*    */ 
/*    */   
/*    */   public abstract Slot getResultSlot();
/*    */   
/*    */   public abstract List<Slot> getInputGridSlots();
/*    */   
/* 87 */   public int getGridWidth() { return this.width; }
/*    */ 
/*    */ 
/*    */   
/* 91 */   public int getGridHeight() { return this.height; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract Player owner();
/*    */ 
/*    */   
/* 98 */   public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) { this.craftSlots.fillStackedContents(stackedContents); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\AbstractCraftingMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
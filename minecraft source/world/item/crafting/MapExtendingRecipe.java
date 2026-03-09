/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.MapItem;
/*    */ import net.minecraft.world.item.component.MapPostProcessing;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*    */ 
/*    */ public class MapExtendingRecipe
/*    */   extends ShapedRecipe {
/*    */   public MapExtendingRecipe(CraftingBookCategory category) {
/* 16 */     super("", category, 
/* 17 */         ShapedRecipePattern.of(
/* 18 */           Map.of(
/* 19 */             Character.valueOf('#'), Ingredient.of(Items.PAPER), 
/* 20 */             Character.valueOf('x'), Ingredient.of(Items.FILLED_MAP)), new String[] { "###", "#x#", "###" }), new ItemStack(Items.MAP));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingInput input, Level level) {
/* 32 */     if (!super.matches(input, level)) {
/* 33 */       return false;
/*    */     }
/* 35 */     ItemStack map = findFilledMap(input);
/*    */     
/* 37 */     if (map.isEmpty()) {
/* 38 */       return false;
/*    */     }
/* 40 */     MapItemSavedData data = MapItem.getSavedData(map, level);
/* 41 */     if (data == null) {
/* 42 */       return false;
/*    */     }
/*    */     
/* 45 */     if (data.isExplorationMap()) {
/* 46 */       return false;
/*    */     }
/*    */     
/* 49 */     return (data.scale < 4);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/* 54 */     ItemStack map = findFilledMap(input).copyWithCount(1);
/* 55 */     map.set(DataComponents.MAP_POST_PROCESSING, MapPostProcessing.SCALE);
/* 56 */     return map;
/*    */   }
/*    */   
/*    */   private static ItemStack findFilledMap(CraftingInput input) {
/* 60 */     for (int i = 0; i < input.size(); i++) {
/* 61 */       ItemStack itemStack = input.getItem(i);
/* 62 */       if (itemStack.has(DataComponents.MAP_ID)) {
/* 63 */         return itemStack;
/*    */       }
/*    */     } 
/* 66 */     return ItemStack.EMPTY;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public boolean isSpecial() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   public RecipeSerializer<MapExtendingRecipe> getSerializer() { return RecipeSerializer.MAP_EXTENDING; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\MapExtendingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
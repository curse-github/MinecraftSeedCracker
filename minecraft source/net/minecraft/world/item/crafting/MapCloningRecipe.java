/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class MapCloningRecipe
/*    */   extends CustomRecipe {
/* 11 */   public MapCloningRecipe(CraftingBookCategory category) { super(category); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingInput input, Level level) {
/* 17 */     if (input.ingredientCount() < 2) {
/* 18 */       return false;
/*    */     }
/*    */     
/* 21 */     boolean hasEmptyMaps = false;
/* 22 */     boolean hasSourceMap = false;
/*    */     
/* 24 */     for (int slot = 0; slot < input.size(); slot++) {
/* 25 */       ItemStack itemStack = input.getItem(slot);
/* 26 */       if (!itemStack.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 30 */         if (itemStack.has(DataComponents.MAP_ID)) {
/* 31 */           if (hasSourceMap) {
/* 32 */             return false;
/*    */           }
/* 34 */           hasSourceMap = true;
/* 35 */         } else if (itemStack.is(Items.MAP)) {
/* 36 */           hasEmptyMaps = true;
/*    */         } else {
/* 38 */           return false;
/*    */         } 
/*    */       }
/*    */     } 
/* 42 */     return (hasSourceMap && hasEmptyMaps);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/* 47 */     int count = 0;
/* 48 */     ItemStack source = ItemStack.EMPTY;
/*    */     
/* 50 */     for (int slot = 0; slot < input.size(); slot++) {
/* 51 */       ItemStack itemStack = input.getItem(slot);
/* 52 */       if (!itemStack.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 56 */         if (itemStack.has(DataComponents.MAP_ID)) {
/* 57 */           if (!source.isEmpty()) {
/* 58 */             return ItemStack.EMPTY;
/*    */           }
/* 60 */           source = itemStack;
/* 61 */         } else if (itemStack.is(Items.MAP)) {
/* 62 */           count++;
/*    */         } else {
/* 64 */           return ItemStack.EMPTY;
/*    */         } 
/*    */       }
/*    */     } 
/* 68 */     if (source.isEmpty() || count < 1) {
/* 69 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 72 */     return source.copyWithCount(count + 1);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public RecipeSerializer<MapCloningRecipe> getSerializer() { return RecipeSerializer.MAP_CLONING; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\MapCloningRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
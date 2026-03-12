/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.BannerItem;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*    */ 
/*    */ public class ShieldDecorationRecipe
/*    */   extends CustomRecipe {
/* 13 */   public ShieldDecorationRecipe(CraftingBookCategory category) { super(category); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingInput input, Level level) {
/* 19 */     if (input.ingredientCount() != 2) {
/* 20 */       return false;
/*    */     }
/*    */     
/* 23 */     boolean hasClearShield = false;
/* 24 */     boolean hasPatternBanner = false;
/*    */     
/* 26 */     for (int slot = 0; slot < input.size(); slot++) {
/* 27 */       ItemStack itemStack = input.getItem(slot);
/* 28 */       if (!itemStack.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 32 */         if (itemStack.getItem() instanceof BannerItem) {
/* 33 */           if (hasPatternBanner) {
/* 34 */             return false;
/*    */           }
/* 36 */           hasPatternBanner = true;
/* 37 */         } else if (itemStack.is(Items.SHIELD)) {
/* 38 */           if (hasClearShield) {
/* 39 */             return false;
/*    */           }
/* 41 */           BannerPatternLayers patterns = (BannerPatternLayers)itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
/* 42 */           if (!patterns.layers().isEmpty()) {
/* 43 */             return false;
/*    */           }
/* 45 */           hasClearShield = true;
/*    */         } else {
/* 47 */           return false;
/*    */         } 
/*    */       }
/*    */     } 
/* 51 */     return (hasClearShield && hasPatternBanner);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/* 56 */     ItemStack patternBanner = ItemStack.EMPTY;
/* 57 */     ItemStack shield = ItemStack.EMPTY;
/*    */     
/* 59 */     for (int slot = 0; slot < input.size(); slot++) {
/* 60 */       ItemStack itemStack = input.getItem(slot);
/* 61 */       if (!itemStack.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 65 */         if (itemStack.getItem() instanceof BannerItem) {
/* 66 */           patternBanner = itemStack;
/* 67 */         } else if (itemStack.is(Items.SHIELD)) {
/* 68 */           shield = itemStack.copy();
/*    */         } 
/*    */       }
/*    */     } 
/* 72 */     if (shield.isEmpty()) {
/* 73 */       return shield;
/*    */     }
/*    */     
/* 76 */     shield.set(DataComponents.BANNER_PATTERNS, (BannerPatternLayers)patternBanner.get(DataComponents.BANNER_PATTERNS));
/* 77 */     shield.set(DataComponents.BASE_COLOR, ((BannerItem)patternBanner.getItem()).getColor());
/*    */     
/* 79 */     return shield;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 84 */   public RecipeSerializer<ShieldDecorationRecipe> getSerializer() { return RecipeSerializer.SHIELD_DECORATION; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\ShieldDecorationRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
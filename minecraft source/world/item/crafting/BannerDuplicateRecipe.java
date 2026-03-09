/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.world.item.BannerItem;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ 
/*     */ public class BannerDuplicateRecipe
/*     */   extends CustomRecipe
/*     */ {
/*  16 */   public BannerDuplicateRecipe(CraftingBookCategory category) { super(category); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  22 */     if (input.ingredientCount() != 2) {
/*  23 */       return false;
/*     */     }
/*     */     
/*  26 */     DyeColor color = null;
/*  27 */     boolean hasTarget = false;
/*  28 */     boolean hasSource = false;
/*     */     
/*  30 */     for (int slot = 0; slot < input.size(); slot++) {
/*  31 */       ItemStack itemStack = input.getItem(slot);
/*  32 */       if (!itemStack.isEmpty()) {
/*     */ 
/*     */         
/*  35 */         Item item = itemStack.getItem();
/*  36 */         if (item instanceof BannerItem) { BannerItem banner = (BannerItem)item;
/*  37 */           if (color == null) {
/*  38 */             color = banner.getColor();
/*  39 */           } else if (color != banner.getColor()) {
/*  40 */             return false;
/*     */           }  }
/*     */         else
/*  43 */         { return false; }
/*     */ 
/*     */         
/*  46 */         int patternCount = ((BannerPatternLayers)itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)).layers().size();
/*  47 */         if (patternCount > 6) {
/*  48 */           return false;
/*     */         }
/*     */         
/*  51 */         if (patternCount > 0) {
/*  52 */           if (hasSource) {
/*  53 */             return false;
/*     */           }
/*  55 */           hasSource = true;
/*     */         } else {
/*     */           
/*  58 */           if (hasTarget) {
/*  59 */             return false;
/*     */           }
/*  61 */           hasTarget = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  66 */     return (hasSource && hasTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/*  72 */     for (int slot = 0; slot < input.size(); slot++) {
/*  73 */       ItemStack itemStack = input.getItem(slot);
/*  74 */       if (!itemStack.isEmpty()) {
/*     */ 
/*     */         
/*  77 */         int patternCount = ((BannerPatternLayers)itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)).layers().size();
/*  78 */         if (patternCount > 0 && patternCount <= 6) {
/*  79 */           return itemStack.copyWithCount(1);
/*     */         }
/*     */       } 
/*     */     } 
/*  83 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
/*  88 */     NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);
/*     */     
/*  90 */     for (int slot = 0; slot < result.size(); slot++) {
/*  91 */       ItemStack itemStack = input.getItem(slot);
/*  92 */       if (!itemStack.isEmpty()) {
/*  93 */         ItemStack remainder = itemStack.getItem().getCraftingRemainder();
/*  94 */         if (!remainder.isEmpty()) {
/*  95 */           result.set(slot, remainder);
/*  96 */         } else if (!((BannerPatternLayers)itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)).layers().isEmpty()) {
/*  97 */           result.set(slot, itemStack.copyWithCount(1));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 102 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public RecipeSerializer<BannerDuplicateRecipe> getSerializer() { return RecipeSerializer.BANNER_DUPLICATE; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\BannerDuplicateRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
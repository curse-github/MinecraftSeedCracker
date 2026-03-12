/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import net.minecraft.world.inventory.RecipeBookType;
/*    */ 
/*    */ public class RecipeBook {
/*  6 */   protected final RecipeBookSettings bookSettings = new RecipeBookSettings();
/*    */ 
/*    */   
/*  9 */   public boolean isOpen(RecipeBookType recipeBookType) { return this.bookSettings.isOpen(recipeBookType); }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public void setOpen(RecipeBookType recipeBookType, boolean open) { this.bookSettings.setOpen(recipeBookType, open); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public boolean isFiltering(RecipeBookType type) { return this.bookSettings.isFiltering(type); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public void setFiltering(RecipeBookType type, boolean filtering) { this.bookSettings.setFiltering(type, filtering); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public void setBookSettings(RecipeBookSettings settings) { this.bookSettings.replaceFrom(settings); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public RecipeBookSettings getBookSettings() { return this.bookSettings; }
/*    */ 
/*    */   
/*    */   public void setBookSetting(RecipeBookType bookType, boolean open, boolean filtering) {
/* 33 */     this.bookSettings.setOpen(bookType, open);
/* 34 */     this.bookSettings.setFiltering(bookType, filtering);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\stats\RecipeBook.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ public static enum RecipeCategory {
/*  4 */   BUILDING_BLOCKS("building_blocks"),
/*  5 */   DECORATIONS("decorations"),
/*  6 */   REDSTONE("redstone"),
/*  7 */   TRANSPORTATION("transportation"),
/*  8 */   TOOLS("tools"),
/*  9 */   COMBAT("combat"),
/* 10 */   FOOD("food"),
/* 11 */   BREWING("brewing"),
/* 12 */   MISC("misc");
/*    */   
/*    */   private final String recipeFolderName;
/*    */ 
/*    */   
/* 17 */   RecipeCategory(String recipeFolderName) { this.recipeFolderName = recipeFolderName; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public String getFolderName() { return this.recipeFolderName; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\RecipeCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
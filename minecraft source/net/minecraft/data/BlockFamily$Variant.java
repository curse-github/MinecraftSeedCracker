/*    */ package net.minecraft.data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public static enum Variant
/*    */ {
/* 21 */   BUTTON("button"),
/* 22 */   CHISELED("chiseled"),
/* 23 */   CRACKED("cracked"),
/* 24 */   CUT("cut"),
/* 25 */   DOOR("door"),
/* 26 */   CUSTOM_FENCE("fence"),
/* 27 */   FENCE("fence"),
/* 28 */   CUSTOM_FENCE_GATE("fence_gate"),
/* 29 */   FENCE_GATE("fence_gate"),
/* 30 */   MOSAIC("mosaic"),
/* 31 */   SIGN("sign"),
/* 32 */   SLAB("slab"),
/* 33 */   STAIRS("stairs"),
/* 34 */   PRESSURE_PLATE("pressure_plate"),
/* 35 */   POLISHED("polished"),
/* 36 */   TRAPDOOR("trapdoor"),
/* 37 */   WALL("wall"),
/* 38 */   WALL_SIGN("wall_sign");
/*    */   
/*    */   private final String recipeGroup;
/*    */ 
/*    */   
/* 43 */   Variant(String recipeGroup) { this.recipeGroup = recipeGroup; }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public String getRecipeGroup() { return this.recipeGroup; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\BlockFamily$Variant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
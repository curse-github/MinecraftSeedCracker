/*    */ package net.minecraft.world.level.block;
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
/*    */ public static enum Types
/*    */   implements SkullBlock.Type
/*    */ {
/* 39 */   SKELETON("skeleton"),
/* 40 */   WITHER_SKELETON("wither_skeleton"),
/* 41 */   PLAYER("player"),
/* 42 */   ZOMBIE("zombie"),
/* 43 */   CREEPER("creeper"),
/* 44 */   PIGLIN("piglin"),
/* 45 */   DRAGON("dragon");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   Types(String name) {
/* 51 */     this.name = name;
/* 52 */     TYPES.put(name, this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SkullBlock$Types.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
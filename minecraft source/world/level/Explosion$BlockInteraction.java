/*    */ package net.minecraft.world.level;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public static enum BlockInteraction
/*    */ {
/* 43 */   KEEP(false),
/* 44 */   DESTROY(true),
/* 45 */   DESTROY_WITH_DECAY(true),
/* 46 */   TRIGGER_BLOCK(false);
/*    */   
/*    */   private final boolean shouldAffectBlocklikeEntities;
/*    */ 
/*    */   
/* 51 */   BlockInteraction(boolean shouldAffectBlocklikeEntities) { this.shouldAffectBlocklikeEntities = shouldAffectBlocklikeEntities; }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public boolean shouldAffectBlocklikeEntities() { return this.shouldAffectBlocklikeEntities; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\Explosion$BlockInteraction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
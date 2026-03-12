/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ public static enum FullChunkStatus {
/*  4 */   INACCESSIBLE,
/*  5 */   FULL,
/*  6 */   BLOCK_TICKING,
/*  7 */   ENTITY_TICKING;
/*    */ 
/*    */ 
/*    */   
/* 11 */   public boolean isOrAfter(FullChunkStatus step) { return (ordinal() >= step.ordinal()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\FullChunkStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
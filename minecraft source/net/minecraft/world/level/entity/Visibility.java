/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import net.minecraft.server.level.FullChunkStatus;
/*    */ 
/*    */ public static enum Visibility {
/*  6 */   HIDDEN(false, false),
/*  7 */   TRACKED(true, false),
/*  8 */   TICKING(true, true);
/*    */   
/*    */   private final boolean accessible;
/*    */   private final boolean ticking;
/*    */   
/*    */   Visibility(boolean accessible, boolean ticking) {
/* 14 */     this.accessible = accessible;
/* 15 */     this.ticking = ticking;
/*    */   }
/*    */ 
/*    */   
/* 19 */   public boolean isTicking() { return this.ticking; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public boolean isAccessible() { return this.accessible; }
/*    */ 
/*    */   
/*    */   public static Visibility fromFullChunkStatus(FullChunkStatus status) {
/* 27 */     if (status.isOrAfter(FullChunkStatus.ENTITY_TICKING)) {
/* 28 */       return TICKING;
/*    */     }
/* 30 */     if (status.isOrAfter(FullChunkStatus.FULL)) {
/* 31 */       return TRACKED;
/*    */     }
/* 33 */     return HIDDEN;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\entity\Visibility.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
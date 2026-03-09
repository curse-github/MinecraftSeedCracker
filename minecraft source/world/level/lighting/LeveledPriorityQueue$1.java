/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends LongLinkedOpenHashSet
/*    */ {
/* 15 */   null(int expected, float f) { super(expected, f); }
/*    */   
/*    */   protected void rehash(int newN) {
/* 18 */     if (newN > minSize)
/* 19 */       super.rehash(newN); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\lighting\LeveledPriorityQueue$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
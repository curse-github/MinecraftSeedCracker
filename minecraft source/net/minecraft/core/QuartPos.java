/*    */ package net.minecraft.core;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class QuartPos
/*    */ {
/*    */   public static final int BITS = 2;
/*    */   public static final int SIZE = 4;
/*    */   public static final int MASK = 3;
/*    */   private static final int SECTION_TO_QUARTS_BITS = 2;
/*    */   
/* 14 */   public static int fromBlock(int blockCoord) { return blockCoord >> 2; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static int quartLocal(int blockCoord) { return blockCoord & 0x3; }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static int toBlock(int quart) { return quart << 2; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static int fromSection(int section) { return section << 2; }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static int toSection(int quart) { return quart >> 2; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\QuartPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
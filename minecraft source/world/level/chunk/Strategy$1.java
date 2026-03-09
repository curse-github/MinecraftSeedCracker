/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import net.minecraft.core.IdMap;
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
/*    */ class null
/*    */   extends Strategy<T>
/*    */ {
/* 39 */   null(IdMap<T> globalMap, int bitsPerAxis) { super(globalMap, bitsPerAxis); }
/*    */   
/*    */   public Configuration getConfigurationForBitCount(int entryBits) {
/* 42 */     switch (entryBits) { case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8:  }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 50 */       new Configuration.Global(this.globalPaletteBitsInMemory, entryBits);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\Strategy$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
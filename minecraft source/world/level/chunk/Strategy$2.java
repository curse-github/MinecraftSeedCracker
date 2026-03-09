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
/* 57 */   null(IdMap<T> globalMap, int bitsPerAxis) { super(globalMap, bitsPerAxis); }
/*    */   
/*    */   public Configuration getConfigurationForBitCount(int entryBits) {
/* 60 */     switch (entryBits) { case 0: case 1: case 2: case 3:  }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 66 */       new Configuration.Global(this.globalPaletteBitsInMemory, entryBits);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\Strategy$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
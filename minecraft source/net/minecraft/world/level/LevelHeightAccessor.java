/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface LevelHeightAccessor
/*    */ {
/*    */   int getHeight();
/*    */   
/*    */   int getMinY();
/*    */   
/* 16 */   default int getMaxY() { return getMinY() + getHeight() - 1; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   default int getSectionsCount() { return getMaxSectionY() - getMinSectionY() + 1; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   default int getMinSectionY() { return SectionPos.blockToSectionCoord(getMinY()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   default int getMaxSectionY() { return SectionPos.blockToSectionCoord(getMaxY()); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   default boolean isInsideBuildHeight(int blockY) { return (blockY >= getMinY() && blockY <= getMaxY()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   default boolean isOutsideBuildHeight(BlockPos pos) { return isOutsideBuildHeight(pos.getY()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   default boolean isOutsideBuildHeight(int blockY) { return (blockY < getMinY() || blockY > getMaxY()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   default int getSectionIndex(int blockY) { return getSectionIndexFromSectionY(SectionPos.blockToSectionCoord(blockY)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   default int getSectionIndexFromSectionY(int sectionY) { return sectionY - getMinSectionY(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   default int getSectionYFromSectionIndex(int sectionIndex) { return sectionIndex + getMinSectionY(); }
/*    */ 
/*    */   
/*    */   static LevelHeightAccessor create(final int minY, final int height) {
/* 64 */     return new LevelHeightAccessor()
/*    */       {
/*    */         public int getHeight() {
/* 67 */           return height;
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 72 */         public int getMinY() { return minY; }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\LevelHeightAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
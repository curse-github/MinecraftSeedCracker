/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public static enum DummyLightLayerEventListener
/*    */   implements LayerLightEventListener
/*    */ {
/* 16 */   INSTANCE;
/*    */ 
/*    */ 
/*    */   
/* 20 */   public DataLayer getDataLayerData(SectionPos pos) { return null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public int getLightValue(BlockPos pos) { return 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void checkBlock(BlockPos pos) {}
/*    */ 
/*    */ 
/*    */   
/* 34 */   public boolean hasLightWork() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public int runLightUpdates() { return 0; }
/*    */   
/*    */   public void updateSectionStatus(SectionPos pos, boolean sectionEmpty) {}
/*    */   
/*    */   public void setLightEnabled(ChunkPos pos, boolean enable) {}
/*    */   
/*    */   public void propagateLightSources(ChunkPos pos) {}
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\lighting\LayerLightEventListener$DummyLightLayerEventListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
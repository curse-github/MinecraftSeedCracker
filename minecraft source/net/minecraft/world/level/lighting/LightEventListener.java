/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ public interface LightEventListener
/*    */ {
/*    */   void checkBlock(BlockPos paramBlockPos);
/*    */   
/*    */   boolean hasLightWork();
/*    */   
/*    */   int runLightUpdates();
/*    */   
/* 15 */   default void updateSectionStatus(BlockPos pos, boolean sectionEmpty) { updateSectionStatus(SectionPos.of(pos), sectionEmpty); }
/*    */   
/*    */   void updateSectionStatus(SectionPos paramSectionPos, boolean paramBoolean);
/*    */   
/*    */   void setLightEnabled(ChunkPos paramChunkPos, boolean paramBoolean);
/*    */   
/*    */   void propagateLightSources(ChunkPos paramChunkPos);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\lighting\LightEventListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.server.level.progress;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements LevelLoadListener
/*    */ {
/*    */   public void start(LevelLoadListener.Stage stage, int totalChunks) {
/* 12 */     first.start(stage, totalChunks);
/* 13 */     second.start(stage, totalChunks);
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(LevelLoadListener.Stage stage, int currentChunks, int totalChunks) {
/* 18 */     first.update(stage, currentChunks, totalChunks);
/* 19 */     second.update(stage, currentChunks, totalChunks);
/*    */   }
/*    */ 
/*    */   
/*    */   public void finish(LevelLoadListener.Stage stage) {
/* 24 */     first.finish(stage);
/* 25 */     second.finish(stage);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateFocus(ResourceKey<Level> dimension, ChunkPos chunkPos) {
/* 30 */     first.updateFocus(dimension, chunkPos);
/* 31 */     second.updateFocus(dimension, chunkPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\progress\LevelLoadListener$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
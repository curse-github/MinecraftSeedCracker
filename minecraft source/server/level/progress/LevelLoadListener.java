/*    */ package net.minecraft.server.level.progress;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public interface LevelLoadListener {
/*    */   static LevelLoadListener compose(final LevelLoadListener first, final LevelLoadListener second) {
/*  9 */     return new LevelLoadListener()
/*    */       {
/*    */         public void start(Stage stage, int totalChunks) {
/* 12 */           first.start(stage, totalChunks);
/* 13 */           second.start(stage, totalChunks);
/*    */         }
/*    */ 
/*    */         
/*    */         public void update(Stage stage, int currentChunks, int totalChunks) {
/* 18 */           first.update(stage, currentChunks, totalChunks);
/* 19 */           second.update(stage, currentChunks, totalChunks);
/*    */         }
/*    */ 
/*    */         
/*    */         public void finish(Stage stage) {
/* 24 */           first.finish(stage);
/* 25 */           second.finish(stage);
/*    */         }
/*    */ 
/*    */         
/*    */         public void updateFocus(ResourceKey<Level> dimension, ChunkPos chunkPos) {
/* 30 */           first.updateFocus(dimension, chunkPos);
/* 31 */           second.updateFocus(dimension, chunkPos);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   void start(Stage paramStage, int paramInt);
/*    */   
/*    */   void update(Stage paramStage, int paramInt1, int paramInt2);
/*    */   
/*    */   void finish(Stage paramStage);
/*    */   
/*    */   void updateFocus(ResourceKey<Level> paramResourceKey, ChunkPos paramChunkPos);
/*    */   
/*    */   public enum Stage {
/* 45 */     START_SERVER,
/* 46 */     PREPARE_GLOBAL_SPAWN,
/* 47 */     LOAD_INITIAL_CHUNKS,
/* 48 */     LOAD_PLAYER_CHUNKS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\progress\LevelLoadListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
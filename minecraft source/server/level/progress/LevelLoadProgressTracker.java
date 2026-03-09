/*    */ package net.minecraft.server.level.progress;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class LevelLoadProgressTracker
/*    */   implements LevelLoadListener {
/*    */   private static final int PREPARE_SERVER_WEIGHT = 10;
/* 11 */   private static final int EXPECTED_PLAYER_CHUNKS = Mth.square(7);
/*    */ 
/*    */   
/*    */   private final boolean includePlayerChunks;
/*    */   
/*    */   private int totalWeight;
/*    */   
/*    */   private int finalizedWeight;
/*    */   
/*    */   private int segmentWeight;
/*    */   
/*    */   private float segmentFraction;
/*    */ 
/*    */   
/* 25 */   public LevelLoadProgressTracker(boolean includePlayerChunks) { this.includePlayerChunks = includePlayerChunks; }
/*    */ 
/*    */   
/*    */   public void start(LevelLoadListener.Stage stage, int totalChunks) {
/*    */     int playerChunksWeight;
/* 30 */     if (!tracksStage(stage)) {
/*    */       return;
/*    */     }
/* 33 */     switch (stage) {
/*    */       case LOAD_INITIAL_CHUNKS:
/* 35 */         playerChunksWeight = this.includePlayerChunks ? EXPECTED_PLAYER_CHUNKS : 0;
/*    */         
/* 37 */         this.totalWeight = 10 + totalChunks + playerChunksWeight;
/* 38 */         beginSegment(10);
/* 39 */         finishSegment();
/* 40 */         beginSegment(totalChunks); break;
/*    */       case LOAD_PLAYER_CHUNKS:
/* 42 */         beginSegment(EXPECTED_PLAYER_CHUNKS);
/*    */         break;
/*    */     } 
/*    */   }
/*    */   private void beginSegment(int weight) {
/* 47 */     this.segmentWeight = weight;
/* 48 */     this.segmentFraction = 0.0F;
/* 49 */     updateProgress();
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(LevelLoadListener.Stage stage, int currentChunks, int totalChunks) {
/* 54 */     if (tracksStage(stage)) {
/* 55 */       this.segmentFraction = (totalChunks == 0) ? 0.0F : (currentChunks / totalChunks);
/* 56 */       updateProgress();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void finish(LevelLoadListener.Stage stage) {
/* 62 */     if (tracksStage(stage)) {
/* 63 */       finishSegment();
/*    */     }
/*    */   }
/*    */   
/*    */   private void finishSegment() {
/* 68 */     this.finalizedWeight += this.segmentWeight;
/* 69 */     this.segmentWeight = 0;
/* 70 */     updateProgress();
/*    */   }
/*    */   
/*    */   private boolean tracksStage(LevelLoadListener.Stage stage) {
/* 74 */     switch (stage) { case LOAD_INITIAL_CHUNKS: case LOAD_PLAYER_CHUNKS:  }  return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void updateProgress() {
/* 82 */     if (this.totalWeight == 0) {
/* 83 */       this.progress = 0.0F;
/*    */     } else {
/* 85 */       float currentWeight = this.finalizedWeight + this.segmentFraction * this.segmentWeight;
/* 86 */       this.progress = currentWeight / this.totalWeight;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 91 */   public float get() { return this.progress; }
/*    */   
/*    */   public void updateFocus(ResourceKey<Level> dimension, ChunkPos chunkPos) {}
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\progress\LevelLoadProgressTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.server.level.progress;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class LoggingLevelLoadListener implements LevelLoadListener {
/* 13 */   private static final Logger LOGGER = LogUtils.getLogger(); private final boolean includePlayerChunks;
/*    */   private final LevelLoadProgressTracker progressTracker;
/*    */   private boolean closed;
/*    */   private long startTime;
/*    */   private long nextLogTime;
/*    */   
/*    */   public LoggingLevelLoadListener(boolean includePlayerChunks) {
/* 20 */     this.startTime = Float.MAX_VALUE;
/* 21 */     this.nextLogTime = Float.MAX_VALUE;
/*    */ 
/*    */     
/* 24 */     this.includePlayerChunks = includePlayerChunks;
/* 25 */     this.progressTracker = new LevelLoadProgressTracker(includePlayerChunks);
/*    */   }
/*    */ 
/*    */   
/* 29 */   public static LoggingLevelLoadListener forDedicatedServer() { return new LoggingLevelLoadListener(false); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static LoggingLevelLoadListener forSingleplayer() { return new LoggingLevelLoadListener(true); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void start(LevelLoadListener.Stage stage, int totalChunks) {
/* 38 */     if (this.closed) {
/*    */       return;
/*    */     }
/* 41 */     if (this.startTime == Float.MAX_VALUE) {
/* 42 */       long now = Util.getMillis();
/* 43 */       this.startTime = now;
/* 44 */       this.nextLogTime = now;
/*    */     } 
/* 46 */     this.progressTracker.start(stage, totalChunks);
/* 47 */     switch (stage) { case PREPARE_GLOBAL_SPAWN:
/* 48 */         LOGGER.info("Selecting global world spawn..."); break;
/* 49 */       case LOAD_INITIAL_CHUNKS: LOGGER.info("Loading {} persistent chunks...", Integer.valueOf(totalChunks)); break;
/* 50 */       case LOAD_PLAYER_CHUNKS: LOGGER.info("Loading {} chunks for player spawn...", Integer.valueOf(totalChunks));
/*    */         break; }
/*    */   
/*    */   }
/*    */   
/*    */   public void update(LevelLoadListener.Stage stage, int currentChunks, int totalChunks) {
/* 56 */     if (this.closed) {
/*    */       return;
/*    */     }
/* 59 */     this.progressTracker.update(stage, currentChunks, totalChunks);
/* 60 */     if (Util.getMillis() > this.nextLogTime) {
/* 61 */       this.nextLogTime += 500L;
/* 62 */       int percent = Mth.floor(this.progressTracker.get() * 100.0F);
/* 63 */       LOGGER.info(Component.translatable("menu.preparingSpawn", new Object[] { Integer.valueOf(percent) }).getString());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void finish(LevelLoadListener.Stage stage) {
/* 69 */     if (this.closed) {
/*    */       return;
/*    */     }
/* 72 */     this.progressTracker.finish(stage);
/* 73 */     LevelLoadListener.Stage finalStage = this.includePlayerChunks ? LevelLoadListener.Stage.LOAD_PLAYER_CHUNKS : LevelLoadListener.Stage.LOAD_INITIAL_CHUNKS;
/* 74 */     if (stage == finalStage) {
/* 75 */       LOGGER.info("Time elapsed: {} ms", Long.valueOf(Util.getMillis() - this.startTime));
/* 76 */       this.nextLogTime = Float.MAX_VALUE;
/* 77 */       this.closed = true;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void updateFocus(ResourceKey<Level> dimension, ChunkPos chunkPos) {}
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\progress\LoggingLevelLoadListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
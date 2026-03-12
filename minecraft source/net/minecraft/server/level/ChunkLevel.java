/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import net.minecraft.world.level.chunk.status.ChunkPyramid;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStep;
/*    */ import org.jetbrains.annotations.Contract;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChunkLevel
/*    */ {
/*    */   private static final int FULL_CHUNK_LEVEL = 33;
/*    */   private static final int BLOCK_TICKING_LEVEL = 32;
/*    */   private static final int ENTITY_TICKING_LEVEL = 31;
/* 16 */   private static final ChunkStep FULL_CHUNK_STEP = ChunkPyramid.GENERATION_PYRAMID.getStepTo(ChunkStatus.FULL);
/* 17 */   public static final int RADIUS_AROUND_FULL_CHUNK = FULL_CHUNK_STEP.accumulatedDependencies().getRadius();
/* 18 */   public static final int MAX_LEVEL = 33 + RADIUS_AROUND_FULL_CHUNK;
/*    */ 
/*    */   
/* 21 */   public static ChunkStatus generationStatus(int level) { return getStatusAroundFullChunk(level - 33, null); }
/*    */ 
/*    */   
/*    */   @Contract("_,!null->!null;_,_->_")
/*    */   public static ChunkStatus getStatusAroundFullChunk(int distanceToFullChunk, ChunkStatus defaultValue) {
/* 26 */     if (distanceToFullChunk > RADIUS_AROUND_FULL_CHUNK) {
/* 27 */       return defaultValue;
/*    */     }
/* 29 */     if (distanceToFullChunk <= 0) {
/* 30 */       return ChunkStatus.FULL;
/*    */     }
/* 32 */     return FULL_CHUNK_STEP.accumulatedDependencies().get(distanceToFullChunk);
/*    */   }
/*    */ 
/*    */   
/* 36 */   public static ChunkStatus getStatusAroundFullChunk(int distanceToFullChunk) { return getStatusAroundFullChunk(distanceToFullChunk, ChunkStatus.EMPTY); }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public static int byStatus(ChunkStatus status) { return 33 + FULL_CHUNK_STEP.getAccumulatedRadiusOf(status); }
/*    */ 
/*    */   
/*    */   public static FullChunkStatus fullStatus(int level) {
/* 44 */     if (level <= 31)
/* 45 */       return FullChunkStatus.ENTITY_TICKING; 
/* 46 */     if (level <= 32)
/* 47 */       return FullChunkStatus.BLOCK_TICKING; 
/* 48 */     if (level <= 33) {
/* 49 */       return FullChunkStatus.FULL;
/*    */     }
/* 51 */     return FullChunkStatus.INACCESSIBLE;
/*    */   }
/*    */   
/*    */   public static int byStatus(FullChunkStatus status) {
/* 55 */     switch (status) { default: throw new MatchException(null, null);case INACCESSIBLE: case FULL: case BLOCK_TICKING: case ENTITY_TICKING: break; }  return 
/*    */ 
/*    */ 
/*    */       
/* 59 */       31;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public static boolean isEntityTicking(int level) { return (level <= 31); }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public static boolean isBlockTicking(int level) { return (level <= 32); }
/*    */ 
/*    */ 
/*    */   
/* 72 */   public static boolean isLoaded(int level) { return (level <= MAX_LEVEL); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ChunkLevel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
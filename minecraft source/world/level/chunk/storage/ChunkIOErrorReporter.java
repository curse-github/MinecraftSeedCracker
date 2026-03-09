/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ChunkIOErrorReporter
/*    */ {
/*    */   void reportChunkLoadFailure(Throwable paramThrowable, RegionStorageInfo paramRegionStorageInfo, ChunkPos paramChunkPos);
/*    */   
/*    */   void reportChunkSaveFailure(Throwable paramThrowable, RegionStorageInfo paramRegionStorageInfo, ChunkPos paramChunkPos);
/*    */   
/*    */   static ReportedException createMisplacedChunkReport(ChunkPos storedPos, ChunkPos requestedPos) {
/* 20 */     CrashReport report = CrashReport.forThrowable(new IllegalStateException("Retrieved chunk position " + String.valueOf(storedPos) + " does not match requested " + String.valueOf(requestedPos)), "Chunk found in invalid location");
/* 21 */     CrashReportCategory category = report.addCategory("Misplaced Chunk");
/* 22 */     Objects.requireNonNull(storedPos); category.setDetail("Stored Position", storedPos::toString);
/* 23 */     return new ReportedException(report);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   default void reportMisplacedChunk(ChunkPos storedPos, ChunkPos requestedPos, RegionStorageInfo storageInfo) { reportChunkLoadFailure(createMisplacedChunkReport(storedPos, requestedPos), storageInfo, requestedPos); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\ChunkIOErrorReporter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
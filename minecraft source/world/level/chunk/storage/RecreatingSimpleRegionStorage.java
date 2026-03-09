/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixer;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import org.apache.commons.io.FileUtils;
/*    */ 
/*    */ public class RecreatingSimpleRegionStorage
/*    */   extends SimpleRegionStorage {
/*    */   private final IOWorker writeWorker;
/*    */   private final Path writeFolder;
/*    */   
/*    */   public RecreatingSimpleRegionStorage(RegionStorageInfo readInfo, Path readFolder, RegionStorageInfo writeInfo, Path writeFolder, DataFixer fixerUpper, boolean syncWrites, DataFixTypes dataFixType, Supplier<LegacyTagFixer> legacyFixer) {
/* 19 */     super(readInfo, readFolder, fixerUpper, syncWrites, dataFixType, legacyFixer);
/* 20 */     this.writeFolder = writeFolder;
/* 21 */     this.writeWorker = new IOWorker(writeInfo, writeFolder, syncWrites);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<Void> write(ChunkPos pos, Supplier<CompoundTag> supplier) {
/* 26 */     markChunkDone(pos);
/* 27 */     return this.writeWorker.store(pos, supplier);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 32 */     super.close();
/* 33 */     this.writeWorker.close();
/* 34 */     if (this.writeFolder.toFile().exists())
/* 35 */       FileUtils.deleteDirectory(this.writeFolder.toFile()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\RecreatingSimpleRegionStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
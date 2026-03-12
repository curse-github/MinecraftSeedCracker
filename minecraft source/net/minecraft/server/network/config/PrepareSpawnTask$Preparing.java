/*     */ package net.minecraft.server.network.config;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ChunkLoadCounter;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.server.level.progress.LevelLoadListener;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Preparing
/*     */   implements PrepareSpawnTask.State
/*     */ {
/*     */   private final ServerLevel spawnLevel;
/*     */   private final CompletableFuture<Vec3> spawnPosition;
/*     */   private final Vec2 spawnAngle;
/*     */   private CompletableFuture<?> chunkLoadFuture;
/*     */   private final ChunkLoadCounter chunkLoadCounter;
/*     */   
/*     */   private Preparing(ServerLevel spawnLevel, CompletableFuture<Vec3> spawnPosition, Vec2 spawnAngle) {
/* 125 */     this.chunkLoadCounter = new ChunkLoadCounter();
/*     */ 
/*     */     
/* 128 */     this.spawnLevel = spawnLevel;
/* 129 */     this.spawnPosition = spawnPosition;
/* 130 */     this.spawnAngle = spawnAngle;
/*     */   }
/*     */ 
/*     */   
/* 134 */   public void cancel() { this.spawnPosition.cancel(false); }
/*     */ 
/*     */   
/*     */   public PrepareSpawnTask.Ready tick() {
/* 138 */     if (!this.spawnPosition.isDone()) {
/* 139 */       return null;
/*     */     }
/*     */     
/* 142 */     Vec3 spawnPosition = (Vec3)this.spawnPosition.join();
/*     */     
/* 144 */     if (this.chunkLoadFuture == null) {
/* 145 */       ChunkPos spawnChunk = new ChunkPos(BlockPos.containing(spawnPosition));
/* 146 */       this.chunkLoadCounter.track(this.spawnLevel, () -> 
/* 147 */           this.chunkLoadFuture = this.spawnLevel.getChunkSource().addTicketAndLoadWithRadius(TicketType.PLAYER_SPAWN, spawnChunk, 3));
/*     */       
/* 149 */       PrepareSpawnTask.this.loadListener.start(LevelLoadListener.Stage.LOAD_PLAYER_CHUNKS, this.chunkLoadCounter.totalChunks());
/* 150 */       PrepareSpawnTask.this.loadListener.updateFocus(this.spawnLevel.dimension(), spawnChunk);
/*     */     } 
/*     */     
/* 153 */     PrepareSpawnTask.this.loadListener.update(LevelLoadListener.Stage.LOAD_PLAYER_CHUNKS, this.chunkLoadCounter.readyChunks(), this.chunkLoadCounter.totalChunks());
/*     */     
/* 155 */     if (!this.chunkLoadFuture.isDone()) {
/* 156 */       return null;
/*     */     }
/*     */     
/* 159 */     PrepareSpawnTask.this.loadListener.finish(LevelLoadListener.Stage.LOAD_PLAYER_CHUNKS);
/*     */     
/* 161 */     return new PrepareSpawnTask.Ready(PrepareSpawnTask.this, this.spawnLevel, spawnPosition, this.spawnAngle);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\config\PrepareSpawnTask$Preparing.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
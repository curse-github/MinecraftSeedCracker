/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.common.collect.Comparators;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.protocol.game.ClientboundChunkBatchFinishedPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundChunkBatchStartPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PlayerChunkSender
/*     */ {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger(); public static final float MIN_CHUNKS_PER_TICK = 0.01F; public static final float MAX_CHUNKS_PER_TICK = 64.0F;
/*     */   private static final float START_CHUNKS_PER_TICK = 9.0F;
/*     */   private static final int MAX_UNACKNOWLEDGED_BATCHES = 10;
/*     */   private final LongSet pendingChunks;
/*     */   
/*     */   public PlayerChunkSender(boolean memoryConnection) {
/*  31 */     this.pendingChunks = new LongOpenHashSet();
/*     */ 
/*     */     
/*  34 */     this.desiredChunksPerTick = 9.0F;
/*     */ 
/*     */     
/*  37 */     this.maxUnacknowledgedBatches = 1;
/*     */ 
/*     */     
/*  40 */     this.memoryConnection = memoryConnection;
/*     */   }
/*     */   private final boolean memoryConnection; private float desiredChunksPerTick; private float batchQuota; private int unacknowledgedBatches; private int maxUnacknowledgedBatches;
/*     */   
/*  44 */   public void markChunkPendingToSend(LevelChunk chunk) { this.pendingChunks.add(chunk.getPos().toLong()); }
/*     */ 
/*     */   
/*     */   public void dropChunk(ServerPlayer player, ChunkPos pos) {
/*  48 */     if (!this.pendingChunks.remove(pos.toLong()))
/*     */     {
/*  50 */       if (player.isAlive()) {
/*  51 */         player.connection.send(new ClientboundForgetLevelChunkPacket(pos));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendNextChunks(ServerPlayer player) {
/*  57 */     if (this.unacknowledgedBatches >= this.maxUnacknowledgedBatches) {
/*     */       return;
/*     */     }
/*  60 */     float maxBatchSize = Math.max(1.0F, this.desiredChunksPerTick);
/*  61 */     this.batchQuota = Math.min(this.batchQuota + this.desiredChunksPerTick, maxBatchSize);
/*  62 */     if (this.batchQuota < 1.0F) {
/*     */       return;
/*     */     }
/*  65 */     if (this.pendingChunks.isEmpty()) {
/*     */       return;
/*     */     }
/*  68 */     ServerLevel level = player.level();
/*  69 */     ChunkMap chunkMap = (level.getChunkSource()).chunkMap;
/*     */     
/*  71 */     List<LevelChunk> chunksToSend = collectChunksToSend(chunkMap, player.chunkPosition());
/*  72 */     if (chunksToSend.isEmpty()) {
/*     */       return;
/*     */     }
/*  75 */     ServerGamePacketListenerImpl connection = player.connection;
/*     */     
/*  77 */     this.unacknowledgedBatches++;
/*  78 */     connection.send(ClientboundChunkBatchStartPacket.INSTANCE);
/*  79 */     for (LevelChunk chunk : chunksToSend) {
/*  80 */       sendChunk(connection, level, chunk);
/*     */     }
/*  82 */     connection.send(new ClientboundChunkBatchFinishedPacket(chunksToSend.size()));
/*  83 */     this.batchQuota -= chunksToSend.size();
/*     */   }
/*     */   
/*     */   private static void sendChunk(ServerGamePacketListenerImpl connection, ServerLevel level, LevelChunk chunk) {
/*  87 */     connection.send(new ClientboundLevelChunkWithLightPacket(chunk, level.getLightEngine(), null, null));
/*  88 */     ChunkPos pos = chunk.getPos();
/*  89 */     if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/*  90 */       LOGGER.debug("SEN {}", pos);
/*     */     }
/*  92 */     level.debugSynchronizers().startTrackingChunk(connection.player, chunk.getPos());
/*     */   }
/*     */   
/*     */   private List<LevelChunk> collectChunksToSend(ChunkMap chunkMap, ChunkPos playerPos) {
/*     */     List<LevelChunk> chunks;
/*  97 */     int maxBatchSize = Mth.floor(this.batchQuota);
/*  98 */     if (this.memoryConnection || this.pendingChunks.size() <= maxBatchSize) {
/*     */       
/* 100 */       Objects.requireNonNull(chunkMap);
/*     */ 
/*     */       
/* 103 */       chunks = this.pendingChunks.longStream().mapToObj(chunkMap::getChunkToSend).filter(Objects::nonNull).sorted(Comparator.comparingInt(chunk -> playerPos.distanceSquared(chunk.getPos()))).toList();
/*     */     } else {
/*     */       
/* 106 */       Objects.requireNonNull(playerPos);
/*     */       
/* 108 */       Objects.requireNonNull(chunkMap);
/*     */       
/* 110 */       chunks = ((List)this.pendingChunks.stream().collect(Comparators.least(maxBatchSize, Comparator.comparingInt(playerPos::distanceSquared)))).stream().mapToLong(Long::longValue).mapToObj(chunkMap::getChunkToSend).filter(Objects::nonNull).toList();
/*     */     } 
/* 112 */     for (LevelChunk chunk : chunks) {
/* 113 */       this.pendingChunks.remove(chunk.getPos().toLong());
/*     */     }
/* 115 */     return chunks;
/*     */   }
/*     */   
/*     */   public void onChunkBatchReceivedByClient(float desiredChunksPerTick) {
/* 119 */     this.unacknowledgedBatches--;
/* 120 */     this.desiredChunksPerTick = Double.isNaN(desiredChunksPerTick) ? 0.01F : Mth.clamp(desiredChunksPerTick, 0.01F, 64.0F);
/* 121 */     if (this.unacknowledgedBatches == 0)
/*     */     {
/* 123 */       this.batchQuota = 1.0F;
/*     */     }
/* 125 */     this.maxUnacknowledgedBatches = 10;
/*     */   }
/*     */ 
/*     */   
/* 129 */   public boolean isPending(long pos) { return this.pendingChunks.contains(pos); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\PlayerChunkSender.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugChunkValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugEntityValuePacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
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
/*     */ public class SourceSynchronizer<T>
/*     */   extends TrackingDebugSynchronizer<T>
/*     */ {
/* 120 */   private final Map<ChunkPos, TrackingDebugSynchronizer.ValueSource<T>> chunkSources = new HashMap();
/* 121 */   private final Map<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> blockEntitySources = new HashMap();
/* 122 */   private final Map<UUID, TrackingDebugSynchronizer.ValueSource<T>> entitySources = new HashMap();
/*     */ 
/*     */   
/* 125 */   public SourceSynchronizer(DebugSubscription<T> subscription) { super(subscription); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void clear() {
/* 130 */     this.chunkSources.clear();
/* 131 */     this.blockEntitySources.clear();
/* 132 */     this.entitySources.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pollAndSendUpdates(ServerLevel level) {
/* 137 */     for (Map.Entry<ChunkPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.chunkSources.entrySet()) {
/* 138 */       DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource)entry.getValue()).pollUpdate(this.subscription);
/* 139 */       if (update != null) {
/* 140 */         ChunkPos chunkPos = (ChunkPos)entry.getKey();
/* 141 */         sendToPlayersTrackingChunk(level, chunkPos, new ClientboundDebugChunkValuePacket(chunkPos, update));
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     for (Map.Entry<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.blockEntitySources.entrySet()) {
/* 146 */       DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource)entry.getValue()).pollUpdate(this.subscription);
/* 147 */       if (update != null) {
/* 148 */         BlockPos blockPos = (BlockPos)entry.getKey();
/* 149 */         ChunkPos chunkPos = new ChunkPos(blockPos);
/* 150 */         sendToPlayersTrackingChunk(level, chunkPos, new ClientboundDebugBlockValuePacket(blockPos, update));
/*     */       } 
/*     */     } 
/*     */     
/* 154 */     for (Map.Entry<UUID, TrackingDebugSynchronizer.ValueSource<T>> entry : this.entitySources.entrySet()) {
/* 155 */       DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource)entry.getValue()).pollUpdate(this.subscription);
/* 156 */       if (update != null) {
/* 157 */         Entity entity = (Entity)Objects.requireNonNull(level.getEntity((UUID)entry.getKey()));
/* 158 */         sendToPlayersTrackingEntity(level, entity, new ClientboundDebugEntityValuePacket(entity.getId(), update));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 164 */   public void registerChunk(ChunkPos chunkPos, DebugValueSource.ValueGetter<T> getter) { this.chunkSources.put(chunkPos, new TrackingDebugSynchronizer.ValueSource(getter)); }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public void registerBlockEntity(BlockPos blockPos, DebugValueSource.ValueGetter<T> getter) { this.blockEntitySources.put(blockPos, new TrackingDebugSynchronizer.ValueSource(getter)); }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public void registerEntity(UUID entityId, DebugValueSource.ValueGetter<T> getter) { this.entitySources.put(entityId, new TrackingDebugSynchronizer.ValueSource(getter)); }
/*     */ 
/*     */   
/*     */   public void dropChunk(ChunkPos chunkPos) {
/* 176 */     this.chunkSources.remove(chunkPos);
/*     */ 
/*     */     
/* 179 */     Objects.requireNonNull(chunkPos); this.blockEntitySources.keySet().removeIf(chunkPos::contains);
/*     */   }
/*     */   
/*     */   public void dropBlockEntity(ServerLevel level, BlockPos blockPos) {
/* 183 */     TrackingDebugSynchronizer.ValueSource<T> source = (TrackingDebugSynchronizer.ValueSource)this.blockEntitySources.remove(blockPos);
/* 184 */     if (source != null) {
/* 185 */       ChunkPos chunkPos = new ChunkPos(blockPos);
/* 186 */       sendToPlayersTrackingChunk(level, chunkPos, new ClientboundDebugBlockValuePacket(blockPos, this.subscription.emptyUpdate()));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 191 */   public void dropEntity(Entity entity) { this.entitySources.remove(entity.getUUID()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 196 */     TrackingDebugSynchronizer.ValueSource<T> chunkSource = (TrackingDebugSynchronizer.ValueSource)this.chunkSources.get(chunkPos);
/* 197 */     if (chunkSource != null && chunkSource.lastSyncedValue != null) {
/* 198 */       player.connection.send(new ClientboundDebugChunkValuePacket(chunkPos, this.subscription.packUpdate(chunkSource.lastSyncedValue)));
/*     */     }
/*     */     
/* 201 */     for (Map.Entry<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.blockEntitySources.entrySet()) {
/* 202 */       T lastValue = (T)((TrackingDebugSynchronizer.ValueSource)entry.getValue()).lastSyncedValue;
/* 203 */       if (lastValue == null) {
/*     */         continue;
/*     */       }
/* 206 */       BlockPos blockPos = (BlockPos)entry.getKey();
/* 207 */       if (chunkPos.contains(blockPos)) {
/* 208 */         player.connection.send(new ClientboundDebugBlockValuePacket(blockPos, this.subscription.packUpdate(lastValue)));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void sendInitialEntity(ServerPlayer player, Entity entity) {
/* 215 */     TrackingDebugSynchronizer.ValueSource<T> source = (TrackingDebugSynchronizer.ValueSource)this.entitySources.get(entity.getUUID());
/* 216 */     if (source != null && source.lastSyncedValue != null)
/* 217 */       player.connection.send(new ClientboundDebugEntityValuePacket(entity.getId(), this.subscription.packUpdate(source.lastSyncedValue))); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\TrackingDebugSynchronizer$SourceSynchronizer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.util.debug;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugEntityValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugEventPacket;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ 
/*     */ public class LevelDebugSynchronizers {
/*     */   private final ServerLevel level;
/*     */   private final List<TrackingDebugSynchronizer<?>> allSynchronizers;
/*     */   private final Map<DebugSubscription<?>, TrackingDebugSynchronizer.SourceSynchronizer<?>> sourceSynchronizers;
/*     */   
/*     */   public LevelDebugSynchronizers(ServerLevel level) {
/*  28 */     this.allSynchronizers = new ArrayList();
/*     */     
/*  30 */     this.sourceSynchronizers = new HashMap();
/*  31 */     this.poiSynchronizer = new TrackingDebugSynchronizer.PoiSynchronizer();
/*  32 */     this.villageSectionSynchronizer = new TrackingDebugSynchronizer.VillageSectionSynchronizer();
/*     */ 
/*     */     
/*  35 */     this.sleeping = true;
/*  36 */     this.enabledSubscriptions = Set.of();
/*     */ 
/*     */     
/*  39 */     this.level = level;
/*  40 */     for (DebugSubscription<?> subscription : BuiltInRegistries.DEBUG_SUBSCRIPTION) {
/*  41 */       if (subscription.valueStreamCodec() != null) {
/*  42 */         this.sourceSynchronizers.put(subscription, new TrackingDebugSynchronizer.SourceSynchronizer(subscription));
/*     */       }
/*     */     } 
/*  45 */     this.allSynchronizers.addAll(this.sourceSynchronizers.values());
/*  46 */     this.allSynchronizers.add(this.poiSynchronizer);
/*  47 */     this.allSynchronizers.add(this.villageSectionSynchronizer);
/*     */   }
/*     */   private final TrackingDebugSynchronizer.PoiSynchronizer poiSynchronizer; private final TrackingDebugSynchronizer.VillageSectionSynchronizer villageSectionSynchronizer; private boolean sleeping; private Set<DebugSubscription<?>> enabledSubscriptions;
/*     */   public void tick(ServerDebugSubscribers serverSubscribers) {
/*  51 */     this.enabledSubscriptions = serverSubscribers.enabledSubscriptions();
/*     */     
/*  53 */     boolean shouldSleep = this.enabledSubscriptions.isEmpty();
/*  54 */     if (this.sleeping != shouldSleep) {
/*  55 */       this.sleeping = shouldSleep;
/*  56 */       if (shouldSleep) {
/*  57 */         for (TrackingDebugSynchronizer<?> synchronizer : this.allSynchronizers) {
/*  58 */           synchronizer.clear();
/*     */         }
/*     */       } else {
/*  61 */         wakeUp();
/*     */       } 
/*     */     } 
/*     */     
/*  65 */     if (!this.sleeping) {
/*  66 */       for (TrackingDebugSynchronizer<?> synchronizer : this.allSynchronizers) {
/*  67 */         synchronizer.tick(this.level);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void wakeUp() {
/*  73 */     ChunkMap chunkMap = (this.level.getChunkSource()).chunkMap;
/*  74 */     chunkMap.forEachReadyToSendChunk(this::registerChunk);
/*  75 */     for (Entity entity : this.level.getAllEntities()) {
/*  76 */       if (chunkMap.isTrackedByAnyPlayer(entity)) {
/*  77 */         registerEntity(entity);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  84 */   private <T> TrackingDebugSynchronizer.SourceSynchronizer<T> getSourceSynchronizer(DebugSubscription<T> subscription) { return (TrackingDebugSynchronizer.SourceSynchronizer)this.sourceSynchronizers.get(subscription); }
/*     */ 
/*     */   
/*     */   public void registerChunk(final LevelChunk chunk) {
/*  88 */     if (this.sleeping) {
/*     */       return;
/*     */     }
/*  91 */     chunk.registerDebugValues(this.level, new DebugValueSource.Registration()
/*     */         {
/*     */           public <T> void register(DebugSubscription<T> subscription, DebugValueSource.ValueGetter<T> getter) {
/*  94 */             LevelDebugSynchronizers.this.getSourceSynchronizer(subscription).registerChunk(chunk.getPos(), getter);
/*     */           }
/*     */         });
/*  97 */     chunk.getBlockEntities().values().forEach(this::registerBlockEntity);
/*     */   }
/*     */   
/*     */   public void dropChunk(ChunkPos chunkPos) {
/* 101 */     if (this.sleeping) {
/*     */       return;
/*     */     }
/* 104 */     for (TrackingDebugSynchronizer.SourceSynchronizer<?> synchronizer : this.sourceSynchronizers.values()) {
/* 105 */       synchronizer.dropChunk(chunkPos);
/*     */     }
/*     */   }
/*     */   
/*     */   public void registerBlockEntity(final BlockEntity blockEntity) {
/* 110 */     if (this.sleeping) {
/*     */       return;
/*     */     }
/* 113 */     blockEntity.registerDebugValues(this.level, new DebugValueSource.Registration()
/*     */         {
/*     */           public <T> void register(DebugSubscription<T> subscription, DebugValueSource.ValueGetter<T> getter) {
/* 116 */             LevelDebugSynchronizers.this.getSourceSynchronizer(subscription).registerBlockEntity(blockEntity.getBlockPos(), getter);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropBlockEntity(BlockPos blockPos) {
/* 123 */     if (this.sleeping) {
/*     */       return;
/*     */     }
/* 126 */     for (TrackingDebugSynchronizer.SourceSynchronizer<?> synchronizer : this.sourceSynchronizers.values()) {
/* 127 */       synchronizer.dropBlockEntity(this.level, blockPos);
/*     */     }
/*     */   }
/*     */   
/*     */   public void registerEntity(final Entity entity) {
/* 132 */     if (this.sleeping) {
/*     */       return;
/*     */     }
/* 135 */     entity.registerDebugValues(this.level, new DebugValueSource.Registration()
/*     */         {
/*     */           public <T> void register(DebugSubscription<T> subscription, DebugValueSource.ValueGetter<T> getter) {
/* 138 */             LevelDebugSynchronizers.this.getSourceSynchronizer(subscription).registerEntity(entity.getUUID(), getter);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void dropEntity(Entity entity) {
/* 144 */     if (this.sleeping) {
/*     */       return;
/*     */     }
/* 147 */     for (TrackingDebugSynchronizer.SourceSynchronizer<?> synchronizer : this.sourceSynchronizers.values()) {
/* 148 */       synchronizer.dropEntity(entity);
/*     */     }
/*     */   }
/*     */   
/*     */   public void startTrackingChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 153 */     if (this.sleeping) {
/*     */       return;
/*     */     }
/* 156 */     for (TrackingDebugSynchronizer<?> synchronizer : this.allSynchronizers) {
/* 157 */       synchronizer.startTrackingChunk(player, chunkPos);
/*     */     }
/*     */   }
/*     */   
/*     */   public void startTrackingEntity(ServerPlayer player, Entity entity) {
/* 162 */     if (this.sleeping) {
/*     */       return;
/*     */     }
/* 165 */     for (TrackingDebugSynchronizer<?> synchronizer : this.allSynchronizers) {
/* 166 */       synchronizer.startTrackingEntity(player, entity);
/*     */     }
/*     */   }
/*     */   
/*     */   public void registerPoi(PoiRecord poi) {
/* 171 */     if (this.sleeping) {
/*     */       return;
/*     */     }
/* 174 */     this.poiSynchronizer.onPoiAdded(this.level, poi);
/* 175 */     this.villageSectionSynchronizer.onPoiAdded(this.level, poi);
/*     */   }
/*     */   
/*     */   public void updatePoi(BlockPos pos) {
/* 179 */     if (this.sleeping) {
/*     */       return;
/*     */     }
/* 182 */     this.poiSynchronizer.onPoiTicketCountChanged(this.level, pos);
/*     */   }
/*     */   
/*     */   public void dropPoi(BlockPos pos) {
/* 186 */     if (this.sleeping) {
/*     */       return;
/*     */     }
/* 189 */     this.poiSynchronizer.onPoiRemoved(this.level, pos);
/* 190 */     this.villageSectionSynchronizer.onPoiRemoved(this.level, pos);
/*     */   }
/*     */ 
/*     */   
/* 194 */   public boolean hasAnySubscriberFor(DebugSubscription<?> subscription) { return this.enabledSubscriptions.contains(subscription); }
/*     */ 
/*     */   
/*     */   public <T> void sendBlockValue(BlockPos blockPos, DebugSubscription<T> subscription, T value) {
/* 198 */     if (hasAnySubscriberFor(subscription)) {
/* 199 */       broadcastToTracking(new ChunkPos(blockPos), subscription, new ClientboundDebugBlockValuePacket(blockPos, subscription.packUpdate(value)));
/*     */     }
/*     */   }
/*     */   
/*     */   public <T> void clearBlockValue(BlockPos blockPos, DebugSubscription<T> subscription) {
/* 204 */     if (hasAnySubscriberFor(subscription)) {
/* 205 */       broadcastToTracking(new ChunkPos(blockPos), subscription, new ClientboundDebugBlockValuePacket(blockPos, subscription.emptyUpdate()));
/*     */     }
/*     */   }
/*     */   
/*     */   public <T> void sendEntityValue(Entity entity, DebugSubscription<T> subscription, T value) {
/* 210 */     if (hasAnySubscriberFor(subscription)) {
/* 211 */       broadcastToTracking(entity, subscription, new ClientboundDebugEntityValuePacket(entity.getId(), subscription.packUpdate(value)));
/*     */     }
/*     */   }
/*     */   
/*     */   public <T> void clearEntityValue(Entity entity, DebugSubscription<T> subscription) {
/* 216 */     if (hasAnySubscriberFor(subscription)) {
/* 217 */       broadcastToTracking(entity, subscription, new ClientboundDebugEntityValuePacket(entity.getId(), subscription.emptyUpdate()));
/*     */     }
/*     */   }
/*     */   
/*     */   public <T> void broadcastEventToTracking(BlockPos blockPos, DebugSubscription<T> subscription, T value) {
/* 222 */     if (hasAnySubscriberFor(subscription)) {
/* 223 */       broadcastToTracking(new ChunkPos(blockPos), subscription, new ClientboundDebugEventPacket(subscription.packEvent(value)));
/*     */     }
/*     */   }
/*     */   
/*     */   private void broadcastToTracking(ChunkPos trackedChunk, DebugSubscription<?> subscription, Packet<? super ClientGamePacketListener> packet) {
/* 228 */     ChunkMap chunkMap = (this.level.getChunkSource()).chunkMap;
/* 229 */     for (ServerPlayer player : chunkMap.getPlayers(trackedChunk, false)) {
/* 230 */       if (player.debugSubscriptions().contains(subscription)) {
/* 231 */         player.connection.send(packet);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void broadcastToTracking(Entity trackedEntity, DebugSubscription<?> subscription, Packet<? super ClientGamePacketListener> packet) {
/* 237 */     ChunkMap chunkMap = (this.level.getChunkSource()).chunkMap;
/* 238 */     chunkMap.sendToTrackingPlayersFiltered(trackedEntity, packet, player -> 
/*     */ 
/*     */         
/* 241 */         player.debugSubscriptions().contains(subscription));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\LevelDebugSynchronizers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
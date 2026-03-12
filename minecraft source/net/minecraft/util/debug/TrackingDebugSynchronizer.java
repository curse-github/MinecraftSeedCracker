/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugChunkValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugEntityValuePacket;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ 
/*     */ public abstract class TrackingDebugSynchronizer<T>
/*     */   extends Object
/*     */ {
/*     */   protected final DebugSubscription<T> subscription;
/*     */   private final Set<UUID> subscribedPlayers;
/*     */   
/*     */   public TrackingDebugSynchronizer(DebugSubscription<T> subscription) {
/*  36 */     this.subscribedPlayers = new ObjectOpenHashSet();
/*     */ 
/*     */     
/*  39 */     this.subscription = subscription;
/*     */   }
/*     */   
/*     */   public final void tick(ServerLevel level) {
/*  43 */     for (ServerPlayer player : level.players()) {
/*  44 */       boolean wasSubscribed = this.subscribedPlayers.contains(player.getUUID());
/*  45 */       boolean isSubscribed = player.debugSubscriptions().contains(this.subscription);
/*  46 */       if (isSubscribed == wasSubscribed) {
/*     */         continue;
/*     */       }
/*  49 */       if (isSubscribed) {
/*  50 */         addSubscriber(player); continue;
/*     */       } 
/*  52 */       this.subscribedPlayers.remove(player.getUUID());
/*     */     } 
/*     */     
/*  55 */     this.subscribedPlayers.removeIf(id -> (level.getPlayerByUUID(id) == null));
/*     */ 
/*     */     
/*  58 */     if (!this.subscribedPlayers.isEmpty()) {
/*  59 */       pollAndSendUpdates(level);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addSubscriber(ServerPlayer player) {
/*  64 */     this.subscribedPlayers.add(player.getUUID());
/*     */     
/*  66 */     player.getChunkTrackingView().forEach(chunkPos -> {
/*  67 */           if (!player.connection.chunkSender.isPending(chunkPos.toLong())) {
/*  68 */             startTrackingChunk(player, chunkPos);
/*     */           }
/*     */         });
/*     */     
/*  72 */     (player.level().getChunkSource()).chunkMap.forEachEntityTrackedBy(player, entity -> 
/*  73 */         startTrackingEntity(player, entity));
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos trackedChunk, Packet<? super ClientGamePacketListener> packet) {
/*  78 */     ChunkMap chunkMap = (level.getChunkSource()).chunkMap;
/*  79 */     for (UUID playerId : this.subscribedPlayers) {
/*  80 */       Player player1 = level.getPlayerByUUID(playerId); if (player1 instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)player1; if (chunkMap.isChunkTracked(player, trackedChunk.x, trackedChunk.z))
/*  81 */           player.connection.send(packet);  }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void sendToPlayersTrackingEntity(ServerLevel level, Entity trackedEntity, Packet<? super ClientGamePacketListener> packet) {
/*  87 */     ChunkMap chunkMap = (level.getChunkSource()).chunkMap;
/*  88 */     chunkMap.sendToTrackingPlayersFiltered(trackedEntity, packet, player -> 
/*     */ 
/*     */         
/*  91 */         this.subscribedPlayers.contains(player.getUUID()));
/*     */   }
/*     */ 
/*     */   
/*     */   public final void startTrackingChunk(ServerPlayer player, ChunkPos chunkPos) {
/*  96 */     if (this.subscribedPlayers.contains(player.getUUID())) {
/*  97 */       sendInitialChunk(player, chunkPos);
/*     */     }
/*     */   }
/*     */   
/*     */   public final void startTrackingEntity(ServerPlayer player, Entity entity) {
/* 102 */     if (this.subscribedPlayers.contains(player.getUUID())) {
/* 103 */       sendInitialEntity(player, entity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void clear() {}
/*     */ 
/*     */   
/*     */   protected void pollAndSendUpdates(ServerLevel level) {}
/*     */   
/*     */   protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {}
/*     */   
/*     */   protected void sendInitialEntity(ServerPlayer player, Entity entity) {}
/*     */   
/*     */   public static class SourceSynchronizer<T>
/*     */     extends TrackingDebugSynchronizer<T>
/*     */   {
/* 120 */     private final Map<ChunkPos, TrackingDebugSynchronizer.ValueSource<T>> chunkSources = new HashMap();
/* 121 */     private final Map<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> blockEntitySources = new HashMap();
/* 122 */     private final Map<UUID, TrackingDebugSynchronizer.ValueSource<T>> entitySources = new HashMap();
/*     */ 
/*     */     
/* 125 */     public SourceSynchronizer(DebugSubscription<T> subscription) { super(subscription); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void clear() {
/* 130 */       this.chunkSources.clear();
/* 131 */       this.blockEntitySources.clear();
/* 132 */       this.entitySources.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void pollAndSendUpdates(ServerLevel level) {
/* 137 */       for (Map.Entry<ChunkPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.chunkSources.entrySet()) {
/* 138 */         DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource)entry.getValue()).pollUpdate(this.subscription);
/* 139 */         if (update != null) {
/* 140 */           ChunkPos chunkPos = (ChunkPos)entry.getKey();
/* 141 */           sendToPlayersTrackingChunk(level, chunkPos, new ClientboundDebugChunkValuePacket(chunkPos, update));
/*     */         } 
/*     */       } 
/*     */       
/* 145 */       for (Map.Entry<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.blockEntitySources.entrySet()) {
/* 146 */         DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource)entry.getValue()).pollUpdate(this.subscription);
/* 147 */         if (update != null) {
/* 148 */           BlockPos blockPos = (BlockPos)entry.getKey();
/* 149 */           ChunkPos chunkPos = new ChunkPos(blockPos);
/* 150 */           sendToPlayersTrackingChunk(level, chunkPos, new ClientboundDebugBlockValuePacket(blockPos, update));
/*     */         } 
/*     */       } 
/*     */       
/* 154 */       for (Map.Entry<UUID, TrackingDebugSynchronizer.ValueSource<T>> entry : this.entitySources.entrySet()) {
/* 155 */         DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource)entry.getValue()).pollUpdate(this.subscription);
/* 156 */         if (update != null) {
/* 157 */           Entity entity = (Entity)Objects.requireNonNull(level.getEntity((UUID)entry.getKey()));
/* 158 */           sendToPlayersTrackingEntity(level, entity, new ClientboundDebugEntityValuePacket(entity.getId(), update));
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 164 */     public void registerChunk(ChunkPos chunkPos, DebugValueSource.ValueGetter<T> getter) { this.chunkSources.put(chunkPos, new TrackingDebugSynchronizer.ValueSource(getter)); }
/*     */ 
/*     */ 
/*     */     
/* 168 */     public void registerBlockEntity(BlockPos blockPos, DebugValueSource.ValueGetter<T> getter) { this.blockEntitySources.put(blockPos, new TrackingDebugSynchronizer.ValueSource(getter)); }
/*     */ 
/*     */ 
/*     */     
/* 172 */     public void registerEntity(UUID entityId, DebugValueSource.ValueGetter<T> getter) { this.entitySources.put(entityId, new TrackingDebugSynchronizer.ValueSource(getter)); }
/*     */ 
/*     */     
/*     */     public void dropChunk(ChunkPos chunkPos) {
/* 176 */       this.chunkSources.remove(chunkPos);
/*     */ 
/*     */       
/* 179 */       Objects.requireNonNull(chunkPos); this.blockEntitySources.keySet().removeIf(chunkPos::contains);
/*     */     }
/*     */     
/*     */     public void dropBlockEntity(ServerLevel level, BlockPos blockPos) {
/* 183 */       TrackingDebugSynchronizer.ValueSource<T> source = (TrackingDebugSynchronizer.ValueSource)this.blockEntitySources.remove(blockPos);
/* 184 */       if (source != null) {
/* 185 */         ChunkPos chunkPos = new ChunkPos(blockPos);
/* 186 */         sendToPlayersTrackingChunk(level, chunkPos, new ClientboundDebugBlockValuePacket(blockPos, this.subscription.emptyUpdate()));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 191 */     public void dropEntity(Entity entity) { this.entitySources.remove(entity.getUUID()); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 196 */       TrackingDebugSynchronizer.ValueSource<T> chunkSource = (TrackingDebugSynchronizer.ValueSource)this.chunkSources.get(chunkPos);
/* 197 */       if (chunkSource != null && chunkSource.lastSyncedValue != null) {
/* 198 */         player.connection.send(new ClientboundDebugChunkValuePacket(chunkPos, this.subscription.packUpdate(chunkSource.lastSyncedValue)));
/*     */       }
/*     */       
/* 201 */       for (Map.Entry<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.blockEntitySources.entrySet()) {
/* 202 */         T lastValue = (T)((TrackingDebugSynchronizer.ValueSource)entry.getValue()).lastSyncedValue;
/* 203 */         if (lastValue == null) {
/*     */           continue;
/*     */         }
/* 206 */         BlockPos blockPos = (BlockPos)entry.getKey();
/* 207 */         if (chunkPos.contains(blockPos)) {
/* 208 */           player.connection.send(new ClientboundDebugBlockValuePacket(blockPos, this.subscription.packUpdate(lastValue)));
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void sendInitialEntity(ServerPlayer player, Entity entity) {
/* 215 */       TrackingDebugSynchronizer.ValueSource<T> source = (TrackingDebugSynchronizer.ValueSource)this.entitySources.get(entity.getUUID());
/* 216 */       if (source != null && source.lastSyncedValue != null)
/* 217 */         player.connection.send(new ClientboundDebugEntityValuePacket(entity.getId(), this.subscription.packUpdate(source.lastSyncedValue))); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ValueSource<T>
/*     */     extends Object
/*     */   {
/*     */     private final DebugValueSource.ValueGetter<T> getter;
/*     */     private T lastSyncedValue;
/*     */     
/* 227 */     private ValueSource(DebugValueSource.ValueGetter<T> getter) { this.getter = getter; }
/*     */ 
/*     */     
/*     */     public DebugSubscription.Update<T> pollUpdate(DebugSubscription<T> subscription) {
/* 231 */       T newValue = (T)this.getter.get();
/* 232 */       if (!Objects.equals(newValue, this.lastSyncedValue)) {
/* 233 */         this.lastSyncedValue = newValue;
/* 234 */         return subscription.packUpdate(newValue);
/*     */       } 
/* 236 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PoiSynchronizer
/*     */     extends TrackingDebugSynchronizer<DebugPoiInfo> {
/* 242 */     public PoiSynchronizer() { super(DebugSubscriptions.POIS); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 247 */       ServerLevel level = player.level();
/* 248 */       PoiManager poiManager = level.getPoiManager();
/* 249 */       poiManager.getInChunk(t -> true, chunkPos, PoiManager.Occupancy.ANY).forEach(record -> 
/* 250 */           player.connection.send(new ClientboundDebugBlockValuePacket(record
/* 251 */               .getPos(), this.subscription.packUpdate(new DebugPoiInfo(record)))));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 257 */     public void onPoiAdded(ServerLevel level, PoiRecord record) { sendToPlayersTrackingChunk(level, new ChunkPos(record.getPos()), new ClientboundDebugBlockValuePacket(record.getPos(), this.subscription.packUpdate(new DebugPoiInfo(record)))); }
/*     */ 
/*     */ 
/*     */     
/* 261 */     public void onPoiRemoved(ServerLevel level, BlockPos poiPos) { sendToPlayersTrackingChunk(level, new ChunkPos(poiPos), new ClientboundDebugBlockValuePacket(poiPos, this.subscription.emptyUpdate())); }
/*     */ 
/*     */ 
/*     */     
/* 265 */     public void onPoiTicketCountChanged(ServerLevel level, BlockPos poiPos) { sendToPlayersTrackingChunk(level, new ChunkPos(poiPos), new ClientboundDebugBlockValuePacket(poiPos, this.subscription.packUpdate(level.getPoiManager().getDebugPoiInfo(poiPos)))); }
/*     */   }
/*     */   
/*     */   public static class VillageSectionSynchronizer
/*     */     extends TrackingDebugSynchronizer<Unit>
/*     */   {
/* 271 */     public VillageSectionSynchronizer() { super(DebugSubscriptions.VILLAGE_SECTIONS); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 276 */       ServerLevel level = player.level();
/* 277 */       PoiManager poiManager = level.getPoiManager();
/* 278 */       poiManager.getInChunk(t -> true, chunkPos, PoiManager.Occupancy.ANY).forEach(record -> {
/* 279 */             SectionPos centerSection = SectionPos.of(record.getPos());
/* 280 */             forEachVillageSectionUpdate(level, centerSection, ());
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     public void onPoiAdded(ServerLevel level, PoiRecord record) { sendVillageSectionsPacket(level, record.getPos()); }
/*     */ 
/*     */ 
/*     */     
/* 294 */     public void onPoiRemoved(ServerLevel level, BlockPos poiPos) { sendVillageSectionsPacket(level, poiPos); }
/*     */ 
/*     */     
/*     */     private void sendVillageSectionsPacket(ServerLevel level, BlockPos poiPos) {
/* 298 */       forEachVillageSectionUpdate(level, SectionPos.of(poiPos), (sectionPos, isVillage) -> {
/* 299 */             BlockPos sectionBlockPos = sectionPos.center();
/* 300 */             if (isVillage.booleanValue()) {
/* 301 */               sendToPlayersTrackingChunk(level, new ChunkPos(sectionBlockPos), new ClientboundDebugBlockValuePacket(sectionBlockPos, this.subscription.packUpdate(Unit.INSTANCE)));
/*     */             } else {
/* 303 */               sendToPlayersTrackingChunk(level, new ChunkPos(sectionBlockPos), new ClientboundDebugBlockValuePacket(sectionBlockPos, this.subscription.emptyUpdate()));
/*     */             } 
/*     */           });
/*     */     }
/*     */     
/*     */     private static void forEachVillageSectionUpdate(ServerLevel level, SectionPos centerSection, BiConsumer<SectionPos, Boolean> consumer) {
/* 309 */       for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
/* 310 */         for (int offsetX = -1; offsetX <= 1; offsetX++) {
/* 311 */           for (int offsetY = -1; offsetY <= 1; offsetY++) {
/* 312 */             SectionPos sectionPos = centerSection.offset(offsetX, offsetY, offsetZ);
/* 313 */             if (level.isVillage(sectionPos.center())) {
/* 314 */               consumer.accept(sectionPos, Boolean.valueOf(true));
/*     */             } else {
/* 316 */               consumer.accept(sectionPos, Boolean.valueOf(false));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\TrackingDebugSynchronizer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
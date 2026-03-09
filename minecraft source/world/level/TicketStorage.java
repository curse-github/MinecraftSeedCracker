/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.server.level.ChunkHolder;
/*     */ import net.minecraft.server.level.ChunkLevel;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.server.level.FullChunkStatus;
/*     */ import net.minecraft.server.level.Ticket;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TicketStorage
/*     */   extends SavedData
/*     */ {
/*     */   private static final int INITIAL_TICKET_LIST_CAPACITY = 4;
/*  41 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  43 */   private static final Codec<Pair<ChunkPos, Ticket>> TICKET_ENTRY = Codec.mapPair(ChunkPos.CODEC
/*  44 */       .fieldOf("chunk_pos"), Ticket.CODEC)
/*     */     
/*  46 */     .codec();
/*     */   
/*  48 */   public static final Codec<TicketStorage> CODEC = RecordCodecBuilder.create(i -> i.group(TICKET_ENTRY
/*  49 */         .listOf().optionalFieldOf("tickets", List.of()).forGetter(TicketStorage::packTickets))
/*  50 */       .apply(i, TicketStorage::fromPacked));
/*     */   
/*  52 */   public static final SavedDataType<TicketStorage> TYPE = new SavedDataType("chunks", TicketStorage::new, CODEC, DataFixTypes.SAVED_DATA_FORCED_CHUNKS); private final Long2ObjectOpenHashMap<List<Ticket>> tickets;
/*     */   private final Long2ObjectOpenHashMap<List<Ticket>> deactivatedTickets;
/*     */   
/*     */   private TicketStorage(Long2ObjectOpenHashMap<List<Ticket>> tickets, Long2ObjectOpenHashMap<List<Ticket>> deactivatedTickets) {
/*  56 */     this.chunksWithForcedTickets = new LongOpenHashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     this.tickets = tickets;
/*  62 */     this.deactivatedTickets = deactivatedTickets;
/*  63 */     updateForcedChunks();
/*     */   }
/*     */   private LongSet chunksWithForcedTickets; private ChunkUpdated loadingChunkUpdatedListener; private ChunkUpdated simulationChunkUpdatedListener;
/*     */   
/*  67 */   public TicketStorage() { this(new Long2ObjectOpenHashMap(4), new Long2ObjectOpenHashMap()); }
/*     */ 
/*     */   
/*     */   private static TicketStorage fromPacked(List<Pair<ChunkPos, Ticket>> tickets) {
/*  71 */     Long2ObjectOpenHashMap<List<Ticket>> ticketsToLoad = new Long2ObjectOpenHashMap<List<Ticket>>();
/*  72 */     for (Pair<ChunkPos, Ticket> ticket : tickets) {
/*  73 */       ChunkPos pos = (ChunkPos)ticket.getFirst();
/*  74 */       List<Ticket> ticketsInChunk = (List)ticketsToLoad.computeIfAbsent(pos.toLong(), k -> new ObjectArrayList(4));
/*  75 */       ticketsInChunk.add((Ticket)ticket.getSecond());
/*     */     } 
/*     */     
/*  78 */     return new TicketStorage(new Long2ObjectOpenHashMap(4), ticketsToLoad);
/*     */   }
/*     */   
/*     */   private List<Pair<ChunkPos, Ticket>> packTickets() {
/*  82 */     List<Pair<ChunkPos, Ticket>> tickets = new ArrayList<Pair<ChunkPos, Ticket>>();
/*  83 */     forEachTicket((pos, ticket) -> {
/*  84 */           if (ticket.getType().persist()) {
/*  85 */             tickets.add(new Pair(pos, ticket));
/*     */           }
/*     */         });
/*  88 */     return tickets;
/*     */   }
/*     */   
/*     */   private void forEachTicket(BiConsumer<ChunkPos, Ticket> output) {
/*  92 */     forEachTicket(output, this.tickets);
/*  93 */     forEachTicket(output, this.deactivatedTickets);
/*     */   }
/*     */   
/*     */   private static void forEachTicket(BiConsumer<ChunkPos, Ticket> output, Long2ObjectOpenHashMap<List<Ticket>> tickets) {
/*  97 */     for (ObjectIterator objectIterator = Long2ObjectMaps.fastIterable(tickets).iterator(); objectIterator.hasNext(); ) { Long2ObjectMap.Entry<List<Ticket>> entry = (Long2ObjectMap.Entry)objectIterator.next();
/*  98 */       ChunkPos chunkPos = new ChunkPos(entry.getLongKey());
/*  99 */       for (Ticket ticket : (List)entry.getValue()) {
/* 100 */         output.accept(chunkPos, ticket);
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   public void activateAllDeactivatedTickets() {
/* 106 */     for (ObjectIterator objectIterator = Long2ObjectMaps.fastIterable(this.deactivatedTickets).iterator(); objectIterator.hasNext(); ) { Long2ObjectMap.Entry<List<Ticket>> entry = (Long2ObjectMap.Entry)objectIterator.next();
/* 107 */       for (Ticket ticket : (List)entry.getValue()) {
/* 108 */         addTicket(entry.getLongKey(), ticket);
/*     */       } }
/*     */     
/* 111 */     this.deactivatedTickets.clear();
/*     */   }
/*     */ 
/*     */   
/* 115 */   public void setLoadingChunkUpdatedListener(ChunkUpdated loadingChunkUpdatedListener) { this.loadingChunkUpdatedListener = loadingChunkUpdatedListener; }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public void setSimulationChunkUpdatedListener(ChunkUpdated simulationChunkUpdatedListener) { this.simulationChunkUpdatedListener = simulationChunkUpdatedListener; }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public boolean hasTickets() { return !this.tickets.isEmpty(); }
/*     */ 
/*     */   
/*     */   public boolean shouldKeepDimensionActive() {
/* 127 */     for (ObjectIterator objectIterator = this.tickets.values().iterator(); objectIterator.hasNext(); ) { List<Ticket> group = (List)objectIterator.next();
/* 128 */       for (Ticket ticket : group) {
/* 129 */         if (ticket.getType().shouldKeepDimensionActive()) {
/* 130 */           return true;
/*     */         }
/*     */       }  }
/*     */     
/* 134 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 138 */   public List<Ticket> getTickets(long key) { return (List)this.tickets.getOrDefault(key, List.of()); }
/*     */ 
/*     */ 
/*     */   
/* 142 */   private List<Ticket> getOrCreateTickets(long key) { return (List)this.tickets.computeIfAbsent(key, k -> new ObjectArrayList(4)); }
/*     */ 
/*     */   
/*     */   public void addTicketWithRadius(TicketType type, ChunkPos chunkPos, int radius) {
/* 146 */     Ticket ticket = new Ticket(type, ChunkLevel.byStatus(FullChunkStatus.FULL) - radius);
/* 147 */     addTicket(chunkPos.toLong(), ticket);
/*     */   }
/*     */ 
/*     */   
/* 151 */   public void addTicket(Ticket ticket, ChunkPos chunkPos) { addTicket(chunkPos.toLong(), ticket); }
/*     */ 
/*     */   
/*     */   public boolean addTicket(long key, Ticket ticket) {
/* 155 */     List<Ticket> tickets = getOrCreateTickets(key);
/* 156 */     for (Ticket t : tickets) {
/* 157 */       if (isTicketSameTypeAndLevel(ticket, t)) {
/* 158 */         t.resetTicksLeft();
/* 159 */         setDirty();
/* 160 */         return false;
/*     */       } 
/*     */     } 
/*     */     
/* 164 */     int oldSimulationTicketLevel = getTicketLevelAt(tickets, true);
/* 165 */     int oldLoadingTicketLevel = getTicketLevelAt(tickets, false);
/* 166 */     tickets.add(ticket);
/*     */     
/* 168 */     if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/* 169 */       LOGGER.debug("ATI {} {}", new ChunkPos(key), ticket);
/*     */     }
/* 171 */     if (ticket.getType().doesSimulate() && 
/* 172 */       ticket.getTicketLevel() < oldSimulationTicketLevel && this.simulationChunkUpdatedListener != null) {
/* 173 */       this.simulationChunkUpdatedListener.update(key, ticket.getTicketLevel(), true);
/*     */     }
/*     */     
/* 176 */     if (ticket.getType().doesLoad() && 
/* 177 */       ticket.getTicketLevel() < oldLoadingTicketLevel && this.loadingChunkUpdatedListener != null) {
/* 178 */       this.loadingChunkUpdatedListener.update(key, ticket.getTicketLevel(), true);
/*     */     }
/*     */     
/* 181 */     if (ticket.getType().equals(TicketType.FORCED)) {
/* 182 */       this.chunksWithForcedTickets.add(key);
/*     */     }
/* 184 */     setDirty();
/* 185 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 189 */   private static boolean isTicketSameTypeAndLevel(Ticket ticket, Ticket t) { return (t.getType() == ticket.getType() && t.getTicketLevel() == ticket.getTicketLevel()); }
/*     */ 
/*     */ 
/*     */   
/* 193 */   public int getTicketLevelAt(long key, boolean simulation) { return getTicketLevelAt(getTickets(key), simulation); }
/*     */ 
/*     */   
/*     */   private static int getTicketLevelAt(List<Ticket> tickets, boolean simulation) {
/* 197 */     Ticket lowestTicket = getLowestTicket(tickets, simulation);
/* 198 */     return (lowestTicket == null) ? (ChunkLevel.MAX_LEVEL + 1) : lowestTicket.getTicketLevel();
/*     */   }
/*     */   
/*     */   private static Ticket getLowestTicket(List<Ticket> tickets, boolean simulation) {
/* 202 */     if (tickets == null) {
/* 203 */       return null;
/*     */     }
/* 205 */     Ticket t = null;
/* 206 */     for (Ticket ticket : tickets) {
/* 207 */       if (t == null || ticket.getTicketLevel() < t.getTicketLevel()) {
/* 208 */         if (simulation && ticket.getType().doesSimulate()) {
/* 209 */           t = ticket; continue;
/* 210 */         }  if (!simulation && ticket.getType().doesLoad()) {
/* 211 */           t = ticket;
/*     */         }
/*     */       } 
/*     */     } 
/* 215 */     return t;
/*     */   }
/*     */   
/*     */   public void removeTicketWithRadius(TicketType type, ChunkPos chunkPos, int radius) {
/* 219 */     Ticket ticket = new Ticket(type, ChunkLevel.byStatus(FullChunkStatus.FULL) - radius);
/* 220 */     removeTicket(chunkPos.toLong(), ticket);
/*     */   }
/*     */ 
/*     */   
/* 224 */   public void removeTicket(Ticket ticket, ChunkPos chunkPos) { removeTicket(chunkPos.toLong(), ticket); }
/*     */ 
/*     */   
/*     */   public boolean removeTicket(long key, Ticket ticket) {
/* 228 */     List<Ticket> tickets = (List)this.tickets.get(key);
/* 229 */     if (tickets == null) {
/* 230 */       return false;
/*     */     }
/*     */     
/* 233 */     boolean found = false;
/* 234 */     for (Iterator<Ticket> iterator = tickets.iterator(); iterator.hasNext(); ) {
/* 235 */       Ticket t = (Ticket)iterator.next();
/* 236 */       if (isTicketSameTypeAndLevel(ticket, t)) {
/* 237 */         iterator.remove();
/* 238 */         if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/* 239 */           LOGGER.debug("RTI {} {}", new ChunkPos(key), t);
/*     */         }
/* 241 */         found = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 246 */     if (!found) {
/* 247 */       return false;
/*     */     }
/*     */     
/* 250 */     if (tickets.isEmpty()) {
/* 251 */       this.tickets.remove(key);
/*     */     }
/*     */     
/* 254 */     if (ticket.getType().doesSimulate() && this.simulationChunkUpdatedListener != null) {
/* 255 */       this.simulationChunkUpdatedListener.update(key, getTicketLevelAt(tickets, true), false);
/*     */     }
/* 257 */     if (ticket.getType().doesLoad() && this.loadingChunkUpdatedListener != null) {
/* 258 */       this.loadingChunkUpdatedListener.update(key, getTicketLevelAt(tickets, false), false);
/*     */     }
/* 260 */     if (ticket.getType().equals(TicketType.FORCED)) {
/* 261 */       updateForcedChunks();
/*     */     }
/* 263 */     setDirty();
/* 264 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 268 */   private void updateForcedChunks() { this.chunksWithForcedTickets = getAllChunksWithTicketThat(t -> t.getType().equals(TicketType.FORCED)); }
/*     */ 
/*     */   
/*     */   public String getTicketDebugString(long key, boolean simulation) {
/* 272 */     List<Ticket> tickets = getTickets(key);
/* 273 */     Ticket lowestTicket = getLowestTicket(tickets, simulation);
/* 274 */     return (lowestTicket == null) ? "no_ticket" : lowestTicket.toString();
/*     */   }
/*     */   
/*     */   public void purgeStaleTickets(ChunkMap chunkMap) {
/* 278 */     removeTicketIf((ticket, chunkPos) -> {
/* 279 */           if (canTicketExpire(chunkMap, ticket, chunkPos)) {
/* 280 */             ticket.decreaseTicksLeft();
/* 281 */             return ticket.isTimedOut();
/*     */           } 
/* 283 */           return false;
/*     */         }null);
/* 285 */     setDirty();
/*     */   }
/*     */   
/*     */   private boolean canTicketExpire(ChunkMap chunkMap, Ticket ticket, long chunkPos) {
/* 289 */     if (!ticket.getType().hasTimeout()) {
/* 290 */       return false;
/*     */     }
/* 292 */     if (ticket.getType().canExpireIfUnloaded()) {
/* 293 */       return true;
/*     */     }
/* 295 */     ChunkHolder updatingChunk = chunkMap.getUpdatingChunkIfPresent(chunkPos);
/*     */ 
/*     */ 
/*     */     
/* 299 */     return (updatingChunk == null || updatingChunk.isReadyForSaving());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 304 */   public void deactivateTicketsOnClosing() { removeTicketIf((ticket, chunkPos) -> (ticket.getType() != TicketType.UNKNOWN), this.deactivatedTickets); }
/*     */ 
/*     */   
/*     */   public void removeTicketIf(TicketPredicate predicate, Long2ObjectOpenHashMap<List<Ticket>> removedTickets) {
/* 308 */     ObjectIterator<Long2ObjectMap.Entry<List<Ticket>>> ticketsPerChunkIterator = this.tickets.long2ObjectEntrySet().fastIterator();
/* 309 */     boolean removedForced = false;
/* 310 */     while (ticketsPerChunkIterator.hasNext()) {
/* 311 */       Long2ObjectMap.Entry<List<Ticket>> entry = (Long2ObjectMap.Entry)ticketsPerChunkIterator.next();
/* 312 */       Iterator<Ticket> chunkTicketsIterator = ((List)entry.getValue()).iterator();
/* 313 */       long chunkPos = entry.getLongKey();
/* 314 */       boolean removedSimulation = false;
/* 315 */       boolean removedLoading = false;
/* 316 */       while (chunkTicketsIterator.hasNext()) {
/* 317 */         Ticket ticket = (Ticket)chunkTicketsIterator.next();
/* 318 */         if (predicate.test(ticket, chunkPos)) {
/* 319 */           if (removedTickets != null) {
/* 320 */             List<Ticket> tickets = (List)removedTickets.computeIfAbsent(chunkPos, k -> new ObjectArrayList(((List)entry.getValue()).size()));
/* 321 */             tickets.add(ticket);
/*     */           } 
/* 323 */           chunkTicketsIterator.remove();
/* 324 */           if (ticket.getType().doesLoad()) {
/* 325 */             removedLoading = true;
/*     */           }
/* 327 */           if (ticket.getType().doesSimulate()) {
/* 328 */             removedSimulation = true;
/*     */           }
/* 330 */           if (ticket.getType().equals(TicketType.FORCED)) {
/* 331 */             removedForced = true;
/*     */           }
/*     */         } 
/*     */       } 
/* 335 */       if (!removedLoading && !removedSimulation) {
/*     */         continue;
/*     */       }
/* 338 */       if (removedLoading && this.loadingChunkUpdatedListener != null) {
/* 339 */         this.loadingChunkUpdatedListener.update(chunkPos, getTicketLevelAt((List)entry.getValue(), false), false);
/*     */       }
/* 341 */       if (removedSimulation && this.simulationChunkUpdatedListener != null) {
/* 342 */         this.simulationChunkUpdatedListener.update(chunkPos, getTicketLevelAt((List)entry.getValue(), true), false);
/*     */       }
/* 344 */       setDirty();
/* 345 */       if (((List)entry.getValue()).isEmpty()) {
/* 346 */         ticketsPerChunkIterator.remove();
/*     */       }
/*     */     } 
/* 349 */     if (removedForced) {
/* 350 */       updateForcedChunks();
/*     */     }
/*     */   }
/*     */   
/*     */   public void replaceTicketLevelOfType(int newLevel, TicketType ticketType) {
/* 355 */     List<Pair<Ticket, Long>> affectedTickets = new ArrayList<Pair<Ticket, Long>>();
/* 356 */     for (ObjectIterator objectIterator = this.tickets.long2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) { Long2ObjectMap.Entry<List<Ticket>> entry = (Long2ObjectMap.Entry)objectIterator.next();
/* 357 */       for (Ticket ticket : (List)entry.getValue()) {
/* 358 */         if (ticket.getType() == ticketType) {
/* 359 */           affectedTickets.add(Pair.of(ticket, Long.valueOf(entry.getLongKey())));
/*     */         }
/*     */       }  }
/*     */     
/* 363 */     for (Pair<Ticket, Long> pair : affectedTickets) {
/* 364 */       Long key = (Long)pair.getSecond();
/* 365 */       Ticket ticket = (Ticket)pair.getFirst();
/* 366 */       removeTicket(key.longValue(), ticket);
/* 367 */       TicketType type = ticket.getType();
/* 368 */       addTicket(key.longValue(), new Ticket(type, newLevel));
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean updateChunkForced(ChunkPos chunkPos, boolean forced) {
/* 373 */     Ticket ticket = new Ticket(TicketType.FORCED, ChunkMap.FORCED_TICKET_LEVEL);
/* 374 */     if (forced) {
/* 375 */       return addTicket(chunkPos.toLong(), ticket);
/*     */     }
/* 377 */     return removeTicket(chunkPos.toLong(), ticket);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 383 */   public LongSet getForceLoadedChunks() { return this.chunksWithForcedTickets; }
/*     */ 
/*     */   
/*     */   private LongSet getAllChunksWithTicketThat(Predicate<Ticket> ticketCheck) {
/* 387 */     LongOpenHashSet chunks = new LongOpenHashSet();
/* 388 */     for (ObjectIterator objectIterator = Long2ObjectMaps.fastIterable(this.tickets).iterator(); objectIterator.hasNext(); ) { Long2ObjectMap.Entry<List<Ticket>> entry = (Long2ObjectMap.Entry)objectIterator.next();
/* 389 */       for (Ticket ticket : (List)entry.getValue()) {
/* 390 */         if (ticketCheck.test(ticket)) {
/* 391 */           chunks.add(entry.getLongKey());
/*     */         }
/*     */       }  }
/*     */ 
/*     */     
/* 396 */     return chunks;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ChunkUpdated {
/*     */     void update(long param1Long, int param1Int, boolean param1Boolean);
/*     */   }
/*     */   
/*     */   public static interface TicketPredicate {
/*     */     boolean test(Ticket param1Ticket, long param1Long);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\TicketStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
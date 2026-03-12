/*     */ package net.minecraft.world.ticks;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2LongMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2LongMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.LongSummaryStatistics;
/*     */ import java.util.Objects;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.LongPredicate;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ 
/*     */ public class LevelTicks<T> extends Object implements LevelTickAccess<T> {
/*  32 */   private static final Comparator<LevelChunkTicks<?>> CONTAINER_DRAIN_ORDER = (o1, o2) -> ScheduledTick.INTRA_TICK_DRAIN_ORDER.compare(o1.peek(), o2.peek()); private final LongPredicate tickCheck; private final Long2ObjectMap<LevelChunkTicks<T>> allContainers; private final Long2LongMap nextTickForContainer;
/*     */   private final Queue<LevelChunkTicks<T>> containersToTick;
/*     */   
/*     */   public LevelTicks(LongPredicate tickCheck) {
/*  36 */     this.allContainers = new Long2ObjectOpenHashMap();
/*  37 */     this.nextTickForContainer = (Long2LongMap)Util.make(new Long2LongOpenHashMap(), m -> m.defaultReturnValue(Float.MAX_VALUE));
/*     */     
/*  39 */     this.containersToTick = new PriorityQueue(CONTAINER_DRAIN_ORDER);
/*  40 */     this.toRunThisTick = new ArrayDeque();
/*  41 */     this.alreadyRunThisTick = new ArrayList();
/*     */ 
/*     */     
/*  44 */     this.toRunThisTickSet = new ObjectOpenCustomHashSet(ScheduledTick.UNIQUE_TICK_HASH);
/*     */     
/*  46 */     this.chunkScheduleUpdater = ((container, newTick) -> {
/*  47 */         if (newTick.equals(container.peek()))
/*     */         {
/*  49 */           updateContainerScheduling(newTick);
/*     */         }
/*     */       });
/*     */ 
/*     */     
/*  54 */     this.tickCheck = tickCheck;
/*     */   }
/*     */   private final Queue<ScheduledTick<T>> toRunThisTick; private final List<ScheduledTick<T>> alreadyRunThisTick; private final Set<ScheduledTick<?>> toRunThisTickSet; private final BiConsumer<LevelChunkTicks<T>, ScheduledTick<T>> chunkScheduleUpdater;
/*     */   
/*     */   public void addContainer(ChunkPos pos, LevelChunkTicks<T> container) {
/*  59 */     long posKey = pos.toLong();
/*  60 */     this.allContainers.put(posKey, container);
/*  61 */     ScheduledTick<T> nextTick = container.peek();
/*  62 */     if (nextTick != null) {
/*  63 */       this.nextTickForContainer.put(posKey, nextTick.triggerTick());
/*     */     }
/*     */     
/*  66 */     container.setOnTickAdded(this.chunkScheduleUpdater);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeContainer(ChunkPos pos) {
/*  71 */     long chunkKey = pos.toLong();
/*  72 */     LevelChunkTicks<T> removedContainer = (LevelChunkTicks)this.allContainers.remove(chunkKey);
/*  73 */     this.nextTickForContainer.remove(chunkKey);
/*  74 */     if (removedContainer != null) {
/*  75 */       removedContainer.setOnTickAdded(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void schedule(ScheduledTick<T> tick) {
/*  81 */     long chunkKey = ChunkPos.asLong(tick.pos());
/*  82 */     LevelChunkTicks<T> tickContainer = (LevelChunkTicks)this.allContainers.get(chunkKey);
/*  83 */     if (tickContainer == null) {
/*  84 */       Util.logAndPauseIfInIde("Trying to schedule tick in not loaded position " + String.valueOf(tick.pos()));
/*     */       return;
/*     */     } 
/*  87 */     tickContainer.schedule(tick);
/*     */   }
/*     */   
/*     */   public void tick(long currentTick, int maxTicksToProcess, BiConsumer<BlockPos, T> output) {
/*  91 */     ProfilerFiller profiler = Profiler.get();
/*  92 */     profiler.push("collect");
/*  93 */     collectTicks(currentTick, maxTicksToProcess, profiler);
/*  94 */     profiler.popPush("run");
/*  95 */     profiler.incrementCounter("ticksToRun", this.toRunThisTick.size());
/*  96 */     runCollectedTicks(output);
/*  97 */     profiler.popPush("cleanup");
/*  98 */     cleanupAfterTick();
/*  99 */     profiler.pop();
/*     */   }
/*     */   
/*     */   private void collectTicks(long currentTick, int maxTicksToProcess, ProfilerFiller profiler) {
/* 103 */     sortContainersToTick(currentTick);
/* 104 */     profiler.incrementCounter("containersToTick", this.containersToTick.size());
/* 105 */     drainContainers(currentTick, maxTicksToProcess);
/* 106 */     rescheduleLeftoverContainers();
/*     */   }
/*     */   
/*     */   private void sortContainersToTick(long currentTick) {
/* 110 */     ObjectIterator<Long2LongMap.Entry> it = Long2LongMaps.fastIterator(this.nextTickForContainer);
/* 111 */     while (it.hasNext()) {
/* 112 */       Long2LongMap.Entry entry = (Long2LongMap.Entry)it.next();
/* 113 */       long chunkPos = entry.getLongKey();
/* 114 */       long nextTick = entry.getLongValue();
/* 115 */       if (nextTick <= currentTick) {
/* 116 */         LevelChunkTicks<T> candidateContainer = (LevelChunkTicks)this.allContainers.get(chunkPos);
/* 117 */         if (candidateContainer == null) {
/*     */           
/* 119 */           it.remove(); continue;
/*     */         } 
/* 121 */         ScheduledTick<T> scheduledTick = candidateContainer.peek();
/* 122 */         if (scheduledTick == null) {
/*     */           
/* 124 */           it.remove(); continue;
/* 125 */         }  if (scheduledTick.triggerTick() > currentTick) {
/*     */           
/* 127 */           entry.setValue(scheduledTick.triggerTick()); continue;
/* 128 */         }  if (this.tickCheck.test(chunkPos)) {
/*     */           
/* 130 */           it.remove();
/* 131 */           this.containersToTick.add(candidateContainer);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void drainContainers(long currentTick, int maxTicksToProcess) {
/*     */     LevelChunkTicks<T> topContainer;
/* 141 */     while (canScheduleMoreTicks(maxTicksToProcess) && (topContainer = (LevelChunkTicks)this.containersToTick.poll()) != null) {
/* 142 */       ScheduledTick<T> tick = topContainer.poll();
/*     */       
/* 144 */       scheduleForThisTick(tick);
/*     */ 
/*     */       
/* 147 */       drainFromCurrentContainer(this.containersToTick, topContainer, currentTick, maxTicksToProcess);
/* 148 */       ScheduledTick<T> nextTick = topContainer.peek();
/* 149 */       if (nextTick != null) {
/* 150 */         if (nextTick.triggerTick() <= currentTick && canScheduleMoreTicks(maxTicksToProcess)) {
/*     */           
/* 152 */           this.containersToTick.add(topContainer);
/*     */           continue;
/*     */         } 
/* 155 */         updateContainerScheduling(nextTick);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void rescheduleLeftoverContainers() {
/* 163 */     for (LevelChunkTicks<T> container : this.containersToTick)
/*     */     {
/* 165 */       updateContainerScheduling(container.peek());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 170 */   private void updateContainerScheduling(ScheduledTick<T> nextTick) { this.nextTickForContainer.put(ChunkPos.asLong(nextTick.pos()), nextTick.triggerTick()); }
/*     */ 
/*     */   
/*     */   private void drainFromCurrentContainer(Queue<LevelChunkTicks<T>> containersToTick, LevelChunkTicks<T> currentContainer, long currentTick, int maxTicksToProcess) {
/* 174 */     if (!canScheduleMoreTicks(maxTicksToProcess)) {
/*     */       return;
/*     */     }
/*     */     
/* 178 */     LevelChunkTicks<T> nextBestContainer = (LevelChunkTicks)containersToTick.peek();
/* 179 */     ScheduledTick<T> nextFromNextContainer = (nextBestContainer != null) ? nextBestContainer.peek() : null;
/*     */     
/* 181 */     while (canScheduleMoreTicks(maxTicksToProcess)) {
/* 182 */       ScheduledTick<T> nextFromCurrentContainer = currentContainer.peek();
/* 183 */       if (nextFromCurrentContainer == null || nextFromCurrentContainer.triggerTick() > currentTick) {
/*     */         break;
/*     */       }
/*     */       
/* 187 */       if (nextFromNextContainer != null && ScheduledTick.INTRA_TICK_DRAIN_ORDER.compare(nextFromCurrentContainer, nextFromNextContainer) > 0) {
/*     */         break;
/*     */       }
/*     */       
/* 191 */       currentContainer.poll();
/* 192 */       scheduleForThisTick(nextFromCurrentContainer);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 197 */   private void scheduleForThisTick(ScheduledTick<T> tick) { this.toRunThisTick.add(tick); }
/*     */ 
/*     */ 
/*     */   
/* 201 */   private boolean canScheduleMoreTicks(int maxTicksToProcess) { return (this.toRunThisTick.size() < maxTicksToProcess); }
/*     */ 
/*     */   
/*     */   private void runCollectedTicks(BiConsumer<BlockPos, T> output) {
/* 205 */     while (!this.toRunThisTick.isEmpty()) {
/*     */ 
/*     */       
/* 208 */       ScheduledTick<T> entry = (ScheduledTick)this.toRunThisTick.poll();
/* 209 */       if (!this.toRunThisTickSet.isEmpty()) {
/* 210 */         this.toRunThisTickSet.remove(entry);
/*     */       }
/* 212 */       this.alreadyRunThisTick.add(entry);
/* 213 */       output.accept(entry.pos(), entry.type());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cleanupAfterTick() {
/* 218 */     this.toRunThisTick.clear();
/* 219 */     this.containersToTick.clear();
/* 220 */     this.alreadyRunThisTick.clear();
/* 221 */     this.toRunThisTickSet.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasScheduledTick(BlockPos pos, T block) {
/* 228 */     LevelChunkTicks<T> tickContainer = (LevelChunkTicks)this.allContainers.get(ChunkPos.asLong(pos));
/* 229 */     return (tickContainer != null && tickContainer.hasScheduledTick(pos, block));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean willTickThisTick(BlockPos pos, T type) {
/* 235 */     calculateTickSetIfNeeded();
/* 236 */     return this.toRunThisTickSet.contains(ScheduledTick.probe(type, pos));
/*     */   }
/*     */   
/*     */   private void calculateTickSetIfNeeded() {
/* 240 */     if (this.toRunThisTickSet.isEmpty() && !this.toRunThisTick.isEmpty()) {
/* 241 */       this.toRunThisTickSet.addAll(this.toRunThisTick);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void forContainersInArea(BoundingBox bb, PosAndContainerConsumer<T> ouput) {
/* 251 */     int xMin = SectionPos.posToSectionCoord(bb.minX());
/* 252 */     int zMin = SectionPos.posToSectionCoord(bb.minZ());
/*     */     
/* 254 */     int xMax = SectionPos.posToSectionCoord(bb.maxX());
/* 255 */     int zMax = SectionPos.posToSectionCoord(bb.maxZ());
/*     */     
/* 257 */     for (int x = xMin; x <= xMax; x++) {
/* 258 */       for (int z = zMin; z <= zMax; z++) {
/* 259 */         long containerPos = ChunkPos.asLong(x, z);
/* 260 */         LevelChunkTicks<T> container = (LevelChunkTicks)this.allContainers.get(containerPos);
/* 261 */         if (container != null) {
/* 262 */           ouput.accept(containerPos, container);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clearArea(BoundingBox area) {
/* 269 */     Predicate<ScheduledTick<T>> tickInsideBB = t -> area.isInside(t.pos());
/* 270 */     forContainersInArea(area, (pos, container) -> {
/* 271 */           ScheduledTick<T> previousTop = container.peek();
/* 272 */           container.removeIf(tickInsideBB);
/* 273 */           ScheduledTick<T> newTop = container.peek();
/* 274 */           if (newTop != previousTop) {
/* 275 */             if (newTop != null) {
/* 276 */               updateContainerScheduling(newTop);
/*     */             } else {
/* 278 */               this.nextTickForContainer.remove(pos);
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 283 */     this.alreadyRunThisTick.removeIf(tickInsideBB);
/* 284 */     this.toRunThisTick.removeIf(tickInsideBB);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 295 */   public void copyArea(BoundingBox area, Vec3i offset) { copyAreaFrom(this, area, offset); }
/*     */ 
/*     */   
/*     */   public void copyAreaFrom(LevelTicks<T> source, BoundingBox area, Vec3i offset) {
/* 299 */     List<ScheduledTick<T>> ticksToAdd = new ArrayList<ScheduledTick<T>>();
/*     */     
/* 301 */     Predicate<ScheduledTick<T>> tickInsideBB = t -> area.isInside(t.pos());
/*     */     
/* 303 */     Objects.requireNonNull(ticksToAdd); source.alreadyRunThisTick.stream().filter(tickInsideBB).forEach(ticksToAdd::add);
/* 304 */     Objects.requireNonNull(ticksToAdd); source.toRunThisTick.stream().filter(tickInsideBB).forEach(ticksToAdd::add);
/*     */     
/* 306 */     source.forContainersInArea(area, (pos, container) -> { Objects.requireNonNull(ticksToAdd); container.getAll().filter(tickInsideBB).forEach(ticksToAdd::add);
/*     */         });
/* 308 */     LongSummaryStatistics info = ticksToAdd.stream().mapToLong(ScheduledTick::subTickOrder).summaryStatistics();
/* 309 */     long minSubTick = info.getMin();
/* 310 */     long maxSubTick = info.getMax();
/*     */     
/* 312 */     ticksToAdd.forEach(tick -> schedule(new ScheduledTick(tick.type(), tick.pos().offset(offset), tick.triggerTick(), tick.priority(), tick.subTickOrder() - minSubTick + maxSubTick + 1L)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 317 */   public int count() { return this.allContainers.values().stream().mapToInt(TickAccess::count).sum(); }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface PosAndContainerConsumer<T> {
/*     */     void accept(long param1Long, LevelChunkTicks<T> param1LevelChunkTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ticks\LevelTicks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
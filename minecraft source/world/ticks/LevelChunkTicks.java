/*     */ package net.minecraft.world.ticks;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ 
/*     */ public class LevelChunkTicks<T>
/*     */   extends Object
/*     */   implements TickContainerAccess<T>, SerializableTickContainer<T> {
/*  18 */   private final Queue<ScheduledTick<T>> tickQueue = new PriorityQueue(ScheduledTick.DRAIN_ORDER);
/*     */ 
/*     */   
/*     */   private List<SavedTick<T>> pendingTicks;
/*     */ 
/*     */   
/*  24 */   private final Set<ScheduledTick<?>> ticksPerPosition = new ObjectOpenCustomHashSet(ScheduledTick.UNIQUE_TICK_HASH);
/*     */   
/*     */   private BiConsumer<LevelChunkTicks<T>, ScheduledTick<T>> onTickAdded;
/*     */ 
/*     */   
/*     */   public LevelChunkTicks() {}
/*     */   
/*     */   public LevelChunkTicks(List<SavedTick<T>> pendingTicks) {
/*  32 */     this.pendingTicks = pendingTicks;
/*  33 */     for (SavedTick<T> pendingTick : pendingTicks) {
/*  34 */       this.ticksPerPosition.add(ScheduledTick.probe(pendingTick.type(), pendingTick.pos()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  39 */   public void setOnTickAdded(BiConsumer<LevelChunkTicks<T>, ScheduledTick<T>> onTickAdded) { this.onTickAdded = onTickAdded; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public ScheduledTick<T> peek() { return (ScheduledTick)this.tickQueue.peek(); }
/*     */ 
/*     */   
/*     */   public ScheduledTick<T> poll() {
/*  50 */     ScheduledTick<T> result = (ScheduledTick)this.tickQueue.poll();
/*  51 */     if (result != null) {
/*  52 */       this.ticksPerPosition.remove(result);
/*     */     }
/*  54 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void schedule(ScheduledTick<T> tick) {
/*  59 */     if (this.ticksPerPosition.add(tick)) {
/*  60 */       scheduleUnchecked(tick);
/*     */     }
/*     */   }
/*     */   
/*     */   private void scheduleUnchecked(ScheduledTick<T> tick) {
/*  65 */     this.tickQueue.add(tick);
/*     */     
/*  67 */     if (this.onTickAdded != null) {
/*  68 */       this.onTickAdded.accept(this, tick);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public boolean hasScheduledTick(BlockPos pos, T type) { return this.ticksPerPosition.contains(ScheduledTick.probe(type, pos)); }
/*     */ 
/*     */   
/*     */   public void removeIf(Predicate<ScheduledTick<T>> test) {
/*  79 */     for (Iterator<ScheduledTick<T>> iterator = this.tickQueue.iterator(); iterator.hasNext(); ) {
/*  80 */       ScheduledTick<T> tick = (ScheduledTick)iterator.next();
/*  81 */       if (test.test(tick)) {
/*  82 */         iterator.remove();
/*  83 */         this.ticksPerPosition.remove(tick);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  89 */   public Stream<ScheduledTick<T>> getAll() { return this.tickQueue.stream(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public int count() { return this.tickQueue.size() + ((this.pendingTicks != null) ? this.pendingTicks.size() : 0); }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<SavedTick<T>> pack(long currentTick) {
/*  99 */     List<SavedTick<T>> ticks = new ArrayList<SavedTick<T>>(this.tickQueue.size());
/* 100 */     if (this.pendingTicks != null) {
/* 101 */       ticks.addAll(this.pendingTicks);
/*     */     }
/* 103 */     for (ScheduledTick<T> tick : this.tickQueue) {
/* 104 */       ticks.add(tick.toSavedTick(currentTick));
/*     */     }
/* 106 */     return ticks;
/*     */   }
/*     */   
/*     */   public void unpack(long currentTick) {
/* 110 */     if (this.pendingTicks != null) {
/* 111 */       int subTickBase = -this.pendingTicks.size();
/* 112 */       for (SavedTick<T> pendingTick : this.pendingTicks)
/*     */       {
/* 114 */         scheduleUnchecked(pendingTick.unpack(currentTick, subTickBase++));
/*     */       }
/*     */     } 
/*     */     
/* 118 */     this.pendingTicks = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ticks\LevelChunkTicks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
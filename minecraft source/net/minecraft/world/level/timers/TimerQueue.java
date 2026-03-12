/*     */ package net.minecraft.world.level.timers;
/*     */ 
/*     */ import com.google.common.collect.HashBasedTable;
/*     */ import com.google.common.collect.Table;
/*     */ import com.google.common.primitives.UnsignedLong;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TimerQueue<T> extends Object {
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final String CALLBACK_DATA_TAG = "Callback";
/*     */   private static final String TIMER_NAME_TAG = "Name";
/*     */   private static final String TIMER_TRIGGER_TIME_TAG = "TriggerTime";
/*     */   private final TimerCallbacks<T> callbacksRegistry;
/*     */   private final Queue<Event<T>> queue;
/*     */   private UnsignedLong sequentialId;
/*     */   private final Table<String, Long, Event<T>> events;
/*     */   
/*     */   public static class Event<T> extends Object { public final long triggerTime;
/*     */     public final UnsignedLong sequentialId;
/*     */     
/*     */     private Event(long triggerTime, UnsignedLong sequentialId, String id, TimerCallback<T> callback) {
/*  35 */       this.triggerTime = triggerTime;
/*  36 */       this.sequentialId = sequentialId;
/*  37 */       this.id = id;
/*  38 */       this.callback = callback;
/*     */     }
/*     */     public final String id;
/*     */     public final TimerCallback<T> callback; }
/*     */   
/*  43 */   private static <T> Comparator<Event<T>> createComparator() { return Comparator.comparingLong(l -> l.triggerTime).thenComparing(l -> l.sequentialId); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimerQueue(TimerCallbacks<T> callbacksRegistry, Stream<? extends Dynamic<?>> eventData)
/*     */   {
/*  55 */     this(callbacksRegistry);
/*  56 */     this.queue.clear();
/*  57 */     this.events.clear();
/*  58 */     this.sequentialId = UnsignedLong.ZERO;
/*     */     
/*  60 */     eventData.forEach(input -> {
/*  61 */           Tag tag = (Tag)input.convert(NbtOps.INSTANCE).getValue();
/*  62 */           if (tag instanceof CompoundTag) { CompoundTag compoundTag = (CompoundTag)tag;
/*  63 */             loadEvent(compoundTag); }
/*     */           else
/*  65 */           { LOGGER.warn("Invalid format of events: {}", tag); }
/*     */         
/*     */         }); } public TimerQueue(TimerCallbacks<T> callbacksRegistry) {
/*     */     this.queue = new PriorityQueue(createComparator());
/*     */     this.sequentialId = UnsignedLong.ZERO;
/*     */     this.events = HashBasedTable.create();
/*  71 */     this.callbacksRegistry = callbacksRegistry;
/*     */   }
/*     */   
/*     */   public void tick(T context, long currentTick) {
/*     */     while (true) {
/*  76 */       Event<T> event = (Event)this.queue.peek();
/*  77 */       if (event == null || event.triggerTime > currentTick) {
/*     */         break;
/*     */       }
/*     */       
/*  81 */       this.queue.remove();
/*  82 */       this.events.remove(event.id, Long.valueOf(currentTick));
/*     */       
/*  84 */       event.callback.handle(context, this, currentTick);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void schedule(String id, long time, TimerCallback<T> callback) {
/*  89 */     if (this.events.contains(id, Long.valueOf(time))) {
/*     */       return;
/*     */     }
/*  92 */     this.sequentialId = this.sequentialId.plus(UnsignedLong.ONE);
/*  93 */     Event<T> newEvent = new Event<T>(time, this.sequentialId, id, callback);
/*  94 */     this.events.put(id, Long.valueOf(time), newEvent);
/*  95 */     this.queue.add(newEvent);
/*     */   }
/*     */   
/*     */   public int remove(String id) {
/*  99 */     Collection<Event<T>> eventsToRemove = this.events.row(id).values();
/* 100 */     Objects.requireNonNull(this.queue); eventsToRemove.forEach(this.queue::remove);
/* 101 */     int size = eventsToRemove.size();
/* 102 */     eventsToRemove.clear();
/* 103 */     return size;
/*     */   }
/*     */ 
/*     */   
/* 107 */   public Set<String> getEventsIds() { return Collections.unmodifiableSet(this.events.rowKeySet()); }
/*     */ 
/*     */   
/*     */   private void loadEvent(CompoundTag tag) {
/* 111 */     TimerCallback<T> callback = (TimerCallback)tag.read("Callback", this.callbacksRegistry.codec()).orElse(null);
/* 112 */     if (callback != null) {
/* 113 */       String id = tag.getStringOr("Name", "");
/* 114 */       long time = tag.getLongOr("TriggerTime", 0L);
/* 115 */       schedule(id, time, callback);
/*     */     } 
/*     */   }
/*     */   
/*     */   private CompoundTag storeEvent(Event<T> event) {
/* 120 */     CompoundTag result = new CompoundTag();
/* 121 */     result.putString("Name", event.id);
/* 122 */     result.putLong("TriggerTime", event.triggerTime);
/* 123 */     result.store("Callback", this.callbacksRegistry.codec(), event.callback);
/* 124 */     return result;
/*     */   }
/*     */   
/*     */   public ListTag store() {
/* 128 */     ListTag result = new ListTag();
/* 129 */     Objects.requireNonNull(result); this.queue.stream().sorted(createComparator()).map(this::storeEvent).forEach(result::add);
/* 130 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\timers\TimerQueue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
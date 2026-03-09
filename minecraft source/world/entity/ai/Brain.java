/*     */ package net.minecraft.world.entity.ai;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeSystem;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*     */ import net.minecraft.world.entity.ai.memory.ExpirableValue;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Brain<E extends LivingEntity>
/*     */   extends Object
/*     */ {
/*  51 */   private static final Logger LOGGER = LogUtils.getLogger(); private final Supplier<Codec<Brain<E>>> codec; private static final int SCHEDULE_UPDATE_DELAY = 20; private final Map<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> memories; private final Map<SensorType<? extends Sensor<? super E>>, Sensor<? super E>> sensors; private final Map<Integer, Map<Activity, Set<BehaviorControl<? super E>>>> availableBehaviorsByPriority; private EnvironmentAttribute<Activity> schedule; private final Map<Activity, Set<Pair<MemoryModuleType<?>, MemoryStatus>>> activityRequirements; private final Map<Activity, Set<MemoryModuleType<?>>> activityMemoriesToEraseWhenStopped;
/*     */   private Set<Activity> coreActivities;
/*     */   private final Set<Activity> activeActivities;
/*     */   private Activity defaultActivity;
/*     */   private long lastScheduleUpdate;
/*     */   
/*     */   public static final class Provider<E extends LivingEntity> extends Object { private final Collection<? extends MemoryModuleType<?>> memoryTypes;
/*     */     
/*     */     private Provider(Collection<? extends MemoryModuleType<?>> memoryTypes, Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes) {
/*  60 */       this.memoryTypes = memoryTypes;
/*  61 */       this.sensorTypes = sensorTypes;
/*  62 */       this.codec = Brain.codec(memoryTypes, sensorTypes);
/*     */     }
/*     */     private final Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes; private final Codec<Brain<E>> codec;
/*     */     
/*  66 */     public Brain<E> makeBrain(Dynamic<?> input) { Objects.requireNonNull(Brain.LOGGER); return (Brain)this.codec.parse(input).resultOrPartial(Brain.LOGGER::error).orElseGet(() -> new Brain(this.memoryTypes, this.sensorTypes, ImmutableList.of(), ())); } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static <E extends LivingEntity> Provider<E> provider(Collection<? extends MemoryModuleType<?>> memoryTypes, Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes) { return new Provider(memoryTypes, sensorTypes); }
/*     */ 
/*     */   
/*     */   public static <E extends LivingEntity> Codec<Brain<E>> codec(final Collection<? extends MemoryModuleType<?>> memoryTypes, final Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes) {
/*  75 */     final MutableObject<Codec<Brain<E>>> codecReference = new MutableObject<Codec<Brain<E>>>();
/*     */     
/*  77 */     codecReference.setValue((new MapCodec<Brain<E>>()
/*     */         {
/*     */           public <T> Stream<T> keys(DynamicOps<T> ops) {
/*  80 */             return memoryTypes.stream()
/*  81 */               .flatMap(t -> t.getCodec().map(()).stream())
/*  82 */               .map(l -> ops.createString(l.toString()));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<Brain<E>> decode(DynamicOps<T> ops, MapLike<T> input) {
/*  87 */             MutableObject<DataResult<ImmutableList.Builder<Brain.MemoryValue<?>>>> result = new MutableObject<DataResult<ImmutableList.Builder<Brain.MemoryValue<?>>>>(DataResult.success(ImmutableList.builder()));
/*     */             
/*  89 */             input.entries().forEach(pair -> {
/*  90 */                   DataResult<MemoryModuleType<?>> typeResult = BuiltInRegistries.MEMORY_MODULE_TYPE.byNameCodec().parse(ops, pair.getFirst());
/*  91 */                   DataResult<? extends Brain.MemoryValue<?>> entryResult = typeResult.flatMap(());
/*  92 */                   result.setValue(((DataResult)result.get()).apply2(ImmutableList.Builder::add, entryResult));
/*     */                 });
/*     */             
/*  95 */             Objects.requireNonNull(Brain.LOGGER); ImmutableList<Brain.MemoryValue<?>> memories = (ImmutableList)((DataResult)result.get()).resultOrPartial(Brain.LOGGER::error).map(ImmutableList.Builder::build).orElseGet(ImmutableList::of);
/*  96 */             return DataResult.success(new Brain(memoryTypes, sensorTypes, memories, codecReference));
/*     */           }
/*     */ 
/*     */           
/* 100 */           private <T, U> DataResult<Brain.MemoryValue<U>> captureRead(MemoryModuleType<U> type, DynamicOps<T> ops, T input) { return ((DataResult)type.getCodec().map(DataResult::success).orElseGet(() -> DataResult.error(())))
/* 101 */               .flatMap(c -> c.parse(ops, input))
/* 102 */               .map(v -> new Brain.MemoryValue(type, Optional.of(v))); }
/*     */ 
/*     */ 
/*     */           
/*     */           public <T> RecordBuilder<T> encode(Brain<E> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 107 */             input.memories().forEach(m -> m.serialize(ops, prefix));
/* 108 */             return prefix;
/*     */           }
/* 110 */         }).fieldOf("memories").codec());
/*     */     
/* 112 */     return (Codec)codecReference.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public Brain(Collection<? extends MemoryModuleType<?>> memoryTypes, Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes, ImmutableList<MemoryValue<?>> memories, Supplier<Codec<Brain<E>>> codec) {
/* 117 */     this.memories = Maps.newHashMap();
/*     */ 
/*     */     
/* 120 */     this.sensors = Maps.newLinkedHashMap();
/*     */ 
/*     */     
/* 123 */     this.availableBehaviorsByPriority = Maps.newTreeMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     this.activityRequirements = Maps.newHashMap();
/*     */     
/* 131 */     this.activityMemoriesToEraseWhenStopped = Maps.newHashMap();
/*     */ 
/*     */     
/* 134 */     this.coreActivities = Sets.newHashSet();
/*     */ 
/*     */     
/* 137 */     this.activeActivities = Sets.newHashSet();
/*     */ 
/*     */     
/* 140 */     this.defaultActivity = Activity.IDLE;
/*     */     
/* 142 */     this.lastScheduleUpdate = -9999L;
/*     */ 
/*     */     
/* 145 */     this.codec = codec;
/* 146 */     for (MemoryModuleType<?> memoryType : memoryTypes) {
/* 147 */       this.memories.put(memoryType, Optional.empty());
/*     */     }
/* 149 */     for (SensorType<? extends Sensor<? super E>> sensorType : sensorTypes) {
/* 150 */       this.sensors.put(sensorType, sensorType.create());
/*     */     }
/*     */     
/* 153 */     for (Sensor<? super E> sensor : this.sensors.values()) {
/* 154 */       for (MemoryModuleType<?> type : sensor.requires()) {
/* 155 */         this.memories.put(type, Optional.empty());
/*     */       }
/*     */     } 
/*     */     
/* 159 */     for (UnmodifiableIterator unmodifiableIterator = memories.iterator(); unmodifiableIterator.hasNext(); ) { MemoryValue<?> memory = (MemoryValue)unmodifiableIterator.next();
/* 160 */       memory.setMemoryInternal(this); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/* 165 */   public <T> DataResult<T> serializeStart(DynamicOps<T> ops) { return ((Codec)this.codec.get()).encodeStart(ops, this); }
/*     */ 
/*     */   
/*     */   private static final class MemoryValue<U>
/*     */     extends Object
/*     */   {
/*     */     private final MemoryModuleType<U> type;
/*     */     private final Optional<? extends ExpirableValue<U>> value;
/*     */     
/* 174 */     private static <U> MemoryValue<U> createUnchecked(MemoryModuleType<U> type, Optional<? extends ExpirableValue<?>> value) { return new MemoryValue(type, value); }
/*     */ 
/*     */     
/*     */     private MemoryValue(MemoryModuleType<U> type, Optional<? extends ExpirableValue<U>> value) {
/* 178 */       this.type = type;
/* 179 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/* 183 */     private void setMemoryInternal(Brain<?> brain) { brain.setMemoryInternal(this.type, this.value); }
/*     */ 
/*     */     
/*     */     public <T> void serialize(DynamicOps<T> ops, RecordBuilder<T> builder) {
/* 187 */       this.type.getCodec().ifPresent(codec -> this.value.ifPresent(()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 192 */   private Stream<MemoryValue<?>> memories() { return this.memories.entrySet().stream().map(e -> MemoryValue.createUnchecked((MemoryModuleType)e.getKey(), (Optional)e.getValue())); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 199 */   public boolean hasMemoryValue(MemoryModuleType<?> type) { return checkMemory(type, MemoryStatus.VALUE_PRESENT); }
/*     */ 
/*     */   
/*     */   public void clearMemories() {
/* 203 */     this.memories.keySet().forEach(key -> 
/* 204 */         this.memories.put(key, Optional.empty()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 209 */   public <U> void eraseMemory(MemoryModuleType<U> type) { setMemory(type, Optional.empty()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   public <U> void setMemory(MemoryModuleType<U> type, U value) { setMemory(type, Optional.ofNullable(value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 226 */   public <U> void setMemoryWithExpiry(MemoryModuleType<U> type, U value, long timeToLive) { setMemoryInternal(type, Optional.of(ExpirableValue.of(value, timeToLive))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 234 */   public <U> void setMemory(MemoryModuleType<U> type, Optional<? extends U> optionalValue) { setMemoryInternal(type, optionalValue.map(ExpirableValue::of)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <U> void setMemoryInternal(MemoryModuleType<U> type, Optional<? extends ExpirableValue<?>> optionalExpirableValue) {
/* 243 */     if (this.memories.containsKey(type)) {
/* 244 */       if (optionalExpirableValue.isPresent() && isEmptyCollection(((ExpirableValue)optionalExpirableValue.get()).getValue())) {
/* 245 */         eraseMemory(type);
/*     */       } else {
/* 247 */         this.memories.put(type, optionalExpirableValue);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public <U> Optional<U> getMemory(MemoryModuleType<U> type) {
/* 254 */     Optional<? extends ExpirableValue<?>> expirableValue = (Optional)this.memories.get(type);
/* 255 */     if (expirableValue == null) {
/* 256 */       throw new IllegalStateException("Unregistered memory fetched: " + String.valueOf(type));
/*     */     }
/* 258 */     return expirableValue.map(ExpirableValue::getValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public <U> Optional<U> getMemoryInternal(MemoryModuleType<U> type) {
/* 263 */     Optional<? extends ExpirableValue<?>> expirableValue = (Optional)this.memories.get(type);
/* 264 */     if (expirableValue == null) {
/* 265 */       return null;
/*     */     }
/* 267 */     return expirableValue.map(ExpirableValue::getValue);
/*     */   }
/*     */   
/*     */   public <U> long getTimeUntilExpiry(MemoryModuleType<U> type) {
/* 271 */     Optional<? extends ExpirableValue<?>> memory = (Optional)this.memories.get(type);
/* 272 */     return ((Long)memory.map(ExpirableValue::getTimeToLive).orElse(Long.valueOf(0L))).longValue();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForDebug
/* 278 */   public Map<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> getMemories() { return this.memories; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <U> boolean isMemoryValue(MemoryModuleType<U> memoryType, U value) {
/* 285 */     if (!hasMemoryValue(memoryType)) {
/* 286 */       return false;
/*     */     }
/* 288 */     return getMemory(memoryType).filter(memory -> memory.equals(value)).isPresent();
/*     */   }
/*     */   
/*     */   public boolean checkMemory(MemoryModuleType<?> type, MemoryStatus status) {
/* 292 */     Optional<? extends ExpirableValue<?>> optionalExpirableValue = (Optional)this.memories.get(type);
/* 293 */     if (optionalExpirableValue == null) {
/* 294 */       return false;
/*     */     }
/*     */     
/* 297 */     return (status == MemoryStatus.REGISTERED || (status == MemoryStatus.VALUE_PRESENT && optionalExpirableValue
/* 298 */       .isPresent()) || (status == MemoryStatus.VALUE_ABSENT && optionalExpirableValue
/* 299 */       .isEmpty()));
/*     */   }
/*     */ 
/*     */   
/* 303 */   public void setSchedule(EnvironmentAttribute<Activity> schedule) { this.schedule = schedule; }
/*     */ 
/*     */ 
/*     */   
/* 307 */   public void setCoreActivities(Set<Activity> activities) { this.coreActivities = activities; }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForDebug
/* 313 */   public Set<Activity> getActiveActivities() { return this.activeActivities; }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForDebug
/*     */   public List<BehaviorControl<? super E>> getRunningBehaviors() {
/* 319 */     ObjectArrayList objectArrayList = new ObjectArrayList();
/* 320 */     for (Map<Activity, Set<BehaviorControl<? super E>>> behavioursByActivities : this.availableBehaviorsByPriority.values()) {
/* 321 */       for (Set<BehaviorControl<? super E>> behaviors : behavioursByActivities.values()) {
/* 322 */         for (BehaviorControl<? super E> behavior : behaviors) {
/* 323 */           if (behavior.getStatus() == Behavior.Status.RUNNING) {
/* 324 */             objectArrayList.add(behavior);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 329 */     return objectArrayList;
/*     */   }
/*     */ 
/*     */   
/* 333 */   public void useDefaultActivity() { setActiveActivity(this.defaultActivity); }
/*     */ 
/*     */   
/*     */   public Optional<Activity> getActiveNonCoreActivity() {
/* 337 */     for (Activity activity : this.activeActivities) {
/* 338 */       if (!this.coreActivities.contains(activity)) {
/* 339 */         return Optional.of(activity);
/*     */       }
/*     */     } 
/* 342 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActiveActivityIfPossible(Activity activity) {
/* 351 */     if (activityRequirementsAreMet(activity)) {
/* 352 */       setActiveActivity(activity);
/*     */     } else {
/* 354 */       useDefaultActivity();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setActiveActivity(Activity activity) {
/* 359 */     if (isActive(activity)) {
/*     */       return;
/*     */     }
/*     */     
/* 363 */     eraseMemoriesForOtherActivitesThan(activity);
/* 364 */     this.activeActivities.clear();
/* 365 */     this.activeActivities.addAll(this.coreActivities);
/* 366 */     this.activeActivities.add(activity);
/*     */   }
/*     */   
/*     */   private void eraseMemoriesForOtherActivitesThan(Activity activity) {
/* 370 */     for (Activity oldActivity : this.activeActivities) {
/* 371 */       if (oldActivity != activity) {
/* 372 */         Set<MemoryModuleType<?>> memoryModuleTypes = (Set)this.activityMemoriesToEraseWhenStopped.get(oldActivity);
/* 373 */         if (memoryModuleTypes != null) {
/* 374 */           for (MemoryModuleType<?> memoryModuleType : memoryModuleTypes) {
/* 375 */             eraseMemory(memoryModuleType);
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateActivityFromSchedule(EnvironmentAttributeSystem environmentAttributes, long gameTime, Vec3 pos) {
/* 387 */     if (gameTime - this.lastScheduleUpdate > 20L) {
/* 388 */       this.lastScheduleUpdate = gameTime;
/* 389 */       Activity scheduledActivity = (this.schedule != null) ? (Activity)environmentAttributes.getValue(this.schedule, pos) : Activity.IDLE;
/* 390 */       if (!this.activeActivities.contains(scheduledActivity)) {
/* 391 */         setActiveActivityIfPossible(scheduledActivity);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActiveActivityToFirstValid(List<Activity> activities) {
/* 400 */     for (Activity activity : activities) {
/* 401 */       if (activityRequirementsAreMet(activity)) {
/* 402 */         setActiveActivity(activity);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 409 */   public void setDefaultActivity(Activity activity) { this.defaultActivity = activity; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 416 */   public void addActivity(Activity activity, int priorityOfFirstBehavior, ImmutableList<? extends BehaviorControl<? super E>> behaviorList) { addActivity(activity, createPriorityPairs(priorityOfFirstBehavior, behaviorList)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActivityAndRemoveMemoryWhenStopped(Activity activity, int priorityOfFirstBehavior, ImmutableList<? extends BehaviorControl<? super E>> behaviorList, MemoryModuleType<?> memoryThatMustHaveValueAndWillBeErasedAfter) {
/* 425 */     ImmutableSet immutableSet1 = ImmutableSet.of(
/* 426 */         Pair.of(memoryThatMustHaveValueAndWillBeErasedAfter, MemoryStatus.VALUE_PRESENT));
/*     */     
/* 428 */     ImmutableSet immutableSet2 = ImmutableSet.of(memoryThatMustHaveValueAndWillBeErasedAfter);
/* 429 */     addActivityAndRemoveMemoriesWhenStopped(activity, createPriorityPairs(priorityOfFirstBehavior, behaviorList), immutableSet1, immutableSet2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 436 */   public void addActivity(Activity activity, ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> behaviorPriorityPairs) { addActivityAndRemoveMemoriesWhenStopped(activity, behaviorPriorityPairs, ImmutableSet.of(), Sets.newHashSet()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 444 */   public void addActivityWithConditions(Activity activity, int priorityOfFirstBehavior, ImmutableList<? extends BehaviorControl<? super E>> behaviorList, Set<Pair<MemoryModuleType<?>, MemoryStatus>> conditions) { addActivityWithConditions(activity, createPriorityPairs(priorityOfFirstBehavior, behaviorList), conditions); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 452 */   public void addActivityWithConditions(Activity activity, ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> behaviorPriorityPairs, Set<Pair<MemoryModuleType<?>, MemoryStatus>> conditions) { addActivityAndRemoveMemoriesWhenStopped(activity, behaviorPriorityPairs, conditions, Sets.newHashSet()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActivityAndRemoveMemoriesWhenStopped(Activity activity, ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> behaviorPriorityPairs, Set<Pair<MemoryModuleType<?>, MemoryStatus>> conditions, Set<MemoryModuleType<?>> memoriesToEraseWhenStopped) {
/* 461 */     this.activityRequirements.put(activity, conditions);
/* 462 */     if (!memoriesToEraseWhenStopped.isEmpty()) {
/* 463 */       this.activityMemoriesToEraseWhenStopped.put(activity, memoriesToEraseWhenStopped);
/*     */     }
/* 465 */     for (UnmodifiableIterator unmodifiableIterator = behaviorPriorityPairs.iterator(); unmodifiableIterator.hasNext(); ) { Pair<Integer, ? extends BehaviorControl<? super E>> pair = (Pair)unmodifiableIterator.next();
/* 466 */       ((Set)((Map)this.availableBehaviorsByPriority
/* 467 */         .computeIfAbsent((Integer)pair.getFirst(), key -> Maps.newHashMap()))
/* 468 */         .computeIfAbsent(activity, key -> Sets.newLinkedHashSet()))
/* 469 */         .add((BehaviorControl)pair.getSecond()); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 475 */   public void removeAllBehaviors() { this.availableBehaviorsByPriority.clear(); }
/*     */ 
/*     */ 
/*     */   
/* 479 */   public boolean isActive(Activity activity) { return this.activeActivities.contains(activity); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Brain<E> copyWithoutBehaviors() {
/* 484 */     Brain<E> brain = new Brain<E>(this.memories.keySet(), this.sensors.keySet(), ImmutableList.of(), this.codec);
/* 485 */     for (Map.Entry<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> memoryEntry : this.memories.entrySet()) {
/* 486 */       MemoryModuleType<?> memoryModuleType = (MemoryModuleType)memoryEntry.getKey();
/* 487 */       if (((Optional)memoryEntry.getValue()).isPresent()) {
/* 488 */         brain.memories.put(memoryModuleType, (Optional)memoryEntry.getValue());
/*     */       }
/*     */     } 
/* 491 */     return brain;
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel level, E body) {
/* 495 */     forgetOutdatedMemories();
/* 496 */     tickSensors(level, body);
/* 497 */     startEachNonRunningBehavior(level, body);
/* 498 */     tickEachRunningBehavior(level, body);
/*     */   }
/*     */   
/*     */   private void tickSensors(ServerLevel level, E body) {
/* 502 */     for (Sensor<? super E> sensor : this.sensors.values()) {
/* 503 */       sensor.tick(level, body);
/*     */     }
/*     */   }
/*     */   
/*     */   private void forgetOutdatedMemories() {
/* 508 */     for (Map.Entry<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> entry : this.memories.entrySet()) {
/* 509 */       if (((Optional)entry.getValue()).isPresent()) {
/* 510 */         ExpirableValue<?> memory = (ExpirableValue)((Optional)entry.getValue()).get();
/* 511 */         if (memory.hasExpired()) {
/* 512 */           eraseMemory((MemoryModuleType)entry.getKey());
/*     */         }
/* 514 */         memory.tick();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stopAll(ServerLevel level, E body) {
/* 520 */     long timestamp = body.level().getGameTime();
/* 521 */     for (BehaviorControl<? super E> behavior : getRunningBehaviors()) {
/* 522 */       behavior.doStop(level, body, timestamp);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void startEachNonRunningBehavior(ServerLevel level, E body) {
/* 530 */     long time = level.getGameTime();
/* 531 */     for (Map<Activity, Set<BehaviorControl<? super E>>> behavioursByActivities : this.availableBehaviorsByPriority.values()) {
/* 532 */       for (Map.Entry<Activity, Set<BehaviorControl<? super E>>> behavioursForActivity : behavioursByActivities.entrySet()) {
/* 533 */         Activity activity = (Activity)behavioursForActivity.getKey();
/* 534 */         if (this.activeActivities.contains(activity)) {
/* 535 */           Set<BehaviorControl<? super E>> behaviors = (Set)behavioursForActivity.getValue();
/* 536 */           for (BehaviorControl<? super E> behavior : behaviors) {
/* 537 */             if (behavior.getStatus() == Behavior.Status.STOPPED) {
/* 538 */               behavior.tryStart(level, body, time);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void tickEachRunningBehavior(ServerLevel level, E body) {
/* 551 */     long timestamp = level.getGameTime();
/* 552 */     for (BehaviorControl<? super E> behavior : getRunningBehaviors()) {
/* 553 */       behavior.tickOrStop(level, body, timestamp);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean activityRequirementsAreMet(Activity activity) {
/* 558 */     if (!this.activityRequirements.containsKey(activity)) {
/* 559 */       return false;
/*     */     }
/*     */     
/* 562 */     for (Pair<MemoryModuleType<?>, MemoryStatus> memoryRequirement : (Set)this.activityRequirements.get(activity)) {
/* 563 */       MemoryModuleType<?> memoryType = (MemoryModuleType)memoryRequirement.getFirst();
/* 564 */       MemoryStatus memoryStatus = (MemoryStatus)memoryRequirement.getSecond();
/* 565 */       if (!checkMemory(memoryType, memoryStatus)) {
/* 566 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 570 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 574 */   private boolean isEmptyCollection(Object object) { return (object instanceof Collection && ((Collection)object).isEmpty()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> createPriorityPairs(int priorityOfFirstBehavior, ImmutableList<? extends BehaviorControl<? super E>> behaviorList) {
/* 581 */     int nextPrio = priorityOfFirstBehavior;
/* 582 */     ImmutableList.Builder<Pair<Integer, ? extends BehaviorControl<? super E>>> listBuilder = ImmutableList.builder();
/* 583 */     for (UnmodifiableIterator unmodifiableIterator = behaviorList.iterator(); unmodifiableIterator.hasNext(); ) { BehaviorControl<? super E> behavior = (BehaviorControl)unmodifiableIterator.next();
/* 584 */       listBuilder.add(Pair.of(Integer.valueOf(nextPrio++), behavior)); }
/*     */     
/* 586 */     return listBuilder.build();
/*     */   }
/*     */ 
/*     */   
/* 590 */   public boolean isBrainDead() { return (this.memories.isEmpty() && this.sensors.isEmpty() && this.availableBehaviorsByPriority.isEmpty()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\Brain.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
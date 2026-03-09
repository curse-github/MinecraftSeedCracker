/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import org.apache.commons.lang3.mutable.MutableLong;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AcquirePoi
/*     */ {
/*     */   public static final int SCAN_RANGE = 48;
/*     */   
/*  39 */   public static BehaviorControl<PathfinderMob> create(Predicate<Holder<PoiType>> poiType, MemoryModuleType<GlobalPos> memoryToAcquire, boolean onlyIfAdult, Optional<Byte> onPoiAcquisitionEvent, BiPredicate<ServerLevel, BlockPos> validPoi) { return create(poiType, memoryToAcquire, memoryToAcquire, onlyIfAdult, onPoiAcquisitionEvent, validPoi); }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public static BehaviorControl<PathfinderMob> create(Predicate<Holder<PoiType>> poiType, MemoryModuleType<GlobalPos> memoryToAcquire, boolean onlyIfAdult, Optional<Byte> onPoiAcquisitionEvent) { return create(poiType, memoryToAcquire, memoryToAcquire, onlyIfAdult, onPoiAcquisitionEvent, (l, p) -> true); }
/*     */ 
/*     */   
/*     */   public static BehaviorControl<PathfinderMob> create(Predicate<Holder<PoiType>> poiType, MemoryModuleType<GlobalPos> memoryToValidate, MemoryModuleType<GlobalPos> memoryToAcquire, boolean onlyIfAdult, Optional<Byte> onPoiAcquisitionEvent, BiPredicate<ServerLevel, BlockPos> validPoi) {
/*  47 */     int batchSize = 5;
/*  48 */     int rate = 20;
/*     */ 
/*     */     
/*  51 */     MutableLong nextScheduledStart = new MutableLong(0L);
/*  52 */     Long2ObjectOpenHashMap long2ObjectOpenHashMap = new Long2ObjectOpenHashMap();
/*     */     
/*  54 */     OneShot<PathfinderMob> acquirePoi = BehaviorBuilder.create(i -> i.group(i
/*  55 */           .absent(memoryToAcquire))
/*  56 */         .apply(i, ()));
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
/* 117 */     if (memoryToAcquire == memoryToValidate) {
/* 118 */       return acquirePoi;
/*     */     }
/*     */     
/* 121 */     return BehaviorBuilder.create(i -> i.group(i
/* 122 */           .absent(memoryToValidate))
/* 123 */         .apply(i, ()));
/*     */   }
/*     */   
/*     */   public static Path findPathToPois(Mob body, Set<Pair<Holder<PoiType>, BlockPos>> pois) {
/* 127 */     if (pois.isEmpty()) {
/* 128 */       return null;
/*     */     }
/* 130 */     Set<BlockPos> targets = new HashSet<BlockPos>();
/* 131 */     int maxRange = 1;
/* 132 */     for (Pair<Holder<PoiType>, BlockPos> p : pois) {
/* 133 */       maxRange = Math.max(maxRange, ((PoiType)((Holder)p.getFirst()).value()).validRange());
/* 134 */       targets.add((BlockPos)p.getSecond());
/*     */     } 
/* 136 */     return body.getNavigation().createPath(targets, maxRange);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class JitteredLinearRetry
/*     */   {
/*     */     private static final int MIN_INTERVAL_INCREASE = 40;
/*     */     private static final int MAX_INTERVAL_INCREASE = 80;
/*     */     private static final int MAX_RETRY_PATHFINDING_INTERVAL = 400;
/*     */     private final RandomSource random;
/*     */     private long previousAttemptTimestamp;
/*     */     private long nextScheduledAttemptTimestamp;
/*     */     private int currentDelay;
/*     */     
/*     */     JitteredLinearRetry(RandomSource random, long firstAttemptTimestamp) {
/* 151 */       this.random = random;
/* 152 */       markAttempt(firstAttemptTimestamp);
/*     */     }
/*     */     
/*     */     public void markAttempt(long timestamp) {
/* 156 */       this.previousAttemptTimestamp = timestamp;
/* 157 */       int suggestedDelay = this.currentDelay + this.random.nextInt(40) + 40;
/* 158 */       this.currentDelay = Math.min(suggestedDelay, 400);
/* 159 */       this.nextScheduledAttemptTimestamp = timestamp + this.currentDelay;
/*     */     }
/*     */ 
/*     */     
/* 163 */     public boolean isStillValid(long timestamp) { return (timestamp - this.previousAttemptTimestamp < 400L); }
/*     */ 
/*     */ 
/*     */     
/* 167 */     public boolean shouldRetry(long timestamp) { return (timestamp >= this.nextScheduledAttemptTimestamp); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     public String toString() { return "RetryMarker{, previousAttemptAt=" + this.previousAttemptTimestamp + ", nextScheduledAttemptAt=" + this.nextScheduledAttemptTimestamp + ", currentDelay=" + this.currentDelay + "}"; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\AcquirePoi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.entity.ai.village.poi;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.SectionTracker;
/*     */ import net.minecraft.tags.PoiTypeTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.util.debug.DebugPoiInfo;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.storage.ChunkIOErrorReporter;
/*     */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*     */ import net.minecraft.world.level.chunk.storage.SectionStorage;
/*     */ import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
/*     */ 
/*     */ public class PoiManager
/*     */   extends SectionStorage<PoiSection, PoiSection.Packed> {
/*     */   public static final int MAX_VILLAGE_DISTANCE = 6;
/*     */   public static final int VILLAGE_SECTION_SIZE = 1;
/*     */   private final DistanceTracker distanceTracker;
/*  49 */   private final LongSet loadedChunks = new LongOpenHashSet();
/*     */   
/*     */   public PoiManager(RegionStorageInfo info, Path folder, DataFixer fixerUpper, boolean sync, RegistryAccess registryAccess, ChunkIOErrorReporter errorReporter, LevelHeightAccessor levelHeightAccessor) {
/*  52 */     super(new SimpleRegionStorage(info, folder, fixerUpper, sync, DataFixTypes.POI_CHUNK), PoiSection.Packed.CODEC, PoiSection::pack, PoiSection.Packed::unpack, PoiSection::new, registryAccess, errorReporter, levelHeightAccessor);
/*  53 */     this.distanceTracker = new DistanceTracker();
/*     */   }
/*     */ 
/*     */   
/*  57 */   public PoiRecord add(BlockPos pos, Holder<PoiType> type) { return ((PoiSection)getOrCreate(SectionPos.asLong(pos))).add(pos, type); }
/*     */ 
/*     */ 
/*     */   
/*  61 */   public void remove(BlockPos pos) { getOrLoad(SectionPos.asLong(pos)).ifPresent(poiSection -> poiSection.remove(pos)); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public long getCountInRange(Predicate<Holder<PoiType>> predicate, BlockPos center, int radius, Occupancy occupancy) { return getInRange(predicate, center, radius, occupancy).count(); }
/*     */ 
/*     */ 
/*     */   
/*  69 */   public boolean existsAtPosition(ResourceKey<PoiType> poiType, BlockPos blockPos) { return exists(blockPos, p -> p.is(poiType)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stream<PoiRecord> getInSquare(Predicate<Holder<PoiType>> predicate, BlockPos center, int radius, Occupancy occupancy) {
/*  76 */     int chunkRadius = Math.floorDiv(radius, 16) + 1;
/*     */     
/*  78 */     return ChunkPos.rangeClosed(new ChunkPos(center), chunkRadius).flatMap(pos -> getInChunk(predicate, pos, occupancy))
/*  79 */       .filter(record -> {
/*  80 */           BlockPos pos = record.getPos();
/*  81 */           return (Math.abs(pos.getX() - center.getX()) <= radius && 
/*  82 */             Math.abs(pos.getZ() - center.getZ()) <= radius);
/*     */         });
/*     */   }
/*     */   
/*     */   public Stream<PoiRecord> getInRange(Predicate<Holder<PoiType>> predicate, BlockPos center, int radius, Occupancy occupancy) {
/*  87 */     int radiusSqr = radius * radius;
/*  88 */     return getInSquare(predicate, center, radius, occupancy).filter(r -> (r.getPos().distSqr(center) <= radiusSqr));
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/*  93 */   public Stream<PoiRecord> getInChunk(Predicate<Holder<PoiType>> predicate, ChunkPos chunkPos, Occupancy occupancy) { return IntStream.rangeClosed(this.levelHeightAccessor.getMinSectionY(), this.levelHeightAccessor.getMaxSectionY()).boxed()
/*  94 */       .map(sectionY -> getOrLoad(SectionPos.of(chunkPos, sectionY.intValue()).asLong()))
/*  95 */       .filter(Optional::isPresent)
/*  96 */       .flatMap(poiSection -> ((PoiSection)poiSection.get()).getRecords(predicate, occupancy)); }
/*     */ 
/*     */   
/*     */   public Stream<BlockPos> findAll(Predicate<Holder<PoiType>> predicate, Predicate<BlockPos> filter, BlockPos center, int radius, Occupancy occupancy) {
/* 100 */     return getInRange(predicate, center, radius, occupancy)
/* 101 */       .map(PoiRecord::getPos)
/* 102 */       .filter(filter);
/*     */   }
/*     */ 
/*     */   
/* 106 */   public Stream<Pair<Holder<PoiType>, BlockPos>> findAllWithType(Predicate<Holder<PoiType>> predicate, Predicate<BlockPos> filter, BlockPos center, int radius, Occupancy occupancy) { return getInRange(predicate, center, radius, occupancy)
/* 107 */       .filter(p -> filter.test(p.getPos()))
/* 108 */       .map(p -> Pair.of(p.getPoiType(), p.getPos())); }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public Stream<Pair<Holder<PoiType>, BlockPos>> findAllClosestFirstWithType(Predicate<Holder<PoiType>> predicate, Predicate<BlockPos> filter, BlockPos center, int radius, Occupancy occupancy) { return findAllWithType(predicate, filter, center, radius, occupancy)
/* 113 */       .sorted(Comparator.comparingDouble(p -> ((BlockPos)p.getSecond()).distSqr(center))); }
/*     */ 
/*     */ 
/*     */   
/* 117 */   public Optional<BlockPos> find(Predicate<Holder<PoiType>> predicate, Predicate<BlockPos> filter, BlockPos center, int radius, Occupancy occupancy) { return findAll(predicate, filter, center, radius, occupancy).findFirst(); }
/*     */ 
/*     */ 
/*     */   
/* 121 */   public Optional<BlockPos> findClosest(Predicate<Holder<PoiType>> predicate, BlockPos center, int radius, Occupancy occupancy) { return getInRange(predicate, center, radius, occupancy)
/* 122 */       .map(PoiRecord::getPos)
/* 123 */       .min(Comparator.comparingDouble(pos -> pos.distSqr(center))); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public Optional<Pair<Holder<PoiType>, BlockPos>> findClosestWithType(Predicate<Holder<PoiType>> predicate, BlockPos center, int radius, Occupancy occupancy) { return getInRange(predicate, center, radius, occupancy)
/* 128 */       .min(Comparator.comparingDouble(r -> r.getPos().distSqr(center)))
/* 129 */       .map(p -> Pair.of(p.getPoiType(), p.getPos())); }
/*     */ 
/*     */ 
/*     */   
/* 133 */   public Optional<BlockPos> findClosest(Predicate<Holder<PoiType>> predicate, Predicate<BlockPos> filter, BlockPos center, int radius, Occupancy occupancy) { return getInRange(predicate, center, radius, occupancy)
/* 134 */       .map(PoiRecord::getPos)
/* 135 */       .filter(filter)
/* 136 */       .min(Comparator.comparingDouble(pos -> pos.distSqr(center))); }
/*     */ 
/*     */   
/*     */   public Optional<BlockPos> take(Predicate<Holder<PoiType>> predicate, BiPredicate<Holder<PoiType>, BlockPos> filter, BlockPos center, int radius) {
/* 140 */     return getInRange(predicate, center, radius, Occupancy.HAS_SPACE)
/* 141 */       .filter(poi -> filter.test(poi.getPoiType(), poi.getPos()))
/* 142 */       .findFirst()
/* 143 */       .map(r -> {
/* 144 */           r.acquireTicket();
/* 145 */           return r.getPos();
/*     */         });
/*     */   }
/*     */   
/*     */   public Optional<BlockPos> getRandom(Predicate<Holder<PoiType>> predicate, Predicate<BlockPos> filter, Occupancy occupancy, BlockPos center, int radius, RandomSource random) {
/* 150 */     List<PoiRecord> collect = Util.toShuffledList(getInRange(predicate, center, radius, occupancy), random);
/* 151 */     return collect.stream().filter(poi -> filter.test(poi.getPos())).findFirst().map(PoiRecord::getPos);
/*     */   }
/*     */ 
/*     */   
/* 155 */   public boolean release(BlockPos pos) { return ((Boolean)getOrLoad(SectionPos.asLong(pos))
/* 156 */       .map(section -> Boolean.valueOf(section.release(pos)))
/* 157 */       .orElseThrow(() -> (IllegalStateException)Util.pauseInIde(new IllegalStateException("POI never registered at " + String.valueOf(pos))))).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 161 */   public boolean exists(BlockPos pos, Predicate<Holder<PoiType>> predicate) { return ((Boolean)getOrLoad(SectionPos.asLong(pos)).map(s -> Boolean.valueOf(s.exists(pos, predicate))).orElse(Boolean.valueOf(false))).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 165 */   public Optional<Holder<PoiType>> getType(BlockPos pos) { return getOrLoad(SectionPos.asLong(pos)).flatMap(section -> section.getType(pos)); }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/* 170 */   public DebugPoiInfo getDebugPoiInfo(BlockPos pos) { return (DebugPoiInfo)getOrLoad(SectionPos.asLong(pos)).flatMap(section -> section.getDebugPoiInfo(pos)).orElse(null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int sectionsToVillage(SectionPos sectionPos) {
/* 179 */     this.distanceTracker.runAllUpdates();
/* 180 */     return this.distanceTracker.getLevel(sectionPos.asLong());
/*     */   }
/*     */   
/*     */   private boolean isVillageCenter(long sectionPos) {
/* 184 */     Optional<PoiSection> section = get(sectionPos);
/* 185 */     if (section == null) {
/* 186 */       return false;
/*     */     }
/*     */     
/* 189 */     return ((Boolean)section.map(s -> Boolean.valueOf(s.getRecords((), Occupancy.IS_OCCUPIED).findAny().isPresent())).orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BooleanSupplier haveTime) {
/* 194 */     super.tick(haveTime);
/* 195 */     this.distanceTracker.runAllUpdates();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setDirty(long sectionPos) {
/* 200 */     super.setDirty(sectionPos);
/* 201 */     this.distanceTracker.update(sectionPos, this.distanceTracker.getLevelFromSource(sectionPos), false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 206 */   protected void onSectionLoad(long sectionPos) { this.distanceTracker.update(sectionPos, this.distanceTracker.getLevelFromSource(sectionPos), false); }
/*     */ 
/*     */   
/*     */   public void checkConsistencyWithBlocks(SectionPos sectionPos, LevelChunkSection blockSection) {
/* 210 */     Util.ifElse(getOrLoad(sectionPos.asLong()), section -> 
/*     */         
/* 212 */         section.refresh(()), () -> {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 219 */           if (mayHavePoi(blockSection)) {
/* 220 */             PoiSection newSection = (PoiSection)getOrCreate(sectionPos.asLong());
/* 221 */             Objects.requireNonNull(newSection); updateFromSection(blockSection, sectionPos, newSection::add);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 228 */   private static boolean mayHavePoi(LevelChunkSection blockSection) { return blockSection.maybeHas(PoiTypes::hasPoi); }
/*     */ 
/*     */   
/*     */   private void updateFromSection(LevelChunkSection blockSection, SectionPos pos, BiConsumer<BlockPos, Holder<PoiType>> output) {
/* 232 */     pos.blocksInside().forEach(blockPos -> {
/* 233 */           BlockState state = blockSection.getBlockState(
/* 234 */               SectionPos.sectionRelative(blockPos.getX()), 
/* 235 */               SectionPos.sectionRelative(blockPos.getY()), 
/* 236 */               SectionPos.sectionRelative(blockPos.getZ()));
/*     */           
/* 238 */           PoiTypes.forState(state).ifPresent(());
/*     */         });
/*     */   }
/*     */ 
/*     */   
/* 243 */   public void ensureLoadedAndValid(LevelReader reader, BlockPos center, int radius) { SectionPos.aroundChunk(new ChunkPos(center), Math.floorDiv(radius, 16), this.levelHeightAccessor.getMinSectionY(), this.levelHeightAccessor.getMaxSectionY())
/* 244 */       .map(pos -> Pair.of(pos, getOrLoad(pos.asLong())))
/* 245 */       .filter(poiSection -> !((Boolean)((Optional)poiSection.getSecond()).map(PoiSection::isValid).orElse(Boolean.valueOf(false))).booleanValue())
/* 246 */       .map(p -> ((SectionPos)p.getFirst()).chunk())
/* 247 */       .filter(pos -> this.loadedChunks.add(pos.toLong()))
/* 248 */       .forEach(pos -> reader.getChunk(pos.x, pos.z, ChunkStatus.EMPTY)); }
/*     */   
/*     */   public enum Occupancy
/*     */   {
/* 252 */     HAS_SPACE(PoiRecord::hasSpace),
/* 253 */     IS_OCCUPIED(PoiRecord::isOccupied),
/* 254 */     ANY(poiRecord -> true);
/*     */     
/*     */     private final Predicate<? super PoiRecord> test;
/*     */ 
/*     */     
/* 259 */     Occupancy(Predicate<? super PoiRecord> test) { this.test = test; }
/*     */ 
/*     */ 
/*     */     
/* 263 */     public Predicate<? super PoiRecord> getTest() { return this.test; }
/*     */   }
/*     */   
/*     */   private final class DistanceTracker
/*     */     extends SectionTracker {
/*     */     private final Long2ByteMap levels;
/*     */     
/*     */     protected DistanceTracker() {
/* 271 */       super(7, 16, 256);
/* 272 */       this.levels = new Long2ByteOpenHashMap();
/* 273 */       this.levels.defaultReturnValue((byte)7);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 278 */     protected int getLevelFromSource(long to) { return PoiManager.this.isVillageCenter(to) ? 0 : 7; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 283 */     protected int getLevel(long node) { return this.levels.get(node); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void setLevel(long node, int level) {
/* 288 */       if (level > 6) {
/* 289 */         this.levels.remove(node);
/*     */       } else {
/* 291 */         this.levels.put(node, (byte)level);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 296 */     public void runAllUpdates() { runUpdates(2147483647); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\village\poi\PoiManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
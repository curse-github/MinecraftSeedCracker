/*     */ package net.minecraft.world.level.entity;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongBidirectionalIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSortedSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Spliterators;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.AbortableIterationConsumer;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class EntitySectionStorage<T extends EntityAccess>
/*     */   extends Object {
/*     */   public static final int CHONKY_ENTITY_SEARCH_GRACE = 2;
/*     */   public static final int MAX_NON_CHONKY_ENTITY_SIZE = 4;
/*     */   private final Class<T> entityClass;
/*     */   private final Long2ObjectFunction<Visibility> intialSectionVisibility;
/*     */   private final Long2ObjectMap<EntitySection<T>> sections;
/*     */   private final LongSortedSet sectionIds;
/*     */   
/*     */   public EntitySectionStorage(Class<T> entityClass, Long2ObjectFunction<Visibility> intialSectionVisibility) {
/*  32 */     this.sections = new Long2ObjectOpenHashMap();
/*     */ 
/*     */     
/*  35 */     this.sectionIds = new LongAVLTreeSet();
/*     */ 
/*     */     
/*  38 */     this.entityClass = entityClass;
/*  39 */     this.intialSectionVisibility = intialSectionVisibility;
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEachAccessibleNonEmptySection(AABB bb, AbortableIterationConsumer<EntitySection<T>> output) {
/*  44 */     int xMin = SectionPos.posToSectionCoord(bb.minX - 2.0D);
/*  45 */     int yMin = SectionPos.posToSectionCoord(bb.minY - 4.0D);
/*  46 */     int zMin = SectionPos.posToSectionCoord(bb.minZ - 2.0D);
/*     */     
/*  48 */     int xMax = SectionPos.posToSectionCoord(bb.maxX + 2.0D);
/*  49 */     int yMax = SectionPos.posToSectionCoord(bb.maxY + 0.0D);
/*  50 */     int zMax = SectionPos.posToSectionCoord(bb.maxZ + 2.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  55 */     for (int x = xMin; x <= xMax; x++) {
/*  56 */       long lowestAbsoluteSectionKey = SectionPos.asLong(x, 0, 0);
/*  57 */       long highestAbsoluteSectionKey = SectionPos.asLong(x, -1, -1);
/*  58 */       LongBidirectionalIterator longBidirectionalIterator = this.sectionIds.subSet(lowestAbsoluteSectionKey, highestAbsoluteSectionKey + 1L).iterator();
/*  59 */       while (longBidirectionalIterator.hasNext()) {
/*  60 */         long sectionKey = longBidirectionalIterator.nextLong();
/*  61 */         int y = SectionPos.y(sectionKey);
/*  62 */         int z = SectionPos.z(sectionKey);
/*  63 */         if (y >= yMin && y <= yMax && z >= zMin && z <= zMax) {
/*  64 */           EntitySection<T> entitySection = (EntitySection)this.sections.get(sectionKey);
/*  65 */           if (entitySection != null && !entitySection.isEmpty() && entitySection.getStatus().isAccessible() && 
/*  66 */             output.accept(entitySection).shouldAbort()) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public LongStream getExistingSectionPositionsInChunk(long chunkKey) {
/*  76 */     int x = ChunkPos.getX(chunkKey);
/*  77 */     int z = ChunkPos.getZ(chunkKey);
/*  78 */     LongSortedSet chunkSections = getChunkSections(x, z);
/*  79 */     if (chunkSections.isEmpty()) {
/*  80 */       return LongStream.empty();
/*     */     }
/*  82 */     LongBidirectionalIterator longBidirectionalIterator = chunkSections.iterator();
/*  83 */     return StreamSupport.longStream(Spliterators.spliteratorUnknownSize(longBidirectionalIterator, 1301), false);
/*     */   }
/*     */   
/*     */   private LongSortedSet getChunkSections(int x, int z) {
/*  87 */     long lowestAbsoluteSectionKey = SectionPos.asLong(x, 0, z);
/*  88 */     long highestAbsoluteSectionKey = SectionPos.asLong(x, -1, z);
/*  89 */     return this.sectionIds.subSet(lowestAbsoluteSectionKey, highestAbsoluteSectionKey + 1L);
/*     */   }
/*     */ 
/*     */   
/*  93 */   public Stream<EntitySection<T>> getExistingSectionsInChunk(long chunkKey) { Objects.requireNonNull(this.sections); return getExistingSectionPositionsInChunk(chunkKey).mapToObj(this.sections::get).filter(Objects::nonNull); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   private static long getChunkKeyFromSectionKey(long sectionPos) { return ChunkPos.asLong(SectionPos.x(sectionPos), SectionPos.z(sectionPos)); }
/*     */ 
/*     */ 
/*     */   
/* 101 */   public EntitySection<T> getOrCreateSection(long key) { return (EntitySection)this.sections.computeIfAbsent(key, this::createSection); }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public EntitySection<T> getSection(long key) { return (EntitySection)this.sections.get(key); }
/*     */ 
/*     */   
/*     */   private EntitySection<T> createSection(long sectionPos) {
/* 109 */     long chunkPos = getChunkKeyFromSectionKey(sectionPos);
/* 110 */     Visibility chunkStatus = (Visibility)this.intialSectionVisibility.get(chunkPos);
/* 111 */     this.sectionIds.add(sectionPos);
/* 112 */     return new EntitySection(this.entityClass, chunkStatus);
/*     */   }
/*     */   
/*     */   public LongSet getAllChunksWithExistingSections() {
/* 116 */     LongOpenHashSet longOpenHashSet = new LongOpenHashSet();
/* 117 */     this.sections.keySet().forEach(sectionKey -> chunks.add(getChunkKeyFromSectionKey(sectionKey)));
/* 118 */     return longOpenHashSet;
/*     */   }
/*     */ 
/*     */   
/* 122 */   public void getEntities(AABB bb, AbortableIterationConsumer<T> output) { forEachAccessibleNonEmptySection(bb, section -> section.getEntities(bb, output)); }
/*     */ 
/*     */ 
/*     */   
/* 126 */   public <U extends T> void getEntities(EntityTypeTest<T, U> type, AABB bb, AbortableIterationConsumer<U> consumer) { forEachAccessibleNonEmptySection(bb, section -> section.getEntities(type, bb, consumer)); }
/*     */ 
/*     */   
/*     */   public void remove(long sectionKey) {
/* 130 */     this.sections.remove(sectionKey);
/* 131 */     this.sectionIds.remove(sectionKey);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/* 136 */   public int count() { return this.sectionIds.size(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\entity\EntitySectionStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
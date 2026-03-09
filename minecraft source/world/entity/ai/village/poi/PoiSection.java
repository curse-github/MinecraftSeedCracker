/*     */ package net.minecraft.world.entity.ai.village.poi;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PoiSection {
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Short2ObjectMap<PoiRecord> records;
/*     */   
/*     */   private final Map<Holder<PoiType>, Set<PoiRecord>> byType;
/*     */   private final Runnable setDirty;
/*     */   private boolean isValid;
/*     */   
/*  38 */   public PoiSection(Runnable setDirty) { this(setDirty, true, ImmutableList.of()); }
/*     */   private PoiSection(Runnable setDirty, boolean isValid, List<PoiRecord> records) {
/*     */     this.records = new Short2ObjectOpenHashMap();
/*     */     this.byType = Maps.newHashMap();
/*  42 */     this.setDirty = setDirty;
/*  43 */     this.isValid = isValid;
/*  44 */     records.forEach(this::add);
/*     */   }
/*     */ 
/*     */   
/*  48 */   public Packed pack() { return new Packed(this.isValid, this.records.values().stream().map(PoiRecord::pack).toList()); }
/*     */ 
/*     */ 
/*     */   
/*  52 */   public Stream<PoiRecord> getRecords(Predicate<Holder<PoiType>> predicate, PoiManager.Occupancy occupancy) { return this.byType.entrySet()
/*  53 */       .stream()
/*  54 */       .filter(e -> predicate.test((Holder)e.getKey()))
/*  55 */       .flatMap(e -> ((Set)e.getValue()).stream())
/*  56 */       .filter(occupancy.getTest()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public PoiRecord add(BlockPos blockPos, Holder<PoiType> type) {
/*  61 */     PoiRecord record = new PoiRecord(blockPos, type, this.setDirty);
/*  62 */     if (add(record)) {
/*  63 */       LOGGER.debug("Added POI of type {} @ {}", type.getRegisteredName(), blockPos);
/*  64 */       this.setDirty.run();
/*  65 */       return record;
/*     */     } 
/*  67 */     return null;
/*     */   }
/*     */   
/*     */   private boolean add(PoiRecord record) {
/*  71 */     BlockPos blockPos = record.getPos();
/*  72 */     Holder<PoiType> poiType = record.getPoiType();
/*  73 */     short key = SectionPos.sectionRelativePos(blockPos);
/*  74 */     PoiRecord oldRecord = (PoiRecord)this.records.get(key);
/*     */     
/*  76 */     if (oldRecord != null) {
/*  77 */       if (poiType.equals(oldRecord.getPoiType())) {
/*  78 */         return false;
/*     */       }
/*  80 */       Util.logAndPauseIfInIde("POI data mismatch: already registered at " + String.valueOf(blockPos));
/*     */     } 
/*     */ 
/*     */     
/*  84 */     this.records.put(key, record);
/*  85 */     ((Set)this.byType.computeIfAbsent(poiType, k -> Sets.newHashSet())).add(record);
/*  86 */     return true;
/*     */   }
/*     */   
/*     */   public void remove(BlockPos pos) {
/*  90 */     PoiRecord poiRecord = (PoiRecord)this.records.remove(SectionPos.sectionRelativePos(pos));
/*  91 */     if (poiRecord == null) {
/*  92 */       LOGGER.error("POI data mismatch: never registered at {}", pos);
/*     */       return;
/*     */     } 
/*  95 */     ((Set)this.byType.get(poiRecord.getPoiType())).remove(poiRecord);
/*     */     
/*  97 */     Objects.requireNonNull(poiRecord); Objects.requireNonNull(poiRecord); LOGGER.debug("Removed POI of type {} @ {}", LogUtils.defer(poiRecord::getPoiType), LogUtils.defer(poiRecord::getPos));
/*  98 */     this.setDirty.run();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForDebug
/* 104 */   public int getFreeTickets(BlockPos pos) { return ((Integer)getPoiRecord(pos).map(PoiRecord::getFreeTickets).orElse(Integer.valueOf(0))).intValue(); }
/*     */ 
/*     */   
/*     */   public boolean release(BlockPos pos) {
/* 108 */     PoiRecord record = (PoiRecord)this.records.get(SectionPos.sectionRelativePos(pos));
/* 109 */     if (record == null) {
/* 110 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("POI never registered at " + String.valueOf(pos)));
/*     */     }
/* 112 */     boolean success = record.releaseTicket();
/* 113 */     this.setDirty.run();
/* 114 */     return success;
/*     */   }
/*     */ 
/*     */   
/* 118 */   public boolean exists(BlockPos pos, Predicate<Holder<PoiType>> predicate) { return getType(pos).filter(predicate).isPresent(); }
/*     */ 
/*     */ 
/*     */   
/* 122 */   public Optional<Holder<PoiType>> getType(BlockPos pos) { return getPoiRecord(pos).map(PoiRecord::getPoiType); }
/*     */ 
/*     */ 
/*     */   
/* 126 */   private Optional<PoiRecord> getPoiRecord(BlockPos pos) { return Optional.ofNullable((PoiRecord)this.records.get(SectionPos.sectionRelativePos(pos))); }
/*     */ 
/*     */ 
/*     */   
/* 130 */   public Optional<DebugPoiInfo> getDebugPoiInfo(BlockPos pos) { return getPoiRecord(pos).map(DebugPoiInfo::new); }
/*     */ 
/*     */   
/*     */   public void refresh(Consumer<BiConsumer<BlockPos, Holder<PoiType>>> updater) {
/* 134 */     if (!this.isValid) {
/* 135 */       Short2ObjectOpenHashMap short2ObjectOpenHashMap = new Short2ObjectOpenHashMap(this.records);
/* 136 */       clear();
/* 137 */       updater.accept((blockPos, poiType) -> {
/* 138 */             short key = SectionPos.sectionRelativePos(blockPos);
/* 139 */             PoiRecord newRecord = (PoiRecord)oldRecords.computeIfAbsent(key, ());
/* 140 */             add(newRecord);
/*     */           });
/* 142 */       this.isValid = true;
/* 143 */       this.setDirty.run();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clear() {
/* 148 */     this.records.clear();
/* 149 */     this.byType.clear();
/*     */   }
/*     */ 
/*     */   
/* 153 */   boolean isValid() { return this.isValid; }
/*     */   public static final class Packed extends Record { private final boolean isValid; private final List<PoiRecord.Packed> records;
/*     */     
/* 156 */     public Packed(boolean isValid, List<PoiRecord.Packed> records) { this.isValid = isValid; this.records = records; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #156	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 156 */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed; } public boolean isValid() { return this.isValid; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #156	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #156	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed;
/* 156 */       //   0	8	1	o	Ljava/lang/Object; } public List<PoiRecord.Packed> records() { return this.records; }
/*     */ 
/*     */ 
/*     */     
/* 160 */     public static final Codec<Packed> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.BOOL
/* 161 */           .lenientOptionalFieldOf("Valid", Boolean.valueOf(false)).forGetter(Packed::isValid), PoiRecord.Packed.CODEC
/* 162 */           .listOf().fieldOf("Records").forGetter(Packed::records))
/* 163 */         .apply(i, Packed::new));
/*     */ 
/*     */     
/* 166 */     public PoiSection unpack(Runnable setDirty) { return new PoiSection(setDirty, this.isValid, this.records.stream().map(record -> record.unpack(setDirty)).toList()); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\village\poi\PoiSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
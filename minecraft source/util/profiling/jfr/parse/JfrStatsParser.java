/*     */ package net.minecraft.util.profiling.jfr.parse;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.io.IOException;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import jdk.jfr.consumer.RecordedEvent;
/*     */ import jdk.jfr.consumer.RecordingFile;
/*     */ import net.minecraft.util.profiling.jfr.stats.ChunkGenStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.ChunkIdentification;
/*     */ import net.minecraft.util.profiling.jfr.stats.CpuLoadStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.FileIOStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.FpsStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.GcHeapStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.IoSummary;
/*     */ import net.minecraft.util.profiling.jfr.stats.PacketIdentification;
/*     */ import net.minecraft.util.profiling.jfr.stats.StructureGenStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.ThreadAllocationStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.TickTimeStat;
/*     */ 
/*     */ public class JfrStatsParser {
/*     */   private Instant recordingStarted;
/*     */   private Instant recordingEnded;
/*     */   private final List<ChunkGenStat> chunkGenStats;
/*     */   private final List<StructureGenStat> structureGenStats;
/*     */   private final List<CpuLoadStat> cpuLoadStat;
/*     */   private final Map<PacketIdentification, MutableCountAndSize> receivedPackets;
/*     */   private final Map<PacketIdentification, MutableCountAndSize> sentPackets;
/*     */   private final Map<ChunkIdentification, MutableCountAndSize> readChunks;
/*     */   private final Map<ChunkIdentification, MutableCountAndSize> writtenChunks;
/*     */   private final List<FileIOStat> fileWrites;
/*     */   private final List<FileIOStat> fileReads;
/*     */   private int garbageCollections;
/*     */   private Duration gcTotalDuration;
/*     */   private final List<GcHeapStat> gcHeapStats;
/*     */   private final List<ThreadAllocationStat> threadAllocationStats;
/*     */   private final List<FpsStat> fps;
/*     */   private final List<TickTimeStat> serverTickTimes;
/*     */   private Duration worldCreationDuration;
/*     */   
/*     */   private JfrStatsParser(Stream<RecordedEvent> events) {
/*  47 */     this.recordingStarted = Instant.EPOCH;
/*  48 */     this.recordingEnded = Instant.EPOCH;
/*     */     
/*  50 */     this.chunkGenStats = new ArrayList();
/*  51 */     this.structureGenStats = new ArrayList();
/*  52 */     this.cpuLoadStat = new ArrayList();
/*  53 */     this.receivedPackets = new HashMap();
/*  54 */     this.sentPackets = new HashMap();
/*  55 */     this.readChunks = new HashMap();
/*  56 */     this.writtenChunks = new HashMap();
/*  57 */     this.fileWrites = new ArrayList();
/*  58 */     this.fileReads = new ArrayList();
/*     */     
/*  60 */     this.gcTotalDuration = Duration.ZERO;
/*  61 */     this.gcHeapStats = new ArrayList();
/*  62 */     this.threadAllocationStats = new ArrayList();
/*     */     
/*  64 */     this.fps = new ArrayList();
/*  65 */     this.serverTickTimes = new ArrayList();
/*     */     
/*  67 */     this.worldCreationDuration = null;
/*     */ 
/*     */     
/*  70 */     capture(events);
/*     */   }
/*     */   public static JfrStatsResult parse(Path path) {
/*     */     
/*  74 */     try { final RecordingFile recordingFile = new RecordingFile(path); 
/*  75 */       try { Iterator<RecordedEvent> iterator = new Iterator<RecordedEvent>()
/*     */           {
/*     */             public boolean hasNext() {
/*  78 */               return recordingFile.hasMoreEvents();
/*     */             }
/*     */ 
/*     */             
/*     */             public RecordedEvent next() {
/*  83 */               if (!hasNext()) {
/*  84 */                 throw new NoSuchElementException();
/*     */               }
/*     */               try {
/*  87 */                 return recordingFile.readEvent();
/*  88 */               } catch (IOException e) {
/*  89 */                 throw new UncheckedIOException(e);
/*     */               } 
/*     */             }
/*     */           };
/*  93 */         Stream<RecordedEvent> events = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 1297), false);
/*  94 */         JfrStatsResult jfrStatsResult = (new JfrStatsParser(events)).results();
/*  95 */         recordingFile.close(); return jfrStatsResult; } catch (Throwable throwable) { try { recordingFile.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/*  96 */     { throw new UncheckedIOException(e); }
/*     */   
/*     */   }
/*     */   
/*     */   private JfrStatsResult results() {
/* 101 */     Duration recordingDuration = Duration.between(this.recordingStarted, this.recordingEnded);
/* 102 */     return new JfrStatsResult(this.recordingStarted, this.recordingEnded, recordingDuration, this.worldCreationDuration, this.fps, this.serverTickTimes, this.cpuLoadStat, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 110 */         GcHeapStat.summary(recordingDuration, this.gcHeapStats, this.gcTotalDuration, this.garbageCollections), 
/* 111 */         ThreadAllocationStat.summary(this.threadAllocationStats), 
/* 112 */         collectIoStats(recordingDuration, this.receivedPackets), 
/* 113 */         collectIoStats(recordingDuration, this.sentPackets), 
/* 114 */         collectIoStats(recordingDuration, this.writtenChunks), 
/* 115 */         collectIoStats(recordingDuration, this.readChunks), 
/* 116 */         FileIOStat.summary(recordingDuration, this.fileWrites), 
/* 117 */         FileIOStat.summary(recordingDuration, this.fileReads), this.chunkGenStats, this.structureGenStats);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void capture(Stream<RecordedEvent> events) {
/* 124 */     events.forEach(event -> {
/* 125 */           if (event.getEndTime().isAfter(this.recordingEnded) || this.recordingEnded.equals(Instant.EPOCH)) {
/* 126 */             this.recordingEnded = event.getEndTime();
/*     */           }
/* 128 */           if (event.getStartTime().isBefore(this.recordingStarted) || this.recordingStarted.equals(Instant.EPOCH)) {
/* 129 */             this.recordingStarted = event.getStartTime();
/*     */           }
/*     */           
/* 132 */           switch (event.getEventType().getName()) { case "minecraft.ChunkGeneration":
/* 133 */               this.chunkGenStats.add(ChunkGenStat.from(event)); break;
/* 134 */             case "minecraft.StructureGeneration": this.structureGenStats.add(StructureGenStat.from(event)); break;
/* 135 */             case "minecraft.LoadWorld": this.worldCreationDuration = event.getDuration(); break;
/* 136 */             case "minecraft.ClientFps": this.fps.add(FpsStat.from(event, "fps")); break;
/* 137 */             case "minecraft.ServerTickTime": this.serverTickTimes.add(TickTimeStat.from(event)); break;
/* 138 */             case "minecraft.PacketReceived": incrementPacket(event, event.getInt("bytes"), this.receivedPackets); break;
/* 139 */             case "minecraft.PacketSent": incrementPacket(event, event.getInt("bytes"), this.sentPackets); break;
/* 140 */             case "minecraft.ChunkRegionRead": incrementChunk(event, event.getInt("bytes"), this.readChunks); break;
/* 141 */             case "minecraft.ChunkRegionWrite": incrementChunk(event, event.getInt("bytes"), this.writtenChunks); break;
/* 142 */             case "jdk.ThreadAllocationStatistics": this.threadAllocationStats.add(ThreadAllocationStat.from(event)); break;
/* 143 */             case "jdk.GCHeapSummary": this.gcHeapStats.add(GcHeapStat.from(event)); break;
/* 144 */             case "jdk.CPULoad": this.cpuLoadStat.add(CpuLoadStat.from(event)); break;
/* 145 */             case "jdk.FileWrite": appendFileIO(event, this.fileWrites, "bytesWritten"); break;
/* 146 */             case "jdk.FileRead": appendFileIO(event, this.fileReads, "bytesRead"); break;
/*     */             case "jdk.GarbageCollection":
/* 148 */               this.garbageCollections++;
/* 149 */               this.gcTotalDuration = this.gcTotalDuration.plus(event.getDuration());
/*     */               break; }
/*     */         
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   private void incrementPacket(RecordedEvent event, int packetSize, Map<PacketIdentification, MutableCountAndSize> packets) { ((MutableCountAndSize)packets.computeIfAbsent(PacketIdentification.from(event), ignored -> new MutableCountAndSize())).increment(packetSize); }
/*     */ 
/*     */ 
/*     */   
/* 162 */   private void incrementChunk(RecordedEvent event, int chunkSize, Map<ChunkIdentification, MutableCountAndSize> packets) { ((MutableCountAndSize)packets.computeIfAbsent(ChunkIdentification.from(event), ignored -> new MutableCountAndSize())).increment(chunkSize); }
/*     */ 
/*     */ 
/*     */   
/* 166 */   private void appendFileIO(RecordedEvent event, List<FileIOStat> stats, String sizeField) { stats.add(new FileIOStat(event.getDuration(), event.getString("path"), event.getLong(sizeField))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> IoSummary<T> collectIoStats(Duration recordingDuration, Map<T, MutableCountAndSize> packetStats) {
/* 172 */     List<Pair<T, IoSummary.CountAndSize>> summaryStats = packetStats.entrySet().stream().map(e -> Pair.of(e.getKey(), ((MutableCountAndSize)e.getValue()).toCountAndSize())).toList();
/* 173 */     return new IoSummary(recordingDuration, summaryStats);
/*     */   }
/*     */   
/*     */   public static final class MutableCountAndSize {
/*     */     private long count;
/*     */     private long totalSize;
/*     */     
/*     */     public void increment(int bytes) {
/* 181 */       this.totalSize += bytes;
/* 182 */       this.count++;
/*     */     }
/*     */ 
/*     */     
/* 186 */     public IoSummary.CountAndSize toCountAndSize() { return new IoSummary.CountAndSize(this.count, this.totalSize); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\parse\JfrStatsParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
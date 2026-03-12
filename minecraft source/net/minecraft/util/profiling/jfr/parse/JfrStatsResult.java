/*    */ package net.minecraft.util.profiling.jfr.parse;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.time.Duration;
/*    */ import java.time.Instant;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.profiling.jfr.stats.ChunkGenStat;
/*    */ import net.minecraft.util.profiling.jfr.stats.ChunkIdentification;
/*    */ import net.minecraft.util.profiling.jfr.stats.FileIOStat;
/*    */ import net.minecraft.util.profiling.jfr.stats.GcHeapStat;
/*    */ import net.minecraft.util.profiling.jfr.stats.IoSummary;
/*    */ import net.minecraft.util.profiling.jfr.stats.PacketIdentification;
/*    */ import net.minecraft.util.profiling.jfr.stats.ThreadAllocationStat;
/*    */ import net.minecraft.util.profiling.jfr.stats.TimedStatSummary;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ 
/*    */ public final class JfrStatsResult extends Record {
/*    */   private final Instant recordingStarted;
/*    */   private final Instant recordingEnded;
/*    */   private final Duration recordingDuration;
/*    */   private final Duration worldCreationDuration;
/*    */   private final List<FpsStat> fps;
/*    */   private final List<TickTimeStat> serverTickTimes;
/*    */   private final List<CpuLoadStat> cpuLoadStats;
/*    */   private final GcHeapStat.Summary heapSummary;
/*    */   
/* 27 */   public JfrStatsResult(Instant recordingStarted, Instant recordingEnded, Duration recordingDuration, Duration worldCreationDuration, List<FpsStat> fps, List<TickTimeStat> serverTickTimes, List<CpuLoadStat> cpuLoadStats, GcHeapStat.Summary heapSummary, ThreadAllocationStat.Summary threadAllocationSummary, IoSummary<PacketIdentification> receivedPacketsSummary, IoSummary<PacketIdentification> sentPacketsSummary, IoSummary<ChunkIdentification> writtenChunks, IoSummary<ChunkIdentification> readChunks, FileIOStat.Summary fileWrites, FileIOStat.Summary fileReads, List<ChunkGenStat> chunkGenStats, List<StructureGenStat> structureGenStats) { this.recordingStarted = recordingStarted; this.recordingEnded = recordingEnded; this.recordingDuration = recordingDuration; this.worldCreationDuration = worldCreationDuration; this.fps = fps; this.serverTickTimes = serverTickTimes; this.cpuLoadStats = cpuLoadStats; this.heapSummary = heapSummary; this.threadAllocationSummary = threadAllocationSummary; this.receivedPacketsSummary = receivedPacketsSummary; this.sentPacketsSummary = sentPacketsSummary; this.writtenChunks = writtenChunks; this.readChunks = readChunks; this.fileWrites = fileWrites; this.fileReads = fileReads; this.chunkGenStats = chunkGenStats; this.structureGenStats = structureGenStats; } private final ThreadAllocationStat.Summary threadAllocationSummary; private final IoSummary<PacketIdentification> receivedPacketsSummary; private final IoSummary<PacketIdentification> sentPacketsSummary; private final IoSummary<ChunkIdentification> writtenChunks; private final IoSummary<ChunkIdentification> readChunks; private final FileIOStat.Summary fileWrites; private final FileIOStat.Summary fileReads; private final List<ChunkGenStat> chunkGenStats; private final List<StructureGenStat> structureGenStats; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/parse/JfrStatsResult;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/parse/JfrStatsResult; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/parse/JfrStatsResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/parse/JfrStatsResult; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/parse/JfrStatsResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/parse/JfrStatsResult;
/* 27 */     //   0	8	1	o	Ljava/lang/Object; } public Instant recordingStarted() { return this.recordingStarted; } public Instant recordingEnded() { return this.recordingEnded; } public Duration recordingDuration() { return this.recordingDuration; } public Duration worldCreationDuration() { return this.worldCreationDuration; } public List<FpsStat> fps() { return this.fps; } public List<TickTimeStat> serverTickTimes() { return this.serverTickTimes; } public List<CpuLoadStat> cpuLoadStats() { return this.cpuLoadStats; } public GcHeapStat.Summary heapSummary() { return this.heapSummary; } public ThreadAllocationStat.Summary threadAllocationSummary() { return this.threadAllocationSummary; } public IoSummary<PacketIdentification> receivedPacketsSummary() { return this.receivedPacketsSummary; } public IoSummary<PacketIdentification> sentPacketsSummary() { return this.sentPacketsSummary; } public IoSummary<ChunkIdentification> writtenChunks() { return this.writtenChunks; } public IoSummary<ChunkIdentification> readChunks() { return this.readChunks; } public FileIOStat.Summary fileWrites() { return this.fileWrites; } public FileIOStat.Summary fileReads() { return this.fileReads; } public List<ChunkGenStat> chunkGenStats() { return this.chunkGenStats; } public List<StructureGenStat> structureGenStats() { return this.structureGenStats; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Pair<ChunkStatus, TimedStatSummary<ChunkGenStat>>> chunkGenSummary() {
/* 47 */     Map<ChunkStatus, List<ChunkGenStat>> byStatus = (Map)this.chunkGenStats.stream().collect(Collectors.groupingBy(ChunkGenStat::status));
/* 48 */     return byStatus.entrySet().stream()
/* 49 */       .map(e -> Pair.of((ChunkStatus)e.getKey(), TimedStatSummary.summary((List)e.getValue())))
/* 50 */       .filter(pair -> ((Optional)pair.getSecond()).isPresent())
/* 51 */       .map(e -> Pair.of((ChunkStatus)e.getFirst(), (TimedStatSummary)((Optional)e.getSecond()).get()))
/* 52 */       .sorted(Comparator.comparing(pair -> ((TimedStatSummary)pair.getSecond()).totalDuration()).reversed())
/* 53 */       .toList();
/*    */   }
/*    */ 
/*    */   
/* 57 */   public String asJson() { return (new JfrResultJsonSerializer()).format(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\parse\JfrStatsResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
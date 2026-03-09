/*     */ package net.minecraft.server.packs;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.io.IOException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Instant;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.HttpUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.eventlog.JsonEventLog;
/*     */ import net.minecraft.util.thread.ConsecutiveExecutor;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DownloadQueue implements AutoCloseable {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final int MAX_KEPT_PACKS = 20; private final Path cacheDir; private final JsonEventLog<LogEntry> eventLog; private final ConsecutiveExecutor tasks;
/*     */   private static final class FileInfoEntry extends Record { private final String name;
/*     */     private final long size;
/*     */     
/*  38 */     private FileInfoEntry(String name, long size) { this.name = name; this.size = size; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #38	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  38 */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry; } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #38	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #38	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;
/*  38 */       //   0	8	1	o	Ljava/lang/Object; } public long size() { return this.size; }
/*     */ 
/*     */ 
/*     */     
/*  42 */     public static final Codec<FileInfoEntry> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.STRING
/*  43 */           .fieldOf("name").forGetter(FileInfoEntry::name), Codec.LONG
/*  44 */           .fieldOf("size").forGetter(FileInfoEntry::size))
/*  45 */         .apply(i, FileInfoEntry::new)); }
/*     */   private static final class LogEntry extends Record { private final UUID id; private final String url; private final Instant time; private final Optional<String> hash; private final Either<String, DownloadQueue.FileInfoEntry> errorOrFileInfo;
/*     */     
/*  48 */     private LogEntry(UUID id, String url, Instant time, Optional<String> hash, Either<String, DownloadQueue.FileInfoEntry> errorOrFileInfo) { this.id = id; this.url = url; this.time = time; this.hash = hash; this.errorOrFileInfo = errorOrFileInfo; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadQueue$LogEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$LogEntry; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadQueue$LogEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$LogEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadQueue$LogEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/DownloadQueue$LogEntry;
/*  48 */       //   0	8	1	o	Ljava/lang/Object; } public UUID id() { return this.id; } public String url() { return this.url; } public Instant time() { return this.time; } public Optional<String> hash() { return this.hash; } public Either<String, DownloadQueue.FileInfoEntry> errorOrFileInfo() { return this.errorOrFileInfo; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  55 */     public static final Codec<LogEntry> CODEC = RecordCodecBuilder.create(i -> i.group(UUIDUtil.STRING_CODEC
/*  56 */           .fieldOf("id").forGetter(LogEntry::id), Codec.STRING
/*  57 */           .fieldOf("url").forGetter(LogEntry::url), ExtraCodecs.INSTANT_ISO8601
/*  58 */           .fieldOf("time").forGetter(LogEntry::time), Codec.STRING
/*  59 */           .optionalFieldOf("hash").forGetter(LogEntry::hash), 
/*  60 */           Codec.mapEither(Codec.STRING.fieldOf("error"), DownloadQueue.FileInfoEntry.CODEC.fieldOf("file")).forGetter(LogEntry::errorOrFileInfo))
/*  61 */         .apply(i, LogEntry::new)); }
/*     */   public static final class BatchResult extends Record { private final Map<UUID, Path> downloaded; private final Set<UUID> failed;
/*     */     
/*  64 */     public Set<UUID> failed() { return this.failed; } public Map<UUID, Path> downloaded() { return this.downloaded; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadQueue$BatchResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/DownloadQueue$BatchResult;
/*     */       //   0	8	1	o	Ljava/lang/Object; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadQueue$BatchResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$BatchResult; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadQueue$BatchResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  64 */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$BatchResult; } public BatchResult(Map<UUID, Path> downloaded, Set<UUID> failed) { this.downloaded = downloaded; this.failed = failed; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     public BatchResult() throws IOException { this(new HashMap(), new HashSet()); } }
/*     */ 
/*     */   
/*     */   public static final class DownloadRequest extends Record {
/*     */     private final URL url;
/*     */     private final HashCode hash;
/*     */     
/*  76 */     public DownloadRequest(URL url, HashCode hash) { this.url = url; this.hash = hash; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;
/*  76 */       //   0	8	1	o	Ljava/lang/Object; } public URL url() { return this.url; } public HashCode hash() { return this.hash; } }
/*     */   public static final class BatchConfig extends Record { private final HashFunction hashFunction; private final int maxSize; private final Map<String, String> headers;
/*     */     private final Proxy proxy;
/*     */     private final HttpUtil.DownloadProgressListener listener;
/*     */     
/*  81 */     public BatchConfig(HashFunction hashFunction, int maxSize, Map<String, String> headers, Proxy proxy, HttpUtil.DownloadProgressListener listener) { this.hashFunction = hashFunction; this.maxSize = maxSize; this.headers = headers; this.proxy = proxy; this.listener = listener; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #81	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$BatchConfig; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #81	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$BatchConfig; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #81	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;
/*  81 */       //   0	8	1	o	Ljava/lang/Object; } public HashFunction hashFunction() { return this.hashFunction; } public int maxSize() { return this.maxSize; } public Map<String, String> headers() { return this.headers; } public Proxy proxy() { return this.proxy; } public HttpUtil.DownloadProgressListener listener() { return this.listener; } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DownloadQueue(Path cacheDir) throws IOException {
/*  91 */     this.tasks = new ConsecutiveExecutor(Util.nonCriticalIoPool(), "download-queue");
/*     */ 
/*     */     
/*  94 */     this.cacheDir = cacheDir;
/*  95 */     FileUtil.createDirectoriesSafe(cacheDir);
/*  96 */     this.eventLog = JsonEventLog.open(LogEntry.CODEC, cacheDir.resolve("log.json"));
/*     */     
/*  98 */     DownloadCacheCleaner.vacuumCacheDir(cacheDir, 20);
/*     */   }
/*     */   
/*     */   private BatchResult runDownload(BatchConfig config, Map<UUID, DownloadRequest> requests) {
/* 102 */     BatchResult result = new BatchResult();
/* 103 */     requests.forEach((id, request) -> {
/* 104 */           Path targetDir = this.cacheDir.resolve(id.toString());
/* 105 */           Path downloadedFile = null;
/*     */           try {
/* 107 */             downloadedFile = HttpUtil.downloadFile(targetDir, request.url, config.headers, config.hashFunction, request.hash, config.maxSize, config.proxy, config.listener);
/* 108 */             result.downloaded.put(id, downloadedFile);
/* 109 */           } catch (Exception e) {
/* 110 */             LOGGER.error("Failed to download {}", request.url, e);
/* 111 */             result.failed.add(id);
/*     */           } 
/*     */           try {
/* 114 */             this.eventLog.write(new LogEntry(id, request.url
/*     */                   
/* 116 */                   .toString(), 
/* 117 */                   Instant.now(), 
/* 118 */                   Optional.ofNullable(request.hash).map(HashCode::toString), 
/* 119 */                   (downloadedFile != null) ? getFileInfo(downloadedFile) : Either.left("download_failed")));
/*     */           }
/* 121 */           catch (Exception e) {
/* 122 */             LOGGER.error("Failed to log download of {}", request.url, e);
/*     */           } 
/*     */         });
/* 125 */     return result;
/*     */   }
/*     */   
/*     */   private Either<String, FileInfoEntry> getFileInfo(Path downloadedFile) {
/*     */     try {
/* 130 */       long size = Files.size(downloadedFile);
/* 131 */       Path relativePath = this.cacheDir.relativize(downloadedFile);
/* 132 */       return Either.right(new FileInfoEntry(relativePath.toString(), size));
/* 133 */     } catch (IOException e) {
/* 134 */       LOGGER.error("Failed to get file size of {}", downloadedFile, e);
/* 135 */       return Either.left("no_access");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 140 */   public CompletableFuture<BatchResult> downloadBatch(BatchConfig config, Map<UUID, DownloadRequest> requests) { Objects.requireNonNull(this.tasks); return CompletableFuture.supplyAsync(() -> runDownload(config, requests), this.tasks::schedule); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 145 */     this.tasks.close();
/* 146 */     this.eventLog.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\DownloadQueue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
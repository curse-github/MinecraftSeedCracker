/*     */ package net.minecraft.data;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.Hashing;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.WorldVersion;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class HashCache {
/*     */   private static final String HEADER_MARKER = "// ";
/*     */   private final Path rootDir;
/*     */   private final Path cacheDir;
/*     */   private final String versionId;
/*     */   private final Map<String, ProviderCache> caches;
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger(); private final Set<String> cachesToWrite; private final Set<Path> cachePaths; private final int initialCount; private int writes;
/*     */   private static final class ProviderCache extends Record { private final String version; private final ImmutableMap<Path, HashCode> data;
/*     */     
/*  38 */     private ProviderCache(String version, ImmutableMap<Path, HashCode> data) { this.version = version; this.data = data; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/HashCache$ProviderCache;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #38	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/HashCache$ProviderCache; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/HashCache$ProviderCache;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #38	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/HashCache$ProviderCache; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/HashCache$ProviderCache;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #38	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/data/HashCache$ProviderCache;
/*  38 */       //   0	8	1	o	Ljava/lang/Object; } public String version() { return this.version; } public ImmutableMap<Path, HashCode> data() { return this.data; }
/*     */     
/*  40 */     public HashCode get(Path path) { return (HashCode)this.data.get(path); }
/*     */ 
/*     */ 
/*     */     
/*  44 */     public int count() { return this.data.size(); }
/*     */     
/*     */     public static ProviderCache load(Path rootDir, Path cacheFile)
/*     */     {
/*  48 */       BufferedReader reader = Files.newBufferedReader(cacheFile, StandardCharsets.UTF_8); 
/*  49 */       try { String header = reader.readLine();
/*  50 */         if (!header.startsWith("// ")) {
/*  51 */           throw new IllegalStateException("Missing cache file header");
/*     */         }
/*  53 */         String[] headerFields = header.substring("// ".length()).split("\t", 2);
/*  54 */         String savedVersionId = headerFields[0];
/*  55 */         ImmutableMap.Builder<Path, HashCode> result = ImmutableMap.builder();
/*  56 */         reader.lines().forEach(s -> {
/*  57 */               int i = s.indexOf(' ');
/*  58 */               result.put(rootDir.resolve(s.substring(i + 1)), HashCode.fromString(s.substring(0, i)));
/*     */             });
/*  60 */         ProviderCache providerCache = new ProviderCache(savedVersionId, result.build());
/*  61 */         if (reader != null) reader.close();  return providerCache; } catch (Throwable throwable) { if (reader != null)
/*     */           try { reader.close(); }
/*     */           catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */             throw throwable; }
/*  65 */        } public void save(Path rootDir, Path cacheFile, String extraHeaderInfo) { try { BufferedWriter output = Files.newBufferedWriter(cacheFile, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]); 
/*  66 */         try { output.write("// ");
/*  67 */           output.write(this.version);
/*  68 */           output.write(9);
/*  69 */           output.write(extraHeaderInfo);
/*  70 */           output.newLine();
/*  71 */           for (UnmodifiableIterator unmodifiableIterator = this.data.entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<Path, HashCode> e = (Map.Entry)unmodifiableIterator.next();
/*  72 */             output.write(((HashCode)e.getValue()).toString());
/*  73 */             output.write(32);
/*  74 */             output.write(rootDir.relativize((Path)e.getKey()).toString());
/*  75 */             output.newLine(); }
/*     */           
/*  77 */           if (output != null) output.close();  } catch (Throwable throwable) { if (output != null) try { output.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/*  78 */       { HashCache.LOGGER.warn("Unable write cachefile {}: {}", cacheFile, e); }
/*     */        }
/*     */      }
/*     */   private static final class ProviderCacheBuilder extends Record { private final String version; private final ConcurrentMap<Path, HashCode> data;
/*     */     
/*  83 */     private ProviderCacheBuilder(String version, ConcurrentMap<Path, HashCode> data) { this.version = version; this.data = data; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/HashCache$ProviderCacheBuilder;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #83	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/HashCache$ProviderCacheBuilder; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/HashCache$ProviderCacheBuilder;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #83	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/HashCache$ProviderCacheBuilder; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/HashCache$ProviderCacheBuilder;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #83	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/data/HashCache$ProviderCacheBuilder;
/*  83 */       //   0	8	1	o	Ljava/lang/Object; } public String version() { return this.version; } public ConcurrentMap<Path, HashCode> data() { return this.data; }
/*     */     
/*  85 */     ProviderCacheBuilder(String version) { this(version, new ConcurrentHashMap()); }
/*     */ 
/*     */ 
/*     */     
/*  89 */     public void put(Path path, HashCode hash) { this.data.put(path, hash); }
/*     */ 
/*     */ 
/*     */     
/*  93 */     public HashCache.ProviderCache build() { return new HashCache.ProviderCache(this.version, ImmutableMap.copyOf(this.data)); } }
/*     */   
/*     */   private static class CacheUpdater implements CachedOutput { private final String provider;
/*     */     private final HashCache.ProviderCache oldCache;
/*     */     private final HashCache.ProviderCacheBuilder newCache;
/*     */     private final AtomicInteger writes;
/*     */     
/*     */     private CacheUpdater(String provider, String newVersionId, HashCache.ProviderCache oldCache) {
/* 101 */       this.writes = new AtomicInteger();
/*     */ 
/*     */ 
/*     */       
/* 105 */       this.provider = provider;
/* 106 */       this.oldCache = oldCache;
/* 107 */       this.newCache = new HashCache.ProviderCacheBuilder(newVersionId);
/*     */     }
/*     */ 
/*     */     
/* 111 */     private boolean shouldWrite(Path path, HashCode hash) { return (!Objects.equals(this.oldCache.get(path), hash) || !Files.exists(path, new java.nio.file.LinkOption[0])); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void writeIfNeeded(Path path, byte[] input, HashCode hash) throws IOException {
/* 116 */       if (this.closed) {
/* 117 */         throw new IllegalStateException("Cannot write to cache as it has already been closed");
/*     */       }
/* 119 */       if (shouldWrite(path, hash)) {
/* 120 */         this.writes.incrementAndGet();
/* 121 */         Files.createDirectories(path.getParent(), new java.nio.file.attribute.FileAttribute[0]);
/* 122 */         Files.write(path, input, new java.nio.file.OpenOption[0]);
/*     */       } 
/* 124 */       this.newCache.put(path, hash);
/*     */     }
/*     */     
/*     */     public HashCache.UpdateResult close() {
/* 128 */       this.closed = true;
/* 129 */       return new HashCache.UpdateResult(this.provider, this.newCache.build(), this.writes.get());
/*     */     } }
/*     */   public static final class UpdateResult extends Record { private final String providerId; private final HashCache.ProviderCache cache; private final int writes;
/*     */     
/* 133 */     public UpdateResult(String providerId, HashCache.ProviderCache cache, int writes) { this.providerId = providerId; this.cache = cache; this.writes = writes; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/HashCache$UpdateResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #133	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/HashCache$UpdateResult; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/HashCache$UpdateResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #133	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/HashCache$UpdateResult; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/HashCache$UpdateResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #133	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/data/HashCache$UpdateResult;
/* 133 */       //   0	8	1	o	Ljava/lang/Object; } public String providerId() { return this.providerId; } public HashCache.ProviderCache cache() { return this.cache; } public int writes() { return this.writes; } }
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
/* 147 */   private Path getProviderCachePath(String provider) { return this.cacheDir.resolve(Hashing.sha1().hashString(provider, StandardCharsets.UTF_8).toString()); }
/*     */   public HashCache(Path rootDir, Collection<String> providerIds, WorldVersion version) throws IOException {
/*     */     this.cachesToWrite = new HashSet();
/*     */     this.cachePaths = new HashSet();
/* 151 */     this.versionId = version.id();
/* 152 */     this.rootDir = rootDir;
/* 153 */     this.cacheDir = rootDir.resolve(".cache");
/* 154 */     Files.createDirectories(this.cacheDir, new java.nio.file.attribute.FileAttribute[0]);
/*     */     
/* 156 */     Map<String, ProviderCache> loadedCaches = new HashMap<String, ProviderCache>();
/* 157 */     int initialCount = 0;
/* 158 */     for (String providerId : providerIds) {
/* 159 */       Path providerCachePath = getProviderCachePath(providerId);
/* 160 */       this.cachePaths.add(providerCachePath);
/* 161 */       ProviderCache providerCache = readCache(rootDir, providerCachePath);
/* 162 */       loadedCaches.put(providerId, providerCache);
/* 163 */       initialCount += providerCache.count();
/*     */     } 
/* 165 */     this.caches = loadedCaches;
/* 166 */     this.initialCount = initialCount;
/*     */   }
/*     */   
/*     */   private static ProviderCache readCache(Path rootDir, Path providerCachePath) {
/* 170 */     if (Files.isReadable(providerCachePath)) {
/*     */       try {
/* 172 */         return ProviderCache.load(rootDir, providerCachePath);
/* 173 */       } catch (Exception e) {
/* 174 */         LOGGER.warn("Failed to parse cache {}, discarding", providerCachePath, e);
/*     */       } 
/*     */     }
/* 177 */     return new ProviderCache("unknown", ImmutableMap.of());
/*     */   }
/*     */   
/*     */   public boolean shouldRunInThisVersion(String providerId) {
/* 181 */     ProviderCache result = (ProviderCache)this.caches.get(providerId);
/* 182 */     return (result == null || !result.version.equals(this.versionId));
/*     */   }
/*     */   
/*     */   public CompletableFuture<UpdateResult> generateUpdate(String providerId, UpdateFunction function) {
/* 186 */     ProviderCache existingCache = (ProviderCache)this.caches.get(providerId);
/* 187 */     if (existingCache == null) {
/* 188 */       throw new IllegalStateException("Provider not registered: " + providerId);
/*     */     }
/* 190 */     CacheUpdater output = new CacheUpdater(providerId, this.versionId, existingCache);
/* 191 */     return function.update(output).thenApply(unused -> output.close());
/*     */   }
/*     */   
/*     */   public void applyUpdate(UpdateResult result) {
/* 195 */     this.caches.put(result.providerId(), result.cache());
/* 196 */     this.cachesToWrite.add(result.providerId());
/* 197 */     this.writes += result.writes();
/*     */   }
/*     */   
/*     */   public void purgeStaleAndWrite() throws IOException {
/* 201 */     final Set<Path> allowedFiles = new HashSet<Path>();
/* 202 */     this.caches.forEach((providerId, cache) -> {
/* 203 */           if (this.cachesToWrite.contains(providerId)) {
/* 204 */             Path cachePath = getProviderCachePath(providerId);
/* 205 */             cache.save(this.rootDir, cachePath, DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(ZonedDateTime.now()) + "\t" + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(ZonedDateTime.now()));
/*     */           } 
/* 207 */           allowedFiles.addAll(cache.data().keySet());
/*     */         });
/*     */     
/* 210 */     allowedFiles.add(this.rootDir.resolve("version.json"));
/*     */     
/* 212 */     final MutableInt found = new MutableInt();
/* 213 */     final MutableInt removed = new MutableInt();
/* 214 */     Files.walkFileTree(this.rootDir, new SimpleFileVisitor<Path>()
/*     */         {
/*     */           public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
/* 217 */             if (HashCache.this.cachePaths.contains(file)) {
/* 218 */               return FileVisitResult.CONTINUE;
/*     */             }
/* 220 */             found.increment();
/* 221 */             if (allowedFiles.contains(file)) {
/* 222 */               return FileVisitResult.CONTINUE;
/*     */             }
/*     */             try {
/* 225 */               Files.delete(file);
/* 226 */             } catch (IOException e) {
/* 227 */               HashCache.LOGGER.warn("Failed to delete file {}", file, e);
/*     */             } 
/* 229 */             removed.increment();
/* 230 */             return FileVisitResult.CONTINUE;
/*     */           }
/*     */         });
/* 233 */     LOGGER.info("Caching: total files: {}, old count: {}, new count: {}, removed stale: {}, written: {}", new Object[] { found, Integer.valueOf(this.initialCount), Integer.valueOf(allowedFiles.size()), removed, Integer.valueOf(this.writes) });
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface UpdateFunction {
/*     */     CompletableFuture<?> update(CachedOutput param1CachedOutput);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\HashCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
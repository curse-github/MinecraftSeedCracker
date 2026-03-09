/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import org.slf4j.Logger;
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
/*     */ public class RegionFile
/*     */   implements AutoCloseable
/*     */ {
/*  85 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int SECTOR_BYTES = 4096;
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static final int SECTOR_INTS = 1024;
/*     */   
/*     */   private static final int CHUNK_HEADER_SIZE = 5;
/*     */   private static final int HEADER_OFFSET = 0;
/*  94 */   private static final ByteBuffer PADDING_BUFFER = ByteBuffer.allocateDirect(1);
/*     */   
/*     */   private static final String EXTERNAL_FILE_EXTENSION = ".mcc";
/*     */   
/*     */   private static final int EXTERNAL_STREAM_FLAG = 128;
/*     */   
/*     */   private static final int EXTERNAL_CHUNK_THRESHOLD = 256;
/*     */   
/*     */   private static final int CHUNK_NOT_PRESENT = 0;
/*     */   
/*     */   private final RegionStorageInfo info;
/*     */   private final Path path;
/*     */   private final FileChannel file;
/*     */   private final Path externalFileDir;
/*     */   private final RegionFileVersion version;
/*     */   private final ByteBuffer header;
/*     */   private final IntBuffer offsets;
/*     */   private final IntBuffer timestamps;
/*     */   @VisibleForTesting
/*     */   protected final RegionBitmap usedSectors;
/*     */   
/* 115 */   public RegionFile(RegionStorageInfo info, Path path, Path externalFileDir, boolean sync) throws IOException { this(info, path, externalFileDir, RegionFileVersion.getSelected(), sync); }
/*     */   public RegionFile(RegionStorageInfo info, Path path, Path externalFileDir, RegionFileVersion version, boolean sync) throws IOException {
/*     */     this.header = ByteBuffer.allocateDirect(8192);
/*     */     this.usedSectors = new RegionBitmap();
/* 119 */     this.info = info;
/* 120 */     this.path = path;
/* 121 */     this.version = version;
/* 122 */     if (!Files.isDirectory(externalFileDir, new java.nio.file.LinkOption[0])) {
/* 123 */       throw new IllegalArgumentException("Expected directory, got " + String.valueOf(externalFileDir.toAbsolutePath()));
/*     */     }
/* 125 */     this.externalFileDir = externalFileDir;
/* 126 */     this.offsets = this.header.asIntBuffer();
/* 127 */     this.offsets.limit(1024);
/* 128 */     this.header.position(4096);
/* 129 */     this.timestamps = this.header.asIntBuffer();
/*     */     
/* 131 */     if (sync) {
/* 132 */       this.file = FileChannel.open(path, new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.DSYNC });
/*     */     } else {
/* 134 */       this.file = FileChannel.open(path, new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE });
/*     */     } 
/*     */ 
/*     */     
/* 138 */     this.usedSectors.force(0, 2);
/*     */     
/* 140 */     this.header.position(0);
/* 141 */     int readHeaderBytes = this.file.read(this.header, 0L);
/* 142 */     if (readHeaderBytes != -1) {
/* 143 */       if (readHeaderBytes != 8192) {
/* 144 */         LOGGER.warn("Region file {} has truncated header: {}", path, Integer.valueOf(readHeaderBytes));
/*     */       }
/*     */       
/* 147 */       long size = Files.size(path);
/* 148 */       for (int i = 0; i < 1024; i++) {
/* 149 */         int offset = this.offsets.get(i);
/* 150 */         if (offset != 0) {
/* 151 */           int sectorNumber = getSectorNumber(offset);
/* 152 */           int numSectors = getNumSectors(offset);
/* 153 */           if (sectorNumber < 2) {
/* 154 */             LOGGER.warn("Region file {} has invalid sector at index: {}; sector {} overlaps with header", new Object[] { path, Integer.valueOf(i), Integer.valueOf(sectorNumber) });
/* 155 */             this.offsets.put(i, 0);
/* 156 */           } else if (numSectors == 0) {
/* 157 */             LOGGER.warn("Region file {} has an invalid sector at index: {}; size has to be > 0", path, Integer.valueOf(i));
/* 158 */             this.offsets.put(i, 0);
/* 159 */           } else if (sectorNumber * 4096L > size) {
/* 160 */             LOGGER.warn("Region file {} has an invalid sector at index: {}; sector {} is out of bounds", new Object[] { path, Integer.valueOf(i), Integer.valueOf(sectorNumber) });
/* 161 */             this.offsets.put(i, 0);
/*     */           } else {
/* 163 */             this.usedSectors.force(sectorNumber, numSectors);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 171 */   public Path getPath() { return this.path; }
/*     */ 
/*     */   
/*     */   private Path getExternalChunkPath(ChunkPos pos) {
/* 175 */     String externalFileName = "c." + pos.x + "." + pos.z + ".mcc";
/* 176 */     return this.externalFileDir.resolve(externalFileName);
/*     */   }
/*     */   
/*     */   public DataInputStream getChunkDataInputStream(ChunkPos pos) throws IOException {
/* 180 */     int offset = getOffset(pos);
/* 181 */     if (offset == 0) {
/* 182 */       return null;
/*     */     }
/*     */     
/* 185 */     int sectorNumber = getSectorNumber(offset);
/* 186 */     int numSectors = getNumSectors(offset);
/*     */     
/* 188 */     int sectorsLength = numSectors * 4096;
/* 189 */     ByteBuffer buffer = ByteBuffer.allocate(sectorsLength);
/* 190 */     this.file.read(buffer, (sectorNumber * 4096));
/* 191 */     buffer.flip();
/*     */     
/* 193 */     if (buffer.remaining() < 5) {
/* 194 */       LOGGER.error("Chunk {} header is truncated: expected {} but read {}", new Object[] { pos, Integer.valueOf(sectorsLength), Integer.valueOf(buffer.remaining()) });
/* 195 */       return null;
/*     */     } 
/*     */     
/* 198 */     int length = buffer.getInt();
/* 199 */     byte versionId = buffer.get();
/*     */     
/* 201 */     if (length == 0) {
/* 202 */       LOGGER.warn("Chunk {} is allocated, but stream is missing", pos);
/* 203 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 207 */     int streamLength = length - 1;
/*     */     
/* 209 */     if (isExternalStreamChunk(versionId)) {
/* 210 */       if (streamLength != 0) {
/* 211 */         LOGGER.warn("Chunk has both internal and external streams");
/*     */       }
/* 213 */       return createExternalChunkInputStream(pos, getExternalChunkVersion(versionId));
/*     */     } 
/*     */     
/* 216 */     if (streamLength > buffer.remaining()) {
/* 217 */       LOGGER.error("Chunk {} stream is truncated: expected {} but read {}", new Object[] { pos, Integer.valueOf(streamLength), Integer.valueOf(buffer.remaining()) });
/* 218 */       return null;
/*     */     } 
/*     */     
/* 221 */     if (streamLength < 0) {
/* 222 */       LOGGER.error("Declared size {} of chunk {} is negative", Integer.valueOf(length), pos);
/* 223 */       return null;
/*     */     } 
/*     */     
/* 226 */     JvmProfiler.INSTANCE.onRegionFileRead(this.info, pos, this.version, streamLength);
/*     */     
/* 228 */     return createChunkInputStream(pos, versionId, createStream(buffer, streamLength));
/*     */   }
/*     */ 
/*     */   
/* 232 */   private static int getTimestamp() { return (int)(Util.getEpochMillis() / 1000L); }
/*     */ 
/*     */ 
/*     */   
/* 236 */   private static boolean isExternalStreamChunk(byte version) { return ((version & 0x80) != 0); }
/*     */ 
/*     */ 
/*     */   
/* 240 */   private static byte getExternalChunkVersion(byte version) { return (byte)(version & 0xFFFFFF7F); }
/*     */ 
/*     */   
/*     */   private DataInputStream createChunkInputStream(ChunkPos pos, byte versionId, InputStream chunkStream) throws IOException {
/* 244 */     RegionFileVersion version = RegionFileVersion.fromId(versionId);
/*     */     
/* 246 */     if (version == RegionFileVersion.VERSION_CUSTOM) {
/* 247 */       String type = (new DataInputStream(chunkStream)).readUTF();
/* 248 */       Identifier id = Identifier.tryParse(type);
/* 249 */       if (id != null) {
/* 250 */         LOGGER.error("Unrecognized custom compression {}", id);
/* 251 */         return null;
/*     */       } 
/* 253 */       LOGGER.error("Invalid custom compression id {}", type);
/* 254 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 258 */     if (version == null) {
/* 259 */       LOGGER.error("Chunk {} has invalid chunk stream version {}", pos, Byte.valueOf(versionId));
/* 260 */       return null;
/*     */     } 
/* 262 */     return new DataInputStream(version.wrap(chunkStream));
/*     */   }
/*     */   
/*     */   private DataInputStream createExternalChunkInputStream(ChunkPos pos, byte versionId) throws IOException {
/* 266 */     Path externalFile = getExternalChunkPath(pos);
/* 267 */     if (!Files.isRegularFile(externalFile, new java.nio.file.LinkOption[0])) {
/* 268 */       LOGGER.error("External chunk path {} is not file", externalFile);
/* 269 */       return null;
/*     */     } 
/*     */     
/* 272 */     return createChunkInputStream(pos, versionId, Files.newInputStream(externalFile, new OpenOption[0]));
/*     */   }
/*     */ 
/*     */   
/* 276 */   private static ByteArrayInputStream createStream(ByteBuffer buffer, int length) { return new ByteArrayInputStream(buffer.array(), buffer.position(), length); }
/*     */ 
/*     */ 
/*     */   
/* 280 */   private int packSectorOffset(int index, int size) { return index << 8 | size; }
/*     */ 
/*     */ 
/*     */   
/* 284 */   private static int getNumSectors(int offset) { return offset & 0xFF; }
/*     */ 
/*     */ 
/*     */   
/* 288 */   private static int getSectorNumber(int offset) { return offset >> 8 & 0xFFFFFF; }
/*     */ 
/*     */ 
/*     */   
/* 292 */   private static int sizeToSectors(int size) { return (size + 4096 - 1) / 4096; }
/*     */ 
/*     */   
/*     */   public boolean doesChunkExist(ChunkPos pos) {
/* 296 */     int offset = getOffset(pos);
/* 297 */     if (offset == 0) {
/* 298 */       return false;
/*     */     }
/*     */     
/* 301 */     int sectorNumber = getSectorNumber(offset);
/* 302 */     int numSectors = getNumSectors(offset);
/*     */     
/* 304 */     ByteBuffer streamHeader = ByteBuffer.allocate(5);
/*     */     try {
/* 306 */       this.file.read(streamHeader, (sectorNumber * 4096));
/* 307 */       streamHeader.flip();
/* 308 */       if (streamHeader.remaining() != 5) {
/* 309 */         return false;
/*     */       }
/*     */       
/* 312 */       int length = streamHeader.getInt();
/* 313 */       byte versionId = streamHeader.get();
/* 314 */       if (isExternalStreamChunk(versionId)) {
/* 315 */         if (!RegionFileVersion.isValidVersion(getExternalChunkVersion(versionId))) {
/* 316 */           return false;
/*     */         }
/*     */         
/* 319 */         if (!Files.isRegularFile(getExternalChunkPath(pos), new java.nio.file.LinkOption[0])) {
/* 320 */           return false;
/*     */         }
/*     */       } else {
/* 323 */         if (!RegionFileVersion.isValidVersion(versionId)) {
/* 324 */           return false;
/*     */         }
/*     */         
/* 327 */         if (length == 0) {
/* 328 */           return false;
/*     */         }
/*     */         
/* 331 */         int streamLength = length - 1;
/* 332 */         if (streamLength < 0 || streamLength > 4096 * numSectors) {
/* 333 */           return false;
/*     */         }
/*     */       } 
/* 336 */     } catch (IOException e) {
/* 337 */       return false;
/*     */     } 
/*     */     
/* 340 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 344 */   public DataOutputStream getChunkDataOutputStream(ChunkPos pos) throws IOException { return new DataOutputStream(this.version.wrap(new ChunkBuffer(this, pos))); }
/*     */ 
/*     */ 
/*     */   
/* 348 */   public void flush() throws IOException { this.file.force(true); }
/*     */ 
/*     */   
/*     */   public void clear(ChunkPos pos) throws IOException {
/* 352 */     int offsetIndex = getOffsetIndex(pos);
/* 353 */     int offset = this.offsets.get(offsetIndex);
/* 354 */     if (offset == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 358 */     this.offsets.put(offsetIndex, 0);
/* 359 */     this.timestamps.put(offsetIndex, getTimestamp());
/* 360 */     writeHeader();
/*     */     
/* 362 */     Files.deleteIfExists(getExternalChunkPath(pos));
/* 363 */     this.usedSectors.free(getSectorNumber(offset), getNumSectors(offset));
/*     */   }
/*     */ 
/*     */   
/*     */   private class ChunkBuffer
/*     */     extends ByteArrayOutputStream
/*     */   {
/*     */     private final ChunkPos pos;
/*     */ 
/*     */     
/*     */     public ChunkBuffer(ChunkPos pos) {
/* 374 */       super(8096);
/*     */ 
/*     */       
/* 377 */       write(0);
/* 378 */       write(0);
/* 379 */       write(0);
/* 380 */       write(0);
/*     */       
/* 382 */       write(RegionFile.this.version.getId());
/* 383 */       this.pos = pos;
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 388 */       ByteBuffer result = ByteBuffer.wrap(this.buf, 0, this.count);
/*     */       
/* 390 */       int streamLength = this.count - 5 + 1;
/* 391 */       JvmProfiler.INSTANCE.onRegionFileWrite(RegionFile.this.info, this.pos, RegionFile.this.version, streamLength);
/* 392 */       result.putInt(0, streamLength);
/* 393 */       RegionFile.this.write(this.pos, result);
/*     */     } }
/*     */   
/*     */   protected void write(ChunkPos pos, ByteBuffer data) throws IOException {
/*     */     CommitOp commitOp;
/* 398 */     int newSectorNumber, offsetIndex = getOffsetIndex(pos);
/* 399 */     int offset = this.offsets.get(offsetIndex);
/* 400 */     int sectorNumber = getSectorNumber(offset);
/* 401 */     int currentSectorCount = getNumSectors(offset);
/*     */     
/* 403 */     int dataSize = data.remaining();
/* 404 */     int sectorsNeeded = sizeToSectors(dataSize);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 409 */     if (sectorsNeeded >= 256) {
/* 410 */       Path externalChunkPath = getExternalChunkPath(pos);
/* 411 */       LOGGER.warn("Saving oversized chunk {} ({} bytes} to external file {}", new Object[] { pos, Integer.valueOf(dataSize), externalChunkPath });
/* 412 */       sectorsNeeded = 1;
/* 413 */       newSectorNumber = this.usedSectors.allocate(sectorsNeeded);
/* 414 */       commitOp = writeToExternalFile(externalChunkPath, data);
/* 415 */       ByteBuffer stub = createExternalStub();
/* 416 */       this.file.write(stub, (newSectorNumber * 4096));
/*     */     } else {
/* 418 */       newSectorNumber = this.usedSectors.allocate(sectorsNeeded);
/* 419 */       commitOp = (() -> Files.deleteIfExists(getExternalChunkPath(pos)));
/* 420 */       this.file.write(data, (newSectorNumber * 4096));
/*     */     } 
/*     */     
/* 423 */     this.offsets.put(offsetIndex, packSectorOffset(newSectorNumber, sectorsNeeded));
/* 424 */     this.timestamps.put(offsetIndex, getTimestamp());
/* 425 */     writeHeader();
/*     */     
/* 427 */     commitOp.run();
/*     */     
/* 429 */     if (sectorNumber != 0) {
/* 430 */       this.usedSectors.free(sectorNumber, currentSectorCount);
/*     */     }
/*     */   }
/*     */   
/*     */   private ByteBuffer createExternalStub() {
/* 435 */     ByteBuffer stub = ByteBuffer.allocate(5);
/* 436 */     stub.putInt(1);
/* 437 */     stub.put((byte)(this.version.getId() | 0x80));
/* 438 */     stub.flip();
/* 439 */     return stub;
/*     */   }
/*     */   
/*     */   private CommitOp writeToExternalFile(Path path, ByteBuffer data) throws IOException {
/* 443 */     Path tmpPath = Files.createTempFile(this.externalFileDir, "tmp", null, new java.nio.file.attribute.FileAttribute[0]);
/* 444 */     FileChannel extFile = FileChannel.open(tmpPath, new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE }); 
/* 445 */     try { data.position(5);
/* 446 */       extFile.write(data);
/* 447 */       if (extFile != null) extFile.close();  } catch (Throwable throwable) { if (extFile != null)
/* 448 */         try { extFile.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  return () -> Files.move(tmpPath, path, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*     */   }
/*     */   
/*     */   private void writeHeader() throws IOException {
/* 452 */     this.header.position(0);
/* 453 */     this.file.write(this.header, 0L);
/*     */   }
/*     */ 
/*     */   
/* 457 */   private int getOffset(ChunkPos pos) { return this.offsets.get(getOffsetIndex(pos)); }
/*     */ 
/*     */ 
/*     */   
/* 461 */   public boolean hasChunk(ChunkPos pos) { return (getOffset(pos) != 0); }
/*     */ 
/*     */ 
/*     */   
/* 465 */   private static int getOffsetIndex(ChunkPos pos) { return pos.getRegionLocalX() + pos.getRegionLocalZ() * 32; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 471 */       padToFullSector();
/*     */     } finally {
/*     */       try {
/* 474 */         this.file.force(true);
/*     */       } finally {
/* 476 */         this.file.close();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void padToFullSector() throws IOException {
/* 484 */     int fileSize = (int)this.file.size();
/* 485 */     int paddedSize = sizeToSectors(fileSize) * 4096;
/* 486 */     if (fileSize != paddedSize) {
/* 487 */       ByteBuffer padding = PADDING_BUFFER.duplicate();
/* 488 */       padding.position(0);
/* 489 */       this.file.write(padding, (paddedSize - 1));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static interface CommitOp {
/*     */     void run() throws IOException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\RegionFile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
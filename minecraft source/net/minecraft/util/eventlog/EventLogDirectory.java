/*     */ package net.minecraft.util.eventlog;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileLock;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.time.LocalDate;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class EventLogDirectory
/*     */ {
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int COMPRESS_BUFFER_SIZE = 4096;
/*     */   
/*     */   private static final String COMPRESSED_EXTENSION = ".gz";
/*     */   
/*     */   private final Path root;
/*     */   private final String extension;
/*     */   
/*     */   private EventLogDirectory(Path root, String extension) {
/*  45 */     this.root = root;
/*  46 */     this.extension = extension;
/*     */   }
/*     */   
/*     */   public static EventLogDirectory open(Path root, String extension) throws IOException {
/*  50 */     Files.createDirectories(root, new java.nio.file.attribute.FileAttribute[0]);
/*  51 */     return new EventLogDirectory(root, extension);
/*     */   }
/*     */   
/*     */   public FileList listFiles() throws IOException {
/*  55 */     Stream<Path> list = Files.list(this.root);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     try { FileList fileList = new FileList(list.filter(x$0 -> Files.isRegularFile(x$0, new java.nio.file.LinkOption[0])).map(this::parseFile).filter(Objects::nonNull).toList());
/*     */       
/*  62 */       if (list != null) list.close();  return fileList; } catch (Throwable throwable) { if (list != null)
/*     */         try { list.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  66 */      } private File parseFile(Path path) { String fileName = path.getFileName().toString();
/*  67 */     int extensionIndex = fileName.indexOf('.');
/*  68 */     if (extensionIndex == -1) {
/*  69 */       return null;
/*     */     }
/*     */     
/*  72 */     FileId id = FileId.parse(fileName.substring(0, extensionIndex));
/*  73 */     if (id != null) {
/*  74 */       String extension = fileName.substring(extensionIndex);
/*  75 */       if (extension.equals(this.extension))
/*  76 */         return new RawFile(path, id); 
/*  77 */       if (extension.equals(this.extension + ".gz")) {
/*  78 */         return new CompressedFile(path, id);
/*     */       }
/*     */     } 
/*     */     
/*  82 */     return null; }
/*     */ 
/*     */   
/*     */   private static void tryCompress(Path raw, Path compressed) throws IOException {
/*  86 */     if (Files.exists(compressed, new java.nio.file.LinkOption[0])) {
/*  87 */       throw new IOException("Compressed target file already exists: " + String.valueOf(compressed));
/*     */     }
/*  89 */     FileChannel channel = FileChannel.open(raw, new OpenOption[] { StandardOpenOption.WRITE, StandardOpenOption.READ }); 
/*  90 */     try { FileLock lock = channel.tryLock();
/*  91 */       if (lock == null)
/*     */       {
/*  93 */         throw new IOException("Raw log file is already locked, cannot compress: " + String.valueOf(raw));
/*     */       }
/*  95 */       writeCompressed(channel, compressed);
/*     */       
/*  97 */       channel.truncate(0L);
/*  98 */       if (channel != null) channel.close();  } catch (Throwable throwable) { if (channel != null)
/*  99 */         try { channel.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  Files.delete(raw);
/*     */   }
/*     */   
/*     */   private static void writeCompressed(ReadableByteChannel channel, Path target) throws IOException {
/* 103 */     OutputStream output = new GZIPOutputStream(Files.newOutputStream(target, new OpenOption[0])); 
/* 104 */     try { byte[] bytes = new byte[4096];
/* 105 */       ByteBuffer buffer = ByteBuffer.wrap(bytes);
/* 106 */       while (channel.read(buffer) >= 0) {
/* 107 */         buffer.flip();
/* 108 */         output.write(bytes, 0, buffer.limit());
/* 109 */         buffer.clear();
/*     */       } 
/* 111 */       output.close(); }
/*     */     catch (Throwable throwable) { try { output.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 115 */      } public RawFile createNewFile(LocalDate date) throws IOException { FileId id; int index = 1;
/*     */     
/* 117 */     Set<FileId> files = listFiles().ids();
/*     */     do {
/* 119 */       id = new FileId(date, index++);
/* 120 */     } while (files.contains(id));
/* 121 */     RawFile file = new RawFile(this.root.resolve(id.toFileName(this.extension)), id);
/* 122 */     Files.createFile(file.path(), new java.nio.file.attribute.FileAttribute[0]);
/* 123 */     return file; }
/*     */ 
/*     */   
/*     */   public static class FileList
/*     */     extends Object implements Iterable<File> {
/*     */     private final List<EventLogDirectory.File> files;
/*     */     
/* 130 */     private FileList(List<EventLogDirectory.File> files) { this.files = new ArrayList(files); }
/*     */ 
/*     */     
/*     */     public FileList prune(LocalDate date, int expiryDays) {
/* 134 */       this.files.removeIf(file -> {
/* 135 */             EventLogDirectory.FileId id = file.id();
/* 136 */             LocalDate expiresAt = id.date().plusDays(expiryDays);
/* 137 */             if (!date.isBefore(expiresAt)) {
/*     */               try {
/* 139 */                 Files.delete(file.path());
/* 140 */                 return true;
/* 141 */               } catch (IOException e) {
/* 142 */                 EventLogDirectory.LOGGER.warn("Failed to delete expired event log file: {}", file.path(), e);
/*     */               } 
/*     */             }
/* 145 */             return false;
/*     */           });
/* 147 */       return this;
/*     */     }
/*     */     
/*     */     public FileList compressAll() throws IOException {
/* 151 */       ListIterator<EventLogDirectory.File> iterator = this.files.listIterator();
/* 152 */       while (iterator.hasNext()) {
/* 153 */         EventLogDirectory.File file = (EventLogDirectory.File)iterator.next();
/*     */         try {
/* 155 */           iterator.set(file.compress());
/* 156 */         } catch (IOException e) {
/* 157 */           EventLogDirectory.LOGGER.warn("Failed to compress event log file: {}", file.path(), e);
/*     */         } 
/*     */       } 
/* 160 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 165 */     public Iterator<EventLogDirectory.File> iterator() { return this.files.iterator(); }
/*     */ 
/*     */ 
/*     */     
/* 169 */     public Stream<EventLogDirectory.File> stream() { return this.files.stream(); }
/*     */ 
/*     */ 
/*     */     
/* 173 */     public Set<EventLogDirectory.FileId> ids() { return (Set)this.files.stream().map(EventLogDirectory.File::id).collect(Collectors.toSet()); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class RawFile
/*     */     extends Record
/*     */     implements File
/*     */   {
/*     */     private final Path path;
/*     */     
/*     */     private final EventLogDirectory.FileId id;
/*     */ 
/*     */     
/* 187 */     public RawFile(Path path, EventLogDirectory.FileId id) { this.path = path; this.id = id; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/eventlog/EventLogDirectory$RawFile;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/eventlog/EventLogDirectory$RawFile; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/eventlog/EventLogDirectory$RawFile;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/eventlog/EventLogDirectory$RawFile; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/eventlog/EventLogDirectory$RawFile;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/eventlog/EventLogDirectory$RawFile;
/* 187 */       //   0	8	1	o	Ljava/lang/Object; } public Path path() { return this.path; } public EventLogDirectory.FileId id() { return this.id; }
/*     */     
/* 189 */     public FileChannel openChannel() throws IOException { return FileChannel.open(this.path, new OpenOption[] { StandardOpenOption.WRITE, StandardOpenOption.READ }); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     public Reader openReader() throws IOException { return Files.exists(this.path, new java.nio.file.LinkOption[0]) ? Files.newBufferedReader(this.path) : null; }
/*     */ 
/*     */ 
/*     */     
/*     */     public EventLogDirectory.CompressedFile compress() throws IOException {
/* 199 */       Path compressedPath = this.path.resolveSibling(this.path.getFileName().toString() + ".gz");
/* 200 */       EventLogDirectory.tryCompress(this.path, compressedPath);
/* 201 */       return new EventLogDirectory.CompressedFile(compressedPath, this.id);
/*     */     } }
/*     */   public static final class CompressedFile extends Record implements File { private final Path path; private final EventLogDirectory.FileId id;
/*     */     
/* 205 */     public CompressedFile(Path path, EventLogDirectory.FileId id) { this.path = path; this.id = id; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/eventlog/EventLogDirectory$CompressedFile;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #205	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/eventlog/EventLogDirectory$CompressedFile; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/eventlog/EventLogDirectory$CompressedFile;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #205	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/eventlog/EventLogDirectory$CompressedFile; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/eventlog/EventLogDirectory$CompressedFile;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #205	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/eventlog/EventLogDirectory$CompressedFile;
/* 205 */       //   0	8	1	o	Ljava/lang/Object; } public Path path() { return this.path; } public EventLogDirectory.FileId id() { return this.id; }
/*     */     
/*     */     public Reader openReader() throws IOException {
/* 208 */       if (!Files.exists(this.path, new java.nio.file.LinkOption[0])) {
/* 209 */         return null;
/*     */       }
/* 211 */       return new BufferedReader(new InputStreamReader(new GZIPInputStream(Files.newInputStream(this.path, new OpenOption[0])), StandardCharsets.UTF_8));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 216 */     public CompressedFile compress() throws IOException { return this; } }
/*     */   public static final class FileId extends Record { private final LocalDate date;
/*     */     private final int index;
/*     */     
/* 220 */     public FileId(LocalDate date, int index) { this.date = date; this.index = index; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/eventlog/EventLogDirectory$FileId;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #220	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/eventlog/EventLogDirectory$FileId; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/eventlog/EventLogDirectory$FileId;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #220	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/eventlog/EventLogDirectory$FileId;
/* 220 */       //   0	8	1	o	Ljava/lang/Object; } public LocalDate date() { return this.date; } public int index() { return this.index; }
/* 221 */     private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
/*     */     
/*     */     public static FileId parse(String name) {
/* 224 */       int separator = name.indexOf("-");
/* 225 */       if (separator == -1) {
/* 226 */         return null;
/*     */       }
/*     */       
/* 229 */       String date = name.substring(0, separator);
/* 230 */       String index = name.substring(separator + 1);
/*     */       
/*     */       try {
/* 233 */         return new FileId(
/* 234 */             LocalDate.parse(date, DATE_FORMATTER), 
/* 235 */             Integer.parseInt(index));
/*     */       }
/* 237 */       catch (NumberFormatException|java.time.format.DateTimeParseException e) {
/* 238 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 244 */     public String toString() { return DATE_FORMATTER.format(this.date) + "-" + DATE_FORMATTER.format(this.date); }
/*     */ 
/*     */ 
/*     */     
/* 248 */     public String toFileName(String extension) { return String.valueOf(this) + String.valueOf(this); } }
/*     */ 
/*     */   
/*     */   public static interface File {
/*     */     Path path();
/*     */     
/*     */     EventLogDirectory.FileId id();
/*     */     
/*     */     Reader openReader() throws IOException;
/*     */     
/*     */     EventLogDirectory.CompressedFile compress() throws IOException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\eventlog\EventLogDirectory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
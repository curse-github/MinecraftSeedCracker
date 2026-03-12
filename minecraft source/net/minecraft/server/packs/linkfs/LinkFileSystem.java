/*     */ package net.minecraft.server.packs.linkfs;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.nio.file.WatchService;
/*     */ import java.nio.file.attribute.UserPrincipalLookupService;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
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
/*     */ public class LinkFileSystem
/*     */   extends FileSystem
/*     */ {
/*  39 */   private static final Set<String> VIEWS = Set.of("basic");
/*     */   public static final String PATH_SEPARATOR = "/";
/*  41 */   private static final Splitter PATH_SPLITTER = Splitter.on('/'); private final FileStore store; private final FileSystemProvider provider; private final LinkFSPath root;
/*     */   
/*     */   private LinkFileSystem(String name, DirectoryEntry rootEntry) {
/*  44 */     this.provider = new LinkFSProvider();
/*     */ 
/*     */ 
/*     */     
/*  48 */     this.store = new LinkFSFileStore(name);
/*  49 */     this.root = buildPath(rootEntry, this, "", null);
/*     */   }
/*     */   
/*     */   private static LinkFSPath buildPath(DirectoryEntry entry, LinkFileSystem fileSystem, String selfName, LinkFSPath parent) {
/*  53 */     Object2ObjectOpenHashMap<String, LinkFSPath> children = new Object2ObjectOpenHashMap<String, LinkFSPath>();
/*  54 */     LinkFSPath result = new LinkFSPath(fileSystem, selfName, parent, new PathContents.DirectoryContents(children));
/*  55 */     entry.files.forEach((name, linkTarget) -> 
/*  56 */         children.put(name, new LinkFSPath(fileSystem, name, result, new PathContents.FileContents(linkTarget))));
/*     */     
/*  58 */     entry.children.forEach((name, childEntry) -> 
/*  59 */         children.put(name, buildPath(childEntry, fileSystem, name, result)));
/*     */     
/*  61 */     children.trim();
/*  62 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  67 */   public FileSystemProvider provider() { return this.provider; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {}
/*     */ 
/*     */ 
/*     */   
/*  76 */   public boolean isOpen() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public boolean isReadOnly() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public String getSeparator() { return "/"; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public Iterable<Path> getRootDirectories() { return List.of(this.root); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public Iterable<FileStore> getFileStores() { return List.of(this.store); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public Set<String> supportedFileAttributeViews() { return VIEWS; }
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getPath(String first, String... more) {
/* 106 */     Stream<String> path = Stream.of(first);
/* 107 */     if (more.length > 0) {
/* 108 */       path = Stream.concat(path, Stream.of(more));
/*     */     }
/* 110 */     String joinedPath = (String)path.collect(Collectors.joining("/"));
/* 111 */     if (joinedPath.equals("/")) {
/* 112 */       return this.root;
/*     */     }
/*     */     
/* 115 */     if (joinedPath.startsWith("/")) {
/* 116 */       LinkFSPath result = this.root;
/* 117 */       for (String segment : PATH_SPLITTER.split(joinedPath.substring(1))) {
/* 118 */         if (segment.isEmpty()) {
/* 119 */           throw new IllegalArgumentException("Empty paths not allowed");
/*     */         }
/* 121 */         result = result.resolveName(segment);
/*     */       } 
/* 123 */       return result;
/*     */     } 
/* 125 */     LinkFSPath result = null;
/* 126 */     for (String segment : PATH_SPLITTER.split(joinedPath)) {
/* 127 */       if (segment.isEmpty()) {
/* 128 */         throw new IllegalArgumentException("Empty paths not allowed");
/*     */       }
/* 130 */       result = new LinkFSPath(this, segment, result, PathContents.RELATIVE);
/*     */     } 
/* 132 */     if (result == null) {
/* 133 */       throw new IllegalArgumentException("Empty paths not allowed");
/*     */     }
/* 135 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   public PathMatcher getPathMatcher(String syntaxAndPattern) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public UserPrincipalLookupService getUserPrincipalLookupService() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   public WatchService newWatchService() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */   
/* 155 */   public FileStore store() { return this.store; }
/*     */ 
/*     */ 
/*     */   
/* 159 */   public LinkFSPath rootPath() { return this.root; }
/*     */   private static final class DirectoryEntry extends Record { private final Map<String, DirectoryEntry> children; private final Map<String, Path> files;
/*     */     
/* 162 */     public Map<String, Path> files() { return this.files; } public Map<String, DirectoryEntry> children() { return this.children; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #162	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;
/* 162 */       //   0	8	1	o	Ljava/lang/Object; } private DirectoryEntry(Map<String, DirectoryEntry> children, Map<String, Path> files) { this.children = children; this.files = files; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #162	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry; }
/*     */     public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #162	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry; }
/* 164 */     public DirectoryEntry() { this(new HashMap(), new HashMap()); } }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/* 169 */     private final LinkFileSystem.DirectoryEntry root = new LinkFileSystem.DirectoryEntry();
/*     */     
/*     */     public Builder put(List<String> path, String name, Path target) {
/* 172 */       LinkFileSystem.DirectoryEntry currentEntry = this.root;
/* 173 */       for (String segment : path) {
/* 174 */         currentEntry = (LinkFileSystem.DirectoryEntry)currentEntry.children.computeIfAbsent(segment, n -> new LinkFileSystem.DirectoryEntry());
/*     */       }
/* 176 */       currentEntry.files.put(name, target);
/* 177 */       return this;
/*     */     }
/*     */     
/*     */     public Builder put(List<String> path, Path target) {
/* 181 */       if (path.isEmpty()) {
/* 182 */         throw new IllegalArgumentException("Path can't be empty");
/*     */       }
/* 184 */       int lastIndex = path.size() - 1;
/* 185 */       return put(path.subList(0, lastIndex), (String)path.get(lastIndex), target);
/*     */     }
/*     */ 
/*     */     
/* 189 */     public FileSystem build(String name) { return new LinkFileSystem(name, this.root); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 194 */   public static Builder builder() { return new Builder(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\linkfs\LinkFileSystem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
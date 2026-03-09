/*     */ package net.minecraft.server.packs.linkfs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.AccessDeniedException;
/*     */ import java.nio.file.AccessMode;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.DirectoryIteratorException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.NotDirectoryException;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.ProviderMismatchException;
/*     */ import java.nio.file.ReadOnlyFileSystemException;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LinkFSProvider
/*     */   extends FileSystemProvider
/*     */ {
/*     */   public static final String SCHEME = "x-mc-link";
/*     */   
/*  38 */   public String getScheme() { return "x-mc-link"; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   public FileSystem newFileSystem(URI uri, Map<String, ?> env) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public FileSystem getFileSystem(URI uri) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public Path getPath(URI uri) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute... attrs) throws IOException {
/*  58 */     if (options.contains(StandardOpenOption.CREATE_NEW) || options
/*  59 */       .contains(StandardOpenOption.CREATE) || options
/*  60 */       .contains(StandardOpenOption.APPEND) || options
/*  61 */       .contains(StandardOpenOption.WRITE))
/*     */     {
/*  63 */       throw new UnsupportedOperationException();
/*     */     }
/*  65 */     Path targetPath = toLinkPath(path).toAbsolutePath().getTargetPath();
/*  66 */     if (targetPath == null) {
/*  67 */       throw new NoSuchFileException(path.toString());
/*     */     }
/*  69 */     return Files.newByteChannel(targetPath, options, attrs);
/*     */   }
/*     */ 
/*     */   
/*     */   public DirectoryStream<Path> newDirectoryStream(Path dir, final DirectoryStream.Filter<? super Path> filter) throws IOException {
/*  74 */     final PathContents.DirectoryContents directoryContents = toLinkPath(dir).toAbsolutePath().getDirectoryContents();
/*  75 */     if (directoryContents == null) {
/*  76 */       throw new NotDirectoryException(dir.toString());
/*     */     }
/*     */     
/*  79 */     return new DirectoryStream<Path>(this)
/*     */       {
/*     */         public Iterator<Path> iterator() {
/*  82 */           return directoryContents.children().values()
/*  83 */             .stream()
/*  84 */             .filter(path -> {
/*     */                 try {
/*  86 */                   return filter.accept(path);
/*  87 */                 } catch (IOException e) {
/*  88 */                   throw new DirectoryIteratorException(e);
/*     */                 }
/*     */               
/*  91 */               }).map(path -> path)
/*  92 */             .iterator();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void close() {}
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public void createDirectory(Path dir, FileAttribute... attrs) { throw new ReadOnlyFileSystemException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public void delete(Path path) { throw new ReadOnlyFileSystemException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public void copy(Path source, Path target, CopyOption... options) { throw new ReadOnlyFileSystemException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public void move(Path source, Path target, CopyOption... options) { throw new ReadOnlyFileSystemException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public boolean isSameFile(Path path, Path path2) { return (path instanceof LinkFSPath && path2 instanceof LinkFSPath && path.equals(path2)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   public boolean isHidden(Path path) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public FileStore getFileStore(Path path) { return toLinkPath(path).getFileSystem().store(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkAccess(Path path, AccessMode... modes) throws IOException {
/* 138 */     if (modes.length == 0 && 
/* 139 */       !toLinkPath(path).exists()) {
/* 140 */       throw new NoSuchFileException(path.toString());
/*     */     }
/*     */ 
/*     */     
/* 144 */     for (AccessMode mode : modes) {
/* 145 */       switch (mode) {
/*     */         case READ:
/* 147 */           if (!toLinkPath(path).exists())
/* 148 */             throw new NoSuchFileException(path.toString());  break;
/*     */         case EXECUTE:
/*     */         case WRITE:
/* 151 */           throw new AccessDeniedException(mode.toString());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V extends java.nio.file.attribute.FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
/* 159 */     LinkFSPath linkPath = toLinkPath(path);
/* 160 */     if (type == java.nio.file.attribute.BasicFileAttributeView.class) {
/* 161 */       return (V)linkPath.getBasicAttributeView();
/*     */     }
/* 163 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends java.nio.file.attribute.BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
/* 169 */     LinkFSPath linkPath = toLinkPath(path).toAbsolutePath();
/* 170 */     if (type == java.nio.file.attribute.BasicFileAttributes.class) {
/* 171 */       return (A)linkPath.getBasicAttributes();
/*     */     }
/* 173 */     throw new UnsupportedOperationException("Attributes of type " + type.getName() + " not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   public void setAttribute(Path path, String attribute, Object value, LinkOption... options) { throw new ReadOnlyFileSystemException(); }
/*     */ 
/*     */   
/*     */   private static LinkFSPath toLinkPath(Path path) {
/* 188 */     if (path == null) {
/* 189 */       throw new NullPointerException();
/*     */     }
/* 191 */     if (path instanceof LinkFSPath) return (LinkFSPath)path;
/*     */ 
/*     */     
/* 194 */     throw new ProviderMismatchException();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\linkfs\LinkFSProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.hash.Funnels;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hasher;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.Proxy;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.time.Instant;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.OptionalLong;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class HttpUtil
/*     */ {
/*  32 */   private static final Logger LOGGER = LogUtils.getLogger();
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
/*     */   public static Path downloadFile(Path targetDir, URL url, Map<String, String> headers, HashFunction hashFunction, HashCode requestedHash, int maxSize, Proxy proxy, DownloadProgressListener listener) {
/*     */     Path targetFile;
/*  48 */     HttpURLConnection connection = null;
/*  49 */     input = null;
/*     */     
/*  51 */     listener.requestStart();
/*     */ 
/*     */     
/*  54 */     if (requestedHash != null) {
/*  55 */       targetFile = cachedFilePath(targetDir, requestedHash);
/*     */       try {
/*  57 */         if (checkExistingFile(targetFile, hashFunction, requestedHash)) {
/*  58 */           LOGGER.info("Returning cached file since actual hash matches requested");
/*  59 */           listener.requestFinished(true);
/*     */           
/*  61 */           updateModificationTime(targetFile);
/*  62 */           return targetFile;
/*     */         } 
/*  64 */       } catch (IOException e) {
/*  65 */         LOGGER.warn("Failed to check cached file {}", targetFile, e);
/*     */       } 
/*     */       try {
/*  68 */         LOGGER.warn("Existing file {} not found or had mismatched hash", targetFile);
/*  69 */         Files.deleteIfExists(targetFile);
/*  70 */       } catch (IOException e) {
/*  71 */         listener.requestFinished(false);
/*  72 */         throw new UncheckedIOException("Failed to remove existing file " + String.valueOf(targetFile), e);
/*     */       } 
/*     */     } else {
/*  75 */       targetFile = null;
/*     */     } 
/*     */     
/*     */     try {
/*  79 */       connection = (HttpURLConnection)url.openConnection(proxy);
/*  80 */       connection.setInstanceFollowRedirects(true);
/*     */       
/*  82 */       Objects.requireNonNull(connection); headers.forEach(connection::setRequestProperty);
/*     */       
/*  84 */       input = connection.getInputStream();
/*  85 */       long contentLength = connection.getContentLengthLong();
/*  86 */       OptionalLong size = (contentLength != -1L) ? OptionalLong.of(contentLength) : OptionalLong.empty();
/*     */       
/*  88 */       FileUtil.createDirectoriesSafe(targetDir);
/*     */       
/*  90 */       listener.downloadStart(size);
/*     */       
/*  92 */       if (size.isPresent() && 
/*  93 */         size.getAsLong() > maxSize) {
/*  94 */         throw new IOException("Filesize is bigger than maximum allowed (file is " + String.valueOf(size) + ", limit is " + maxSize + ")");
/*     */       }
/*     */ 
/*     */       
/*  98 */       if (targetFile != null) {
/*  99 */         HashCode actualHash = downloadAndHash(hashFunction, maxSize, listener, input, targetFile);
/* 100 */         if (!actualHash.equals(requestedHash)) {
/* 101 */           throw new IOException("Hash of downloaded file (" + String.valueOf(actualHash) + ") did not match requested (" + String.valueOf(requestedHash) + ")");
/*     */         }
/* 103 */         listener.requestFinished(true);
/* 104 */         return targetFile;
/*     */       } 
/* 106 */       tmpPath = Files.createTempFile(targetDir, "download", ".tmp", new java.nio.file.attribute.FileAttribute[0]);
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
/*     */     }
/* 124 */     catch (Throwable t) {
/* 125 */       if (connection != null) {
/* 126 */         InputStream error = connection.getErrorStream();
/* 127 */         if (error != null) {
/*     */           try {
/* 129 */             LOGGER.error("HTTP response error: {}", IOUtils.toString(error, StandardCharsets.UTF_8));
/* 130 */           } catch (Exception e) {
/* 131 */             LOGGER.error("Failed to read response from server");
/*     */           } 
/*     */         }
/*     */       } 
/* 135 */       listener.requestFinished(false);
/* 136 */       throw new IllegalStateException("Failed to download file " + String.valueOf(url), t);
/*     */     } finally {
/* 138 */       IOUtils.closeQuietly(input);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void updateModificationTime(Path targetFile) {
/*     */     try {
/* 144 */       Files.setLastModifiedTime(targetFile, FileTime.from(Instant.now()));
/* 145 */     } catch (IOException e) {
/* 146 */       LOGGER.warn("Failed to update modification time of {}", targetFile, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static HashCode hashFile(Path file, HashFunction hashFunction) throws IOException {
/* 151 */     Hasher hasher = hashFunction.newHasher();
/* 152 */     OutputStream outputStream = Funnels.asOutputStream(hasher); 
/* 153 */     try { InputStream fileInput = Files.newInputStream(file, new OpenOption[0]);
/*     */       
/* 155 */       try { fileInput.transferTo(outputStream);
/* 156 */         if (fileInput != null) fileInput.close();  } catch (Throwable throwable) { if (fileInput != null) try { fileInput.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (outputStream != null) outputStream.close();  } catch (Throwable throwable) { if (outputStream != null)
/* 157 */         try { outputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  return hasher.hash();
/*     */   }
/*     */   
/*     */   private static boolean checkExistingFile(Path file, HashFunction hashFunction, HashCode expectedHash) throws IOException {
/* 161 */     if (Files.exists(file, new java.nio.file.LinkOption[0])) {
/* 162 */       HashCode actualHash = hashFile(file, hashFunction);
/* 163 */       if (actualHash.equals(expectedHash)) {
/* 164 */         return true;
/*     */       }
/* 166 */       LOGGER.warn("Mismatched hash of file {}, expected {} but found {}", new Object[] { file, expectedHash, actualHash });
/*     */     } 
/*     */     
/* 169 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 173 */   private static Path cachedFilePath(Path targetDir, HashCode requestedHash) { return targetDir.resolve(requestedHash.toString()); }
/*     */ 
/*     */   
/*     */   private static HashCode downloadAndHash(HashFunction hashFunction, int maxSize, DownloadProgressListener listener, InputStream input, Path downloadFile) throws IOException {
/* 177 */     OutputStream output = Files.newOutputStream(downloadFile, new OpenOption[] { StandardOpenOption.CREATE }); 
/* 178 */     try { Hasher hasher = hashFunction.newHasher();
/*     */       
/* 180 */       byte[] buffer = new byte[8196];
/*     */       
/* 182 */       long readSoFar = 0L; int read;
/* 183 */       while ((read = input.read(buffer)) >= 0) {
/* 184 */         readSoFar += read;
/* 185 */         listener.downloadedBytes(readSoFar);
/*     */         
/* 187 */         if (readSoFar > maxSize) {
/* 188 */           throw new IOException("Filesize was bigger than maximum allowed (got >= " + readSoFar + ", limit was " + maxSize + ")");
/*     */         }
/*     */         
/* 191 */         if (Thread.interrupted()) {
/* 192 */           LOGGER.error("INTERRUPTED");
/* 193 */           throw new IOException("Download interrupted");
/*     */         } 
/*     */         
/* 196 */         output.write(buffer, 0, read);
/* 197 */         hasher.putBytes(buffer, 0, read);
/*     */       } 
/* 199 */       HashCode hashCode = hasher.hash();
/* 200 */       if (output != null) output.close();  return hashCode; } catch (Throwable throwable) { if (output != null)
/*     */         try { output.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 204 */      } public static int getAvailablePort() { try { server = new ServerSocket(0); 
/* 205 */       try { int i = server.getLocalPort();
/* 206 */         server.close(); return i; } catch (Throwable throwable) { try { server.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ignored)
/* 207 */     { return 25564; }
/*     */      }
/*     */ 
/*     */   
/*     */   public static boolean isPortAvailable(int port) {
/* 212 */     if (port < 0 || port > 65535)
/* 213 */       return false; 
/*     */     
/* 215 */     try { ServerSocket server = new ServerSocket(port); 
/* 216 */       try { boolean bool = (server.getLocalPort() == port);
/* 217 */         server.close(); return bool; } catch (Throwable throwable) { try { server.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ignored)
/* 218 */     { return false; }
/*     */   
/*     */   }
/*     */   
/*     */   public static interface DownloadProgressListener {
/*     */     void requestStart();
/*     */     
/*     */     void downloadStart(OptionalLong param1OptionalLong);
/*     */     
/*     */     void downloadedBytes(long param1Long);
/*     */     
/*     */     void requestFinished(boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\HttpUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
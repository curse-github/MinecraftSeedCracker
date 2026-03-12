/*    */ package net.minecraft.data.structures;
/*    */ 
/*    */ import com.google.common.hash.Hashing;
/*    */ import com.google.common.hash.HashingOutputStream;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.nbt.NbtAccounter;
/*    */ import net.minecraft.nbt.NbtIo;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.util.FastBufferedInputStream;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class NbtToSnbt implements DataProvider {
/* 30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Iterable<Path> inputFolders;
/*    */   private final PackOutput output;
/*    */   
/*    */   public NbtToSnbt(PackOutput output, Collection<Path> inputFolders) {
/* 36 */     this.inputFolders = inputFolders;
/* 37 */     this.output = output;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 42 */     Path output = this.output.getOutputFolder();
/*    */     
/* 44 */     List<CompletableFuture<?>> tasks = new ArrayList<CompletableFuture<?>>();
/*    */     
/* 46 */     for (Iterator iterator = this.inputFolders.iterator(); iterator.hasNext(); ) { Path input = (Path)iterator.next();
/* 47 */       tasks.add(CompletableFuture.supplyAsync(() -> { 
/* 48 */               try { Stream<Path> walk = Files.walk(input, new java.nio.file.FileVisitOption[0]); 
/* 49 */                 try { CompletableFuture completableFuture = CompletableFuture.allOf((CompletableFuture[])walk
/* 50 */                       .filter(())
/* 51 */                       .map(())
/* 52 */                       .toArray(()));
/* 53 */                   if (walk != null) walk.close();  return completableFuture; } catch (Throwable throwable) { if (walk != null) try { walk.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 54 */               { LOGGER.error("Failed to read structure input directory", e);
/* 55 */                 return CompletableFuture.completedFuture(null); }
/*    */             
/* 57 */             }Util.backgroundExecutor().forName("NbtToSnbt")).thenCompose(v -> v)); }
/*    */ 
/*    */     
/* 60 */     return CompletableFuture.allOf((CompletableFuture[])tasks.toArray(x$0 -> new CompletableFuture[x$0]));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public final String getName() { return "NBT -> SNBT"; }
/*    */ 
/*    */   
/*    */   private static String getName(Path root, Path path) {
/* 69 */     String name = root.relativize(path).toString().replaceAll("\\\\", "/");
/* 70 */     return name.substring(0, name.length() - ".nbt".length());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Path convertStructure(CachedOutput cache, Path path, String name, Path output) {
/*    */     
/* 79 */     try { InputStream rawInput = Files.newInputStream(path, new java.nio.file.OpenOption[0]); 
/* 80 */       try { FastBufferedInputStream fastBufferedInputStream = new FastBufferedInputStream(rawInput);
/*    */         
/* 82 */         try { Path resultPath = output.resolve(name + ".snbt");
/* 83 */           writeSnbt(cache, resultPath, NbtUtils.structureToSnbt(NbtIo.readCompressed(fastBufferedInputStream, NbtAccounter.unlimitedHeap())));
/* 84 */           LOGGER.info("Converted {} from NBT to SNBT", name);
/* 85 */           Path path1 = resultPath;
/* 86 */           fastBufferedInputStream.close(); if (rawInput != null) rawInput.close();  return path1; } catch (Throwable throwable) { try { fastBufferedInputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable throwable) { if (rawInput != null) try { rawInput.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 87 */     { LOGGER.error("Couldn't convert {} from NBT to SNBT at {}", new Object[] { name, path, e });
/* 88 */       return null; }
/*    */   
/*    */   }
/*    */   
/*    */   public static void writeSnbt(CachedOutput cache, Path destination, String text) throws IOException {
/* 93 */     ByteArrayOutputStream bytes = new ByteArrayOutputStream();
/* 94 */     HashingOutputStream hashedBytes = new HashingOutputStream(Hashing.sha1(), bytes);
/* 95 */     hashedBytes.write(text.getBytes(StandardCharsets.UTF_8));
/* 96 */     hashedBytes.write(10);
/* 97 */     cache.writeIfNeeded(destination, bytes.toByteArray(), hashedBytes.hash());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\structures\NbtToSnbt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
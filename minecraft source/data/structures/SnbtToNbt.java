/*     */ package net.minecraft.data.structures;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.google.common.hash.HashingOutputStream;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SnbtToNbt implements DataProvider {
/*  28 */   private static final Logger LOGGER = LogUtils.getLogger(); private final PackOutput output;
/*     */   private final Iterable<Path> inputFolders;
/*     */   private final List<Filter> filters;
/*     */   
/*     */   public SnbtToNbt(PackOutput output, Iterable<Path> inputFolders) {
/*  33 */     this.filters = Lists.newArrayList();
/*     */ 
/*     */     
/*  36 */     this.output = output;
/*  37 */     this.inputFolders = inputFolders;
/*     */   }
/*     */   
/*     */   public SnbtToNbt addFilter(Filter filter) {
/*  41 */     this.filters.add(filter);
/*  42 */     return this;
/*     */   }
/*     */   
/*     */   private CompoundTag applyFilters(String name, CompoundTag input) {
/*  46 */     CompoundTag result = input;
/*  47 */     for (Filter filter : this.filters) {
/*  48 */       result = filter.apply(name, result);
/*     */     }
/*  50 */     return result;
/*     */   } @FunctionalInterface
/*     */   public static interface Filter {
/*  53 */     CompoundTag apply(String param1String, CompoundTag param1CompoundTag); } private static final class TaskResult extends Record { private final String name; private final byte[] payload; private final HashCode hash; private TaskResult(String name, byte[] payload, HashCode hash) { this.name = name; this.payload = payload; this.hash = hash; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #53	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/structures/SnbtToNbt$TaskResult; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #53	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/structures/SnbtToNbt$TaskResult; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #53	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;
/*  53 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public byte[] payload() { return this.payload; } public HashCode hash() { return this.hash; } }
/*     */ 
/*     */   
/*     */   public CompletableFuture<?> run(CachedOutput cache) {
/*  57 */     Path output = this.output.getOutputFolder();
/*     */     
/*  59 */     List<CompletableFuture<?>> tasks = Lists.newArrayList();
/*     */     
/*  61 */     for (Iterator iterator = this.inputFolders.iterator(); iterator.hasNext(); ) { Path input = (Path)iterator.next();
/*  62 */       tasks.add(CompletableFuture.supplyAsync(() -> { 
/*  63 */               try { Stream<Path> files = Files.walk(input, new java.nio.file.FileVisitOption[0]); 
/*  64 */                 try { CompletableFuture completableFuture = CompletableFuture.allOf((CompletableFuture[])files.filter(())
/*  65 */                       .map(())
/*     */ 
/*     */ 
/*     */                       
/*  69 */                       .toArray(()));
/*  70 */                   if (files != null) files.close();  return completableFuture; } catch (Throwable throwable) { if (files != null) try { files.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/*  71 */               { throw new RuntimeException("Failed to read structure input directory, aborting", e); }
/*     */             
/*  73 */             }Util.backgroundExecutor().forName("SnbtToNbt")).thenCompose(v -> v)); }
/*     */ 
/*     */     
/*  76 */     return Util.sequenceFailFast(tasks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public final String getName() { return "SNBT -> NBT"; }
/*     */ 
/*     */   
/*     */   private String getName(Path root, Path path) {
/*  85 */     String name = root.relativize(path).toString().replaceAll("\\\\", "/");
/*  86 */     return name.substring(0, name.length() - ".snbt".length());
/*     */   }
/*     */   private TaskResult readStructure(Path path, String name) {
/*     */     
/*  90 */     try { BufferedReader reader = Files.newBufferedReader(path); 
/*  91 */       try { String input = IOUtils.toString(reader);
/*  92 */         CompoundTag updated = applyFilters(name, NbtUtils.snbtToStructure(input));
/*  93 */         ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  94 */         HashingOutputStream hos = new HashingOutputStream(Hashing.sha1(), bos);
/*  95 */         NbtIo.writeCompressed(updated, hos);
/*  96 */         byte[] bytes = bos.toByteArray();
/*  97 */         HashCode hash = hos.hash();
/*  98 */         TaskResult taskResult = new TaskResult(name, bytes, hash);
/*  99 */         if (reader != null) reader.close();  return taskResult; } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable t)
/* 100 */     { throw new StructureConversionException(path, t); }
/*     */   
/*     */   }
/*     */   
/*     */   private void storeStructureIfChanged(CachedOutput cache, TaskResult task, Path output) {
/* 105 */     Path destination = output.resolve(task.name + ".nbt");
/*     */     try {
/* 107 */       cache.writeIfNeeded(destination, task.payload, task.hash);
/* 108 */     } catch (IOException e) {
/* 109 */       LOGGER.error("Couldn't write structure {} at {}", new Object[] { task.name, destination, e });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StructureConversionException
/*     */     extends RuntimeException
/*     */   {
/* 120 */     public StructureConversionException(Path path, Throwable t) { super(path.toAbsolutePath().toString(), t); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\structures\SnbtToNbt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
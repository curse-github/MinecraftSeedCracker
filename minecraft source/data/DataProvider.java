/*    */ package net.minecraft.data;
/*    */ 
/*    */ import com.google.common.hash.Hashing;
/*    */ import com.google.common.hash.HashingOutputStream;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Comparator;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public interface DataProvider {
/* 31 */   public static final ToIntFunction<String> FIXED_ORDER_FIELDS = (ToIntFunction)Util.make(new Object2IntOpenHashMap(), m -> {
/* 32 */         m.put("type", 0);
/* 33 */         m.put("parent", 1);
/* 34 */         m.defaultReturnValue(2);
/*    */       });
/* 36 */   public static final Comparator<String> KEY_COMPARATOR = Comparator.comparingInt(FIXED_ORDER_FIELDS).thenComparing(e -> e);
/*    */   
/* 38 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   static <T> CompletableFuture<?> saveAll(CachedOutput cache, Codec<T> codec, PackOutput.PathProvider pathProvider, Map<Identifier, T> entries) { Objects.requireNonNull(pathProvider); return saveAll(cache, codec, pathProvider::json, entries); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   static <T, E> CompletableFuture<?> saveAll(CachedOutput cache, Codec<E> codec, Function<T, Path> pathGetter, Map<T, E> contents) { return saveAll(cache, e -> (JsonElement)codec.encodeStart(JsonOps.INSTANCE, e).getOrThrow(), pathGetter, contents); }
/*    */ 
/*    */   
/*    */   static <T, E> CompletableFuture<?> saveAll(CachedOutput cache, Function<E, JsonElement> serializer, Function<T, Path> pathGetter, Map<T, E> contents) {
/* 53 */     return CompletableFuture.allOf((CompletableFuture[])contents.entrySet().stream()
/* 54 */         .map(entry -> {
/* 55 */             Path path = (Path)pathGetter.apply(entry.getKey());
/* 56 */             JsonElement json = (JsonElement)serializer.apply(entry.getValue());
/* 57 */             return saveStable(cache, json, path);
/*    */           
/* 59 */           }).toArray(x$0 -> new CompletableFuture[x$0]));
/*    */   }
/*    */   
/*    */   static <T> CompletableFuture<?> saveStable(CachedOutput cache, HolderLookup.Provider registries, Codec<T> codec, T value, Path path) {
/* 63 */     RegistryOps<JsonElement> ops = registries.createSerializationContext(JsonOps.INSTANCE);
/* 64 */     return saveStable(cache, ops, codec, value, path);
/*    */   }
/*    */ 
/*    */   
/* 68 */   static <T> CompletableFuture<?> saveStable(CachedOutput cache, Codec<T> codec, T value, Path path) { return saveStable(cache, JsonOps.INSTANCE, codec, value, path); }
/*    */ 
/*    */   
/*    */   private static <T> CompletableFuture<?> saveStable(CachedOutput cache, DynamicOps<JsonElement> ops, Codec<T> codec, T value, Path path) {
/* 72 */     JsonElement json = (JsonElement)codec.encodeStart(ops, value).getOrThrow();
/* 73 */     return saveStable(cache, json, path);
/*    */   }
/*    */   
/*    */   static CompletableFuture<?> saveStable(CachedOutput cache, JsonElement root, Path path) {
/* 77 */     return CompletableFuture.runAsync(() -> {
/*    */           try {
/* 79 */             ByteArrayOutputStream bytes = new ByteArrayOutputStream();
/* 80 */             HashingOutputStream hashedBytes = new HashingOutputStream(Hashing.sha1(), bytes);
/* 81 */             JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(hashedBytes, StandardCharsets.UTF_8)); 
/* 82 */             try { jsonWriter.setSerializeNulls(false);
/* 83 */               jsonWriter.setIndent("  ");
/* 84 */               GsonHelper.writeValue(jsonWriter, root, KEY_COMPARATOR);
/* 85 */               jsonWriter.close(); } catch (Throwable throwable) { try { jsonWriter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 86 */              cache.writeIfNeeded(path, bytes.toByteArray(), hashedBytes.hash());
/* 87 */           } catch (IOException e) {
/* 88 */             LOGGER.error("Failed to save file to {}", path, e);
/*    */           } 
/* 90 */         }Util.backgroundExecutor().forName("saveStable"));
/*    */   }
/*    */   
/*    */   CompletableFuture<?> run(CachedOutput paramCachedOutput);
/*    */   
/*    */   String getName();
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Factory<T extends DataProvider> {
/*    */     T create(PackOutput param1PackOutput);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\DataProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.io.Reader;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.FileToIdConverter;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.StrictJsonParser;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public abstract class SimpleJsonResourceReloadListener<T>
/*    */   extends SimplePreparableReloadListener<Map<Identifier, T>> {
/* 24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final DynamicOps<JsonElement> ops;
/*    */   
/*    */   private final Codec<T> codec;
/*    */   private final FileToIdConverter lister;
/*    */   
/* 31 */   protected SimpleJsonResourceReloadListener(HolderLookup.Provider registries, Codec<T> codec, ResourceKey<? extends Registry<T>> registryKey) { this(registries.createSerializationContext(JsonOps.INSTANCE), codec, FileToIdConverter.registry(registryKey)); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   protected SimpleJsonResourceReloadListener(Codec<T> codec, FileToIdConverter lister) { this(JsonOps.INSTANCE, codec, lister); }
/*    */ 
/*    */   
/*    */   private SimpleJsonResourceReloadListener(DynamicOps<JsonElement> ops, Codec<T> codec, FileToIdConverter lister) {
/* 39 */     this.ops = ops;
/* 40 */     this.codec = codec;
/* 41 */     this.lister = lister;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Map<Identifier, T> prepare(ResourceManager manager, ProfilerFiller profiler) {
/* 46 */     Map<Identifier, T> result = new HashMap<Identifier, T>();
/* 47 */     scanDirectory(manager, this.lister, this.ops, this.codec, result);
/* 48 */     return result;
/*    */   }
/*    */ 
/*    */   
/* 52 */   public static <T> void scanDirectory(ResourceManager manager, ResourceKey<? extends Registry<T>> registryKey, DynamicOps<JsonElement> ops, Codec<T> codec, Map<Identifier, T> result) { scanDirectory(manager, FileToIdConverter.registry(registryKey), ops, codec, result); }
/*    */ 
/*    */   
/*    */   public static <T> void scanDirectory(ResourceManager manager, FileToIdConverter lister, DynamicOps<JsonElement> ops, Codec<T> codec, Map<Identifier, T> result) {
/* 56 */     for (Map.Entry<Identifier, Resource> entry : lister.listMatchingResources(manager).entrySet()) {
/* 57 */       Identifier location = (Identifier)entry.getKey();
/* 58 */       Identifier id = lister.fileToId(location);
/*    */       
/* 60 */       try { Reader reader = ((Resource)entry.getValue()).openAsReader(); 
/* 61 */         try { codec.parse(ops, StrictJsonParser.parse(reader))
/* 62 */             .ifSuccess(parsed -> {
/* 63 */                 if (result.putIfAbsent(id, parsed) != null) {
/* 64 */                   throw new IllegalStateException("Duplicate data file ignored with ID " + String.valueOf(id));
/*    */                 }
/*    */               
/* 67 */               }).ifError(error -> LOGGER.error("Couldn't parse data file '{}' from '{}': {}", new Object[] { id, location, error }));
/* 68 */           if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (JsonParseException|IllegalArgumentException|java.io.IOException e)
/* 69 */       { LOGGER.error("Couldn't parse data file '{}' from '{}'", new Object[] { id, location, e }); }
/*    */     
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\SimpleJsonResourceReloadListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
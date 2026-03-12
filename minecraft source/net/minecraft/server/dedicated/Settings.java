/*     */ package net.minecraft.server.dedicated;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Objects;
/*     */ import java.util.Properties;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class Settings<T extends Settings<T>>
/*     */   extends Object {
/*     */   public class MutableValue<V>
/*     */     extends Object implements Supplier<V> {
/*     */     private final String key;
/*     */     private final V value;
/*     */     private final Function<V, String> serializer;
/*     */     
/*     */     private MutableValue(String key, V value, Function<V, String> serializer) {
/*  34 */       this.key = key;
/*  35 */       this.value = value;
/*  36 */       this.serializer = serializer;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  41 */     public V get() { return (V)this.value; }
/*     */ 
/*     */     
/*     */     public T update(RegistryAccess registryAccess, V value) {
/*  45 */       Properties properties = Settings.this.cloneProperties();
/*  46 */       properties.put(this.key, this.serializer.apply(value));
/*  47 */       return (T)Settings.this.reload(registryAccess, properties);
/*     */     }
/*     */   }
/*     */   
/*  51 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   protected final Properties properties;
/*     */ 
/*     */   
/*  56 */   public Settings(Properties properties) { this.properties = properties; }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Properties loadFromFile(Path file) {
/*     */     
/*  62 */     try { InputStream is = Files.newInputStream(file, new java.nio.file.OpenOption[0]);
/*     */ 
/*     */       
/*  65 */       try { CharsetDecoder reportingUtf8Decoder = StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
/*     */         
/*  67 */         Properties properties = new Properties();
/*  68 */         properties.load(new InputStreamReader(is, reportingUtf8Decoder));
/*  69 */         Properties properties1 = properties;
/*  70 */         if (is != null) is.close();  return properties1; } catch (Throwable throwable) { if (is != null)
/*  71 */           try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (CharacterCodingException e)
/*  72 */     { LOGGER.info("Failed to load properties as UTF-8 from file {}, trying ISO_8859_1", file);
/*  73 */       Reader reader = Files.newBufferedReader(file, StandardCharsets.ISO_8859_1); 
/*  74 */       try { Properties properties = new Properties();
/*  75 */         properties.load(reader);
/*  76 */         Properties properties1 = properties;
/*  77 */         if (reader != null) reader.close();  return properties1; } catch (Throwable throwable) { if (reader != null)
/*     */           try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  }
/*  79 */     catch (IOException e)
/*  80 */     { LOGGER.error("Failed to load properties from file: {}", file, e);
/*     */       
/*  82 */       return new Properties(); }
/*     */   
/*     */   } public void store(Path output) {
/*     */     
/*  86 */     try { Writer os = Files.newBufferedWriter(output, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]); 
/*  87 */       try { this.properties.store(os, "Minecraft server properties");
/*  88 */         if (os != null) os.close();  } catch (Throwable throwable) { if (os != null) try { os.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/*  89 */     { LOGGER.error("Failed to store properties to file: {}", output); }
/*     */   
/*     */   }
/*     */   
/*     */   private static <V extends Number> Function<String, V> wrapNumberDeserializer(Function<String, V> inner) {
/*  94 */     return s -> {
/*     */         try {
/*  96 */           return (Number)inner.apply(s);
/*  97 */         } catch (NumberFormatException e) {
/*  98 */           return null;
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   protected static <V> Function<String, V> dispatchNumberOrString(IntFunction<V> intDeserializer, Function<String, V> stringDeserializer) {
/* 104 */     return s -> {
/*     */         try {
/* 106 */           return intDeserializer.apply(Integer.parseInt(s));
/* 107 */         } catch (NumberFormatException e) {
/* 108 */           return stringDeserializer.apply(s);
/*     */         } 
/*     */       };
/*     */   }
/*     */ 
/*     */   
/* 114 */   private String getStringRaw(String key) { return (String)this.properties.get(key); }
/*     */ 
/*     */   
/*     */   protected <V> V getLegacy(String key, Function<String, V> deserializer) {
/* 118 */     String value = getStringRaw(key);
/* 119 */     if (value == null) {
/* 120 */       return null;
/*     */     }
/* 122 */     this.properties.remove(key);
/* 123 */     return (V)deserializer.apply(value);
/*     */   }
/*     */   
/*     */   protected <V> V get(String key, Function<String, V> deserializer, Function<V, String> serializer, V defaultValue) {
/* 127 */     String value = getStringRaw(key);
/* 128 */     V result = (V)MoreObjects.firstNonNull((value != null) ? deserializer.apply(value) : null, defaultValue);
/* 129 */     this.properties.put(key, serializer.apply(result));
/* 130 */     return result;
/*     */   }
/*     */   
/*     */   protected <V> MutableValue<V> getMutable(String key, Function<String, V> deserializer, Function<V, String> serializer, V defaultValue) {
/* 134 */     String value = getStringRaw(key);
/* 135 */     V result = (V)MoreObjects.firstNonNull((value != null) ? deserializer.apply(value) : null, defaultValue);
/* 136 */     this.properties.put(key, serializer.apply(result));
/* 137 */     return new MutableValue(key, result, serializer);
/*     */   }
/*     */ 
/*     */   
/* 141 */   protected <V> V get(String key, Function<String, V> deserializer, UnaryOperator<V> validator, Function<V, String> serializer, V defaultValue) { return (V)get(key, s -> {
/* 142 */           V result = (V)deserializer.apply(s);
/* 143 */           return (result != null) ? validator.apply(result) : null;
/*     */         }serializer, defaultValue); }
/*     */ 
/*     */ 
/*     */   
/* 148 */   protected <V> V get(String key, Function<String, V> deserializer, V defaultValue) { return (V)get(key, deserializer, Objects::toString, defaultValue); }
/*     */ 
/*     */ 
/*     */   
/* 152 */   protected <V> MutableValue<V> getMutable(String key, Function<String, V> deserializer, V defaultValue) { return getMutable(key, deserializer, Objects::toString, defaultValue); }
/*     */ 
/*     */ 
/*     */   
/* 156 */   protected String get(String key, String defaultValue) { return (String)get(key, Function.identity(), Function.identity(), defaultValue); }
/*     */ 
/*     */ 
/*     */   
/* 160 */   protected String getLegacyString(String key) { return (String)getLegacy(key, Function.identity()); }
/*     */ 
/*     */ 
/*     */   
/* 164 */   protected int get(String key, int defaultValue) { return ((Integer)get(key, wrapNumberDeserializer(Integer::parseInt), Integer.valueOf(defaultValue))).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 168 */   protected MutableValue<Integer> getMutable(String key, int defaultValue) { return getMutable(key, wrapNumberDeserializer(Integer::parseInt), Integer.valueOf(defaultValue)); }
/*     */ 
/*     */ 
/*     */   
/* 172 */   protected MutableValue<String> getMutable(String key, String defaultValue) { return getMutable(key, String::new, defaultValue); }
/*     */ 
/*     */ 
/*     */   
/* 176 */   protected int get(String key, UnaryOperator<Integer> validator, int defaultValue) { return ((Integer)get(key, wrapNumberDeserializer(Integer::parseInt), validator, Objects::toString, Integer.valueOf(defaultValue))).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 180 */   protected long get(String key, long defaultValue) { return ((Long)get(key, wrapNumberDeserializer(Long::parseLong), Long.valueOf(defaultValue))).longValue(); }
/*     */ 
/*     */ 
/*     */   
/* 184 */   protected boolean get(String key, boolean defaultValue) { return ((Boolean)get(key, Boolean::valueOf, Boolean.valueOf(defaultValue))).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 188 */   protected MutableValue<Boolean> getMutable(String key, boolean defaultValue) { return getMutable(key, Boolean::valueOf, Boolean.valueOf(defaultValue)); }
/*     */ 
/*     */ 
/*     */   
/* 192 */   protected Boolean getLegacyBoolean(String key) { return (Boolean)getLegacy(key, Boolean::valueOf); }
/*     */ 
/*     */   
/*     */   protected Properties cloneProperties() {
/* 196 */     Properties result = new Properties();
/* 197 */     result.putAll(this.properties);
/* 198 */     return result;
/*     */   }
/*     */   
/*     */   protected abstract T reload(RegistryAccess paramRegistryAccess, Properties paramProperties);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dedicated\Settings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
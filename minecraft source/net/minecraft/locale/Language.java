/*     */ package net.minecraft.locale;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.FormattedCharSink;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.StringDecomposer;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Language
/*     */ {
/*  31 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  32 */   private static final Gson GSON = new Gson();
/*     */ 
/*     */   
/*  35 */   private static final Pattern UNSUPPORTED_FORMAT_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d.]*[df]");
/*     */   
/*     */   public static final String DEFAULT = "en_us";
/*     */   
/*     */   private static Language loadDefault() {
/*  40 */     deprecatedInfo = DeprecatedTranslationsInfo.loadFromDefaultResource();
/*     */     
/*  42 */     Map<String, String> loadedData = new HashMap<String, String>();
/*  43 */     Objects.requireNonNull(loadedData); BiConsumer<String, String> output = loadedData::put;
/*  44 */     parseTranslations(output, "/assets/minecraft/lang/en_us.json");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  50 */     deprecatedInfo.applyToMap(loadedData);
/*     */     
/*  52 */     final Map<String, String> storage = Map.copyOf(loadedData);
/*  53 */     return new Language()
/*     */       {
/*     */         public String getOrDefault(String elementId, String defaultValue) {
/*  56 */           return (String)storage.getOrDefault(elementId, defaultValue);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  61 */         public boolean has(String elementId) { return storage.containsKey(elementId); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  66 */         public boolean isDefaultRightToLeft() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public FormattedCharSequence getVisualOrder(FormattedText logicalOrderText) {
/*  72 */           return output -> logicalOrderText.visit((), Style.EMPTY)
/*     */             
/*  74 */             .isPresent();
/*     */         }
/*     */       };
/*     */   }
/*     */   private static void parseTranslations(BiConsumer<String, String> output, String path) {
/*     */     
/*  80 */     try { InputStream stream = Language.class.getResourceAsStream(path); 
/*  81 */       try { loadFromJson(stream, output);
/*  82 */         if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException|com.google.gson.JsonParseException e)
/*  83 */     { LOGGER.error("Couldn't read strings from {}", path, e); }
/*     */   
/*     */   }
/*     */   
/*     */   public static void loadFromJson(InputStream stream, BiConsumer<String, String> output) {
/*  88 */     JsonObject entries = (JsonObject)GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JsonObject.class);
/*  89 */     for (Map.Entry<String, JsonElement> entry : entries.entrySet()) {
/*  90 */       String text = UNSUPPORTED_FORMAT_PATTERN.matcher(GsonHelper.convertToString((JsonElement)entry.getValue(), (String)entry.getKey())).replaceAll("%$1s");
/*  91 */       output.accept((String)entry.getKey(), text);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  96 */   public static Language getInstance() { return instance; }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public static void inject(Language language) { instance = language; }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public String getOrDefault(String elementId) { return getOrDefault(elementId, elementId); }
/*     */ 
/*     */   
/*     */   public abstract String getOrDefault(String paramString1, String paramString2);
/*     */ 
/*     */   
/*     */   public abstract boolean has(String paramString);
/*     */   
/*     */   public abstract boolean isDefaultRightToLeft();
/*     */   
/*     */   public abstract FormattedCharSequence getVisualOrder(FormattedText paramFormattedText);
/*     */   
/* 116 */   public List<FormattedCharSequence> getVisualOrder(List<FormattedText> lines) { return (List)lines.stream().map(this::getVisualOrder).collect(ImmutableList.toImmutableList()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\locale\Language.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
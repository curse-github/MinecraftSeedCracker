/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.zip.DeflaterOutputStream;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import java.util.zip.InflaterInputStream;
/*     */ import net.jpountz.lz4.LZ4BlockInputStream;
/*     */ import net.jpountz.lz4.LZ4BlockOutputStream;
/*     */ import net.minecraft.util.FastBufferedInputStream;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class RegionFileVersion
/*     */ {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  25 */   private static final Int2ObjectMap<RegionFileVersion> VERSIONS = new Int2ObjectOpenHashMap();
/*  26 */   private static final Object2ObjectMap<String, RegionFileVersion> VERSIONS_BY_NAME = new Object2ObjectOpenHashMap();
/*     */   
/*  28 */   public static final RegionFileVersion VERSION_GZIP = register(new RegionFileVersion(1, null, in -> 
/*     */         
/*  30 */         new FastBufferedInputStream(new GZIPInputStream(in)), out -> 
/*  31 */         new BufferedOutputStream(new GZIPOutputStream(out))));
/*     */ 
/*     */   
/*  34 */   public static final RegionFileVersion VERSION_DEFLATE = register(new RegionFileVersion(2, "deflate", in -> 
/*     */         
/*  36 */         new FastBufferedInputStream(new InflaterInputStream(in)), out -> 
/*  37 */         new BufferedOutputStream(new DeflaterOutputStream(out))));
/*     */ 
/*     */   
/*  40 */   public static final RegionFileVersion VERSION_NONE = register(new RegionFileVersion(3, "none", FastBufferedInputStream::new, BufferedOutputStream::new));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static final RegionFileVersion VERSION_LZ4 = register(new RegionFileVersion(4, "lz4", in -> 
/*     */         
/*  48 */         new FastBufferedInputStream(new LZ4BlockInputStream(in)), out -> 
/*  49 */         new BufferedOutputStream(new LZ4BlockOutputStream(out))));
/*     */ 
/*     */   
/*  52 */   public static final RegionFileVersion VERSION_CUSTOM = register(new RegionFileVersion(127, null, in -> {
/*     */ 
/*     */           
/*  55 */           throw new UnsupportedOperationException();
/*     */         }out -> {
/*     */           
/*  58 */           throw new UnsupportedOperationException();
/*     */         }));
/*     */ 
/*     */   
/*  62 */   public static final RegionFileVersion DEFAULT = VERSION_DEFLATE;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final String optionName;
/*     */   private final StreamWrapper<InputStream> inputWrapper;
/*     */   private final StreamWrapper<OutputStream> outputWrapper;
/*     */   
/*     */   private RegionFileVersion(int id, String optionName, StreamWrapper<InputStream> inputWrapper, StreamWrapper<OutputStream> outputWrapper) {
/*  71 */     this.id = id;
/*  72 */     this.optionName = optionName;
/*  73 */     this.inputWrapper = inputWrapper;
/*  74 */     this.outputWrapper = outputWrapper;
/*     */   }
/*     */   
/*     */   private static RegionFileVersion register(RegionFileVersion version) {
/*  78 */     VERSIONS.put(version.id, version);
/*  79 */     if (version.optionName != null) {
/*  80 */       VERSIONS_BY_NAME.put(version.optionName, version);
/*     */     }
/*  82 */     return version;
/*     */   }
/*     */ 
/*     */   
/*  86 */   public static RegionFileVersion fromId(int id) { return (RegionFileVersion)VERSIONS.get(id); }
/*     */ 
/*     */   
/*     */   public static void configure(String optionName) {
/*  90 */     RegionFileVersion version = (RegionFileVersion)VERSIONS_BY_NAME.get(optionName);
/*  91 */     if (version != null) {
/*  92 */       selected = version;
/*     */     } else {
/*  94 */       LOGGER.error("Invalid `region-file-compression` value `{}` in server.properties. Please use one of: {}", optionName, String.join(", ", VERSIONS_BY_NAME.keySet()));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  99 */   public static RegionFileVersion getSelected() { return selected; }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static boolean isValidVersion(int version) { return VERSIONS.containsKey(version); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public int getId() { return this.id; }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public OutputStream wrap(OutputStream is) throws IOException { return (OutputStream)this.outputWrapper.wrap(is); }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public InputStream wrap(InputStream is) throws IOException { return (InputStream)this.inputWrapper.wrap(is); }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface StreamWrapper<O> {
/*     */     O wrap(O param1O) throws IOException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\RegionFileVersion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
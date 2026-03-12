/*     */ package net.minecraft.server.packs.metadata.pack;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.InclusiveRange;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class PackFormat extends Record implements Comparable<PackFormat> {
/*     */   private final int major;
/*     */   private final int minor;
/*     */   
/*  22 */   public PackFormat(int major, int minor) { this.major = major; this.minor = minor; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/metadata/pack/PackFormat;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #22	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/packs/metadata/pack/PackFormat; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/metadata/pack/PackFormat;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #22	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/packs/metadata/pack/PackFormat;
/*  22 */     //   0	8	1	o	Ljava/lang/Object; } public int major() { return this.major; } public int minor() { return this.minor; }
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */   
/*     */   private static Codec<PackFormat> fullCodec(int defaultMinor) {
/*  27 */     return ExtraCodecs.compactListCodec(ExtraCodecs.NON_NEGATIVE_INT, ExtraCodecs.NON_NEGATIVE_INT.listOf(1, 256)).xmap(list -> 
/*  28 */         (list.size() > 1) ? of(((Integer)list.getFirst()).intValue(), ((Integer)list.get(1)).intValue()) : of(((Integer)list.getFirst()).intValue(), defaultMinor), pf -> 
/*  29 */         (pf.minor != defaultMinor) ? List.of(Integer.valueOf(pf.major()), Integer.valueOf(pf.minor())) : List.of(Integer.valueOf(pf.major())));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  34 */   public static final Codec<PackFormat> BOTTOM_CODEC = fullCodec(0);
/*     */ 
/*     */   
/*  37 */   public static final Codec<PackFormat> TOP_CODEC = fullCodec(2147483647);
/*     */   public static interface IntermediaryFormatHolder {
/*     */     PackFormat.IntermediaryFormat format(); }
/*     */   public static final class IntermediaryFormat extends Record { private final Optional<PackFormat> min;
/*     */     private final Optional<PackFormat> max;
/*     */     private final Optional<Integer> format;
/*     */     private final Optional<InclusiveRange<Integer>> supported;
/*     */     
/*  45 */     public IntermediaryFormat(Optional<PackFormat> min, Optional<PackFormat> max, Optional<Integer> format, Optional<InclusiveRange<Integer>> supported) { this.min = min; this.max = max; this.format = format; this.supported = supported; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/metadata/pack/PackFormat$IntermediaryFormat;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  45 */       //   0	7	0	this	Lnet/minecraft/server/packs/metadata/pack/PackFormat$IntermediaryFormat; } public Optional<PackFormat> min() { return this.min; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/metadata/pack/PackFormat$IntermediaryFormat;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/metadata/pack/PackFormat$IntermediaryFormat; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/metadata/pack/PackFormat$IntermediaryFormat;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/metadata/pack/PackFormat$IntermediaryFormat;
/*  45 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<PackFormat> max() { return this.max; } public Optional<Integer> format() { return this.format; } public Optional<InclusiveRange<Integer>> supported() { return this.supported; }
/*  46 */     private static final MapCodec<IntermediaryFormat> PACK_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(PackFormat.BOTTOM_CODEC
/*  47 */           .optionalFieldOf("min_format").forGetter(IntermediaryFormat::min), PackFormat.TOP_CODEC
/*  48 */           .optionalFieldOf("max_format").forGetter(IntermediaryFormat::max), Codec.INT
/*  49 */           .optionalFieldOf("pack_format").forGetter(IntermediaryFormat::format), 
/*  50 */           InclusiveRange.codec(Codec.INT).optionalFieldOf("supported_formats").forGetter(IntermediaryFormat::supported))
/*  51 */         .apply(i, IntermediaryFormat::new));
/*     */     
/*  53 */     public static final MapCodec<IntermediaryFormat> OVERLAY_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(PackFormat.BOTTOM_CODEC
/*  54 */           .optionalFieldOf("min_format").forGetter(IntermediaryFormat::min), PackFormat.TOP_CODEC
/*  55 */           .optionalFieldOf("max_format").forGetter(IntermediaryFormat::max), 
/*  56 */           InclusiveRange.codec(Codec.INT).optionalFieldOf("formats").forGetter(IntermediaryFormat::supported))
/*  57 */         .apply(i, ()));
/*     */     
/*     */     public static IntermediaryFormat fromRange(InclusiveRange<PackFormat> range, int lastPreMinorVersion) {
/*  60 */       InclusiveRange<Integer> majorRange = range.map(PackFormat::major);
/*  61 */       return new IntermediaryFormat(
/*  62 */           Optional.of((PackFormat)range.minInclusive()), 
/*  63 */           Optional.of((PackFormat)range.maxInclusive()), 
/*  64 */           majorRange.isValueInRange(Integer.valueOf(lastPreMinorVersion)) ? Optional.of((Integer)majorRange.minInclusive()) : Optional.empty(), 
/*  65 */           majorRange.isValueInRange(Integer.valueOf(lastPreMinorVersion)) ? Optional.of(new InclusiveRange((Integer)majorRange.minInclusive(), (Integer)majorRange.maxInclusive())) : Optional.empty());
/*     */     }
/*     */ 
/*     */     
/*     */     public int effectiveMinMajorVersion() {
/*  70 */       if (this.min.isPresent()) {
/*  71 */         if (this.supported.isPresent()) {
/*  72 */           return Math.min(((PackFormat)this.min.get()).major(), ((Integer)((InclusiveRange)this.supported.get()).minInclusive()).intValue());
/*     */         }
/*  74 */         return ((PackFormat)this.min.get()).major();
/*     */       } 
/*  76 */       if (this.supported.isPresent()) {
/*  77 */         return ((Integer)((InclusiveRange)this.supported.get()).minInclusive()).intValue();
/*     */       }
/*  79 */       return Integer.MAX_VALUE;
/*     */     }
/*     */     
/*     */     public DataResult<InclusiveRange<PackFormat>> validate(int lastPreMinorVersion, boolean hasPackFormatField, boolean requireOldField, String context, String oldFieldName) {
/*  83 */       if (this.min.isPresent() != this.max.isPresent()) {
/*  84 */         return DataResult.error(() -> context + " missing field, must declare both min_format and max_format");
/*     */       }
/*     */       
/*  87 */       if (requireOldField && this.supported.isEmpty()) {
/*  88 */         return DataResult.error(() -> context + " missing required field " + context + ", must be present in all overlays for any overlays to work across game versions");
/*     */       }
/*     */ 
/*     */       
/*  92 */       if (this.min.isPresent()) {
/*  93 */         return validateNewFormat(lastPreMinorVersion, hasPackFormatField, requireOldField, context, oldFieldName);
/*     */       }
/*     */ 
/*     */       
/*  97 */       if (this.supported.isPresent()) {
/*  98 */         return validateOldFormat(lastPreMinorVersion, hasPackFormatField, context, oldFieldName);
/*     */       }
/*     */ 
/*     */       
/* 102 */       if (hasPackFormatField && this.format.isPresent()) {
/* 103 */         int mainFormat = ((Integer)this.format.get()).intValue();
/* 104 */         if (mainFormat > lastPreMinorVersion) {
/* 105 */           return DataResult.error(() -> context + " declares support for version newer than " + context + ", but is missing mandatory fields min_format and max_format");
/*     */         }
/* 107 */         return DataResult.success(new InclusiveRange(PackFormat.of(mainFormat)));
/*     */       } 
/* 109 */       return DataResult.error(() -> context + " could not be parsed, missing format version information");
/*     */     }
/*     */     
/*     */     private DataResult<InclusiveRange<PackFormat>> validateNewFormat(int lastPreMinorVersion, boolean hasPackFormatField, boolean requireOldField, String context, String oldFieldName) {
/* 113 */       int majorMin = ((PackFormat)this.min.get()).major();
/* 114 */       int majorMax = ((PackFormat)this.max.get()).major();
/* 115 */       if (((PackFormat)this.min.get()).compareTo((PackFormat)this.max.get()) > 0) {
/* 116 */         return DataResult.error(() -> context + " min_format (" + context + ") is greater than max_format (" + String.valueOf(this.min.get()) + ")");
/*     */       }
/* 118 */       if (majorMin > lastPreMinorVersion && !requireOldField) {
/*     */         
/* 120 */         if (this.supported.isPresent()) {
/* 121 */           return DataResult.error(() -> context + " key " + context + " is deprecated starting from pack format " + oldFieldName + ". Remove " + lastPreMinorVersion + 1 + " from your pack.mcmeta.");
/*     */         }
/* 123 */         if (hasPackFormatField && this.format.isPresent()) {
/* 124 */           String packFormatError = validatePackFormatForRange(majorMin, majorMax);
/* 125 */           if (packFormatError != null) {
/* 126 */             return DataResult.error(() -> packFormatError);
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         
/* 131 */         if (this.supported.isPresent()) {
/* 132 */           InclusiveRange<Integer> oldSupportedVersions = (InclusiveRange)this.supported.get();
/* 133 */           if (((Integer)oldSupportedVersions.minInclusive()).intValue() != majorMin) {
/* 134 */             return DataResult.error(() -> context + " version declaration mismatch between " + context + " (from " + oldFieldName + ") and min_format (" + String.valueOf(oldSupportedVersions.minInclusive()) + ")");
/*     */           }
/* 136 */           if (((Integer)oldSupportedVersions.maxInclusive()).intValue() != majorMax && ((Integer)oldSupportedVersions.maxInclusive()).intValue() != lastPreMinorVersion) {
/* 137 */             return DataResult.error(() -> context + " version declaration mismatch between " + context + " (up to " + oldFieldName + ") and max_format (" + String.valueOf(oldSupportedVersions.maxInclusive()) + ")");
/*     */           }
/*     */         } else {
/* 140 */           return DataResult.error(() -> context + " declares support for format " + context + ", but game versions supporting formats 17 to " + majorMin + " require a " + lastPreMinorVersion + " field. Add \"" + oldFieldName + "\": [" + oldFieldName + ", " + majorMin + "] or require a version greater or equal to " + lastPreMinorVersion + ".0.");
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 146 */         if (hasPackFormatField) {
/* 147 */           if (this.format.isPresent()) {
/* 148 */             String packFormatError = validatePackFormatForRange(majorMin, majorMax);
/* 149 */             if (packFormatError != null) {
/* 150 */               return DataResult.error(() -> packFormatError);
/*     */             }
/*     */           } else {
/* 153 */             return DataResult.error(() -> context + " declares support for formats up to " + context + ", but game versions supporting formats 17 to " + lastPreMinorVersion + " require a pack_format field. Add \"pack_format\": " + lastPreMinorVersion + " or require a version greater or equal to " + majorMin + ".0.");
/*     */           } 
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 160 */       return DataResult.success(new InclusiveRange((PackFormat)this.min.get(), (PackFormat)this.max.get()));
/*     */     }
/*     */     
/*     */     private DataResult<InclusiveRange<PackFormat>> validateOldFormat(int lastPreMinorVersion, boolean hasPackFormatField, String context, String oldFieldName) {
/* 164 */       InclusiveRange<Integer> oldSupportedVersions = (InclusiveRange)this.supported.get();
/* 165 */       int min = ((Integer)oldSupportedVersions.minInclusive()).intValue();
/* 166 */       int max = ((Integer)oldSupportedVersions.maxInclusive()).intValue();
/*     */ 
/*     */       
/* 169 */       if (max > lastPreMinorVersion) {
/* 170 */         return DataResult.error(() -> context + " declares support for version newer than " + context + ", but is missing mandatory fields min_format and max_format");
/*     */       }
/* 172 */       if (hasPackFormatField) {
/* 173 */         if (this.format.isPresent()) {
/* 174 */           String packFormatError = validatePackFormatForRange(min, max);
/* 175 */           if (packFormatError != null) {
/* 176 */             return DataResult.error(() -> packFormatError);
/*     */           }
/*     */         } else {
/* 179 */           return DataResult.error(() -> context + " declares support for formats up to " + context + ", but game versions supporting formats 17 to " + lastPreMinorVersion + " require a pack_format field. Add \"pack_format\": " + lastPreMinorVersion + " or require a version greater or equal to " + min + ".0.");
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 185 */       return DataResult.success((new InclusiveRange(Integer.valueOf(min), Integer.valueOf(max))).map(PackFormat::of));
/*     */     }
/*     */     
/*     */     private String validatePackFormatForRange(int min, int max) {
/* 189 */       int mainFormat = ((Integer)this.format.get()).intValue();
/* 190 */       if (mainFormat < min || mainFormat > max) {
/* 191 */         return "Pack declared support for versions " + min + " to " + max + " but declared main format is " + mainFormat;
/*     */       }
/* 193 */       if (mainFormat < 15) {
/* 194 */         return "Multi-version packs cannot support minimum version of less than 15, since this will leave versions in range unable to load pack.";
/*     */       }
/* 196 */       return null;
/*     */     } }
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
/*     */   public static <ResultType, HolderType extends IntermediaryFormatHolder> DataResult<List<ResultType>> validateHolderList(List<HolderType> list, int lastPreMinorVersion, BiFunction<HolderType, InclusiveRange<PackFormat>, ResultType> constructor) {
/* 209 */     int minVersion = list.stream().map(IntermediaryFormatHolder::format).mapToInt(IntermediaryFormat::effectiveMinMajorVersion).min().orElse(2147483647);
/* 210 */     List<ResultType> result = new ArrayList<ResultType>(list.size());
/* 211 */     for (Iterator iterator = list.iterator(); iterator.hasNext(); ) { HolderType entry = (HolderType)(IntermediaryFormatHolder)iterator.next();
/* 212 */       IntermediaryFormat format = entry.format();
/* 213 */       if (format.min().isEmpty() && format.max().isEmpty() && format.supported().isEmpty()) {
/*     */         
/* 215 */         LOGGER.warn("Unknown or broken overlay entry {}", entry);
/*     */         continue;
/*     */       } 
/* 218 */       DataResult<InclusiveRange<PackFormat>> entryResult = format.validate(lastPreMinorVersion, false, (minVersion <= lastPreMinorVersion), "Overlay \"" + String.valueOf(entry) + "\"", "formats");
/* 219 */       if (entryResult.isSuccess()) {
/* 220 */         result.add(constructor.apply(entry, (InclusiveRange)entryResult.getOrThrow())); continue;
/*     */       } 
/* 222 */       Objects.requireNonNull((DataResult.Error)entryResult.error().get()); return DataResult.error((DataResult.Error)entryResult.error().get()::message); }
/*     */ 
/*     */     
/* 225 */     return DataResult.success(List.copyOf(result));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static int lastPreMinorVersion(PackType type) {
/* 230 */     switch (type) { default: throw new MatchException(null, null);case CLIENT_RESOURCES: case SERVER_DATA: break; }  return 
/*     */       
/* 232 */       81;
/*     */   }
/*     */ 
/*     */   
/*     */   public static MapCodec<InclusiveRange<PackFormat>> packCodec(PackType type) {
/* 237 */     int lastPreMinorVersion = lastPreMinorVersion(type);
/* 238 */     return IntermediaryFormat.PACK_CODEC.flatXmap(intermediaryFormat -> 
/* 239 */         intermediaryFormat.validate(lastPreMinorVersion, true, false, "Pack", "supported_formats"), range -> 
/* 240 */         DataResult.success(IntermediaryFormat.fromRange(range, lastPreMinorVersion)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 245 */   public static PackFormat of(int major, int minor) { return new PackFormat(major, minor); }
/*     */ 
/*     */ 
/*     */   
/* 249 */   public static PackFormat of(int major) { return new PackFormat(major, 0); }
/*     */ 
/*     */ 
/*     */   
/* 253 */   public InclusiveRange<PackFormat> minorRange() { return new InclusiveRange(this, of(this.major, 2147483647)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(PackFormat other) {
/* 258 */     int majorDiff = Integer.compare(major(), other.major());
/* 259 */     if (majorDiff != 0) {
/* 260 */       return majorDiff;
/*     */     }
/* 262 */     return Integer.compare(minor(), other.minor());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 267 */     if (this.minor == Integer.MAX_VALUE) {
/* 268 */       return String.format(Locale.ROOT, "%d.*", new Object[] { Integer.valueOf(major()) });
/*     */     }
/* 270 */     return String.format(Locale.ROOT, "%d.%d", new Object[] { Integer.valueOf(major()), Integer.valueOf(minor()) });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\metadata\pack\PackFormat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.data.info;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryDataLoader;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public class DatapackStructureReport
/*     */   implements DataProvider {
/*     */   private final PackOutput output;
/*  29 */   private static final Entry PSEUDO_REGISTRY = new Entry(true, false, true);
/*     */   
/*  31 */   private static final Entry STABLE_DYNAMIC_REGISTRY = new Entry(true, true, true);
/*  32 */   private static final Entry UNSTABLE_DYNAMIC_REGISTRY = new Entry(true, true, false);
/*     */   
/*  34 */   private static final Entry BUILT_IN_REGISTRY = new Entry(false, true, true);
/*     */   
/*  36 */   private static final Map<ResourceKey<? extends Registry<?>>, Entry> MANUAL_ENTRIES = Map.of(Registries.RECIPE, PSEUDO_REGISTRY, Registries.ADVANCEMENT, PSEUDO_REGISTRY, Registries.LOOT_TABLE, STABLE_DYNAMIC_REGISTRY, Registries.ITEM_MODIFIER, STABLE_DYNAMIC_REGISTRY, Registries.PREDICATE, STABLE_DYNAMIC_REGISTRY);
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
/*  47 */   private static final Map<String, CustomPackEntry> NON_REGISTRY_ENTRIES = Map.of("structure", new CustomPackEntry(Format.STRUCTURE, new Entry(true, false, true)), "function", new CustomPackEntry(Format.MCFUNCTION, new Entry(true, true, true)));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private static final Codec<ResourceKey<? extends Registry<?>>> REGISTRY_KEY_CODEC = Identifier.CODEC.xmap(ResourceKey::createRegistryKey, ResourceKey::identifier);
/*     */   private static final class Report extends Record { private final Map<ResourceKey<? extends Registry<?>>, DatapackStructureReport.Entry> registries; private final Map<String, DatapackStructureReport.CustomPackEntry> others;
/*  54 */     private Report(Map<ResourceKey<? extends Registry<?>>, DatapackStructureReport.Entry> registries, Map<String, DatapackStructureReport.CustomPackEntry> others) { this.registries = registries; this.others = others; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/info/DatapackStructureReport$Report;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #54	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  54 */       //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Report; } public Map<ResourceKey<? extends Registry<?>>, DatapackStructureReport.Entry> registries() { return this.registries; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/info/DatapackStructureReport$Report;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #54	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Report; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/info/DatapackStructureReport$Report;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #54	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Report;
/*  54 */       //   0	8	1	o	Ljava/lang/Object; } public Map<String, DatapackStructureReport.CustomPackEntry> others() { return this.others; }
/*     */ 
/*     */ 
/*     */     
/*  58 */     public static final Codec<Report> CODEC = RecordCodecBuilder.create(i -> i.group(
/*  59 */           Codec.unboundedMap(DatapackStructureReport.REGISTRY_KEY_CODEC, DatapackStructureReport.Entry.CODEC).fieldOf("registries").forGetter(Report::registries), 
/*  60 */           Codec.unboundedMap(Codec.STRING, DatapackStructureReport.CustomPackEntry.CODEC).fieldOf("others").forGetter(Report::others))
/*  61 */         .apply(i, Report::new)); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public DatapackStructureReport(PackOutput output) { this.output = output; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<?> run(CachedOutput cache) {
/*  71 */     Report report = new Report(listRegistries(), NON_REGISTRY_ENTRIES);
/*     */ 
/*     */ 
/*     */     
/*  75 */     Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("datapack.json");
/*  76 */     return DataProvider.saveStable(cache, (JsonElement)Report.CODEC.encodeStart(JsonOps.INSTANCE, report).getOrThrow(), path);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public String getName() { return "Datapack Structure"; }
/*     */ 
/*     */   
/*     */   private void putIfNotPresent(Map<ResourceKey<? extends Registry<?>>, Entry> output, ResourceKey<? extends Registry<?>> key, Entry entry) {
/*  85 */     Entry previous = (Entry)output.putIfAbsent(key, entry);
/*  86 */     if (previous != null) {
/*  87 */       throw new IllegalStateException("Duplicate entry for key " + String.valueOf(key.identifier()));
/*     */     }
/*     */   }
/*     */   
/*     */   private Map<ResourceKey<? extends Registry<?>>, Entry> listRegistries() {
/*  92 */     Map<ResourceKey<? extends Registry<?>>, Entry> result = new HashMap<ResourceKey<? extends Registry<?>>, Entry>();
/*     */ 
/*     */     
/*  95 */     BuiltInRegistries.REGISTRY.forEach(entry -> putIfNotPresent(result, entry.key(), BUILT_IN_REGISTRY));
/*  96 */     RegistryDataLoader.WORLDGEN_REGISTRIES.forEach(entry -> putIfNotPresent(result, entry.key(), UNSTABLE_DYNAMIC_REGISTRY));
/*  97 */     RegistryDataLoader.DIMENSION_REGISTRIES.forEach(entry -> putIfNotPresent(result, entry.key(), UNSTABLE_DYNAMIC_REGISTRY));
/*     */ 
/*     */     
/* 100 */     MANUAL_ENTRIES.forEach((key, entry) -> putIfNotPresent(result, key, entry));
/*     */     
/* 102 */     return result;
/*     */   }
/*     */   private static final class Entry extends Record { private final boolean elements; private final boolean tags; private final boolean stable;
/* 105 */     private Entry(boolean elements, boolean tags, boolean stable) { this.elements = elements; this.tags = tags; this.stable = stable; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/info/DatapackStructureReport$Entry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Entry; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/info/DatapackStructureReport$Entry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Entry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/info/DatapackStructureReport$Entry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Entry;
/* 105 */       //   0	8	1	o	Ljava/lang/Object; } public boolean elements() { return this.elements; } public boolean tags() { return this.tags; } public boolean stable() { return this.stable; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     public static final MapCodec<Entry> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.BOOL
/* 111 */           .fieldOf("elements").forGetter(Entry::elements), Codec.BOOL
/* 112 */           .fieldOf("tags").forGetter(Entry::tags), Codec.BOOL
/* 113 */           .fieldOf("stable").forGetter(Entry::stable))
/* 114 */         .apply(i, Entry::new));
/*     */     
/* 116 */     public static final Codec<Entry> CODEC = MAP_CODEC.codec(); }
/*     */ 
/*     */   
/*     */   private enum Format implements StringRepresentable {
/* 120 */     STRUCTURE("structure"),
/* 121 */     MCFUNCTION("mcfunction"); public static final Codec<Format> CODEC; private final String name;
/*     */     
/*     */     static  {
/* 124 */       CODEC = StringRepresentable.fromEnum(Format::values);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 129 */     Format(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     public String getSerializedName() { return this.name; } }
/*     */   private static final class CustomPackEntry extends Record { private final DatapackStructureReport.Format format;
/*     */     private final DatapackStructureReport.Entry entry;
/*     */     
/* 138 */     private CustomPackEntry(DatapackStructureReport.Format format, DatapackStructureReport.Entry entry) { this.format = format; this.entry = entry; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;
/* 138 */       //   0	8	1	o	Ljava/lang/Object; } public DatapackStructureReport.Format format() { return this.format; } public DatapackStructureReport.Entry entry() { return this.entry; }
/*     */ 
/*     */ 
/*     */     
/* 142 */     public static final Codec<CustomPackEntry> CODEC = RecordCodecBuilder.create(i -> i.group(DatapackStructureReport.Format.CODEC
/* 143 */           .fieldOf("format").forGetter(CustomPackEntry::format), DatapackStructureReport.Entry.MAP_CODEC
/* 144 */           .forGetter(CustomPackEntry::entry))
/* 145 */         .apply(i, CustomPackEntry::new)); }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\info\DatapackStructureReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
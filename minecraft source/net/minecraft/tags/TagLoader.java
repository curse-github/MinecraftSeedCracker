/*     */ package net.minecraft.tags;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.SequencedSet;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.FileToIdConverter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.DependencySorter;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TagLoader<T>
/*     */   extends Object
/*     */ {
/*  39 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final ElementLookup<T> elementLookup;
/*     */   private final String directory;
/*     */   
/*     */   public TagLoader(ElementLookup<T> elementLookup, String directory) {
/*  45 */     this.elementLookup = elementLookup;
/*  46 */     this.directory = directory;
/*     */   }
/*     */   
/*     */   public Map<Identifier, List<EntryWithSource>> load(ResourceManager resourceManager) {
/*  50 */     Map<Identifier, List<EntryWithSource>> builders = new HashMap<Identifier, List<EntryWithSource>>();
/*     */     
/*  52 */     FileToIdConverter lister = FileToIdConverter.json(this.directory);
/*     */     
/*  54 */     for (Map.Entry<Identifier, List<Resource>> entry : lister.listMatchingResourceStacks(resourceManager).entrySet()) {
/*  55 */       Identifier location = (Identifier)entry.getKey();
/*  56 */       Identifier id = lister.fileToId(location);
/*  57 */       for (Resource resource : (List)entry.getValue()) { 
/*  58 */         try { Reader reader = resource.openAsReader(); 
/*  59 */           try { JsonElement element = StrictJsonParser.parse(reader);
/*  60 */             List<EntryWithSource> tagContents = (List)builders.computeIfAbsent(id, key -> new ArrayList());
/*  61 */             TagFile parsedContents = (TagFile)TagFile.CODEC.parse(new Dynamic(JsonOps.INSTANCE, element)).getOrThrow();
/*  62 */             if (parsedContents.replace()) {
/*  63 */               tagContents.clear();
/*     */             }
/*  65 */             String sourceId = resource.sourcePackId();
/*  66 */             parsedContents.entries().forEach(e -> tagContents.add(new EntryWithSource(e, sourceId)));
/*  67 */             if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/*  68 */         { LOGGER.error("Couldn't read tag list {} from {} in data pack {}", new Object[] { id, location, resource.sourcePackId(), e }); }
/*     */          }
/*     */     
/*     */     } 
/*     */     
/*  73 */     return builders;
/*     */   }
/*     */   public static final class EntryWithSource extends Record { private final TagEntry entry; private final String source;
/*  76 */     public EntryWithSource(TagEntry entry, String source) { this.entry = entry; this.source = source; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/tags/TagLoader$EntryWithSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/tags/TagLoader$EntryWithSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/tags/TagLoader$EntryWithSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/tags/TagLoader$EntryWithSource;
/*  76 */       //   0	8	1	o	Ljava/lang/Object; } public TagEntry entry() { return this.entry; } public String source() { return this.source; }
/*     */     
/*     */     public String toString() {
/*  79 */       return String.valueOf(this.entry) + " (from " + String.valueOf(this.entry) + ")";
/*     */     } }
/*     */   private static final class SortingEntry extends Record implements DependencySorter.Entry<Identifier> { private final List<TagLoader.EntryWithSource> entries;
/*     */     
/*  83 */     private SortingEntry(List<TagLoader.EntryWithSource> entries) { this.entries = entries; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/tags/TagLoader$SortingEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #83	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/tags/TagLoader$SortingEntry; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/tags/TagLoader$SortingEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #83	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/tags/TagLoader$SortingEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/tags/TagLoader$SortingEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #83	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/tags/TagLoader$SortingEntry;
/*  83 */       //   0	8	1	o	Ljava/lang/Object; } public List<TagLoader.EntryWithSource> entries() { return this.entries; }
/*     */ 
/*     */     
/*  86 */     public void visitRequiredDependencies(Consumer<Identifier> output) { this.entries.forEach(e -> e.entry.visitRequiredDependencies(output)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     public void visitOptionalDependencies(Consumer<Identifier> output) { this.entries.forEach(e -> e.entry.visitOptionalDependencies(output)); } }
/*     */ 
/*     */ 
/*     */   
/*     */   private Either<List<EntryWithSource>, List<T>> tryBuildTag(TagEntry.Lookup<T> lookup, List<EntryWithSource> entries) {
/*  96 */     SequencedSet<T> values = new LinkedHashSet<T>();
/*  97 */     List<EntryWithSource> missingElements = new ArrayList<EntryWithSource>();
/*  98 */     for (EntryWithSource entry : entries) {
/*  99 */       Objects.requireNonNull(values); if (!entry.entry().build(lookup, values::add)) {
/* 100 */         missingElements.add(entry);
/*     */       }
/*     */     } 
/* 103 */     return missingElements.isEmpty() ? Either.right(List.copyOf(values)) : Either.left(missingElements);
/*     */   }
/*     */   
/*     */   public Map<Identifier, List<T>> build(Map<Identifier, List<EntryWithSource>> builders) {
/* 107 */     final Map<Identifier, List<T>> newTags = new HashMap<Identifier, List<T>>();
/*     */     
/* 109 */     TagEntry.Lookup<T> lookup = new TagEntry.Lookup<T>()
/*     */       {
/*     */         public T element(Identifier key, boolean required) {
/* 112 */           return (T)TagLoader.this.elementLookup.get(key, required).orElse(null);
/*     */         }
/*     */ 
/*     */         
/*     */         public Collection<T> tag(Identifier key) {
/* 117 */           return (Collection)newTags.get(key);
/*     */         }
/*     */       };
/*     */     
/* 121 */     DependencySorter<Identifier, SortingEntry> sorter = new DependencySorter<Identifier, SortingEntry>();
/*     */     
/* 123 */     builders.forEach((id, entry) -> sorter.addEntry(id, new SortingEntry(entry)));
/*     */     
/* 125 */     sorter.orderByDependencies((id, contents) -> tryBuildTag(lookup, contents.entries)
/* 126 */         .ifLeft(())
/* 127 */         .ifRight(()));
/*     */ 
/*     */     
/* 130 */     return newTags;
/*     */   }
/*     */ 
/*     */   
/* 134 */   public static <T> void loadTagsFromNetwork(TagNetworkSerialization.NetworkPayload tags, WritableRegistry<T> registry) { Objects.requireNonNull(registry); (tags.resolve(registry)).tags.forEach(registry::bindTag); }
/*     */ 
/*     */   
/*     */   public static List<Registry.PendingTags<?>> loadTagsForExistingRegistries(ResourceManager manager, RegistryAccess layer) {
/* 138 */     return (List)layer.registries()
/* 139 */       .map(entry -> loadPendingTags(manager, entry.value()))
/* 140 */       .flatMap(Optional::stream)
/* 141 */       .collect(Collectors.toUnmodifiableList());
/*     */   }
/*     */   
/*     */   public static <T> void loadTagsForRegistry(ResourceManager manager, WritableRegistry<T> registry) {
/* 145 */     ResourceKey<? extends Registry<T>> key = registry.key();
/*     */     
/* 147 */     TagLoader<Holder<T>> loader = new TagLoader<Holder<T>>(ElementLookup.fromWritableRegistry(registry), Registries.tagsDirPath(key));
/* 148 */     loader.build(loader.load(manager))
/* 149 */       .forEach((tagId, values) -> registry.bindTag(TagKey.create(key, tagId), values));
/*     */   }
/*     */ 
/*     */   
/* 153 */   private static <T> Map<TagKey<T>, List<Holder<T>>> wrapTags(ResourceKey<? extends Registry<T>> registryKey, Map<Identifier, List<Holder<T>>> tags) { return (Map)tags.entrySet().stream().collect(Collectors.toUnmodifiableMap(e -> 
/* 154 */           TagKey.create(registryKey, (Identifier)e.getKey()), Map.Entry::getValue)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Optional<Registry.PendingTags<T>> loadPendingTags(ResourceManager manager, Registry<T> registry) {
/* 160 */     ResourceKey<? extends Registry<T>> key = registry.key();
/* 161 */     TagLoader<Holder<T>> loader = new TagLoader<Holder<T>>(ElementLookup.fromFrozenRegistry(registry), Registries.tagsDirPath(key));
/* 162 */     LoadResult<T> tags = new LoadResult<T>(key, wrapTags(registry.key(), loader.build(loader.load(manager))));
/* 163 */     return tags.tags().isEmpty() ? Optional.empty() : Optional.of(registry.prepareTagReload(tags));
/*     */   }
/*     */   
/*     */   public static List<HolderLookup.RegistryLookup<?>> buildUpdatedLookups(RegistryAccess.Frozen registries, List<Registry.PendingTags<?>> tags) {
/* 167 */     List<HolderLookup.RegistryLookup<?>> result = new ArrayList<HolderLookup.RegistryLookup<?>>();
/* 168 */     registries.registries().forEach(lookup -> {
/* 169 */           Registry.PendingTags<?> foundTags = findTagsForRegistry(tags, lookup.key());
/* 170 */           result.add((foundTags != null) ? foundTags.lookup() : lookup.value());
/*     */         });
/* 172 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Registry.PendingTags<?> findTagsForRegistry(List<Registry.PendingTags<?>> tags, ResourceKey<? extends Registry<?>> registryKey) {
/* 177 */     for (Registry.PendingTags<?> tag : tags) {
/* 178 */       if (tag.key() == registryKey) {
/* 179 */         return tag;
/*     */       }
/*     */     } 
/* 182 */     return null;
/*     */   }
/*     */   public static final class LoadResult<T> extends Record { private final ResourceKey<? extends Registry<T>> key; private final Map<TagKey<T>, List<Holder<T>>> tags;
/* 185 */     public LoadResult(ResourceKey<? extends Registry<T>> key, Map<TagKey<T>, List<Holder<T>>> tags) { this.key = key; this.tags = tags; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/tags/TagLoader$LoadResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #185	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/tags/TagLoader$LoadResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/tags/TagLoader$LoadResult<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/tags/TagLoader$LoadResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #185	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/tags/TagLoader$LoadResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/tags/TagLoader$LoadResult<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/tags/TagLoader$LoadResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #185	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/tags/TagLoader$LoadResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 185 */       //   0	8	0	this	Lnet/minecraft/tags/TagLoader$LoadResult<TT;>; } public ResourceKey<? extends Registry<T>> key() { return this.key; } public Map<TagKey<T>, List<Holder<T>>> tags() { return this.tags; } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface ElementLookup<T>
/*     */   {
/* 192 */     static <T> ElementLookup<? extends Holder<T>> fromFrozenRegistry(Registry<T> registry) { return (id, required) -> registry.get(id); }
/*     */ 
/*     */     
/*     */     static <T> ElementLookup<Holder<T>> fromWritableRegistry(WritableRegistry<T> registry) {
/* 196 */       HolderGetter<T> registrationLookup = registry.createRegistrationLookup();
/*     */       
/* 198 */       return (id, required) -> (required ? registrationLookup : registry).get(ResourceKey.create(registry.key(), id));
/*     */     }
/*     */     
/*     */     Optional<? extends T> get(Identifier param1Identifier, boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\tags\TagLoader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
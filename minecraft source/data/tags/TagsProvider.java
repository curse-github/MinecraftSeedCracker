/*     */ package net.minecraft.data.tags;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagBuilder;
/*     */ import net.minecraft.tags.TagEntry;
/*     */ import net.minecraft.tags.TagFile;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public abstract class TagsProvider<T>
/*     */   extends Object
/*     */   implements DataProvider {
/*     */   protected final PackOutput.PathProvider pathProvider;
/*     */   private final CompletableFuture<HolderLookup.Provider> lookupProvider;
/*     */   private final CompletableFuture<Void> contentsDone;
/*     */   private final CompletableFuture<TagLookup<T>> parentProvider;
/*     */   protected final ResourceKey<? extends Registry<T>> registryKey;
/*     */   private final Map<Identifier, TagBuilder> builders;
/*     */   
/*  38 */   protected TagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider) { this(output, registryKey, lookupProvider, CompletableFuture.completedFuture(TagLookup.empty())); }
/*     */   protected TagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<T>> parentProvider) {
/*     */     this.contentsDone = new CompletableFuture();
/*     */     this.builders = Maps.newLinkedHashMap();
/*  42 */     this.pathProvider = output.createRegistryTagsPathProvider(registryKey);
/*  43 */     this.registryKey = registryKey;
/*     */     
/*  45 */     this.parentProvider = parentProvider;
/*  46 */     this.lookupProvider = lookupProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public final String getName() { return "Tags for " + String.valueOf(this.registryKey.identifier()); }
/*     */   public CompletableFuture<?> run(CachedOutput cache) {
/*     */     static final class CombinedData<T> extends Record { private final HolderLookup.Provider contents; private final TagsProvider.TagLookup<T> parent;
/*     */       public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/data/tags/TagsProvider$1CombinedData;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #58	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData;
/*     */         // Local variable type table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	7	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData<TT;>; }
/*     */       
/*     */       public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/tags/TagsProvider$1CombinedData;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #58	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData;
/*     */         // Local variable type table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	7	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData<TT;>; }
/*     */       
/*  58 */       CombinedData(HolderLookup.Provider contents, TagsProvider.TagLookup<T> parent) { this.contents = contents; this.parent = parent; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/data/tags/TagsProvider$1CombinedData;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #58	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */         // Local variable type table:
/*     */         //   start	length	slot	name	signature
/*  58 */         //   0	8	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData<TT;>; } public HolderLookup.Provider contents() { return this.contents; } public TagsProvider.TagLookup<T> parent() { return this.parent; } }
/*     */     ;
/*  60 */     return createContentsProvider()
/*  61 */       .thenApply(provider -> {
/*  62 */           this.contentsDone.complete(null);
/*  63 */           return provider;
/*     */         
/*  65 */         }).thenCombineAsync(this.parentProvider, (x$0, x$1) -> new CombinedData(x$0, x$1), Util.backgroundExecutor())
/*  66 */       .thenCompose(c -> {
/*  67 */           HolderLookup.RegistryLookup<T> lookup = c.contents.lookupOrThrow(this.registryKey);
/*  68 */           Predicate<Identifier> elementCheck = ();
/*  69 */           Predicate<Identifier> tagCheck = ();
/*     */           
/*  71 */           return CompletableFuture.allOf((CompletableFuture[])this.builders.entrySet().stream()
/*  72 */               .map(())
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
/*     */ 
/*     */               
/*  85 */               .toArray(()));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*  90 */   protected TagBuilder getOrCreateRawBuilder(TagKey<T> tag) { return (TagBuilder)this.builders.computeIfAbsent(tag.location(), k -> TagBuilder.create()); }
/*     */ 
/*     */   
/*     */   public CompletableFuture<TagLookup<T>> contentsGetter() {
/*  94 */     return this.contentsDone.thenApply(ignore -> ());
/*     */   }
/*     */   
/*     */   protected CompletableFuture<HolderLookup.Provider> createContentsProvider() {
/*  98 */     return this.lookupProvider.thenApply(registries -> {
/*  99 */           this.builders.clear();
/* 100 */           addTags(registries);
/* 101 */           return registries;
/*     */         });
/*     */   }
/*     */   
/*     */   protected abstract void addTags(HolderLookup.Provider paramProvider);
/*     */   
/*     */   @FunctionalInterface
/* 108 */   public static interface TagLookup<T> extends Function<TagKey<T>, Optional<TagBuilder>> { static <T> TagLookup<T> empty() { return id -> Optional.empty(); }
/*     */ 
/*     */ 
/*     */     
/* 112 */     default boolean contains(TagKey<T> key) { return ((Optional)apply(key)).isPresent(); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\tags\TagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
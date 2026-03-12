/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectList;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.tags.TagLoader;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MappedRegistry<T>
/*     */   extends Object
/*     */   implements WritableRegistry<T>
/*     */ {
/*     */   private final ResourceKey<? extends Registry<T>> key;
/*     */   private final ObjectList<Holder.Reference<T>> byId;
/*     */   private final Reference2IntMap<T> toId;
/*     */   private final Map<Identifier, Holder.Reference<T>> byLocation;
/*     */   private final Map<ResourceKey<T>, Holder.Reference<T>> byKey;
/*     */   private final Map<T, Holder.Reference<T>> byValue;
/*     */   private final Map<ResourceKey<T>, RegistrationInfo> registrationInfos;
/*     */   private Lifecycle registryLifecycle;
/*     */   private final Map<TagKey<T>, HolderSet.Named<T>> frozenTags;
/*     */   private TagSet<T> allTags;
/*     */   private boolean frozen;
/*     */   private Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders;
/*     */   
/*  61 */   public Stream<HolderSet.Named<T>> listTags() { return getTags(); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public MappedRegistry(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle) { this(key, lifecycle, false); } public MappedRegistry(ResourceKey<? extends Registry<T>> key, Lifecycle initialLifecycle, boolean intrusiveHolders) { this.byId = new ObjectArrayList(256); this.toId = (Reference2IntMap)Util.make(new Reference2IntOpenHashMap(), t -> t.defaultReturnValue(-1)); this.byLocation = new HashMap(); this.byKey = new HashMap(); this.byValue = new IdentityHashMap();
/*     */     this.registrationInfos = new IdentityHashMap();
/*     */     this.frozenTags = new IdentityHashMap();
/*     */     this.allTags = TagSet.unbound();
/*  69 */     this.key = key;
/*  70 */     this.registryLifecycle = initialLifecycle;
/*  71 */     if (intrusiveHolders) {
/*  72 */       this.unregisteredIntrusiveHolders = new IdentityHashMap();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public ResourceKey<? extends Registry<T>> key() { return this.key; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public String toString() { return "Registry[" + String.valueOf(this.key) + " (" + String.valueOf(this.registryLifecycle) + ")]"; }
/*     */ 
/*     */   
/*     */   private void validateWrite() {
/*  87 */     if (this.frozen) {
/*  88 */       throw new IllegalStateException("Registry is already frozen");
/*     */     }
/*     */   }
/*     */   
/*     */   private void validateWrite(ResourceKey<T> key) {
/*  93 */     if (this.frozen) {
/*  94 */       throw new IllegalStateException("Registry is already frozen (trying to add key " + String.valueOf(key) + ")");
/*     */     }
/*     */   }
/*     */   
/*     */   public Holder.Reference<T> register(ResourceKey<T> key, T value, RegistrationInfo registrationInfo) {
/*     */     Holder.Reference<T> holder;
/* 100 */     validateWrite(key);
/* 101 */     Objects.requireNonNull(key);
/* 102 */     Objects.requireNonNull(value);
/*     */     
/* 104 */     if (this.byLocation.containsKey(key.identifier())) {
/* 105 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Adding duplicate key '" + String.valueOf(key) + "' to registry"));
/*     */     }
/*     */     
/* 108 */     if (this.byValue.containsKey(value)) {
/* 109 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Adding duplicate value '" + String.valueOf(value) + "' to registry"));
/*     */     }
/*     */ 
/*     */     
/* 113 */     if (this.unregisteredIntrusiveHolders != null) {
/*     */       
/* 115 */       holder = (Holder.Reference)this.unregisteredIntrusiveHolders.remove(value);
/* 116 */       if (holder == null) {
/* 117 */         throw new AssertionError("Missing intrusive holder for " + String.valueOf(key) + ":" + String.valueOf(value));
/*     */       }
/* 119 */       holder.bindKey(key);
/*     */     } else {
/*     */       
/* 122 */       holder = (Holder.Reference)this.byKey.computeIfAbsent(key, k -> Holder.Reference.createStandAlone(this, k));
/*     */     } 
/*     */     
/* 125 */     this.byKey.put(key, holder);
/* 126 */     this.byLocation.put(key.identifier(), holder);
/* 127 */     this.byValue.put(value, holder);
/*     */     
/* 129 */     int newId = this.byId.size();
/* 130 */     this.byId.add(holder);
/* 131 */     this.toId.put(value, newId);
/*     */     
/* 133 */     this.registrationInfos.put(key, registrationInfo);
/* 134 */     this.registryLifecycle = this.registryLifecycle.add(registrationInfo.lifecycle());
/* 135 */     return holder;
/*     */   }
/*     */ 
/*     */   
/*     */   public Identifier getKey(T thing) {
/* 140 */     Holder.Reference<T> holder = (Holder.Reference)this.byValue.get(thing);
/* 141 */     return (holder != null) ? holder.key().identifier() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 146 */   public Optional<ResourceKey<T>> getResourceKey(T thing) { return Optional.ofNullable((Holder.Reference)this.byValue.get(thing)).map(Holder.Reference::key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   public int getId(T thing) { return this.toId.getInt(thing); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   public T getValue(ResourceKey<T> key) { return (T)getValueFromNullable((Holder.Reference)this.byKey.get(key)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public T byId(int id) {
/* 161 */     if (id < 0 || id >= this.byId.size()) {
/* 162 */       return null;
/*     */     }
/* 164 */     return (T)((Holder.Reference)this.byId.get(id)).value();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Holder.Reference<T>> get(int id) {
/* 169 */     if (id < 0 || id >= this.byId.size()) {
/* 170 */       return Optional.empty();
/*     */     }
/* 172 */     return Optional.ofNullable((Holder.Reference)this.byId.get(id));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 177 */   public Optional<Holder.Reference<T>> get(Identifier id) { return Optional.ofNullable((Holder.Reference)this.byLocation.get(id)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   public Optional<Holder.Reference<T>> get(ResourceKey<T> id) { return Optional.ofNullable((Holder.Reference)this.byKey.get(id)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 187 */   public Optional<Holder.Reference<T>> getAny() { return this.byId.isEmpty() ? Optional.empty() : Optional.of((Holder.Reference)this.byId.getFirst()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Holder<T> wrapAsHolder(T value) {
/* 192 */     Holder.Reference<T> existingHolder = (Holder.Reference)this.byValue.get(value);
/* 193 */     return (existingHolder != null) ? existingHolder : Holder.direct(value);
/*     */   }
/*     */   
/*     */   private Holder.Reference<T> getOrCreateHolderOrThrow(ResourceKey<T> key) {
/* 197 */     return (Holder.Reference)this.byKey.computeIfAbsent(key, id -> {
/* 198 */           if (this.unregisteredIntrusiveHolders != null) {
/* 199 */             throw new IllegalStateException("This registry can't create new holders without value");
/*     */           }
/* 201 */           validateWrite(id);
/* 202 */           return Holder.Reference.createStandAlone(this, id);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 208 */   public int size() { return this.byKey.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 213 */   public Optional<RegistrationInfo> registrationInfo(ResourceKey<T> element) { return Optional.ofNullable((RegistrationInfo)this.registrationInfos.get(element)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 218 */   public Lifecycle registryLifecycle() { return this.registryLifecycle; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 223 */   public Iterator<T> iterator() { return Iterators.transform(this.byId.iterator(), Holder::value); }
/*     */ 
/*     */ 
/*     */   
/*     */   public T getValue(Identifier key) {
/* 228 */     Holder.Reference<T> result = (Holder.Reference)this.byLocation.get(key);
/* 229 */     return (T)getValueFromNullable(result);
/*     */   }
/*     */ 
/*     */   
/* 233 */   private static <T> T getValueFromNullable(Holder.Reference<T> result) { return (T)((result != null) ? result.value() : null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 238 */   public Set<Identifier> keySet() { return Collections.unmodifiableSet(this.byLocation.keySet()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 243 */   public Set<ResourceKey<T>> registryKeySet() { return Collections.unmodifiableSet(this.byKey.keySet()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 248 */   public Set<Map.Entry<ResourceKey<T>, T>> entrySet() { return Collections.unmodifiableSet(Util.mapValuesLazy(this.byKey, Holder::value).entrySet()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 253 */   public Stream<Holder.Reference<T>> listElements() { return this.byId.stream(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 258 */   public Stream<HolderSet.Named<T>> getTags() { return this.allTags.getTags(); }
/*     */ 
/*     */ 
/*     */   
/* 262 */   private HolderSet.Named<T> getOrCreateTagForRegistration(TagKey<T> tag) { return (HolderSet.Named)this.frozenTags.computeIfAbsent(tag, this::createTag); }
/*     */ 
/*     */ 
/*     */   
/* 266 */   private HolderSet.Named<T> createTag(TagKey<T> tag) { return new HolderSet.Named(this, tag); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 271 */   public boolean isEmpty() { return this.byKey.isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 276 */   public Optional<Holder.Reference<T>> getRandom(RandomSource random) { return Util.getRandomSafe(this.byId, random); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 281 */   public boolean containsKey(Identifier key) { return this.byLocation.containsKey(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 286 */   public boolean containsKey(ResourceKey<T> key) { return this.byKey.containsKey(key); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Registry<T> freeze() {
/* 291 */     if (this.frozen) {
/* 292 */       return this;
/*     */     }
/* 294 */     this.frozen = true;
/* 295 */     this.byValue.forEach((value, holder) -> holder.bindValue(value));
/*     */     
/* 297 */     List<Identifier> unboundEntries = this.byKey.entrySet().stream().filter(e -> !((Holder.Reference)e.getValue()).isBound()).map(e -> ((ResourceKey)e.getKey()).identifier()).sorted().toList();
/* 298 */     if (!unboundEntries.isEmpty()) {
/* 299 */       throw new IllegalStateException("Unbound values in registry " + String.valueOf(key()) + ": " + String.valueOf(unboundEntries));
/*     */     }
/* 301 */     if (this.unregisteredIntrusiveHolders != null) {
/* 302 */       if (!this.unregisteredIntrusiveHolders.isEmpty()) {
/* 303 */         throw new IllegalStateException("Some intrusive holders were not registered: " + String.valueOf(this.unregisteredIntrusiveHolders.values()));
/*     */       }
/* 305 */       this.unregisteredIntrusiveHolders = null;
/*     */     } 
/*     */     
/* 308 */     if (this.allTags.isBound())
/*     */     {
/* 310 */       throw new IllegalStateException("Tags already present before freezing");
/*     */     }
/* 312 */     List<Identifier> unboundTags = this.frozenTags.entrySet().stream().filter(e -> !((HolderSet.Named)e.getValue()).isBound()).map(e -> ((TagKey)e.getKey()).location()).sorted().toList();
/* 313 */     if (!unboundTags.isEmpty()) {
/* 314 */       throw new IllegalStateException("Unbound tags in registry " + String.valueOf(key()) + ": " + String.valueOf(unboundTags));
/*     */     }
/*     */     
/* 317 */     this.allTags = TagSet.fromMap(this.frozenTags);
/* 318 */     refreshTagsInHolders();
/* 319 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Holder.Reference<T> createIntrusiveHolder(T value) {
/* 324 */     if (this.unregisteredIntrusiveHolders == null) {
/* 325 */       throw new IllegalStateException("This registry can't create intrusive holders");
/*     */     }
/* 327 */     validateWrite();
/* 328 */     return (Holder.Reference)this.unregisteredIntrusiveHolders.computeIfAbsent(value, v -> Holder.Reference.createIntrusive(this, v));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 333 */   public Optional<HolderSet.Named<T>> get(TagKey<T> id) { return this.allTags.get(id); }
/*     */ 
/*     */   
/*     */   private Holder.Reference<T> validateAndUnwrapTagElement(TagKey<T> id, Holder<T> value) {
/* 337 */     if (!value.canSerializeIn(this)) {
/* 338 */       throw new IllegalStateException("Can't create named set " + String.valueOf(id) + " containing value " + String.valueOf(value) + " from outside registry " + String.valueOf(this));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 343 */     if (value instanceof Holder.Reference) return (Holder.Reference)value;
/*     */ 
/*     */     
/* 346 */     throw new IllegalStateException("Found direct holder " + String.valueOf(value) + " value in tag " + String.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void bindTag(TagKey<T> id, List<Holder<T>> values) {
/* 352 */     validateWrite();
/* 353 */     getOrCreateTagForRegistration(id).bind(values);
/*     */   }
/*     */   
/*     */   private void refreshTagsInHolders() {
/* 357 */     Map<Holder.Reference<T>, List<TagKey<T>>> tagsForElement = new IdentityHashMap<Holder.Reference<T>, List<TagKey<T>>>();
/* 358 */     this.byKey.values().forEach(h -> tagsForElement.put(h, new ArrayList()));
/* 359 */     this.allTags.forEach((id, values) -> {
/* 360 */           for (Holder<T> value : values) {
/* 361 */             Holder.Reference<T> reference = validateAndUnwrapTagElement(id, value);
/* 362 */             ((List)tagsForElement.get(reference)).add(id);
/*     */           } 
/*     */         });
/* 365 */     tagsForElement.forEach(Holder.Reference::bindTags);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bindAllTagsToEmpty() {
/* 372 */     validateWrite();
/* 373 */     this.frozenTags.values().forEach(e -> e.bind(List.of()));
/*     */   }
/*     */ 
/*     */   
/*     */   public HolderGetter<T> createRegistrationLookup() {
/* 378 */     validateWrite();
/* 379 */     return new HolderGetter<T>()
/*     */       {
/*     */         public Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/* 382 */           return Optional.of(getOrThrow(id));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 387 */         public Holder.Reference<T> getOrThrow(ResourceKey<T> id) { return MappedRegistry.this.getOrCreateHolderOrThrow(id); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 392 */         public Optional<HolderSet.Named<T>> get(TagKey<T> id) { return Optional.of(getOrThrow(id)); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 397 */         public HolderSet.Named<T> getOrThrow(TagKey<T> id) { return MappedRegistry.this.getOrCreateTagForRegistration(id); }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Registry.PendingTags<T> prepareTagReload(TagLoader.LoadResult<T> tags) {
/* 404 */     if (!this.frozen) {
/* 405 */       throw new IllegalStateException("Invalid method used for tag loading");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 413 */     ImmutableMap.Builder<TagKey<T>, HolderSet.Named<T>> pendingTagsBuilder = ImmutableMap.builder();
/* 414 */     final Map<TagKey<T>, List<Holder<T>>> pendingContents = new HashMap<TagKey<T>, List<Holder<T>>>();
/*     */     
/* 416 */     tags.tags().forEach((id, contents) -> {
/* 417 */           HolderSet.Named<T> tagToAdd = (HolderSet.Named)this.frozenTags.get(id);
/* 418 */           if (tagToAdd == null) {
/* 419 */             tagToAdd = createTag(id);
/*     */           }
/* 421 */           pendingTagsBuilder.put(id, tagToAdd);
/* 422 */           pendingContents.put(id, List.copyOf(contents));
/*     */         });
/*     */     
/* 425 */     final ImmutableMap<TagKey<T>, HolderSet.Named<T>> pendingTags = pendingTagsBuilder.build();
/*     */     
/* 427 */     final HolderLookup.RegistryLookup<T> patchedHolder = new HolderLookup.RegistryLookup.Delegate<T>()
/*     */       {
/*     */         public HolderLookup.RegistryLookup<T> parent() {
/* 430 */           return MappedRegistry.this;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 435 */         public Optional<HolderSet.Named<T>> get(TagKey<T> id) { return Optional.ofNullable((HolderSet.Named)pendingTags.get(id)); }
/*     */ 
/*     */ 
/*     */         
/*     */         public Stream<HolderSet.Named<T>> listTags() {
/* 440 */           return pendingTags.values().stream();
/*     */         }
/*     */       };
/*     */     
/* 444 */     return new Registry.PendingTags<T>()
/*     */       {
/*     */         public ResourceKey<? extends Registry<? extends T>> key() {
/* 447 */           return MappedRegistry.this.key();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 452 */         public int size() { return pendingContents.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 457 */         public HolderLookup.RegistryLookup<T> lookup() { return patchedHolder; }
/*     */ 
/*     */ 
/*     */         
/*     */         public void apply() {
/* 462 */           pendingTags.forEach((id, tag) -> {
/* 463 */                 List<Holder<T>> values = (List)pendingContents.getOrDefault(id, List.of());
/* 464 */                 tag.bind(values);
/*     */               });
/* 466 */           MappedRegistry.this.allTags = MappedRegistry.TagSet.fromMap(pendingTags);
/* 467 */           MappedRegistry.this.refreshTagsInHolders();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static interface TagSet<T> {
/*     */     static <T> TagSet<T> unbound() {
/* 474 */       return new TagSet<T>()
/*     */         {
/*     */           public boolean isBound() {
/* 477 */             return false;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 482 */           public Optional<HolderSet.Named<T>> get(TagKey<T> id) { throw new IllegalStateException("Tags not bound, trying to access " + String.valueOf(id)); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 487 */           public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) { throw new IllegalStateException("Tags not bound"); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 492 */           public Stream<HolderSet.Named<T>> getTags() { throw new IllegalStateException("Tags not bound"); }
/*     */         };
/*     */     }
/*     */     
/*     */     static <T> TagSet<T> fromMap(final Map<TagKey<T>, HolderSet.Named<T>> tags)
/*     */     {
/* 498 */       return new TagSet<T>()
/*     */         {
/*     */           public boolean isBound() {
/* 501 */             return true;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 506 */           public Optional<HolderSet.Named<T>> get(TagKey<T> id) { return Optional.ofNullable((HolderSet.Named)tags.get(id)); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 511 */           public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) { tags.forEach(action); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 516 */           public Stream<HolderSet.Named<T>> getTags() { return tags.values().stream(); } }; } boolean isBound(); Optional<HolderSet.Named<T>> get(TagKey<T> param1TagKey); void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> param1BiConsumer); Stream<HolderSet.Named<T>> getTags(); } class null extends Object implements TagSet<T> { public boolean isBound() { return false; } public Optional<HolderSet.Named<T>> get(TagKey<T> id) { throw new IllegalStateException("Tags not bound, trying to access " + String.valueOf(id)); } public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) { throw new IllegalStateException("Tags not bound"); } public Stream<HolderSet.Named<T>> getTags() { throw new IllegalStateException("Tags not bound"); } } class null extends Object implements TagSet<T> { public Stream<HolderSet.Named<T>> getTags() { return tags.values().stream(); }
/*     */     
/*     */     public boolean isBound() { return true; }
/*     */     
/*     */     public Optional<HolderSet.Named<T>> get(TagKey<T> id) { return Optional.ofNullable((HolderSet.Named)tags.get(id)); }
/*     */     
/*     */     public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) { tags.forEach(action); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\MappedRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
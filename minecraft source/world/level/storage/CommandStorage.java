/*     */ package net.minecraft.world.level.storage;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ 
/*     */ public class CommandStorage {
/*     */   private static final String ID_PREFIX = "command_storage_";
/*     */   private final Map<String, Container> namespaces;
/*     */   private final DimensionDataStorage storage;
/*     */   
/*     */   private static class Container extends SavedData {
/*  19 */     public static final Codec<Container> CODEC = RecordCodecBuilder.create(i -> i.group(
/*  20 */           Codec.unboundedMap(ExtraCodecs.RESOURCE_PATH_CODEC, CompoundTag.CODEC).fieldOf("contents").forGetter(()))
/*  21 */         .apply(i, Container::new));
/*     */     
/*     */     private final Map<String, CompoundTag> storage;
/*     */ 
/*     */     
/*  26 */     private Container(Map<String, CompoundTag> storage) { this.storage = new HashMap(storage); }
/*     */ 
/*     */ 
/*     */     
/*  30 */     private Container() { this(new HashMap()); }
/*     */ 
/*     */ 
/*     */     
/*  34 */     public static SavedDataType<Container> type(String namespace) { return new SavedDataType(CommandStorage.createId(namespace), Container::new, CODEC, DataFixTypes.SAVED_DATA_COMMAND_STORAGE); }
/*     */ 
/*     */     
/*     */     public CompoundTag get(String id) {
/*  38 */       CompoundTag result = (CompoundTag)this.storage.get(id);
/*  39 */       return (result != null) ? result : new CompoundTag();
/*     */     }
/*     */     
/*     */     public void put(String id, CompoundTag contents) {
/*  43 */       if (contents.isEmpty()) {
/*  44 */         this.storage.remove(id);
/*     */       } else {
/*  46 */         this.storage.put(id, contents);
/*     */       } 
/*  48 */       setDirty();
/*     */     }
/*     */ 
/*     */     
/*  52 */     public Stream<Identifier> getKeys(String namespace) { return this.storage.keySet().stream().map(p -> Identifier.fromNamespaceAndPath(namespace, p)); }
/*     */   }
/*     */ 
/*     */   
/*     */   public CommandStorage(DimensionDataStorage storage) {
/*  57 */     this.namespaces = new HashMap();
/*     */ 
/*     */ 
/*     */     
/*  61 */     this.storage = storage;
/*     */   }
/*     */   
/*     */   public CompoundTag get(Identifier id) {
/*  65 */     Container container = getContainer(id.getNamespace());
/*  66 */     if (container != null) {
/*  67 */       return container.get(id.getPath());
/*     */     }
/*  69 */     return new CompoundTag();
/*     */   }
/*     */   
/*     */   private Container getContainer(String namespace) {
/*  73 */     Container container = (Container)this.namespaces.get(namespace);
/*  74 */     if (container != null) {
/*  75 */       return container;
/*     */     }
/*  77 */     Container newContainer = (Container)this.storage.get(Container.type(namespace));
/*  78 */     if (newContainer != null) {
/*  79 */       this.namespaces.put(namespace, newContainer);
/*     */     }
/*  81 */     return newContainer;
/*     */   }
/*     */   
/*     */   private Container getOrCreateContainer(String namespace) {
/*  85 */     Container container = (Container)this.namespaces.get(namespace);
/*  86 */     if (container != null) {
/*  87 */       return container;
/*     */     }
/*  89 */     Container newContainer = (Container)this.storage.computeIfAbsent(Container.type(namespace));
/*  90 */     this.namespaces.put(namespace, newContainer);
/*  91 */     return newContainer;
/*     */   }
/*     */ 
/*     */   
/*  95 */   public void set(Identifier id, CompoundTag contents) { getOrCreateContainer(id.getNamespace()).put(id.getPath(), contents); }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public Stream<Identifier> keys() { return this.namespaces.entrySet().stream().flatMap(e -> ((Container)e.getValue()).getKeys((String)e.getKey())); }
/*     */ 
/*     */ 
/*     */   
/* 103 */   private static String createId(String namespace) { return "command_storage_" + namespace; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\CommandStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
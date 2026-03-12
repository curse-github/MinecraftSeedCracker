/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LayeredRegistryAccess<T>
/*     */   extends Object
/*     */ {
/*     */   private final List<T> keys;
/*     */   private final List<RegistryAccess.Frozen> values;
/*     */   private final RegistryAccess.Frozen composite;
/*     */   
/*     */   public LayeredRegistryAccess(List<T> keys) {
/*  23 */     this(keys, 
/*     */         
/*  25 */         (List)Util.make(() -> {
/*  26 */             RegistryAccess.Frozen[] arrayOfFrozen = new RegistryAccess.Frozen[keys.size()];
/*  27 */             Arrays.fill(arrayOfFrozen, RegistryAccess.EMPTY);
/*  28 */             return Arrays.asList(arrayOfFrozen);
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/*     */   private LayeredRegistryAccess(List<T> keys, List<RegistryAccess.Frozen> values) {
/*  34 */     this.keys = List.copyOf(keys);
/*  35 */     this.values = List.copyOf(values);
/*  36 */     this.composite = (new RegistryAccess.ImmutableRegistryAccess(collectRegistries(values.stream()))).freeze();
/*     */   }
/*     */   
/*     */   private int getLayerIndexOrThrow(T layer) {
/*  40 */     int index = this.keys.indexOf(layer);
/*  41 */     if (index == -1) {
/*  42 */       throw new IllegalStateException("Can't find " + String.valueOf(layer) + " inside " + String.valueOf(this.keys));
/*     */     }
/*  44 */     return index;
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen getLayer(T layer) {
/*  48 */     int index = getLayerIndexOrThrow(layer);
/*  49 */     return (RegistryAccess.Frozen)this.values.get(index);
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen getAccessForLoading(T forLayer) {
/*  53 */     int index = getLayerIndexOrThrow(forLayer);
/*  54 */     return getCompositeAccessForLayers(0, index);
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen getAccessFrom(T forLayer) {
/*  58 */     int index = getLayerIndexOrThrow(forLayer);
/*  59 */     return getCompositeAccessForLayers(index, this.values.size());
/*     */   }
/*     */ 
/*     */   
/*  63 */   private RegistryAccess.Frozen getCompositeAccessForLayers(int from, int to) { return (new RegistryAccess.ImmutableRegistryAccess(collectRegistries(this.values.subList(from, to).stream()))).freeze(); }
/*     */ 
/*     */ 
/*     */   
/*  67 */   public LayeredRegistryAccess<T> replaceFrom(T fromLayer, Frozen... layers) { return replaceFrom(fromLayer, Arrays.asList(layers)); }
/*     */ 
/*     */   
/*     */   public LayeredRegistryAccess<T> replaceFrom(T fromLayer, List<RegistryAccess.Frozen> layers) {
/*  71 */     int index = getLayerIndexOrThrow(fromLayer);
/*     */     
/*  73 */     if (layers.size() > this.values.size() - index) {
/*  74 */       throw new IllegalStateException("Too many values to replace");
/*     */     }
/*     */     
/*  77 */     List<RegistryAccess.Frozen> newValues = new ArrayList<RegistryAccess.Frozen>();
/*     */     
/*  79 */     for (int i = 0; i < index; i++) {
/*  80 */       newValues.add((RegistryAccess.Frozen)this.values.get(i));
/*     */     }
/*     */     
/*  83 */     newValues.addAll(layers);
/*     */     
/*  85 */     while (newValues.size() < this.values.size()) {
/*  86 */       newValues.add(RegistryAccess.EMPTY);
/*     */     }
/*  88 */     return new LayeredRegistryAccess(this.keys, newValues);
/*     */   }
/*     */ 
/*     */   
/*  92 */   public RegistryAccess.Frozen compositeAccess() { return this.composite; }
/*     */ 
/*     */   
/*     */   private static Map<ResourceKey<? extends Registry<?>>, Registry<?>> collectRegistries(Stream<? extends RegistryAccess> registries) {
/*  96 */     Map<ResourceKey<? extends Registry<?>>, Registry<?>> result = new HashMap<ResourceKey<? extends Registry<?>>, Registry<?>>();
/*     */     
/*  98 */     registries.forEach(access -> 
/*  99 */         access.registries().forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\LayeredRegistryAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
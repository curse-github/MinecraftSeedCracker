/*     */ package net.minecraft.server.packs.repository;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ public class PackRepository {
/*     */   private final Set<RepositorySource> sources;
/*     */   
/*     */   public PackRepository(RepositorySource... sources) {
/*  23 */     this.available = ImmutableMap.of();
/*  24 */     this.selected = ImmutableList.of();
/*     */ 
/*     */     
/*  27 */     this.sources = ImmutableSet.copyOf(sources);
/*     */   }
/*     */   private Map<String, Pack> available; private List<Pack> selected;
/*     */   
/*  31 */   public static String displayPackList(Collection<Pack> packs) { return (String)packs.stream().map(pack -> pack.getId() + pack.getId()).collect(Collectors.joining(", ")); }
/*     */ 
/*     */   
/*     */   public void reload() {
/*  35 */     List<String> currentlySelectedNames = (List)this.selected.stream().map(Pack::getId).collect(ImmutableList.toImmutableList());
/*  36 */     this.available = discoverAvailable();
/*  37 */     this.selected = rebuildSelected(currentlySelectedNames);
/*     */   }
/*     */   
/*     */   private Map<String, Pack> discoverAvailable() {
/*  41 */     Map<String, Pack> discovered = Maps.newTreeMap();
/*  42 */     for (RepositorySource source : this.sources) {
/*  43 */       source.loadPacks(pack -> discovered.put(pack.getId(), pack));
/*     */     }
/*  45 */     return ImmutableMap.copyOf(discovered);
/*     */   }
/*     */   
/*     */   public boolean isAbleToClearAnyPack() {
/*  49 */     List<Pack> newSelected = rebuildSelected(List.of());
/*  50 */     return !this.selected.equals(newSelected);
/*     */   }
/*     */ 
/*     */   
/*  54 */   public void setSelected(Collection<String> packs) { this.selected = rebuildSelected(packs); }
/*     */ 
/*     */   
/*     */   public boolean addPack(String packId) {
/*  58 */     Pack pack = (Pack)this.available.get(packId);
/*  59 */     if (pack != null && !this.selected.contains(pack)) {
/*  60 */       List<Pack> selectedCopy = Lists.newArrayList(this.selected);
/*  61 */       selectedCopy.add(pack);
/*  62 */       this.selected = selectedCopy;
/*  63 */       return true;
/*     */     } 
/*  65 */     return false;
/*     */   }
/*     */   
/*     */   public boolean removePack(String packId) {
/*  69 */     Pack pack = (Pack)this.available.get(packId);
/*  70 */     if (pack != null && this.selected.contains(pack)) {
/*  71 */       List<Pack> selectedCopy = Lists.newArrayList(this.selected);
/*  72 */       selectedCopy.remove(pack);
/*  73 */       this.selected = selectedCopy;
/*  74 */       return true;
/*     */     } 
/*  76 */     return false;
/*     */   }
/*     */   
/*     */   private List<Pack> rebuildSelected(Collection<String> selectedNames) {
/*  80 */     List<Pack> selectedAndPresent = (List)getAvailablePacks(selectedNames).collect(Util.toMutableList());
/*     */     
/*  82 */     for (Pack pack : this.available.values()) {
/*     */       
/*  84 */       if (pack.isRequired() && !selectedAndPresent.contains(pack)) {
/*  85 */         pack.getDefaultPosition().insert(selectedAndPresent, pack, Pack::selectionConfig, false);
/*     */       }
/*     */     } 
/*  88 */     return ImmutableList.copyOf(selectedAndPresent);
/*     */   }
/*     */ 
/*     */   
/*  92 */   private Stream<Pack> getAvailablePacks(Collection<String> ids) { Objects.requireNonNull(this.available); return ids.stream().map(this.available::get).filter(Objects::nonNull); }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public Collection<String> getAvailableIds() { return this.available.keySet(); }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public Collection<Pack> getAvailablePacks() { return this.available.values(); }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public Collection<String> getSelectedIds() { return (Collection)this.selected.stream().map(Pack::getId).collect(ImmutableSet.toImmutableSet()); }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public FeatureFlagSet getRequestedFeatureFlags() { return (FeatureFlagSet)getSelectedPacks().stream().map(Pack::getRequestedFeatures).reduce(FeatureFlagSet::join).orElse(FeatureFlagSet.of()); }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public Collection<Pack> getSelectedPacks() { return this.selected; }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public Pack getPack(String id) { return (Pack)this.available.get(id); }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public boolean isAvailable(String id) { return this.available.containsKey(id); }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public List<PackResources> openAllSelected() { return (List)this.selected.stream().map(Pack::open).collect(ImmutableList.toImmutableList()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\repository\PackRepository.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
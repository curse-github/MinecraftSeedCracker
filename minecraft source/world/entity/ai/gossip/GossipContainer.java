/*     */ package net.minecraft.world.entity.ai.gossip;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.DoublePredicate;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ 
/*     */ public class GossipContainer {
/*  29 */   public static final Codec<GossipContainer> CODEC = GossipEntry.CODEC.listOf().xmap(GossipContainer::new, container -> 
/*     */       
/*  31 */       container.unpack().toList()); public static final int DISCARD_THRESHOLD = 2;
/*     */   
/*     */   private static final class GossipEntry extends Record { private final UUID target;
/*     */     private final GossipType type;
/*     */     private final int value;
/*     */     
/*  37 */     private GossipEntry(UUID target, GossipType type, int value) { this.target = target; this.type = type; this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #37	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  37 */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry; } public UUID target() { return this.target; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #37	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #37	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry;
/*  37 */       //   0	8	1	o	Ljava/lang/Object; } public GossipType type() { return this.type; } public int value() { return this.value; }
/*  38 */     public static final Codec<GossipEntry> CODEC = RecordCodecBuilder.create(i -> i.group(UUIDUtil.CODEC
/*  39 */           .fieldOf("Target").forGetter(GossipEntry::target), GossipType.CODEC
/*  40 */           .fieldOf("Type").forGetter(GossipEntry::type), ExtraCodecs.POSITIVE_INT
/*  41 */           .fieldOf("Value").forGetter(GossipEntry::value))
/*  42 */         .apply(i, GossipEntry::new));
/*     */ 
/*     */     
/*  45 */     public int weightedValue() { return this.value * this.type.weight; } }
/*     */ 
/*     */   
/*     */   private static class EntityGossips
/*     */   {
/*  50 */     private final Object2IntMap<GossipType> entries = new Object2IntOpenHashMap();
/*     */ 
/*     */     
/*  53 */     public int weightedValue(Predicate<GossipType> types) { return this.entries.object2IntEntrySet()
/*  54 */         .stream()
/*  55 */         .filter(e -> types.test((GossipType)e.getKey()))
/*  56 */         .mapToInt(e -> e.getIntValue() * ((GossipType)e.getKey()).weight)
/*  57 */         .sum(); }
/*     */ 
/*     */ 
/*     */     
/*  61 */     public Stream<GossipContainer.GossipEntry> unpack(UUID target) { return this.entries.object2IntEntrySet().stream().map(e -> new GossipContainer.GossipEntry(target, (GossipType)e.getKey(), e.getIntValue())); }
/*     */ 
/*     */     
/*     */     public void decay() {
/*  65 */       ObjectIterator<Object2IntMap.Entry<GossipType>> it = this.entries.object2IntEntrySet().iterator();
/*  66 */       while (it.hasNext()) {
/*  67 */         Object2IntMap.Entry<GossipType> gossip = (Object2IntMap.Entry)it.next();
/*  68 */         int newValue = gossip.getIntValue() - ((GossipType)gossip.getKey()).decayPerDay;
/*  69 */         if (newValue < 2) {
/*  70 */           it.remove(); continue;
/*     */         } 
/*  72 */         gossip.setValue(newValue);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  78 */     public boolean isEmpty() { return this.entries.isEmpty(); }
/*     */ 
/*     */     
/*     */     public void makeSureValueIsntTooLowOrTooHigh(GossipType type) {
/*  82 */       int value = this.entries.getInt(type);
/*  83 */       if (value > type.max) {
/*  84 */         this.entries.put(type, type.max);
/*     */       }
/*  86 */       if (value < 2) {
/*  87 */         remove(type);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*  92 */     public void remove(GossipType type) { this.entries.removeInt(type); }
/*     */   }
/*     */ 
/*     */   
/*  96 */   private final Map<UUID, EntityGossips> gossips = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   private GossipContainer(List<GossipEntry> entries) { entries.forEach(e -> (getOrCreate(e.target)).entries.put(e.type, e.value)); }
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/*     */   public Map<UUID, Object2IntMap<GossipType>> getGossipEntries() {
/* 107 */     Map<UUID, Object2IntMap<GossipType>> result = Maps.newHashMap();
/* 108 */     this.gossips.keySet().forEach(uuid -> {
/* 109 */           EntityGossips entityGossips = (EntityGossips)this.gossips.get(uuid);
/* 110 */           result.put(uuid, entityGossips.entries);
/*     */         });
/* 112 */     return result;
/*     */   }
/*     */   
/*     */   public void decay() {
/* 116 */     Iterator<EntityGossips> iterator = this.gossips.values().iterator();
/* 117 */     while (iterator.hasNext()) {
/* 118 */       EntityGossips entityGossips = (EntityGossips)iterator.next();
/* 119 */       entityGossips.decay();
/* 120 */       if (entityGossips.isEmpty())
/*     */       {
/* 122 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 128 */   private Stream<GossipEntry> unpack() { return this.gossips.entrySet().stream().flatMap(e -> ((EntityGossips)e.getValue()).unpack((UUID)e.getKey())); }
/*     */ 
/*     */   
/*     */   private Collection<GossipEntry> selectGossipsForTransfer(RandomSource random, int maxCount) {
/* 132 */     List<GossipEntry> entries = unpack().toList();
/* 133 */     if (entries.isEmpty()) {
/* 134 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 137 */     int[] ranges = new int[entries.size()];
/* 138 */     int rangesEnd = 0;
/* 139 */     for (int i = 0; i < entries.size(); i++) {
/* 140 */       GossipEntry gossip = (GossipEntry)entries.get(i);
/* 141 */       rangesEnd += Math.abs(gossip.weightedValue());
/* 142 */       ranges[i] = rangesEnd - 1;
/*     */     } 
/*     */     
/* 145 */     Set<GossipEntry> results = Sets.newIdentityHashSet();
/* 146 */     for (int i = 0; i < maxCount; i++) {
/* 147 */       int choice = random.nextInt(rangesEnd);
/* 148 */       int selectedIndex = Arrays.binarySearch(ranges, choice);
/* 149 */       results.add((GossipEntry)entries.get((selectedIndex < 0) ? (-selectedIndex - 1) : selectedIndex));
/*     */     } 
/* 151 */     return results;
/*     */   }
/*     */ 
/*     */   
/* 155 */   private EntityGossips getOrCreate(UUID target) { return (EntityGossips)this.gossips.computeIfAbsent(target, uuid -> new EntityGossips()); }
/*     */ 
/*     */   
/*     */   public void transferFrom(GossipContainer source, RandomSource random, int maxCount) {
/* 159 */     Collection<GossipEntry> newGossips = source.selectGossipsForTransfer(random, maxCount);
/*     */     
/* 161 */     newGossips.forEach(newGossip -> {
/* 162 */           int decayedValue = newGossip.value - newGossip.type.decayPerTransfer;
/* 163 */           if (decayedValue >= 2) {
/* 164 */             (getOrCreate(newGossip.target)).entries.mergeInt(newGossip.type, decayedValue, GossipContainer::mergeValuesForTransfer);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getReputation(UUID entity, Predicate<GossipType> types) {
/* 174 */     EntityGossips entry = (EntityGossips)this.gossips.get(entity);
/* 175 */     return (entry != null) ? entry.weightedValue(types) : 0;
/*     */   }
/*     */ 
/*     */   
/* 179 */   public long getCountForType(GossipType type, DoublePredicate valueTest) { return this.gossips.values().stream().filter(e -> valueTest.test((e.entries.getOrDefault(type, 0) * type.weight))).count(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(UUID target, GossipType type, int amountToAdd) {
/* 186 */     EntityGossips entityGossips = getOrCreate(target);
/* 187 */     entityGossips.entries.mergeInt(type, amountToAdd, (o, n) -> mergeValuesForAddition(type, o, n));
/* 188 */     entityGossips.makeSureValueIsntTooLowOrTooHigh(type);
/* 189 */     if (entityGossips.isEmpty()) {
/* 190 */       this.gossips.remove(target);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 199 */   public void remove(UUID target, GossipType type, int amountToRemove) { add(target, type, -amountToRemove); }
/*     */ 
/*     */   
/*     */   public void remove(UUID target, GossipType type) {
/* 203 */     EntityGossips entityGossips = (EntityGossips)this.gossips.get(target);
/* 204 */     if (entityGossips != null) {
/* 205 */       entityGossips.remove(type);
/* 206 */       if (entityGossips.isEmpty()) {
/* 207 */         this.gossips.remove(target);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void remove(GossipType type) {
/* 213 */     Iterator<EntityGossips> iterator = this.gossips.values().iterator();
/* 214 */     while (iterator.hasNext()) {
/* 215 */       EntityGossips entityGossips = (EntityGossips)iterator.next();
/* 216 */       entityGossips.remove(type);
/* 217 */       if (entityGossips.isEmpty()) {
/* 218 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 224 */   public void clear() { this.gossips.clear(); }
/*     */ 
/*     */   
/*     */   public void putAll(GossipContainer container) {
/* 228 */     container.gossips.forEach((target, gossips) -> 
/* 229 */         (getOrCreate(target)).entries.putAll(gossips.entries));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 234 */   private static int mergeValuesForTransfer(int oldValue, int newValue) { return Math.max(oldValue, newValue); }
/*     */ 
/*     */   
/*     */   private int mergeValuesForAddition(GossipType type, int oldValue, int newValue) {
/* 238 */     int sum = oldValue + newValue;
/* 239 */     return (sum > type.max) ? Math.max(type.max, oldValue) : sum;
/*     */   }
/*     */   
/*     */   public GossipContainer copy() {
/* 243 */     GossipContainer container = new GossipContainer();
/* 244 */     container.putAll(this);
/* 245 */     return container;
/*     */   }
/*     */   
/*     */   public GossipContainer() {}
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\gossip\GossipContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
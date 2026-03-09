/*     */ package net.minecraft.world.entity.monster.warden;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ public class AngerManagement {
/*     */   @VisibleForTesting
/*     */   protected static final int CONVERSION_DELAY = 2;
/*     */   @VisibleForTesting
/*     */   protected static final int MAX_ANGER = 150;
/*     */   private static final int DEFAULT_ANGER_DECREASE = 1;
/*     */   private int conversionDelay;
/*     */   private int highestAnger;
/*     */   private final Predicate<Entity> filter;
/*     */   @VisibleForTesting
/*     */   protected final ArrayList<Entity> suspects;
/*     */   private final Sorter suspectSorter;
/*     */   @VisibleForTesting
/*     */   protected final Object2IntMap<Entity> angerBySuspect;
/*  40 */   private static final Codec<Pair<UUID, Integer>> SUSPECT_ANGER_PAIR = RecordCodecBuilder.create(i -> i.group(UUIDUtil.CODEC
/*  41 */         .fieldOf("uuid").forGetter(Pair::getFirst), ExtraCodecs.NON_NEGATIVE_INT
/*  42 */         .fieldOf("anger").forGetter(Pair::getSecond))
/*  43 */       .apply(i, Pair::of)); @VisibleForTesting
/*     */   protected final Object2IntMap<UUID> angerByUuid;
/*     */   public static Codec<AngerManagement> codec(Predicate<Entity> filter) {
/*  46 */     return RecordCodecBuilder.create(i -> i.group(SUSPECT_ANGER_PAIR
/*  47 */           .listOf().fieldOf("suspects").orElse(Collections.emptyList()).forGetter(AngerManagement::createUuidAngerPairs))
/*  48 */         .apply(i, ()));
/*     */   }
/*     */   @VisibleForTesting
/*     */   protected static final class Sorter extends Record implements Comparator<Entity> { private final AngerManagement angerManagement;
/*     */     public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #58	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter; }
/*     */     
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #58	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter; }
/*     */     
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #58	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/*     */     
/*  58 */     public AngerManagement angerManagement() { return this.angerManagement; }
/*  59 */     protected Sorter(AngerManagement angerManagement) { this.angerManagement = angerManagement; }
/*     */     
/*     */     public int compare(Entity entity1, Entity entity2) {
/*  62 */       if (entity1.equals(entity2)) {
/*  63 */         return 0;
/*     */       }
/*     */       
/*  66 */       int anger1 = this.angerManagement.angerBySuspect.getOrDefault(entity1, 0);
/*  67 */       int anger2 = this.angerManagement.angerBySuspect.getOrDefault(entity2, 0);
/*     */ 
/*     */       
/*  70 */       this.angerManagement.highestAnger = Math.max(this.angerManagement.highestAnger, Math.max(anger1, anger2));
/*     */       
/*  72 */       boolean angryAt1 = AngerLevel.byAnger(anger1).isAngry();
/*  73 */       boolean angryAt2 = AngerLevel.byAnger(anger2).isAngry();
/*  74 */       if (angryAt1 != angryAt2) {
/*  75 */         return angryAt1 ? -1 : 1;
/*     */       }
/*     */ 
/*     */       
/*  79 */       boolean isPlayer1 = entity1 instanceof net.minecraft.world.entity.player.Player;
/*  80 */       boolean isPlayer2 = entity2 instanceof net.minecraft.world.entity.player.Player;
/*  81 */       if (isPlayer1 != isPlayer2) {
/*  82 */         return isPlayer1 ? -1 : 1;
/*     */       }
/*  84 */       return Integer.compare(anger2, anger1);
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
/*     */   public AngerManagement(Predicate<Entity> filter, List<Pair<UUID, Integer>> angerByUuid) {
/*     */     this.conversionDelay = Mth.randomBetweenInclusive(RandomSource.create(), 0, 2);
/*  98 */     this.filter = filter;
/*  99 */     this.suspects = new ArrayList();
/* 100 */     this.suspectSorter = new Sorter(this);
/* 101 */     this.angerBySuspect = new Object2IntOpenHashMap();
/*     */     
/* 103 */     this.angerByUuid = new Object2IntOpenHashMap(angerByUuid.size());
/* 104 */     angerByUuid.forEach(pair -> this.angerByUuid.put((UUID)pair.getFirst(), (Integer)pair.getSecond()));
/*     */   }
/*     */   
/*     */   private List<Pair<UUID, Integer>> createUuidAngerPairs() {
/* 108 */     return (List)Streams.concat(new Stream[] { this.suspects
/* 109 */           .stream().map(e -> Pair.of(e.getUUID(), Integer.valueOf(this.angerBySuspect.getInt(e)))), this.angerByUuid
/* 110 */           .object2IntEntrySet().stream().map(e -> Pair.of((UUID)e.getKey(), Integer.valueOf(e.getIntValue())))
/* 111 */         }).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel level, Predicate<Entity> validEntity) {
/* 115 */     this.conversionDelay--;
/* 116 */     if (this.conversionDelay <= 0) {
/* 117 */       convertFromUuids(level);
/* 118 */       this.conversionDelay = 2;
/*     */     } 
/*     */ 
/*     */     
/* 122 */     ObjectIterator<Object2IntMap.Entry<UUID>> serializedIterator = this.angerByUuid.object2IntEntrySet().iterator();
/* 123 */     while (serializedIterator.hasNext()) {
/* 124 */       Object2IntMap.Entry<UUID> entry = (Object2IntMap.Entry)serializedIterator.next();
/* 125 */       int anger = entry.getIntValue();
/*     */       
/* 127 */       if (anger <= 1) {
/* 128 */         serializedIterator.remove(); continue;
/*     */       } 
/* 130 */       entry.setValue(anger - 1);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 135 */     ObjectIterator<Object2IntMap.Entry<Entity>> iterator = this.angerBySuspect.object2IntEntrySet().iterator();
/* 136 */     while (iterator.hasNext()) {
/* 137 */       Object2IntMap.Entry<Entity> entry = (Object2IntMap.Entry)iterator.next();
/* 138 */       int anger = entry.getIntValue();
/* 139 */       Entity entity = (Entity)entry.getKey();
/* 140 */       Entity.RemovalReason removalReason = entity.getRemovalReason();
/* 141 */       if (anger <= 1 || !validEntity.test(entity) || removalReason != null) {
/* 142 */         this.suspects.remove(entity);
/* 143 */         iterator.remove();
/*     */ 
/*     */         
/* 146 */         if (anger > 1 && removalReason != null)
/* 147 */           switch (removalReason) { case CHANGED_DIMENSION: case UNLOADED_TO_CHUNK: case UNLOADED_WITH_PLAYER:
/* 148 */               this.angerByUuid.put(entity.getUUID(), anger - 1); continue; }
/*     */            
/*     */         continue;
/*     */       } 
/* 152 */       entry.setValue(anger - 1);
/*     */     } 
/*     */ 
/*     */     
/* 156 */     sortAndUpdateHighestAnger();
/*     */   }
/*     */   
/*     */   private void sortAndUpdateHighestAnger() {
/* 160 */     this.highestAnger = 0;
/* 161 */     this.suspects.sort(this.suspectSorter);
/*     */ 
/*     */     
/* 164 */     if (this.suspects.size() == 1) {
/* 165 */       this.highestAnger = this.angerBySuspect.getInt(this.suspects.get(0));
/*     */     }
/*     */   }
/*     */   
/*     */   private void convertFromUuids(ServerLevel level) {
/* 170 */     ObjectIterator<Object2IntMap.Entry<UUID>> iterator = this.angerByUuid.object2IntEntrySet().iterator();
/* 171 */     while (iterator.hasNext()) {
/* 172 */       Object2IntMap.Entry<UUID> entry = (Object2IntMap.Entry)iterator.next();
/* 173 */       int anger = entry.getIntValue();
/*     */       
/* 175 */       Entity entity = level.getEntity((UUID)entry.getKey());
/* 176 */       if (entity != null) {
/* 177 */         this.angerBySuspect.put(entity, anger);
/* 178 */         this.suspects.add(entity);
/* 179 */         iterator.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int increaseAnger(Entity entity, int increment) {
/* 185 */     boolean newSuspect = !this.angerBySuspect.containsKey(entity);
/* 186 */     int currentAnger = this.angerBySuspect.computeInt(entity, (k, anger) -> Integer.valueOf(Math.min(150, ((anger == null) ? 0 : anger.intValue()) + increment)));
/*     */     
/* 188 */     if (newSuspect) {
/* 189 */       int serializedAnger = this.angerByUuid.removeInt(entity.getUUID());
/* 190 */       currentAnger += serializedAnger;
/* 191 */       this.angerBySuspect.put(entity, currentAnger);
/* 192 */       this.suspects.add(entity);
/*     */     } 
/* 194 */     sortAndUpdateHighestAnger();
/* 195 */     return currentAnger;
/*     */   }
/*     */   
/*     */   public void clearAnger(Entity entity) {
/* 199 */     this.angerBySuspect.removeInt(entity);
/* 200 */     this.suspects.remove(entity);
/* 201 */     sortAndUpdateHighestAnger();
/*     */   }
/*     */ 
/*     */   
/* 205 */   private Entity getTopSuspect() { return (Entity)this.suspects.stream().filter(this.filter).findFirst().orElse(null); }
/*     */ 
/*     */ 
/*     */   
/* 209 */   public int getActiveAnger(Entity currentTarget) { return (currentTarget == null) ? this.highestAnger : this.angerBySuspect.getInt(currentTarget); }
/*     */ 
/*     */ 
/*     */   
/* 213 */   public Optional<LivingEntity> getActiveEntity() { return Optional.ofNullable(getTopSuspect())
/* 214 */       .filter(e -> e instanceof LivingEntity)
/* 215 */       .map(e -> (LivingEntity)e); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\warden\AngerManagement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
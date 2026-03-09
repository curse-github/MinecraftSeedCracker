/*     */ package net.minecraft.world.entity.raid;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.List;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.PoiTypeTags;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Raids extends SavedData {
/*     */   private static final String RAID_FILE_ID = "raids";
/*  35 */   public static final Codec<Raids> CODEC = RecordCodecBuilder.create(i -> i.group(RaidWithId.CODEC
/*  36 */         .listOf().optionalFieldOf("raids", List.of()).forGetter(()), Codec.INT
/*  37 */         .fieldOf("next_id").forGetter(()), Codec.INT
/*  38 */         .fieldOf("tick").forGetter(()))
/*  39 */       .apply(i, Raids::new));
/*     */   
/*  41 */   public static final SavedDataType<Raids> TYPE = new SavedDataType("raids", Raids::new, CODEC, DataFixTypes.SAVED_DATA_RAIDS);
/*  42 */   public static final SavedDataType<Raids> TYPE_END = new SavedDataType("raids_end", Raids::new, CODEC, DataFixTypes.SAVED_DATA_RAIDS);
/*     */   
/*     */   private final Int2ObjectMap<Raid> raidMap;
/*     */   
/*     */   private int nextId;
/*     */   private int tick;
/*     */   
/*     */   public static SavedDataType<Raids> getType(Holder<DimensionType> type) {
/*  50 */     if (type.is(BuiltinDimensionTypes.END)) {
/*  51 */       return TYPE_END;
/*     */     }
/*  53 */     return TYPE;
/*     */   } public Raids() {
/*     */     this.raidMap = new Int2ObjectOpenHashMap();
/*     */     this.nextId = 1;
/*  57 */     setDirty();
/*     */   } private Raids(List<RaidWithId> raids, int nextId, int tick) {
/*     */     this.raidMap = new Int2ObjectOpenHashMap();
/*     */     this.nextId = 1;
/*  61 */     for (RaidWithId raid : raids) {
/*  62 */       this.raidMap.put(raid.id, raid.raid);
/*     */     }
/*  64 */     this.nextId = nextId;
/*  65 */     this.tick = tick;
/*     */   }
/*     */ 
/*     */   
/*  69 */   public Raid get(int raidId) { return (Raid)this.raidMap.get(raidId); }
/*     */ 
/*     */   
/*     */   public OptionalInt getId(Raid raid) {
/*  73 */     for (ObjectIterator objectIterator = this.raidMap.int2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) { Int2ObjectMap.Entry<Raid> entry = (Int2ObjectMap.Entry)objectIterator.next();
/*  74 */       if (entry.getValue() == raid) {
/*  75 */         return OptionalInt.of(entry.getIntKey());
/*     */       } }
/*     */     
/*  78 */     return OptionalInt.empty();
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel level) {
/*  82 */     this.tick++;
/*  83 */     ObjectIterator objectIterator = this.raidMap.values().iterator();
/*     */     
/*  85 */     while (objectIterator.hasNext()) {
/*  86 */       Raid raid = (Raid)objectIterator.next();
/*  87 */       if (!((Boolean)level.getGameRules().get(GameRules.RAIDS)).booleanValue()) {
/*  88 */         raid.stop();
/*     */       }
/*  90 */       if (raid.isStopped()) {
/*  91 */         objectIterator.remove();
/*  92 */         setDirty();
/*     */         
/*     */         continue;
/*     */       } 
/*  96 */       raid.tick(level);
/*     */     } 
/*     */ 
/*     */     
/* 100 */     if (this.tick % 200 == 0) {
/* 101 */       setDirty();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 106 */   public static boolean canJoinRaid(Raider raider) { return (raider.isAlive() && raider.canJoinRaid() && raider.getNoActionTime() <= 2400); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Raid createOrExtendRaid(ServerPlayer player, BlockPos raidPosition) {
/*     */     BlockPos raidCenterPos;
/* 114 */     if (player.isSpectator()) {
/* 115 */       return null;
/*     */     }
/*     */     
/* 118 */     ServerLevel level = player.level();
/* 119 */     if (!((Boolean)level.getGameRules().get(GameRules.RAIDS)).booleanValue()) {
/* 120 */       return null;
/*     */     }
/*     */     
/* 123 */     if (!((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.CAN_START_RAID, raidPosition)).booleanValue()) {
/* 124 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 128 */     List<PoiRecord> posses = level.getPoiManager().getInRange(e -> e.is(PoiTypeTags.VILLAGE), raidPosition, 64, PoiManager.Occupancy.IS_OCCUPIED).toList();
/* 129 */     int count = 0;
/* 130 */     Vec3 posTotals = Vec3.ZERO;
/* 131 */     for (PoiRecord p : posses) {
/* 132 */       BlockPos pos = p.getPos();
/* 133 */       posTotals = posTotals.add(pos.getX(), pos.getY(), pos.getZ());
/* 134 */       count++;
/*     */     } 
/*     */ 
/*     */     
/* 138 */     if (count > 0) {
/*     */       
/* 140 */       posTotals = posTotals.scale(1.0D / count);
/* 141 */       raidCenterPos = BlockPos.containing(posTotals);
/*     */     } else {
/*     */       
/* 144 */       raidCenterPos = raidPosition;
/*     */     } 
/*     */     
/* 147 */     Raid raid = getOrCreateRaid(level, raidCenterPos);
/*     */     
/* 149 */     if (!raid.isStarted() && !this.raidMap.containsValue(raid)) {
/* 150 */       this.raidMap.put(getUniqueId(), raid);
/*     */     }
/*     */     
/* 153 */     if (!raid.isStarted() || raid.getRaidOmenLevel() < raid.getMaxRaidOmenLevel()) {
/* 154 */       raid.absorbRaidOmen(player);
/*     */     }
/*     */     
/* 157 */     setDirty();
/*     */     
/* 159 */     return raid;
/*     */   }
/*     */   
/*     */   private Raid getOrCreateRaid(ServerLevel level, BlockPos pos) {
/* 163 */     Raid raid = level.getRaidAt(pos);
/* 164 */     return (raid != null) ? raid : new Raid(pos, level.getDifficulty());
/*     */   }
/*     */ 
/*     */   
/* 168 */   public static Raids load(CompoundTag tag) { return (Raids)CODEC.parse(NbtOps.INSTANCE, tag).resultOrPartial().orElseGet(Raids::new); }
/*     */ 
/*     */ 
/*     */   
/* 172 */   private int getUniqueId() { return ++this.nextId; }
/*     */ 
/*     */   
/*     */   public Raid getNearbyRaid(BlockPos pos, int maxDistSqr) {
/* 176 */     Raid closest = null;
/* 177 */     double closestDistanceSqr = maxDistSqr;
/* 178 */     for (ObjectIterator objectIterator = this.raidMap.values().iterator(); objectIterator.hasNext(); ) { Raid raid = (Raid)objectIterator.next();
/* 179 */       double distance = raid.getCenter().distSqr(pos);
/* 180 */       if (!raid.isActive()) {
/*     */         continue;
/*     */       }
/* 183 */       if (distance < closestDistanceSqr) {
/* 184 */         closest = raid;
/* 185 */         closestDistanceSqr = distance;
/*     */       }  }
/*     */     
/* 188 */     return closest;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/*     */   public List<BlockPos> getRaidCentersInChunk(ChunkPos chunkPos) {
/* 195 */     Objects.requireNonNull(chunkPos); return this.raidMap.values().stream().map(Raid::getCenter).filter(chunkPos::contains)
/* 196 */       .toList();
/*     */   }
/*     */   private static final class RaidWithId extends Record { private final int id; private final Raid raid;
/* 199 */     private RaidWithId(int id, Raid raid) { this.id = id; this.raid = raid; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/raid/Raids$RaidWithId;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #199	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 199 */       //   0	7	0	this	Lnet/minecraft/world/entity/raid/Raids$RaidWithId; } public int id() { return this.id; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/raid/Raids$RaidWithId;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #199	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/raid/Raids$RaidWithId; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/raid/Raids$RaidWithId;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #199	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/raid/Raids$RaidWithId;
/* 199 */       //   0	8	1	o	Ljava/lang/Object; } public Raid raid() { return this.raid; }
/* 200 */     public static final Codec<RaidWithId> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/* 201 */           .fieldOf("id").forGetter(RaidWithId::id), Raid.MAP_CODEC
/* 202 */           .forGetter(RaidWithId::raid))
/* 203 */         .apply(i, RaidWithId::new));
/*     */ 
/*     */     
/* 206 */     public static RaidWithId from(Int2ObjectMap.Entry<Raid> entry) { return new RaidWithId(entry.getIntKey(), (Raid)entry.getValue()); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\raid\Raids.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
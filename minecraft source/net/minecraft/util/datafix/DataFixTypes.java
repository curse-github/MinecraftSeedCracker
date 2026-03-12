/*    */ package net.minecraft.util.datafix;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixer;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public static enum DataFixTypes {
/* 18 */   LEVEL(References.LEVEL),
/* 19 */   LEVEL_SUMMARY(References.LIGHTWEIGHT_LEVEL),
/* 20 */   PLAYER(References.PLAYER),
/* 21 */   CHUNK(References.CHUNK),
/* 22 */   HOTBAR(References.HOTBAR),
/* 23 */   OPTIONS(References.OPTIONS),
/* 24 */   STRUCTURE(References.STRUCTURE),
/* 25 */   STATS(References.STATS),
/* 26 */   SAVED_DATA_COMMAND_STORAGE(References.SAVED_DATA_COMMAND_STORAGE),
/* 27 */   SAVED_DATA_FORCED_CHUNKS(References.SAVED_DATA_TICKETS),
/* 28 */   SAVED_DATA_MAP_DATA(References.SAVED_DATA_MAP_DATA),
/* 29 */   SAVED_DATA_MAP_INDEX(References.SAVED_DATA_MAP_INDEX),
/* 30 */   SAVED_DATA_RAIDS(References.SAVED_DATA_RAIDS),
/* 31 */   SAVED_DATA_RANDOM_SEQUENCES(References.SAVED_DATA_RANDOM_SEQUENCES),
/* 32 */   SAVED_DATA_SCOREBOARD(References.SAVED_DATA_SCOREBOARD),
/* 33 */   SAVED_DATA_STOPWATCHES(References.SAVED_DATA_STOPWATCHES),
/* 34 */   SAVED_DATA_STRUCTURE_FEATURE_INDICES(References.SAVED_DATA_STRUCTURE_FEATURE_INDICES),
/* 35 */   SAVED_DATA_WORLD_BORDER(References.SAVED_DATA_WORLD_BORDER),
/* 36 */   ADVANCEMENTS(References.ADVANCEMENTS),
/* 37 */   POI_CHUNK(References.POI_CHUNK),
/* 38 */   WORLD_GEN_SETTINGS(References.WORLD_GEN_SETTINGS),
/* 39 */   ENTITY_CHUNK(References.ENTITY_CHUNK),
/* 40 */   DEBUG_PROFILE(References.DEBUG_PROFILE);
/*    */   
/*    */   static  {
/* 43 */     TYPES_FOR_LEVEL_LIST = Set.of(LEVEL_SUMMARY.type);
/*    */   }
/*    */   public static final Set<DSL.TypeReference> TYPES_FOR_LEVEL_LIST;
/*    */   private final DSL.TypeReference type;
/*    */   
/* 48 */   DataFixTypes(DSL.TypeReference type) { this.type = type; }
/*    */ 
/*    */ 
/*    */   
/* 52 */   private static int currentVersion() { return SharedConstants.getCurrentVersion().dataVersion().version(); }
/*    */ 
/*    */   
/*    */   public <A> Codec<A> wrapCodec(final Codec<A> codec, final DataFixer dataFixer, final int defaultVersion) {
/* 56 */     return new Codec<A>()
/*    */       {
/*    */         public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/* 59 */           return codec.encode(input, ops, prefix).flatMap(data -> ops.mergeToMap(data, ops
/* 60 */                 .createString("DataVersion"), ops.createInt(DataFixTypes.currentVersion())));
/*    */         }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/*    */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/* 67 */           Objects.requireNonNull(ops);
/*    */ 
/*    */           
/* 70 */           int fromVersion = ((Integer)ops.get(input, "DataVersion").flatMap(ops::getNumberValue).map(Number::intValue).result().orElse(Integer.valueOf(defaultVersion))).intValue();
/* 71 */           Dynamic<T> dataWithoutVersion = new Dynamic<T>(ops, ops.remove(input, "DataVersion"));
/* 72 */           Dynamic<T> fixedData = DataFixTypes.this.updateToCurrentVersion(dataFixer, dataWithoutVersion, fromVersion);
/* 73 */           return codec.decode(fixedData);
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/* 79 */   public <T> Dynamic<T> update(DataFixer fixerUpper, Dynamic<T> input, int fromVersion, int toVersion) { return fixerUpper.update(this.type, input, fromVersion, toVersion); }
/*    */ 
/*    */ 
/*    */   
/* 83 */   public <T> Dynamic<T> updateToCurrentVersion(DataFixer fixerUpper, Dynamic<T> input, int dataVersion) { return update(fixerUpper, input, dataVersion, currentVersion()); }
/*    */ 
/*    */ 
/*    */   
/* 87 */   public CompoundTag update(DataFixer fixer, CompoundTag tag, int fromVersion, int toVersion) { return (CompoundTag)update(fixer, new Dynamic(NbtOps.INSTANCE, tag), fromVersion, toVersion).getValue(); }
/*    */ 
/*    */ 
/*    */   
/* 91 */   public CompoundTag updateToCurrentVersion(DataFixer fixer, CompoundTag tag, int fromVersion) { return update(fixer, tag, fromVersion, currentVersion()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\DataFixTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
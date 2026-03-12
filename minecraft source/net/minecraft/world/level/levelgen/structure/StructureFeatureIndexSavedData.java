/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.longs.LongCollection;
/*    */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.saveddata.SavedData;
/*    */ import net.minecraft.world.level.saveddata.SavedDataType;
/*    */ 
/*    */ public class StructureFeatureIndexSavedData extends SavedData {
/* 16 */   private static final Codec<LongSet> LONG_SET = Codec.LONG_STREAM.xmap(LongOpenHashSet::toSet, LongCollection::longStream); private final LongSet all;
/*    */   private final LongSet remaining;
/* 18 */   public static final Codec<StructureFeatureIndexSavedData> CODEC = RecordCodecBuilder.create(i -> i.group(LONG_SET
/* 19 */         .fieldOf("All").forGetter(()), LONG_SET
/* 20 */         .fieldOf("Remaining").forGetter(()))
/* 21 */       .apply(i, StructureFeatureIndexSavedData::new));
/*    */ 
/*    */   
/* 24 */   public static SavedDataType<StructureFeatureIndexSavedData> type(String id) { return new SavedDataType(id, StructureFeatureIndexSavedData::new, CODEC, DataFixTypes.SAVED_DATA_STRUCTURE_FEATURE_INDICES); }
/*    */ 
/*    */   
/*    */   private StructureFeatureIndexSavedData(LongSet all, LongSet remaining) {
/* 28 */     this.all = all;
/* 29 */     this.remaining = remaining;
/*    */   }
/*    */ 
/*    */   
/* 33 */   public StructureFeatureIndexSavedData() { this(new LongOpenHashSet(), new LongOpenHashSet()); }
/*    */ 
/*    */   
/*    */   public void addIndex(long chunkPosKey) {
/* 37 */     this.all.add(chunkPosKey);
/* 38 */     this.remaining.add(chunkPosKey);
/* 39 */     setDirty();
/*    */   }
/*    */ 
/*    */   
/* 43 */   public boolean hasStartIndex(long chunkPosKey) { return this.all.contains(chunkPosKey); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public boolean hasUnhandledIndex(long chunkPosKey) { return this.remaining.contains(chunkPosKey); }
/*    */ 
/*    */   
/*    */   public void removeIndex(long chunkPosKey) {
/* 51 */     if (this.remaining.remove(chunkPosKey)) {
/* 52 */       setDirty();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 57 */   public LongSet getAll() { return this.all; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\StructureFeatureIndexSavedData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
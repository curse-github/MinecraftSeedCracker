/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.saveddata.SavedData;
/*    */ import net.minecraft.world.level.saveddata.SavedDataType;
/*    */ 
/*    */ public class MapIndex extends SavedData {
/* 12 */   public static final Codec<MapIndex> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/* 13 */         .optionalFieldOf("map", Integer.valueOf(-1)).forGetter(()))
/* 14 */       .apply(i, MapIndex::new));
/*    */   private static final int NO_MAP_ID = -1;
/* 16 */   public static final SavedDataType<MapIndex> TYPE = new SavedDataType("idcounts", MapIndex::new, CODEC, DataFixTypes.SAVED_DATA_MAP_INDEX);
/*    */   
/*    */   private int lastMapId;
/*    */ 
/*    */   
/* 21 */   public MapIndex() { this(-1); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public MapIndex(int lastMapId) { this.lastMapId = lastMapId; }
/*    */ 
/*    */   
/*    */   public MapId getNextMapId() {
/* 29 */     MapId id = new MapId(++this.lastMapId);
/* 30 */     setDirty();
/* 31 */     return id;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\saveddata\maps\MapIndex.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class BlockDataLayerStorageMap
/*    */   extends DataLayerStorageMap<BlockLightSectionStorage.BlockDataLayerStorageMap>
/*    */ {
/* 31 */   public BlockDataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> map) { super(map); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public BlockDataLayerStorageMap copy() { return new BlockDataLayerStorageMap(this.map.clone()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\lighting\BlockLightSectionStorage$BlockDataLayerStorageMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.LightLayer;
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*    */ 
/*    */ public class BlockLightSectionStorage
/*    */   extends LayerLightSectionStorage<BlockLightSectionStorage.BlockDataLayerStorageMap> {
/* 12 */   protected BlockLightSectionStorage(LightChunkGetter chunkSource) { super(LightLayer.BLOCK, chunkSource, new BlockDataLayerStorageMap(new Long2ObjectOpenHashMap())); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getLightValue(long blockNode) {
/* 17 */     long sectionNode = SectionPos.blockToSection(blockNode);
/* 18 */     DataLayer layer = getDataLayer(sectionNode, false);
/* 19 */     if (layer == null) {
/* 20 */       return 0;
/*    */     }
/* 22 */     return layer.get(
/* 23 */         SectionPos.sectionRelative(BlockPos.getX(blockNode)), 
/* 24 */         SectionPos.sectionRelative(BlockPos.getY(blockNode)), 
/* 25 */         SectionPos.sectionRelative(BlockPos.getZ(blockNode)));
/*    */   }
/*    */   
/*    */   protected static final class BlockDataLayerStorageMap
/*    */     extends DataLayerStorageMap<BlockDataLayerStorageMap>
/*    */   {
/* 31 */     public BlockDataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> map) { super(map); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 36 */     public BlockDataLayerStorageMap copy() { return new BlockDataLayerStorageMap(this.map.clone()); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\lighting\BlockLightSectionStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface CommonLevelAccessor
/*    */   extends LevelReader, LevelSimulatedRW, EntityGetter
/*    */ {
/* 18 */   default <T extends net.minecraft.world.level.block.entity.BlockEntity> Optional<T> getBlockEntity(BlockPos pos, BlockEntityType<T> type) { return super.getBlockEntity(pos, type); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   default List<VoxelShape> getEntityCollisions(Entity source, AABB testArea) { return super.getEntityCollisions(source, testArea); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   default boolean isUnobstructed(Entity source, VoxelShape shape) { return super.isUnobstructed(source, shape); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   default BlockPos getHeightmapPos(Heightmap.Types type, BlockPos pos) { return super.getHeightmapPos(type, pos); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\CommonLevelAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BaseSpawner;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.SpawnData;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
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
/*    */ class null
/*    */   extends BaseSpawner
/*    */ {
/*    */   null(SpawnerBlockEntity this$0) {}
/*    */   
/* 25 */   public void broadcastEvent(Level level, BlockPos pos, int id) { level.blockEvent(pos, Blocks.SPAWNER, id, 0); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setNextSpawnData(Level level, BlockPos pos, SpawnData nextSpawnData) {
/* 30 */     super.setNextSpawnData(level, pos, nextSpawnData);
/* 31 */     if (level != null) {
/* 32 */       BlockState state = level.getBlockState(pos);
/* 33 */       level.sendBlockUpdated(pos, state, state, 260);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\SpawnerBlockEntity$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
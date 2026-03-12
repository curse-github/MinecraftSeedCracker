/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.level.block.Block;
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
/*    */ public interface Strategy
/*    */ {
/*    */   @Deprecated
/*    */   public static final Strategy LEGACY_IRON_GOLEM = (level, pos, blockState, abovePos, aboveState) -> {
/* 60 */       if (blockState.is(Blocks.COBWEB) || blockState
/* 61 */         .is(Blocks.CACTUS) || blockState
/* 62 */         .is(Blocks.GLASS_PANE) || blockState
/* 63 */         .getBlock() instanceof net.minecraft.world.level.block.StainedGlassPaneBlock || blockState
/* 64 */         .getBlock() instanceof net.minecraft.world.level.block.StainedGlassBlock || blockState
/* 65 */         .getBlock() instanceof net.minecraft.world.level.block.LeavesBlock || blockState
/* 66 */         .is(Blocks.CONDUIT) || blockState
/* 67 */         .is(Blocks.ICE) || blockState
/* 68 */         .is(Blocks.TNT) || blockState
/* 69 */         .is(Blocks.GLOWSTONE) || blockState
/* 70 */         .is(Blocks.BEACON) || blockState
/* 71 */         .is(Blocks.SEA_LANTERN) || blockState
/* 72 */         .is(Blocks.FROSTED_ICE) || blockState
/* 73 */         .is(Blocks.TINTED_GLASS) || blockState
/* 74 */         .is(Blocks.GLASS))
/*    */       {
/* 76 */         return false;
/*    */       }
/* 78 */       return ((aboveState.isAir() || aboveState.liquid()) && (blockState.isSolid() || blockState.is(Blocks.POWDER_SNOW)));
/*    */     };
/*    */ 
/*    */   
/* 82 */   public static final Strategy ON_TOP_OF_COLLIDER = (level, pos, blockState, abovePos, aboveState) -> (aboveState.getCollisionShape(level, abovePos).isEmpty() && Block.isFaceFull(blockState.getCollisionShape(level, pos), Direction.UP));
/*    */ 
/*    */   
/* 85 */   public static final Strategy ON_TOP_OF_COLLIDER_NO_LEAVES = (level, pos, blockState, abovePos, aboveState) -> (aboveState.getCollisionShape(level, abovePos).isEmpty() && !blockState.is(BlockTags.LEAVES) && Block.isFaceFull(blockState.getCollisionShape(level, pos), Direction.UP));
/*    */   
/*    */   boolean canSpawnOn(ServerLevel paramServerLevel, BlockPos paramBlockPos1, BlockState paramBlockState1, BlockPos paramBlockPos2, BlockState paramBlockState2);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\SpawnUtil$Strategy.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
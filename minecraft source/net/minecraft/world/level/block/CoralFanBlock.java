/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class CoralFanBlock extends BaseCoralFanBlock {
/* 16 */   public static final MapCodec<CoralFanBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(CoralBlock.DEAD_CORAL_FIELD
/* 17 */         .forGetter(()), 
/* 18 */         propertiesCodec())
/* 19 */       .apply(i, CoralFanBlock::new));
/*    */   
/*    */   private final Block deadBlock;
/*    */   
/* 23 */   public MapCodec<CoralFanBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected CoralFanBlock(Block deadBlock, BlockBehaviour.Properties properties) {
/* 29 */     super(properties);
/* 30 */     this.deadBlock = deadBlock;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) { tryScheduleDieTick(state, level, level, level.random, pos); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 40 */     if (!scanForWater(state, level, pos)) {
/* 41 */       level.setBlock(pos, (BlockState)this.deadBlock.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(false)), 2);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 47 */     if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
/* 48 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 51 */     tryScheduleDieTick(state, level, ticks, random, pos);
/*    */     
/* 53 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 54 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*    */     }
/*    */     
/* 57 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CoralFanBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
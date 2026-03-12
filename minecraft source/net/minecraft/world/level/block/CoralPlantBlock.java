/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class CoralPlantBlock extends BaseCoralPlantTypeBlock {
/* 19 */   public static final MapCodec<CoralPlantBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(CoralBlock.DEAD_CORAL_FIELD
/* 20 */         .forGetter(()), 
/* 21 */         propertiesCodec())
/* 22 */       .apply(i, CoralPlantBlock::new));
/*    */   
/*    */   private final Block deadBlock;
/*    */   
/* 26 */   public MapCodec<CoralPlantBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 15.0D);
/*    */   
/*    */   protected CoralPlantBlock(Block deadBlock, BlockBehaviour.Properties properties) {
/* 34 */     super(properties);
/* 35 */     this.deadBlock = deadBlock;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) { tryScheduleDieTick(state, level, level, level.random, pos); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 45 */     if (!scanForWater(state, level, pos)) {
/* 46 */       level.setBlock(pos, (BlockState)this.deadBlock.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(false)), 2);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 52 */     if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
/* 53 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 56 */     tryScheduleDieTick(state, level, ticks, random, pos);
/*    */     
/* 58 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 59 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*    */     }
/*    */     
/* 62 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 67 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CoralPlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
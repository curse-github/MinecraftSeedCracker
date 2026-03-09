/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*    */ import net.minecraft.world.level.block.state.properties.WoodType;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class StandingSignBlock extends SignBlock {
/* 21 */   public static final MapCodec<StandingSignBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(WoodType.CODEC
/* 22 */         .fieldOf("wood_type").forGetter(SignBlock::type), 
/* 23 */         propertiesCodec())
/* 24 */       .apply(i, StandingSignBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 28 */   public MapCodec<StandingSignBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 31 */   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*    */   
/*    */   public StandingSignBlock(WoodType type, BlockBehaviour.Properties properties) {
/* 34 */     super(type, properties.sound(type.soundType()));
/* 35 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(ROTATION, Integer.valueOf(0))).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return level.getBlockState(pos.below()).isSolid(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 45 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 46 */     return (BlockState)((BlockState)defaultBlockState().setValue(ROTATION, Integer.valueOf(RotationSegment.convertToSegment(context.getRotation() + 180.0F)))).setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 51 */     if (directionToNeighbour == Direction.DOWN && !canSurvive(state, level, pos)) {
/* 52 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 54 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 59 */   public float getYRotationDegrees(BlockState state) { return RotationSegment.convertToDegrees(((Integer)state.getValue(ROTATION)).intValue()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(ROTATION, Integer.valueOf(rotation.rotate(((Integer)state.getValue(ROTATION)).intValue(), 16))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   protected BlockState mirror(BlockState state, Mirror mirror) { return (BlockState)state.setValue(ROTATION, Integer.valueOf(mirror.mirror(((Integer)state.getValue(ROTATION)).intValue(), 16))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { ROTATION, WATERLOGGED }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\StandingSignBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
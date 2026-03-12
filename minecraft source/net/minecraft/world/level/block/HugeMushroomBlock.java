/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class HugeMushroomBlock extends Block {
/* 18 */   public static final MapCodec<HugeMushroomBlock> CODEC = simpleCodec(HugeMushroomBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 22 */   public MapCodec<HugeMushroomBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 25 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/* 26 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/* 27 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/* 28 */   public static final BooleanProperty WEST = PipeBlock.WEST;
/* 29 */   public static final BooleanProperty UP = PipeBlock.UP;
/* 30 */   public static final BooleanProperty DOWN = PipeBlock.DOWN;
/*    */   
/* 32 */   private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
/*    */   
/*    */   public HugeMushroomBlock(BlockBehaviour.Properties properties) {
/* 35 */     super(properties);
/* 36 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(NORTH, Boolean.valueOf(true))).setValue(EAST, Boolean.valueOf(true))).setValue(SOUTH, Boolean.valueOf(true))).setValue(WEST, Boolean.valueOf(true))).setValue(UP, Boolean.valueOf(true))).setValue(DOWN, Boolean.valueOf(true)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 41 */     Level level1 = context.getLevel();
/* 42 */     BlockPos pos = context.getClickedPos();
/*    */     
/* 44 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState()
/* 45 */       .setValue(DOWN, Boolean.valueOf(!level1.getBlockState(pos.below()).is(this))))
/* 46 */       .setValue(UP, Boolean.valueOf(!level1.getBlockState(pos.above()).is(this))))
/* 47 */       .setValue(NORTH, Boolean.valueOf(!level1.getBlockState(pos.north()).is(this))))
/* 48 */       .setValue(EAST, Boolean.valueOf(!level1.getBlockState(pos.east()).is(this))))
/* 49 */       .setValue(SOUTH, Boolean.valueOf(!level1.getBlockState(pos.south()).is(this))))
/* 50 */       .setValue(WEST, Boolean.valueOf(!level1.getBlockState(pos.west()).is(this)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 56 */     if (neighbourState.is(this)) {
/* 57 */       return (BlockState)state.setValue((Property)PROPERTY_BY_DIRECTION.get(directionToNeighbour), Boolean.valueOf(false));
/*    */     }
/* 59 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 64 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)state
/* 65 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.NORTH)), (Boolean)state.getValue(NORTH)))
/* 66 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.SOUTH)), (Boolean)state.getValue(SOUTH)))
/* 67 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.EAST)), (Boolean)state.getValue(EAST)))
/* 68 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.WEST)), (Boolean)state.getValue(WEST)))
/* 69 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.UP)), (Boolean)state.getValue(UP)))
/* 70 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.DOWN)), (Boolean)state.getValue(DOWN));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 76 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)state
/* 77 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.NORTH)), (Boolean)state.getValue(NORTH)))
/* 78 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.SOUTH)), (Boolean)state.getValue(SOUTH)))
/* 79 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.EAST)), (Boolean)state.getValue(EAST)))
/* 80 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.WEST)), (Boolean)state.getValue(WEST)))
/* 81 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.UP)), (Boolean)state.getValue(UP)))
/* 82 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.DOWN)), (Boolean)state.getValue(DOWN));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 88 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { UP, DOWN, NORTH, EAST, SOUTH, WEST }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\HugeMushroomBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
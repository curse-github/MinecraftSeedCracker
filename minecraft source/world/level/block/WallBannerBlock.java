/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WallBannerBlock extends AbstractBannerBlock {
/* 23 */   public static final MapCodec<WallBannerBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(DyeColor.CODEC
/* 24 */         .fieldOf("color").forGetter(AbstractBannerBlock::getColor), 
/* 25 */         propertiesCodec())
/* 26 */       .apply(i, WallBannerBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 30 */   public MapCodec<WallBannerBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 33 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*    */   
/* 35 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(16.0D, 0.0D, 12.5D, 14.0D, 16.0D));
/*    */   
/*    */   public WallBannerBlock(DyeColor color, BlockBehaviour.Properties properties) {
/* 38 */     super(color, properties);
/* 39 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return level.getBlockState(pos.relative(((Direction)state.getValue(FACING)).getOpposite())).isSolid(); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 50 */     if (directionToNeighbour == ((Direction)state.getValue(FACING)).getOpposite() && !state.canSurvive(level, pos)) {
/* 51 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 54 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 59 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)SHAPES.get(state.getValue(FACING)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 64 */     BlockState state = defaultBlockState();
/*    */     
/* 66 */     Level level1 = context.getLevel();
/* 67 */     BlockPos pos = context.getClickedPos();
/*    */     
/* 69 */     Direction[] directions = context.getNearestLookingDirections();
/* 70 */     for (Direction direction : directions) {
/* 71 */       if (direction.getAxis().isHorizontal()) {
/*    */ 
/*    */ 
/*    */         
/* 75 */         Direction facing = direction.getOpposite();
/*    */         
/* 77 */         state = (BlockState)state.setValue(FACING, facing);
/* 78 */         if (state.canSurvive(level1, pos)) {
/* 79 */           return state;
/*    */         }
/*    */       } 
/*    */     } 
/* 83 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 88 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 93 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 98 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WallBannerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
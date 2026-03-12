/*    */ package net.minecraft.world.level.block;
/*    */ import com.google.common.collect.Maps;
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
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BannerBlock extends AbstractBannerBlock {
/* 25 */   public static final MapCodec<BannerBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(DyeColor.CODEC
/* 26 */         .fieldOf("color").forGetter(AbstractBannerBlock::getColor), 
/* 27 */         propertiesCodec())
/* 28 */       .apply(i, BannerBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 32 */   public MapCodec<BannerBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 35 */   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*    */   
/* 37 */   private static final Map<DyeColor, Block> BY_COLOR = Maps.newHashMap();
/* 38 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 16.0D);
/*    */   
/*    */   public BannerBlock(DyeColor color, BlockBehaviour.Properties properties) {
/* 41 */     super(color, properties);
/* 42 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(ROTATION, Integer.valueOf(0)));
/*    */     
/* 44 */     BY_COLOR.put(color, this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 49 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return level.getBlockState(pos.below()).isSolid(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)defaultBlockState().setValue(ROTATION, Integer.valueOf(RotationSegment.convertToSegment(context.getRotation() + 180.0F))); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 64 */     if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
/* 65 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 68 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 73 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(ROTATION, Integer.valueOf(rotation.rotate(((Integer)state.getValue(ROTATION)).intValue(), 16))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 78 */   protected BlockState mirror(BlockState state, Mirror mirror) { return (BlockState)state.setValue(ROTATION, Integer.valueOf(mirror.mirror(((Integer)state.getValue(ROTATION)).intValue(), 16))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 83 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { ROTATION }); }
/*    */ 
/*    */ 
/*    */   
/* 87 */   public static Block byColor(DyeColor color) { return (Block)BY_COLOR.getOrDefault(color, Blocks.WHITE_BANNER); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BannerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
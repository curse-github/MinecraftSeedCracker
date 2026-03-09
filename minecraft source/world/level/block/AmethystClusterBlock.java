/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class AmethystClusterBlock extends AmethystBlock implements SimpleWaterloggedBlock {
/*  29 */   public static final MapCodec<AmethystClusterBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/*  30 */         .fieldOf("height").forGetter(()), Codec.FLOAT
/*  31 */         .fieldOf("width").forGetter(()), 
/*  32 */         propertiesCodec())
/*  33 */       .apply(i, AmethystClusterBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  37 */   public MapCodec<AmethystClusterBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  40 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  41 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
/*     */   
/*     */   private final float height;
/*     */   
/*     */   private final float width;
/*     */   private final Map<Direction, VoxelShape> shapes;
/*     */   
/*     */   public AmethystClusterBlock(float height, float width, BlockBehaviour.Properties props) {
/*  49 */     super(props);
/*  50 */     registerDefaultState((BlockState)((BlockState)defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(false))).setValue(FACING, Direction.UP));
/*  51 */     this.shapes = Shapes.rotateAll(Block.boxZ(width, (16.0F - height), 16.0D));
/*     */     
/*  53 */     this.height = height;
/*  54 */     this.width = width;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  59 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)this.shapes.get(state.getValue(FACING)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  64 */     Direction direction = (Direction)state.getValue(FACING);
/*  65 */     BlockPos adjacentPos = pos.relative(direction.getOpposite());
/*  66 */     return level.getBlockState(adjacentPos).isFaceSturdy(level, adjacentPos, direction);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  71 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  72 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/*  75 */     if (directionToNeighbour == ((Direction)state.getValue(FACING)).getOpposite() && !state.canSurvive(level, pos)) {
/*  76 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  79 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  84 */     Level level1 = context.getLevel();
/*  85 */     BlockPos pos = context.getClickedPos();
/*  86 */     return (BlockState)((BlockState)defaultBlockState()
/*  87 */       .setValue(WATERLOGGED, Boolean.valueOf((level1.getFluidState(pos).getType() == Fluids.WATER))))
/*  88 */       .setValue(FACING, context.getClickedFace());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  93 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 103 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 104 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 106 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 111 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { WATERLOGGED, FACING }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\AmethystClusterBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.level.block.piston;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DirectionalBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PistonHeadBlock extends DirectionalBlock {
/*  36 */   public static final MapCodec<PistonHeadBlock> CODEC = simpleCodec(PistonHeadBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  40 */   protected MapCodec<PistonHeadBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  43 */   public static final EnumProperty<PistonType> TYPE = BlockStateProperties.PISTON_TYPE;
/*  44 */   public static final BooleanProperty SHORT = BlockStateProperties.SHORT;
/*     */   
/*     */   public static final int PLATFORM_THICKNESS = 4;
/*     */   
/*  48 */   private static final VoxelShape SHAPE_PLATFORM = Block.boxZ(16.0D, 0.0D, 4.0D);
/*  49 */   private static final Map<Direction, VoxelShape> SHAPES_SHORT = Shapes.rotateAll(Shapes.or(SHAPE_PLATFORM, 
/*     */         
/*  51 */         Block.boxZ(4.0D, 4.0D, 16.0D)));
/*     */ 
/*     */   
/*  54 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(Shapes.or(SHAPE_PLATFORM, 
/*     */         
/*  56 */         Block.boxZ(4.0D, 4.0D, 20.0D)));
/*     */ 
/*     */   
/*     */   public PistonHeadBlock(BlockBehaviour.Properties properties) {
/*  60 */     super(properties);
/*  61 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(TYPE, PistonType.DEFAULT)).setValue(SHORT, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  66 */   protected boolean useShapeForLightOcclusion(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)(((Boolean)state.getValue(SHORT)).booleanValue() ? SHAPES_SHORT : SHAPES).get(state.getValue(FACING)); }
/*     */ 
/*     */   
/*     */   private boolean isFittingBase(BlockState armState, BlockState potentialBase) {
/*  75 */     Block baseBlock = (armState.getValue(TYPE) == PistonType.DEFAULT) ? Blocks.PISTON : Blocks.STICKY_PISTON;
/*  76 */     return (potentialBase.is(baseBlock) && ((Boolean)potentialBase.getValue(PistonBaseBlock.EXTENDED)).booleanValue() && potentialBase.getValue(FACING) == armState.getValue(FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/*  81 */     if (!level.isClientSide() && player.preventsBlockDrops()) {
/*  82 */       BlockPos basePos = pos.relative(((Direction)state.getValue(FACING)).getOpposite());
/*  83 */       if (isFittingBase(state, level.getBlockState(basePos))) {
/*  84 */         level.destroyBlock(basePos, false);
/*     */       }
/*     */     } 
/*  87 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/*  93 */     BlockPos basePos = pos.relative(((Direction)state.getValue(FACING)).getOpposite());
/*  94 */     if (isFittingBase(state, level.getBlockState(basePos))) {
/*  95 */       level.destroyBlock(basePos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 101 */     if (directionToNeighbour.getOpposite() == state.getValue(FACING) && 
/* 102 */       !state.canSurvive(level, pos)) {
/* 103 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 106 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 111 */     BlockState base = level.getBlockState(pos.relative(((Direction)state.getValue(FACING)).getOpposite()));
/*     */     
/* 113 */     return (isFittingBase(state, base) || (base.is(Blocks.MOVING_PISTON) && base.getValue(FACING) == state.getValue(FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 118 */     if (state.canSurvive(level, pos)) {
/* 119 */       level.neighborChanged(pos.relative(((Direction)state.getValue(FACING)).getOpposite()), block, ExperimentalRedstoneUtils.withFront(orientation, ((Direction)state.getValue(FACING)).getOpposite()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 125 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return new ItemStack((state.getValue(TYPE) == PistonType.STICKY) ? Blocks.STICKY_PISTON : Blocks.PISTON); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, TYPE, SHORT }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\piston\PistonHeadBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
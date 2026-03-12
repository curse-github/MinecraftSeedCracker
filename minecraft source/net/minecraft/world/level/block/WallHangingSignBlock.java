/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WallHangingSignBlock extends SignBlock {
/*  40 */   public static final MapCodec<WallHangingSignBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(WoodType.CODEC
/*  41 */         .fieldOf("wood_type").forGetter(SignBlock::type), 
/*  42 */         propertiesCodec())
/*  43 */       .apply(i, WallHangingSignBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  47 */   public MapCodec<WallHangingSignBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  50 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  52 */   private static final Map<Direction.Axis, VoxelShape> SHAPES_PLANK = Shapes.rotateHorizontalAxis(Block.column(16.0D, 4.0D, 14.0D, 16.0D));
/*  53 */   private static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateHorizontalAxis(Shapes.or((VoxelShape)SHAPES_PLANK
/*  54 */         .get(Direction.Axis.Z), 
/*  55 */         Block.column(14.0D, 2.0D, 0.0D, 10.0D)));
/*     */ 
/*     */   
/*     */   public WallHangingSignBlock(WoodType type, BlockBehaviour.Properties properties) {
/*  59 */     super(type, properties.sound(type.hangingSignSoundType()));
/*  60 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  65 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof SignBlockEntity) { SignBlockEntity signEntity = (SignBlockEntity)blockEntity;
/*  66 */       if (shouldTryToChainAnotherHangingSign(state, player, hitResult, signEntity, itemStack)) {
/*  67 */         return InteractionResult.PASS;
/*     */       } }
/*     */     
/*  70 */     return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
/*     */   }
/*     */   
/*     */   private boolean shouldTryToChainAnotherHangingSign(BlockState state, Player player, BlockHitResult hitResult, SignBlockEntity signEntity, ItemStack itemStack) {
/*  74 */     return (!signEntity.canExecuteClickCommands(signEntity.isFacingFrontText(player), player) && itemStack
/*  75 */       .getItem() instanceof net.minecraft.world.item.HangingSignItem && !isHittingEditableSide(hitResult, state));
/*     */   }
/*     */ 
/*     */   
/*  79 */   private boolean isHittingEditableSide(BlockHitResult hitResult, BlockState state) { return (hitResult.getDirection().getAxis() == ((Direction)state.getValue(FACING)).getAxis()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)SHAPES.get(((Direction)state.getValue(FACING)).getAxis()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) { return getShape(state, level, pos, CollisionContext.empty()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)SHAPES_PLANK.get(((Direction)state.getValue(FACING)).getAxis()); }
/*     */ 
/*     */   
/*     */   public boolean canPlace(BlockState state, LevelReader level, BlockPos pos) {
/*  98 */     Direction clockwise = ((Direction)state.getValue(FACING)).getClockWise();
/*  99 */     Direction counterClockwise = ((Direction)state.getValue(FACING)).getCounterClockWise();
/*     */     
/* 101 */     return (canAttachTo(level, state, pos.relative(clockwise), counterClockwise) || canAttachTo(level, state, pos.relative(counterClockwise), clockwise));
/*     */   }
/*     */   
/*     */   public boolean canAttachTo(LevelReader level, BlockState state, BlockPos attachPos, Direction attachFace) {
/* 105 */     BlockState attachState = level.getBlockState(attachPos);
/*     */ 
/*     */     
/* 108 */     if (attachState.is(BlockTags.WALL_HANGING_SIGNS)) {
/* 109 */       return ((Direction)attachState.getValue(FACING)).getAxis().test((Direction)state.getValue(FACING));
/*     */     }
/*     */     
/* 112 */     return attachState.isFaceSturdy(level, attachPos, attachFace, SupportType.FULL);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 117 */     BlockState state = defaultBlockState();
/* 118 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*     */     
/* 120 */     Level level1 = context.getLevel();
/* 121 */     BlockPos pos = context.getClickedPos();
/*     */     
/* 123 */     for (Direction direction : context.getNearestLookingDirections()) {
/* 124 */       if (direction.getAxis().isHorizontal() && !direction.getAxis().test(context.getClickedFace())) {
/*     */ 
/*     */ 
/*     */         
/* 128 */         Direction facing = direction.getOpposite();
/* 129 */         state = (BlockState)state.setValue(FACING, facing);
/* 130 */         if (state.canSurvive(level1, pos) && canPlace(state, level1, pos)) {
/* 131 */           return (BlockState)state.setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/*     */         }
/*     */       } 
/*     */     } 
/* 135 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 140 */     if (directionToNeighbour.getAxis() == ((Direction)state.getValue(FACING)).getClockWise().getAxis() && !state.canSurvive(level, pos)) {
/* 141 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/* 143 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 148 */   public float getYRotationDegrees(BlockState state) { return ((Direction)state.getValue(FACING)).toYRot(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 168 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new HangingSignBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 173 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) { return createTickerHelper(type, BlockEntityType.HANGING_SIGN, SignBlockEntity::tick); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WallHangingSignBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
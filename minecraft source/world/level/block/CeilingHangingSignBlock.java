/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.stream.Collectors;
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
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CeilingHangingSignBlock extends SignBlock {
/*  44 */   public static final MapCodec<CeilingHangingSignBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(WoodType.CODEC
/*  45 */         .fieldOf("wood_type").forGetter(SignBlock::type), 
/*  46 */         propertiesCodec())
/*  47 */       .apply(i, CeilingHangingSignBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  51 */   public MapCodec<CeilingHangingSignBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  54 */   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*  55 */   public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
/*     */   
/*  57 */   private static final VoxelShape SHAPE_DEFAULT = Block.column(10.0D, 0.0D, 16.0D);
/*  58 */   private static final Map<Integer, VoxelShape> SHAPES = (Map)Shapes.rotateHorizontal(Block.column(14.0D, 2.0D, 0.0D, 10.0D)).entrySet().stream().collect(Collectors.toMap(e -> 
/*  59 */         Integer.valueOf(RotationSegment.convertToSegment((Direction)e.getKey())), Map.Entry::getValue));
/*     */ 
/*     */ 
/*     */   
/*     */   public CeilingHangingSignBlock(WoodType type, BlockBehaviour.Properties properties) {
/*  64 */     super(type, properties.sound(type.hangingSignSoundType()));
/*  65 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(ROTATION, Integer.valueOf(0))).setValue(ATTACHED, Boolean.valueOf(false))).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  70 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof SignBlockEntity) { SignBlockEntity signEntity = (SignBlockEntity)blockEntity;
/*  71 */       if (shouldTryToChainAnotherHangingSign(player, hitResult, signEntity, itemStack)) {
/*  72 */         return InteractionResult.PASS;
/*     */       } }
/*     */     
/*  75 */     return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
/*     */   }
/*     */   
/*     */   private boolean shouldTryToChainAnotherHangingSign(Player player, BlockHitResult hitResult, SignBlockEntity signEntity, ItemStack itemStack) {
/*  79 */     return (!signEntity.canExecuteClickCommands(signEntity.isFacingFrontText(player), player) && itemStack
/*  80 */       .getItem() instanceof net.minecraft.world.item.HangingSignItem && hitResult.getDirection().equals(Direction.DOWN));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  85 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return level.getBlockState(pos.above()).isFaceSturdy(level, pos.above(), Direction.DOWN, SupportType.CENTER); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  90 */     Level level = context.getLevel();
/*  91 */     FluidState replacedFluidState = level.getFluidState(context.getClickedPos());
/*  92 */     BlockPos above = context.getClickedPos().above();
/*  93 */     BlockState stateAbove = level.getBlockState(above);
/*  94 */     boolean isBelowHangingSign = stateAbove.is(BlockTags.ALL_HANGING_SIGNS);
/*  95 */     Direction direction = Direction.fromYRot(context.getRotation());
/*  96 */     boolean attachedToMiddle = (!Block.isFaceFull(stateAbove.getCollisionShape(level, above), Direction.DOWN) || context.isSecondaryUseActive());
/*     */     
/*  98 */     if (isBelowHangingSign && !context.isSecondaryUseActive()) {
/*  99 */       if (stateAbove.hasProperty(WallHangingSignBlock.FACING)) {
/* 100 */         Direction aboveDirection = (Direction)stateAbove.getValue(WallHangingSignBlock.FACING);
/* 101 */         if (aboveDirection.getAxis().test(direction)) {
/* 102 */           attachedToMiddle = false;
/*     */         }
/* 104 */       } else if (stateAbove.hasProperty(ROTATION)) {
/* 105 */         Optional<Direction> aboveDirection = RotationSegment.convertToDirection(((Integer)stateAbove.getValue(ROTATION)).intValue());
/* 106 */         if (aboveDirection.isPresent() && ((Direction)aboveDirection.get()).getAxis().test(direction)) {
/* 107 */           attachedToMiddle = false;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 112 */     int rotationSegment = !attachedToMiddle ? RotationSegment.convertToSegment(direction.getOpposite()) : RotationSegment.convertToSegment(context.getRotation() + 180.0F);
/* 113 */     return (BlockState)((BlockState)((BlockState)defaultBlockState().setValue(ATTACHED, Boolean.valueOf(attachedToMiddle))).setValue(ROTATION, Integer.valueOf(rotationSegment))).setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 118 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)SHAPES.getOrDefault(state.getValue(ROTATION), SHAPE_DEFAULT); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) { return getShape(state, level, pos, CollisionContext.empty()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 128 */     if (directionToNeighbour == Direction.UP && !canSurvive(state, level, pos)) {
/* 129 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/* 131 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 136 */   public float getYRotationDegrees(BlockState state) { return RotationSegment.convertToDegrees(((Integer)state.getValue(ROTATION)).intValue()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(ROTATION, Integer.valueOf(rotation.rotate(((Integer)state.getValue(ROTATION)).intValue(), 16))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   protected BlockState mirror(BlockState state, Mirror mirror) { return (BlockState)state.setValue(ROTATION, Integer.valueOf(mirror.mirror(((Integer)state.getValue(ROTATION)).intValue(), 16))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { ROTATION, ATTACHED, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new HangingSignBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) { return createTickerHelper(type, BlockEntityType.HANGING_SIGN, SignBlockEntity::tick); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CeilingHangingSignBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
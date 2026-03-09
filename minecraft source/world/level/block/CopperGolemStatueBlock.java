/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
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
/*     */ import net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CopperGolemStatueBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*  45 */   public static final MapCodec<CopperGolemStatueBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(WeatheringCopper.WeatherState.CODEC
/*  46 */         .fieldOf("weathering_state").forGetter(CopperGolemStatueBlock::getWeatheringState), 
/*  47 */         propertiesCodec())
/*  48 */       .apply(i, CopperGolemStatueBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  52 */   public MapCodec<? extends CopperGolemStatueBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  55 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*  56 */   public static final EnumProperty<Pose> POSE = BlockStateProperties.COPPER_GOLEM_POSE;
/*  57 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  59 */   private static final VoxelShape SHAPE = Block.column(10.0D, 0.0D, 14.0D);
/*     */   
/*     */   private final WeatheringCopper.WeatherState weatheringState;
/*     */   
/*     */   public CopperGolemStatueBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
/*  64 */     super(properties);
/*  65 */     this.weatheringState = weatherState;
/*  66 */     registerDefaultState((BlockState)((BlockState)((BlockState)defaultBlockState()
/*  67 */         .setValue(FACING, Direction.NORTH))
/*  68 */         .setValue(POSE, Pose.STANDING))
/*  69 */         .setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  75 */     super.createBlockStateDefinition(builder);
/*  76 */     builder.add(new Property[] { FACING, POSE, WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  81 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  82 */     return (BlockState)((BlockState)defaultBlockState()
/*  83 */       .setValue(FACING, context.getHorizontalDirection().getOpposite()))
/*  84 */       .setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  89 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public WeatheringCopper.WeatherState getWeatheringState() { return this.weatheringState; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 108 */     if (itemStack.is(ItemTags.AXES)) {
/* 109 */       return InteractionResult.PASS;
/*     */     }
/* 111 */     updatePose(level, state, pos, player);
/* 112 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   void updatePose(Level level, BlockState state, BlockPos pos, Player player) {
/* 116 */     level.playSound(null, pos, SoundEvents.COPPER_GOLEM_BECOME_STATUE, SoundSource.BLOCKS);
/* 117 */     level.setBlock(pos, (BlockState)state.setValue(POSE, ((Pose)state.getValue(POSE)).getNextPose()), 3);
/* 118 */     level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 123 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return (type == PathComputationType.WATER && state.getFluidState().is(FluidTags.WATER)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new CopperGolemStatueBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public boolean shouldChangedStateKeepBlockEntity(BlockState oldState) { return oldState.is(BlockTags.COPPER_GOLEM_STATUES); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) { return ((Pose)state.getValue(POSE)).ordinal() + 1; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 148 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof CopperGolemStatueBlockEntity) { CopperGolemStatueBlockEntity entity = (CopperGolemStatueBlockEntity)blockEntity;
/* 149 */       return entity.getItem(asItem().getDefaultInstance(), (Pose)state.getValue(POSE)); }
/*     */ 
/*     */     
/* 152 */     return super.getCloneItemStack(level, pos, state, includeData);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 157 */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) { level.updateNeighbourForOutputSignal(pos, state.getBlock()); }
/*     */   
/*     */   public enum Pose
/*     */     implements StringRepresentable {
/* 161 */     STANDING("standing"),
/* 162 */     SITTING("sitting"),
/* 163 */     RUNNING("running"),
/* 164 */     STAR("star"); public static final IntFunction<Pose> BY_ID;
/*     */     static  {
/* 166 */       BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 167 */       CODEC = StringRepresentable.fromEnum(Pose::values);
/*     */     }
/*     */     public static final Codec<Pose> CODEC; private final String name;
/*     */     
/* 171 */     Pose(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/* 180 */     public Pose getNextPose() { return (Pose)BY_ID.apply(ordinal() + 1); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 186 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 187 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 189 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 194 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 195 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/* 197 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CopperGolemStatueBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FlowingFluid;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LiquidBlock extends Block implements BucketPickup {
/*  42 */   private static final Codec<FlowingFluid> FLOWING_FLUID = BuiltInRegistries.FLUID.byNameCodec().comapFlatMap(fluid -> { FlowingFluid flowing = (FlowingFluid)fluid; return (fluid instanceof FlowingFluid) ? DataResult.success(flowing) : DataResult.error(()); }fluid -> fluid);
/*     */   
/*  44 */   public static final MapCodec<LiquidBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(FLOWING_FLUID
/*  45 */         .fieldOf("fluid").forGetter(()), 
/*  46 */         propertiesCodec())
/*  47 */       .apply(i, LiquidBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  51 */   public MapCodec<LiquidBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  54 */   public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;
/*     */   
/*     */   protected final FlowingFluid fluid;
/*     */   
/*     */   private final List<FluidState> stateCache;
/*  59 */   public static final VoxelShape SHAPE_STABLE = Block.column(16.0D, 0.0D, 8.0D);
/*     */   
/*  61 */   public static final ImmutableList<Direction> POSSIBLE_FLOW_DIRECTIONS = ImmutableList.of(Direction.DOWN, Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST);
/*     */   
/*     */   protected LiquidBlock(FlowingFluid fluid, BlockBehaviour.Properties properties) {
/*  64 */     super(properties);
/*  65 */     this.fluid = fluid;
/*  66 */     this.stateCache = Lists.newArrayList();
/*  67 */     this.stateCache.add(fluid.getSource(false));
/*  68 */     for (int level = 1; level < 8; level++) {
/*  69 */       this.stateCache.add(fluid.getFlowing(8 - level, false));
/*     */     }
/*  71 */     this.stateCache.add(fluid.getFlowing(8, true));
/*  72 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(LEVEL, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  77 */     if (context.alwaysCollideWithFluid()) {
/*  78 */       return Shapes.block();
/*     */     }
/*     */     
/*  81 */     if (context.isAbove(SHAPE_STABLE, pos, true) && ((Integer)state.getValue(LEVEL)).intValue() == 0 && context.canStandOnFluid(level.getFluidState(pos.above()), state.getFluidState())) {
/*  82 */       return SHAPE_STABLE;
/*     */     }
/*  84 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  89 */   protected boolean isRandomlyTicking(BlockState state) { return state.getFluidState().isRandomlyTicking(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { state.getFluidState().randomTick(level, pos, random); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   protected boolean propagatesSkylightDown(BlockState state) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return !this.fluid.is(FluidTags.LAVA); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 109 */     int level = ((Integer)state.getValue(LEVEL)).intValue();
/* 110 */     return (FluidState)this.stateCache.get(Math.min(level, 8));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 115 */   protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) { return neighborState.getFluidState().getType().isSame(this.fluid); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   protected RenderShape getRenderShape(BlockState state) { return RenderShape.INVISIBLE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) { return Collections.emptyList(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return Shapes.empty(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 135 */     if (shouldSpreadLiquid(level, pos, state)) {
/* 136 */       level.scheduleTick(pos, state.getFluidState().getType(), this.fluid.getTickDelay(level));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 142 */     if (state.getFluidState().isSource() || neighbourState.getFluidState().isSource()) {
/* 143 */       ticks.scheduleTick(pos, state.getFluidState().getType(), this.fluid.getTickDelay(level));
/*     */     }
/*     */     
/* 146 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 151 */     if (shouldSpreadLiquid(level, pos, state)) {
/* 152 */       level.scheduleTick(pos, state.getFluidState().getType(), this.fluid.getTickDelay(level));
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean shouldSpreadLiquid(Level level, BlockPos pos, BlockState state) {
/* 157 */     if (this.fluid.is(FluidTags.LAVA)) {
/* 158 */       boolean isOverSoulSoil = level.getBlockState(pos.below()).is(Blocks.SOUL_SOIL);
/*     */       
/* 160 */       for (UnmodifiableIterator unmodifiableIterator = POSSIBLE_FLOW_DIRECTIONS.iterator(); unmodifiableIterator.hasNext(); ) { Direction direction = (Direction)unmodifiableIterator.next();
/* 161 */         BlockPos neighbourPos = pos.relative(direction.getOpposite());
/*     */         
/* 163 */         if (level.getFluidState(neighbourPos).is(FluidTags.WATER)) {
/* 164 */           Block convertToBlock = level.getFluidState(pos).isSource() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE;
/* 165 */           level.setBlockAndUpdate(pos, convertToBlock.defaultBlockState());
/* 166 */           fizz(level, pos);
/* 167 */           return false;
/*     */         } 
/*     */         
/* 170 */         if (isOverSoulSoil && level.getBlockState(neighbourPos).is(Blocks.BLUE_ICE)) {
/* 171 */           level.setBlockAndUpdate(pos, Blocks.BASALT.defaultBlockState());
/* 172 */           fizz(level, pos);
/* 173 */           return false;
/*     */         }  }
/*     */     
/*     */     } 
/* 177 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 181 */   private void fizz(LevelAccessor level, BlockPos pos) { level.levelEvent(1501, pos, 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 186 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { LEVEL }); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack pickupBlock(LivingEntity user, LevelAccessor level, BlockPos pos, BlockState state) {
/* 191 */     if (((Integer)state.getValue(LEVEL)).intValue() == 0) {
/* 192 */       level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
/* 193 */       return new ItemStack(this.fluid.getBucket());
/*     */     } 
/* 195 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 200 */   public Optional<SoundEvent> getPickupSound() { return this.fluid.getPickupSound(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\LiquidBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
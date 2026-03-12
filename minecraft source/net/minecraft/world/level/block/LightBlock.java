/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LightBlock extends Block implements SimpleWaterloggedBlock {
/*  32 */   public static final MapCodec<LightBlock> CODEC = simpleCodec(LightBlock::new);
/*     */   
/*     */   public static final int MAX_LEVEL = 15;
/*     */   
/*  36 */   public MapCodec<LightBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*  40 */   public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;
/*  41 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  42 */   public static final ToIntFunction<BlockState> LIGHT_EMISSION = state -> ((Integer)state.getValue(LEVEL)).intValue();
/*     */   
/*     */   public LightBlock(BlockBehaviour.Properties properties) {
/*  45 */     super(properties);
/*  46 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(LEVEL, Integer.valueOf(15))).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  51 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { LEVEL, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  56 */     if (!level.isClientSide() && player.canUseGameMasterBlocks()) {
/*  57 */       level.setBlock(pos, (BlockState)state.cycle(LEVEL), 2);
/*  58 */       return InteractionResult.SUCCESS_SERVER;
/*     */     } 
/*  60 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  65 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return context.isHoldingItem(Items.LIGHT) ? Shapes.block() : Shapes.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   protected boolean propagatesSkylightDown(BlockState state) { return state.getFluidState().isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   protected RenderShape getRenderShape(BlockState state) { return RenderShape.INVISIBLE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) { return 1.0F; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbour, RandomSource random) {
/*  85 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  86 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*  88 */     return super.updateShape(state, level, ticks, pos, direction, neighbourPos, neighbour, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/*  93 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  94 */       return Fluids.WATER.getSource(false);
/*     */     }
/*  96 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 101 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return setLightOnStack(super.getCloneItemStack(level, pos, state, includeData), ((Integer)state.getValue(LEVEL)).intValue()); }
/*     */ 
/*     */   
/*     */   public static ItemStack setLightOnStack(ItemStack result, int lightLevel) {
/* 105 */     result.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(LEVEL, Integer.valueOf(lightLevel)));
/* 106 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\LightBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
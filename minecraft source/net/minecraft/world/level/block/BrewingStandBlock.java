/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BrewingStandBlock extends BaseEntityBlock {
/*  32 */   public static final MapCodec<BrewingStandBlock> CODEC = simpleCodec(BrewingStandBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  36 */   public MapCodec<BrewingStandBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  39 */   public static final BooleanProperty[] HAS_BOTTLE = { BlockStateProperties.HAS_BOTTLE_0, BlockStateProperties.HAS_BOTTLE_1, BlockStateProperties.HAS_BOTTLE_2 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private static final VoxelShape SHAPE = Shapes.or(
/*  46 */       Block.column(2.0D, 2.0D, 14.0D), 
/*  47 */       Block.column(14.0D, 0.0D, 2.0D));
/*     */ 
/*     */   
/*     */   public BrewingStandBlock(BlockBehaviour.Properties properties) {
/*  51 */     super(properties);
/*  52 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(HAS_BOTTLE[0], Boolean.valueOf(false))).setValue(HAS_BOTTLE[1], Boolean.valueOf(false))).setValue(HAS_BOTTLE[2], Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  57 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new BrewingStandBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) { return level.isClientSide() ? null : createTickerHelper(type, BlockEntityType.BREWING_STAND, BrewingStandBlockEntity::serverTick); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  72 */     if (!level.isClientSide()) { BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof BrewingStandBlockEntity) { BrewingStandBlockEntity brewingStandBlockEntity = (BrewingStandBlockEntity)blockEntity;
/*  73 */         player.openMenu(brewingStandBlockEntity);
/*  74 */         player.awardStat(Stats.INTERACT_WITH_BREWINGSTAND); }
/*     */        }
/*  76 */      return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  81 */     double x = pos.getX() + 0.4D + random.nextFloat() * 0.2D;
/*  82 */     double y = pos.getY() + 0.7D + random.nextFloat() * 0.3D;
/*  83 */     double z = pos.getZ() + 0.4D + random.nextFloat() * 0.2D;
/*     */     
/*  85 */     level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  90 */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) { Containers.updateNeighboursAfterDestroy(state, level, pos); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) { return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { HAS_BOTTLE[0], HAS_BOTTLE[1], HAS_BOTTLE[2] }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BrewingStandBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
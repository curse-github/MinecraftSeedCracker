/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.TestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.TestBlockMode;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class TestBlock extends BaseEntityBlock implements GameMasterBlock {
/*  30 */   public static final MapCodec<TestBlock> CODEC = simpleCodec(TestBlock::new);
/*  31 */   public static final EnumProperty<TestBlockMode> MODE = BlockStateProperties.TEST_BLOCK_MODE;
/*     */ 
/*     */   
/*  34 */   public TestBlock(BlockBehaviour.Properties properties) { super(properties); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new TestBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  44 */     BlockItemStateProperties stateProperties = (BlockItemStateProperties)context.getItemInHand().get(DataComponents.BLOCK_STATE);
/*  45 */     BlockState toPlace = defaultBlockState();
/*  46 */     if (stateProperties != null) {
/*  47 */       TestBlockMode mode = (TestBlockMode)stateProperties.get(MODE);
/*  48 */       if (mode != null) {
/*  49 */         toPlace = (BlockState)toPlace.setValue(MODE, mode);
/*     */       }
/*     */     } 
/*  52 */     return toPlace;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  57 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { MODE }); }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*     */     TestBlockEntity testBlockEntity;
/*  62 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  63 */     if (blockEntity instanceof TestBlockEntity) { testBlockEntity = (TestBlockEntity)blockEntity; }
/*  64 */     else { return InteractionResult.PASS; }
/*     */     
/*  66 */     if (!player.canUseGameMasterBlocks())
/*     */     {
/*  68 */       return InteractionResult.PASS;
/*     */     }
/*  70 */     if (level.isClientSide()) {
/*  71 */       player.openTestBlock(testBlockEntity);
/*     */     }
/*  73 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  78 */     TestBlockEntity testBlock = getServerTestBlockEntity(level, pos);
/*  79 */     if (testBlock == null) {
/*     */       return;
/*     */     }
/*     */     
/*  83 */     testBlock.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  88 */     TestBlockEntity testBlock = getServerTestBlockEntity(level, pos);
/*  89 */     if (testBlock == null) {
/*     */       return;
/*     */     }
/*  92 */     if (testBlock.getMode() == TestBlockMode.START) {
/*     */       return;
/*     */     }
/*     */     
/*  96 */     boolean shouldTrigger = level.hasNeighborSignal(pos);
/*  97 */     boolean isPowered = testBlock.isPowered();
/*     */     
/*  99 */     if (shouldTrigger && !isPowered) {
/* 100 */       testBlock.setPowered(true);
/* 101 */       testBlock.trigger();
/* 102 */     } else if (!shouldTrigger && isPowered) {
/* 103 */       testBlock.setPowered(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static TestBlockEntity getServerTestBlockEntity(Level level, BlockPos pos) {
/* 108 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; BlockEntity blockEntity = serverLevel.getBlockEntity(pos); if (blockEntity instanceof TestBlockEntity) return (TestBlockEntity)blockEntity;
/*     */        }
/*     */     
/* 111 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 116 */     if (state.getValue(MODE) != TestBlockMode.START) {
/* 117 */       return 0;
/*     */     }
/* 119 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 120 */     if (blockEntity instanceof TestBlockEntity) { TestBlockEntity testBlock = (TestBlockEntity)blockEntity;
/* 121 */       return testBlock.isPowered() ? 15 : 0; }
/*     */     
/* 123 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 128 */     ItemStack itemStack = super.getCloneItemStack(level, pos, state, includeData);
/* 129 */     return setModeOnStack(itemStack, (TestBlockMode)state.getValue(MODE));
/*     */   }
/*     */   
/*     */   public static ItemStack setModeOnStack(ItemStack itemStack, TestBlockMode mode) {
/* 133 */     itemStack.set(DataComponents.BLOCK_STATE, ((BlockItemStateProperties)itemStack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY)).with(MODE, mode));
/* 134 */     return itemStack;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 139 */   protected MapCodec<TestBlock> codec() { return CODEC; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TestBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
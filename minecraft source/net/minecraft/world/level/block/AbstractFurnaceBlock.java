/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Containers;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public abstract class AbstractFurnaceBlock extends BaseEntityBlock {
/* 26 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/* 27 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*    */   
/*    */   protected AbstractFurnaceBlock(BlockBehaviour.Properties properties) {
/* 30 */     super(properties);
/* 31 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(LIT, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends AbstractFurnaceBlock> codec();
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 39 */     if (!level.isClientSide()) {
/* 40 */       openContainer(level, pos, player);
/*    */     }
/* 42 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract void openContainer(Level paramLevel, BlockPos paramBlockPos, Player paramPlayer);
/*    */ 
/*    */   
/* 49 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) { Containers.updateNeighboursAfterDestroy(state, level, pos); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) { return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 79 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, LIT }); }
/*    */ 
/*    */   
/*    */   protected static <T extends net.minecraft.world.level.block.entity.BlockEntity> BlockEntityTicker<T> createFurnaceTicker(Level level, BlockEntityType<T> actualType, BlockEntityType<? extends AbstractFurnaceBlockEntity> expectedType) {
/* 83 */     ServerLevel serverLevel = (ServerLevel)level; return (level instanceof ServerLevel) ? createTickerHelper(actualType, expectedType, (innerLevel, pos, state, entity) -> AbstractFurnaceBlockEntity.serverTick(serverLevel, pos, state, entity)) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\AbstractFurnaceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
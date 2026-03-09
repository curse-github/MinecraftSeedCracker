/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.Containers;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BarrelBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class BarrelBlock extends BaseEntityBlock {
/* 27 */   public static final MapCodec<BarrelBlock> CODEC = simpleCodec(BarrelBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 31 */   public MapCodec<BarrelBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 34 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
/* 35 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*    */   
/*    */   public BarrelBlock(BlockBehaviour.Properties properties) {
/* 38 */     super(properties);
/* 39 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(OPEN, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 44 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof BarrelBlockEntity) { BarrelBlockEntity barrelBlockEntity = (BarrelBlockEntity)blockEntity;
/* 45 */         player.openMenu(barrelBlockEntity);
/* 46 */         player.awardStat(Stats.OPEN_BARREL);
/* 47 */         PiglinAi.angerNearbyPiglins(serverLevel, player, true); }
/*    */        }
/* 49 */      return InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 54 */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) { Containers.updateNeighboursAfterDestroy(state, level, pos); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 59 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*    */     
/* 61 */     if (blockEntity instanceof BarrelBlockEntity) {
/* 62 */       ((BarrelBlockEntity)blockEntity).recheckOpen();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new BarrelBlockEntity(worldPosition, blockState); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 73 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 78 */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) { return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 83 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 88 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 93 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, OPEN }); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 98 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BarrelBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
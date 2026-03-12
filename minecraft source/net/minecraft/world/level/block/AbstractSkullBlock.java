/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ 
/*    */ public abstract class AbstractSkullBlock extends BaseEntityBlock {
/* 21 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*    */   private final SkullBlock.Type type;
/*    */   
/*    */   public AbstractSkullBlock(SkullBlock.Type type, BlockBehaviour.Properties properties) {
/* 25 */     super(properties);
/* 26 */     this.type = type;
/* 27 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(POWERED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends AbstractSkullBlock> codec();
/*    */ 
/*    */   
/* 35 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new SkullBlockEntity(worldPosition, blockState); }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 40 */     if (level.isClientSide()) {
/*    */ 
/*    */ 
/*    */       
/* 44 */       boolean isAnimated = (blockState.is(Blocks.DRAGON_HEAD) || blockState.is(Blocks.DRAGON_WALL_HEAD) || blockState.is(Blocks.PIGLIN_HEAD) || blockState.is(Blocks.PIGLIN_WALL_HEAD));
/*    */       
/* 46 */       if (isAnimated) {
/* 47 */         return createTickerHelper(type, BlockEntityType.SKULL, SkullBlockEntity::animation);
/*    */       }
/*    */     } 
/* 50 */     return null;
/*    */   }
/*    */ 
/*    */   
/* 54 */   public SkullBlock.Type getType() { return this.type; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { POWERED }); }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 69 */     return (BlockState)defaultBlockState()
/* 70 */       .setValue(POWERED, Boolean.valueOf(context.getLevel().hasNeighborSignal(context.getClickedPos())));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 75 */     if (level.isClientSide()) {
/*    */       return;
/*    */     }
/*    */     
/* 79 */     boolean signal = level.hasNeighborSignal(pos);
/* 80 */     if (signal != ((Boolean)state.getValue(POWERED)).booleanValue())
/* 81 */       level.setBlock(pos, (BlockState)state.setValue(POWERED, Boolean.valueOf(signal)), 2); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\AbstractSkullBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
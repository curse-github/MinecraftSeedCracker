/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.CalibratedSculkSensorBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*    */ 
/*    */ public class CalibratedSculkSensorBlock extends SculkSensorBlock {
/* 21 */   public static final MapCodec<CalibratedSculkSensorBlock> CODEC = simpleCodec(CalibratedSculkSensorBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 25 */   public MapCodec<CalibratedSculkSensorBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 28 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*    */   
/*    */   public CalibratedSculkSensorBlock(BlockBehaviour.Properties properties) {
/* 31 */     super(properties);
/* 32 */     registerDefaultState((BlockState)defaultBlockState().setValue(FACING, Direction.NORTH));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new CalibratedSculkSensorBlockEntity(worldPosition, blockState); }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 42 */     if (!level.isClientSide()) {
/* 43 */       return createTickerHelper(type, BlockEntityType.CALIBRATED_SCULK_SENSOR, (innerLevel, pos, state, entity) -> VibrationSystem.Ticker.tick(innerLevel, entity.getVibrationData(), entity.getVibrationUser()));
/*    */     }
/*    */     
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 56 */     if (direction != state.getValue(FACING)) {
/* 57 */       return super.getSignal(state, level, pos, direction);
/*    */     }
/* 59 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 64 */     super.createBlockStateDefinition(builder);
/* 65 */     builder.add(new Property[] { FACING });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   public BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 81 */   public int getActiveTicks() { return 10; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CalibratedSculkSensorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
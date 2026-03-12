/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ 
/*    */ public class CopperBulbBlock extends Block {
/* 19 */   public static final MapCodec<CopperBulbBlock> CODEC = simpleCodec(CopperBulbBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 23 */   protected MapCodec<? extends CopperBulbBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 26 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/* 27 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*    */   
/*    */   public CopperBulbBlock(BlockBehaviour.Properties properties) {
/* 30 */     super(properties);
/* 31 */     registerDefaultState((BlockState)((BlockState)defaultBlockState().setValue(LIT, Boolean.valueOf(false))).setValue(POWERED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 36 */     if (oldState.getBlock() != state.getBlock() && level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 37 */       checkAndFlip(state, serverLevel, pos); }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 43 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 44 */       checkAndFlip(state, serverLevel, pos); }
/*    */   
/*    */   }
/*    */   
/*    */   public void checkAndFlip(BlockState state, ServerLevel level, BlockPos pos) {
/* 49 */     boolean signal = level.hasNeighborSignal(pos);
/*    */     
/* 51 */     if (signal == ((Boolean)state.getValue(POWERED)).booleanValue()) {
/*    */       return;
/*    */     }
/*    */     
/* 55 */     BlockState newState = state;
/* 56 */     if (!((Boolean)state.getValue(POWERED)).booleanValue()) {
/* 57 */       newState = (BlockState)newState.cycle(LIT);
/* 58 */       level.playSound(null, pos, ((Boolean)newState.getValue(LIT)).booleanValue() ? SoundEvents.COPPER_BULB_TURN_ON : SoundEvents.COPPER_BULB_TURN_OFF, SoundSource.BLOCKS);
/*    */     } 
/* 60 */     level.setBlock(pos, (BlockState)newState.setValue(POWERED, Boolean.valueOf(signal)), 3);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 65 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { LIT, POWERED }); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 70 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 75 */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) { return ((Boolean)level.getBlockState(pos).getValue(LIT)).booleanValue() ? 15 : 0; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CopperBulbBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
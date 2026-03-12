/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ 
/*    */ public class RedstoneLampBlock extends Block {
/* 16 */   public static final MapCodec<RedstoneLampBlock> CODEC = simpleCodec(RedstoneLampBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 20 */   public MapCodec<RedstoneLampBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 23 */   public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
/*    */   
/*    */   public RedstoneLampBlock(BlockBehaviour.Properties properties) {
/* 26 */     super(properties);
/* 27 */     registerDefaultState((BlockState)defaultBlockState().setValue(LIT, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)defaultBlockState().setValue(LIT, Boolean.valueOf(context.getLevel().hasNeighborSignal(context.getClickedPos()))); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 37 */     if (level.isClientSide()) {
/*    */       return;
/*    */     }
/*    */     
/* 41 */     boolean isLit = ((Boolean)state.getValue(LIT)).booleanValue();
/* 42 */     if (isLit != level.hasNeighborSignal(pos)) {
/* 43 */       if (isLit) {
/* 44 */         level.scheduleTick(pos, this, 4);
/*    */       } else {
/* 46 */         level.setBlock(pos, (BlockState)state.cycle(LIT), 2);
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 53 */     if (((Boolean)state.getValue(LIT)).booleanValue() && !level.hasNeighborSignal(pos)) {
/* 54 */       level.setBlock(pos, (BlockState)state.cycle(LIT), 2);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 60 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { LIT }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\RedstoneLampBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
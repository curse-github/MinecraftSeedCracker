/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.stats.Stat;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TrappedChestBlock extends ChestBlock {
/* 20 */   public static final MapCodec<TrappedChestBlock> CODEC = simpleCodec(TrappedChestBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 24 */   public MapCodec<TrappedChestBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public TrappedChestBlock(BlockBehaviour.Properties properties) { super(() -> BlockEntityType.TRAPPED_CHEST, SoundEvents.CHEST_OPEN, SoundEvents.CHEST_CLOSE, properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new TrappedChestBlockEntity(worldPosition, blockState); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   protected Stat<Identifier> getOpenChestStat() { return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   protected boolean isSignalSource(BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return Mth.clamp(ChestBlockEntity.getOpenCount(level, pos), 0, 15); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 53 */     if (direction == Direction.UP) {
/* 54 */       return state.getSignal(level, pos, direction);
/*    */     }
/*    */     
/* 57 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TrappedChestBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
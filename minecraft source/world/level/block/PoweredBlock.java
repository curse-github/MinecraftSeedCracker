/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class PoweredBlock extends Block {
/* 11 */   public static final MapCodec<PoweredBlock> CODEC = simpleCodec(PoweredBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 15 */   public MapCodec<PoweredBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public PoweredBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   protected boolean isSignalSource(BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 15; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\PoweredBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
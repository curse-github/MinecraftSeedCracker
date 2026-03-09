/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MyceliumBlock extends SpreadingSnowyDirtBlock {
/* 11 */   public static final MapCodec<MyceliumBlock> CODEC = simpleCodec(MyceliumBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 15 */   public MapCodec<MyceliumBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public MyceliumBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 24 */     super.animateTick(state, level, pos, random);
/* 25 */     if (random.nextInt(10) == 0)
/* 26 */       level.addParticle(ParticleTypes.MYCELIUM, pos.getX() + random.nextDouble(), pos.getY() + 1.1D, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\MyceliumBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
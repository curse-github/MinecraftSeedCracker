/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class CryingObsidianBlock extends Block {
/* 12 */   public static final MapCodec<CryingObsidianBlock> CODEC = simpleCodec(CryingObsidianBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 16 */   public MapCodec<CryingObsidianBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public CryingObsidianBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 25 */     if (random.nextInt(5) != 0) {
/*    */       return;
/*    */     }
/*    */     
/* 29 */     Direction dir = Direction.getRandom(random);
/* 30 */     if (dir == Direction.UP) {
/*    */       return;
/*    */     }
/* 33 */     BlockPos relativePos = pos.relative(dir);
/* 34 */     BlockState blockState = level.getBlockState(relativePos);
/* 35 */     if (state.canOcclude() && blockState.isFaceSturdy(level, relativePos, dir.getOpposite())) {
/*    */       return;
/*    */     }
/*    */     
/* 39 */     double xOffset = (dir.getStepX() == 0) ? random.nextDouble() : (0.5D + dir.getStepX() * 0.6D);
/* 40 */     double yOffset = (dir.getStepY() == 0) ? random.nextDouble() : (0.5D + dir.getStepY() * 0.6D);
/* 41 */     double zOffset = (dir.getStepZ() == 0) ? random.nextDouble() : (0.5D + dir.getStepZ() * 0.6D);
/*    */     
/* 43 */     level.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CryingObsidianBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
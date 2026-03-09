/*    */ package net.minecraft.world.entity.item;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Explosion;
/*    */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends ExplosionDamageCalculator
/*    */ {
/*    */   public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) {
/* 46 */     if (state.is(Blocks.NETHER_PORTAL)) {
/* 47 */       return false;
/*    */     }
/* 49 */     return super.shouldBlockExplode(explosion, level, pos, state, power);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid) {
/* 54 */     if (block.is(Blocks.NETHER_PORTAL)) {
/* 55 */       return Optional.empty();
/*    */     }
/* 57 */     return super.getBlockExplosionResistance(explosion, level, pos, block, fluid);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\item\PrimedTnt$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ExplosionDamageCalculator
/*    */ {
/*    */   public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid) {
/* 13 */     if (block.isAir() && fluid.isEmpty()) {
/* 14 */       return Optional.empty();
/*    */     }
/* 16 */     return Optional.of(Float.valueOf(Math.max(block.getBlock().getExplosionResistance(), fluid.getExplosionResistance())));
/*    */   }
/*    */ 
/*    */   
/* 20 */   public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) { return true; }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public boolean shouldDamageEntity(Explosion explosion, Entity entity) { return true; }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public float getKnockbackMultiplier(Entity entity) { return 1.0F; }
/*    */ 
/*    */   
/*    */   public float getEntityDamageAmount(Explosion explosion, Entity entity, float exposure) {
/* 32 */     float doubleRadius = explosion.radius() * 2.0F;
/* 33 */     Vec3 center = explosion.center();
/*    */     
/* 35 */     double dist = Math.sqrt(entity.distanceToSqr(center)) / doubleRadius;
/* 36 */     double pow = (1.0D - dist) * exposure;
/*    */     
/* 38 */     return (float)((pow * pow + pow) / 2.0D * 7.0D * doubleRadius + 1.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\ExplosionDamageCalculator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
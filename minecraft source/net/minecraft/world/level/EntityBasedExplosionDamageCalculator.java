/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class EntityBasedExplosionDamageCalculator
/*    */   extends ExplosionDamageCalculator
/*    */ {
/*    */   private final Entity source;
/*    */   
/* 14 */   public EntityBasedExplosionDamageCalculator(Entity source) { this.source = source; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid) { return super.getBlockExplosionResistance(explosion, level, pos, block, fluid).map(resistance -> Float.valueOf(this.source.getBlockExplosionResistance(explosion, level, pos, block, fluid, resistance.floatValue()))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) { return this.source.shouldBlockExplode(explosion, level, pos, state, power); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\EntityBasedExplosionDamageCalculator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
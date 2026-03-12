/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class SlimeBlock extends HalfTransparentBlock {
/* 13 */   public static final MapCodec<SlimeBlock> CODEC = simpleCodec(SlimeBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 17 */   public MapCodec<SlimeBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public SlimeBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
/* 26 */     if (!entity.isSuppressingBounce())
/*    */     {
/* 28 */       entity.causeFallDamage(fallDistance, 0.0F, level.damageSources().fall());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateEntityMovementAfterFallOn(BlockGetter level, Entity entity) {
/* 34 */     if (entity.isSuppressingBounce()) {
/* 35 */       super.updateEntityMovementAfterFallOn(level, entity);
/*    */     } else {
/* 37 */       bounceUp(entity);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void bounceUp(Entity entity) {
/* 42 */     Vec3 movement = entity.getDeltaMovement();
/* 43 */     if (movement.y < 0.0D) {
/*    */       
/* 45 */       double factor = (entity instanceof net.minecraft.world.entity.LivingEntity) ? 1.0D : 0.8D;
/* 46 */       entity.setDeltaMovement(movement.x, -movement.y * factor, movement.z);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
/* 56 */     double absDeltaY = Math.abs((entity.getDeltaMovement()).y);
/* 57 */     if (absDeltaY < 0.1D && !entity.isSteppingCarefully()) {
/* 58 */       double scale = 0.4D + absDeltaY * 0.2D;
/* 59 */       entity.setDeltaMovement(entity.getDeltaMovement().multiply(scale, 1.0D, scale));
/*    */     } 
/* 61 */     super.stepOn(level, pos, onState, entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SlimeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Fallable
/*    */ {
/*    */   default void onLand(Level level, BlockPos pos, BlockState state, BlockState replacedBlock, FallingBlockEntity entity) {}
/*    */   
/*    */   default void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity entity) {}
/*    */   
/* 19 */   default DamageSource getFallDamageSource(Entity entity) { return entity.damageSources().fallingBlock(entity); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\Fallable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
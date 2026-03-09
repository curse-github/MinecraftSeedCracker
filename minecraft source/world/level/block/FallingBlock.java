/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.BlockParticleOption;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.ParticleUtils;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ public abstract class FallingBlock
/*    */   extends Block
/*    */   implements Fallable
/*    */ {
/* 25 */   public FallingBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends FallingBlock> codec();
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) { level.scheduleTick(pos, this, getDelayAfterPlace()); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 38 */     ticks.scheduleTick(pos, this, getDelayAfterPlace());
/*    */     
/* 40 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 45 */     if (!isFree(level.getBlockState(pos.below())) || pos.getY() < level.getMinY()) {
/*    */       return;
/*    */     }
/*    */     
/* 49 */     FallingBlockEntity entity = FallingBlockEntity.fall(level, pos, state);
/* 50 */     falling(entity);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void falling(FallingBlockEntity entity) {}
/*    */ 
/*    */   
/* 57 */   protected int getDelayAfterPlace() { return 2; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   public static boolean isFree(BlockState state) { return (state.isAir() || state.is(BlockTags.FIRE) || state.liquid() || state.canBeReplaced()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 67 */     if (random.nextInt(16) == 0) {
/* 68 */       BlockPos below = pos.below();
/*    */       
/* 70 */       if (isFree(level.getBlockState(below)))
/* 71 */         ParticleUtils.spawnParticleBelow(level, pos, random, new BlockParticleOption(ParticleTypes.FALLING_DUST, state)); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract int getDustColor(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\FallingBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
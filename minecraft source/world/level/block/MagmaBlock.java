/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MagmaBlock extends Block {
/* 16 */   public static final MapCodec<MagmaBlock> CODEC = simpleCodec(MagmaBlock::new);
/*    */   
/*    */   private static final int BUBBLE_COLUMN_CHECK_DELAY = 20;
/*    */   
/* 20 */   public MapCodec<MagmaBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public MagmaBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
/* 31 */     if (!entity.isSteppingCarefully() && entity instanceof net.minecraft.world.entity.LivingEntity) {
/* 32 */       entity.hurt(level.damageSources().hotFloor(), 1.0F);
/*    */     }
/*    */     
/* 35 */     super.stepOn(level, pos, onState, entity);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { BubbleColumnBlock.updateColumn(level, pos.above(), state); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 45 */     if (directionToNeighbour == Direction.UP && neighbourState.is(Blocks.WATER)) {
/* 46 */       ticks.scheduleTick(pos, this, 20);
/*    */     }
/*    */     
/* 49 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 54 */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) { level.scheduleTick(pos, this, 20); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\MagmaBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
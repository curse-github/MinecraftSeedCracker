/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.ticks.LevelTickAccess;
/*    */ import net.minecraft.world.ticks.ScheduledTick;
/*    */ import net.minecraft.world.ticks.TickPriority;
/*    */ 
/*    */ public interface ScheduledTickAccess
/*    */ {
/*    */   <T> ScheduledTick<T> createTick(BlockPos paramBlockPos, T paramT, int paramInt, TickPriority paramTickPriority);
/*    */   
/*    */   <T> ScheduledTick<T> createTick(BlockPos paramBlockPos, T paramT, int paramInt);
/*    */   
/*    */   LevelTickAccess<Block> getBlockTicks();
/*    */   
/* 18 */   default void scheduleTick(BlockPos pos, Block type, int tickDelay, TickPriority priority) { getBlockTicks().schedule(createTick(pos, type, tickDelay, priority)); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   default void scheduleTick(BlockPos pos, Block type, int tickDelay) { getBlockTicks().schedule(createTick(pos, type, tickDelay)); }
/*    */ 
/*    */   
/*    */   LevelTickAccess<Fluid> getFluidTicks();
/*    */ 
/*    */   
/* 28 */   default void scheduleTick(BlockPos pos, Fluid type, int tickDelay, TickPriority priority) { getFluidTicks().schedule(createTick(pos, type, tickDelay, priority)); }
/*    */ 
/*    */ 
/*    */   
/* 32 */   default void scheduleTick(BlockPos pos, Fluid type, int tickDelay) { getFluidTicks().schedule(createTick(pos, type, tickDelay)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\ScheduledTickAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
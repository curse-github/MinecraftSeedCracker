/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class WorldGenTickAccess<T>
/*    */   extends Object
/*    */   implements LevelTickAccess<T> {
/*    */   private final Function<BlockPos, TickContainerAccess<T>> containerGetter;
/*    */   
/* 11 */   public WorldGenTickAccess(Function<BlockPos, TickContainerAccess<T>> containerGetter) { this.containerGetter = containerGetter; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public boolean hasScheduledTick(BlockPos pos, T type) { return ((TickContainerAccess)this.containerGetter.apply(pos)).hasScheduledTick(pos, type); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public void schedule(ScheduledTick<T> tick) { ((TickContainerAccess)this.containerGetter.apply(tick.pos())).schedule(tick); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public boolean willTickThisTick(BlockPos pos, T type) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public int count() { return 0; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ticks\WorldGenTickAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
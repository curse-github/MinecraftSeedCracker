/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
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
/*    */   extends Object
/*    */   implements LevelTickAccess<Object>
/*    */ {
/*    */   public void schedule(ScheduledTick<Object> tick) {}
/*    */   
/* 32 */   public boolean hasScheduledTick(BlockPos pos, Object type) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public boolean willTickThisTick(BlockPos pos, Object type) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public int count() { return 0; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ticks\BlackholeTickAccess$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
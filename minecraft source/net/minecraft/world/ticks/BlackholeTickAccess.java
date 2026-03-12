/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlackholeTickAccess
/*    */ {
/*  9 */   private static final TickContainerAccess<Object> CONTAINER_BLACKHOLE = new TickContainerAccess<Object>()
/*    */     {
/*    */       public void schedule(ScheduledTick<Object> tick) {}
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 16 */       public boolean hasScheduledTick(BlockPos pos, Object type) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 21 */       public int count() { return 0; }
/*    */     };
/*    */ 
/*    */   
/* 25 */   private static final LevelTickAccess<Object> LEVEL_BLACKHOLE = new LevelTickAccess<Object>()
/*    */     {
/*    */       public void schedule(ScheduledTick<Object> tick) {}
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 32 */       public boolean hasScheduledTick(BlockPos pos, Object type) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 37 */       public boolean willTickThisTick(BlockPos pos, Object type) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 42 */       public int count() { return 0; }
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public static <T> TickContainerAccess<T> emptyContainer() { return CONTAINER_BLACKHOLE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public static <T> LevelTickAccess<T> emptyLevelList() { return LEVEL_BLACKHOLE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ticks\BlackholeTickAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
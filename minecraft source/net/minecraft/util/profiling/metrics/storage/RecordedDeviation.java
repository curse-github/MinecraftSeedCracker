/*    */ package net.minecraft.util.profiling.metrics.storage;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import net.minecraft.util.profiling.ProfileResults;
/*    */ 
/*    */ public final class RecordedDeviation
/*    */ {
/*    */   public final Instant timestamp;
/*    */   public final int tick;
/*    */   public final ProfileResults profilerResultAtTick;
/*    */   
/*    */   public RecordedDeviation(Instant timestamp, int tick, ProfileResults profilerResultAtTick) {
/* 13 */     this.timestamp = timestamp;
/* 14 */     this.tick = tick;
/* 15 */     this.profilerResultAtTick = profilerResultAtTick;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\metrics\storage\RecordedDeviation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
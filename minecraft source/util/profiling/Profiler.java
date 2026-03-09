/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import com.mojang.jtracy.TracyClient;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public final class Profiler
/*    */ {
/* 10 */   private static final ThreadLocal<TracyZoneFiller> TRACY_FILLER = ThreadLocal.withInitial(TracyZoneFiller::new);
/*    */   
/* 12 */   private static final ThreadLocal<ProfilerFiller> ACTIVE = new ThreadLocal();
/* 13 */   private static final AtomicInteger ACTIVE_COUNT = new AtomicInteger();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Scope use(ProfilerFiller filler) {
/* 19 */     startUsing(filler);
/* 20 */     return Profiler::stopUsing;
/*    */   }
/*    */   
/*    */   private static void startUsing(ProfilerFiller filler) {
/* 24 */     if (ACTIVE.get() != null) {
/* 25 */       throw new IllegalStateException("Profiler is already active");
/*    */     }
/* 27 */     ProfilerFiller active = decorateFiller(filler);
/* 28 */     ACTIVE.set(active);
/* 29 */     ACTIVE_COUNT.incrementAndGet();
/* 30 */     active.startTick();
/*    */   }
/*    */   
/*    */   private static void stopUsing() {
/* 34 */     active = (ProfilerFiller)ACTIVE.get();
/* 35 */     if (active == null) {
/* 36 */       throw new IllegalStateException("Profiler was not active");
/*    */     }
/* 38 */     ACTIVE.remove();
/* 39 */     ACTIVE_COUNT.decrementAndGet();
/* 40 */     active.endTick();
/*    */   }
/*    */ 
/*    */   
/* 44 */   private static ProfilerFiller decorateFiller(ProfilerFiller filler) { return ProfilerFiller.combine(getDefaultFiller(), filler); }
/*    */ 
/*    */   
/*    */   public static ProfilerFiller get() {
/* 48 */     if (ACTIVE_COUNT.get() == 0)
/*    */     {
/* 50 */       return getDefaultFiller();
/*    */     }
/* 52 */     return (ProfilerFiller)Objects.requireNonNullElseGet((ProfilerFiller)ACTIVE.get(), Profiler::getDefaultFiller);
/*    */   }
/*    */   
/*    */   private static ProfilerFiller getDefaultFiller() {
/* 56 */     if (TracyClient.isAvailable()) {
/* 57 */       return (ProfilerFiller)TRACY_FILLER.get();
/*    */     }
/* 59 */     return InactiveProfiler.INSTANCE;
/*    */   }
/*    */   
/*    */   public static interface Scope extends AutoCloseable {
/*    */     void close();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\Profiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
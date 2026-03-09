/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.Semaphore;
/*    */ import java.util.concurrent.locks.Lock;
/*    */ import java.util.concurrent.locks.ReentrantLock;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ThreadingDetector
/*    */ {
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   private final String name;
/*    */   
/*    */   public ThreadingDetector(String name) {
/* 23 */     this.lock = new Semaphore(1);
/*    */     
/* 25 */     this.stackTraceLock = new ReentrantLock();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 30 */     this.name = name;
/*    */   }
/*    */   private final Semaphore lock; private final Lock stackTraceLock;
/*    */   public void checkAndLock() {
/* 34 */     released = false;
/*    */     try {
/* 36 */       this.stackTraceLock.lock();
/*    */ 
/*    */       
/* 39 */       if (!this.lock.tryAcquire()) {
/*    */         
/* 41 */         this.threadThatFailedToAcquire = Thread.currentThread();
/* 42 */         released = true;
/* 43 */         this.stackTraceLock.unlock();
/*    */         
/*    */         try {
/* 46 */           this.lock.acquire();
/* 47 */         } catch (InterruptedException ignored) {
/* 48 */           Thread.currentThread().interrupt();
/*    */         } 
/*    */         
/* 51 */         throw this.fullException;
/*    */       } 
/*    */     } finally {
/* 54 */       if (!released) {
/* 55 */         this.stackTraceLock.unlock();
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public void checkAndUnlock() {
/*    */     try {
/* 62 */       this.stackTraceLock.lock();
/* 63 */       Thread threadThatFailedToAcquire = this.threadThatFailedToAcquire;
/* 64 */       if (threadThatFailedToAcquire != null) {
/*    */ 
/*    */         
/* 67 */         ReportedException fullException = makeThreadingException(this.name, threadThatFailedToAcquire);
/* 68 */         this.fullException = fullException;
/* 69 */         this.lock.release();
/* 70 */         throw fullException;
/*    */       } 
/* 72 */       this.lock.release();
/*    */     } finally {
/*    */       
/* 75 */       this.stackTraceLock.unlock();
/*    */     } 
/*    */   }
/*    */   
/*    */   public static ReportedException makeThreadingException(String name, Thread threadThatFailedToAcquire) {
/* 80 */     String threads = (String)Stream.of(new Thread[] { Thread.currentThread(), threadThatFailedToAcquire }).filter(Objects::nonNull).map(ThreadingDetector::stackTrace).collect(Collectors.joining("\n"));
/* 81 */     String error = "Accessing " + name + " from multiple threads";
/* 82 */     CrashReport report = new CrashReport(error, new IllegalStateException(error));
/* 83 */     CrashReportCategory category = report.addCategory("Thread dumps");
/* 84 */     category.setDetail("Thread dumps", threads);
/* 85 */     LOGGER.error("Thread dumps: \n{}", threads);
/* 86 */     return new ReportedException(report);
/*    */   }
/*    */ 
/*    */   
/* 90 */   private static String stackTrace(Thread thread) { return thread.getName() + ": \n\tat " + thread.getName(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ThreadingDetector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
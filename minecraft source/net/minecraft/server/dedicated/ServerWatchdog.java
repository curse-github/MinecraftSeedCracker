/*     */ package net.minecraft.server.dedicated;
/*     */ 
/*     */ import com.google.common.collect.Streams;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.ThreadInfo;
/*     */ import java.lang.management.ThreadMXBean;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Locale;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportType;
/*     */ import net.minecraft.server.Bootstrap;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerWatchdog implements Runnable {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final long MAX_SHUTDOWN_TIME = 10000L;
/*     */   private static final int SHUTDOWN_STATUS = 1;
/*     */   private final DedicatedServer server;
/*     */   private final long maxTickTimeNanos;
/*     */   
/*     */   public ServerWatchdog(DedicatedServer server) {
/*  32 */     this.server = server;
/*  33 */     this.maxTickTimeNanos = server.getMaxTickLength() * TimeUtil.NANOSECONDS_PER_MILLISECOND;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  38 */     while (this.server.isRunning()) {
/*  39 */       long nextTickTimeNanos = this.server.getNextTickTime();
/*  40 */       long currentTimeNanos = Util.getNanos();
/*  41 */       long deltaNanos = currentTimeNanos - nextTickTimeNanos;
/*     */       
/*  43 */       if (deltaNanos > this.maxTickTimeNanos) {
/*  44 */         LOGGER.error(LogUtils.FATAL_MARKER, "A single server tick took {} seconds (should be max {})", String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf((float)deltaNanos / (float)TimeUtil.NANOSECONDS_PER_SECOND) }), String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(this.server.tickRateManager().millisecondsPerTick() / (float)TimeUtil.MILLISECONDS_PER_SECOND) }));
/*  45 */         LOGGER.error(LogUtils.FATAL_MARKER, "Considering it to be crashed, server will forcibly shutdown.");
/*     */         
/*  47 */         CrashReport report = createWatchdogCrashReport("Watching Server", this.server.getRunningThread().threadId());
/*  48 */         this.server.fillSystemReport(report.getSystemReport());
/*     */         
/*  50 */         CrashReportCategory serverStats = report.addCategory("Performance stats");
/*  51 */         serverStats.setDetail("Random tick rate", () -> this.server.getWorldData().getGameRules().getAsString(GameRules.RANDOM_TICK_SPEED));
/*  52 */         serverStats.setDetail("Level stats", () -> (String)Streams.stream(this.server.getAllLevels()).map(()).collect(Collectors.joining(",\n")));
/*     */         
/*  54 */         Bootstrap.realStdoutPrintln("Crash report:\n" + report.getFriendlyReport(ReportType.CRASH));
/*     */         
/*  56 */         Path file = this.server.getServerDirectory().resolve("crash-reports").resolve("crash-" + Util.getFilenameFormattedDateTime() + "-server.txt");
/*  57 */         if (report.saveToFile(file, ReportType.CRASH)) {
/*  58 */           LOGGER.error("This crash report has been saved to: {}", file.toAbsolutePath());
/*     */         } else {
/*  60 */           LOGGER.error("We were unable to save this crash report to disk.");
/*     */         } 
/*     */         
/*  63 */         exit();
/*     */       } 
/*     */       
/*     */       try {
/*  67 */         Thread.sleep((nextTickTimeNanos + this.maxTickTimeNanos - currentTimeNanos) / TimeUtil.NANOSECONDS_PER_MILLISECOND);
/*  68 */       } catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static CrashReport createWatchdogCrashReport(String message, long mainThreadId) {
/*  74 */     ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
/*  75 */     ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);
/*     */     
/*  77 */     StringBuilder builder = new StringBuilder();
/*  78 */     Error exception = new Error("Watchdog");
/*     */     
/*  80 */     for (ThreadInfo threadInfo : threadInfos) {
/*  81 */       if (threadInfo.getThreadId() == mainThreadId) {
/*  82 */         exception.setStackTrace(threadInfo.getStackTrace());
/*     */       }
/*     */       
/*  85 */       builder.append(threadInfo);
/*  86 */       builder.append("\n");
/*     */     } 
/*     */     
/*  89 */     CrashReport report = new CrashReport(message, exception);
/*  90 */     CrashReportCategory threadDump = report.addCategory("Thread Dump");
/*  91 */     threadDump.setDetail("Threads", builder);
/*  92 */     return report;
/*     */   }
/*     */   
/*     */   private void exit() {
/*     */     try {
/*  97 */       Timer timer = new Timer();
/*  98 */       timer.schedule(new TimerTask(this)
/*     */           {
/*     */             public void run() {
/* 101 */               Runtime.getRuntime().halt(1);
/*     */             }
/*     */           },  10000L);
/*     */       
/* 105 */       System.exit(1);
/* 106 */     } catch (Throwable ignored) {
/* 107 */       Runtime.getRuntime().halt(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dedicated\ServerWatchdog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
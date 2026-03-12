/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.network.PacketProcessor;
/*    */ import net.minecraft.server.RunningOnDifferentThreadException;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public class PacketUtils
/*    */ {
/* 15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */   
/* 18 */   public static <T extends net.minecraft.network.PacketListener> void ensureRunningOnSameThread(Packet<T> packet, T listener, ServerLevel level) throws RunningOnDifferentThreadException { ensureRunningOnSameThread(packet, listener, level.getServer().packetProcessor()); }
/*    */ 
/*    */   
/*    */   public static <T extends net.minecraft.network.PacketListener> void ensureRunningOnSameThread(Packet<T> packet, T listener, PacketProcessor packetProcessor) throws RunningOnDifferentThreadException {
/* 22 */     if (!packetProcessor.isSameThread()) {
/* 23 */       packetProcessor.scheduleIfPossible(listener, packet);
/* 24 */       throw RunningOnDifferentThreadException.RUNNING_ON_DIFFERENT_THREAD;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static <T extends net.minecraft.network.PacketListener> ReportedException makeReportedException(Exception cause, Packet<T> packet, T listener) {
/* 29 */     if (cause instanceof ReportedException) { ReportedException re = (ReportedException)cause;
/* 30 */       fillCrashReport(re.getReport(), listener, packet);
/* 31 */       return re; }
/*    */     
/* 33 */     CrashReport report = CrashReport.forThrowable(cause, "Main thread packet handler");
/* 34 */     fillCrashReport(report, listener, packet);
/* 35 */     return new ReportedException(report);
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T extends net.minecraft.network.PacketListener> void fillCrashReport(CrashReport report, T listener, Packet<T> packet) {
/* 40 */     if (packet != null) {
/* 41 */       CrashReportCategory details = report.addCategory("Incoming Packet");
/* 42 */       details.setDetail("Type", () -> packet.type().toString());
/* 43 */       details.setDetail("Is Terminal", () -> Boolean.toString(packet.isTerminal()));
/* 44 */       details.setDetail("Is Skippable", () -> Boolean.toString(packet.isSkippable()));
/*    */     } 
/*    */     
/* 47 */     listener.fillCrashReport(report);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\PacketUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.util.profiling.jfr;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.OpenOption;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.StandardOpenOption;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.server.Bootstrap;
/*    */ import net.minecraft.util.profiling.jfr.parse.JfrStatsParser;
/*    */ import net.minecraft.util.profiling.jfr.parse.JfrStatsResult;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class SummaryReporter
/*    */ {
/* 18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Runnable onDeregistration;
/*    */ 
/*    */   
/* 23 */   protected SummaryReporter(Runnable onDeregistration) { this.onDeregistration = onDeregistration; }
/*    */   
/*    */   public void recordingStopped(Path result) {
/*    */     JfrStatsResult statsResult;
/* 27 */     if (result == null) {
/*    */       return;
/*    */     }
/* 30 */     this.onDeregistration.run();
/*    */     
/* 32 */     infoWithFallback(() -> "Dumped flight recorder profiling to " + String.valueOf(result));
/*    */ 
/*    */     
/*    */     try {
/* 36 */       statsResult = JfrStatsParser.parse(result);
/* 37 */     } catch (Throwable t) {
/* 38 */       warnWithFallback(() -> "Failed to parse JFR recording", t);
/*    */       
/*    */       return;
/*    */     } 
/*    */     try {
/* 43 */       Objects.requireNonNull(statsResult); infoWithFallback(statsResult::asJson);
/* 44 */       Path jsonReport = result.resolveSibling("jfr-report-" + StringUtils.substringBefore(result.getFileName().toString(), ".jfr") + ".json");
/* 45 */       Files.writeString(jsonReport, statsResult.asJson(), new OpenOption[] { StandardOpenOption.CREATE });
/* 46 */       infoWithFallback(() -> "Dumped recording summary to " + String.valueOf(jsonReport));
/* 47 */     } catch (Throwable t) {
/* 48 */       warnWithFallback(() -> "Failed to output JFR report", t);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void infoWithFallback(Supplier<String> message) {
/* 58 */     if (LogUtils.isLoggerActive()) {
/* 59 */       LOGGER.info((String)message.get());
/*    */     } else {
/* 61 */       Bootstrap.realStdoutPrintln((String)message.get());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void warnWithFallback(Supplier<String> message, Throwable t) {
/* 72 */     if (LogUtils.isLoggerActive()) {
/* 73 */       LOGGER.warn((String)message.get(), t);
/*    */     } else {
/* 75 */       Bootstrap.realStdoutPrintln((String)message.get());
/* 76 */       t.printStackTrace(Bootstrap.STDOUT);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\SummaryReporter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
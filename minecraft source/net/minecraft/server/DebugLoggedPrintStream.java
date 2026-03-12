/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.OutputStream;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DebugLoggedPrintStream
/*    */   extends LoggedPrintStream
/*    */ {
/* 10 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */   
/* 13 */   public DebugLoggedPrintStream(String name, OutputStream out) { super(name, out); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void logLine(String out) {
/* 18 */     StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
/* 19 */     StackTraceElement stackTraceElement = stackTrace[Math.min(3, stackTrace.length)];
/* 20 */     LOGGER.info("[{}]@.({}:{}): {}", new Object[] { this.name, stackTraceElement.getFileName(), Integer.valueOf(stackTraceElement.getLineNumber()), out });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\DebugLoggedPrintStream.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class LoggedPrintStream
/*    */   extends PrintStream
/*    */ {
/* 12 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   protected final String name;
/*    */   
/*    */   public LoggedPrintStream(String name, OutputStream out) {
/* 17 */     super(out, false, StandardCharsets.UTF_8);
/* 18 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public void println(String string) { logLine(string); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public void println(Object object) { logLine(String.valueOf(object)); }
/*    */ 
/*    */ 
/*    */   
/* 32 */   protected void logLine(String out) { LOGGER.info("[{}]: {}", this.name, out); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\LoggedPrintStream.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
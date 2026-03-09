/*    */ package net.minecraft;
/*    */ 
/*    */ public class ReportedException
/*    */   extends RuntimeException {
/*    */   private final CrashReport report;
/*    */   
/*  7 */   public ReportedException(CrashReport report) { this.report = report; }
/*    */ 
/*    */ 
/*    */   
/* 11 */   public CrashReport getReport() { return this.report; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public Throwable getCause() { return this.report.getException(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public String getMessage() { return this.report.getTitle(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\ReportedException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
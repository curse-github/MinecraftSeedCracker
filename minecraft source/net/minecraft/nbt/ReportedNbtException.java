/*   */ package net.minecraft.nbt;
/*   */ 
/*   */ import net.minecraft.CrashReport;
/*   */ import net.minecraft.ReportedException;
/*   */ 
/*   */ public class ReportedNbtException
/*   */   extends ReportedException {
/* 8 */   public ReportedNbtException(CrashReport report) { super(report); }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\ReportedNbtException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
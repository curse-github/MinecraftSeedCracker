/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ 
/*    */ public interface ProfileResults
/*    */ {
/*    */   public static final char PATH_SEPARATOR = '\036';
/*    */   
/*    */   List<ResultField> getTimes(String paramString);
/*    */   
/*    */   boolean saveResults(Path paramPath);
/*    */   
/*    */   long getStartTimeNano();
/*    */   
/*    */   int getStartTimeTicks();
/*    */   
/*    */   long getEndTimeNano();
/*    */   
/*    */   int getEndTimeTicks();
/*    */   
/* 22 */   default long getNanoDuration() { return getEndTimeNano() - getStartTimeNano(); }
/*    */ 
/*    */ 
/*    */   
/* 26 */   default int getTickDuration() { return getEndTimeTicks() - getStartTimeTicks(); }
/*    */ 
/*    */   
/*    */   String getProfilerResults();
/*    */ 
/*    */   
/* 32 */   static String demanglePath(String path) { return path.replace('\036', '.'); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\ProfileResults.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
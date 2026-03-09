/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public class EmptyProfileResults implements ProfileResults {
/*  8 */   public static final EmptyProfileResults EMPTY = new EmptyProfileResults();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   public List<ResultField> getTimes(String path) { return Collections.emptyList(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public boolean saveResults(Path file) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public long getStartTimeNano() { return 0L; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public int getStartTimeTicks() { return 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public long getEndTimeNano() { return 0L; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public int getEndTimeTicks() { return 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public String getProfilerResults() { return ""; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\EmptyProfileResults.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
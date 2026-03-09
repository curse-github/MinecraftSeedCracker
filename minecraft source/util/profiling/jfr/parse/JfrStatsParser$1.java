/*    */ package net.minecraft.util.profiling.jfr.parse;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.UncheckedIOException;
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ import jdk.jfr.consumer.RecordedEvent;
/*    */ import jdk.jfr.consumer.RecordingFile;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements Iterator<RecordedEvent>
/*    */ {
/* 78 */   public boolean hasNext() { return recordingFile.hasMoreEvents(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public RecordedEvent next() {
/* 83 */     if (!hasNext()) {
/* 84 */       throw new NoSuchElementException();
/*    */     }
/*    */     try {
/* 87 */       return recordingFile.readEvent();
/* 88 */     } catch (IOException e) {
/* 89 */       throw new UncheckedIOException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\parse\JfrStatsParser$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
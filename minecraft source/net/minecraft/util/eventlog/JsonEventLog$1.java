/*    */ package net.minecraft.util.eventlog;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */   implements JsonEventLogReader<T>
/*    */ {
/*    */   public T next() throws IOException {
/*    */     try {
/* 61 */       JsonEventLog.this.channel.position(this.position);
/* 62 */       object = reader.next(); return (T)object;
/*    */     } finally {
/* 64 */       this.position = JsonEventLog.this.channel.position();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 70 */   public void close() throws IOException { JsonEventLog.this.releaseReference(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\eventlog\JsonEventLog$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
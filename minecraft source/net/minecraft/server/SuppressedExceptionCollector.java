/*    */ package net.minecraft.server;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.util.Queue;
/*    */ import net.minecraft.util.ArrayListDeque;
/*    */ 
/*    */ public class SuppressedExceptionCollector
/*    */ {
/*    */   private static final int LATEST_ENTRY_COUNT = 8;
/* 13 */   private final Queue<LongEntry> latestEntries = new ArrayListDeque();
/*    */   
/* 15 */   private final Object2IntLinkedOpenHashMap<ShortEntry> entryCounts = new Object2IntLinkedOpenHashMap();
/*    */ 
/*    */ 
/*    */   
/* 19 */   private static long currentTimeMs() { return System.currentTimeMillis(); }
/*    */ 
/*    */   
/*    */   public void addEntry(String location, Throwable throwable) {
/* 23 */     long now = currentTimeMs();
/* 24 */     String message = throwable.getMessage();
/*    */     
/* 26 */     this.latestEntries.add(new LongEntry(now, location, throwable.getClass(), message));
/* 27 */     while (this.latestEntries.size() > 8) {
/* 28 */       this.latestEntries.remove();
/*    */     }
/*    */     
/* 31 */     ShortEntry key = new ShortEntry(location, throwable.getClass());
/* 32 */     int currentValue = this.entryCounts.getInt(key);
/* 33 */     this.entryCounts.putAndMoveToFirst(key, currentValue + 1);
/*    */   }
/*    */   
/*    */   public String dump() {
/* 37 */     long current = currentTimeMs();
/* 38 */     StringBuilder result = new StringBuilder();
/* 39 */     if (!this.latestEntries.isEmpty()) {
/* 40 */       result.append("\n\t\tLatest entries:\n");
/* 41 */       for (LongEntry e : this.latestEntries) {
/* 42 */         result
/* 43 */           .append("\t\t\t")
/* 44 */           .append(e.location)
/* 45 */           .append(":")
/* 46 */           .append(e.cls)
/* 47 */           .append(": ")
/* 48 */           .append(e.message)
/* 49 */           .append(" (")
/* 50 */           .append(current - e.timestampMs)
/* 51 */           .append("ms ago)")
/* 52 */           .append("\n");
/*    */       }
/*    */     } 
/*    */     
/* 56 */     if (!this.entryCounts.isEmpty()) {
/* 57 */       if (result.isEmpty()) {
/* 58 */         result.append("\n");
/*    */       }
/* 60 */       result.append("\t\tEntry counts:\n");
/* 61 */       for (ObjectIterator objectIterator = Object2IntMaps.fastIterable(this.entryCounts).iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<ShortEntry> e = (Object2IntMap.Entry)objectIterator.next();
/* 62 */         result
/* 63 */           .append("\t\t\t")
/* 64 */           .append(((ShortEntry)e.getKey()).location)
/* 65 */           .append(":")
/* 66 */           .append(((ShortEntry)e.getKey()).cls)
/* 67 */           .append(" x ")
/* 68 */           .append(e.getIntValue())
/* 69 */           .append("\n"); }
/*    */     
/*    */     } 
/*    */     
/* 73 */     if (result.isEmpty()) {
/* 74 */       return "~~NONE~~";
/*    */     }
/* 76 */     return result.toString();
/*    */   }
/*    */   private static final class LongEntry extends Record { private final long timestampMs; private final String location; private final Class<? extends Throwable> cls; private final String message;
/* 79 */     private LongEntry(long timestampMs, String location, Class<? extends Throwable> cls, String message) { this.timestampMs = timestampMs; this.location = location; this.cls = cls; this.message = message; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/SuppressedExceptionCollector$LongEntry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #79	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 79 */       //   0	7	0	this	Lnet/minecraft/server/SuppressedExceptionCollector$LongEntry; } public long timestampMs() { return this.timestampMs; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/SuppressedExceptionCollector$LongEntry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #79	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/SuppressedExceptionCollector$LongEntry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/SuppressedExceptionCollector$LongEntry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #79	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/SuppressedExceptionCollector$LongEntry;
/* 79 */       //   0	8	1	o	Ljava/lang/Object; } public String location() { return this.location; } public Class<? extends Throwable> cls() { return this.cls; } public String message() { return this.message; } }
/*    */ 
/*    */   
/*    */   private static final class ShortEntry
/*    */     extends Record {
/*    */     private final String location;
/*    */     private final Class<? extends Throwable> cls;
/*    */     
/* 87 */     private ShortEntry(String location, Class<? extends Throwable> cls) { this.location = location; this.cls = cls; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/SuppressedExceptionCollector$ShortEntry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #87	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/SuppressedExceptionCollector$ShortEntry; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/SuppressedExceptionCollector$ShortEntry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #87	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/SuppressedExceptionCollector$ShortEntry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/SuppressedExceptionCollector$ShortEntry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #87	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/SuppressedExceptionCollector$ShortEntry;
/* 87 */       //   0	8	1	o	Ljava/lang/Object; } public String location() { return this.location; } public Class<? extends Throwable> cls() { return this.cls; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\SuppressedExceptionCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
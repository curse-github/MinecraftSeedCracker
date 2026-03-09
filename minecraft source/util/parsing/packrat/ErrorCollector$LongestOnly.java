/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.Util;
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
/*    */ public class LongestOnly<S>
/*    */   extends Object
/*    */   implements ErrorCollector<S>
/*    */ {
/*    */   private static class MutableErrorEntry<S>
/*    */     extends Object
/*    */   {
/* 31 */     private SuggestionSupplier<S> suggestions = SuggestionSupplier.empty();
/* 32 */     private Object reason = "empty";
/*    */   }
/*    */   
/* 35 */   private MutableErrorEntry<S>[] entries = new MutableErrorEntry[16];
/*    */   
/*    */   private int nextErrorEntry;
/*    */   
/* 39 */   private int lastCursor = -1;
/*    */   
/*    */   private void discardErrorsFromShorterParse(int cursor) {
/* 42 */     if (cursor > this.lastCursor) {
/* 43 */       this.lastCursor = cursor;
/* 44 */       this.nextErrorEntry = 0;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public void finish(int finalCursor) { discardErrorsFromShorterParse(finalCursor); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void store(int cursor, SuggestionSupplier<S> suggestions, Object reason) {
/* 55 */     discardErrorsFromShorterParse(cursor);
/*    */     
/* 57 */     if (cursor == this.lastCursor) {
/* 58 */       addErrorEntry(suggestions, reason);
/*    */     }
/*    */   }
/*    */   
/*    */   private void addErrorEntry(SuggestionSupplier<S> suggestions, Object reason) {
/* 63 */     int currentSize = this.entries.length;
/* 64 */     if (this.nextErrorEntry >= currentSize) {
/* 65 */       int newSize = Util.growByHalf(currentSize, this.nextErrorEntry + 1);
/* 66 */       MutableErrorEntry[] arrayOfMutableErrorEntry = new MutableErrorEntry[newSize];
/* 67 */       System.arraycopy(this.entries, 0, arrayOfMutableErrorEntry, 0, currentSize);
/* 68 */       this.entries = arrayOfMutableErrorEntry;
/*    */     } 
/*    */     
/* 71 */     int entryIndex = this.nextErrorEntry++;
/* 72 */     MutableErrorEntry<S> entry = this.entries[entryIndex];
/* 73 */     if (entry == null) {
/* 74 */       entry = new MutableErrorEntry<S>();
/* 75 */       this.entries[entryIndex] = entry;
/*    */     } 
/* 77 */     entry.suggestions = suggestions;
/* 78 */     entry.reason = reason;
/*    */   }
/*    */   
/*    */   public List<ErrorEntry<S>> entries() {
/* 82 */     int errorCount = this.nextErrorEntry;
/* 83 */     if (errorCount == 0) {
/* 84 */       return List.of();
/*    */     }
/* 86 */     List<ErrorEntry<S>> result = new ArrayList<ErrorEntry<S>>(errorCount);
/* 87 */     for (int i = 0; i < errorCount; i++) {
/* 88 */       MutableErrorEntry<S> entry = this.entries[i];
/* 89 */       result.add(new ErrorEntry(this.lastCursor, entry.suggestions, entry.reason));
/*    */     } 
/* 91 */     return result;
/*    */   }
/*    */ 
/*    */   
/* 95 */   public int cursor() { return this.lastCursor; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\ErrorCollector$LongestOnly.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
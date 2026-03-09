/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ public interface TextFilter
/*    */ {
/*  9 */   public static final TextFilter DUMMY = new TextFilter()
/*    */     {
/*    */       public CompletableFuture<FilteredText> processStreamMessage(String message) {
/* 12 */         return CompletableFuture.completedFuture(FilteredText.passThrough(message));
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 17 */       public CompletableFuture<List<FilteredText>> processMessageBundle(List<String> messages) { return CompletableFuture.completedFuture((List)messages.stream().map(FilteredText::passThrough).collect(ImmutableList.toImmutableList())); }
/*    */     };
/*    */   
/*    */   default void join() {}
/*    */   
/*    */   default void leave() {}
/*    */   
/*    */   CompletableFuture<FilteredText> processStreamMessage(String paramString);
/*    */   
/*    */   CompletableFuture<List<FilteredText>> processMessageBundle(List<String> paramList);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\TextFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
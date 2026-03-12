/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*    */ import java.util.ArrayDeque;
/*    */ import java.util.List;
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
/*    */ public class MessageSignatureCache
/*    */ {
/*    */   public static final int NOT_FOUND = -1;
/*    */   private static final int DEFAULT_CAPACITY = 128;
/*    */   private final MessageSignature[] entries;
/*    */   
/* 29 */   public MessageSignatureCache(int capacity) { this.entries = new MessageSignature[capacity]; }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static MessageSignatureCache createDefault() { return new MessageSignatureCache(128); }
/*    */ 
/*    */   
/*    */   public int pack(MessageSignature signature) {
/* 37 */     for (int i = 0; i < this.entries.length; i++) {
/* 38 */       if (signature.equals(this.entries[i])) {
/* 39 */         return i;
/*    */       }
/*    */     } 
/* 42 */     return -1;
/*    */   }
/*    */ 
/*    */   
/* 46 */   public MessageSignature unpack(int id) { return this.entries[id]; }
/*    */ 
/*    */   
/*    */   public void push(SignedMessageBody body, MessageSignature signature) {
/* 50 */     List<MessageSignature> lastSeen = body.lastSeen().entries();
/*    */     
/* 52 */     ArrayDeque<MessageSignature> queue = new ArrayDeque<MessageSignature>(lastSeen.size() + 1);
/* 53 */     queue.addAll(lastSeen);
/* 54 */     if (signature != null) {
/* 55 */       queue.add(signature);
/*    */     }
/*    */     
/* 58 */     push(queue);
/*    */   }
/*    */ 
/*    */   
/*    */   @VisibleForTesting
/* 63 */   void push(List<MessageSignature> entries) { push(new ArrayDeque(entries)); }
/*    */ 
/*    */   
/*    */   private void push(ArrayDeque<MessageSignature> queue) {
/* 67 */     ObjectOpenHashSet objectOpenHashSet = new ObjectOpenHashSet(queue);
/*    */     
/* 69 */     int i = 0;
/* 70 */     while (!queue.isEmpty() && i < this.entries.length) {
/* 71 */       MessageSignature entry = this.entries[i];
/* 72 */       this.entries[i] = (MessageSignature)queue.removeLast();
/* 73 */       if (entry != null && !objectOpenHashSet.contains(entry)) {
/* 74 */         queue.addFirst(entry);
/*    */       }
/* 76 */       i++;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\MessageSignatureCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
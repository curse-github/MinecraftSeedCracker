/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LastSeenMessagesValidator
/*    */ {
/*    */   private final int lastSeenCount;
/*    */   private final ObjectList<LastSeenTrackedEntry> trackedMessages;
/*    */   private MessageSignature lastPendingMessage;
/*    */   
/*    */   public LastSeenMessagesValidator(int lastSeenCount) {
/* 19 */     this.trackedMessages = new ObjectArrayList();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 24 */     this.lastSeenCount = lastSeenCount;
/* 25 */     for (int i = 0; i < lastSeenCount; i++) {
/* 26 */       this.trackedMessages.add(null);
/*    */     }
/*    */   }
/*    */   
/*    */   public void addPending(MessageSignature message) {
/* 31 */     if (!message.equals(this.lastPendingMessage)) {
/* 32 */       this.trackedMessages.add(new LastSeenTrackedEntry(message, true));
/* 33 */       this.lastPendingMessage = message;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 38 */   public int trackedMessagesCount() { return this.trackedMessages.size(); }
/*    */ 
/*    */   
/*    */   public void applyOffset(int offset) {
/* 42 */     int maxOffset = this.trackedMessages.size() - this.lastSeenCount;
/* 43 */     if (offset < 0 || offset > maxOffset) {
/* 44 */       throw new ValidationException("Advanced last seen window by " + offset + " messages, but expected at most " + maxOffset);
/*    */     }
/* 46 */     this.trackedMessages.removeElements(0, offset);
/*    */   }
/*    */   
/*    */   public LastSeenMessages applyUpdate(LastSeenMessages.Update update) throws ValidationException {
/* 50 */     applyOffset(update.offset());
/*    */     
/* 52 */     ObjectArrayList objectArrayList = new ObjectArrayList(update.acknowledged().cardinality());
/* 53 */     if (update.acknowledged().length() > this.lastSeenCount) {
/* 54 */       throw new ValidationException("Last seen update contained " + update.acknowledged().length() + " messages, but maximum window size is " + this.lastSeenCount);
/*    */     }
/*    */     
/* 57 */     for (int i = 0; i < this.lastSeenCount; i++) {
/* 58 */       boolean acknowledged = update.acknowledged().get(i);
/* 59 */       LastSeenTrackedEntry message = (LastSeenTrackedEntry)this.trackedMessages.get(i);
/* 60 */       if (acknowledged) {
/* 61 */         if (message == null) {
/* 62 */           throw new ValidationException("Last seen update acknowledged unknown or previously ignored message at index " + i);
/*    */         }
/* 64 */         this.trackedMessages.set(i, message.acknowledge());
/* 65 */         objectArrayList.add(message.signature());
/*    */       } else {
/* 67 */         if (message != null && !message.pending()) {
/* 68 */           throw new ValidationException("Last seen update ignored previously acknowledged message at index " + i + " and signature " + String.valueOf(message.signature()));
/*    */         }
/* 70 */         this.trackedMessages.set(i, null);
/*    */       } 
/*    */     } 
/*    */     
/* 74 */     LastSeenMessages lastSeen = new LastSeenMessages(objectArrayList);
/* 75 */     if (!update.verifyChecksum(lastSeen)) {
/* 76 */       throw new ValidationException("Checksum mismatch on last seen update: the client and server must have desynced");
/*    */     }
/*    */     
/* 79 */     return lastSeen;
/*    */   }
/*    */   
/*    */   public static class ValidationException
/*    */     extends Exception {
/* 84 */     public ValidationException(String message) { super(message); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\LastSeenMessagesValidator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
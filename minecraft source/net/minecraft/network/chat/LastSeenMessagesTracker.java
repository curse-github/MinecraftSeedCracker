/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.BitSet;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LastSeenMessagesTracker
/*    */ {
/*    */   private final LastSeenTrackedEntry[] trackedMessages;
/*    */   private int tail;
/*    */   private int offset;
/*    */   private MessageSignature lastTrackedMessage;
/*    */   
/* 18 */   public LastSeenMessagesTracker(int lastSeenCount) { this.trackedMessages = new LastSeenTrackedEntry[lastSeenCount]; }
/*    */ 
/*    */   
/*    */   public boolean addPending(MessageSignature message, boolean wasShown) {
/* 22 */     if (Objects.equals(message, this.lastTrackedMessage)) {
/* 23 */       return false;
/*    */     }
/* 25 */     this.lastTrackedMessage = message;
/* 26 */     addEntry(wasShown ? new LastSeenTrackedEntry(message, true) : null);
/* 27 */     return true;
/*    */   }
/*    */   
/*    */   private void addEntry(LastSeenTrackedEntry entry) {
/* 31 */     int index = this.tail;
/* 32 */     this.tail = (index + 1) % this.trackedMessages.length;
/* 33 */     this.offset++;
/* 34 */     this.trackedMessages[index] = entry;
/*    */   }
/*    */   
/*    */   public void ignorePending(MessageSignature pendingMessage) {
/* 38 */     for (int i = 0; i < this.trackedMessages.length; i++) {
/* 39 */       LastSeenTrackedEntry entry = this.trackedMessages[i];
/* 40 */       if (entry != null && entry.pending() && pendingMessage.equals(entry.signature())) {
/* 41 */         this.trackedMessages[i] = null;
/*    */         break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public int getAndClearOffset() {
/* 48 */     int originalOffset = this.offset;
/* 49 */     this.offset = 0;
/* 50 */     return originalOffset;
/*    */   }
/*    */   
/*    */   public Update generateAndApplyUpdate() {
/* 54 */     int offset = getAndClearOffset();
/*    */     
/* 56 */     BitSet acknowledged = new BitSet(this.trackedMessages.length);
/* 57 */     ObjectArrayList objectArrayList = new ObjectArrayList(this.trackedMessages.length);
/*    */     
/* 59 */     for (int i = 0; i < this.trackedMessages.length; i++) {
/* 60 */       int index = (this.tail + i) % this.trackedMessages.length;
/* 61 */       LastSeenTrackedEntry message = this.trackedMessages[index];
/* 62 */       if (message != null) {
/* 63 */         acknowledged.set(i, true);
/* 64 */         objectArrayList.add(message.signature());
/* 65 */         this.trackedMessages[index] = message.acknowledge();
/*    */       } 
/*    */     } 
/*    */     
/* 69 */     LastSeenMessages lastSeen = new LastSeenMessages(objectArrayList);
/* 70 */     LastSeenMessages.Update update = new LastSeenMessages.Update(offset, acknowledged, lastSeen.computeChecksum());
/* 71 */     return new Update(lastSeen, update);
/*    */   }
/*    */ 
/*    */   
/* 75 */   public int offset() { return this.offset; }
/*    */   public static final class Update extends Record { private final LastSeenMessages lastSeen; private final LastSeenMessages.Update update;
/*    */     
/* 78 */     public Update(LastSeenMessages lastSeen, LastSeenMessages.Update update) { this.lastSeen = lastSeen; this.update = update; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/LastSeenMessagesTracker$Update;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #78	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 78 */       //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenMessagesTracker$Update; } public LastSeenMessages lastSeen() { return this.lastSeen; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/LastSeenMessagesTracker$Update;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #78	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenMessagesTracker$Update; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/LastSeenMessagesTracker$Update;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #78	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/LastSeenMessagesTracker$Update;
/* 78 */       //   0	8	1	o	Ljava/lang/Object; } public LastSeenMessages.Update update() { return this.update; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\LastSeenMessagesTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.FriendlyByteBuf;
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
/*    */ public final class Packed
/*    */   extends Record
/*    */ {
/*    */   private final List<MessageSignature.Packed> entries;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/LastSeenMessages$Packed;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenMessages$Packed; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/LastSeenMessages$Packed;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenMessages$Packed; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/LastSeenMessages$Packed;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/LastSeenMessages$Packed;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 45 */   public Packed(List<MessageSignature.Packed> entries) { this.entries = entries; } public List<MessageSignature.Packed> entries() { return this.entries; }
/* 46 */   public static final Packed EMPTY = new Packed(List.of());
/*    */ 
/*    */   
/* 49 */   public Packed(FriendlyByteBuf input) { this((List)input.readCollection(FriendlyByteBuf.limitValue(ArrayList::new, 20), MessageSignature.Packed::read)); }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public void write(FriendlyByteBuf output) { output.writeCollection(this.entries, MessageSignature.Packed::write); }
/*    */ 
/*    */   
/*    */   public Optional<LastSeenMessages> unpack(MessageSignatureCache cache) {
/* 57 */     List<MessageSignature> unpacked = new ArrayList<MessageSignature>(this.entries.size());
/* 58 */     for (MessageSignature.Packed packed : this.entries) {
/* 59 */       Optional<MessageSignature> entry = packed.unpack(cache);
/* 60 */       if (entry.isEmpty()) {
/* 61 */         return Optional.empty();
/*    */       }
/* 63 */       unpacked.add((MessageSignature)entry.get());
/*    */     } 
/* 65 */     return Optional.of(new LastSeenMessages(unpacked));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\LastSeenMessages$Packed.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
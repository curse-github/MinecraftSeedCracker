/*    */ package net.minecraft.network.chat;
/*    */ import java.security.SignatureException;
/*    */ import java.util.BitSet;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.util.SignatureUpdater;
/*    */ 
/*    */ public final class LastSeenMessages extends Record {
/*    */   private final List<MessageSignature> entries;
/*    */   
/* 14 */   public LastSeenMessages(List<MessageSignature> entries) { this.entries = entries; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/LastSeenMessages;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenMessages; } public List<MessageSignature> entries() { return this.entries; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/LastSeenMessages;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenMessages; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/LastSeenMessages;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/LastSeenMessages;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 15 */   public static final Codec<LastSeenMessages> CODEC = MessageSignature.CODEC.listOf().xmap(LastSeenMessages::new, LastSeenMessages::entries);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static LastSeenMessages EMPTY = new LastSeenMessages(List.of());
/*    */   
/*    */   public static final int LAST_SEEN_MESSAGES_MAX_LENGTH = 20;
/*    */   
/*    */   public void updateSignature(SignatureUpdater.Output output) throws SignatureException {
/* 25 */     output.update(Ints.toByteArray(this.entries.size()));
/* 26 */     for (MessageSignature entry : this.entries) {
/* 27 */       output.update(entry.bytes());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 32 */   public Packed pack(MessageSignatureCache cache) { return new Packed(this.entries.stream().map(entry -> entry.pack(cache)).toList()); }
/*    */ 
/*    */   
/*    */   public byte computeChecksum() {
/* 36 */     int checksum = 1;
/* 37 */     for (MessageSignature entry : this.entries) {
/* 38 */       checksum = 31 * checksum + entry.checksum();
/*    */     }
/* 40 */     byte checksumByte = (byte)checksum;
/*    */     
/* 42 */     return (checksumByte == 0) ? 1 : checksumByte;
/*    */   }
/*    */   public static final class Packed extends Record { private final List<MessageSignature.Packed> entries;
/* 45 */     public Packed(List<MessageSignature.Packed> entries) { this.entries = entries; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/LastSeenMessages$Packed;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenMessages$Packed; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/LastSeenMessages$Packed;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenMessages$Packed; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/LastSeenMessages$Packed;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/LastSeenMessages$Packed;
/* 45 */       //   0	8	1	o	Ljava/lang/Object; } public List<MessageSignature.Packed> entries() { return this.entries; }
/* 46 */     public static final Packed EMPTY = new Packed(List.of());
/*    */ 
/*    */     
/* 49 */     public Packed(FriendlyByteBuf input) { this((List)input.readCollection(FriendlyByteBuf.limitValue(ArrayList::new, 20), MessageSignature.Packed::read)); }
/*    */ 
/*    */ 
/*    */     
/* 53 */     public void write(FriendlyByteBuf output) { output.writeCollection(this.entries, MessageSignature.Packed::write); }
/*    */ 
/*    */     
/*    */     public Optional<LastSeenMessages> unpack(MessageSignatureCache cache) {
/* 57 */       List<MessageSignature> unpacked = new ArrayList<MessageSignature>(this.entries.size());
/* 58 */       for (MessageSignature.Packed packed : this.entries) {
/* 59 */         Optional<MessageSignature> entry = packed.unpack(cache);
/* 60 */         if (entry.isEmpty()) {
/* 61 */           return Optional.empty();
/*    */         }
/* 63 */         unpacked.add((MessageSignature)entry.get());
/*    */       } 
/* 65 */       return Optional.of(new LastSeenMessages(unpacked));
/*    */     } }
/*    */   public static final class Update extends Record { private final int offset; private final BitSet acknowledged; private final byte checksum; public static final byte IGNORE_CHECKSUM = 0;
/*    */     
/* 69 */     public Update(int offset, BitSet acknowledged, byte checksum) { this.offset = offset; this.acknowledged = acknowledged; this.checksum = checksum; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/LastSeenMessages$Update;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #69	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenMessages$Update; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/LastSeenMessages$Update;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #69	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenMessages$Update; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/LastSeenMessages$Update;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #69	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/LastSeenMessages$Update;
/* 69 */       //   0	8	1	o	Ljava/lang/Object; } public int offset() { return this.offset; } public BitSet acknowledged() { return this.acknowledged; } public byte checksum() { return this.checksum; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 74 */     public Update(FriendlyByteBuf input) { this(input.readVarInt(), input.readFixedBitSet(20), input.readByte()); }
/*    */ 
/*    */     
/*    */     public void write(FriendlyByteBuf output) {
/* 78 */       output.writeVarInt(this.offset);
/* 79 */       output.writeFixedBitSet(this.acknowledged, 20);
/* 80 */       output.writeByte(this.checksum);
/*    */     }
/*    */ 
/*    */     
/* 84 */     public boolean verifyChecksum(LastSeenMessages lastSeen) { return (this.checksum == 0 || this.checksum == lastSeen.computeChecksum()); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\LastSeenMessages.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
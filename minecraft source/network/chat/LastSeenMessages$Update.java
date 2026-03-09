/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.BitSet;
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
/*    */ public final class Update
/*    */   extends Record
/*    */ {
/*    */   private final int offset;
/*    */   private final BitSet acknowledged;
/*    */   private final byte checksum;
/*    */   public static final byte IGNORE_CHECKSUM = 0;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/LastSeenMessages$Update;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #69	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenMessages$Update; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/LastSeenMessages$Update;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #69	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenMessages$Update; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/LastSeenMessages$Update;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #69	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/LastSeenMessages$Update;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 69 */   public Update(int offset, BitSet acknowledged, byte checksum) { this.offset = offset; this.acknowledged = acknowledged; this.checksum = checksum; } public int offset() { return this.offset; } public BitSet acknowledged() { return this.acknowledged; } public byte checksum() { return this.checksum; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   public Update(FriendlyByteBuf input) { this(input.readVarInt(), input.readFixedBitSet(20), input.readByte()); }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf output) {
/* 78 */     output.writeVarInt(this.offset);
/* 79 */     output.writeFixedBitSet(this.acknowledged, 20);
/* 80 */     output.writeByte(this.checksum);
/*    */   }
/*    */ 
/*    */   
/* 84 */   public boolean verifyChecksum(LastSeenMessages lastSeen) { return (this.checksum == 0 || this.checksum == lastSeen.computeChecksum()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\LastSeenMessages$Update.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Packed
/*     */   extends Record
/*     */ {
/*     */   private final int id;
/*     */   private final MessageSignature fullSignature;
/*     */   public static final int FULL_SIGNATURE = -1;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/MessageSignature$Packed;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #79	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/MessageSignature$Packed; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/MessageSignature$Packed;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #79	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/MessageSignature$Packed; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/MessageSignature$Packed;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #79	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/chat/MessageSignature$Packed;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*  79 */   public Packed(int id, MessageSignature fullSignature) { this.id = id; this.fullSignature = fullSignature; } public int id() { return this.id; } public MessageSignature fullSignature() { return this.fullSignature; }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public Packed(MessageSignature signature) { this(-1, signature); }
/*     */ 
/*     */ 
/*     */   
/*  87 */   public Packed(int id) { this(id, null); }
/*     */ 
/*     */   
/*     */   public static Packed read(FriendlyByteBuf input) {
/*  91 */     int id = input.readVarInt() - 1;
/*  92 */     if (id == -1) {
/*  93 */       return new Packed(MessageSignature.read(input));
/*     */     }
/*  95 */     return new Packed(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void write(FriendlyByteBuf output, Packed packed) {
/* 100 */     output.writeVarInt(packed.id() + 1);
/* 101 */     if (packed.fullSignature() != null) {
/* 102 */       MessageSignature.write(output, packed.fullSignature());
/*     */     }
/*     */   }
/*     */   
/*     */   public Optional<MessageSignature> unpack(MessageSignatureCache cache) {
/* 107 */     if (this.fullSignature != null) {
/* 108 */       return Optional.of(this.fullSignature);
/*     */     }
/* 110 */     return Optional.ofNullable(cache.unpack(this.id));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\MessageSignature$Packed.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
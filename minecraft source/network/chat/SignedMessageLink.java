/*    */ package net.minecraft.network.chat;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.security.SignatureException;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.SignatureUpdater;
/*    */ 
/*    */ public final class SignedMessageLink extends Record {
/*    */   private final int index;
/*    */   private final UUID sender;
/*    */   private final UUID sessionId;
/*    */   
/* 15 */   public SignedMessageLink(int index, UUID sender, UUID sessionId) { this.index = index; this.sender = sender; this.sessionId = sessionId; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/SignedMessageLink;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/network/chat/SignedMessageLink; } public int index() { return this.index; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/SignedMessageLink;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignedMessageLink; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/SignedMessageLink;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/SignedMessageLink;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public UUID sender() { return this.sender; } public UUID sessionId() { return this.sessionId; }
/* 16 */   public static final Codec<SignedMessageLink> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.NON_NEGATIVE_INT
/* 17 */         .fieldOf("index").forGetter(SignedMessageLink::index), UUIDUtil.CODEC
/* 18 */         .fieldOf("sender").forGetter(SignedMessageLink::sender), UUIDUtil.CODEC
/* 19 */         .fieldOf("session_id").forGetter(SignedMessageLink::sessionId))
/* 20 */       .apply(i, SignedMessageLink::new));
/*    */ 
/*    */   
/* 23 */   public static SignedMessageLink unsigned(UUID sender) { return root(sender, Util.NIL_UUID); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static SignedMessageLink root(UUID sender, UUID sessionId) { return new SignedMessageLink(0, sender, sessionId); }
/*    */ 
/*    */   
/*    */   public void updateSignature(SignatureUpdater.Output output) throws SignatureException {
/* 31 */     output.update(UUIDUtil.uuidToByteArray(this.sender));
/* 32 */     output.update(UUIDUtil.uuidToByteArray(this.sessionId));
/* 33 */     output.update(Ints.toByteArray(this.index));
/*    */   }
/*    */ 
/*    */   
/* 37 */   public boolean isDescendantOf(SignedMessageLink link) { return (this.index > link.index() && this.sender.equals(link.sender()) && this.sessionId.equals(link.sessionId())); }
/*    */ 
/*    */   
/*    */   public SignedMessageLink advance() {
/* 41 */     if (this.index == Integer.MAX_VALUE) {
/* 42 */       return null;
/*    */     }
/* 44 */     return new SignedMessageLink(this.index + 1, this.sender, this.sessionId);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\SignedMessageLink.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
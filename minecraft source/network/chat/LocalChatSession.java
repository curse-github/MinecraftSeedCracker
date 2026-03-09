/*    */ package net.minecraft.network.chat;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.world.entity.player.ProfileKeyPair;
/*    */ 
/*    */ public final class LocalChatSession extends Record {
/*    */   private final UUID sessionId;
/*    */   private final ProfileKeyPair keyPair;
/*    */   
/*  9 */   public LocalChatSession(UUID sessionId, ProfileKeyPair keyPair) { this.sessionId = sessionId; this.keyPair = keyPair; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/LocalChatSession;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/chat/LocalChatSession; } public UUID sessionId() { return this.sessionId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/LocalChatSession;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/LocalChatSession; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/LocalChatSession;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/LocalChatSession;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public ProfileKeyPair keyPair() { return this.keyPair; }
/*    */   
/* 11 */   public static LocalChatSession create(ProfileKeyPair keyPair) { return new LocalChatSession(UUID.randomUUID(), keyPair); }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public SignedMessageChain.Encoder createMessageEncoder(UUID profileId) { return (new SignedMessageChain(profileId, this.sessionId)).encoder(Signer.from(this.keyPair.privateKey(), "SHA256withRSA")); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public RemoteChatSession asRemote() { return new RemoteChatSession(this.sessionId, this.keyPair.publicKey()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\LocalChatSession.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
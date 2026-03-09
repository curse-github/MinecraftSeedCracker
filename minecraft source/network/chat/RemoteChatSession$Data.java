/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.util.SignatureValidator;
/*    */ import net.minecraft.world.entity.player.ProfilePublicKey;
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
/*    */ public final class Data
/*    */   extends Record
/*    */ {
/*    */   private final UUID sessionId;
/*    */   private final ProfilePublicKey.Data profilePublicKey;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/RemoteChatSession$Data;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/RemoteChatSession$Data; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/RemoteChatSession$Data;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/RemoteChatSession$Data; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/RemoteChatSession$Data;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/RemoteChatSession$Data;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 31 */   public Data(UUID sessionId, ProfilePublicKey.Data profilePublicKey) { this.sessionId = sessionId; this.profilePublicKey = profilePublicKey; } public UUID sessionId() { return this.sessionId; } public ProfilePublicKey.Data profilePublicKey() { return this.profilePublicKey; }
/*    */   
/* 33 */   public static Data read(FriendlyByteBuf input) { return new Data(input.readUUID(), new ProfilePublicKey.Data(input)); }
/*    */ 
/*    */   
/*    */   public static void write(FriendlyByteBuf output, Data data) {
/* 37 */     output.writeUUID(data.sessionId);
/* 38 */     data.profilePublicKey.write(output);
/*    */   }
/*    */ 
/*    */   
/* 42 */   public RemoteChatSession validate(GameProfile profile, SignatureValidator serviceSignatureValidator) throws ProfilePublicKey.ValidationException { return new RemoteChatSession(this.sessionId, ProfilePublicKey.createValidated(serviceSignatureValidator, profile.id(), this.profilePublicKey)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\RemoteChatSession$Data.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
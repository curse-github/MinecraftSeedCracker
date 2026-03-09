/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import java.util.function.BooleanSupplier;
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
/*    */ @FunctionalInterface
/*    */ public interface Decoder
/*    */ {
/*    */   static Decoder unsigned(UUID profileId, BooleanSupplier enforcesSecureChat) {
/* 93 */     return (signature, body) -> {
/* 94 */         if (enforcesSecureChat.getAsBoolean()) {
/* 95 */           throw new SignedMessageChain.DecodeException(SignedMessageChain.DecodeException.MISSING_PROFILE_KEY);
/*    */         }
/* 97 */         return PlayerChatMessage.unsigned(profileId, body.content());
/*    */       };
/*    */   }
/*    */   
/*    */   default void setChainBroken() {}
/*    */   
/*    */   PlayerChatMessage unpack(MessageSignature paramMessageSignature, SignedMessageBody paramSignedMessageBody) throws SignedMessageChain.DecodeException;
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\SignedMessageChain$Decoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
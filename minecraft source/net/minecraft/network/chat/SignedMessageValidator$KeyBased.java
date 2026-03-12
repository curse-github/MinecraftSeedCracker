/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.function.BooleanSupplier;
/*    */ import net.minecraft.util.SignatureValidator;
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
/*    */ public class KeyBased
/*    */   implements SignedMessageValidator
/*    */ {
/*    */   private final SignatureValidator validator;
/*    */   private final BooleanSupplier expired;
/*    */   private PlayerChatMessage lastMessage;
/*    */   private boolean isChainValid;
/*    */   
/*    */   public KeyBased(SignatureValidator validator, BooleanSupplier expired) {
/* 31 */     this.isChainValid = true;
/*    */ 
/*    */     
/* 34 */     this.validator = validator;
/* 35 */     this.expired = expired;
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean validateChain(PlayerChatMessage message) {
/* 40 */     if (message.equals(this.lastMessage)) {
/* 41 */       return true;
/*    */     }
/*    */     
/* 44 */     if (this.lastMessage != null && !message.link().isDescendantOf(this.lastMessage.link())) {
/* 45 */       LOGGER.error("Received out-of-order chat message from {}: expected index > {} for session {}, but was {} for session {}", new Object[] { message.sender(), Integer.valueOf(this.lastMessage.link().index()), this.lastMessage.link().sessionId(), Integer.valueOf(message.link().index()), message.link().sessionId() });
/* 46 */       return false;
/*    */     } 
/*    */     
/* 49 */     return true;
/*    */   }
/*    */   
/*    */   private boolean validate(PlayerChatMessage message) {
/* 53 */     if (this.expired.getAsBoolean()) {
/* 54 */       LOGGER.error("Received message with expired profile public key from {} with session {}", message.sender(), message.link().sessionId());
/* 55 */       return false;
/*    */     } 
/* 57 */     if (!message.verify(this.validator)) {
/* 58 */       LOGGER.error("Received message with invalid signature (is the session wrong, or signature cache out of sync?): {}", PlayerChatMessage.describeSigned(message));
/* 59 */       return false;
/*    */     } 
/* 61 */     return validateChain(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlayerChatMessage updateAndValidate(PlayerChatMessage message) {
/* 66 */     this.isChainValid = (this.isChainValid && validate(message));
/* 67 */     if (!this.isChainValid) {
/* 68 */       return null;
/*    */     }
/* 70 */     this.lastMessage = message;
/* 71 */     return message;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\SignedMessageValidator$KeyBased.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
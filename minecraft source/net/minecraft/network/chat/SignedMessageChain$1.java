/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.time.Instant;
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
/*    */ class null
/*    */   implements SignedMessageChain.Decoder
/*    */ {
/*    */   public PlayerChatMessage unpack(MessageSignature signature, SignedMessageBody body) throws SignedMessageChain.DecodeException {
/* 40 */     if (signature == null) {
/* 41 */       throw new SignedMessageChain.DecodeException(SignedMessageChain.DecodeException.MISSING_PROFILE_KEY);
/*    */     }
/* 43 */     if (profilePublicKey.data().hasExpired()) {
/* 44 */       throw new SignedMessageChain.DecodeException(SignedMessageChain.DecodeException.EXPIRED_PROFILE_KEY);
/*    */     }
/*    */     
/* 47 */     SignedMessageLink link = SignedMessageChain.this.nextLink;
/* 48 */     if (link == null) {
/* 49 */       throw new SignedMessageChain.DecodeException(SignedMessageChain.DecodeException.CHAIN_BROKEN);
/*    */     }
/*    */ 
/*    */     
/* 53 */     if (body.timeStamp().isBefore(SignedMessageChain.this.lastTimeStamp)) {
/* 54 */       setChainBroken();
/* 55 */       throw new SignedMessageChain.DecodeException(SignedMessageChain.DecodeException.OUT_OF_ORDER_CHAT);
/*    */     } 
/* 57 */     SignedMessageChain.this.lastTimeStamp = body.timeStamp();
/*    */     
/* 59 */     PlayerChatMessage unpacked = new PlayerChatMessage(link, signature, body, null, FilterMask.PASS_THROUGH);
/* 60 */     if (!unpacked.verify(signatureValidator)) {
/*    */       
/* 62 */       setChainBroken();
/* 63 */       throw new SignedMessageChain.DecodeException(SignedMessageChain.DecodeException.INVALID_SIGNATURE);
/*    */     } 
/*    */     
/* 66 */     if (unpacked.hasExpiredServer(Instant.now())) {
/* 67 */       SignedMessageChain.LOGGER.warn("Received expired chat: '{}'. Is the client/server system time unsynchronized?", body.content());
/*    */     }
/*    */ 
/*    */     
/* 71 */     SignedMessageChain.this.nextLink = link.advance();
/*    */     
/* 73 */     return unpacked;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 78 */   public void setChainBroken() { SignedMessageChain.this.nextLink = null; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\SignedMessageChain$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
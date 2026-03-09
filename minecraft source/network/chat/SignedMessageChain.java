/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.security.SignatureException;
/*     */ import java.time.Instant;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.util.SignatureUpdater;
/*     */ import net.minecraft.util.SignatureValidator;
/*     */ import net.minecraft.util.Signer;
/*     */ import net.minecraft.world.entity.player.ProfilePublicKey;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SignedMessageChain {
/*  15 */   private static final Logger LOGGER = LogUtils.getLogger(); private SignedMessageLink nextLink;
/*     */   
/*     */   public SignedMessageChain(UUID profileId, UUID sessionId) {
/*  18 */     this.lastTimeStamp = Instant.EPOCH;
/*     */ 
/*     */     
/*  21 */     this.nextLink = SignedMessageLink.root(profileId, sessionId);
/*     */   }
/*     */   private Instant lastTimeStamp;
/*     */   public Encoder encoder(Signer signer) {
/*  25 */     return body -> {
/*  26 */         SignedMessageLink link = this.nextLink;
/*  27 */         if (link == null) {
/*  28 */           return null;
/*     */         }
/*  30 */         this.nextLink = link.advance();
/*  31 */         return new MessageSignature(signer.sign(()));
/*     */       };
/*     */   }
/*     */   
/*     */   public Decoder decoder(final ProfilePublicKey profilePublicKey) {
/*  36 */     final SignatureValidator signatureValidator = profilePublicKey.createSignatureValidator();
/*  37 */     return new Decoder()
/*     */       {
/*     */         public PlayerChatMessage unpack(MessageSignature signature, SignedMessageBody body) throws SignedMessageChain.DecodeException {
/*  40 */           if (signature == null) {
/*  41 */             throw new SignedMessageChain.DecodeException(SignedMessageChain.DecodeException.MISSING_PROFILE_KEY);
/*     */           }
/*  43 */           if (profilePublicKey.data().hasExpired()) {
/*  44 */             throw new SignedMessageChain.DecodeException(SignedMessageChain.DecodeException.EXPIRED_PROFILE_KEY);
/*     */           }
/*     */           
/*  47 */           SignedMessageLink link = SignedMessageChain.this.nextLink;
/*  48 */           if (link == null) {
/*  49 */             throw new SignedMessageChain.DecodeException(SignedMessageChain.DecodeException.CHAIN_BROKEN);
/*     */           }
/*     */ 
/*     */           
/*  53 */           if (body.timeStamp().isBefore(SignedMessageChain.this.lastTimeStamp)) {
/*  54 */             setChainBroken();
/*  55 */             throw new SignedMessageChain.DecodeException(SignedMessageChain.DecodeException.OUT_OF_ORDER_CHAT);
/*     */           } 
/*  57 */           SignedMessageChain.this.lastTimeStamp = body.timeStamp();
/*     */           
/*  59 */           PlayerChatMessage unpacked = new PlayerChatMessage(link, signature, body, null, FilterMask.PASS_THROUGH);
/*  60 */           if (!unpacked.verify(signatureValidator)) {
/*     */             
/*  62 */             setChainBroken();
/*  63 */             throw new SignedMessageChain.DecodeException(SignedMessageChain.DecodeException.INVALID_SIGNATURE);
/*     */           } 
/*     */           
/*  66 */           if (unpacked.hasExpiredServer(Instant.now())) {
/*  67 */             SignedMessageChain.LOGGER.warn("Received expired chat: '{}'. Is the client/server system time unsynchronized?", body.content());
/*     */           }
/*     */ 
/*     */           
/*  71 */           SignedMessageChain.this.nextLink = link.advance();
/*     */           
/*  73 */           return unpacked;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  78 */         public void setChainBroken() { SignedMessageChain.this.nextLink = null; }
/*     */       };
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Encoder
/*     */   {
/*  85 */     public static final Encoder UNSIGNED = body -> null;
/*     */     
/*     */     MessageSignature pack(SignedMessageBody param1SignedMessageBody);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Decoder {
/*     */     static Decoder unsigned(UUID profileId, BooleanSupplier enforcesSecureChat) {
/*  93 */       return (signature, body) -> {
/*  94 */           if (enforcesSecureChat.getAsBoolean()) {
/*  95 */             throw new SignedMessageChain.DecodeException(SignedMessageChain.DecodeException.MISSING_PROFILE_KEY);
/*     */           }
/*  97 */           return PlayerChatMessage.unsigned(profileId, body.content());
/*     */         };
/*     */     }
/*     */     
/*     */     default void setChainBroken() {}
/*     */     
/*     */     PlayerChatMessage unpack(MessageSignature param1MessageSignature, SignedMessageBody param1SignedMessageBody) throws SignedMessageChain.DecodeException;
/*     */   }
/*     */   
/*     */   public static class DecodeException
/*     */     extends ThrowingComponent {
/* 108 */     private static final Component MISSING_PROFILE_KEY = Component.translatable("chat.disabled.missingProfileKey");
/* 109 */     private static final Component CHAIN_BROKEN = Component.translatable("chat.disabled.chain_broken");
/* 110 */     private static final Component EXPIRED_PROFILE_KEY = Component.translatable("chat.disabled.expiredProfileKey");
/* 111 */     private static final Component INVALID_SIGNATURE = Component.translatable("chat.disabled.invalid_signature");
/* 112 */     private static final Component OUT_OF_ORDER_CHAT = Component.translatable("chat.disabled.out_of_order_chat");
/*     */ 
/*     */     
/* 115 */     public DecodeException(Component component) { super(component); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\SignedMessageChain.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
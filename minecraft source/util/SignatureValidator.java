/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.authlib.yggdrasil.ServicesKeyInfo;
/*    */ import com.mojang.authlib.yggdrasil.ServicesKeySet;
/*    */ import com.mojang.authlib.yggdrasil.ServicesKeyType;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.security.PublicKey;
/*    */ import java.security.Signature;
/*    */ import java.security.SignatureException;
/*    */ import java.util.Collection;
/*    */ import java.util.Objects;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public interface SignatureValidator
/*    */ {
/*    */   public static final SignatureValidator NO_VALIDATION = (payload, signature) -> true;
/* 18 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   default boolean validate(byte[] payload, byte[] signature) { return validate(output -> output.update(payload), signature); }
/*    */ 
/*    */   
/*    */   private static boolean verifySignature(SignatureUpdater updater, byte[] signature, Signature verifier) throws SignatureException {
/* 27 */     Objects.requireNonNull(verifier); updater.update(verifier::update);
/* 28 */     return verifier.verify(signature);
/*    */   }
/*    */   
/*    */   static SignatureValidator from(PublicKey publicKey, String algorithm) {
/* 32 */     return (updater, signature) -> {
/*    */         try {
/* 34 */           Signature verifier = Signature.getInstance(algorithm);
/* 35 */           verifier.initVerify(publicKey);
/* 36 */           return verifySignature(updater, signature, verifier);
/* 37 */         } catch (Exception e) {
/* 38 */           LOGGER.error("Failed to verify signature", e);
/*    */           
/* 40 */           return false;
/*    */         } 
/*    */       };
/*    */   }
/*    */   static SignatureValidator from(ServicesKeySet keySet, ServicesKeyType type) {
/* 45 */     Collection<ServicesKeyInfo> keys = keySet.keys(type);
/* 46 */     if (keys.isEmpty()) {
/* 47 */       return null;
/*    */     }
/* 49 */     return (updater, signature) -> keys.stream().anyMatch(());
/*    */   }
/*    */   
/*    */   boolean validate(SignatureUpdater paramSignatureUpdater, byte[] paramArrayOfByte);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\SignatureValidator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.util;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.Signature;
/*    */ import java.security.SignatureException;
/*    */ import java.util.Objects;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public interface Signer {
/* 10 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   default byte[] sign(byte[] payload) { return sign(output -> output.update(payload)); }
/*    */ 
/*    */   
/*    */   static Signer from(PrivateKey privateKey, String algorithm) {
/* 19 */     return updater -> {
/*    */         try {
/* 21 */           Signature signer = Signature.getInstance(algorithm);
/* 22 */           signer.initSign(privateKey);
/* 23 */           Objects.requireNonNull(signer); updater.update(signer::update);
/* 24 */           return signer.sign();
/* 25 */         } catch (Exception e) {
/*    */           
/* 27 */           throw new IllegalStateException("Failed to sign message", e);
/*    */         } 
/*    */       };
/*    */   }
/*    */   
/*    */   byte[] sign(SignatureUpdater paramSignatureUpdater);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\Signer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
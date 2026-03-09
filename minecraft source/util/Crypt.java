/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.primitives.Longs;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import it.unimi.dsi.fastutil.bytes.ByteArrays;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.Key;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGenerator;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.spec.EncodedKeySpec;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import java.util.Base64;
/*     */ import java.util.Objects;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.KeyGenerator;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.spec.IvParameterSpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ 
/*     */ public class Crypt
/*     */ {
/*     */   private static final String SYMMETRIC_ALGORITHM = "AES";
/*     */   private static final int SYMMETRIC_BITS = 128;
/*     */   private static final String ASYMMETRIC_ALGORITHM = "RSA";
/*     */   private static final int ASYMMETRIC_BITS = 1024;
/*     */   private static final String BYTE_ENCODING = "ISO_8859_1";
/*     */   private static final String HASH_ALGORITHM = "SHA-1";
/*     */   public static final String SIGNING_ALGORITHM = "SHA256withRSA";
/*     */   public static final int SIGNATURE_BYTES = 256;
/*     */   private static final String PEM_RSA_PRIVATE_KEY_HEADER = "-----BEGIN RSA PRIVATE KEY-----";
/*     */   private static final String PEM_RSA_PRIVATE_KEY_FOOTER = "-----END RSA PRIVATE KEY-----";
/*     */   public static final String RSA_PUBLIC_KEY_HEADER = "-----BEGIN RSA PUBLIC KEY-----";
/*     */   private static final String RSA_PUBLIC_KEY_FOOTER = "-----END RSA PUBLIC KEY-----";
/*     */   public static final String MIME_LINE_SEPARATOR = "\n";
/*  44 */   public static final Base64.Encoder MIME_ENCODER = Base64.getMimeEncoder(76, "\n".getBytes(StandardCharsets.UTF_8));
/*     */   
/*  46 */   public static final Codec<PublicKey> PUBLIC_KEY_CODEC = Codec.STRING.comapFlatMap(rsaString -> {
/*     */         try {
/*  48 */           return DataResult.success(stringToRsaPublicKey(rsaString));
/*  49 */         } catch (CryptException e) {
/*  50 */           Objects.requireNonNull(e); return DataResult.error(e::getMessage);
/*     */         } 
/*     */       }Crypt::rsaPublicKeyToString);
/*     */   
/*  54 */   public static final Codec<PrivateKey> PRIVATE_KEY_CODEC = Codec.STRING.comapFlatMap(rsaString -> {
/*     */         try {
/*  56 */           return DataResult.success(stringToPemRsaPrivateKey(rsaString));
/*  57 */         } catch (CryptException e) {
/*  58 */           Objects.requireNonNull(e); return DataResult.error(e::getMessage);
/*     */         } 
/*     */       }Crypt::pemRsaPrivateKeyToString);
/*     */   
/*     */   public static SecretKey generateSecretKey() throws CryptException {
/*     */     try {
/*  64 */       keyGenerator = KeyGenerator.getInstance("AES");
/*  65 */       keyGenerator.init(128);
/*  66 */       return keyGenerator.generateKey();
/*  67 */     } catch (Exception e) {
/*  68 */       throw new CryptException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static KeyPair generateKeyPair() throws CryptException {
/*     */     try {
/*  74 */       generator = KeyPairGenerator.getInstance("RSA");
/*  75 */       generator.initialize(1024);
/*     */       
/*  77 */       return generator.generateKeyPair();
/*  78 */     } catch (Exception e) {
/*  79 */       throw new CryptException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static byte[] digestData(String serverId, PublicKey publicKey, SecretKey sharedKey) throws CryptException {
/*     */     try {
/*  85 */       return digestData(new byte[][] { serverId
/*  86 */             .getBytes("ISO_8859_1"), sharedKey
/*  87 */             .getEncoded(), publicKey
/*  88 */             .getEncoded() });
/*     */     }
/*  90 */     catch (Exception e) {
/*  91 */       throw new CryptException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static byte[] digestData(byte[]... inputs) throws Exception {
/*  96 */     MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
/*  97 */     for (byte[] input : inputs) {
/*  98 */       messageDigest.update(input);
/*     */     }
/* 100 */     return messageDigest.digest();
/*     */   }
/*     */   
/*     */   private static <T extends Key> T rsaStringToKey(String input, String header, String footer, ByteArrayToKeyFunction<T> byteArrayToKey) throws CryptException {
/* 104 */     int begin = input.indexOf(header);
/* 105 */     if (begin != -1) {
/* 106 */       begin += header.length();
/* 107 */       int end = input.indexOf(footer, begin);
/*     */       
/* 109 */       input = input.substring(begin, end + 1);
/*     */     } 
/*     */     try {
/* 112 */       return (T)byteArrayToKey.apply(Base64.getMimeDecoder().decode(input));
/* 113 */     } catch (IllegalArgumentException e) {
/* 114 */       throw new CryptException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 119 */   public static PrivateKey stringToPemRsaPrivateKey(String rsaString) throws CryptException { return (PrivateKey)rsaStringToKey(rsaString, "-----BEGIN RSA PRIVATE KEY-----", "-----END RSA PRIVATE KEY-----", Crypt::byteToPrivateKey); }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public static PublicKey stringToRsaPublicKey(String rsaString) throws CryptException { return (PublicKey)rsaStringToKey(rsaString, "-----BEGIN RSA PUBLIC KEY-----", "-----END RSA PUBLIC KEY-----", Crypt::byteToPublicKey); }
/*     */ 
/*     */   
/*     */   public static String rsaPublicKeyToString(PublicKey publicKey) {
/* 127 */     if (!"RSA".equals(publicKey.getAlgorithm())) {
/* 128 */       throw new IllegalArgumentException("Public key must be RSA");
/*     */     }
/*     */     
/* 131 */     return "-----BEGIN RSA PUBLIC KEY-----\n" + MIME_ENCODER
/* 132 */       .encodeToString(publicKey.getEncoded()) + "\n-----END RSA PUBLIC KEY-----\n";
/*     */   }
/*     */ 
/*     */   
/*     */   public static String pemRsaPrivateKeyToString(PrivateKey privateKey) {
/* 137 */     if (!"RSA".equals(privateKey.getAlgorithm())) {
/* 138 */       throw new IllegalArgumentException("Private key must be RSA");
/*     */     }
/*     */     
/* 141 */     return "-----BEGIN RSA PRIVATE KEY-----\n" + MIME_ENCODER
/* 142 */       .encodeToString(privateKey.getEncoded()) + "\n-----END RSA PRIVATE KEY-----\n";
/*     */   }
/*     */ 
/*     */   
/*     */   private static PrivateKey byteToPrivateKey(byte[] keyData) throws CryptException {
/*     */     try {
/* 148 */       EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyData);
/* 149 */       KeyFactory keyFactory = KeyFactory.getInstance("RSA");
/* 150 */       return keyFactory.generatePrivate(keySpec);
/* 151 */     } catch (Exception e) {
/* 152 */       throw new CryptException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static PublicKey byteToPublicKey(byte[] keyData) throws CryptException {
/*     */     try {
/* 158 */       EncodedKeySpec keySpec = new X509EncodedKeySpec(keyData);
/* 159 */       KeyFactory keyFactory = KeyFactory.getInstance("RSA");
/* 160 */       return keyFactory.generatePublic(keySpec);
/* 161 */     } catch (Exception e) {
/* 162 */       throw new CryptException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static SecretKey decryptByteToSecretKey(PrivateKey privateKey, byte[] keyData) throws CryptException {
/* 167 */     byte[] key = decryptUsingKey(privateKey, keyData);
/*     */     try {
/* 169 */       return new SecretKeySpec(key, "AES");
/* 170 */     } catch (Exception e) {
/* 171 */       throw new CryptException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 176 */   public static byte[] encryptUsingKey(Key key, byte[] input) throws CryptException { return cipherData(1, key, input); }
/*     */ 
/*     */ 
/*     */   
/* 180 */   public static byte[] decryptUsingKey(Key key, byte[] input) throws CryptException { return cipherData(2, key, input); }
/*     */ 
/*     */   
/*     */   private static byte[] cipherData(int cipherOpMode, Key key, byte[] input) throws CryptException {
/*     */     try {
/* 185 */       return setupCipher(cipherOpMode, key.getAlgorithm(), key).doFinal(input);
/* 186 */     } catch (Exception e) {
/* 187 */       throw new CryptException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Cipher setupCipher(int cipherOpMode, String algorithm, Key key) throws Exception {
/* 192 */     Cipher cipher = Cipher.getInstance(algorithm);
/* 193 */     cipher.init(cipherOpMode, key);
/* 194 */     return cipher;
/*     */   }
/*     */   
/*     */   public static Cipher getCipher(int opMode, Key key) throws CryptException {
/*     */     try {
/* 199 */       Cipher cip = Cipher.getInstance("AES/CFB8/NoPadding");
/* 200 */       cip.init(opMode, key, new IvParameterSpec(key.getEncoded()));
/* 201 */       return cip;
/* 202 */     } catch (Exception e) {
/* 203 */       throw new CryptException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class SaltSupplier {
/* 208 */     private static final SecureRandom secureRandom = new SecureRandom();
/*     */ 
/*     */     
/* 211 */     public static long getLong() { return secureRandom.nextLong(); } }
/*     */   public static final class SaltSignaturePair extends Record { private final long salt;
/*     */     private final byte[] signature;
/*     */     
/* 215 */     public SaltSignaturePair(long salt, byte[] signature) { this.salt = salt; this.signature = signature; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/Crypt$SaltSignaturePair;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #215	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 215 */       //   0	7	0	this	Lnet/minecraft/util/Crypt$SaltSignaturePair; } public long salt() { return this.salt; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/Crypt$SaltSignaturePair;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #215	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/Crypt$SaltSignaturePair; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/Crypt$SaltSignaturePair;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #215	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/Crypt$SaltSignaturePair;
/* 215 */       //   0	8	1	o	Ljava/lang/Object; } public byte[] signature() { return this.signature; }
/* 216 */     public static final SaltSignaturePair EMPTY = new SaltSignaturePair(0L, ByteArrays.EMPTY_ARRAY);
/*     */ 
/*     */     
/* 219 */     public SaltSignaturePair(FriendlyByteBuf input) { this(input.readLong(), input.readByteArray()); }
/*     */ 
/*     */ 
/*     */     
/* 223 */     public boolean isValid() { return (this.signature.length > 0); }
/*     */ 
/*     */     
/*     */     public static void write(FriendlyByteBuf output, SaltSignaturePair saltSignaturePair) {
/* 227 */       output.writeLong(saltSignaturePair.salt);
/* 228 */       output.writeByteArray(saltSignaturePair.signature);
/*     */     }
/*     */ 
/*     */     
/* 232 */     public byte[] saltAsBytes() { return Longs.toByteArray(this.salt); } }
/*     */ 
/*     */   
/*     */   private static interface ByteArrayToKeyFunction<T extends Key> {
/*     */     T apply(byte[] param1ArrayOfByte) throws CryptException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\Crypt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
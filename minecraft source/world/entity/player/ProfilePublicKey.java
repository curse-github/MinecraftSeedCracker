/*     */ package net.minecraft.world.entity.player;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.security.PublicKey;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ThrowingComponent;
/*     */ import net.minecraft.util.Crypt;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.SignatureValidator;
/*     */ 
/*     */ public final class ProfilePublicKey extends Record {
/*     */   private final Data data;
/*     */   
/*  22 */   public ProfilePublicKey(Data data) { this.data = data; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/ProfilePublicKey;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #22	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  22 */     //   0	7	0	this	Lnet/minecraft/world/entity/player/ProfilePublicKey; } public Data data() { return this.data; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/ProfilePublicKey;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #22	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/player/ProfilePublicKey; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/player/ProfilePublicKey;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #22	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/player/ProfilePublicKey;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*  23 */   public static final Component EXPIRED_PROFILE_PUBLIC_KEY = Component.translatable("multiplayer.disconnect.expired_public_key");
/*  24 */   private static final Component INVALID_SIGNATURE = Component.translatable("multiplayer.disconnect.invalid_public_key_signature");
/*  25 */   public static final Duration EXPIRY_GRACE_PERIOD = Duration.ofHours(8L);
/*     */   
/*  27 */   public static final Codec<ProfilePublicKey> TRUSTED_CODEC = Data.CODEC.xmap(ProfilePublicKey::new, ProfilePublicKey::data);
/*     */   
/*     */   public static ProfilePublicKey createValidated(SignatureValidator validator, UUID profileId, Data data) throws ValidationException {
/*  30 */     if (!data.validateSignature(validator, profileId)) {
/*  31 */       throw new ValidationException(INVALID_SIGNATURE);
/*     */     }
/*     */     
/*  34 */     return new ProfilePublicKey(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public SignatureValidator createSignatureValidator() { return SignatureValidator.from(this.data.key, "SHA256withRSA"); }
/*     */   public static final class Data extends Record { private final Instant expiresAt; private final PublicKey key; private final byte[] keySignature; private static final int MAX_KEY_SIGNATURE_SIZE = 4096;
/*     */     
/*  49 */     public Data(Instant expiresAt, PublicKey key, byte[] keySignature) { this.expiresAt = expiresAt; this.key = key; this.keySignature = keySignature; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/ProfilePublicKey$Data;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #49	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/player/ProfilePublicKey$Data; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/ProfilePublicKey$Data;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #49	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  49 */       //   0	7	0	this	Lnet/minecraft/world/entity/player/ProfilePublicKey$Data; } public Instant expiresAt() { return this.expiresAt; } public PublicKey key() { return this.key; } public byte[] keySignature() { return this.keySignature; }
/*     */ 
/*     */ 
/*     */     
/*  53 */     public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.INSTANT_ISO8601
/*  54 */           .fieldOf("expires_at").forGetter(Data::expiresAt), Crypt.PUBLIC_KEY_CODEC
/*  55 */           .fieldOf("key").forGetter(Data::key), ExtraCodecs.BASE64_STRING
/*  56 */           .fieldOf("signature_v2").forGetter(Data::keySignature))
/*  57 */         .apply(i, Data::new));
/*     */     
/*     */     public Data(FriendlyByteBuf input) {
/*  60 */       this(input
/*  61 */           .readInstant(), input
/*  62 */           .readPublicKey(), input
/*  63 */           .readByteArray(4096));
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(FriendlyByteBuf output) {
/*  68 */       output.writeInstant(this.expiresAt);
/*  69 */       output.writePublicKey(this.key);
/*  70 */       output.writeByteArray(this.keySignature);
/*     */     }
/*     */ 
/*     */     
/*  74 */     private boolean validateSignature(SignatureValidator validator, UUID profileId) { return validator.validate(signedPayload(profileId), this.keySignature); }
/*     */ 
/*     */     
/*     */     private byte[] signedPayload(UUID profileId) {
/*  78 */       byte[] keyBytes = this.key.getEncoded();
/*  79 */       byte[] signedPayload = new byte[24 + keyBytes.length];
/*     */       
/*  81 */       ByteBuffer buffer = ByteBuffer.wrap(signedPayload).order(ByteOrder.BIG_ENDIAN);
/*  82 */       buffer.putLong(profileId.getMostSignificantBits())
/*  83 */         .putLong(profileId.getLeastSignificantBits())
/*  84 */         .putLong(this.expiresAt.toEpochMilli())
/*  85 */         .put(keyBytes);
/*     */       
/*  87 */       return signedPayload;
/*     */     }
/*     */ 
/*     */     
/*  91 */     public boolean hasExpired() { return this.expiresAt.isBefore(Instant.now()); }
/*     */ 
/*     */ 
/*     */     
/*  95 */     public boolean hasExpired(Duration gracePeriod) { return this.expiresAt.plus(gracePeriod).isBefore(Instant.now()); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 100 */       if (o instanceof Data) { Data data = (Data)o;
/* 101 */         return (this.expiresAt.equals(data.expiresAt) && this.key.equals(data.key) && Arrays.equals(this.keySignature, data.keySignature)); }
/*     */       
/* 103 */       return false;
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class ValidationException
/*     */     extends ThrowingComponent {
/* 109 */     public ValidationException(Component component) { super(component); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\player\ProfilePublicKey.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
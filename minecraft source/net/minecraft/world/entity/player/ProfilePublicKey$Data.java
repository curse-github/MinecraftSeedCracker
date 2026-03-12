/*     */ package net.minecraft.world.entity.player;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.security.PublicKey;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.Arrays;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.util.Crypt;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.SignatureValidator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Data
/*     */   extends Record
/*     */ {
/*     */   private final Instant expiresAt;
/*     */   private final PublicKey key;
/*     */   private final byte[] keySignature;
/*     */   private static final int MAX_KEY_SIGNATURE_SIZE = 4096;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/ProfilePublicKey$Data;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #49	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/player/ProfilePublicKey$Data; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/ProfilePublicKey$Data;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #49	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/player/ProfilePublicKey$Data; }
/*     */   
/*  49 */   public Data(Instant expiresAt, PublicKey key, byte[] keySignature) { this.expiresAt = expiresAt; this.key = key; this.keySignature = keySignature; } public Instant expiresAt() { return this.expiresAt; } public PublicKey key() { return this.key; } public byte[] keySignature() { return this.keySignature; }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.INSTANT_ISO8601
/*  54 */         .fieldOf("expires_at").forGetter(Data::expiresAt), Crypt.PUBLIC_KEY_CODEC
/*  55 */         .fieldOf("key").forGetter(Data::key), ExtraCodecs.BASE64_STRING
/*  56 */         .fieldOf("signature_v2").forGetter(Data::keySignature))
/*  57 */       .apply(i, Data::new));
/*     */   
/*     */   public Data(FriendlyByteBuf input) {
/*  60 */     this(input
/*  61 */         .readInstant(), input
/*  62 */         .readPublicKey(), input
/*  63 */         .readByteArray(4096));
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf output) {
/*  68 */     output.writeInstant(this.expiresAt);
/*  69 */     output.writePublicKey(this.key);
/*  70 */     output.writeByteArray(this.keySignature);
/*     */   }
/*     */ 
/*     */   
/*  74 */   private boolean validateSignature(SignatureValidator validator, UUID profileId) { return validator.validate(signedPayload(profileId), this.keySignature); }
/*     */ 
/*     */   
/*     */   private byte[] signedPayload(UUID profileId) {
/*  78 */     byte[] keyBytes = this.key.getEncoded();
/*  79 */     byte[] signedPayload = new byte[24 + keyBytes.length];
/*     */     
/*  81 */     ByteBuffer buffer = ByteBuffer.wrap(signedPayload).order(ByteOrder.BIG_ENDIAN);
/*  82 */     buffer.putLong(profileId.getMostSignificantBits())
/*  83 */       .putLong(profileId.getLeastSignificantBits())
/*  84 */       .putLong(this.expiresAt.toEpochMilli())
/*  85 */       .put(keyBytes);
/*     */     
/*  87 */     return signedPayload;
/*     */   }
/*     */ 
/*     */   
/*  91 */   public boolean hasExpired() { return this.expiresAt.isBefore(Instant.now()); }
/*     */ 
/*     */ 
/*     */   
/*  95 */   public boolean hasExpired(Duration gracePeriod) { return this.expiresAt.plus(gracePeriod).isBefore(Instant.now()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 100 */     if (o instanceof Data) { Data data = (Data)o;
/* 101 */       return (this.expiresAt.equals(data.expiresAt) && this.key.equals(data.key) && Arrays.equals(this.keySignature, data.keySignature)); }
/*     */     
/* 103 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\player\ProfilePublicKey$Data.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
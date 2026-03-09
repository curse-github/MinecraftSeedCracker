/*    */ package net.minecraft.world.entity.player;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.security.PrivateKey;
/*    */ import java.time.Instant;
/*    */ 
/*    */ public final class ProfileKeyPair extends Record {
/*    */   private final PrivateKey privateKey;
/*    */   private final ProfilePublicKey publicKey;
/*    */   private final Instant refreshedAfter;
/*    */   
/* 11 */   public ProfileKeyPair(PrivateKey privateKey, ProfilePublicKey publicKey, Instant refreshedAfter) { this.privateKey = privateKey; this.publicKey = publicKey; this.refreshedAfter = refreshedAfter; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/ProfileKeyPair;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/entity/player/ProfileKeyPair; } public PrivateKey privateKey() { return this.privateKey; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/ProfileKeyPair;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/player/ProfileKeyPair; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/player/ProfileKeyPair;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/player/ProfileKeyPair;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public ProfilePublicKey publicKey() { return this.publicKey; } public Instant refreshedAfter() { return this.refreshedAfter; }
/* 12 */   public static final Codec<ProfileKeyPair> CODEC = RecordCodecBuilder.create(i -> i.group(Crypt.PRIVATE_KEY_CODEC
/* 13 */         .fieldOf("private_key").forGetter(ProfileKeyPair::privateKey), ProfilePublicKey.TRUSTED_CODEC
/* 14 */         .fieldOf("public_key").forGetter(ProfileKeyPair::publicKey), ExtraCodecs.INSTANT_ISO8601
/* 15 */         .fieldOf("refreshed_after").forGetter(ProfileKeyPair::refreshedAfter))
/* 16 */       .apply(i, ProfileKeyPair::new));
/*    */ 
/*    */   
/* 19 */   public boolean dueRefresh() { return this.refreshedAfter.isBefore(Instant.now()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\player\ProfileKeyPair.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
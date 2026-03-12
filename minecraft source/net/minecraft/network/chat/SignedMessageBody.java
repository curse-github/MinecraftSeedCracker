/*    */ package net.minecraft.network.chat;
/*    */ import com.google.common.primitives.Longs;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.security.SignatureException;
/*    */ import java.time.Instant;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.SignatureUpdater;
/*    */ 
/*    */ public final class SignedMessageBody extends Record {
/*    */   private final String content;
/*    */   private final Instant timeStamp;
/*    */   private final long salt;
/*    */   private final LastSeenMessages lastSeen;
/*    */   
/* 18 */   public SignedMessageBody(String content, Instant timeStamp, long salt, LastSeenMessages lastSeen) { this.content = content; this.timeStamp = timeStamp; this.salt = salt; this.lastSeen = lastSeen; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/SignedMessageBody;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/network/chat/SignedMessageBody; } public String content() { return this.content; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/SignedMessageBody;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignedMessageBody; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/SignedMessageBody;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/SignedMessageBody;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public Instant timeStamp() { return this.timeStamp; } public long salt() { return this.salt; } public LastSeenMessages lastSeen() { return this.lastSeen; }
/* 19 */   public static final MapCodec<SignedMessageBody> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING
/* 20 */         .fieldOf("content").forGetter(SignedMessageBody::content), ExtraCodecs.INSTANT_ISO8601
/* 21 */         .fieldOf("time_stamp").forGetter(SignedMessageBody::timeStamp), Codec.LONG
/* 22 */         .fieldOf("salt").forGetter(SignedMessageBody::salt), LastSeenMessages.CODEC
/* 23 */         .optionalFieldOf("last_seen", LastSeenMessages.EMPTY).forGetter(SignedMessageBody::lastSeen))
/* 24 */       .apply(i, SignedMessageBody::new));
/*    */ 
/*    */   
/* 27 */   public static SignedMessageBody unsigned(String content) { return new SignedMessageBody(content, Instant.now(), 0L, LastSeenMessages.EMPTY); }
/*    */ 
/*    */   
/*    */   public void updateSignature(SignatureUpdater.Output output) throws SignatureException {
/* 31 */     output.update(Longs.toByteArray(this.salt));
/* 32 */     output.update(Longs.toByteArray(this.timeStamp.getEpochSecond()));
/* 33 */     byte[] contentBytes = this.content.getBytes(StandardCharsets.UTF_8);
/* 34 */     output.update(Ints.toByteArray(contentBytes.length));
/* 35 */     output.update(contentBytes);
/* 36 */     this.lastSeen.updateSignature(output);
/*    */   }
/*    */ 
/*    */   
/* 40 */   public Packed pack(MessageSignatureCache cache) { return new Packed(this.content, this.timeStamp, this.salt, this.lastSeen.pack(cache)); }
/*    */   public static final class Packed extends Record { private final String content; private final Instant timeStamp; private final long salt; private final LastSeenMessages.Packed lastSeen;
/*    */     
/* 43 */     public Packed(String content, Instant timeStamp, long salt, LastSeenMessages.Packed lastSeen) { this.content = content; this.timeStamp = timeStamp; this.salt = salt; this.lastSeen = lastSeen; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/SignedMessageBody$Packed;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/SignedMessageBody$Packed; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/SignedMessageBody$Packed;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/SignedMessageBody$Packed; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/SignedMessageBody$Packed;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/SignedMessageBody$Packed;
/* 43 */       //   0	8	1	o	Ljava/lang/Object; } public String content() { return this.content; } public Instant timeStamp() { return this.timeStamp; } public long salt() { return this.salt; } public LastSeenMessages.Packed lastSeen() { return this.lastSeen; }
/*    */     
/* 45 */     public Packed(FriendlyByteBuf input) { this(input.readUtf(256), input.readInstant(), input.readLong(), new LastSeenMessages.Packed(input)); }
/*    */ 
/*    */     
/*    */     public void write(FriendlyByteBuf output) {
/* 49 */       output.writeUtf(this.content, 256);
/* 50 */       output.writeInstant(this.timeStamp);
/* 51 */       output.writeLong(this.salt);
/* 52 */       this.lastSeen.write(output);
/*    */     }
/*    */ 
/*    */     
/* 56 */     public Optional<SignedMessageBody> unpack(MessageSignatureCache cache) { return this.lastSeen.unpack(cache).map(lastSeen -> new SignedMessageBody(this.content, this.timeStamp, this.salt, lastSeen)); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\SignedMessageBody.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
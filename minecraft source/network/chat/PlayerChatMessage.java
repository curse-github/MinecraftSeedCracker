/*     */ package net.minecraft.network.chat;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.security.SignatureException;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.util.SignatureUpdater;
/*     */ import net.minecraft.util.SignatureValidator;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public final class PlayerChatMessage extends Record {
/*     */   private final SignedMessageLink link;
/*     */   private final MessageSignature signature;
/*     */   private final SignedMessageBody signedBody;
/*     */   private final Component unsignedContent;
/*     */   private final FilterMask filterMask;
/*     */   
/*  20 */   public PlayerChatMessage(SignedMessageLink link, MessageSignature signature, SignedMessageBody signedBody, Component unsignedContent, FilterMask filterMask) { this.link = link; this.signature = signature; this.signedBody = signedBody; this.unsignedContent = unsignedContent; this.filterMask = filterMask; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/PlayerChatMessage;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  20 */     //   0	7	0	this	Lnet/minecraft/network/chat/PlayerChatMessage; } public SignedMessageLink link() { return this.link; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/PlayerChatMessage;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/PlayerChatMessage; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/PlayerChatMessage;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/chat/PlayerChatMessage;
/*  20 */     //   0	8	1	o	Ljava/lang/Object; } public MessageSignature signature() { return this.signature; } public SignedMessageBody signedBody() { return this.signedBody; } public Component unsignedContent() { return this.unsignedContent; } public FilterMask filterMask() { return this.filterMask; }
/*  21 */   public static final MapCodec<PlayerChatMessage> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(SignedMessageLink.CODEC
/*  22 */         .fieldOf("link").forGetter(PlayerChatMessage::link), MessageSignature.CODEC
/*  23 */         .optionalFieldOf("signature").forGetter(()), SignedMessageBody.MAP_CODEC
/*  24 */         .forGetter(PlayerChatMessage::signedBody), ComponentSerialization.CODEC
/*  25 */         .optionalFieldOf("unsigned_content").forGetter(()), FilterMask.CODEC
/*  26 */         .optionalFieldOf("filter_mask", FilterMask.PASS_THROUGH).forGetter(PlayerChatMessage::filterMask))
/*  27 */       .apply(i, ()));
/*     */   
/*  29 */   private static final UUID SYSTEM_SENDER = Util.NIL_UUID;
/*  30 */   public static final Duration MESSAGE_EXPIRES_AFTER_SERVER = Duration.ofMinutes(5L);
/*  31 */   public static final Duration MESSAGE_EXPIRES_AFTER_CLIENT = MESSAGE_EXPIRES_AFTER_SERVER.plus(Duration.ofMinutes(2L));
/*     */ 
/*     */   
/*  34 */   public static PlayerChatMessage system(String content) { return unsigned(SYSTEM_SENDER, content); }
/*     */ 
/*     */   
/*     */   public static PlayerChatMessage unsigned(UUID sender, String content) {
/*  38 */     SignedMessageBody body = SignedMessageBody.unsigned(content);
/*  39 */     SignedMessageLink link = SignedMessageLink.unsigned(sender);
/*  40 */     return new PlayerChatMessage(link, null, body, null, FilterMask.PASS_THROUGH);
/*     */   }
/*     */   
/*     */   public PlayerChatMessage withUnsignedContent(Component content) {
/*  44 */     Component unsignedContent = !content.equals(Component.literal(signedContent())) ? content : null;
/*  45 */     return new PlayerChatMessage(this.link, this.signature, this.signedBody, unsignedContent, this.filterMask);
/*     */   }
/*     */   
/*     */   public PlayerChatMessage removeUnsignedContent() {
/*  49 */     if (this.unsignedContent != null) {
/*  50 */       return new PlayerChatMessage(this.link, this.signature, this.signedBody, null, this.filterMask);
/*     */     }
/*  52 */     return this;
/*     */   }
/*     */   
/*     */   public PlayerChatMessage filter(FilterMask filterMask) {
/*  56 */     if (this.filterMask.equals(filterMask)) {
/*  57 */       return this;
/*     */     }
/*  59 */     return new PlayerChatMessage(this.link, this.signature, this.signedBody, this.unsignedContent, filterMask);
/*     */   }
/*     */ 
/*     */   
/*  63 */   public PlayerChatMessage filter(boolean filtered) { return filter(filtered ? this.filterMask : FilterMask.PASS_THROUGH); }
/*     */ 
/*     */   
/*     */   public PlayerChatMessage removeSignature() {
/*  67 */     SignedMessageBody body = SignedMessageBody.unsigned(signedContent());
/*  68 */     SignedMessageLink link = SignedMessageLink.unsigned(sender());
/*  69 */     return new PlayerChatMessage(link, null, body, this.unsignedContent, this.filterMask);
/*     */   }
/*     */   
/*     */   public static void updateSignature(SignatureUpdater.Output output, SignedMessageLink link, SignedMessageBody body) throws SignatureException {
/*  73 */     output.update(Ints.toByteArray(1));
/*  74 */     link.updateSignature(output);
/*  75 */     body.updateSignature(output);
/*     */   }
/*     */ 
/*     */   
/*  79 */   public boolean verify(SignatureValidator signatureValidator) { return (this.signature != null && this.signature.verify(signatureValidator, output -> updateSignature(output, this.link, this.signedBody))); }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public String signedContent() { return this.signedBody.content(); }
/*     */ 
/*     */ 
/*     */   
/*  87 */   public Component decoratedContent() { return (Component)Objects.requireNonNullElseGet(this.unsignedContent, () -> Component.literal(signedContent())); }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public Instant timeStamp() { return this.signedBody.timeStamp(); }
/*     */ 
/*     */ 
/*     */   
/*  95 */   public long salt() { return this.signedBody.salt(); }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public boolean hasExpiredServer(Instant now) { return now.isAfter(timeStamp().plus(MESSAGE_EXPIRES_AFTER_SERVER)); }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public boolean hasExpiredClient(Instant now) { return now.isAfter(timeStamp().plus(MESSAGE_EXPIRES_AFTER_CLIENT)); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public UUID sender() { return this.link.sender(); }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public boolean isSystem() { return sender().equals(SYSTEM_SENDER); }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public boolean hasSignature() { return (this.signature != null); }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public boolean hasSignatureFrom(UUID profileId) { return (hasSignature() && this.link.sender().equals(profileId)); }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public boolean isFullyFiltered() { return this.filterMask.isFullyFiltered(); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public static String describeSigned(PlayerChatMessage message) { return "'" + message.signedBody.content() + "' @ " + String.valueOf(message.signedBody.timeStamp()) + "\n - From: " + 
/* 128 */       String.valueOf(message.link.sender()) + "/" + String.valueOf(message.link.sessionId()) + ", message #" + message.link.index() + "\n - Salt: " + message.signedBody
/* 129 */       .salt() + "\n - Signature: " + 
/* 130 */       MessageSignature.describe(message.signature) + "\n - Last Seen: [\n" + (String)message.signedBody
/*     */       
/* 132 */       .lastSeen().entries().stream()
/* 133 */       .map(signature -> "     " + MessageSignature.describe(signature) + "\n")
/* 134 */       .collect(Collectors.joining()) + " ]\n"; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\PlayerChatMessage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
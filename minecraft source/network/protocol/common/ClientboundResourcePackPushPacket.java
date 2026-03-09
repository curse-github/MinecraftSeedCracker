/*    */ package net.minecraft.network.protocol.common;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundResourcePackPushPacket extends Record implements Packet<ClientCommonPacketListener> {
/*    */   private final UUID id;
/*    */   private final String url;
/*    */   private final String hash;
/*    */   private final boolean required;
/*    */   
/* 16 */   public UUID id() { return this.id; } private final Optional<Component> prompt; public static final int MAX_HASH_LENGTH = 40; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundResourcePackPushPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundResourcePackPushPacket; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundResourcePackPushPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundResourcePackPushPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundResourcePackPushPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundResourcePackPushPacket;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public String url() { return this.url; } public String hash() { return this.hash; } public boolean required() { return this.required; } public Optional<Component> prompt() { return this.prompt; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static final StreamCodec<ByteBuf, ClientboundResourcePackPushPacket> STREAM_CODEC = StreamCodec.composite(UUIDUtil.STREAM_CODEC, ClientboundResourcePackPushPacket::id, ByteBufCodecs.STRING_UTF8, ClientboundResourcePackPushPacket::url, 
/*    */ 
/*    */       
/* 28 */       ByteBufCodecs.stringUtf8(40), ClientboundResourcePackPushPacket::hash, ByteBufCodecs.BOOL, ClientboundResourcePackPushPacket::required, ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC
/*    */       
/* 30 */       .apply(ByteBufCodecs::optional), ClientboundResourcePackPushPacket::prompt, ClientboundResourcePackPushPacket::new);
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientboundResourcePackPushPacket(UUID id, String url, String hash, boolean required, Optional<Component> prompt) {
/* 35 */     if (hash.length() > 40)
/* 36 */       throw new IllegalArgumentException("Hash is too long (max 40, was " + hash.length() + ")"); 
/*    */     this.id = id;
/*    */     this.url = url;
/*    */     this.hash = hash;
/*    */     this.required = required;
/*    */     this.prompt = prompt;
/* 42 */   } public PacketType<ClientboundResourcePackPushPacket> type() { return CommonPacketTypes.CLIENTBOUND_RESOURCE_PACK_PUSH; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public void handle(ClientCommonPacketListener listener) { listener.handleResourcePackPush(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ClientboundResourcePackPushPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
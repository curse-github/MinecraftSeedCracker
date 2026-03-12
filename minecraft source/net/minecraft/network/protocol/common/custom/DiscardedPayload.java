/*    */ package net.minecraft.network.protocol.common.custom;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class DiscardedPayload extends Record implements CustomPacketPayload {
/*    */   private final Identifier id;
/*    */   
/*  7 */   public DiscardedPayload(Identifier id) { this.id = id; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/DiscardedPayload; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/DiscardedPayload; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   public static <T extends FriendlyByteBuf> StreamCodec<T, DiscardedPayload> codec(Identifier id, int maxPayloadSize) {
/*  9 */     return CustomPacketPayload.codec((payload, buf) -> {
/*    */         
/*    */         }buf -> {
/* 12 */           int length = buf.readableBytes();
/* 13 */           if (length < 0 || length > maxPayloadSize) {
/* 14 */             throw new IllegalArgumentException("Payload may not be larger than " + maxPayloadSize + " bytes");
/*    */           }
/* 16 */           buf.skipBytes(length);
/* 17 */           return new DiscardedPayload(id);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public CustomPacketPayload.Type<DiscardedPayload> type() { return new CustomPacketPayload.Type(this.id); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\custom\DiscardedPayload.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
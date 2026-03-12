/*    */ package net.minecraft.network.protocol.common.custom;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.codec.StreamMemberEncoder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface CustomPacketPayload
/*    */ {
/*    */   Type<? extends CustomPacketPayload> type();
/*    */   
/* 20 */   static <B extends io.netty.buffer.ByteBuf, T extends CustomPacketPayload> StreamCodec<B, T> codec(StreamMemberEncoder<B, T> writer, StreamDecoder<B, T> reader) { return StreamCodec.ofMember(writer, reader); }
/*    */   public static final class Type<T extends CustomPacketPayload> extends Record { private final Identifier id;
/*    */     
/* 23 */     public Type(Identifier id) { this.id = id; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$Type;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$Type;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 23 */       //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$Type<TT;>; } public Identifier id() { return this.id; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$Type;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$Type;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$Type<TT;>; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$Type;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$Type;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$Type<TT;>; } }
/*    */   
/* 27 */   static <T extends CustomPacketPayload> Type<T> createType(String id) { return new Type(Identifier.withDefaultNamespace(id)); }
/*    */   public static final class TypeAndCodec<B extends FriendlyByteBuf, T extends CustomPacketPayload> extends Record { private final CustomPacketPayload.Type<T> type; private final StreamCodec<B, T> codec;
/*    */     
/* 30 */     public TypeAndCodec(CustomPacketPayload.Type<T> type, StreamCodec<B, T> codec) { this.type = type; this.codec = codec; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 30 */       //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec<TB;TT;>; } public CustomPacketPayload.Type<T> type() { return this.type; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec<TB;TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 30 */       //   0	8	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec<TB;TT;>; } public StreamCodec<B, T> codec() { return this.codec; } }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <B extends FriendlyByteBuf> StreamCodec<B, CustomPacketPayload> codec(final FallbackProvider<B> fallback, List<TypeAndCodec<? super B, ?>> types) {
/* 37 */     final Map<Identifier, StreamCodec<? super B, ? extends CustomPacketPayload>> idToType = (Map)types.stream().collect(Collectors.toUnmodifiableMap(t -> t.type().id(), TypeAndCodec::codec));
/*    */     
/* 39 */     return new StreamCodec<B, CustomPacketPayload>() {
/*    */         private StreamCodec<? super B, ? extends CustomPacketPayload> findCodec(Identifier typeId) {
/* 41 */           StreamCodec<? super B, ? extends CustomPacketPayload> codec = (StreamCodec)idToType.get(typeId);
/* 42 */           if (codec != null) {
/* 43 */             return codec;
/*    */           }
/* 45 */           return fallback.create(typeId);
/*    */         }
/*    */ 
/*    */         
/*    */         private <T extends CustomPacketPayload> void writeCap(B output, CustomPacketPayload.Type<T> type, CustomPacketPayload payload) {
/* 50 */           output.writeIdentifier(type.id());
/* 51 */           StreamCodec<B, T> codec = findCodec(type.id);
/* 52 */           codec.encode(output, payload);
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 57 */         public void encode(B output, CustomPacketPayload value) { writeCap(output, value.type(), value); }
/*    */ 
/*    */ 
/*    */         
/*    */         public CustomPacketPayload decode(B input) {
/* 62 */           Identifier identifier = input.readIdentifier();
/* 63 */           return (CustomPacketPayload)findCodec(identifier).decode(input);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static interface FallbackProvider<B extends FriendlyByteBuf> {
/*    */     StreamCodec<B, ? extends CustomPacketPayload> create(Identifier param1Identifier);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\custom\CustomPacketPayload.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
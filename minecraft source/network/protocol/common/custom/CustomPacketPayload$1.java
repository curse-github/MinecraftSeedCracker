/*    */ package net.minecraft.network.protocol.common.custom;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements StreamCodec<B, CustomPacketPayload>
/*    */ {
/*    */   private StreamCodec<? super B, ? extends CustomPacketPayload> findCodec(Identifier typeId) {
/* 41 */     StreamCodec<? super B, ? extends CustomPacketPayload> codec = (StreamCodec)idToType.get(typeId);
/* 42 */     if (codec != null) {
/* 43 */       return codec;
/*    */     }
/* 45 */     return fallback.create(typeId);
/*    */   }
/*    */ 
/*    */   
/*    */   private <T extends CustomPacketPayload> void writeCap(B output, CustomPacketPayload.Type<T> type, CustomPacketPayload payload) {
/* 50 */     output.writeIdentifier(type.id());
/* 51 */     StreamCodec<B, T> codec = findCodec(type.id);
/* 52 */     codec.encode(output, payload);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public void encode(B output, CustomPacketPayload value) { writeCap(output, value.type(), value); }
/*    */ 
/*    */ 
/*    */   
/*    */   public CustomPacketPayload decode(B input) {
/* 62 */     Identifier identifier = input.readIdentifier();
/* 63 */     return (CustomPacketPayload)findCodec(identifier).decode(input);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\custom\CustomPacketPayload$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
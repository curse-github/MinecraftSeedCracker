/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.SkipPacketDecoderException;
/*    */ import net.minecraft.network.SkipPacketEncoderException;
/*    */ import net.minecraft.network.codec.StreamCodec;
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
/*    */   implements StreamCodec<RegistryFriendlyByteBuf, ServerboundSetCreativeModeSlotPacket>
/*    */ {
/*    */   public ServerboundSetCreativeModeSlotPacket decode(RegistryFriendlyByteBuf input) {
/* 50 */     if (!context.hasInfiniteMaterials()) {
/* 51 */       throw new SkipPacketDecoderException("Not in creative mode");
/*    */     }
/* 53 */     return (ServerboundSetCreativeModeSlotPacket)original.decode(input);
/*    */   }
/*    */ 
/*    */   
/*    */   public void encode(RegistryFriendlyByteBuf output, ServerboundSetCreativeModeSlotPacket value) {
/* 58 */     if (!context.hasInfiniteMaterials()) {
/* 59 */       throw new SkipPacketEncoderException("Not in creative mode");
/*    */     }
/* 61 */     original.encode(output, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\GameProtocols$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
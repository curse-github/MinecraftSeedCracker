/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class ServerboundSetCreativeModeSlotPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final short slotNum;
/*    */   private final ItemStack itemStack;
/*    */   
/* 10 */   public ServerboundSetCreativeModeSlotPacket(short slotNum, ItemStack itemStack) { this.slotNum = slotNum; this.itemStack = itemStack; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundSetCreativeModeSlotPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetCreativeModeSlotPacket; } public short slotNum() { return this.slotNum; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundSetCreativeModeSlotPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetCreativeModeSlotPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundSetCreativeModeSlotPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetCreativeModeSlotPacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public ItemStack itemStack() { return this.itemStack; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSetCreativeModeSlotPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.SHORT, ServerboundSetCreativeModeSlotPacket::slotNum, 
/*    */       
/* 16 */       ItemStack.validatedStreamCodec(ItemStack.OPTIONAL_UNTRUSTED_STREAM_CODEC), ServerboundSetCreativeModeSlotPacket::itemStack, ServerboundSetCreativeModeSlotPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public ServerboundSetCreativeModeSlotPacket(int slotNum, ItemStack itemStack) { this((short)slotNum, itemStack); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public PacketType<ServerboundSetCreativeModeSlotPacket> type() { return GamePacketTypes.SERVERBOUND_SET_CREATIVE_MODE_SLOT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public void handle(ServerGamePacketListener listener) { listener.handleSetCreativeModeSlot(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSetCreativeModeSlotPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
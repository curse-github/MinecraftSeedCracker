/*    */ package net.minecraft.network.protocol.game;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import net.minecraft.network.HashedStack;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.world.inventory.ClickType;
/*    */ 
/*    */ public final class ServerboundContainerClickPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final int containerId;
/*    */   private final int stateId;
/*    */   private final short slotNum;
/*    */   private final byte buttonNum;
/*    */   private final ClickType clickType;
/*    */   
/* 14 */   public int containerId() { return this.containerId; } private final Int2ObjectMap<HashedStack> changedSlots; private final HashedStack carriedItem; private static final int MAX_SLOT_COUNT = 128; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundContainerClickPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerClickPacket; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundContainerClickPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerClickPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundContainerClickPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerClickPacket;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public int stateId() { return this.stateId; } public short slotNum() { return this.slotNum; } public byte buttonNum() { return this.buttonNum; } public ClickType clickType() { return this.clickType; } public Int2ObjectMap<HashedStack> changedSlots() { return this.changedSlots; } public HashedStack carriedItem() { return this.carriedItem; }
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
/* 25 */   private static final StreamCodec<RegistryFriendlyByteBuf, Int2ObjectMap<HashedStack>> SLOTS_STREAM_CODEC = ByteBufCodecs.map(it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap::new, ByteBufCodecs.SHORT
/*    */       
/* 27 */       .map(Short::intValue, Integer::shortValue), HashedStack.STREAM_CODEC, 128);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundContainerClickPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ServerboundContainerClickPacket::containerId, ByteBufCodecs.VAR_INT, ServerboundContainerClickPacket::stateId, ByteBufCodecs.SHORT, ServerboundContainerClickPacket::slotNum, ByteBufCodecs.BYTE, ServerboundContainerClickPacket::buttonNum, ClickType.STREAM_CODEC, ServerboundContainerClickPacket::clickType, SLOTS_STREAM_CODEC, ServerboundContainerClickPacket::changedSlots, HashedStack.STREAM_CODEC, ServerboundContainerClickPacket::carriedItem, ServerboundContainerClickPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ServerboundContainerClickPacket(int containerId, int stateId, short slotNum, byte buttonNum, ClickType clickType, Int2ObjectMap<HashedStack> changedSlots, HashedStack carriedItem)
/*    */   {
/* 44 */     changedSlots = Int2ObjectMaps.unmodifiable(changedSlots); this.containerId = containerId; this.stateId = stateId;
/*    */     this.slotNum = slotNum;
/*    */     this.buttonNum = buttonNum;
/*    */     this.clickType = clickType;
/*    */     this.changedSlots = changedSlots;
/* 49 */     this.carriedItem = carriedItem; } public PacketType<ServerboundContainerClickPacket> type() { return GamePacketTypes.SERVERBOUND_CONTAINER_CLICK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public void handle(ServerGamePacketListener listener) { listener.handleContainerClick(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundContainerClickPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ClientboundContainerSetSlotPacket extends Object implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundContainerSetSlotPacket> STREAM_CODEC = Packet.codec(ClientboundContainerSetSlotPacket::write, ClientboundContainerSetSlotPacket::new);
/*    */   
/*    */   private final int containerId;
/*    */   private final int stateId;
/*    */   private final int slot;
/*    */   private final ItemStack itemStack;
/*    */   
/*    */   public ClientboundContainerSetSlotPacket(int containerId, int stateId, int slot, ItemStack itemStack) {
/* 18 */     this.containerId = containerId;
/* 19 */     this.stateId = stateId;
/* 20 */     this.slot = slot;
/* 21 */     this.itemStack = itemStack.copy();
/*    */   }
/*    */   
/*    */   private ClientboundContainerSetSlotPacket(RegistryFriendlyByteBuf input) {
/* 25 */     this.containerId = input.readContainerId();
/* 26 */     this.stateId = input.readVarInt();
/* 27 */     this.slot = input.readShort();
/* 28 */     this.itemStack = (ItemStack)ItemStack.OPTIONAL_STREAM_CODEC.decode(input);
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 32 */     output.writeContainerId(this.containerId);
/* 33 */     output.writeVarInt(this.stateId);
/* 34 */     output.writeShort(this.slot);
/* 35 */     ItemStack.OPTIONAL_STREAM_CODEC.encode(output, this.itemStack);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public PacketType<ClientboundContainerSetSlotPacket> type() { return GamePacketTypes.CLIENTBOUND_CONTAINER_SET_SLOT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public void handle(ClientGamePacketListener listener) { listener.handleContainerSetSlot(this); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public int getContainerId() { return this.containerId; }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public int getSlot() { return this.slot; }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public ItemStack getItem() { return this.itemStack; }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public int getStateId() { return this.stateId; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundContainerSetSlotPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
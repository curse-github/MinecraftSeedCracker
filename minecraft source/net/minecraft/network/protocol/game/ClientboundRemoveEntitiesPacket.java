/*    */ package net.minecraft.network.protocol.game;
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundRemoveEntitiesPacket extends Object implements Packet<ClientGamePacketListener> {
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ClientboundRemoveEntitiesPacket> STREAM_CODEC = Packet.codec(ClientboundRemoveEntitiesPacket::write, ClientboundRemoveEntitiesPacket::new);
/*    */   
/*    */   private final IntList entityIds;
/*    */ 
/*    */   
/* 17 */   public ClientboundRemoveEntitiesPacket(IntList ids) { this.entityIds = new IntArrayList(ids); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public ClientboundRemoveEntitiesPacket(int... ids) { this.entityIds = new IntArrayList(ids); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   private ClientboundRemoveEntitiesPacket(FriendlyByteBuf input) { this.entityIds = input.readIntIdList(); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   private void write(FriendlyByteBuf output) { output.writeIntIdList(this.entityIds); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public PacketType<ClientboundRemoveEntitiesPacket> type() { return GamePacketTypes.CLIENTBOUND_REMOVE_ENTITIES; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public void handle(ClientGamePacketListener listener) { listener.handleRemoveEntities(this); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public IntList getEntityIds() { return this.entityIds; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundRemoveEntitiesPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
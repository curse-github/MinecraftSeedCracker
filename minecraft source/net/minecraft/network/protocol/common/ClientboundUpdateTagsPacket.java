/*    */ package net.minecraft.network.protocol.common;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagNetworkSerialization;
/*    */ 
/*    */ public class ClientboundUpdateTagsPacket extends Object implements Packet<ClientCommonPacketListener> {
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ClientboundUpdateTagsPacket> STREAM_CODEC = Packet.codec(ClientboundUpdateTagsPacket::write, ClientboundUpdateTagsPacket::new);
/*    */   
/*    */   private final Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> tags;
/*    */ 
/*    */   
/* 19 */   public ClientboundUpdateTagsPacket(Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> tags) { this.tags = tags; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   private ClientboundUpdateTagsPacket(FriendlyByteBuf input) { this.tags = input.readMap(FriendlyByteBuf::readRegistryKey, TagNetworkSerialization.NetworkPayload::read); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 30 */     output.writeMap(this.tags, FriendlyByteBuf::writeResourceKey, (buffer, value) -> 
/*    */         
/* 32 */         value.write(buffer));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public PacketType<ClientboundUpdateTagsPacket> type() { return CommonPacketTypes.CLIENTBOUND_UPDATE_TAGS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public void handle(ClientCommonPacketListener listener) { listener.handleUpdateTags(this); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> getTags() { return this.tags; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ClientboundUpdateTagsPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
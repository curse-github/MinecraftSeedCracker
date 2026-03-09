/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.BitSet;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ 
/*    */ public class ClientboundLightUpdatePacket extends Object implements Packet<ClientGamePacketListener> {
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ClientboundLightUpdatePacket> STREAM_CODEC = Packet.codec(ClientboundLightUpdatePacket::write, ClientboundLightUpdatePacket::new);
/*    */   
/*    */   private final int x;
/*    */   private final int z;
/*    */   private final ClientboundLightUpdatePacketData lightData;
/*    */   
/*    */   public ClientboundLightUpdatePacket(ChunkPos pos, LevelLightEngine lightEngine, BitSet skyChangedLightSectionFilter, BitSet blockChangedLightSectionFilter) {
/* 21 */     this.x = pos.x;
/* 22 */     this.z = pos.z;
/* 23 */     this.lightData = new ClientboundLightUpdatePacketData(pos, lightEngine, skyChangedLightSectionFilter, blockChangedLightSectionFilter);
/*    */   }
/*    */   
/*    */   private ClientboundLightUpdatePacket(FriendlyByteBuf input) {
/* 27 */     this.x = input.readVarInt();
/* 28 */     this.z = input.readVarInt();
/* 29 */     this.lightData = new ClientboundLightUpdatePacketData(input, this.x, this.z);
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 33 */     output.writeVarInt(this.x);
/* 34 */     output.writeVarInt(this.z);
/* 35 */     this.lightData.write(output);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public PacketType<ClientboundLightUpdatePacket> type() { return GamePacketTypes.CLIENTBOUND_LIGHT_UPDATE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public void handle(ClientGamePacketListener listener) { listener.handleLightUpdatePacket(this); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public int getX() { return this.x; }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public int getZ() { return this.z; }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public ClientboundLightUpdatePacketData getLightData() { return this.lightData; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundLightUpdatePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
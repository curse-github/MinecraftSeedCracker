/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.BitSet;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ 
/*    */ public class ClientboundLevelChunkWithLightPacket extends Object implements Packet<ClientGamePacketListener> {
/* 15 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLevelChunkWithLightPacket> STREAM_CODEC = Packet.codec(ClientboundLevelChunkWithLightPacket::write, ClientboundLevelChunkWithLightPacket::new);
/*    */   
/*    */   private final int x;
/*    */   
/*    */   private final int z;
/*    */   private final ClientboundLevelChunkPacketData chunkData;
/*    */   private final ClientboundLightUpdatePacketData lightData;
/*    */   
/*    */   public ClientboundLevelChunkWithLightPacket(LevelChunk levelChunk, LevelLightEngine lightEngine, BitSet skyChangedLightSectionFilter, BitSet blockChangedLightSectionFilter) {
/* 24 */     ChunkPos chunkPos = levelChunk.getPos();
/* 25 */     this.x = chunkPos.x;
/* 26 */     this.z = chunkPos.z;
/*    */     
/* 28 */     this.chunkData = new ClientboundLevelChunkPacketData(levelChunk);
/* 29 */     this.lightData = new ClientboundLightUpdatePacketData(chunkPos, lightEngine, skyChangedLightSectionFilter, blockChangedLightSectionFilter);
/*    */   }
/*    */   
/*    */   private ClientboundLevelChunkWithLightPacket(RegistryFriendlyByteBuf input) {
/* 33 */     this.x = input.readInt();
/* 34 */     this.z = input.readInt();
/* 35 */     this.chunkData = new ClientboundLevelChunkPacketData(input, this.x, this.z);
/* 36 */     this.lightData = new ClientboundLightUpdatePacketData(input, this.x, this.z);
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 40 */     output.writeInt(this.x);
/* 41 */     output.writeInt(this.z);
/* 42 */     this.chunkData.write(output);
/* 43 */     this.lightData.write(output);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public PacketType<ClientboundLevelChunkWithLightPacket> type() { return GamePacketTypes.CLIENTBOUND_LEVEL_CHUNK_WITH_LIGHT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public void handle(ClientGamePacketListener listener) { listener.handleLevelChunkWithLight(this); }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public int getX() { return this.x; }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public int getZ() { return this.z; }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public ClientboundLevelChunkPacketData getChunkData() { return this.chunkData; }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public ClientboundLightUpdatePacketData getLightData() { return this.lightData; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundLevelChunkWithLightPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
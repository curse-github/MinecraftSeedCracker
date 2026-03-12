/*    */ package net.minecraft.network.protocol.game;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundChunkBatchStartPacket extends Object implements Packet<ClientGamePacketListener> {
/*  9 */   public static final ClientboundChunkBatchStartPacket INSTANCE = new ClientboundChunkBatchStartPacket();
/* 10 */   public static final StreamCodec<ByteBuf, ClientboundChunkBatchStartPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public PacketType<ClientboundChunkBatchStartPacket> type() { return GamePacketTypes.CLIENTBOUND_CHUNK_BATCH_START; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public void handle(ClientGamePacketListener listener) { listener.handleChunkBatchStart(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundChunkBatchStartPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
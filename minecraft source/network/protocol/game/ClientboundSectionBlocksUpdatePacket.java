/*    */ package net.minecraft.network.protocol.game;
/*    */ import it.unimi.dsi.fastutil.shorts.ShortIterator;
/*    */ import it.unimi.dsi.fastutil.shorts.ShortSet;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*    */ 
/*    */ public class ClientboundSectionBlocksUpdatePacket extends Object implements Packet<ClientGamePacketListener> {
/* 17 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSectionBlocksUpdatePacket> STREAM_CODEC = Packet.codec(ClientboundSectionBlocksUpdatePacket::write, ClientboundSectionBlocksUpdatePacket::new);
/*    */   
/*    */   private static final int POS_IN_SECTION_BITS = 12;
/*    */   
/*    */   private final SectionPos sectionPos;
/*    */   private final short[] positions;
/*    */   private final BlockState[] states;
/*    */   
/*    */   public ClientboundSectionBlocksUpdatePacket(SectionPos sectionPos, ShortSet changes, LevelChunkSection section) {
/* 26 */     this.sectionPos = sectionPos;
/* 27 */     int count = changes.size();
/* 28 */     this.positions = new short[count];
/* 29 */     this.states = new BlockState[count];
/*    */     
/* 31 */     int i = 0;
/* 32 */     for (ShortIterator shortIterator = changes.iterator(); shortIterator.hasNext(); ) { short packedPos = ((Short)shortIterator.next()).shortValue();
/* 33 */       this.positions[i] = packedPos;
/* 34 */       this.states[i] = section.getBlockState(SectionPos.sectionRelativeX(packedPos), SectionPos.sectionRelativeY(packedPos), SectionPos.sectionRelativeZ(packedPos));
/* 35 */       i++; }
/*    */   
/*    */   }
/*    */   
/*    */   private ClientboundSectionBlocksUpdatePacket(FriendlyByteBuf input) {
/* 40 */     this.sectionPos = (SectionPos)SectionPos.STREAM_CODEC.decode(input);
/* 41 */     int count = input.readVarInt();
/* 42 */     this.positions = new short[count];
/* 43 */     this.states = new BlockState[count];
/*    */     
/* 45 */     for (int i = 0; i < count; i++) {
/* 46 */       long packedChange = input.readVarLong();
/* 47 */       this.positions[i] = (short)(int)(packedChange & 0xFFFL);
/* 48 */       this.states[i] = (BlockState)Block.BLOCK_STATE_REGISTRY.byId((int)(packedChange >>> 12));
/*    */     } 
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 53 */     SectionPos.STREAM_CODEC.encode(output, this.sectionPos);
/* 54 */     output.writeVarInt(this.positions.length);
/*    */     
/* 56 */     for (int i = 0; i < this.positions.length; i++) {
/* 57 */       output.writeVarLong(Block.getId(this.states[i]) << 12 | this.positions[i]);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public PacketType<ClientboundSectionBlocksUpdatePacket> type() { return GamePacketTypes.CLIENTBOUND_SECTION_BLOCKS_UPDATE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 68 */   public void handle(ClientGamePacketListener listener) { listener.handleChunkBlocksUpdate(this); }
/*    */ 
/*    */   
/*    */   public void runUpdates(BiConsumer<BlockPos, BlockState> updateFunction) {
/* 72 */     BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
/* 73 */     for (int i = 0; i < this.positions.length; i++) {
/* 74 */       short packedPos = this.positions[i];
/* 75 */       cursor.set(this.sectionPos.relativeToBlockX(packedPos), this.sectionPos.relativeToBlockY(packedPos), this.sectionPos.relativeToBlockZ(packedPos));
/* 76 */       updateFunction.accept(cursor, this.states[i]);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSectionBlocksUpdatePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
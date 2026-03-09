/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.BitSet;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.LightLayer;
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ 
/*    */ 
/*    */ public class ClientboundLightUpdatePacketData
/*    */ {
/* 19 */   private static final StreamCodec<ByteBuf, byte[]> DATA_LAYER_STREAM_CODEC = ByteBufCodecs.byteArray(2048);
/*    */   
/*    */   private final BitSet skyYMask;
/*    */   private final BitSet blockYMask;
/*    */   private final BitSet emptySkyYMask;
/*    */   private final BitSet emptyBlockYMask;
/*    */   private final List<byte[]> skyUpdates;
/*    */   private final List<byte[]> blockUpdates;
/*    */   
/*    */   public ClientboundLightUpdatePacketData(ChunkPos chunkPos, LevelLightEngine lightEngine, BitSet skyChangedLightSectionFilter, BitSet blockChangedLightSectionFilter) {
/* 29 */     this.skyYMask = new BitSet();
/* 30 */     this.blockYMask = new BitSet();
/* 31 */     this.emptySkyYMask = new BitSet();
/* 32 */     this.emptyBlockYMask = new BitSet();
/* 33 */     this.skyUpdates = Lists.newArrayList();
/* 34 */     this.blockUpdates = Lists.newArrayList();
/* 35 */     for (int sectionIndex = 0; sectionIndex < lightEngine.getLightSectionCount(); sectionIndex++) {
/* 36 */       if (skyChangedLightSectionFilter == null || skyChangedLightSectionFilter.get(sectionIndex)) {
/* 37 */         prepareSectionData(chunkPos, lightEngine, LightLayer.SKY, sectionIndex, this.skyYMask, this.emptySkyYMask, this.skyUpdates);
/*    */       }
/* 39 */       if (blockChangedLightSectionFilter == null || blockChangedLightSectionFilter.get(sectionIndex)) {
/* 40 */         prepareSectionData(chunkPos, lightEngine, LightLayer.BLOCK, sectionIndex, this.blockYMask, this.emptyBlockYMask, this.blockUpdates);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public ClientboundLightUpdatePacketData(FriendlyByteBuf input, int x, int z) {
/* 46 */     this.skyYMask = input.readBitSet();
/* 47 */     this.blockYMask = input.readBitSet();
/* 48 */     this.emptySkyYMask = input.readBitSet();
/* 49 */     this.emptyBlockYMask = input.readBitSet();
/* 50 */     this.skyUpdates = input.readList(DATA_LAYER_STREAM_CODEC);
/* 51 */     this.blockUpdates = input.readList(DATA_LAYER_STREAM_CODEC);
/*    */   }
/*    */   
/*    */   public void write(FriendlyByteBuf output) {
/* 55 */     output.writeBitSet(this.skyYMask);
/* 56 */     output.writeBitSet(this.blockYMask);
/* 57 */     output.writeBitSet(this.emptySkyYMask);
/* 58 */     output.writeBitSet(this.emptyBlockYMask);
/* 59 */     output.writeCollection(this.skyUpdates, DATA_LAYER_STREAM_CODEC);
/* 60 */     output.writeCollection(this.blockUpdates, DATA_LAYER_STREAM_CODEC);
/*    */   }
/*    */   
/*    */   private void prepareSectionData(ChunkPos pos, LevelLightEngine lightEngine, LightLayer layer, int sectionIndex, BitSet mask, BitSet emptyMask, List<byte[]> updates) {
/* 64 */     DataLayer data = lightEngine.getLayerListener(layer).getDataLayerData(SectionPos.of(pos, lightEngine.getMinLightSection() + sectionIndex));
/* 65 */     if (data != null) {
/* 66 */       if (data.isEmpty()) {
/* 67 */         emptyMask.set(sectionIndex);
/*    */       } else {
/* 69 */         mask.set(sectionIndex);
/* 70 */         updates.add(data.copy().getData());
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public BitSet getSkyYMask() { return this.skyYMask; }
/*    */ 
/*    */ 
/*    */   
/* 81 */   public BitSet getEmptySkyYMask() { return this.emptySkyYMask; }
/*    */ 
/*    */ 
/*    */   
/* 85 */   public List<byte[]> getSkyUpdates() { return this.skyUpdates; }
/*    */ 
/*    */ 
/*    */   
/* 89 */   public BitSet getBlockYMask() { return this.blockYMask; }
/*    */ 
/*    */ 
/*    */   
/* 93 */   public BitSet getEmptyBlockYMask() { return this.emptyBlockYMask; }
/*    */ 
/*    */ 
/*    */   
/* 97 */   public List<byte[]> getBlockUpdates() { return this.blockUpdates; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundLightUpdatePacketData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
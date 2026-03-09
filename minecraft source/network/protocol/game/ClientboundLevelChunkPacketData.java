/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ 
/*     */ public class ClientboundLevelChunkPacketData
/*     */ {
/*  28 */   private static final StreamCodec<ByteBuf, Map<Heightmap.Types, long[]>> HEIGHTMAPS_STREAM_CODEC = ByteBufCodecs.map(size -> 
/*  29 */       new EnumMap(Heightmap.Types.class), Heightmap.Types.STREAM_CODEC, ByteBufCodecs.LONG_ARRAY);
/*     */   
/*     */   private static final int TWO_MEGABYTES = 2097152;
/*     */   
/*     */   private final Map<Heightmap.Types, long[]> heightmaps;
/*     */   
/*     */   private final byte[] buffer;
/*     */   
/*     */   private final List<BlockEntityInfo> blockEntitiesData;
/*     */   
/*     */   public ClientboundLevelChunkPacketData(LevelChunk levelChunk) {
/*  40 */     this
/*     */       
/*  42 */       .heightmaps = (Map)levelChunk.getHeightmaps().stream().filter(entry -> ((Heightmap.Types)entry.getKey()).sendToClient()).collect(Collectors.toMap(Map.Entry::getKey, entry -> (long[])((Heightmap)entry.getValue()).getRawData().clone()));
/*     */ 
/*     */     
/*  45 */     this.buffer = new byte[calculateChunkSize(levelChunk)];
/*  46 */     extractChunkData(new FriendlyByteBuf(getWriteBuffer()), levelChunk);
/*     */     
/*  48 */     this.blockEntitiesData = Lists.newArrayList();
/*  49 */     for (Map.Entry<BlockPos, BlockEntity> entry : levelChunk.getBlockEntities().entrySet()) {
/*  50 */       this.blockEntitiesData.add(BlockEntityInfo.create((BlockEntity)entry.getValue()));
/*     */     }
/*     */   }
/*     */   
/*     */   public ClientboundLevelChunkPacketData(RegistryFriendlyByteBuf input, int x, int z) {
/*  55 */     this.heightmaps = (Map)HEIGHTMAPS_STREAM_CODEC.decode(input);
/*     */     
/*  57 */     int size = input.readVarInt();
/*  58 */     if (size > 2097152) {
/*  59 */       throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
/*     */     }
/*     */     
/*  62 */     this.buffer = new byte[size];
/*  63 */     input.readBytes(this.buffer);
/*     */     
/*  65 */     this.blockEntitiesData = (List)BlockEntityInfo.LIST_STREAM_CODEC.decode(input);
/*     */   }
/*     */   
/*     */   public void write(RegistryFriendlyByteBuf output) {
/*  69 */     HEIGHTMAPS_STREAM_CODEC.encode(output, this.heightmaps);
/*  70 */     output.writeVarInt(this.buffer.length);
/*  71 */     output.writeBytes(this.buffer);
/*     */     
/*  73 */     BlockEntityInfo.LIST_STREAM_CODEC.encode(output, this.blockEntitiesData);
/*     */   }
/*     */   
/*     */   private static int calculateChunkSize(LevelChunk chunk) {
/*  77 */     int total = 0;
/*     */     
/*  79 */     for (LevelChunkSection section : chunk.getSections()) {
/*  80 */       total += section.getSerializedSize();
/*     */     }
/*     */     
/*  83 */     return total;
/*     */   }
/*     */   
/*     */   private ByteBuf getWriteBuffer() {
/*  87 */     ByteBuf buffer = Unpooled.wrappedBuffer(this.buffer);
/*  88 */     buffer.writerIndex(0);
/*  89 */     return buffer;
/*     */   }
/*     */   
/*     */   public static void extractChunkData(FriendlyByteBuf buffer, LevelChunk chunk) {
/*  93 */     for (LevelChunkSection section : chunk.getSections()) {
/*  94 */       section.write(buffer);
/*     */     }
/*  96 */     if (buffer.writerIndex() != buffer.capacity()) {
/*  97 */       throw new IllegalStateException("Didn't fill chunk buffer: expected " + buffer.capacity() + " bytes, got " + buffer.writerIndex());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 102 */   public Consumer<BlockEntityTagOutput> getBlockEntitiesTagsConsumer(int x, int z) { return output -> getBlockEntitiesTags(output, x, z); }
/*     */ 
/*     */   
/*     */   private void getBlockEntitiesTags(BlockEntityTagOutput output, int x, int z) {
/* 106 */     int baseX = 16 * x;
/* 107 */     int baseZ = 16 * z;
/* 108 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 109 */     for (BlockEntityInfo data : this.blockEntitiesData) {
/* 110 */       int unpackedX = baseX + SectionPos.sectionRelative(data.packedXZ >> 4);
/* 111 */       int unpackedZ = baseZ + SectionPos.sectionRelative(data.packedXZ);
/* 112 */       pos.set(unpackedX, data.y, unpackedZ);
/* 113 */       output.accept(pos, data.type, data.tag);
/*     */     } 
/*     */   }
/*     */   
/* 117 */   public FriendlyByteBuf getReadBuffer() { return new FriendlyByteBuf(Unpooled.wrappedBuffer(this.buffer)); }
/*     */ 
/*     */ 
/*     */   
/* 121 */   public Map<Heightmap.Types, long[]> getHeightmaps() { return this.heightmaps; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class BlockEntityInfo
/*     */   {
/* 130 */     public static final StreamCodec<RegistryFriendlyByteBuf, BlockEntityInfo> STREAM_CODEC = StreamCodec.ofMember(BlockEntityInfo::write, BlockEntityInfo::new);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     public static final StreamCodec<RegistryFriendlyByteBuf, List<BlockEntityInfo>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
/*     */     
/*     */     private final int packedXZ;
/*     */     private final int y;
/*     */     private final BlockEntityType<?> type;
/*     */     private final CompoundTag tag;
/*     */     
/*     */     private BlockEntityInfo(int packedXZ, int y, BlockEntityType<?> type, CompoundTag tag) {
/* 143 */       this.packedXZ = packedXZ;
/* 144 */       this.y = y;
/* 145 */       this.type = type;
/* 146 */       this.tag = tag;
/*     */     }
/*     */     
/*     */     private BlockEntityInfo(RegistryFriendlyByteBuf input) {
/* 150 */       this.packedXZ = input.readByte();
/* 151 */       this.y = input.readShort();
/* 152 */       this.type = (BlockEntityType)ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE).decode(input);
/* 153 */       this.tag = input.readNbt();
/*     */     }
/*     */     
/*     */     private void write(RegistryFriendlyByteBuf output) {
/* 157 */       output.writeByte(this.packedXZ);
/* 158 */       output.writeShort(this.y);
/* 159 */       ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE).encode(output, this.type);
/* 160 */       output.writeNbt(this.tag);
/*     */     }
/*     */     
/*     */     private static BlockEntityInfo create(BlockEntity blockEntity) {
/* 164 */       CompoundTag tag = blockEntity.getUpdateTag(blockEntity.getLevel().registryAccess());
/* 165 */       BlockPos pos = blockEntity.getBlockPos();
/* 166 */       int xz = SectionPos.sectionRelative(pos.getX()) << 4 | SectionPos.sectionRelative(pos.getZ());
/* 167 */       return new BlockEntityInfo(xz, pos.getY(), blockEntity.getType(), tag.isEmpty() ? null : tag);
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface BlockEntityTagOutput {
/*     */     void accept(BlockPos param1BlockPos, BlockEntityType<?> param1BlockEntityType, CompoundTag param1CompoundTag);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundLevelChunkPacketData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
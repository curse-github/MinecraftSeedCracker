/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BlockEntityInfo
/*     */ {
/* 130 */   public static final StreamCodec<RegistryFriendlyByteBuf, BlockEntityInfo> STREAM_CODEC = StreamCodec.ofMember(BlockEntityInfo::write, BlockEntityInfo::new);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   public static final StreamCodec<RegistryFriendlyByteBuf, List<BlockEntityInfo>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
/*     */   
/*     */   private final int packedXZ;
/*     */   private final int y;
/*     */   private final BlockEntityType<?> type;
/*     */   private final CompoundTag tag;
/*     */   
/*     */   private BlockEntityInfo(int packedXZ, int y, BlockEntityType<?> type, CompoundTag tag) {
/* 143 */     this.packedXZ = packedXZ;
/* 144 */     this.y = y;
/* 145 */     this.type = type;
/* 146 */     this.tag = tag;
/*     */   }
/*     */   
/*     */   private BlockEntityInfo(RegistryFriendlyByteBuf input) {
/* 150 */     this.packedXZ = input.readByte();
/* 151 */     this.y = input.readShort();
/* 152 */     this.type = (BlockEntityType)ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE).decode(input);
/* 153 */     this.tag = input.readNbt();
/*     */   }
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf output) {
/* 157 */     output.writeByte(this.packedXZ);
/* 158 */     output.writeShort(this.y);
/* 159 */     ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE).encode(output, this.type);
/* 160 */     output.writeNbt(this.tag);
/*     */   }
/*     */   
/*     */   private static BlockEntityInfo create(BlockEntity blockEntity) {
/* 164 */     CompoundTag tag = blockEntity.getUpdateTag(blockEntity.getLevel().registryAccess());
/* 165 */     BlockPos pos = blockEntity.getBlockPos();
/* 166 */     int xz = SectionPos.sectionRelative(pos.getX()) << 4 | SectionPos.sectionRelative(pos.getZ());
/* 167 */     return new BlockEntityInfo(xz, pos.getY(), blockEntity.getType(), tag.isEmpty() ? null : tag);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundLevelChunkPacketData$BlockEntityInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
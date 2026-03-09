/*    */ package net.minecraft.network.protocol.game;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ 
/*    */ public class ClientboundBlockEntityDataPacket extends Object implements Packet<ClientGamePacketListener> {
/* 18 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBlockEntityDataPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ClientboundBlockEntityDataPacket::getPos, 
/*    */       
/* 20 */       ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE), ClientboundBlockEntityDataPacket::getType, ByteBufCodecs.TRUSTED_COMPOUND_TAG, ClientboundBlockEntityDataPacket::getTag, ClientboundBlockEntityDataPacket::new);
/*    */   
/*    */   private final BlockPos pos;
/*    */   
/*    */   private final BlockEntityType<?> type;
/*    */   
/*    */   private final CompoundTag tag;
/*    */ 
/*    */   
/*    */   public static ClientboundBlockEntityDataPacket create(BlockEntity blockEntity, BiFunction<BlockEntity, RegistryAccess, CompoundTag> updateTagSaver) {
/* 30 */     RegistryAccess registryAccess = blockEntity.getLevel().registryAccess();
/* 31 */     return new ClientboundBlockEntityDataPacket(blockEntity.getBlockPos(), blockEntity.getType(), (CompoundTag)updateTagSaver.apply(blockEntity, registryAccess));
/*    */   }
/*    */ 
/*    */   
/* 35 */   public static ClientboundBlockEntityDataPacket create(BlockEntity blockEntity) { return create(blockEntity, BlockEntity::getUpdateTag); }
/*    */ 
/*    */   
/*    */   private ClientboundBlockEntityDataPacket(BlockPos pos, BlockEntityType<?> type, CompoundTag tag) {
/* 39 */     this.pos = pos;
/* 40 */     this.type = type;
/* 41 */     this.tag = tag;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public PacketType<ClientboundBlockEntityDataPacket> type() { return GamePacketTypes.CLIENTBOUND_BLOCK_ENTITY_DATA; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public void handle(ClientGamePacketListener listener) { listener.handleBlockEntityData(this); }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public BlockPos getPos() { return this.pos; }
/*    */ 
/*    */ 
/*    */   
/* 59 */   public BlockEntityType<?> getType() { return this.type; }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public CompoundTag getTag() { return this.tag; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBlockEntityDataPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
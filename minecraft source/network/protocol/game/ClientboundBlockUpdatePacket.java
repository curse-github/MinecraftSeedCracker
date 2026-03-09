/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ClientboundBlockUpdatePacket extends Object implements Packet<ClientGamePacketListener> {
/* 15 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBlockUpdatePacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ClientboundBlockUpdatePacket::getPos, 
/*    */       
/* 17 */       ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY), ClientboundBlockUpdatePacket::getBlockState, ClientboundBlockUpdatePacket::new);
/*    */   
/*    */   private final BlockPos pos;
/*    */   
/*    */   private final BlockState blockState;
/*    */ 
/*    */   
/*    */   public ClientboundBlockUpdatePacket(BlockPos pos, BlockState state) {
/* 25 */     this.pos = pos;
/* 26 */     this.blockState = state;
/*    */   }
/*    */ 
/*    */   
/* 30 */   public ClientboundBlockUpdatePacket(BlockGetter level, BlockPos pos) { this(pos, level.getBlockState(pos)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public PacketType<ClientboundBlockUpdatePacket> type() { return GamePacketTypes.CLIENTBOUND_BLOCK_UPDATE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public void handle(ClientGamePacketListener listener) { listener.handleBlockUpdate(this); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public BlockState getBlockState() { return this.blockState; }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public BlockPos getPos() { return this.pos; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBlockUpdatePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
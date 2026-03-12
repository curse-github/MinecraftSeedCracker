/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockParticleOption implements ParticleOptions {
/* 13 */   private static final Codec<BlockState> BLOCK_STATE_CODEC = Codec.withAlternative(BlockState.CODEC, BuiltInRegistries.BLOCK
/*    */       
/* 15 */       .byNameCodec(), Block::defaultBlockState);
/*    */   private final ParticleType<BlockParticleOption> type;
/*    */   private final BlockState state;
/*    */   
/* 19 */   public static MapCodec<BlockParticleOption> codec(ParticleType<BlockParticleOption> type) { return BLOCK_STATE_CODEC.xmap(state -> new BlockParticleOption(type, state), o -> o.state).fieldOf("block_state"); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static StreamCodec<? super RegistryFriendlyByteBuf, BlockParticleOption> streamCodec(ParticleType<BlockParticleOption> type) { return ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY).map(state -> new BlockParticleOption(type, state), o -> o.state); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockParticleOption(ParticleType<BlockParticleOption> type, BlockState state) {
/* 30 */     this.type = type;
/* 31 */     this.state = state;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public ParticleType<BlockParticleOption> getType() { return this.type; }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public BlockState getState() { return this.state; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\BlockParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
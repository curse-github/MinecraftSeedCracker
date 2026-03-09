/*    */ package net.minecraft.world.level.chunk;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public final class PalettedContainerFactory extends Record {
/*    */   private final Strategy<BlockState> blockStatesStrategy;
/*    */   private final BlockState defaultBlockState;
/*    */   private final Codec<PalettedContainer<BlockState>> blockStatesContainerCodec;
/*    */   
/* 14 */   public PalettedContainerFactory(Strategy<BlockState> blockStatesStrategy, BlockState defaultBlockState, Codec<PalettedContainer<BlockState>> blockStatesContainerCodec, Strategy<Holder<Biome>> biomeStrategy, Holder<Biome> defaultBiome, Codec<PalettedContainerRO<Holder<Biome>>> biomeContainerCodec) { this.blockStatesStrategy = blockStatesStrategy; this.defaultBlockState = defaultBlockState; this.blockStatesContainerCodec = blockStatesContainerCodec; this.biomeStrategy = biomeStrategy; this.defaultBiome = defaultBiome; this.biomeContainerCodec = biomeContainerCodec; } private final Strategy<Holder<Biome>> biomeStrategy; private final Holder<Biome> defaultBiome; private final Codec<PalettedContainerRO<Holder<Biome>>> biomeContainerCodec; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/PalettedContainerFactory;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerFactory; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/PalettedContainerFactory;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerFactory; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/PalettedContainerFactory;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerFactory;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Strategy<BlockState> blockStatesStrategy() { return this.blockStatesStrategy; } public BlockState defaultBlockState() { return this.defaultBlockState; } public Codec<PalettedContainer<BlockState>> blockStatesContainerCodec() { return this.blockStatesContainerCodec; } public Strategy<Holder<Biome>> biomeStrategy() { return this.biomeStrategy; } public Holder<Biome> defaultBiome() { return this.defaultBiome; } public Codec<PalettedContainerRO<Holder<Biome>>> biomeContainerCodec() { return this.biomeContainerCodec; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static PalettedContainerFactory create(RegistryAccess registries) {
/* 24 */     Strategy<BlockState> blockStateStrategy = Strategy.createForBlockStates(Block.BLOCK_STATE_REGISTRY);
/* 25 */     BlockState defaultBlockState = Blocks.AIR.defaultBlockState();
/*    */     
/* 27 */     Registry<Biome> biomes = registries.lookupOrThrow(Registries.BIOME);
/*    */     
/* 29 */     Strategy<Holder<Biome>> biomeStrategy = Strategy.createForBiomes(biomes.asHolderIdMap());
/* 30 */     Holder.Reference<Biome> defaultBiome = biomes.getOrThrow(Biomes.PLAINS);
/*    */     
/* 32 */     return new PalettedContainerFactory(blockStateStrategy, defaultBlockState, 
/*    */ 
/*    */         
/* 35 */         PalettedContainer.codecRW(BlockState.CODEC, blockStateStrategy, defaultBlockState), biomeStrategy, defaultBiome, 
/*    */ 
/*    */ 
/*    */         
/* 39 */         PalettedContainer.codecRO(biomes.holderByNameCodec(), biomeStrategy, defaultBiome));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public PalettedContainer<BlockState> createForBlockStates() { return new PalettedContainer(this.defaultBlockState, this.blockStatesStrategy); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public PalettedContainer<Holder<Biome>> createForBiomes() { return new PalettedContainer(this.defaultBiome, this.biomeStrategy); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\PalettedContainerFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
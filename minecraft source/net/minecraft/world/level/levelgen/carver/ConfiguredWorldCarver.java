/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.chunk.CarvingMask;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.levelgen.Aquifer;
/*    */ 
/*    */ public final class ConfiguredWorldCarver<WC extends CarverConfiguration> extends Record {
/*    */   private final WorldCarver<WC> worldCarver;
/*    */   private final WC config;
/*    */   
/* 21 */   public ConfiguredWorldCarver(WorldCarver<WC> worldCarver, WC config) { this.worldCarver = worldCarver; this.config = config; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/carver/ConfiguredWorldCarver;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/carver/ConfiguredWorldCarver;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 21 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/carver/ConfiguredWorldCarver<TWC;>; } public WorldCarver<WC> worldCarver() { return this.worldCarver; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/carver/ConfiguredWorldCarver;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/carver/ConfiguredWorldCarver;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/carver/ConfiguredWorldCarver<TWC;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/carver/ConfiguredWorldCarver;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/carver/ConfiguredWorldCarver;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 21 */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/carver/ConfiguredWorldCarver<TWC;>; } public WC config() { return (WC)this.config; }
/* 22 */   public static final Codec<ConfiguredWorldCarver<?>> DIRECT_CODEC = BuiltInRegistries.CARVER.byNameCodec().dispatch(c -> c.worldCarver, WorldCarver::configuredCodec);
/*    */   
/* 24 */   public static final Codec<Holder<ConfiguredWorldCarver<?>>> CODEC = RegistryFileCodec.create(Registries.CONFIGURED_CARVER, DIRECT_CODEC);
/* 25 */   public static final Codec<HolderSet<ConfiguredWorldCarver<?>>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.CONFIGURED_CARVER, DIRECT_CODEC);
/*    */ 
/*    */   
/* 28 */   public boolean isStartChunk(RandomSource random) { return this.worldCarver.isStartChunk(this.config, random); }
/*    */ 
/*    */   
/*    */   public boolean carve(CarvingContext context, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> biomeGetter, RandomSource random, Aquifer aquifer, ChunkPos sourceChunkPos, CarvingMask mask) {
/* 32 */     if (SharedConstants.debugVoidTerrain(chunk.getPos())) {
/* 33 */       return false;
/*    */     }
/* 35 */     return this.worldCarver.carve(context, this.config, chunk, biomeGetter, random, aquifer, sourceChunkPos, mask);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\carver\ConfiguredWorldCarver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
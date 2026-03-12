/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.NoiseChunk;
/*    */ import net.minecraft.world.level.levelgen.RandomState;
/*    */ import net.minecraft.world.level.levelgen.SurfaceRules;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public class CarvingContext
/*    */   extends WorldGenerationContext {
/*    */   private final RegistryAccess registryAccess;
/*    */   private final NoiseChunk noiseChunk;
/*    */   private final RandomState randomState;
/*    */   private final SurfaceRules.RuleSource surfaceRule;
/*    */   
/*    */   public CarvingContext(NoiseBasedChunkGenerator generator, RegistryAccess registryAccess, LevelHeightAccessor heightAccessor, NoiseChunk noiseChunk, RandomState randomState, SurfaceRules.RuleSource surfaceRule) {
/* 26 */     super(generator, heightAccessor);
/* 27 */     this.registryAccess = registryAccess;
/* 28 */     this.noiseChunk = noiseChunk;
/* 29 */     this.randomState = randomState;
/* 30 */     this.surfaceRule = surfaceRule;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/* 38 */   public Optional<BlockState> topMaterial(Function<BlockPos, Holder<Biome>> biomeGetter, ChunkAccess chunk, BlockPos pos, boolean underFluid) { return this.randomState.surfaceSystem().topMaterial(this.surfaceRule, this, biomeGetter, chunk, this.noiseChunk, pos, underFluid); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/* 46 */   public RegistryAccess registryAccess() { return this.registryAccess; }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public RandomState randomState() { return this.randomState; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\carver\CarvingContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
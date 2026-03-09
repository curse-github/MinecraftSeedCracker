/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.placement.VegetationPlacements;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class GrassBlock extends SpreadingSnowyDirtBlock implements BonemealableBlock {
/* 21 */   public static final MapCodec<GrassBlock> CODEC = simpleCodec(GrassBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 25 */   public MapCodec<GrassBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public GrassBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return level.getBlockState(pos.above()).isAir(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 44 */     BlockPos above = pos.above();
/*    */     
/* 46 */     BlockState grass = Blocks.SHORT_GRASS.defaultBlockState();
/*    */     
/* 48 */     Optional<Holder.Reference<PlacedFeature>> grassFeature = level.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE).get(VegetationPlacements.GRASS_BONEMEAL);
/*    */     
/*    */     int j;
/* 51 */     label34: for (j = 0; j < 128; j++) {
/* 52 */       Holder<PlacedFeature> placementFeature; BlockPos testPos = above;
/* 53 */       for (int i = 0; i < j / 16; ) {
/* 54 */         testPos = testPos.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
/* 55 */         if (level.getBlockState(testPos.below()).is(this)) { if (level.getBlockState(testPos).isCollisionShapeFullBlock(level, testPos))
/*    */             continue label34; 
/*    */           i++; }
/*    */         
/*    */         continue label34;
/*    */       } 
/* 61 */       BlockState testState = level.getBlockState(testPos);
/* 62 */       if (testState.is(grass.getBlock()) && random.nextInt(10) == 0) {
/* 63 */         placementFeature = (BonemealableBlock)grass.getBlock();
/* 64 */         if (placementFeature.isValidBonemealTarget(level, testPos, testState)) {
/* 65 */           placementFeature.performBonemeal(level, random, testPos, testState);
/*    */         }
/*    */       } 
/*    */       
/* 69 */       if (!testState.isAir()) {
/*    */         continue;
/*    */       }
/*    */ 
/*    */       
/* 74 */       if (random.nextInt(8) == 0) {
/* 75 */         List<ConfiguredFeature<?, ?>> features = ((Biome)level.getBiome(testPos).value()).getGenerationSettings().getFlowerFeatures();
/* 76 */         if (features.isEmpty()) {
/*    */           continue;
/*    */         }
/*    */         
/* 80 */         int randomFlowerFeature = random.nextInt(features.size());
/* 81 */         placementFeature = ((RandomPatchConfiguration)((ConfiguredFeature)features.get(randomFlowerFeature)).config()).feature();
/* 82 */       } else if (grassFeature.isPresent()) {
/* 83 */         placementFeature = (Holder)grassFeature.get();
/*    */       } else {
/*    */         continue;
/*    */       } 
/*    */       
/* 88 */       ((PlacedFeature)placementFeature.value()).place(level, level.getChunkSource().getGenerator(), random, testPos);
/*    */       continue;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 94 */   public BonemealableBlock.Type getType() { return BonemealableBlock.Type.NEIGHBOR_SPREADER; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\GrassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
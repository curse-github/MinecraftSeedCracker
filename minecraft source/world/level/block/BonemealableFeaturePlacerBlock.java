/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public class BonemealableFeaturePlacerBlock extends Block implements BonemealableBlock {
/* 16 */   public static final MapCodec<BonemealableFeaturePlacerBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 17 */         ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(()), 
/* 18 */         propertiesCodec())
/* 19 */       .apply(i, BonemealableFeaturePlacerBlock::new));
/*    */ 
/*    */   
/*    */   private final ResourceKey<ConfiguredFeature<?, ?>> feature;
/*    */ 
/*    */   
/* 25 */   public MapCodec<BonemealableFeaturePlacerBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/*    */   public BonemealableFeaturePlacerBlock(ResourceKey<ConfiguredFeature<?, ?>> feature, BlockBehaviour.Properties properties) {
/* 29 */     super(properties);
/* 30 */     this.feature = feature;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return level.getBlockState(pos.above()).isAir(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { level.registryAccess()
/* 46 */       .lookup(Registries.CONFIGURED_FEATURE)
/* 47 */       .flatMap(registry -> registry.get(this.feature))
/* 48 */       .ifPresent(mossPatch -> ((ConfiguredFeature)mossPatch.value()).place(level, level.getChunkSource().getGenerator(), random, pos.above())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public BonemealableBlock.Type getType() { return BonemealableBlock.Type.NEIGHBOR_SPREADER; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BonemealableFeaturePlacerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
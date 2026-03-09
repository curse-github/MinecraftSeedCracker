/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class FungusBlock extends VegetationBlock implements BonemealableBlock {
/* 24 */   public static final MapCodec<FungusBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 25 */         ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(()), BuiltInRegistries.BLOCK
/* 26 */         .byNameCodec().fieldOf("grows_on").forGetter(()), 
/* 27 */         propertiesCodec())
/* 28 */       .apply(i, FungusBlock::new));
/*    */   
/*    */   private static final double BONEMEAL_SUCCESS_PROBABILITY = 0.4D;
/*    */   
/* 32 */   public MapCodec<FungusBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 9.0D);
/*    */   
/*    */   private final Block requiredBlock;
/*    */   private final ResourceKey<ConfiguredFeature<?, ?>> feature;
/*    */   
/*    */   protected FungusBlock(ResourceKey<ConfiguredFeature<?, ?>> feature, Block requiredBlock, BlockBehaviour.Properties properties) {
/* 43 */     super(properties);
/* 44 */     this.feature = feature;
/* 45 */     this.requiredBlock = requiredBlock;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 50 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) { return (state.is(BlockTags.NYLIUM) || state.is(Blocks.MYCELIUM) || state.is(Blocks.SOUL_SOIL) || super.mayPlaceOn(state, level, pos)); }
/*    */ 
/*    */ 
/*    */   
/* 59 */   private Optional<? extends Holder<ConfiguredFeature<?, ?>>> getFeature(LevelReader level) { return level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(this.feature); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 65 */     BlockState belowState = level.getBlockState(pos.below());
/* 66 */     return belowState.is(this.requiredBlock);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return (random.nextFloat() < 0.4D); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { getFeature(level).ifPresent(feature -> ((ConfiguredFeature)feature.value()).place(level, level.getChunkSource().getGenerator(), random, pos)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\FungusBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
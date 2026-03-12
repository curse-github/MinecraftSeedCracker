/*    */ package net.minecraft.world.level.levelgen.feature.rootplacers;
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public abstract class RootPlacer {
/* 22 */   public static final Codec<RootPlacer> CODEC = BuiltInRegistries.ROOT_PLACER_TYPE.byNameCodec().dispatch(RootPlacer::type, RootPlacerType::codec);
/*    */   
/*    */   protected final IntProvider trunkOffsetY;
/*    */   protected final BlockStateProvider rootProvider;
/*    */   protected final Optional<AboveRootPlacement> aboveRootPlacement;
/*    */   
/*    */   protected static <P extends RootPlacer> Products.P3<RecordCodecBuilder.Mu<P>, IntProvider, BlockStateProvider, Optional<AboveRootPlacement>> rootPlacerParts(RecordCodecBuilder.Instance<P> instance) {
/* 29 */     return instance.group(IntProvider.CODEC
/* 30 */         .fieldOf("trunk_offset_y").forGetter(c -> c.trunkOffsetY), BlockStateProvider.CODEC
/* 31 */         .fieldOf("root_provider").forGetter(c -> c.rootProvider), AboveRootPlacement.CODEC
/* 32 */         .optionalFieldOf("above_root_placement").forGetter(c -> c.aboveRootPlacement));
/*    */   }
/*    */ 
/*    */   
/*    */   public RootPlacer(IntProvider trunkOffsetY, BlockStateProvider rootProvider, Optional<AboveRootPlacement> aboveRootPlacement) {
/* 37 */     this.trunkOffsetY = trunkOffsetY;
/* 38 */     this.rootProvider = rootProvider;
/* 39 */     this.aboveRootPlacement = aboveRootPlacement;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   protected boolean canPlaceRoot(LevelSimulatedReader level, BlockPos pos) { return TreeFeature.validTreePos(level, pos); }
/*    */ 
/*    */   
/*    */   protected void placeRoot(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> rootSetter, RandomSource random, BlockPos pos, TreeConfiguration config) {
/* 51 */     if (!canPlaceRoot(level, pos)) {
/*    */       return;
/*    */     }
/* 54 */     rootSetter.accept(pos, getPotentiallyWaterloggedState(level, pos, this.rootProvider.getState(random, pos)));
/* 55 */     if (this.aboveRootPlacement.isPresent()) {
/* 56 */       AboveRootPlacement abovePlacement = (AboveRootPlacement)this.aboveRootPlacement.get();
/* 57 */       BlockPos above = pos.above();
/* 58 */       if (random.nextFloat() < abovePlacement.aboveRootPlacementChance() && level.isStateAtPosition(above, BlockBehaviour.BlockStateBase::isAir)) {
/* 59 */         rootSetter.accept(above, getPotentiallyWaterloggedState(level, above, abovePlacement.aboveRootProvider().getState(random, above)));
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   protected BlockState getPotentiallyWaterloggedState(LevelSimulatedReader level, BlockPos pos, BlockState state) {
/* 65 */     if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
/* 66 */       boolean waterlogged = level.isFluidAtPosition(pos, s -> s.is(FluidTags.WATER));
/* 67 */       return (BlockState)state.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(waterlogged));
/*    */     } 
/* 69 */     return state;
/*    */   }
/*    */ 
/*    */   
/* 73 */   public BlockPos getTrunkOrigin(BlockPos origin, RandomSource random) { return origin.above(this.trunkOffsetY.sample(random)); }
/*    */   
/*    */   protected abstract RootPlacerType<?> type();
/*    */   
/*    */   public abstract boolean placeRoots(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, BlockPos paramBlockPos1, BlockPos paramBlockPos2, TreeConfiguration paramTreeConfiguration);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\rootplacers\RootPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
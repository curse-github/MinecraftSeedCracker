/*     */ package net.minecraft.world.level.levelgen.feature.configurations;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
/*     */ 
/*     */ public class TreeConfiguration implements FeatureConfiguration {
/*  18 */   public static final Codec<TreeConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(BlockStateProvider.CODEC
/*  19 */         .fieldOf("trunk_provider").forGetter(()), TrunkPlacer.CODEC
/*  20 */         .fieldOf("trunk_placer").forGetter(()), BlockStateProvider.CODEC
/*  21 */         .fieldOf("foliage_provider").forGetter(()), FoliagePlacer.CODEC
/*  22 */         .fieldOf("foliage_placer").forGetter(()), RootPlacer.CODEC
/*  23 */         .optionalFieldOf("root_placer").forGetter(()), BlockStateProvider.CODEC
/*  24 */         .fieldOf("dirt_provider").forGetter(()), FeatureSize.CODEC
/*  25 */         .fieldOf("minimum_size").forGetter(()), TreeDecorator.CODEC
/*  26 */         .listOf().fieldOf("decorators").forGetter(()), Codec.BOOL
/*  27 */         .fieldOf("ignore_vines").orElse(Boolean.valueOf(false)).forGetter(()), Codec.BOOL
/*  28 */         .fieldOf("force_dirt").orElse(Boolean.valueOf(false)).forGetter(()))
/*  29 */       .apply(i, TreeConfiguration::new));
/*     */   
/*     */   public final BlockStateProvider trunkProvider;
/*     */   public final BlockStateProvider dirtProvider;
/*     */   public final TrunkPlacer trunkPlacer;
/*     */   public final BlockStateProvider foliageProvider;
/*     */   public final FoliagePlacer foliagePlacer;
/*     */   public final Optional<RootPlacer> rootPlacer;
/*     */   public final FeatureSize minimumSize;
/*     */   public final List<TreeDecorator> decorators;
/*     */   public final boolean ignoreVines;
/*     */   public final boolean forceDirt;
/*     */   
/*     */   protected TreeConfiguration(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, Optional<RootPlacer> rootPlacer, BlockStateProvider dirtProvider, FeatureSize minimumSize, List<TreeDecorator> decorators, boolean ignoreVines, boolean forceDirt) {
/*  43 */     this.trunkProvider = trunkProvider;
/*  44 */     this.trunkPlacer = trunkPlacer;
/*  45 */     this.foliageProvider = foliageProvider;
/*  46 */     this.foliagePlacer = foliagePlacer;
/*  47 */     this.rootPlacer = rootPlacer;
/*  48 */     this.dirtProvider = dirtProvider;
/*  49 */     this.minimumSize = minimumSize;
/*  50 */     this.decorators = decorators;
/*  51 */     this.ignoreVines = ignoreVines;
/*  52 */     this.forceDirt = forceDirt;
/*     */   }
/*     */   
/*     */   public static class TreeConfigurationBuilder {
/*     */     public final BlockStateProvider trunkProvider;
/*     */     private final TrunkPlacer trunkPlacer;
/*     */     public final BlockStateProvider foliageProvider;
/*     */     private final FoliagePlacer foliagePlacer;
/*     */     private final Optional<RootPlacer> rootPlacer;
/*     */     
/*     */     public TreeConfigurationBuilder(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, Optional<RootPlacer> rootPlacer, FeatureSize minimumSize) {
/*  63 */       this.decorators = ImmutableList.of();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  68 */       this.trunkProvider = trunkProvider;
/*  69 */       this.trunkPlacer = trunkPlacer;
/*  70 */       this.foliageProvider = foliageProvider;
/*  71 */       this.dirtProvider = BlockStateProvider.simple(Blocks.DIRT);
/*  72 */       this.foliagePlacer = foliagePlacer;
/*  73 */       this.rootPlacer = rootPlacer;
/*  74 */       this.minimumSize = minimumSize;
/*     */     }
/*     */     private BlockStateProvider dirtProvider; private final FeatureSize minimumSize; private List<TreeDecorator> decorators; private boolean ignoreVines; private boolean forceDirt;
/*     */     
/*  78 */     public TreeConfigurationBuilder(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, FeatureSize minimumSize) { this(trunkProvider, trunkPlacer, foliageProvider, foliagePlacer, Optional.empty(), minimumSize); }
/*     */ 
/*     */     
/*     */     public TreeConfigurationBuilder dirt(BlockStateProvider dirtProvider) {
/*  82 */       this.dirtProvider = dirtProvider;
/*  83 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder decorators(List<TreeDecorator> decorators) {
/*  87 */       this.decorators = decorators;
/*  88 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder ignoreVines() {
/*  92 */       this.ignoreVines = true;
/*  93 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder forceDirt() {
/*  97 */       this.forceDirt = true;
/*  98 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 102 */     public TreeConfiguration build() { return new TreeConfiguration(this.trunkProvider, this.trunkPlacer, this.foliageProvider, this.foliagePlacer, this.rootPlacer, this.dirtProvider, this.minimumSize, this.decorators, this.ignoreVines, this.forceDirt); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\TreeConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
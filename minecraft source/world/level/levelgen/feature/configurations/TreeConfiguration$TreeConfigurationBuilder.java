/*     */ package net.minecraft.world.level.levelgen.feature.configurations;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeConfigurationBuilder
/*     */ {
/*     */   public final BlockStateProvider trunkProvider;
/*     */   private final TrunkPlacer trunkPlacer;
/*     */   public final BlockStateProvider foliageProvider;
/*     */   private final FoliagePlacer foliagePlacer;
/*     */   private final Optional<RootPlacer> rootPlacer;
/*     */   private BlockStateProvider dirtProvider;
/*     */   private final FeatureSize minimumSize;
/*     */   private List<TreeDecorator> decorators;
/*     */   private boolean ignoreVines;
/*     */   private boolean forceDirt;
/*     */   
/*     */   public TreeConfigurationBuilder(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, Optional<RootPlacer> rootPlacer, FeatureSize minimumSize) {
/*  63 */     this.decorators = ImmutableList.of();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     this.trunkProvider = trunkProvider;
/*  69 */     this.trunkPlacer = trunkPlacer;
/*  70 */     this.foliageProvider = foliageProvider;
/*  71 */     this.dirtProvider = BlockStateProvider.simple(Blocks.DIRT);
/*  72 */     this.foliagePlacer = foliagePlacer;
/*  73 */     this.rootPlacer = rootPlacer;
/*  74 */     this.minimumSize = minimumSize;
/*     */   }
/*     */ 
/*     */   
/*  78 */   public TreeConfigurationBuilder(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, FeatureSize minimumSize) { this(trunkProvider, trunkPlacer, foliageProvider, foliagePlacer, Optional.empty(), minimumSize); }
/*     */ 
/*     */   
/*     */   public TreeConfigurationBuilder dirt(BlockStateProvider dirtProvider) {
/*  82 */     this.dirtProvider = dirtProvider;
/*  83 */     return this;
/*     */   }
/*     */   
/*     */   public TreeConfigurationBuilder decorators(List<TreeDecorator> decorators) {
/*  87 */     this.decorators = decorators;
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public TreeConfigurationBuilder ignoreVines() {
/*  92 */     this.ignoreVines = true;
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public TreeConfigurationBuilder forceDirt() {
/*  97 */     this.forceDirt = true;
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 102 */   public TreeConfiguration build() { return new TreeConfiguration(this.trunkProvider, this.trunkPlacer, this.foliageProvider, this.foliagePlacer, this.rootPlacer, this.dirtProvider, this.minimumSize, this.decorators, this.ignoreVines, this.forceDirt); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\TreeConfiguration$TreeConfigurationBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
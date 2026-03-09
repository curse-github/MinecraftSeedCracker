/*     */ package net.minecraft.world.level.block.grower;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.features.TreeFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ 
/*     */ public final class TreeGrower {
/*     */   public static final Codec<TreeGrower> CODEC;
/*     */   public static final TreeGrower OAK;
/*     */   public static final TreeGrower SPRUCE;
/*     */   public static final TreeGrower MANGROVE;
/*     */   public static final TreeGrower AZALEA;
/*  26 */   private static final Map<String, TreeGrower> GROWERS = new Object2ObjectArrayMap(); public static final TreeGrower BIRCH; public static final TreeGrower JUNGLE; static  {
/*  27 */     Objects.requireNonNull(GROWERS); CODEC = Codec.stringResolver(g -> g.name, GROWERS::get);
/*     */     
/*  29 */     OAK = new TreeGrower("oak", 0.1F, Optional.empty(), Optional.empty(), Optional.of(TreeFeatures.OAK), Optional.of(TreeFeatures.FANCY_OAK), Optional.of(TreeFeatures.OAK_BEES_005), Optional.of(TreeFeatures.FANCY_OAK_BEES_005));
/*  30 */     SPRUCE = new TreeGrower("spruce", 0.5F, Optional.of(TreeFeatures.MEGA_SPRUCE), Optional.of(TreeFeatures.MEGA_PINE), Optional.of(TreeFeatures.SPRUCE), Optional.empty(), Optional.empty(), Optional.empty());
/*  31 */     MANGROVE = new TreeGrower("mangrove", 0.85F, Optional.empty(), Optional.empty(), Optional.of(TreeFeatures.MANGROVE), Optional.of(TreeFeatures.TALL_MANGROVE), Optional.empty(), Optional.empty());
/*     */     
/*  33 */     AZALEA = new TreeGrower("azalea", Optional.empty(), Optional.of(TreeFeatures.AZALEA_TREE), Optional.empty());
/*  34 */     BIRCH = new TreeGrower("birch", Optional.empty(), Optional.of(TreeFeatures.BIRCH), Optional.of(TreeFeatures.BIRCH_BEES_005));
/*  35 */     JUNGLE = new TreeGrower("jungle", Optional.of(TreeFeatures.MEGA_JUNGLE_TREE), Optional.of(TreeFeatures.JUNGLE_TREE_NO_VINE), Optional.empty());
/*  36 */     ACACIA = new TreeGrower("acacia", Optional.empty(), Optional.of(TreeFeatures.ACACIA), Optional.empty());
/*  37 */     CHERRY = new TreeGrower("cherry", Optional.empty(), Optional.of(TreeFeatures.CHERRY), Optional.of(TreeFeatures.CHERRY_BEES_005));
/*  38 */     DARK_OAK = new TreeGrower("dark_oak", Optional.of(TreeFeatures.DARK_OAK), Optional.empty(), Optional.empty());
/*  39 */     PALE_OAK = new TreeGrower("pale_oak", Optional.of(TreeFeatures.PALE_OAK_BONEMEAL), Optional.empty(), Optional.empty());
/*     */   }
/*     */   public static final TreeGrower ACACIA; public static final TreeGrower CHERRY; public static final TreeGrower DARK_OAK; public static final TreeGrower PALE_OAK;
/*     */   private final String name;
/*     */   private final float secondaryChance;
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> megaTree;
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryMegaTree;
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree;
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryTree;
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> flowers;
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryFlowers;
/*     */   
/*  51 */   public TreeGrower(String name, Optional<ResourceKey<ConfiguredFeature<?, ?>>> megaTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> flowers) { this(name, 0.0F, megaTree, Optional.empty(), tree, Optional.empty(), flowers, Optional.empty()); }
/*     */ 
/*     */   
/*     */   public TreeGrower(String name, float secondaryChance, Optional<ResourceKey<ConfiguredFeature<?, ?>>> megaTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryMegaTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> flowers, Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryFlowers) {
/*  55 */     this.name = name;
/*  56 */     this.secondaryChance = secondaryChance;
/*  57 */     this.megaTree = megaTree;
/*  58 */     this.secondaryMegaTree = secondaryMegaTree;
/*  59 */     this.tree = tree;
/*  60 */     this.secondaryTree = secondaryTree;
/*  61 */     this.flowers = flowers;
/*  62 */     this.secondaryFlowers = secondaryFlowers;
/*     */     
/*  64 */     GROWERS.put(name, this);
/*     */   }
/*     */   
/*     */   private ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
/*  68 */     if (random.nextFloat() < this.secondaryChance) {
/*  69 */       if (hasFlowers && this.secondaryFlowers.isPresent()) {
/*  70 */         return (ResourceKey)this.secondaryFlowers.get();
/*     */       }
/*  72 */       if (this.secondaryTree.isPresent()) {
/*  73 */         return (ResourceKey)this.secondaryTree.get();
/*     */       }
/*     */     } 
/*  76 */     if (hasFlowers && this.flowers.isPresent()) {
/*  77 */       return (ResourceKey)this.flowers.get();
/*     */     }
/*  79 */     return (ResourceKey)this.tree.orElse(null);
/*     */   }
/*     */   
/*     */   private ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {
/*  83 */     if (this.secondaryMegaTree.isPresent() && random.nextFloat() < this.secondaryChance) {
/*  84 */       return (ResourceKey)this.secondaryMegaTree.get();
/*     */     }
/*  86 */     return (ResourceKey)this.megaTree.orElse(null);
/*     */   }
/*     */   
/*     */   public boolean growTree(ServerLevel level, ChunkGenerator generator, BlockPos pos, BlockState state, RandomSource random) {
/*  90 */     ResourceKey<ConfiguredFeature<?, ?>> megaFeatureKey = getConfiguredMegaFeature(random);
/*  91 */     if (megaFeatureKey != null) {
/*  92 */       Holder<ConfiguredFeature<?, ?>> featureHolder = (Holder)level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(megaFeatureKey).orElse(null);
/*  93 */       if (featureHolder != null) {
/*  94 */         for (int dx = 0; dx >= -1; dx--) {
/*  95 */           for (int dz = 0; dz >= -1; dz--) {
/*  96 */             if (isTwoByTwoSapling(state, level, pos, dx, dz)) {
/*  97 */               ConfiguredFeature<?, ?> feature = (ConfiguredFeature)featureHolder.value();
/*     */               
/*  99 */               BlockState air = Blocks.AIR.defaultBlockState();
/* 100 */               level.setBlock(pos.offset(dx, 0, dz), air, 260);
/* 101 */               level.setBlock(pos.offset(dx + 1, 0, dz), air, 260);
/* 102 */               level.setBlock(pos.offset(dx, 0, dz + 1), air, 260);
/* 103 */               level.setBlock(pos.offset(dx + 1, 0, dz + 1), air, 260);
/*     */               
/* 105 */               if (feature.place(level, generator, random, pos.offset(dx, 0, dz))) {
/* 106 */                 return true;
/*     */               }
/* 108 */               level.setBlock(pos.offset(dx, 0, dz), state, 260);
/* 109 */               level.setBlock(pos.offset(dx + 1, 0, dz), state, 260);
/* 110 */               level.setBlock(pos.offset(dx, 0, dz + 1), state, 260);
/* 111 */               level.setBlock(pos.offset(dx + 1, 0, dz + 1), state, 260);
/* 112 */               return false;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 119 */     ResourceKey<ConfiguredFeature<?, ?>> featureKey = getConfiguredFeature(random, hasFlowers(level, pos));
/* 120 */     if (featureKey == null) {
/* 121 */       return false;
/*     */     }
/*     */     
/* 124 */     Holder<ConfiguredFeature<?, ?>> featureHolder = (Holder)level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(featureKey).orElse(null);
/* 125 */     if (featureHolder == null) {
/* 126 */       return false;
/*     */     }
/*     */     
/* 129 */     ConfiguredFeature<?, ?> feature = (ConfiguredFeature)featureHolder.value();
/*     */     
/* 131 */     BlockState emptyBlock = level.getFluidState(pos).createLegacyBlock();
/* 132 */     level.setBlock(pos, emptyBlock, 260);
/*     */     
/* 134 */     if (feature.place(level, generator, random, pos)) {
/* 135 */       if (level.getBlockState(pos) == emptyBlock) {
/* 136 */         level.sendBlockUpdated(pos, state, emptyBlock, 2);
/*     */       }
/* 138 */       return true;
/*     */     } 
/*     */     
/* 141 */     level.setBlock(pos, state, 260);
/* 142 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isTwoByTwoSapling(BlockState state, BlockGetter level, BlockPos pos, int ox, int oz) {
/* 146 */     Block block = state.getBlock();
/* 147 */     return (level.getBlockState(pos.offset(ox, 0, oz)).is(block) && level
/* 148 */       .getBlockState(pos.offset(ox + 1, 0, oz)).is(block) && level
/* 149 */       .getBlockState(pos.offset(ox, 0, oz + 1)).is(block) && level
/* 150 */       .getBlockState(pos.offset(ox + 1, 0, oz + 1)).is(block));
/*     */   }
/*     */   
/*     */   private boolean hasFlowers(LevelAccessor level, BlockPos pos) {
/* 154 */     for (BlockPos p : BlockPos.MutableBlockPos.betweenClosed(pos.below().north(2).west(2), pos.above().south(2).east(2))) {
/* 155 */       if (level.getBlockState(p).is(BlockTags.FLOWERS)) {
/* 156 */         return true;
/*     */       }
/*     */     } 
/* 159 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\grower\TreeGrower.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
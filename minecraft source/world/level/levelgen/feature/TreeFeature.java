/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LeavesBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
/*     */ import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
/*     */ 
/*     */ public class TreeFeature extends Feature<TreeConfiguration> {
/*     */   @UpdateFlags
/*     */   private static final int BLOCK_UPDATE_FLAGS = 19;
/*     */   
/*  39 */   public TreeFeature(Codec<TreeConfiguration> codec) { super(codec); }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public static boolean isVine(LevelSimulatedReader level, BlockPos pos) { return level.isStateAtPosition(pos, state -> state.is(Blocks.VINE)); }
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static boolean isAirOrLeaves(LevelSimulatedReader level, BlockPos pos) { return level.isStateAtPosition(pos, state -> (state.isAir() || state.is(BlockTags.LEAVES))); }
/*     */ 
/*     */ 
/*     */   
/*  51 */   private static void setBlockKnownShape(LevelWriter level, BlockPos pos, BlockState blockState) { level.setBlock(pos, blockState, 19); }
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static boolean validTreePos(LevelSimulatedReader level, BlockPos pos) { return level.isStateAtPosition(pos, state -> (state.isAir() || state.is(BlockTags.REPLACEABLE_BY_TREES))); }
/*     */ 
/*     */   
/*     */   private boolean doPlace(WorldGenLevel level, RandomSource random, BlockPos origin, BiConsumer<BlockPos, BlockState> rootSetter, BiConsumer<BlockPos, BlockState> trunkSetter, FoliagePlacer.FoliageSetter foliageSetter, TreeConfiguration config) {
/*  59 */     int treeHeight = config.trunkPlacer.getTreeHeight(random);
/*  60 */     int foliageHeight = config.foliagePlacer.foliageHeight(random, treeHeight, config);
/*  61 */     int trunkHeight = treeHeight - foliageHeight;
/*     */     
/*  63 */     int leafRadius = config.foliagePlacer.foliageRadius(random, trunkHeight);
/*     */     
/*  65 */     BlockPos trunkOrigin = (BlockPos)config.rootPlacer.map(rootPlacer -> rootPlacer.getTrunkOrigin(origin, random)).orElse(origin);
/*     */     
/*  67 */     int minY = Math.min(origin.getY(), trunkOrigin.getY());
/*  68 */     int maxY = Math.max(origin.getY(), trunkOrigin.getY()) + treeHeight + 1;
/*  69 */     if (minY < level.getMinY() + 1 || maxY > level.getMaxY() + 1) {
/*  70 */       return false;
/*     */     }
/*     */     
/*  73 */     OptionalInt minClippedHeight = config.minimumSize.minClippedHeight();
/*     */     
/*  75 */     int clippedTreeHeight = getMaxFreeTreeHeight(level, treeHeight, trunkOrigin, config);
/*  76 */     if (clippedTreeHeight < treeHeight && (minClippedHeight.isEmpty() || clippedTreeHeight < minClippedHeight.getAsInt())) {
/*  77 */       return false;
/*     */     }
/*     */     
/*  80 */     if (config.rootPlacer.isPresent() && 
/*  81 */       !((RootPlacer)config.rootPlacer.get()).placeRoots(level, rootSetter, random, origin, trunkOrigin, config)) {
/*  82 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  86 */     List<FoliagePlacer.FoliageAttachment> foliageAttachments = config.trunkPlacer.placeTrunk(level, trunkSetter, random, clippedTreeHeight, trunkOrigin, config);
/*  87 */     foliageAttachments.forEach(foliageAttachment -> 
/*  88 */         config.foliagePlacer.createFoliage(level, foliageSetter, random, config, clippedTreeHeight, foliageAttachment, foliageHeight, leafRadius));
/*     */     
/*  90 */     return true;
/*     */   }
/*     */   
/*     */   private int getMaxFreeTreeHeight(LevelSimulatedReader level, int maxTreeHeight, BlockPos treePos, TreeConfiguration config) {
/*  94 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/*     */     
/*  96 */     for (int y = 0; y <= maxTreeHeight + 1; y++) {
/*  97 */       int r = config.minimumSize.getSizeAtHeight(maxTreeHeight, y);
/*  98 */       for (int x = -r; x <= r; x++) {
/*  99 */         for (int z = -r; z <= r; z++) {
/* 100 */           blockPos.setWithOffset(treePos, x, y, z);
/* 101 */           if (!config.trunkPlacer.isFree(level, blockPos) || (!config.ignoreVines && isVine(level, blockPos))) {
/* 102 */             return y - 2;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 108 */     return maxTreeHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 113 */   protected void setBlock(LevelWriter level, BlockPos pos, BlockState blockState) { setBlockKnownShape(level, pos, blockState); }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean place(FeaturePlaceContext<TreeConfiguration> context) {
/* 118 */     final WorldGenLevel level = context.level();
/* 119 */     RandomSource random = context.random();
/* 120 */     BlockPos origin = context.origin();
/* 121 */     TreeConfiguration config = (TreeConfiguration)context.config();
/*     */     
/* 123 */     Set<BlockPos> rootPositions = Sets.newHashSet();
/* 124 */     Set<BlockPos> trunks = Sets.newHashSet();
/* 125 */     final Set<BlockPos> foliage = Sets.newHashSet();
/* 126 */     Set<BlockPos> decorations = Sets.newHashSet();
/*     */     
/* 128 */     BiConsumer<BlockPos, BlockState> rootSetter = (pos, state) -> {
/* 129 */         rootPositions.add(pos.immutable());
/* 130 */         level.setBlock(pos, state, 19);
/*     */       };
/* 132 */     BiConsumer<BlockPos, BlockState> trunkSetter = (pos, state) -> {
/* 133 */         trunks.add(pos.immutable());
/* 134 */         level.setBlock(pos, state, 19);
/*     */       };
/* 136 */     FoliagePlacer.FoliageSetter foliageSetter = new FoliagePlacer.FoliageSetter(this)
/*     */       {
/*     */         public void set(BlockPos pos, BlockState state) {
/* 139 */           foliage.add(pos.immutable());
/* 140 */           level.setBlock(pos, state, 19);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isSet(BlockPos pos) {
/* 145 */           return foliage.contains(pos);
/*     */         }
/*     */       };
/* 148 */     BiConsumer<BlockPos, BlockState> decorationSetter = (pos, state) -> {
/* 149 */         decorations.add(pos.immutable());
/* 150 */         level.setBlock(pos, state, 19);
/*     */       };
/*     */     
/* 153 */     boolean result = doPlace(level, random, origin, rootSetter, trunkSetter, foliageSetter, config);
/* 154 */     if (!result || (trunks.isEmpty() && foliage.isEmpty())) {
/* 155 */       return false;
/*     */     }
/*     */     
/* 158 */     if (!config.decorators.isEmpty()) {
/* 159 */       TreeDecorator.Context decoratorContext = new TreeDecorator.Context(level, decorationSetter, random, trunks, foliage, rootPositions);
/* 160 */       config.decorators.forEach(decorator -> decorator.place(decoratorContext));
/*     */     } 
/*     */     
/* 163 */     return ((Boolean)BoundingBox.encapsulatingPositions(Iterables.concat(rootPositions, trunks, foliage, decorations)).map(bounds -> {
/* 164 */           DiscreteVoxelShape shape = updateLeaves(level, bounds, trunks, decorations, rootPositions);
/* 165 */           StructureTemplate.updateShapeAtEdge(level, 3, shape, bounds.minX(), bounds.minY(), bounds.minZ());
/*     */           
/* 167 */           return Boolean.valueOf(true);
/* 168 */         }).orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static DiscreteVoxelShape updateLeaves(LevelAccessor level, BoundingBox bounds, Set<BlockPos> logs, Set<BlockPos> decorationSet, Set<BlockPos> rootPositions) {
/* 176 */     BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape = new BitSetDiscreteVoxelShape(bounds.getXSpan(), bounds.getYSpan(), bounds.getZSpan());
/* 177 */     int maxDistance = 7;
/*     */ 
/*     */     
/* 180 */     List<Set<BlockPos>> toCheck = Lists.newArrayList();
/* 181 */     for (int i = 0; i < 7; i++) {
/* 182 */       toCheck.add(Sets.newHashSet());
/*     */     }
/*     */     
/* 185 */     for (BlockPos pos : Lists.newArrayList(Sets.union(decorationSet, rootPositions))) {
/* 186 */       if (bounds.isInside(pos)) {
/* 187 */         bitSetDiscreteVoxelShape.fill(pos.getX() - bounds.minX(), pos.getY() - bounds.minY(), pos.getZ() - bounds.minZ());
/*     */       }
/*     */     } 
/*     */     
/* 191 */     BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
/* 192 */     int smallestDistance = 0;
/* 193 */     ((Set)toCheck.get(0)).addAll(logs);
/*     */     
/*     */     while (true) {
/* 196 */       if (smallestDistance < 7 && ((Set)toCheck.get(smallestDistance)).isEmpty()) {
/* 197 */         smallestDistance++; continue;
/*     */       } 
/* 199 */       if (smallestDistance >= 7) {
/*     */         break;
/*     */       }
/* 202 */       Iterator<BlockPos> iterator = ((Set)toCheck.get(smallestDistance)).iterator();
/* 203 */       BlockPos pos = (BlockPos)iterator.next();
/* 204 */       iterator.remove();
/*     */       
/* 206 */       if (!bounds.isInside(pos)) {
/*     */         continue;
/*     */       }
/* 209 */       if (smallestDistance != 0) {
/* 210 */         BlockState state = level.getBlockState(pos);
/* 211 */         setBlockKnownShape(level, pos, (BlockState)state.setValue(BlockStateProperties.DISTANCE, Integer.valueOf(smallestDistance)));
/*     */       } 
/* 213 */       bitSetDiscreteVoxelShape.fill(pos.getX() - bounds.minX(), pos.getY() - bounds.minY(), pos.getZ() - bounds.minZ());
/*     */       
/* 215 */       for (Direction direction : Direction.values()) {
/* 216 */         neighborPos.setWithOffset(pos, direction);
/* 217 */         if (bounds.isInside(neighborPos)) {
/*     */ 
/*     */           
/* 220 */           int xInShape = neighborPos.getX() - bounds.minX();
/* 221 */           int yInShape = neighborPos.getY() - bounds.minY();
/* 222 */           int zinShape = neighborPos.getZ() - bounds.minZ();
/* 223 */           if (!bitSetDiscreteVoxelShape.isFull(xInShape, yInShape, zinShape)) {
/*     */ 
/*     */ 
/*     */             
/* 227 */             BlockState currentState = level.getBlockState(neighborPos);
/* 228 */             OptionalInt distance = LeavesBlock.getOptionalDistanceAt(currentState);
/*     */             
/* 230 */             if (!distance.isEmpty())
/*     */             
/*     */             { 
/*     */               
/* 234 */               int newDistance = Math.min(distance.getAsInt(), smallestDistance + 1);
/*     */               
/* 236 */               if (newDistance < 7)
/* 237 */               { ((Set)toCheck.get(newDistance)).add(neighborPos.immutable());
/* 238 */                 smallestDistance = Math.min(smallestDistance, newDistance); }  } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 242 */     }  return bitSetDiscreteVoxelShape;
/*     */   }
/*     */   
/*     */   public static List<BlockPos> getLowestTrunkOrRootOfTree(TreeDecorator.Context context) {
/* 246 */     List<BlockPos> blockPositions = Lists.newArrayList();
/* 247 */     ObjectArrayList objectArrayList1 = context.roots();
/* 248 */     ObjectArrayList objectArrayList2 = context.logs();
/*     */     
/* 250 */     if (objectArrayList1.isEmpty()) {
/* 251 */       blockPositions.addAll(objectArrayList2);
/* 252 */     } else if (!objectArrayList2.isEmpty() && ((BlockPos)objectArrayList1.get(0)).getY() == ((BlockPos)objectArrayList2.get(0)).getY()) {
/* 253 */       blockPositions.addAll(objectArrayList2);
/* 254 */       blockPositions.addAll(objectArrayList1);
/*     */     } else {
/* 256 */       blockPositions.addAll(objectArrayList1);
/*     */     } 
/* 258 */     return blockPositions;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\TreeFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
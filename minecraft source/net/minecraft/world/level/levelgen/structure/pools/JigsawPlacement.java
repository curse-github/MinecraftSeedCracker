/*     */ package net.minecraft.world.level.levelgen.structure.pools;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.Pools;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.SequencedPriorityIterator;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.block.JigsawBlock;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class JigsawPlacement
/*     */ {
/*  49 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final int UNSET_HEIGHT = -2147483648;
/*     */   private static final class PieceState extends Record { private final PoolElementStructurePiece piece; private final MutableObject<VoxelShape> free; private final int depth;
/*     */     
/*  52 */     private PieceState(PoolElementStructurePiece piece, MutableObject<VoxelShape> free, int depth) { this.piece = piece; this.free = free; this.depth = depth; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pools/JigsawPlacement$PieceState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #52	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/JigsawPlacement$PieceState; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pools/JigsawPlacement$PieceState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #52	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/JigsawPlacement$PieceState; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pools/JigsawPlacement$PieceState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #52	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/JigsawPlacement$PieceState;
/*  52 */       //   0	8	1	o	Ljava/lang/Object; } public PoolElementStructurePiece piece() { return this.piece; } public MutableObject<VoxelShape> free() { return this.free; } public int depth() { return this.depth; } }
/*     */   private static final class Placer { private final Registry<StructureTemplatePool> pools;
/*     */     private final int maxDepth;
/*     */     private final ChunkGenerator chunkGenerator;
/*     */     private final StructureTemplateManager structureTemplateManager;
/*     */     private final List<? super PoolElementStructurePiece> pieces;
/*     */     private final RandomSource random;
/*     */     private final SequencedPriorityIterator<JigsawPlacement.PieceState> placing;
/*     */     
/*     */     private Placer(Registry<StructureTemplatePool> pools, int maxDepth, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager, List<? super PoolElementStructurePiece> pieces, RandomSource random) {
/*  62 */       this.placing = new SequencedPriorityIterator();
/*     */ 
/*     */       
/*  65 */       this.pools = pools;
/*  66 */       this.maxDepth = maxDepth;
/*  67 */       this.chunkGenerator = chunkGenerator;
/*  68 */       this.structureTemplateManager = structureTemplateManager;
/*  69 */       this.pieces = pieces;
/*  70 */       this.random = random;
/*     */     }
/*     */     
/*     */     private void tryPlacingChildren(PoolElementStructurePiece sourcePiece, MutableObject<VoxelShape> contextFree, int depth, boolean doExpansionHack, LevelHeightAccessor heightAccessor, RandomState randomState, PoolAliasLookup poolAliasLookup, LiquidSettings liquidSettings) {
/*  74 */       StructurePoolElement sourceElement = sourcePiece.getElement();
/*  75 */       BlockPos sourceBoxPosition = sourcePiece.getPosition();
/*  76 */       Rotation sourceRotation = sourcePiece.getRotation();
/*     */       
/*  78 */       StructureTemplatePool.Projection sourceProjection = sourceElement.getProjection();
/*  79 */       boolean sourceRigid = (sourceProjection == StructureTemplatePool.Projection.RIGID);
/*     */       
/*  81 */       MutableObject<VoxelShape> sourceFree = new MutableObject<VoxelShape>();
/*     */       
/*  83 */       BoundingBox sourceBB = sourcePiece.getBoundingBox();
/*  84 */       int sourceBoxY = sourceBB.minY();
/*     */       
/*  86 */       for (StructureTemplate.JigsawBlockInfo sourceJigsaw : sourceElement.getShuffledJigsawBlocks(this.structureTemplateManager, sourceBoxPosition, sourceRotation, this.random)) {
/*  87 */         MutableObject<VoxelShape> childrenFree; StructureTemplate.StructureBlockInfo sourceJigsawInfo = sourceJigsaw.info();
/*  88 */         Direction sourceDirection = JigsawBlock.getFrontFacing(sourceJigsawInfo.state());
/*     */         
/*  90 */         BlockPos sourceJigsawPos = sourceJigsawInfo.pos();
/*  91 */         BlockPos targetJigsawPos = sourceJigsawPos.relative(sourceDirection);
/*     */         
/*  93 */         int sourceJigsawLocalY = sourceJigsawPos.getY() - sourceBoxY;
/*  94 */         int sourceJigsawBaseHeight = Integer.MIN_VALUE;
/*     */         
/*  96 */         ResourceKey<StructureTemplatePool> poolName = poolAliasLookup.lookup(sourceJigsaw.pool());
/*  97 */         Optional<? extends Holder<StructureTemplatePool>> maybeTargetPool = this.pools.get(poolName);
/*     */         
/*  99 */         if (maybeTargetPool.isEmpty()) {
/* 100 */           JigsawPlacement.LOGGER.warn("Empty or non-existent pool: {}", poolName.identifier());
/*     */           
/*     */           continue;
/*     */         } 
/* 104 */         Holder<StructureTemplatePool> targetPool = (Holder)maybeTargetPool.get();
/* 105 */         if (((StructureTemplatePool)targetPool.value()).size() == 0 && !targetPool.is(Pools.EMPTY)) {
/* 106 */           JigsawPlacement.LOGGER.warn("Empty or non-existent pool: {}", poolName.identifier());
/*     */           
/*     */           continue;
/*     */         } 
/* 110 */         Holder<StructureTemplatePool> fallback = ((StructureTemplatePool)targetPool.value()).getFallback();
/*     */         
/* 112 */         if (((StructureTemplatePool)fallback.value()).size() == 0 && !fallback.is(Pools.EMPTY)) {
/* 113 */           JigsawPlacement.LOGGER.warn("Empty or non-existent fallback pool: {}", fallback.unwrapKey().map(e -> e.identifier().toString()).orElse("<unregistered>"));
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 119 */         boolean attachInsideSource = sourceBB.isInside(targetJigsawPos);
/* 120 */         if (attachInsideSource) {
/* 121 */           childrenFree = sourceFree;
/* 122 */           if (sourceFree.get() == null) {
/* 123 */             sourceFree.setValue(Shapes.create(AABB.of(sourceBB)));
/*     */           }
/*     */         } else {
/* 126 */           childrenFree = contextFree;
/*     */         } 
/*     */ 
/*     */         
/* 130 */         List<StructurePoolElement> targetPieces = Lists.newArrayList();
/* 131 */         if (depth != this.maxDepth) {
/* 132 */           targetPieces.addAll(((StructureTemplatePool)targetPool.value()).getShuffledTemplates(this.random));
/*     */         }
/* 134 */         targetPieces.addAll(((StructureTemplatePool)fallback.value()).getShuffledTemplates(this.random));
/* 135 */         int placementPriority = sourceJigsaw.placementPriority();
/*     */ 
/*     */         
/* 138 */         for (StructurePoolElement targetElement : targetPieces) {
/* 139 */           if (targetElement == EmptyPoolElement.INSTANCE) {
/*     */             break;
/*     */           }
/*     */           
/* 143 */           for (Rotation targetRotation : Rotation.getShuffled(this.random)) {
/* 144 */             int expandTo; List<StructureTemplate.JigsawBlockInfo> targetJigsaws = targetElement.getShuffledJigsawBlocks(this.structureTemplateManager, BlockPos.ZERO, targetRotation, this.random);
/* 145 */             BoundingBox hackBox = targetElement.getBoundingBox(this.structureTemplateManager, BlockPos.ZERO, targetRotation);
/*     */ 
/*     */             
/* 148 */             if (!doExpansionHack || hackBox.getYSpan() > 16) {
/* 149 */               expandTo = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             }
/*     */             else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 162 */               expandTo = targetJigsaws.stream().mapToInt(targetJigsaw -> { StructureTemplate.StructureBlockInfo targetJigsawInfo = targetJigsaw.info(); if (!hackBox.isInside(targetJigsawInfo.pos().relative(JigsawBlock.getFrontFacing(targetJigsawInfo.state())))) return 0;  ResourceKey<StructureTemplatePool> childPoolName = poolAliasLookup.lookup(targetJigsaw.pool()); Optional<? extends Holder<StructureTemplatePool>> childPool = this.pools.get(childPoolName); Optional<Holder<StructureTemplatePool>> childFallbackPool = childPool.map(()); int childPoolSize = ((Integer)childPool.map(()).orElse(Integer.valueOf(0))).intValue(); int childFallbackSize = ((Integer)childFallbackPool.map(()).orElse(Integer.valueOf(0))).intValue(); return Math.max(childPoolSize, childFallbackSize); }).max().orElse(0);
/*     */             } 
/*     */             
/* 165 */             for (StructureTemplate.JigsawBlockInfo targetJigsaw : targetJigsaws) {
/* 166 */               int junctionY, targetGroundLevelDelta, targetBoxY; if (!JigsawBlock.canAttach(sourceJigsaw, targetJigsaw)) {
/*     */                 continue;
/*     */               }
/*     */               
/* 170 */               BlockPos targetJigsawLocalPos = targetJigsaw.info().pos();
/*     */               
/* 172 */               BlockPos rawTargetBoxPos = targetJigsawPos.subtract(targetJigsawLocalPos);
/* 173 */               BoundingBox rawTargetBB = targetElement.getBoundingBox(this.structureTemplateManager, rawTargetBoxPos, targetRotation);
/* 174 */               int rawTargetY = rawTargetBB.minY();
/*     */               
/* 176 */               StructureTemplatePool.Projection targetProjection = targetElement.getProjection();
/* 177 */               boolean targetRigid = (targetProjection == StructureTemplatePool.Projection.RIGID);
/*     */ 
/*     */               
/* 180 */               int targetJigsawLocalY = targetJigsawLocalPos.getY();
/*     */               
/* 182 */               int deltaY = sourceJigsawLocalY - targetJigsawLocalY + JigsawBlock.getFrontFacing(sourceJigsawInfo.state()).getStepY();
/*     */ 
/*     */               
/* 185 */               if (sourceRigid && targetRigid) {
/* 186 */                 targetBoxY = sourceBoxY + deltaY;
/*     */               } else {
/* 188 */                 if (sourceJigsawBaseHeight == Integer.MIN_VALUE) {
/* 189 */                   sourceJigsawBaseHeight = this.chunkGenerator.getFirstFreeHeight(sourceJigsawPos.getX(), sourceJigsawPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState);
/*     */                 }
/* 191 */                 targetBoxY = sourceJigsawBaseHeight - targetJigsawLocalY;
/*     */               } 
/*     */               
/* 194 */               int yOffset = targetBoxY - rawTargetY;
/*     */               
/* 196 */               BoundingBox targetBB = rawTargetBB.moved(0, yOffset, 0);
/* 197 */               BlockPos targetBoxPosition = rawTargetBoxPos.offset(0, yOffset, 0);
/*     */               
/* 199 */               if (expandTo > 0) {
/* 200 */                 int newSize = Math.max(expandTo + 1, targetBB.maxY() - targetBB.minY());
/* 201 */                 targetBB.encapsulate(new BlockPos(targetBB.minX(), targetBB.minY() + newSize, targetBB.minZ()));
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/* 206 */               if (Shapes.joinIsNotEmpty((VoxelShape)childrenFree.get(), Shapes.create(AABB.of(targetBB).deflate(0.25D)), BooleanOp.ONLY_SECOND)) {
/*     */                 continue;
/*     */               }
/*     */               
/* 210 */               childrenFree.setValue(Shapes.joinUnoptimized((VoxelShape)childrenFree.get(), Shapes.create(AABB.of(targetBB)), BooleanOp.ONLY_FIRST));
/*     */               
/* 212 */               int sourceGroundLevelDelta = sourcePiece.getGroundLevelDelta();
/*     */               
/* 214 */               if (targetRigid) {
/*     */                 
/* 216 */                 targetGroundLevelDelta = sourceGroundLevelDelta - deltaY;
/*     */               } else {
/* 218 */                 targetGroundLevelDelta = targetElement.getGroundLevelDelta();
/*     */               } 
/*     */               
/* 221 */               PoolElementStructurePiece targetPiece = new PoolElementStructurePiece(this.structureTemplateManager, targetElement, targetBoxPosition, targetGroundLevelDelta, targetRotation, targetBB, liquidSettings);
/*     */ 
/*     */               
/* 224 */               if (sourceRigid) {
/* 225 */                 junctionY = sourceBoxY + sourceJigsawLocalY;
/* 226 */               } else if (targetRigid) {
/* 227 */                 junctionY = targetBoxY + targetJigsawLocalY;
/*     */               } else {
/* 229 */                 if (sourceJigsawBaseHeight == Integer.MIN_VALUE) {
/* 230 */                   sourceJigsawBaseHeight = this.chunkGenerator.getFirstFreeHeight(sourceJigsawPos.getX(), sourceJigsawPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState);
/*     */                 }
/* 232 */                 junctionY = sourceJigsawBaseHeight + deltaY / 2;
/*     */               } 
/*     */               
/* 235 */               sourcePiece.addJunction(new JigsawJunction(targetJigsawPos
/* 236 */                     .getX(), junctionY - sourceJigsawLocalY + sourceGroundLevelDelta, targetJigsawPos
/*     */                     
/* 238 */                     .getZ(), deltaY, targetProjection));
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 243 */               targetPiece.addJunction(new JigsawJunction(sourceJigsawPos
/* 244 */                     .getX(), junctionY - targetJigsawLocalY + targetGroundLevelDelta, sourceJigsawPos
/*     */                     
/* 246 */                     .getZ(), -deltaY, sourceProjection));
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 251 */               this.pieces.add(targetPiece);
/* 252 */               if (depth + 1 <= this.maxDepth) {
/* 253 */                 JigsawPlacement.PieceState state = new JigsawPlacement.PieceState(targetPiece, childrenFree, depth + 1);
/* 254 */                 this.placing.add(state, placementPriority);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } }
/*     */ 
/*     */   
/*     */   public static Optional<Structure.GenerationStub> addPieces(Structure.GenerationContext context, Holder<StructureTemplatePool> startPool, Optional<Identifier> startJigsaw, int maxDepth, BlockPos position, boolean doExpansionHack, Optional<Heightmap.Types> projectStartToHeightmap, JigsawStructure.MaxDistance maxDistanceFromCenter, PoolAliasLookup poolAliasLookup, DimensionPadding dimensionPadding, LiquidSettings liquidSettings) {
/*     */     BlockPos anchoredPosition;
/* 265 */     RegistryAccess registryAccess = context.registryAccess();
/* 266 */     ChunkGenerator chunkGenerator = context.chunkGenerator();
/* 267 */     StructureTemplateManager structureTemplateManager = context.structureTemplateManager();
/* 268 */     LevelHeightAccessor heightAccessor = context.heightAccessor();
/* 269 */     WorldgenRandom random = context.random();
/* 270 */     Registry<StructureTemplatePool> pools = registryAccess.lookupOrThrow(Registries.TEMPLATE_POOL);
/*     */     
/* 272 */     Rotation centerRotation = Rotation.getRandom(random);
/*     */ 
/*     */     
/* 275 */     StructureTemplatePool centerPool = (StructureTemplatePool)startPool.unwrapKey().flatMap(key -> pools.getOptional(poolAliasLookup.lookup(key))).orElse((StructureTemplatePool)startPool.value());
/*     */     
/* 277 */     StructurePoolElement centerElement = centerPool.getRandomTemplate(random);
/*     */     
/* 279 */     if (centerElement == EmptyPoolElement.INSTANCE) {
/* 280 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 284 */     if (startJigsaw.isPresent()) {
/* 285 */       Identifier targetJigsawId = (Identifier)startJigsaw.get();
/* 286 */       Optional<BlockPos> anchor = getRandomNamedJigsaw(centerElement, targetJigsawId, position, centerRotation, structureTemplateManager, random);
/* 287 */       if (anchor.isEmpty()) {
/* 288 */         LOGGER.error("No starting jigsaw {} found in start pool {}", targetJigsawId, startPool.unwrapKey().map(key -> key.identifier().toString()).orElse("<unregistered>"));
/* 289 */         return Optional.empty();
/*     */       } 
/* 291 */       anchoredPosition = (BlockPos)anchor.get();
/*     */     } else {
/* 293 */       anchoredPosition = position;
/*     */     } 
/*     */     
/* 296 */     BlockPos blockPos = anchoredPosition.subtract(position);
/*     */ 
/*     */     
/* 299 */     BlockPos adjustedPosition = position.subtract(blockPos);
/*     */     
/* 301 */     PoolElementStructurePiece centerPiece = new PoolElementStructurePiece(structureTemplateManager, centerElement, adjustedPosition, centerElement.getGroundLevelDelta(), centerRotation, centerElement.getBoundingBox(structureTemplateManager, adjustedPosition, centerRotation), liquidSettings);
/* 302 */     BoundingBox box = centerPiece.getBoundingBox();
/* 303 */     int centerX = (box.maxX() + box.minX()) / 2;
/* 304 */     int centerZ = (box.maxZ() + box.minZ()) / 2;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 309 */     int bottomY = projectStartToHeightmap.isEmpty() ? adjustedPosition.getY() : (position.getY() + chunkGenerator.getFirstFreeHeight(centerX, centerZ, (Heightmap.Types)projectStartToHeightmap.get(), heightAccessor, context.randomState()));
/*     */     
/* 311 */     int oldAbsoluteGroundY = box.minY() + centerPiece.getGroundLevelDelta();
/* 312 */     centerPiece.move(0, bottomY - oldAbsoluteGroundY, 0);
/*     */     
/* 314 */     if (isStartTooCloseToWorldHeightLimits(heightAccessor, dimensionPadding, centerPiece.getBoundingBox())) {
/* 315 */       LOGGER.debug("Center piece {} with bounding box {} does not fit dimension padding {}", new Object[] { centerElement, centerPiece.getBoundingBox(), dimensionPadding });
/* 316 */       return Optional.empty();
/*     */     } 
/*     */     
/* 319 */     int centerY = bottomY + blockPos.getY();
/*     */     
/* 321 */     return Optional.of(new Structure.GenerationStub(new BlockPos(centerX, centerY, centerZ), builder -> {
/* 322 */             List<PoolElementStructurePiece> pieces = Lists.newArrayList();
/* 323 */             pieces.add(centerPiece);
/* 324 */             if (maxDepth <= 0) {
/*     */               return;
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 334 */             AABB aabb = new AABB((centerX - maxDistanceFromCenter.horizontal()), Math.max(centerY - maxDistanceFromCenter.vertical(), heightAccessor.getMinY() + dimensionPadding.bottom()), (centerZ - maxDistanceFromCenter.horizontal()), (centerX + maxDistanceFromCenter.horizontal() + 1), Math.min(centerY + maxDistanceFromCenter.vertical() + 1, heightAccessor.getMaxY() + 1 - dimensionPadding.top()), (centerZ + maxDistanceFromCenter.horizontal() + 1));
/*     */ 
/*     */             
/* 337 */             VoxelShape shape = Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(box)), BooleanOp.ONLY_FIRST);
/* 338 */             addPieces(context.randomState(), maxDepth, doExpansionHack, chunkGenerator, structureTemplateManager, heightAccessor, random, pools, centerPiece, pieces, shape, poolAliasLookup, liquidSettings);
/*     */             
/* 340 */             Objects.requireNonNull(builder); pieces.forEach(builder::addPiece);
/*     */           }));
/*     */   }
/*     */   
/*     */   private static boolean isStartTooCloseToWorldHeightLimits(LevelHeightAccessor heightAccessor, DimensionPadding dimensionPadding, BoundingBox centerPieceBb) {
/* 345 */     if (dimensionPadding == DimensionPadding.ZERO) {
/* 346 */       return false;
/*     */     }
/*     */     
/* 349 */     int minYWithPadding = heightAccessor.getMinY() + dimensionPadding.bottom();
/* 350 */     int maxYWithPadding = heightAccessor.getMaxY() - dimensionPadding.top();
/*     */     
/* 352 */     return (centerPieceBb.minY() < minYWithPadding || centerPieceBb.maxY() > maxYWithPadding);
/*     */   }
/*     */   
/*     */   private static Optional<BlockPos> getRandomNamedJigsaw(StructurePoolElement element, Identifier targetJigsawId, BlockPos position, Rotation rotation, StructureTemplateManager structureTemplateManager, WorldgenRandom random) {
/* 356 */     List<StructureTemplate.JigsawBlockInfo> jigsaws = element.getShuffledJigsawBlocks(structureTemplateManager, position, rotation, random);
/* 357 */     for (StructureTemplate.JigsawBlockInfo jigsaw : jigsaws) {
/* 358 */       if (targetJigsawId.equals(jigsaw.name())) {
/* 359 */         return Optional.of(jigsaw.info().pos());
/*     */       }
/*     */     } 
/* 362 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private static void addPieces(RandomState randomState, int maxDepth, boolean doExpansionHack, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager, LevelHeightAccessor heightAccessor, RandomSource random, Registry<StructureTemplatePool> pools, PoolElementStructurePiece centerPiece, List<PoolElementStructurePiece> pieces, VoxelShape shape, PoolAliasLookup poolAliasLookup, LiquidSettings liquidSettings) {
/* 366 */     Placer placer = new Placer(pools, maxDepth, chunkGenerator, structureTemplateManager, pieces, random);
/*     */ 
/*     */     
/* 369 */     placer.tryPlacingChildren(centerPiece, new MutableObject(shape), 0, doExpansionHack, heightAccessor, randomState, poolAliasLookup, liquidSettings);
/*     */ 
/*     */     
/* 372 */     while (placer.placing.hasNext()) {
/* 373 */       PieceState state = (PieceState)placer.placing.next();
/* 374 */       placer.tryPlacingChildren(state.piece, state.free, state.depth, doExpansionHack, heightAccessor, randomState, poolAliasLookup, liquidSettings);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean generateJigsaw(ServerLevel level, Holder<StructureTemplatePool> pool, Identifier target, int maxDepth, BlockPos position, boolean keepJigsaws) {
/* 379 */     ChunkGenerator generator = level.getChunkSource().getGenerator();
/* 380 */     StructureTemplateManager structureTemplateManager = level.getStructureManager();
/* 381 */     StructureManager structureManager = level.structureManager();
/* 382 */     RandomSource random = level.getRandom();
/*     */     
/* 384 */     Structure.GenerationContext generationContext = new Structure.GenerationContext(level.registryAccess(), generator, generator.getBiomeSource(), level.getChunkSource().randomState(), structureTemplateManager, level.getSeed(), new ChunkPos(position), level, b -> true);
/* 385 */     Optional<Structure.GenerationStub> stub = addPieces(generationContext, pool, Optional.of(target), maxDepth, position, false, Optional.empty(), new JigsawStructure.MaxDistance(128), PoolAliasLookup.EMPTY, JigsawStructure.DEFAULT_DIMENSION_PADDING, JigsawStructure.DEFAULT_LIQUID_SETTINGS);
/*     */     
/* 387 */     if (stub.isPresent()) {
/* 388 */       StructurePiecesBuilder builder = ((Structure.GenerationStub)stub.get()).getPiecesBuilder();
/*     */       
/* 390 */       for (StructurePiece piece : builder.build().pieces()) {
/* 391 */         if (piece instanceof PoolElementStructurePiece) { PoolElementStructurePiece poolPiece = (PoolElementStructurePiece)piece;
/* 392 */           poolPiece.place(level, structureManager, generator, random, BoundingBox.infinite(), position, keepJigsaws); }
/*     */       
/*     */       } 
/* 395 */       return true;
/*     */     } 
/* 397 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\JigsawPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
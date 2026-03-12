/*     */ package net.minecraft.world.level.levelgen.structure.pools;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.data.worldgen.Pools;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.SequencedPriorityIterator;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.JigsawBlock;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
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
/*     */ final class Placer
/*     */ {
/*     */   private final Registry<StructureTemplatePool> pools;
/*     */   private final int maxDepth;
/*     */   private final ChunkGenerator chunkGenerator;
/*     */   private final StructureTemplateManager structureTemplateManager;
/*     */   private final List<? super PoolElementStructurePiece> pieces;
/*     */   private final RandomSource random;
/*     */   private final SequencedPriorityIterator<JigsawPlacement.PieceState> placing;
/*     */   
/*     */   private Placer(Registry<StructureTemplatePool> pools, int maxDepth, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager, List<? super PoolElementStructurePiece> pieces, RandomSource random) {
/*  62 */     this.placing = new SequencedPriorityIterator();
/*     */ 
/*     */     
/*  65 */     this.pools = pools;
/*  66 */     this.maxDepth = maxDepth;
/*  67 */     this.chunkGenerator = chunkGenerator;
/*  68 */     this.structureTemplateManager = structureTemplateManager;
/*  69 */     this.pieces = pieces;
/*  70 */     this.random = random;
/*     */   }
/*     */   
/*     */   private void tryPlacingChildren(PoolElementStructurePiece sourcePiece, MutableObject<VoxelShape> contextFree, int depth, boolean doExpansionHack, LevelHeightAccessor heightAccessor, RandomState randomState, PoolAliasLookup poolAliasLookup, LiquidSettings liquidSettings) {
/*  74 */     StructurePoolElement sourceElement = sourcePiece.getElement();
/*  75 */     BlockPos sourceBoxPosition = sourcePiece.getPosition();
/*  76 */     Rotation sourceRotation = sourcePiece.getRotation();
/*     */     
/*  78 */     StructureTemplatePool.Projection sourceProjection = sourceElement.getProjection();
/*  79 */     boolean sourceRigid = (sourceProjection == StructureTemplatePool.Projection.RIGID);
/*     */     
/*  81 */     MutableObject<VoxelShape> sourceFree = new MutableObject<VoxelShape>();
/*     */     
/*  83 */     BoundingBox sourceBB = sourcePiece.getBoundingBox();
/*  84 */     int sourceBoxY = sourceBB.minY();
/*     */     
/*  86 */     for (StructureTemplate.JigsawBlockInfo sourceJigsaw : sourceElement.getShuffledJigsawBlocks(this.structureTemplateManager, sourceBoxPosition, sourceRotation, this.random)) {
/*  87 */       MutableObject<VoxelShape> childrenFree; StructureTemplate.StructureBlockInfo sourceJigsawInfo = sourceJigsaw.info();
/*  88 */       Direction sourceDirection = JigsawBlock.getFrontFacing(sourceJigsawInfo.state());
/*     */       
/*  90 */       BlockPos sourceJigsawPos = sourceJigsawInfo.pos();
/*  91 */       BlockPos targetJigsawPos = sourceJigsawPos.relative(sourceDirection);
/*     */       
/*  93 */       int sourceJigsawLocalY = sourceJigsawPos.getY() - sourceBoxY;
/*  94 */       int sourceJigsawBaseHeight = Integer.MIN_VALUE;
/*     */       
/*  96 */       ResourceKey<StructureTemplatePool> poolName = poolAliasLookup.lookup(sourceJigsaw.pool());
/*  97 */       Optional<? extends Holder<StructureTemplatePool>> maybeTargetPool = this.pools.get(poolName);
/*     */       
/*  99 */       if (maybeTargetPool.isEmpty()) {
/* 100 */         JigsawPlacement.LOGGER.warn("Empty or non-existent pool: {}", poolName.identifier());
/*     */         
/*     */         continue;
/*     */       } 
/* 104 */       Holder<StructureTemplatePool> targetPool = (Holder)maybeTargetPool.get();
/* 105 */       if (((StructureTemplatePool)targetPool.value()).size() == 0 && !targetPool.is(Pools.EMPTY)) {
/* 106 */         JigsawPlacement.LOGGER.warn("Empty or non-existent pool: {}", poolName.identifier());
/*     */         
/*     */         continue;
/*     */       } 
/* 110 */       Holder<StructureTemplatePool> fallback = ((StructureTemplatePool)targetPool.value()).getFallback();
/*     */       
/* 112 */       if (((StructureTemplatePool)fallback.value()).size() == 0 && !fallback.is(Pools.EMPTY)) {
/* 113 */         JigsawPlacement.LOGGER.warn("Empty or non-existent fallback pool: {}", fallback.unwrapKey().map(e -> e.identifier().toString()).orElse("<unregistered>"));
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 119 */       boolean attachInsideSource = sourceBB.isInside(targetJigsawPos);
/* 120 */       if (attachInsideSource) {
/* 121 */         childrenFree = sourceFree;
/* 122 */         if (sourceFree.get() == null) {
/* 123 */           sourceFree.setValue(Shapes.create(AABB.of(sourceBB)));
/*     */         }
/*     */       } else {
/* 126 */         childrenFree = contextFree;
/*     */       } 
/*     */ 
/*     */       
/* 130 */       List<StructurePoolElement> targetPieces = Lists.newArrayList();
/* 131 */       if (depth != this.maxDepth) {
/* 132 */         targetPieces.addAll(((StructureTemplatePool)targetPool.value()).getShuffledTemplates(this.random));
/*     */       }
/* 134 */       targetPieces.addAll(((StructureTemplatePool)fallback.value()).getShuffledTemplates(this.random));
/* 135 */       int placementPriority = sourceJigsaw.placementPriority();
/*     */ 
/*     */       
/* 138 */       for (StructurePoolElement targetElement : targetPieces) {
/* 139 */         if (targetElement == EmptyPoolElement.INSTANCE) {
/*     */           break;
/*     */         }
/*     */         
/* 143 */         for (Rotation targetRotation : Rotation.getShuffled(this.random)) {
/* 144 */           int expandTo; List<StructureTemplate.JigsawBlockInfo> targetJigsaws = targetElement.getShuffledJigsawBlocks(this.structureTemplateManager, BlockPos.ZERO, targetRotation, this.random);
/* 145 */           BoundingBox hackBox = targetElement.getBoundingBox(this.structureTemplateManager, BlockPos.ZERO, targetRotation);
/*     */ 
/*     */           
/* 148 */           if (!doExpansionHack || hackBox.getYSpan() > 16) {
/* 149 */             expandTo = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 162 */             expandTo = targetJigsaws.stream().mapToInt(targetJigsaw -> { StructureTemplate.StructureBlockInfo targetJigsawInfo = targetJigsaw.info(); if (!hackBox.isInside(targetJigsawInfo.pos().relative(JigsawBlock.getFrontFacing(targetJigsawInfo.state())))) return 0;  ResourceKey<StructureTemplatePool> childPoolName = poolAliasLookup.lookup(targetJigsaw.pool()); Optional<? extends Holder<StructureTemplatePool>> childPool = this.pools.get(childPoolName); Optional<Holder<StructureTemplatePool>> childFallbackPool = childPool.map(()); int childPoolSize = ((Integer)childPool.map(()).orElse(Integer.valueOf(0))).intValue(); int childFallbackSize = ((Integer)childFallbackPool.map(()).orElse(Integer.valueOf(0))).intValue(); return Math.max(childPoolSize, childFallbackSize); }).max().orElse(0);
/*     */           } 
/*     */           
/* 165 */           for (StructureTemplate.JigsawBlockInfo targetJigsaw : targetJigsaws) {
/* 166 */             int junctionY, targetGroundLevelDelta, targetBoxY; if (!JigsawBlock.canAttach(sourceJigsaw, targetJigsaw)) {
/*     */               continue;
/*     */             }
/*     */             
/* 170 */             BlockPos targetJigsawLocalPos = targetJigsaw.info().pos();
/*     */             
/* 172 */             BlockPos rawTargetBoxPos = targetJigsawPos.subtract(targetJigsawLocalPos);
/* 173 */             BoundingBox rawTargetBB = targetElement.getBoundingBox(this.structureTemplateManager, rawTargetBoxPos, targetRotation);
/* 174 */             int rawTargetY = rawTargetBB.minY();
/*     */             
/* 176 */             StructureTemplatePool.Projection targetProjection = targetElement.getProjection();
/* 177 */             boolean targetRigid = (targetProjection == StructureTemplatePool.Projection.RIGID);
/*     */ 
/*     */             
/* 180 */             int targetJigsawLocalY = targetJigsawLocalPos.getY();
/*     */             
/* 182 */             int deltaY = sourceJigsawLocalY - targetJigsawLocalY + JigsawBlock.getFrontFacing(sourceJigsawInfo.state()).getStepY();
/*     */ 
/*     */             
/* 185 */             if (sourceRigid && targetRigid) {
/* 186 */               targetBoxY = sourceBoxY + deltaY;
/*     */             } else {
/* 188 */               if (sourceJigsawBaseHeight == Integer.MIN_VALUE) {
/* 189 */                 sourceJigsawBaseHeight = this.chunkGenerator.getFirstFreeHeight(sourceJigsawPos.getX(), sourceJigsawPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState);
/*     */               }
/* 191 */               targetBoxY = sourceJigsawBaseHeight - targetJigsawLocalY;
/*     */             } 
/*     */             
/* 194 */             int yOffset = targetBoxY - rawTargetY;
/*     */             
/* 196 */             BoundingBox targetBB = rawTargetBB.moved(0, yOffset, 0);
/* 197 */             BlockPos targetBoxPosition = rawTargetBoxPos.offset(0, yOffset, 0);
/*     */             
/* 199 */             if (expandTo > 0) {
/* 200 */               int newSize = Math.max(expandTo + 1, targetBB.maxY() - targetBB.minY());
/* 201 */               targetBB.encapsulate(new BlockPos(targetBB.minX(), targetBB.minY() + newSize, targetBB.minZ()));
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 206 */             if (Shapes.joinIsNotEmpty((VoxelShape)childrenFree.get(), Shapes.create(AABB.of(targetBB).deflate(0.25D)), BooleanOp.ONLY_SECOND)) {
/*     */               continue;
/*     */             }
/*     */             
/* 210 */             childrenFree.setValue(Shapes.joinUnoptimized((VoxelShape)childrenFree.get(), Shapes.create(AABB.of(targetBB)), BooleanOp.ONLY_FIRST));
/*     */             
/* 212 */             int sourceGroundLevelDelta = sourcePiece.getGroundLevelDelta();
/*     */             
/* 214 */             if (targetRigid) {
/*     */               
/* 216 */               targetGroundLevelDelta = sourceGroundLevelDelta - deltaY;
/*     */             } else {
/* 218 */               targetGroundLevelDelta = targetElement.getGroundLevelDelta();
/*     */             } 
/*     */             
/* 221 */             PoolElementStructurePiece targetPiece = new PoolElementStructurePiece(this.structureTemplateManager, targetElement, targetBoxPosition, targetGroundLevelDelta, targetRotation, targetBB, liquidSettings);
/*     */ 
/*     */             
/* 224 */             if (sourceRigid) {
/* 225 */               junctionY = sourceBoxY + sourceJigsawLocalY;
/* 226 */             } else if (targetRigid) {
/* 227 */               junctionY = targetBoxY + targetJigsawLocalY;
/*     */             } else {
/* 229 */               if (sourceJigsawBaseHeight == Integer.MIN_VALUE) {
/* 230 */                 sourceJigsawBaseHeight = this.chunkGenerator.getFirstFreeHeight(sourceJigsawPos.getX(), sourceJigsawPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState);
/*     */               }
/* 232 */               junctionY = sourceJigsawBaseHeight + deltaY / 2;
/*     */             } 
/*     */             
/* 235 */             sourcePiece.addJunction(new JigsawJunction(targetJigsawPos
/* 236 */                   .getX(), junctionY - sourceJigsawLocalY + sourceGroundLevelDelta, targetJigsawPos
/*     */                   
/* 238 */                   .getZ(), deltaY, targetProjection));
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 243 */             targetPiece.addJunction(new JigsawJunction(sourceJigsawPos
/* 244 */                   .getX(), junctionY - targetJigsawLocalY + targetGroundLevelDelta, sourceJigsawPos
/*     */                   
/* 246 */                   .getZ(), -deltaY, sourceProjection));
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 251 */             this.pieces.add(targetPiece);
/* 252 */             if (depth + 1 <= this.maxDepth) {
/* 253 */               JigsawPlacement.PieceState state = new JigsawPlacement.PieceState(targetPiece, childrenFree, depth + 1);
/* 254 */               this.placing.add(state, placementPriority);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\JigsawPlacement$Placer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
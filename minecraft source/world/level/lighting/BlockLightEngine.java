/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LightChunk;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ 
/*     */ public final class BlockLightEngine extends LightEngine<BlockLightSectionStorage.BlockDataLayerStorageMap, BlockLightSectionStorage> {
/*  14 */   private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
/*     */ 
/*     */   
/*  17 */   public BlockLightEngine(LightChunkGetter chunkSource) { this(chunkSource, new BlockLightSectionStorage(chunkSource)); }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*  22 */   public BlockLightEngine(LightChunkGetter chunkSource, BlockLightSectionStorage storage) { super(chunkSource, storage); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkNode(long blockNode) {
/*  27 */     long sectionNode = SectionPos.blockToSection(blockNode);
/*  28 */     if (!((BlockLightSectionStorage)this.storage).storingLightForSection(sectionNode)) {
/*     */       return;
/*     */     }
/*  31 */     BlockState state = getState(this.mutablePos.set(blockNode));
/*  32 */     int lightEmission = getEmission(blockNode, state);
/*  33 */     int oldLevel = ((BlockLightSectionStorage)this.storage).getStoredLevel(blockNode);
/*  34 */     if (lightEmission < oldLevel) {
/*  35 */       ((BlockLightSectionStorage)this.storage).setStoredLevel(blockNode, 0);
/*  36 */       enqueueDecrease(blockNode, LightEngine.QueueEntry.decreaseAllDirections(oldLevel));
/*     */     } else {
/*  38 */       enqueueDecrease(blockNode, PULL_LIGHT_IN_ENTRY);
/*     */     } 
/*  40 */     if (lightEmission > 0) {
/*  41 */       enqueueIncrease(blockNode, LightEngine.QueueEntry.increaseLightFromEmission(lightEmission, isEmptyShape(state)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void propagateIncrease(long fromNode, long increaseData, int fromLevel) {
/*  47 */     BlockState fromState = null;
/*  48 */     for (Direction propagationDirection : PROPAGATION_DIRECTIONS) {
/*  49 */       if (LightEngine.QueueEntry.shouldPropagateInDirection(increaseData, propagationDirection)) {
/*     */ 
/*     */         
/*  52 */         long toNode = BlockPos.offset(fromNode, propagationDirection);
/*  53 */         if (((BlockLightSectionStorage)this.storage).storingLightForSection(SectionPos.blockToSection(toNode))) {
/*     */ 
/*     */ 
/*     */           
/*  57 */           int toLevel = ((BlockLightSectionStorage)this.storage).getStoredLevel(toNode);
/*  58 */           int maxPossibleNewToLevel = fromLevel - 1;
/*  59 */           if (maxPossibleNewToLevel > toLevel) {
/*     */ 
/*     */ 
/*     */             
/*  63 */             this.mutablePos.set(toNode);
/*  64 */             BlockState toState = getState(this.mutablePos);
/*  65 */             int newToLevel = fromLevel - getOpacity(toState);
/*  66 */             if (newToLevel > toLevel) {
/*     */ 
/*     */ 
/*     */               
/*  70 */               if (fromState == null) {
/*  71 */                 fromState = LightEngine.QueueEntry.isFromEmptyShape(increaseData) ? Blocks.AIR.defaultBlockState() : getState(this.mutablePos.set(fromNode));
/*     */               }
/*  73 */               if (!shapeOccludes(fromState, toState, propagationDirection)) {
/*  74 */                 ((BlockLightSectionStorage)this.storage).setStoredLevel(toNode, newToLevel);
/*  75 */                 if (newToLevel > 1)
/*  76 */                   enqueueIncrease(toNode, LightEngine.QueueEntry.increaseSkipOneDirection(newToLevel, isEmptyShape(toState), propagationDirection.getOpposite())); 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } protected void propagateDecrease(long fromNode, long decreaseData) {
/*  84 */     int oldFromLevel = LightEngine.QueueEntry.getFromLevel(decreaseData);
/*  85 */     for (Direction propagationDirection : PROPAGATION_DIRECTIONS) {
/*  86 */       if (LightEngine.QueueEntry.shouldPropagateInDirection(decreaseData, propagationDirection)) {
/*     */ 
/*     */         
/*  89 */         long toNode = BlockPos.offset(fromNode, propagationDirection);
/*  90 */         if (((BlockLightSectionStorage)this.storage).storingLightForSection(SectionPos.blockToSection(toNode))) {
/*     */ 
/*     */ 
/*     */           
/*  94 */           int toLevel = ((BlockLightSectionStorage)this.storage).getStoredLevel(toNode);
/*  95 */           if (toLevel != 0)
/*     */           {
/*     */ 
/*     */             
/*  99 */             if (toLevel <= oldFromLevel - 1) {
/* 100 */               BlockState toState = getState(this.mutablePos.set(toNode));
/* 101 */               int toEmission = getEmission(toNode, toState);
/* 102 */               ((BlockLightSectionStorage)this.storage).setStoredLevel(toNode, 0);
/* 103 */               if (toEmission < toLevel) {
/* 104 */                 enqueueDecrease(toNode, LightEngine.QueueEntry.decreaseSkipOneDirection(toLevel, propagationDirection.getOpposite()));
/*     */               }
/* 106 */               if (toEmission > 0) {
/* 107 */                 enqueueIncrease(toNode, LightEngine.QueueEntry.increaseLightFromEmission(toEmission, isEmptyShape(toState)));
/*     */               }
/*     */             } else {
/* 110 */               enqueueIncrease(toNode, LightEngine.QueueEntry.increaseOnlyOneDirection(toLevel, false, propagationDirection.getOpposite()));
/*     */             }  } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } private int getEmission(long blockNode, BlockState state) {
/* 116 */     int emission = state.getLightEmission();
/* 117 */     if (emission > 0 && ((BlockLightSectionStorage)this.storage).lightOnInSection(SectionPos.blockToSection(blockNode))) {
/* 118 */       return emission;
/*     */     }
/* 120 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void propagateLightSources(ChunkPos pos) {
/* 125 */     setLightEnabled(pos, true);
/* 126 */     LightChunk chunk = this.chunkSource.getChunkForLighting(pos.x, pos.z);
/* 127 */     if (chunk != null)
/* 128 */       chunk.findBlockLightSources((lightPos, state) -> {
/* 129 */             int lightEmission = state.getLightEmission();
/* 130 */             enqueueIncrease(lightPos.asLong(), LightEngine.QueueEntry.increaseLightFromEmission(lightEmission, isEmptyShape(state)));
/*     */           }); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\lighting\BlockLightEngine.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
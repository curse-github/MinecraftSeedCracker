/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ 
/*     */ public class LevelLightEngine
/*     */   implements LightEventListener
/*     */ {
/*     */   public static final int LIGHT_SECTION_PADDING = 1;
/*  15 */   public static final LevelLightEngine EMPTY = new LevelLightEngine();
/*     */   
/*     */   protected final LevelHeightAccessor levelHeightAccessor;
/*     */   private final LightEngine<?, ?> blockEngine;
/*     */   private final LightEngine<?, ?> skyEngine;
/*     */   
/*     */   public LevelLightEngine(LightChunkGetter chunkSource, boolean hasBlockLight, boolean hasSkyLight) {
/*  22 */     this.levelHeightAccessor = chunkSource.getLevel();
/*  23 */     this.blockEngine = hasBlockLight ? new BlockLightEngine(chunkSource) : null;
/*  24 */     this.skyEngine = hasSkyLight ? new SkyLightEngine(chunkSource) : null;
/*     */   }
/*     */   
/*     */   private LevelLightEngine() {
/*  28 */     this.levelHeightAccessor = LevelHeightAccessor.create(0, 0);
/*  29 */     this.blockEngine = null;
/*  30 */     this.skyEngine = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkBlock(BlockPos pos) {
/*  36 */     if (this.blockEngine != null) {
/*  37 */       this.blockEngine.checkBlock(pos);
/*     */     }
/*  39 */     if (this.skyEngine != null) {
/*  40 */       this.skyEngine.checkBlock(pos);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLightWork() {
/*  47 */     if (this.skyEngine != null && this.skyEngine.hasLightWork()) {
/*  48 */       return true;
/*     */     }
/*  50 */     return (this.blockEngine != null && this.blockEngine.hasLightWork());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int runLightUpdates() {
/*  56 */     int count = 0;
/*  57 */     if (this.blockEngine != null) {
/*  58 */       count += this.blockEngine.runLightUpdates();
/*     */     }
/*  60 */     if (this.skyEngine != null) {
/*  61 */       count += this.skyEngine.runLightUpdates();
/*     */     }
/*  63 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSectionStatus(SectionPos pos, boolean sectionEmpty) {
/*  72 */     if (this.blockEngine != null) {
/*  73 */       this.blockEngine.updateSectionStatus(pos, sectionEmpty);
/*     */     }
/*  75 */     if (this.skyEngine != null) {
/*  76 */       this.skyEngine.updateSectionStatus(pos, sectionEmpty);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLightEnabled(ChunkPos pos, boolean enable) {
/*  83 */     if (this.blockEngine != null) {
/*  84 */       this.blockEngine.setLightEnabled(pos, enable);
/*     */     }
/*  86 */     if (this.skyEngine != null) {
/*  87 */       this.skyEngine.setLightEnabled(pos, enable);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void propagateLightSources(ChunkPos pos) {
/*  94 */     if (this.blockEngine != null) {
/*  95 */       this.blockEngine.propagateLightSources(pos);
/*     */     }
/*  97 */     if (this.skyEngine != null) {
/*  98 */       this.skyEngine.propagateLightSources(pos);
/*     */     }
/*     */   }
/*     */   
/*     */   public LayerLightEventListener getLayerListener(LightLayer layer) {
/* 103 */     if (layer == LightLayer.BLOCK) {
/* 104 */       if (this.blockEngine == null) {
/* 105 */         return LayerLightEventListener.DummyLightLayerEventListener.INSTANCE;
/*     */       }
/* 107 */       return this.blockEngine;
/*     */     } 
/* 109 */     if (this.skyEngine == null) {
/* 110 */       return LayerLightEventListener.DummyLightLayerEventListener.INSTANCE;
/*     */     }
/* 112 */     return this.skyEngine;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDebugData(LightLayer layer, SectionPos pos) {
/* 117 */     if (layer == LightLayer.BLOCK) {
/* 118 */       if (this.blockEngine != null) {
/* 119 */         return this.blockEngine.getDebugData(pos.asLong());
/*     */       }
/*     */     }
/* 122 */     else if (this.skyEngine != null) {
/* 123 */       return this.skyEngine.getDebugData(pos.asLong());
/*     */     } 
/*     */     
/* 126 */     return "n/a";
/*     */   }
/*     */   
/*     */   public LayerLightSectionStorage.SectionType getDebugSectionType(LightLayer layer, SectionPos pos) {
/* 130 */     if (layer == LightLayer.BLOCK) {
/* 131 */       if (this.blockEngine != null) {
/* 132 */         return this.blockEngine.getDebugSectionType(pos.asLong());
/*     */       }
/*     */     }
/* 135 */     else if (this.skyEngine != null) {
/* 136 */       return this.skyEngine.getDebugSectionType(pos.asLong());
/*     */     } 
/*     */     
/* 139 */     return LayerLightSectionStorage.SectionType.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void queueSectionData(LightLayer layer, SectionPos pos, DataLayer data) {
/* 147 */     if (layer == LightLayer.BLOCK) {
/* 148 */       if (this.blockEngine != null) {
/* 149 */         this.blockEngine.queueSectionData(pos.asLong(), data);
/*     */       }
/*     */     }
/* 152 */     else if (this.skyEngine != null) {
/* 153 */       this.skyEngine.queueSectionData(pos.asLong(), data);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void retainData(ChunkPos pos, boolean retain) {
/* 159 */     if (this.blockEngine != null) {
/* 160 */       this.blockEngine.retainData(pos, retain);
/*     */     }
/* 162 */     if (this.skyEngine != null) {
/* 163 */       this.skyEngine.retainData(pos, retain);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getRawBrightness(BlockPos pos, int skyDampen) {
/* 168 */     int skyLight = (this.skyEngine == null) ? 0 : (this.skyEngine.getLightValue(pos) - skyDampen);
/* 169 */     int blockLight = (this.blockEngine == null) ? 0 : this.blockEngine.getLightValue(pos);
/*     */     
/* 171 */     return Math.max(blockLight, skyLight);
/*     */   }
/*     */   
/*     */   public boolean lightOnInColumn(long sectionZeroNode) {
/* 175 */     return (this.blockEngine == null || (this.blockEngine.storage.lightOnInColumn(sectionZeroNode) && (this.skyEngine == null || this.skyEngine.storage
/* 176 */       .lightOnInColumn(sectionZeroNode))));
/*     */   }
/*     */ 
/*     */   
/* 180 */   public int getLightSectionCount() { return this.levelHeightAccessor.getSectionsCount() + 2; }
/*     */ 
/*     */ 
/*     */   
/* 184 */   public int getMinLightSection() { return this.levelHeightAccessor.getMinSectionY() - 1; }
/*     */ 
/*     */ 
/*     */   
/* 188 */   public int getMaxLightSection() { return getMinLightSection() + getLightSectionCount(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\lighting\LevelLightEngine.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ 
/*     */ 
/*     */ public class StructurePlaceSettings
/*     */ {
/*  16 */   private Mirror mirror = Mirror.NONE;
/*  17 */   private Rotation rotation = Rotation.NONE;
/*  18 */   private BlockPos rotationPivot = BlockPos.ZERO;
/*     */   private boolean ignoreEntities;
/*     */   private BoundingBox boundingBox;
/*  21 */   private LiquidSettings liquidSettings = LiquidSettings.APPLY_WATERLOGGING;
/*     */   private RandomSource random;
/*     */   private int palette;
/*  24 */   private final List<StructureProcessor> processors = Lists.newArrayList();
/*     */   private boolean knownShape;
/*     */   private boolean finalizeEntities;
/*     */   
/*     */   public StructurePlaceSettings copy() {
/*  29 */     StructurePlaceSettings setting = new StructurePlaceSettings();
/*  30 */     setting.mirror = this.mirror;
/*  31 */     setting.rotation = this.rotation;
/*  32 */     setting.rotationPivot = this.rotationPivot;
/*  33 */     setting.ignoreEntities = this.ignoreEntities;
/*  34 */     setting.boundingBox = this.boundingBox;
/*  35 */     setting.liquidSettings = this.liquidSettings;
/*  36 */     setting.random = this.random;
/*  37 */     setting.palette = this.palette;
/*  38 */     setting.processors.addAll(this.processors);
/*  39 */     setting.knownShape = this.knownShape;
/*  40 */     setting.finalizeEntities = this.finalizeEntities;
/*  41 */     return setting;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setMirror(Mirror mirror) {
/*  45 */     this.mirror = mirror;
/*  46 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setRotation(Rotation rotation) {
/*  50 */     this.rotation = rotation;
/*  51 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setRotationPivot(BlockPos rotationPivot) {
/*  55 */     this.rotationPivot = rotationPivot;
/*  56 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setIgnoreEntities(boolean ignoreEntities) {
/*  60 */     this.ignoreEntities = ignoreEntities;
/*  61 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setBoundingBox(BoundingBox boundingBox) {
/*  65 */     this.boundingBox = boundingBox;
/*  66 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setRandom(RandomSource random) {
/*  70 */     this.random = random;
/*  71 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setLiquidSettings(LiquidSettings liquidSettings) {
/*  75 */     this.liquidSettings = liquidSettings;
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setKnownShape(boolean knownShape) {
/*  80 */     this.knownShape = knownShape;
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings clearProcessors() {
/*  85 */     this.processors.clear();
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings addProcessor(StructureProcessor processor) {
/*  90 */     this.processors.add(processor);
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings popProcessor(StructureProcessor processor) {
/*  95 */     this.processors.remove(processor);
/*  96 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 100 */   public Mirror getMirror() { return this.mirror; }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public Rotation getRotation() { return this.rotation; }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public BlockPos getRotationPivot() { return this.rotationPivot; }
/*     */ 
/*     */   
/*     */   public RandomSource getRandom(BlockPos pos) {
/* 112 */     if (this.random != null) {
/* 113 */       return this.random;
/*     */     }
/*     */     
/* 116 */     if (pos == null) {
/* 117 */       return RandomSource.create(Util.getMillis());
/*     */     }
/*     */     
/* 120 */     return RandomSource.create(Mth.getSeed(pos));
/*     */   }
/*     */ 
/*     */   
/* 124 */   public boolean isIgnoreEntities() { return this.ignoreEntities; }
/*     */ 
/*     */ 
/*     */   
/* 128 */   public BoundingBox getBoundingBox() { return this.boundingBox; }
/*     */ 
/*     */ 
/*     */   
/* 132 */   public boolean getKnownShape() { return this.knownShape; }
/*     */ 
/*     */ 
/*     */   
/* 136 */   public List<StructureProcessor> getProcessors() { return this.processors; }
/*     */ 
/*     */ 
/*     */   
/* 140 */   public boolean shouldApplyWaterlogging() { return (this.liquidSettings == LiquidSettings.APPLY_WATERLOGGING); }
/*     */ 
/*     */   
/*     */   public StructureTemplate.Palette getRandomPalette(List<StructureTemplate.Palette> palettes, BlockPos pos) {
/* 144 */     int paletteSize = palettes.size();
/* 145 */     if (paletteSize == 0)
/*     */     {
/* 147 */       throw new IllegalStateException("No palettes");
/*     */     }
/* 149 */     return (StructureTemplate.Palette)palettes.get(getRandom(pos).nextInt(paletteSize));
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setFinalizeEntities(boolean finalizeEntities) {
/* 153 */     this.finalizeEntities = finalizeEntities;
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 158 */   public boolean shouldFinalizeEntities() { return this.finalizeEntities; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructurePlaceSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
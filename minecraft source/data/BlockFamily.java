/*     */ package net.minecraft.data;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ 
/*     */ public class BlockFamily
/*     */ {
/*     */   private final Block baseBlock;
/*     */   private final Map<Variant, Block> variants;
/*     */   private boolean generateModel;
/*     */   private boolean generateRecipe;
/*     */   private String recipeGroupPrefix;
/*     */   private String recipeUnlockedBy;
/*     */   
/*     */   public enum Variant
/*     */   {
/*  21 */     BUTTON("button"),
/*  22 */     CHISELED("chiseled"),
/*  23 */     CRACKED("cracked"),
/*  24 */     CUT("cut"),
/*  25 */     DOOR("door"),
/*  26 */     CUSTOM_FENCE("fence"),
/*  27 */     FENCE("fence"),
/*  28 */     CUSTOM_FENCE_GATE("fence_gate"),
/*  29 */     FENCE_GATE("fence_gate"),
/*  30 */     MOSAIC("mosaic"),
/*  31 */     SIGN("sign"),
/*  32 */     SLAB("slab"),
/*  33 */     STAIRS("stairs"),
/*  34 */     PRESSURE_PLATE("pressure_plate"),
/*  35 */     POLISHED("polished"),
/*  36 */     TRAPDOOR("trapdoor"),
/*  37 */     WALL("wall"),
/*  38 */     WALL_SIGN("wall_sign");
/*     */     
/*     */     private final String recipeGroup;
/*     */ 
/*     */     
/*  43 */     Variant(String recipeGroup) { this.recipeGroup = recipeGroup; }
/*     */ 
/*     */ 
/*     */     
/*  47 */     public String getRecipeGroup() { return this.recipeGroup; } }
/*     */   private BlockFamily(Block baseBlock) {
/*     */     this.variants = Maps.newHashMap();
/*     */     this.generateModel = true;
/*     */     this.generateRecipe = true;
/*  52 */     this.baseBlock = baseBlock;
/*     */   }
/*     */ 
/*     */   
/*  56 */   public Block getBaseBlock() { return this.baseBlock; }
/*     */ 
/*     */ 
/*     */   
/*  60 */   public Map<Variant, Block> getVariants() { return this.variants; }
/*     */ 
/*     */ 
/*     */   
/*  64 */   public Block get(Variant variant) { return (Block)this.variants.get(variant); }
/*     */ 
/*     */ 
/*     */   
/*  68 */   public boolean shouldGenerateModel() { return this.generateModel; }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public boolean shouldGenerateRecipe() { return this.generateRecipe; }
/*     */ 
/*     */   
/*     */   public Optional<String> getRecipeGroupPrefix() {
/*  76 */     if (StringUtil.isBlank(this.recipeGroupPrefix)) {
/*  77 */       return Optional.empty();
/*     */     }
/*  79 */     return Optional.of(this.recipeGroupPrefix);
/*     */   }
/*     */   
/*     */   public Optional<String> getRecipeUnlockedBy() {
/*  83 */     if (StringUtil.isBlank(this.recipeUnlockedBy)) {
/*  84 */       return Optional.empty();
/*     */     }
/*  86 */     return Optional.of(this.recipeUnlockedBy);
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final BlockFamily family;
/*     */     
/*  93 */     public Builder(Block baseBlock) { this.family = new BlockFamily(baseBlock); }
/*     */ 
/*     */ 
/*     */     
/*  97 */     public BlockFamily getFamily() { return this.family; }
/*     */ 
/*     */     
/*     */     public Builder button(Block button) {
/* 101 */       this.family.variants.put(BlockFamily.Variant.BUTTON, button);
/* 102 */       return this;
/*     */     }
/*     */     
/*     */     public Builder chiseled(Block chiseled) {
/* 106 */       this.family.variants.put(BlockFamily.Variant.CHISELED, chiseled);
/* 107 */       return this;
/*     */     }
/*     */     
/*     */     public Builder mosaic(Block mosaic) {
/* 111 */       this.family.variants.put(BlockFamily.Variant.MOSAIC, mosaic);
/* 112 */       return this;
/*     */     }
/*     */     
/*     */     public Builder cracked(Block cracked) {
/* 116 */       this.family.variants.put(BlockFamily.Variant.CRACKED, cracked);
/* 117 */       return this;
/*     */     }
/*     */     
/*     */     public Builder cut(Block cut) {
/* 121 */       this.family.variants.put(BlockFamily.Variant.CUT, cut);
/* 122 */       return this;
/*     */     }
/*     */     
/*     */     public Builder door(Block door) {
/* 126 */       this.family.variants.put(BlockFamily.Variant.DOOR, door);
/* 127 */       return this;
/*     */     }
/*     */     
/*     */     public Builder customFence(Block fence) {
/* 131 */       this.family.variants.put(BlockFamily.Variant.CUSTOM_FENCE, fence);
/* 132 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder fence(Block fence) {
/* 137 */       this.family.variants.put(BlockFamily.Variant.FENCE, fence);
/* 138 */       return this;
/*     */     }
/*     */     
/*     */     public Builder customFenceGate(Block fenceGate) {
/* 142 */       this.family.variants.put(BlockFamily.Variant.CUSTOM_FENCE_GATE, fenceGate);
/* 143 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder fenceGate(Block fenceGate) {
/* 148 */       this.family.variants.put(BlockFamily.Variant.FENCE_GATE, fenceGate);
/* 149 */       return this;
/*     */     }
/*     */     
/*     */     public Builder sign(Block sign, Block wallSign) {
/* 153 */       this.family.variants.put(BlockFamily.Variant.SIGN, sign);
/* 154 */       this.family.variants.put(BlockFamily.Variant.WALL_SIGN, wallSign);
/* 155 */       return this;
/*     */     }
/*     */     
/*     */     public Builder slab(Block slab) {
/* 159 */       this.family.variants.put(BlockFamily.Variant.SLAB, slab);
/* 160 */       return this;
/*     */     }
/*     */     
/*     */     public Builder stairs(Block stairs) {
/* 164 */       this.family.variants.put(BlockFamily.Variant.STAIRS, stairs);
/* 165 */       return this;
/*     */     }
/*     */     
/*     */     public Builder pressurePlate(Block pressurePlate) {
/* 169 */       this.family.variants.put(BlockFamily.Variant.PRESSURE_PLATE, pressurePlate);
/* 170 */       return this;
/*     */     }
/*     */     
/*     */     public Builder polished(Block polished) {
/* 174 */       this.family.variants.put(BlockFamily.Variant.POLISHED, polished);
/* 175 */       return this;
/*     */     }
/*     */     
/*     */     public Builder trapdoor(Block trapdoor) {
/* 179 */       this.family.variants.put(BlockFamily.Variant.TRAPDOOR, trapdoor);
/* 180 */       return this;
/*     */     }
/*     */     
/*     */     public Builder wall(Block wall) {
/* 184 */       this.family.variants.put(BlockFamily.Variant.WALL, wall);
/* 185 */       return this;
/*     */     }
/*     */     
/*     */     public Builder dontGenerateModel() {
/* 189 */       this.family.generateModel = false;
/* 190 */       return this;
/*     */     }
/*     */     
/*     */     public Builder dontGenerateRecipe() {
/* 194 */       this.family.generateRecipe = false;
/* 195 */       return this;
/*     */     }
/*     */     
/*     */     public Builder recipeGroupPrefix(String recipeGroupPrefix) {
/* 199 */       this.family.recipeGroupPrefix = recipeGroupPrefix;
/* 200 */       return this;
/*     */     }
/*     */     
/*     */     public Builder recipeUnlockedBy(String recipeUnlockedBy) {
/* 204 */       this.family.recipeUnlockedBy = recipeUnlockedBy;
/* 205 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\BlockFamily.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
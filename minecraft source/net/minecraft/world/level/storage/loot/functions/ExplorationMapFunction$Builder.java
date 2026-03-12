/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.saveddata.maps.MapDecorationType;
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
/*     */ public class Builder
/*     */   extends LootItemConditionalFunction.Builder<ExplorationMapFunction.Builder>
/*     */ {
/*  93 */   private TagKey<Structure> destination = ExplorationMapFunction.DEFAULT_DESTINATION;
/*  94 */   private Holder<MapDecorationType> mapDecoration = ExplorationMapFunction.DEFAULT_DECORATION;
/*  95 */   private byte zoom = 2;
/*  96 */   private int searchRadius = 50;
/*     */   
/*     */   private boolean skipKnownStructures = true;
/*     */ 
/*     */   
/* 101 */   protected Builder getThis() { return this; }
/*     */ 
/*     */   
/*     */   public Builder setDestination(TagKey<Structure> destination) {
/* 105 */     this.destination = destination;
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setMapDecoration(Holder<MapDecorationType> mapDecoration) {
/* 110 */     this.mapDecoration = mapDecoration;
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setZoom(byte zoom) {
/* 115 */     this.zoom = zoom;
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSearchRadius(int searchRadius) {
/* 120 */     this.searchRadius = searchRadius;
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSkipKnownStructures(boolean skipKnownStructures) {
/* 125 */     this.skipKnownStructures = skipKnownStructures;
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 131 */   public LootItemFunction build() { return new ExplorationMapFunction(getConditions(), this.destination, this.mapDecoration, this.zoom, this.searchRadius, this.skipKnownStructures); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\ExplorationMapFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/* 65 */   private final ImmutableList.Builder<BannerPatternLayers.Layer> layers = ImmutableList.builder();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public Builder addIfRegistered(HolderGetter<BannerPattern> patternGetter, ResourceKey<BannerPattern> patternKey, DyeColor color) {
/* 72 */     Optional<Holder.Reference<BannerPattern>> pattern = patternGetter.get(patternKey);
/* 73 */     if (pattern.isEmpty()) {
/* 74 */       BannerPatternLayers.LOGGER.warn("Unable to find banner pattern with id: '{}'", patternKey.identifier());
/* 75 */       return this;
/*    */     } 
/* 77 */     return add((Holder)pattern.get(), color);
/*    */   }
/*    */ 
/*    */   
/* 81 */   public Builder add(Holder<BannerPattern> pattern, DyeColor color) { return add(new BannerPatternLayers.Layer(pattern, color)); }
/*    */ 
/*    */   
/*    */   public Builder add(BannerPatternLayers.Layer layer) {
/* 85 */     this.layers.add(layer);
/* 86 */     return this;
/*    */   }
/*    */   
/*    */   public Builder addAll(BannerPatternLayers layers) {
/* 90 */     this.layers.addAll(layers.layers);
/* 91 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 95 */   public BannerPatternLayers build() { return new BannerPatternLayers(this.layers.build()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BannerPatternLayers$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
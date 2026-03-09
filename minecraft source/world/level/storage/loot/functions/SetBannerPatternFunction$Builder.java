/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.entity.BannerPattern;
/*    */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
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
/*    */   extends LootItemConditionalFunction.Builder<SetBannerPatternFunction.Builder>
/*    */ {
/*    */   private final BannerPatternLayers.Builder patterns;
/*    */   private final boolean append;
/*    */   
/*    */   private Builder(boolean append) {
/* 50 */     this.patterns = new BannerPatternLayers.Builder();
/*    */ 
/*    */ 
/*    */     
/* 54 */     this.append = append;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 59 */   protected Builder getThis() { return this; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   public LootItemFunction build() { return new SetBannerPatternFunction(getConditions(), this.patterns.build(), this.append); }
/*    */ 
/*    */   
/*    */   public Builder addPattern(Holder<BannerPattern> pattern, DyeColor color) {
/* 68 */     this.patterns.add(pattern, color);
/* 69 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetBannerPatternFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
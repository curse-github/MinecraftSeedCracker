/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BannerPattern;
/*    */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetBannerPatternFunction extends LootItemConditionalFunction {
/* 18 */   public static final MapCodec<SetBannerPatternFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(BannerPatternLayers.CODEC
/* 19 */           .fieldOf("patterns").forGetter(()), Codec.BOOL
/* 20 */           .fieldOf("append").forGetter(())))
/* 21 */       .apply(i, SetBannerPatternFunction::new));
/*    */   
/*    */   private final BannerPatternLayers patterns;
/*    */   private final boolean append;
/*    */   
/*    */   private SetBannerPatternFunction(List<LootItemCondition> predicates, BannerPatternLayers patterns, boolean append) {
/* 27 */     super(predicates);
/* 28 */     this.patterns = patterns;
/* 29 */     this.append = append;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/* 34 */     if (this.append) {
/* 35 */       itemStack.update(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY, this.patterns, (base, appended) -> (
/* 36 */           new BannerPatternLayers.Builder()).addAll(base).addAll(appended).build());
/*    */     } else {
/*    */       
/* 39 */       itemStack.set(DataComponents.BANNER_PATTERNS, this.patterns);
/*    */     } 
/* 41 */     return itemStack;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public LootItemFunctionType<SetBannerPatternFunction> getType() { return LootItemFunctions.SET_BANNER_PATTERN; }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*    */     private Builder(boolean append) {
/* 50 */       this.patterns = new BannerPatternLayers.Builder();
/*    */ 
/*    */ 
/*    */       
/* 54 */       this.append = append;
/*    */     }
/*    */     private final BannerPatternLayers.Builder patterns;
/*    */     private final boolean append;
/*    */     
/* 59 */     protected Builder getThis() { return this; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 64 */     public LootItemFunction build() { return new SetBannerPatternFunction(getConditions(), this.patterns.build(), this.append); }
/*    */ 
/*    */     
/*    */     public Builder addPattern(Holder<BannerPattern> pattern, DyeColor color) {
/* 68 */       this.patterns.add(pattern, color);
/* 69 */       return this;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 74 */   public static Builder setBannerPattern(boolean append) { return new Builder(append); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetBannerPatternFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
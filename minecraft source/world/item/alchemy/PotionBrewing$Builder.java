/*     */ package net.minecraft.world.item.alchemy;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
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
/*     */ {
/*     */   private final List<Ingredient> containers;
/*     */   private final List<PotionBrewing.Mix<Potion>> potionMixes;
/*     */   private final List<PotionBrewing.Mix<Item>> containerMixes;
/*     */   private final FeatureFlagSet enabledFeatures;
/*     */   
/*     */   public Builder(FeatureFlagSet enabledFeatures) {
/* 226 */     this.containers = new ArrayList();
/* 227 */     this.potionMixes = new ArrayList();
/* 228 */     this.containerMixes = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     this.enabledFeatures = enabledFeatures;
/*     */   }
/*     */   
/*     */   private static void expectPotion(Item from) {
/* 237 */     if (!(from instanceof net.minecraft.world.item.PotionItem)) {
/* 238 */       throw new IllegalArgumentException("Expected a potion, got: " + String.valueOf(BuiltInRegistries.ITEM.getKey(from)));
/*     */     }
/*     */   }
/*     */   
/*     */   public void addContainerRecipe(Item from, Item ingredient, Item to) {
/* 243 */     if (!from.isEnabled(this.enabledFeatures) || !ingredient.isEnabled(this.enabledFeatures) || !to.isEnabled(this.enabledFeatures)) {
/*     */       return;
/*     */     }
/*     */     
/* 247 */     expectPotion(from);
/* 248 */     expectPotion(to);
/* 249 */     this.containerMixes.add(new PotionBrewing.Mix(from.builtInRegistryHolder(), Ingredient.of(ingredient), to.builtInRegistryHolder()));
/*     */   }
/*     */   
/*     */   public void addContainer(Item item) {
/* 253 */     if (!item.isEnabled(this.enabledFeatures)) {
/*     */       return;
/*     */     }
/* 256 */     expectPotion(item);
/* 257 */     this.containers.add(Ingredient.of(item));
/*     */   }
/*     */   
/*     */   public void addMix(Holder<Potion> from, Item ingredient, Holder<Potion> to) {
/* 261 */     if (((Potion)from.value()).isEnabled(this.enabledFeatures) && ingredient.isEnabled(this.enabledFeatures) && ((Potion)to.value()).isEnabled(this.enabledFeatures)) {
/* 262 */       this.potionMixes.add(new PotionBrewing.Mix(from, Ingredient.of(ingredient), to));
/*     */     }
/*     */   }
/*     */   
/*     */   public void addStartMix(Item ingredient, Holder<Potion> potion) {
/* 267 */     if (((Potion)potion.value()).isEnabled(this.enabledFeatures)) {
/* 268 */       addMix(Potions.WATER, ingredient, Potions.MUNDANE);
/* 269 */       addMix(Potions.AWKWARD, ingredient, potion);
/*     */     } 
/*     */   }
/*     */   
/*     */   public PotionBrewing build() {
/* 274 */     return new PotionBrewing(
/* 275 */         List.copyOf(this.containers), 
/* 276 */         List.copyOf(this.potionMixes), 
/* 277 */         List.copyOf(this.containerMixes));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\alchemy\PotionBrewing$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
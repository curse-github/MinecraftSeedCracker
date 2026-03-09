/*     */ package net.minecraft.world.item.alchemy;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PotionBrewing
/*     */ {
/*     */   public static final int BREWING_TIME_SECONDS = 20;
/*  20 */   public static final PotionBrewing EMPTY = new PotionBrewing(
/*  21 */       List.of(), 
/*  22 */       List.of(), 
/*  23 */       List.of());
/*     */   
/*     */   private final List<Ingredient> containers;
/*     */   
/*     */   private final List<Mix<Potion>> potionMixes;
/*     */   private final List<Mix<Item>> containerMixes;
/*     */   
/*     */   private PotionBrewing(List<Ingredient> containers, List<Mix<Potion>> potionMixes, List<Mix<Item>> containerMixes) {
/*  31 */     this.containers = containers;
/*  32 */     this.potionMixes = potionMixes;
/*  33 */     this.containerMixes = containerMixes;
/*     */   }
/*     */ 
/*     */   
/*  37 */   public boolean isIngredient(ItemStack ingredient) { return (isContainerIngredient(ingredient) || isPotionIngredient(ingredient)); }
/*     */ 
/*     */   
/*     */   private boolean isContainer(ItemStack input) {
/*  41 */     for (Ingredient allowedContainer : this.containers) {
/*  42 */       if (allowedContainer.test(input)) {
/*  43 */         return true;
/*     */       }
/*     */     } 
/*  46 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isContainerIngredient(ItemStack ingredient) {
/*  50 */     for (Mix<Item> containerMix : this.containerMixes) {
/*  51 */       if (containerMix.ingredient.test(ingredient)) {
/*  52 */         return true;
/*     */       }
/*     */     } 
/*  55 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPotionIngredient(ItemStack ingredient) {
/*  59 */     for (Mix<Potion> potionMix : this.potionMixes) {
/*  60 */       if (potionMix.ingredient.test(ingredient)) {
/*  61 */         return true;
/*     */       }
/*     */     } 
/*  64 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isBrewablePotion(Holder<Potion> potion) {
/*  68 */     for (Mix<Potion> mix : this.potionMixes) {
/*  69 */       if (mix.to.is(potion)) {
/*  70 */         return true;
/*     */       }
/*     */     } 
/*  73 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasMix(ItemStack source, ItemStack ingredient) {
/*  77 */     if (!isContainer(source)) {
/*  78 */       return false;
/*     */     }
/*     */     
/*  81 */     return (hasContainerMix(source, ingredient) || hasPotionMix(source, ingredient));
/*     */   }
/*     */   
/*     */   public boolean hasContainerMix(ItemStack source, ItemStack ingredient) {
/*  85 */     for (Mix<Item> mix : this.containerMixes) {
/*  86 */       if (source.is(mix.from) && mix.ingredient.test(ingredient)) {
/*  87 */         return true;
/*     */       }
/*     */     } 
/*  90 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasPotionMix(ItemStack source, ItemStack ingredient) {
/*  94 */     Optional<Holder<Potion>> potion = ((PotionContents)source.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)).potion();
/*  95 */     if (potion.isEmpty()) {
/*  96 */       return false;
/*     */     }
/*  98 */     for (Mix<Potion> mix : this.potionMixes) {
/*  99 */       if (mix.from.is((Holder)potion.get()) && mix.ingredient.test(ingredient)) {
/* 100 */         return true;
/*     */       }
/*     */     } 
/* 103 */     return false;
/*     */   }
/*     */   
/*     */   public ItemStack mix(ItemStack ingredient, ItemStack source) {
/* 107 */     if (source.isEmpty()) {
/* 108 */       return source;
/*     */     }
/*     */     
/* 111 */     Optional<Holder<Potion>> potion = ((PotionContents)source.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)).potion();
/* 112 */     if (potion.isEmpty()) {
/* 113 */       return source;
/*     */     }
/*     */     
/* 116 */     for (Mix<Item> mix : this.containerMixes) {
/* 117 */       if (source.is(mix.from) && mix.ingredient.test(ingredient)) {
/* 118 */         return PotionContents.createItemStack((Item)mix.to.value(), (Holder)potion.get());
/*     */       }
/*     */     } 
/*     */     
/* 122 */     for (Mix<Potion> mix : this.potionMixes) {
/* 123 */       if (mix.from.is((Holder)potion.get()) && mix.ingredient.test(ingredient)) {
/* 124 */         return PotionContents.createItemStack(source.getItem(), mix.to);
/*     */       }
/*     */     } 
/*     */     
/* 128 */     return source;
/*     */   }
/*     */   
/*     */   public static PotionBrewing bootstrap(FeatureFlagSet enabledFeatures) {
/* 132 */     Builder builder = new Builder(enabledFeatures);
/* 133 */     addVanillaMixes(builder);
/* 134 */     return builder.build();
/*     */   }
/*     */   
/*     */   public static void addVanillaMixes(Builder builder) {
/* 138 */     builder.addContainer(Items.POTION);
/* 139 */     builder.addContainer(Items.SPLASH_POTION);
/* 140 */     builder.addContainer(Items.LINGERING_POTION);
/*     */     
/* 142 */     builder.addContainerRecipe(Items.POTION, Items.GUNPOWDER, Items.SPLASH_POTION);
/* 143 */     builder.addContainerRecipe(Items.SPLASH_POTION, Items.DRAGON_BREATH, Items.LINGERING_POTION);
/*     */     
/* 145 */     builder.addMix(Potions.WATER, Items.GLOWSTONE_DUST, Potions.THICK);
/*     */     
/* 147 */     builder.addMix(Potions.WATER, Items.REDSTONE, Potions.MUNDANE);
/*     */     
/* 149 */     builder.addMix(Potions.WATER, Items.NETHER_WART, Potions.AWKWARD);
/*     */     
/* 151 */     builder.addStartMix(Items.BREEZE_ROD, Potions.WIND_CHARGED);
/*     */     
/* 153 */     builder.addStartMix(Items.SLIME_BLOCK, Potions.OOZING);
/*     */     
/* 155 */     builder.addStartMix(Items.STONE, Potions.INFESTED);
/*     */     
/* 157 */     builder.addStartMix(Items.COBWEB, Potions.WEAVING);
/*     */     
/* 159 */     builder.addMix(Potions.AWKWARD, Items.GOLDEN_CARROT, Potions.NIGHT_VISION);
/* 160 */     builder.addMix(Potions.NIGHT_VISION, Items.REDSTONE, Potions.LONG_NIGHT_VISION);
/*     */     
/* 162 */     builder.addMix(Potions.NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, Potions.INVISIBILITY);
/* 163 */     builder.addMix(Potions.LONG_NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, Potions.LONG_INVISIBILITY);
/*     */     
/* 165 */     builder.addMix(Potions.INVISIBILITY, Items.REDSTONE, Potions.LONG_INVISIBILITY);
/*     */     
/* 167 */     builder.addStartMix(Items.MAGMA_CREAM, Potions.FIRE_RESISTANCE);
/* 168 */     builder.addMix(Potions.FIRE_RESISTANCE, Items.REDSTONE, Potions.LONG_FIRE_RESISTANCE);
/*     */     
/* 170 */     builder.addStartMix(Items.RABBIT_FOOT, Potions.LEAPING);
/* 171 */     builder.addMix(Potions.LEAPING, Items.REDSTONE, Potions.LONG_LEAPING);
/* 172 */     builder.addMix(Potions.LEAPING, Items.GLOWSTONE_DUST, Potions.STRONG_LEAPING);
/*     */     
/* 174 */     builder.addMix(Potions.LEAPING, Items.FERMENTED_SPIDER_EYE, Potions.SLOWNESS);
/* 175 */     builder.addMix(Potions.LONG_LEAPING, Items.FERMENTED_SPIDER_EYE, Potions.LONG_SLOWNESS);
/*     */     
/* 177 */     builder.addMix(Potions.SLOWNESS, Items.REDSTONE, Potions.LONG_SLOWNESS);
/*     */     
/* 179 */     builder.addMix(Potions.SLOWNESS, Items.GLOWSTONE_DUST, Potions.STRONG_SLOWNESS);
/* 180 */     builder.addMix(Potions.AWKWARD, Items.TURTLE_HELMET, Potions.TURTLE_MASTER);
/* 181 */     builder.addMix(Potions.TURTLE_MASTER, Items.REDSTONE, Potions.LONG_TURTLE_MASTER);
/* 182 */     builder.addMix(Potions.TURTLE_MASTER, Items.GLOWSTONE_DUST, Potions.STRONG_TURTLE_MASTER);
/*     */     
/* 184 */     builder.addMix(Potions.SWIFTNESS, Items.FERMENTED_SPIDER_EYE, Potions.SLOWNESS);
/* 185 */     builder.addMix(Potions.LONG_SWIFTNESS, Items.FERMENTED_SPIDER_EYE, Potions.LONG_SLOWNESS);
/*     */     
/* 187 */     builder.addStartMix(Items.SUGAR, Potions.SWIFTNESS);
/* 188 */     builder.addMix(Potions.SWIFTNESS, Items.REDSTONE, Potions.LONG_SWIFTNESS);
/* 189 */     builder.addMix(Potions.SWIFTNESS, Items.GLOWSTONE_DUST, Potions.STRONG_SWIFTNESS);
/*     */     
/* 191 */     builder.addMix(Potions.AWKWARD, Items.PUFFERFISH, Potions.WATER_BREATHING);
/* 192 */     builder.addMix(Potions.WATER_BREATHING, Items.REDSTONE, Potions.LONG_WATER_BREATHING);
/*     */     
/* 194 */     builder.addStartMix(Items.GLISTERING_MELON_SLICE, Potions.HEALING);
/* 195 */     builder.addMix(Potions.HEALING, Items.GLOWSTONE_DUST, Potions.STRONG_HEALING);
/*     */     
/* 197 */     builder.addMix(Potions.HEALING, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
/* 198 */     builder.addMix(Potions.STRONG_HEALING, Items.FERMENTED_SPIDER_EYE, Potions.STRONG_HARMING);
/*     */     
/* 200 */     builder.addMix(Potions.HARMING, Items.GLOWSTONE_DUST, Potions.STRONG_HARMING);
/*     */     
/* 202 */     builder.addMix(Potions.POISON, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
/* 203 */     builder.addMix(Potions.LONG_POISON, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
/* 204 */     builder.addMix(Potions.STRONG_POISON, Items.FERMENTED_SPIDER_EYE, Potions.STRONG_HARMING);
/*     */     
/* 206 */     builder.addStartMix(Items.SPIDER_EYE, Potions.POISON);
/* 207 */     builder.addMix(Potions.POISON, Items.REDSTONE, Potions.LONG_POISON);
/* 208 */     builder.addMix(Potions.POISON, Items.GLOWSTONE_DUST, Potions.STRONG_POISON);
/*     */     
/* 210 */     builder.addStartMix(Items.GHAST_TEAR, Potions.REGENERATION);
/* 211 */     builder.addMix(Potions.REGENERATION, Items.REDSTONE, Potions.LONG_REGENERATION);
/* 212 */     builder.addMix(Potions.REGENERATION, Items.GLOWSTONE_DUST, Potions.STRONG_REGENERATION);
/*     */     
/* 214 */     builder.addStartMix(Items.BLAZE_POWDER, Potions.STRENGTH);
/* 215 */     builder.addMix(Potions.STRENGTH, Items.REDSTONE, Potions.LONG_STRENGTH);
/* 216 */     builder.addMix(Potions.STRENGTH, Items.GLOWSTONE_DUST, Potions.STRONG_STRENGTH);
/*     */     
/* 218 */     builder.addMix(Potions.WATER, Items.FERMENTED_SPIDER_EYE, Potions.WEAKNESS);
/* 219 */     builder.addMix(Potions.WEAKNESS, Items.REDSTONE, Potions.LONG_WEAKNESS);
/*     */     
/* 221 */     builder.addMix(Potions.AWKWARD, Items.PHANTOM_MEMBRANE, Potions.SLOW_FALLING);
/* 222 */     builder.addMix(Potions.SLOW_FALLING, Items.REDSTONE, Potions.LONG_SLOW_FALLING);
/*     */   }
/*     */   public static class Builder { private final List<Ingredient> containers; private final List<PotionBrewing.Mix<Potion>> potionMixes; private final List<PotionBrewing.Mix<Item>> containerMixes; private final FeatureFlagSet enabledFeatures;
/*     */     public Builder(FeatureFlagSet enabledFeatures) {
/* 226 */       this.containers = new ArrayList();
/* 227 */       this.potionMixes = new ArrayList();
/* 228 */       this.containerMixes = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 233 */       this.enabledFeatures = enabledFeatures;
/*     */     }
/*     */     
/*     */     private static void expectPotion(Item from) {
/* 237 */       if (!(from instanceof net.minecraft.world.item.PotionItem)) {
/* 238 */         throw new IllegalArgumentException("Expected a potion, got: " + String.valueOf(BuiltInRegistries.ITEM.getKey(from)));
/*     */       }
/*     */     }
/*     */     
/*     */     public void addContainerRecipe(Item from, Item ingredient, Item to) {
/* 243 */       if (!from.isEnabled(this.enabledFeatures) || !ingredient.isEnabled(this.enabledFeatures) || !to.isEnabled(this.enabledFeatures)) {
/*     */         return;
/*     */       }
/*     */       
/* 247 */       expectPotion(from);
/* 248 */       expectPotion(to);
/* 249 */       this.containerMixes.add(new PotionBrewing.Mix(from.builtInRegistryHolder(), Ingredient.of(ingredient), to.builtInRegistryHolder()));
/*     */     }
/*     */     
/*     */     public void addContainer(Item item) {
/* 253 */       if (!item.isEnabled(this.enabledFeatures)) {
/*     */         return;
/*     */       }
/* 256 */       expectPotion(item);
/* 257 */       this.containers.add(Ingredient.of(item));
/*     */     }
/*     */     
/*     */     public void addMix(Holder<Potion> from, Item ingredient, Holder<Potion> to) {
/* 261 */       if (((Potion)from.value()).isEnabled(this.enabledFeatures) && ingredient.isEnabled(this.enabledFeatures) && ((Potion)to.value()).isEnabled(this.enabledFeatures)) {
/* 262 */         this.potionMixes.add(new PotionBrewing.Mix(from, Ingredient.of(ingredient), to));
/*     */       }
/*     */     }
/*     */     
/*     */     public void addStartMix(Item ingredient, Holder<Potion> potion) {
/* 267 */       if (((Potion)potion.value()).isEnabled(this.enabledFeatures)) {
/* 268 */         addMix(Potions.WATER, ingredient, Potions.MUNDANE);
/* 269 */         addMix(Potions.AWKWARD, ingredient, potion);
/*     */       } 
/*     */     }
/*     */     
/*     */     public PotionBrewing build() {
/* 274 */       return new PotionBrewing(
/* 275 */           List.copyOf(this.containers), 
/* 276 */           List.copyOf(this.potionMixes), 
/* 277 */           List.copyOf(this.containerMixes));
/*     */     } }
/*     */   private static final class Mix<T> extends Record { private final Holder<T> from; private final Ingredient ingredient;
/*     */     private final Holder<T> to;
/*     */     
/* 282 */     private Mix(Holder<T> from, Ingredient ingredient, Holder<T> to) { this.from = from; this.ingredient = ingredient; this.to = to; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #282	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 282 */       //   0	7	0	this	Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix<TT;>; } public Holder<T> from() { return this.from; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #282	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #282	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 282 */       //   0	8	0	this	Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix<TT;>; } public Ingredient ingredient() { return this.ingredient; } public Holder<T> to() { return this.to; } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\alchemy\PotionBrewing.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
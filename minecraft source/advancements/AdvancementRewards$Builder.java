/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   private int experience;
/*  78 */   private final ImmutableList.Builder<ResourceKey<LootTable>> loot = ImmutableList.builder();
/*  79 */   private final ImmutableList.Builder<ResourceKey<Recipe<?>>> recipes = ImmutableList.builder();
/*  80 */   private Optional<Identifier> function = Optional.empty();
/*     */ 
/*     */   
/*  83 */   public static Builder experience(int amount) { return (new Builder()).addExperience(amount); }
/*     */ 
/*     */   
/*     */   public Builder addExperience(int amount) {
/*  87 */     this.experience += amount;
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   
/*  92 */   public static Builder loot(ResourceKey<LootTable> id) { return (new Builder()).addLootTable(id); }
/*     */ 
/*     */   
/*     */   public Builder addLootTable(ResourceKey<LootTable> id) {
/*  96 */     this.loot.add(id);
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 101 */   public static Builder recipe(ResourceKey<Recipe<?>> id) { return (new Builder()).addRecipe(id); }
/*     */ 
/*     */   
/*     */   public Builder addRecipe(ResourceKey<Recipe<?>> id) {
/* 105 */     this.recipes.add(id);
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 110 */   public static Builder function(Identifier id) { return (new Builder()).runs(id); }
/*     */ 
/*     */   
/*     */   public Builder runs(Identifier function) {
/* 114 */     this.function = Optional.of(function);
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 119 */   public AdvancementRewards build() { return new AdvancementRewards(this.experience, this.loot.build(), this.recipes.build(), this.function.map(net.minecraft.commands.CacheableFunction::new)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\AdvancementRewards$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 190 */   private MinMaxBounds.Ints level = MinMaxBounds.Ints.ANY;
/* 191 */   private GameTypePredicate gameType = GameTypePredicate.ANY;
/* 192 */   private final ImmutableList.Builder<PlayerPredicate.StatMatcher<?>> stats = ImmutableList.builder();
/* 193 */   private final Object2BooleanMap<ResourceKey<Recipe<?>>> recipes = new Object2BooleanOpenHashMap();
/* 194 */   private final Map<Identifier, PlayerPredicate.AdvancementPredicate> advancements = Maps.newHashMap();
/* 195 */   private Optional<EntityPredicate> lookingAt = Optional.empty();
/* 196 */   private Optional<InputPredicate> input = Optional.empty();
/*     */ 
/*     */   
/* 199 */   public static Builder player() { return new Builder(); }
/*     */ 
/*     */   
/*     */   public Builder setLevel(MinMaxBounds.Ints level) {
/* 203 */     this.level = level;
/* 204 */     return this;
/*     */   }
/*     */   
/*     */   public <T> Builder addStat(StatType<T> type, Holder.Reference<T> value, MinMaxBounds.Ints range) {
/* 208 */     this.stats.add(new PlayerPredicate.StatMatcher(type, value, range));
/* 209 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addRecipe(ResourceKey<Recipe<?>> recipe, boolean present) {
/* 213 */     this.recipes.put(recipe, present);
/* 214 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setGameType(GameTypePredicate gameType) {
/* 218 */     this.gameType = gameType;
/* 219 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setLookingAt(EntityPredicate.Builder lookingAt) {
/* 223 */     this.lookingAt = Optional.of(lookingAt.build());
/* 224 */     return this;
/*     */   }
/*     */   
/*     */   public Builder checkAdvancementDone(Identifier advancement, boolean isDone) {
/* 228 */     this.advancements.put(advancement, new PlayerPredicate.AdvancementDonePredicate(isDone));
/* 229 */     return this;
/*     */   }
/*     */   
/*     */   public Builder checkAdvancementCriterions(Identifier advancement, Map<String, Boolean> criterions) {
/* 233 */     this.advancements.put(advancement, new PlayerPredicate.AdvancementCriterionsPredicate(new Object2BooleanOpenHashMap(criterions)));
/* 234 */     return this;
/*     */   }
/*     */   
/*     */   public Builder hasInput(InputPredicate input) {
/* 238 */     this.input = Optional.of(input);
/* 239 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 243 */   public PlayerPredicate build() { return new PlayerPredicate(this.level, this.gameType, this.stats.build(), this.recipes, this.advancements, this.lookingAt, this.input); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\PlayerPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
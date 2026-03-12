/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ public class RecipeCraftedTrigger
/*    */   extends SimpleCriterionTrigger<RecipeCraftedTrigger.TriggerInstance> {
/* 20 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public void trigger(ServerPlayer player, ResourceKey<Recipe<?>> id, List<ItemStack> usedIngredients) { trigger(player, t -> t.matches(id, usedIngredients)); }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final ResourceKey<Recipe<?>> recipeId; private final List<ItemPredicate> ingredients;
/*    */     
/* 27 */     public TriggerInstance(Optional<ContextAwarePredicate> player, ResourceKey<Recipe<?>> recipeId, List<ItemPredicate> ingredients) { this.player = player; this.recipeId = recipeId; this.ingredients = ingredients; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/RecipeCraftedTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 27 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/RecipeCraftedTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/RecipeCraftedTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/RecipeCraftedTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/RecipeCraftedTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/RecipeCraftedTrigger$TriggerInstance;
/* 27 */       //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<Recipe<?>> recipeId() { return this.recipeId; } public List<ItemPredicate> ingredients() { return this.ingredients; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 32 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 33 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), Recipe.KEY_CODEC
/* 34 */           .fieldOf("recipe_id").forGetter(TriggerInstance::recipeId), ItemPredicate.CODEC
/* 35 */           .listOf().optionalFieldOf("ingredients", List.of()).forGetter(TriggerInstance::ingredients))
/* 36 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 39 */     public static Criterion<TriggerInstance> craftedItem(ResourceKey<Recipe<?>> recipeId, List<ItemPredicate.Builder> predicates) { return CriteriaTriggers.RECIPE_CRAFTED.createCriterion(new TriggerInstance(Optional.empty(), recipeId, predicates.stream().map(ItemPredicate.Builder::build).toList())); }
/*    */ 
/*    */ 
/*    */     
/* 43 */     public static Criterion<TriggerInstance> craftedItem(ResourceKey<Recipe<?>> recipeId) { return CriteriaTriggers.RECIPE_CRAFTED.createCriterion(new TriggerInstance(Optional.empty(), recipeId, List.of())); }
/*    */ 
/*    */ 
/*    */     
/* 47 */     public static Criterion<TriggerInstance> crafterCraftedItem(ResourceKey<Recipe<?>> recipeId) { return CriteriaTriggers.CRAFTER_RECIPE_CRAFTED.createCriterion(new TriggerInstance(Optional.empty(), recipeId, List.of())); }
/*    */ 
/*    */     
/*    */     private boolean matches(ResourceKey<Recipe<?>> id, List<ItemStack> usedIngredients) {
/* 51 */       if (id != this.recipeId) {
/* 52 */         return false;
/*    */       }
/*    */       
/* 55 */       List<ItemStack> remaining = new ArrayList<ItemStack>(usedIngredients);
/* 56 */       for (ItemPredicate predicate : this.ingredients) {
/* 57 */         boolean found = false;
/* 58 */         for (Iterator<ItemStack> iterator = remaining.iterator(); iterator.hasNext();) {
/* 59 */           if (predicate.test((ItemStack)iterator.next())) {
/* 60 */             iterator.remove();
/* 61 */             found = true;
/*    */             break;
/*    */           } 
/*    */         } 
/* 65 */         if (!found) {
/* 66 */           return false;
/*    */         }
/*    */       } 
/* 69 */       return true;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\RecipeCraftedTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
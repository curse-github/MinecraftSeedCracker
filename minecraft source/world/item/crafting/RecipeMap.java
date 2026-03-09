/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableMultimap;
/*    */ import com.google.common.collect.Multimap;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ public class RecipeMap
/*    */ {
/* 15 */   public static final RecipeMap EMPTY = new RecipeMap(ImmutableMultimap.of(), Map.of());
/*    */   
/*    */   private final Multimap<RecipeType<?>, RecipeHolder<?>> byType;
/*    */   
/*    */   private final Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey;
/*    */   
/*    */   private RecipeMap(Multimap<RecipeType<?>, RecipeHolder<?>> byType, Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey) {
/* 22 */     this.byType = byType;
/* 23 */     this.byKey = byKey;
/*    */   }
/*    */   
/*    */   public static RecipeMap create(Iterable<RecipeHolder<?>> recipes) {
/* 27 */     ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> byType = ImmutableMultimap.builder();
/* 28 */     ImmutableMap.Builder<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey = ImmutableMap.builder();
/*    */     
/* 30 */     for (RecipeHolder<?> recipe : recipes) {
/* 31 */       byType.put(recipe.value().getType(), recipe);
/* 32 */       byKey.put(recipe.id(), recipe);
/*    */     } 
/*    */     
/* 35 */     return new RecipeMap(byType.build(), byKey.build());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public <I extends RecipeInput, T extends Recipe<I>> Collection<RecipeHolder<T>> byType(RecipeType<T> type) { return this.byType.get(type); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public Collection<RecipeHolder<?>> values() { return this.byKey.values(); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public RecipeHolder<?> byKey(ResourceKey<Recipe<?>> recipeId) { return (RecipeHolder)this.byKey.get(recipeId); }
/*    */ 
/*    */   
/*    */   public <I extends RecipeInput, T extends Recipe<I>> Stream<RecipeHolder<T>> getRecipesFor(RecipeType<T> type, I container, Level level) {
/* 52 */     if (container.isEmpty()) {
/* 53 */       return Stream.empty();
/*    */     }
/* 55 */     return byType(type).stream().filter(r -> r.value().matches(container, level));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\RecipeMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
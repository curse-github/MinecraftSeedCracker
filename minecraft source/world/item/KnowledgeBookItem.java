/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ import net.minecraft.world.item.crafting.RecipeManager;
/*    */ import net.minecraft.world.level.Level;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class KnowledgeBookItem
/*    */   extends Item {
/* 21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */   
/* 24 */   public KnowledgeBookItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 29 */     ItemStack itemStack = player.getItemInHand(hand);
/* 30 */     List<ResourceKey<Recipe<?>>> recipeIds = (List)itemStack.getOrDefault(DataComponents.RECIPES, List.of());
/*    */     
/* 32 */     itemStack.consume(1, player);
/*    */     
/* 34 */     if (recipeIds.isEmpty()) {
/* 35 */       return InteractionResult.FAIL;
/*    */     }
/*    */     
/* 38 */     if (!level.isClientSide()) {
/* 39 */       RecipeManager recipeManager = level.getServer().getRecipeManager();
/* 40 */       List<RecipeHolder<?>> recipes = new ArrayList<RecipeHolder<?>>(recipeIds.size());
/*    */       
/* 42 */       for (ResourceKey<Recipe<?>> recipeId : recipeIds) {
/* 43 */         Optional<RecipeHolder<?>> recipe = recipeManager.byKey(recipeId);
/* 44 */         if (recipe.isPresent()) {
/* 45 */           recipes.add((RecipeHolder)recipe.get()); continue;
/*    */         } 
/* 47 */         LOGGER.error("Invalid recipe: {}", recipeId);
/* 48 */         return InteractionResult.FAIL;
/*    */       } 
/*    */ 
/*    */       
/* 52 */       player.awardRecipes(recipes);
/* 53 */       player.awardStat(Stats.ITEM_USED.get(this));
/*    */     } 
/*    */     
/* 56 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\KnowledgeBookItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
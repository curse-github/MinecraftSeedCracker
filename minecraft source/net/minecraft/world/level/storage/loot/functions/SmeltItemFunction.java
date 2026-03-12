/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ import net.minecraft.world.item.crafting.SingleRecipeInput;
/*    */ import net.minecraft.world.item.crafting.SmeltingRecipe;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class SmeltItemFunction extends LootItemConditionalFunction {
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 21 */   public static final MapCodec<SmeltItemFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).apply(i, SmeltItemFunction::new));
/*    */ 
/*    */   
/* 24 */   private SmeltItemFunction(List<LootItemCondition> predicates) { super(predicates); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public LootItemFunctionType<SmeltItemFunction> getType() { return LootItemFunctions.FURNACE_SMELT; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 34 */     if (itemStack.isEmpty()) {
/* 35 */       return itemStack;
/*    */     }
/*    */     
/* 38 */     SingleRecipeInput input = new SingleRecipeInput(itemStack);
/* 39 */     Optional<RecipeHolder<SmeltingRecipe>> recipe = context.getLevel().recipeAccess().getRecipeFor(RecipeType.SMELTING, input, context.getLevel());
/* 40 */     if (recipe.isPresent()) {
/* 41 */       ItemStack result = ((SmeltingRecipe)((RecipeHolder)recipe.get()).value()).assemble(input, context.getLevel().registryAccess());
/*    */       
/* 43 */       if (!result.isEmpty()) {
/* 44 */         return result.copyWithCount(itemStack.getCount());
/*    */       }
/*    */     } 
/*    */     
/* 48 */     LOGGER.warn("Couldn't smelt {} because there is no smelting recipe", itemStack);
/* 49 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/* 53 */   public static LootItemConditionalFunction.Builder<?> smelted() { return simpleBuilder(SmeltItemFunction::new); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SmeltItemFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
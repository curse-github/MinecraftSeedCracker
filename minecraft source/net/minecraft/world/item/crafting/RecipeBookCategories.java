/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class RecipeBookCategories {
/*  7 */   public static final RecipeBookCategory CRAFTING_BUILDING_BLOCKS = register("crafting_building_blocks");
/*  8 */   public static final RecipeBookCategory CRAFTING_REDSTONE = register("crafting_redstone");
/*  9 */   public static final RecipeBookCategory CRAFTING_EQUIPMENT = register("crafting_equipment");
/* 10 */   public static final RecipeBookCategory CRAFTING_MISC = register("crafting_misc");
/*    */   
/* 12 */   public static final RecipeBookCategory FURNACE_FOOD = register("furnace_food");
/* 13 */   public static final RecipeBookCategory FURNACE_BLOCKS = register("furnace_blocks");
/* 14 */   public static final RecipeBookCategory FURNACE_MISC = register("furnace_misc");
/*    */   
/* 16 */   public static final RecipeBookCategory BLAST_FURNACE_BLOCKS = register("blast_furnace_blocks");
/* 17 */   public static final RecipeBookCategory BLAST_FURNACE_MISC = register("blast_furnace_misc");
/*    */   
/* 19 */   public static final RecipeBookCategory SMOKER_FOOD = register("smoker_food");
/*    */   
/* 21 */   public static final RecipeBookCategory STONECUTTER = register("stonecutter");
/*    */   
/* 23 */   public static final RecipeBookCategory SMITHING = register("smithing");
/*    */   
/* 25 */   public static final RecipeBookCategory CAMPFIRE = register("campfire");
/*    */ 
/*    */   
/* 28 */   private static RecipeBookCategory register(String id) { return (RecipeBookCategory)Registry.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY, id, new RecipeBookCategory()); }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public static RecipeBookCategory bootstrap(Registry<RecipeBookCategory> registry) { return CAMPFIRE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\RecipeBookCategories.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
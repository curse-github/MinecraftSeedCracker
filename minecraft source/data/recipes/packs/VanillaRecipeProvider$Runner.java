/*    */ package net.minecraft.data.recipes.packs;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.data.recipes.RecipeOutput;
/*    */ import net.minecraft.data.recipes.RecipeProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Runner
/*    */   extends RecipeProvider.Runner
/*    */ {
/* 75 */   public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) { super(packOutput, registries); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 80 */   protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) { return new VanillaRecipeProvider(registries, output); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 85 */   public String getName() { return "Vanilla Recipes"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\packs\VanillaRecipeProvider$Runner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
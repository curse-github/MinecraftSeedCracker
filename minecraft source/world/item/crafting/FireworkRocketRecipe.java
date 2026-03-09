/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.component.FireworkExplosion;
/*    */ import net.minecraft.world.item.component.Fireworks;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class FireworkRocketRecipe
/*    */   extends CustomRecipe {
/* 15 */   private static final Ingredient PAPER_INGREDIENT = Ingredient.of(Items.PAPER);
/* 16 */   private static final Ingredient GUNPOWDER_INGREDIENT = Ingredient.of(Items.GUNPOWDER);
/* 17 */   private static final Ingredient STAR_INGREDIENT = Ingredient.of(Items.FIREWORK_STAR);
/*    */ 
/*    */   
/* 20 */   public FireworkRocketRecipe(CraftingBookCategory category) { super(category); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingInput input, Level level) {
/* 26 */     if (input.ingredientCount() < 2) {
/* 27 */       return false;
/*    */     }
/*    */     
/* 30 */     boolean paper = false;
/* 31 */     int gunPowder = 0;
/*    */     
/* 33 */     for (int slot = 0; slot < input.size(); slot++) {
/* 34 */       ItemStack itemStack = input.getItem(slot);
/* 35 */       if (!itemStack.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 39 */         if (PAPER_INGREDIENT.test(itemStack)) {
/* 40 */           if (paper) {
/* 41 */             return false;
/*    */           }
/* 43 */           paper = true;
/* 44 */         } else if (GUNPOWDER_INGREDIENT.test(itemStack)) {
/* 45 */           gunPowder++;
/* 46 */           if (gunPowder > 3) {
/* 47 */             return false;
/*    */           }
/* 49 */         } else if (!STAR_INGREDIENT.test(itemStack)) {
/* 50 */           return false;
/*    */         } 
/*    */       }
/*    */     } 
/* 54 */     return (paper && gunPowder >= 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/* 59 */     List<FireworkExplosion> explosions = new ArrayList<FireworkExplosion>();
/*    */     
/* 61 */     int gunPowder = 0;
/*    */     
/* 63 */     for (int slot = 0; slot < input.size(); slot++) {
/* 64 */       ItemStack itemStack = input.getItem(slot);
/* 65 */       if (!itemStack.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 69 */         if (GUNPOWDER_INGREDIENT.test(itemStack)) {
/* 70 */           gunPowder++;
/* 71 */         } else if (STAR_INGREDIENT.test(itemStack)) {
/* 72 */           FireworkExplosion explosion = (FireworkExplosion)itemStack.get(DataComponents.FIREWORK_EXPLOSION);
/* 73 */           if (explosion != null) {
/* 74 */             explosions.add(explosion);
/*    */           }
/*    */         } 
/*    */       }
/*    */     } 
/* 79 */     ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET, 3);
/* 80 */     rocket.set(DataComponents.FIREWORKS, new Fireworks(gunPowder, explosions));
/* 81 */     return rocket;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 86 */   public RecipeSerializer<FireworkRocketRecipe> getSerializer() { return RecipeSerializer.FIREWORK_ROCKET; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\FireworkRocketRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
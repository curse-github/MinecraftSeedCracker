/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.CraftingInput;
/*     */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class ResultSlot
/*     */   extends Slot {
/*     */   private final CraftingContainer craftSlots;
/*     */   
/*     */   public ResultSlot(Player player, CraftingContainer craftSlots, Container container, int id, int x, int y) {
/*  19 */     super(container, id, x, y);
/*  20 */     this.player = player;
/*  21 */     this.craftSlots = craftSlots;
/*     */   }
/*     */   private final Player player;
/*     */   private int removeCount;
/*     */   
/*  26 */   public boolean mayPlace(ItemStack itemStack) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack remove(int amount) {
/*  31 */     if (hasItem()) {
/*  32 */       this.removeCount += Math.min(amount, getItem().getCount());
/*     */     }
/*  34 */     return super.remove(amount);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onQuickCraft(ItemStack picked, int count) {
/*  39 */     this.removeCount += count;
/*  40 */     checkTakeAchievements(picked);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  45 */   protected void onSwapCraft(int count) { this.removeCount += count; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkTakeAchievements(ItemStack carried) {
/*  50 */     if (this.removeCount > 0) {
/*  51 */       carried.onCraftedBy(this.player, this.removeCount);
/*     */     }
/*  53 */     Container container = this.container; if (container instanceof RecipeCraftingHolder) { RecipeCraftingHolder recipeCraftingHolder = (RecipeCraftingHolder)container;
/*  54 */       recipeCraftingHolder.awardUsedRecipes(this.player, this.craftSlots.getItems()); }
/*     */     
/*  56 */     this.removeCount = 0;
/*     */   }
/*     */   
/*     */   private static NonNullList<ItemStack> copyAllInputItems(CraftingInput input) {
/*  60 */     NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);
/*  61 */     for (int slot = 0; slot < result.size(); slot++) {
/*  62 */       result.set(slot, input.getItem(slot));
/*     */     }
/*  64 */     return result;
/*     */   }
/*     */   
/*     */   private NonNullList<ItemStack> getRemainingItems(CraftingInput input, Level level) {
/*  68 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*     */       
/*  70 */       return (NonNullList)serverLevel.recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, serverLevel)
/*  71 */         .map(recipe -> ((CraftingRecipe)recipe.value()).getRemainingItems(input))
/*  72 */         .orElseGet(() -> copyAllInputItems(input)); }
/*     */ 
/*     */ 
/*     */     
/*  76 */     return CraftingRecipe.defaultCraftingReminder(input);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTake(Player player, ItemStack carried) {
/*  82 */     checkTakeAchievements(carried);
/*     */     
/*  84 */     CraftingInput.Positioned positionedRecipe = this.craftSlots.asPositionedCraftInput();
/*  85 */     CraftingInput input = positionedRecipe.input();
/*  86 */     int recipeLeft = positionedRecipe.left();
/*  87 */     int recipeTop = positionedRecipe.top();
/*     */     
/*  89 */     NonNullList<ItemStack> remaining = getRemainingItems(input, player.level());
/*     */     
/*  91 */     for (int y = 0; y < input.height(); y++) {
/*  92 */       for (int x = 0; x < input.width(); x++) {
/*  93 */         int slot = x + recipeLeft + (y + recipeTop) * this.craftSlots.getWidth();
/*  94 */         ItemStack itemStack = this.craftSlots.getItem(slot);
/*     */         
/*  96 */         ItemStack replacement = (ItemStack)remaining.get(x + y * input.width());
/*     */         
/*  98 */         if (!itemStack.isEmpty()) {
/*  99 */           this.craftSlots.removeItem(slot, 1);
/* 100 */           itemStack = this.craftSlots.getItem(slot);
/*     */         } 
/*     */         
/* 103 */         if (!replacement.isEmpty()) {
/* 104 */           if (itemStack.isEmpty()) {
/*     */             
/* 106 */             this.craftSlots.setItem(slot, replacement);
/* 107 */           } else if (ItemStack.isSameItemSameComponents(itemStack, replacement)) {
/* 108 */             replacement.grow(itemStack.getCount());
/* 109 */             this.craftSlots.setItem(slot, replacement);
/* 110 */           } else if (!this.player.getInventory().add(replacement)) {
/*     */             
/* 112 */             this.player.drop(replacement, false);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 121 */   public boolean isFake() { return true; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\ResultSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
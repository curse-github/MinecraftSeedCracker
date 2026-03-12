/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.lang.ref.WeakReference;
/*    */ import java.util.Arrays;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RecipeCache
/*    */ {
/*    */   private final Entry[] entries;
/*    */   private WeakReference<RecipeManager> cachedRecipeManager;
/*    */   
/*    */   public RecipeCache(int capacity) {
/* 19 */     this.cachedRecipeManager = new WeakReference(null);
/*    */ 
/*    */     
/* 22 */     this.entries = new Entry[capacity];
/*    */   }
/*    */   
/*    */   public Optional<RecipeHolder<CraftingRecipe>> get(ServerLevel level, CraftingInput input) {
/* 26 */     if (input.isEmpty()) {
/* 27 */       return Optional.empty();
/*    */     }
/*    */     
/* 30 */     validateRecipeManager(level);
/*    */     
/* 32 */     for (int i = 0; i < this.entries.length; i++) {
/* 33 */       Entry entry = this.entries[i];
/* 34 */       if (entry != null && entry.matches(input)) {
/* 35 */         moveEntryToFront(i);
/* 36 */         return Optional.ofNullable(entry.value());
/*    */       } 
/*    */     } 
/* 39 */     return compute(input, level);
/*    */   }
/*    */   
/*    */   private void validateRecipeManager(ServerLevel level) {
/* 43 */     RecipeManager recipeManager = level.recipeAccess();
/* 44 */     if (recipeManager != this.cachedRecipeManager.get()) {
/*    */       
/* 46 */       this.cachedRecipeManager = new WeakReference(recipeManager);
/* 47 */       Arrays.fill(this.entries, null);
/*    */     } 
/*    */   }
/*    */   
/*    */   private Optional<RecipeHolder<CraftingRecipe>> compute(CraftingInput input, ServerLevel level) {
/* 52 */     Optional<RecipeHolder<CraftingRecipe>> recipe = level.recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, level);
/* 53 */     insert(input, (RecipeHolder)recipe.orElse(null));
/* 54 */     return recipe;
/*    */   }
/*    */   
/*    */   private void moveEntryToFront(int index) {
/* 58 */     if (index > 0) {
/* 59 */       Entry entry = this.entries[index];
/* 60 */       System.arraycopy(this.entries, 0, this.entries, 1, index);
/* 61 */       this.entries[0] = entry;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void insert(CraftingInput input, RecipeHolder<CraftingRecipe> recipe) {
/* 66 */     NonNullList<ItemStack> key = NonNullList.withSize(input.size(), ItemStack.EMPTY);
/* 67 */     for (int i = 0; i < input.size(); i++) {
/* 68 */       key.set(i, input.getItem(i).copyWithCount(1));
/*    */     }
/* 70 */     System.arraycopy(this.entries, 0, this.entries, 1, this.entries.length - 1);
/* 71 */     this.entries[0] = new Entry(key, input.width(), input.height(), recipe);
/*    */   }
/*    */   private static final class Entry extends Record { private final NonNullList<ItemStack> key; private final int width; private final int height; private final RecipeHolder<CraftingRecipe> value;
/* 74 */     private Entry(NonNullList<ItemStack> key, int width, int height, RecipeHolder<CraftingRecipe> value) { this.key = key; this.width = width; this.height = height; this.value = value; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/RecipeCache$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 74 */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/RecipeCache$Entry; } public NonNullList<ItemStack> key() { return this.key; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/RecipeCache$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/RecipeCache$Entry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/RecipeCache$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/RecipeCache$Entry;
/* 74 */       //   0	8	1	o	Ljava/lang/Object; } public int width() { return this.width; } public int height() { return this.height; } public RecipeHolder<CraftingRecipe> value() { return this.value; }
/*    */     public boolean matches(CraftingInput input) {
/* 76 */       if (this.width != input.width() || this.height != input.height()) {
/* 77 */         return false;
/*    */       }
/* 79 */       for (int i = 0; i < this.key.size(); i++) {
/* 80 */         if (!ItemStack.isSameItemSameComponents((ItemStack)this.key.get(i), input.getItem(i))) {
/* 81 */           return false;
/*    */         }
/*    */       } 
/* 84 */       return true;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\RecipeCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
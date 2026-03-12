/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.WrittenBookContent;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class BookCloningRecipe
/*     */   extends CustomRecipe {
/*  13 */   public BookCloningRecipe(CraftingBookCategory category) { super(category); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  19 */     if (input.ingredientCount() < 2) {
/*  20 */       return false;
/*     */     }
/*     */     
/*  23 */     boolean hasEmptyBooks = false;
/*  24 */     boolean hasSourceBook = false;
/*     */     
/*  26 */     for (int slot = 0; slot < input.size(); slot++) {
/*  27 */       ItemStack itemStack = input.getItem(slot);
/*  28 */       if (!itemStack.isEmpty())
/*     */       {
/*     */ 
/*     */         
/*  32 */         if (itemStack.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
/*  33 */           if (hasSourceBook) {
/*  34 */             return false;
/*     */           }
/*  36 */           hasSourceBook = true;
/*  37 */         } else if (itemStack.is(ItemTags.BOOK_CLONING_TARGET)) {
/*  38 */           hasEmptyBooks = true;
/*     */         } else {
/*  40 */           return false;
/*     */         } 
/*     */       }
/*     */     } 
/*  44 */     return (hasSourceBook && hasEmptyBooks);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/*  49 */     int count = 0;
/*  50 */     ItemStack source = ItemStack.EMPTY;
/*     */     
/*  52 */     for (int slot = 0; slot < input.size(); slot++) {
/*  53 */       ItemStack itemStack = input.getItem(slot);
/*  54 */       if (!itemStack.isEmpty())
/*     */       {
/*     */ 
/*     */         
/*  58 */         if (itemStack.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
/*  59 */           if (!source.isEmpty()) {
/*  60 */             return ItemStack.EMPTY;
/*     */           }
/*  62 */           source = itemStack;
/*  63 */         } else if (itemStack.is(ItemTags.BOOK_CLONING_TARGET)) {
/*  64 */           count++;
/*     */         } else {
/*  66 */           return ItemStack.EMPTY;
/*     */         } 
/*     */       }
/*     */     } 
/*  70 */     WrittenBookContent sourceContent = (WrittenBookContent)source.get(DataComponents.WRITTEN_BOOK_CONTENT);
/*  71 */     if (source.isEmpty() || count < 1 || sourceContent == null) {
/*  72 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/*  75 */     WrittenBookContent copiedContent = sourceContent.tryCraftCopy();
/*  76 */     if (copiedContent == null) {
/*  77 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/*  80 */     ItemStack result = source.copyWithCount(count);
/*  81 */     result.set(DataComponents.WRITTEN_BOOK_CONTENT, copiedContent);
/*     */     
/*  83 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
/*  88 */     NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);
/*     */     
/*  90 */     for (int slot = 0; slot < result.size(); slot++) {
/*  91 */       ItemStack itemStack = input.getItem(slot);
/*  92 */       ItemStack remainder = itemStack.getItem().getCraftingRemainder();
/*  93 */       if (!remainder.isEmpty()) {
/*  94 */         result.set(slot, remainder);
/*  95 */       } else if (itemStack.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
/*  96 */         result.set(slot, itemStack.copyWithCount(1));
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 101 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 106 */   public RecipeSerializer<BookCloningRecipe> getSerializer() { return RecipeSerializer.BOOK_CLONING; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\BookCloningRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
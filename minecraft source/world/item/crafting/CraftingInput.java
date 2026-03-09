/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class CraftingInput
/*     */   implements RecipeInput {
/*  10 */   public static final CraftingInput EMPTY = new CraftingInput(0, 0, List.of()); private final int width; private final int height;
/*     */   private final List<ItemStack> items;
/*     */   private final StackedItemContents stackedContents;
/*     */   private final int ingredientCount;
/*     */   
/*     */   private CraftingInput(int width, int height, List<ItemStack> items) {
/*  16 */     this.stackedContents = new StackedItemContents();
/*     */ 
/*     */ 
/*     */     
/*  20 */     this.width = width;
/*  21 */     this.height = height;
/*  22 */     this.items = items;
/*     */     
/*  24 */     int ingredientCount = 0;
/*  25 */     for (ItemStack item : items) {
/*  26 */       if (!item.isEmpty()) {
/*  27 */         ingredientCount++;
/*  28 */         this.stackedContents.accountStack(item, 1);
/*     */       } 
/*     */     } 
/*  31 */     this.ingredientCount = ingredientCount;
/*     */   }
/*     */ 
/*     */   
/*  35 */   public static CraftingInput of(int width, int height, List<ItemStack> items) { return ofPositioned(width, height, items).input(); }
/*     */ 
/*     */   
/*     */   public static Positioned ofPositioned(int width, int height, List<ItemStack> items) {
/*  39 */     if (width == 0 || height == 0) {
/*  40 */       return Positioned.EMPTY;
/*     */     }
/*     */     
/*  43 */     int left = width - 1;
/*  44 */     int right = 0;
/*  45 */     int top = height - 1;
/*  46 */     int bottom = 0;
/*     */     
/*  48 */     for (int y = 0; y < height; y++) {
/*  49 */       boolean rowEmpty = true;
/*  50 */       for (int x = 0; x < width; x++) {
/*  51 */         ItemStack item = (ItemStack)items.get(x + y * width);
/*  52 */         if (!item.isEmpty()) {
/*  53 */           left = Math.min(left, x);
/*  54 */           right = Math.max(right, x);
/*  55 */           rowEmpty = false;
/*     */         } 
/*     */       } 
/*     */       
/*  59 */       if (!rowEmpty) {
/*  60 */         top = Math.min(top, y);
/*  61 */         bottom = Math.max(bottom, y);
/*     */       } 
/*     */     } 
/*     */     
/*  65 */     int newWidth = right - left + 1;
/*  66 */     int newHeight = bottom - top + 1;
/*  67 */     if (newWidth <= 0 || newHeight <= 0)
/*  68 */       return Positioned.EMPTY; 
/*  69 */     if (newWidth == width && newHeight == height) {
/*  70 */       return new Positioned(new CraftingInput(width, height, items), left, top);
/*     */     }
/*     */     
/*  73 */     List<ItemStack> newItems = new ArrayList<ItemStack>(newWidth * newHeight);
/*  74 */     for (int y = 0; y < newHeight; y++) {
/*  75 */       for (int x = 0; x < newWidth; x++) {
/*  76 */         int index = x + left + (y + top) * width;
/*  77 */         newItems.add((ItemStack)items.get(index));
/*     */       } 
/*     */     } 
/*     */     
/*  81 */     return new Positioned(new CraftingInput(newWidth, newHeight, newItems), left, top);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  86 */   public ItemStack getItem(int index) { return (ItemStack)this.items.get(index); }
/*     */ 
/*     */ 
/*     */   
/*  90 */   public ItemStack getItem(int x, int y) { return (ItemStack)this.items.get(x + y * this.width); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public int size() { return this.items.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public boolean isEmpty() { return (this.ingredientCount == 0); }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public StackedItemContents stackedContents() { return this.stackedContents; }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public List<ItemStack> items() { return this.items; }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public int ingredientCount() { return this.ingredientCount; }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public int width() { return this.width; }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public int height() { return this.height; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 125 */     if (obj == this) {
/* 126 */       return true;
/*     */     }
/* 128 */     if (obj instanceof CraftingInput) { CraftingInput input = (CraftingInput)obj;
/* 129 */       return (this.width == input.width && this.height == input.height && this.ingredientCount == input.ingredientCount && ItemStack.listMatches(this.items, input.items)); }
/*     */     
/* 131 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 136 */     result = ItemStack.hashStackList(this.items);
/* 137 */     result = 31 * result + this.width;
/* 138 */     return 31 * result + this.height;
/*     */   }
/*     */   public static final class Positioned extends Record { private final CraftingInput input; private final int left; private final int top;
/*     */     
/* 142 */     public Positioned(CraftingInput input, int left, int top) { this.input = input; this.left = left; this.top = top; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/CraftingInput$Positioned;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 142 */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/CraftingInput$Positioned; } public CraftingInput input() { return this.input; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/CraftingInput$Positioned;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/CraftingInput$Positioned; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/CraftingInput$Positioned;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/CraftingInput$Positioned;
/* 142 */       //   0	8	1	o	Ljava/lang/Object; } public int left() { return this.left; } public int top() { return this.top; }
/* 143 */     public static final Positioned EMPTY = new Positioned(CraftingInput.EMPTY, 0, 0); }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\CraftingInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
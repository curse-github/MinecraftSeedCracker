/*    */ package net.minecraft.world.item.crafting;public final class SmithingRecipeInput extends Record implements RecipeInput {
/*    */   private final ItemStack template;
/*    */   private final ItemStack base;
/*    */   private final ItemStack addition;
/*    */   
/*  6 */   public SmithingRecipeInput(ItemStack template, ItemStack base, ItemStack addition) { this.template = template; this.base = base; this.addition = addition; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/SmithingRecipeInput;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  6 */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SmithingRecipeInput; } public ItemStack template() { return this.template; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/SmithingRecipeInput;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SmithingRecipeInput; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/SmithingRecipeInput;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/SmithingRecipeInput;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public ItemStack base() { return this.base; } public ItemStack addition() { return this.addition; }
/*    */   
/*    */   public ItemStack getItem(int index) {
/*  9 */     switch (index) { case 0: 
/*    */       case 1:
/*    */       
/*    */       case 2:
/* 13 */        }  throw new IllegalArgumentException("Recipe does not contain slot " + index);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public int size() { return 3; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public boolean isEmpty() { return (this.template.isEmpty() && this.base.isEmpty() && this.addition.isEmpty()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\SmithingRecipeInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.item.crafting;
/*    */ public final class SingleRecipeInput extends Record implements RecipeInput {
/*    */   private final ItemStack item;
/*    */   
/*  5 */   public SingleRecipeInput(ItemStack item) { this.item = item; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/SingleRecipeInput;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  5 */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SingleRecipeInput; } public ItemStack item() { return this.item; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/SingleRecipeInput;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SingleRecipeInput; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/SingleRecipeInput;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/SingleRecipeInput;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   public ItemStack getItem(int index) {
/*  8 */     if (index != 0) {
/*  9 */       throw new IllegalArgumentException("No item for index " + index);
/*    */     }
/* 11 */     return this.item;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 16 */   public int size() { return 1; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\SingleRecipeInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
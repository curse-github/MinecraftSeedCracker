/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
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
/*    */ public final class SingleInputSet<T extends Recipe<?>>
/*    */   extends Record
/*    */ {
/*    */   private final List<SelectableRecipe.SingleInputEntry<T>> entries;
/*    */   
/* 33 */   public SingleInputSet(List<SelectableRecipe.SingleInputEntry<T>> entries) { this.entries = entries; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 33 */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet<TT;>; } public List<SelectableRecipe.SingleInputEntry<T>> entries() { return this.entries; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet<TT;>; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet<TT;>; }
/* 35 */   public static <T extends Recipe<?>> SingleInputSet<T> empty() { return new SingleInputSet(List.of()); }
/*    */ 
/*    */   
/*    */   public static <T extends Recipe<?>> StreamCodec<RegistryFriendlyByteBuf, SingleInputSet<T>> noRecipeCodec() {
/* 39 */     return StreamCodec.composite(
/* 40 */         SelectableRecipe.SingleInputEntry.noRecipeCodec().apply(ByteBufCodecs.list()), SingleInputSet::entries, SingleInputSet::new);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public boolean acceptsInput(ItemStack input) { return this.entries.stream().anyMatch(e -> e.input.test(input)); }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public SingleInputSet<T> selectByInput(ItemStack input) { return new SingleInputSet(this.entries.stream().filter(e -> e.input.test(input)).toList()); }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public boolean isEmpty() { return this.entries.isEmpty(); }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public int size() { return this.entries.size(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\SelectableRecipe$SingleInputSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
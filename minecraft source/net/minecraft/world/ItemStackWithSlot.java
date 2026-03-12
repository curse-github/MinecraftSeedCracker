/*    */ package net.minecraft.world;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class ItemStackWithSlot extends Record {
/*    */   private final int slot;
/*    */   private final ItemStack stack;
/*    */   
/*  8 */   public ItemStackWithSlot(int slot, ItemStack stack) { this.slot = slot; this.stack = stack; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/ItemStackWithSlot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/world/ItemStackWithSlot; } public int slot() { return this.slot; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/ItemStackWithSlot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/ItemStackWithSlot; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/ItemStackWithSlot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/ItemStackWithSlot;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public ItemStack stack() { return this.stack; }
/*    */ 
/*    */ 
/*    */   
/* 12 */   public static final Codec<ItemStackWithSlot> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.UNSIGNED_BYTE
/*    */         
/* 14 */         .fieldOf("Slot").orElse(Integer.valueOf(0)).forGetter(ItemStackWithSlot::slot), ItemStack.MAP_CODEC
/* 15 */         .forGetter(ItemStackWithSlot::stack))
/* 16 */       .apply(i, ItemStackWithSlot::new));
/*    */ 
/*    */   
/* 19 */   public boolean isValidInContainer(int containerSize) { return (this.slot >= 0 && this.slot < containerSize); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ItemStackWithSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world;
/*    */ 
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
/*    */ public final class Success
/*    */   extends Record
/*    */   implements InteractionResult
/*    */ {
/*    */   private final InteractionResult.SwingSource swingSource;
/*    */   private final InteractionResult.ItemContext itemContext;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/InteractionResult$Success;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Success; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/InteractionResult$Success;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Success; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/InteractionResult$Success;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/InteractionResult$Success;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 51 */   public Success(InteractionResult.SwingSource swingSource, InteractionResult.ItemContext itemContext) { this.swingSource = swingSource; this.itemContext = itemContext; } public InteractionResult.SwingSource swingSource() { return this.swingSource; } public InteractionResult.ItemContext itemContext() { return this.itemContext; }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public boolean consumesAction() { return true; }
/*    */ 
/*    */ 
/*    */   
/* 59 */   public Success heldItemTransformedTo(ItemStack itemStack) { return new Success(this.swingSource, new InteractionResult.ItemContext(true, itemStack)); }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public Success withoutItem() { return new Success(this.swingSource, InteractionResult.ItemContext.NONE); }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public boolean wasItemInteraction() { return this.itemContext.wasItemInteraction; }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public ItemStack heldItemTransformedTo() { return this.itemContext.heldItemTransformedTo; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\InteractionResult$Success.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
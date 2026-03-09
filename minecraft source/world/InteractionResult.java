/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public interface InteractionResult
/*    */ {
/*    */   public enum SwingSource {
/*  8 */     NONE,
/*  9 */     CLIENT,
/* 10 */     SERVER;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final Success SUCCESS = new Success(SwingSource.CLIENT, ItemContext.DEFAULT);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static final Success SUCCESS_SERVER = new Success(SwingSource.SERVER, ItemContext.DEFAULT);
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static final Success CONSUME = new Success(SwingSource.NONE, ItemContext.DEFAULT);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public static final Fail FAIL = new Fail();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static final Pass PASS = new Pass();
/*    */ 
/*    */ 
/*    */   
/* 45 */   public static final TryEmptyHandInteraction TRY_WITH_EMPTY_HAND = new TryEmptyHandInteraction();
/*    */ 
/*    */   
/* 48 */   default boolean consumesAction() { return false; }
/*    */   public static final class Success extends Record implements InteractionResult { private final InteractionResult.SwingSource swingSource; private final InteractionResult.ItemContext itemContext;
/*    */     
/* 51 */     public Success(InteractionResult.SwingSource swingSource, InteractionResult.ItemContext itemContext) { this.swingSource = swingSource; this.itemContext = itemContext; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/InteractionResult$Success;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #51	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 51 */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Success; } public InteractionResult.SwingSource swingSource() { return this.swingSource; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/InteractionResult$Success;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #51	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Success; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/InteractionResult$Success;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #51	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/InteractionResult$Success;
/* 51 */       //   0	8	1	o	Ljava/lang/Object; } public InteractionResult.ItemContext itemContext() { return this.itemContext; }
/*    */ 
/*    */ 
/*    */     
/* 55 */     public boolean consumesAction() { return true; }
/*    */ 
/*    */ 
/*    */     
/* 59 */     public Success heldItemTransformedTo(ItemStack itemStack) { return new Success(this.swingSource, new InteractionResult.ItemContext(true, itemStack)); }
/*    */ 
/*    */ 
/*    */     
/* 63 */     public Success withoutItem() { return new Success(this.swingSource, InteractionResult.ItemContext.NONE); }
/*    */ 
/*    */ 
/*    */     
/* 67 */     public boolean wasItemInteraction() { return this.itemContext.wasItemInteraction; }
/*    */ 
/*    */ 
/*    */     
/* 71 */     public ItemStack heldItemTransformedTo() { return this.itemContext.heldItemTransformedTo; } }
/*    */   public static final class ItemContext extends Record { private final boolean wasItemInteraction;
/*    */     private final ItemStack heldItemTransformedTo;
/*    */     
/* 75 */     public ItemContext(boolean wasItemInteraction, ItemStack heldItemTransformedTo) { this.wasItemInteraction = wasItemInteraction; this.heldItemTransformedTo = heldItemTransformedTo; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/InteractionResult$ItemContext;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #75	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$ItemContext; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/InteractionResult$ItemContext;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #75	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$ItemContext; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/InteractionResult$ItemContext;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #75	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/InteractionResult$ItemContext;
/* 75 */       //   0	8	1	o	Ljava/lang/Object; } public boolean wasItemInteraction() { return this.wasItemInteraction; } public ItemStack heldItemTransformedTo() { return this.heldItemTransformedTo; }
/* 76 */     static ItemContext NONE = new ItemContext(false, null);
/* 77 */     static ItemContext DEFAULT = new ItemContext(true, null); }
/*    */ 
/*    */   
/*    */   public static final class Fail extends Record implements InteractionResult {
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/InteractionResult$Fail;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #80	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Fail; }
/*    */     
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/InteractionResult$Fail;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #80	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Fail; }
/*    */     
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/InteractionResult$Fail;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #80	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/InteractionResult$Fail;
/*    */       //   0	8	1	o	Ljava/lang/Object; }
/*    */   }
/*    */   
/*    */   public static final class Pass extends Record implements InteractionResult {
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/InteractionResult$Pass;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #83	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Pass; }
/*    */     
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/InteractionResult$Pass;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #83	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Pass; }
/*    */     
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/InteractionResult$Pass;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #83	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/InteractionResult$Pass;
/*    */       //   0	8	1	o	Ljava/lang/Object; }
/*    */   }
/*    */   
/*    */   public static final class TryEmptyHandInteraction extends Record implements InteractionResult {
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/InteractionResult$TryEmptyHandInteraction;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #86	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$TryEmptyHandInteraction; }
/*    */     
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/InteractionResult$TryEmptyHandInteraction;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #86	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$TryEmptyHandInteraction; }
/*    */     
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/InteractionResult$TryEmptyHandInteraction;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #86	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/InteractionResult$TryEmptyHandInteraction;
/*    */       //   0	8	1	o	Ljava/lang/Object; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\InteractionResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
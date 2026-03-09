/*    */ package net.minecraft.world.item;public final class Default extends Record implements TooltipFlag { private final boolean advanced;
/*    */   private final boolean creative;
/*    */   
/*  4 */   public Default(boolean advanced, boolean creative) { this.advanced = advanced; this.creative = creative; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/TooltipFlag$Default;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #4	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  4 */     //   0	7	0	this	Lnet/minecraft/world/item/TooltipFlag$Default; } public boolean advanced() { return this.advanced; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/TooltipFlag$Default;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #4	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/TooltipFlag$Default; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/TooltipFlag$Default;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #4	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/TooltipFlag$Default;
/*  4 */     //   0	8	1	o	Ljava/lang/Object; } public boolean creative() { return this.creative; }
/*    */ 
/*    */   
/*  7 */   public boolean isAdvanced() { return this.advanced; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 12 */   public boolean isCreative() { return this.creative; }
/*    */ 
/*    */ 
/*    */   
/* 16 */   public Default asCreative() { return new Default(this.advanced, true); } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\TooltipFlag$Default.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
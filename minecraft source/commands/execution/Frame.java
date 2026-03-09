/*    */ package net.minecraft.commands.execution;public final class Frame extends Record { private final int depth;
/*    */   private final CommandResultCallback returnValueConsumer;
/*    */   private final FrameControl frameControl;
/*    */   
/*  5 */   public Frame(int depth, CommandResultCallback returnValueConsumer, FrameControl frameControl) { this.depth = depth; this.returnValueConsumer = returnValueConsumer; this.frameControl = frameControl; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/execution/Frame;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  5 */     //   0	7	0	this	Lnet/minecraft/commands/execution/Frame; } public int depth() { return this.depth; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/execution/Frame;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/execution/Frame; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/execution/Frame;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/execution/Frame;
/*  5 */     //   0	8	1	o	Ljava/lang/Object; } public CommandResultCallback returnValueConsumer() { return this.returnValueConsumer; } public FrameControl frameControl() { return this.frameControl; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 11 */   public void returnSuccess(int value) { this.returnValueConsumer.onSuccess(value); }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public void returnFailure() { this.returnValueConsumer.onFailure(); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public void discard() { this.frameControl.discard(); }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface FrameControl {
/*    */     void discard();
/*    */   } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\Frame.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
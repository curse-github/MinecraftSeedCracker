/*   */ package net.minecraft.commands.execution;public final class CommandQueueEntry<T> extends Record { private final Frame frame; private final EntryAction<T> action;
/*   */   
/* 3 */   public CommandQueueEntry(Frame frame, EntryAction<T> action) { this.frame = frame; this.action = action; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/execution/CommandQueueEntry;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/commands/execution/CommandQueueEntry;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/* 3 */     //   0	7	0	this	Lnet/minecraft/commands/execution/CommandQueueEntry<TT;>; } public Frame frame() { return this.frame; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/execution/CommandQueueEntry;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/commands/execution/CommandQueueEntry;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/*   */     //   0	7	0	this	Lnet/minecraft/commands/execution/CommandQueueEntry<TT;>; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/execution/CommandQueueEntry;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/commands/execution/CommandQueueEntry;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/* 3 */     //   0	8	0	this	Lnet/minecraft/commands/execution/CommandQueueEntry<TT;>; } public EntryAction<T> action() { return this.action; }
/*   */ 
/*   */ 
/*   */ 
/*   */   
/* 8 */   public void execute(ExecutionContext<T> context) { this.action.execute(context, this.frame); } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\CommandQueueEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
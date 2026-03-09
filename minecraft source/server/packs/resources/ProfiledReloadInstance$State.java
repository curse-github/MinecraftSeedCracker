/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class State
/*    */   extends Record
/*    */ {
/*    */   private final String name;
/*    */   private final AtomicLong preparationNanos;
/*    */   private final AtomicLong preparationCount;
/*    */   private final AtomicLong reloadNanos;
/*    */   private final AtomicLong reloadCount;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #91	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #91	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #91	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 91 */   public State(String name, AtomicLong preparationNanos, AtomicLong preparationCount, AtomicLong reloadNanos, AtomicLong reloadCount) { this.name = name; this.preparationNanos = preparationNanos; this.preparationCount = preparationCount; this.reloadNanos = reloadNanos; this.reloadCount = reloadCount; } public String name() { return this.name; } public AtomicLong preparationNanos() { return this.preparationNanos; } public AtomicLong preparationCount() { return this.preparationCount; } public AtomicLong reloadNanos() { return this.reloadNanos; } public AtomicLong reloadCount() { return this.reloadCount; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\ProfiledReloadInstance$State.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
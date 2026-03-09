/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ProblemReporter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class RecursiveReferenceProblem
/*    */   extends Record
/*    */   implements ProblemReporter.Problem
/*    */ {
/*    */   private final ResourceKey<?> referenced;
/*    */   
/* 91 */   public RecursiveReferenceProblem(ResourceKey<?> referenced) { this.referenced = referenced; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #91	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 91 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem; } public ResourceKey<?> referenced() { return this.referenced; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #91	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #91	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   public String description() {
/* 94 */     return String.valueOf(this.referenced.identifier()) + " of type " + String.valueOf(this.referenced.identifier()) + " is recursively called";
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\ValidationContext$RecursiveReferenceProblem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
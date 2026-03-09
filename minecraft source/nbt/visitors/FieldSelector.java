/*    */ package net.minecraft.nbt.visitors;
/*    */ public final class FieldSelector extends Record {
/*    */   private final List<String> path;
/*    */   private final TagType<?> type;
/*    */   private final String name;
/*    */   
/*  7 */   public FieldSelector(List<String> path, TagType<?> type, String name) { this.path = path; this.type = type; this.name = name; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/nbt/visitors/FieldSelector;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/nbt/visitors/FieldSelector; } public List<String> path() { return this.path; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/visitors/FieldSelector;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/nbt/visitors/FieldSelector; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/visitors/FieldSelector;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/nbt/visitors/FieldSelector;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public TagType<?> type() { return this.type; } public String name() { return this.name; }
/*    */   
/*  9 */   public FieldSelector(TagType<?> type, String name) { this(List.of(), type, name); }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public FieldSelector(String parent, TagType<?> type, String name) { this(List.of(parent), type, name); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public FieldSelector(String grandparent, String parent, TagType<?> type, String name) { this(List.of(grandparent, parent), type, name); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\visitors\FieldSelector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
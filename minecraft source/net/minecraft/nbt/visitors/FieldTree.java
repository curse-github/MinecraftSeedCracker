/*    */ package net.minecraft.nbt.visitors;
/*    */ 
/*    */ public final class FieldTree extends Record {
/*    */   private final int depth;
/*    */   private final Map<String, TagType<?>> selectedFields;
/*    */   private final Map<String, FieldTree> fieldsToRecurse;
/*    */   
/*  8 */   public FieldTree(int depth, Map<String, TagType<?>> selectedFields, Map<String, FieldTree> fieldsToRecurse) { this.depth = depth; this.selectedFields = selectedFields; this.fieldsToRecurse = fieldsToRecurse; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/nbt/visitors/FieldTree;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/nbt/visitors/FieldTree; } public int depth() { return this.depth; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/visitors/FieldTree;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/nbt/visitors/FieldTree; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/visitors/FieldTree;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/nbt/visitors/FieldTree;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public Map<String, TagType<?>> selectedFields() { return this.selectedFields; } public Map<String, FieldTree> fieldsToRecurse() { return this.fieldsToRecurse; }
/*    */   
/* 10 */   private FieldTree(int depth) { this(depth, new HashMap(), new HashMap()); }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static FieldTree createRoot() { return new FieldTree(1); }
/*    */ 
/*    */   
/*    */   public void addEntry(FieldSelector field) {
/* 18 */     if (this.depth <= field.path().size()) {
/* 19 */       ((FieldTree)this.fieldsToRecurse.computeIfAbsent((String)field.path().get(this.depth - 1), s -> new FieldTree(this.depth + 1))).addEntry(field);
/*    */     } else {
/* 21 */       this.selectedFields.put(field.name(), field.type());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 26 */   public boolean isSelected(TagType<?> type, String id) { return type.equals(selectedFields().get(id)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\visitors\FieldTree.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
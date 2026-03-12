/*   */ package net.minecraft.server.packs;public final class PackSelectionConfig extends Record { private final boolean required;
/*   */   private final Pack.Position defaultPosition;
/*   */   private final boolean fixedPosition;
/*   */   
/* 5 */   public PackSelectionConfig(boolean required, Pack.Position defaultPosition, boolean fixedPosition) { this.required = required; this.defaultPosition = defaultPosition; this.fixedPosition = fixedPosition; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/PackSelectionConfig;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 5 */     //   0	7	0	this	Lnet/minecraft/server/packs/PackSelectionConfig; } public boolean required() { return this.required; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/PackSelectionConfig;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/server/packs/PackSelectionConfig; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/PackSelectionConfig;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/server/packs/PackSelectionConfig;
/* 5 */     //   0	8	1	o	Ljava/lang/Object; } public Pack.Position defaultPosition() { return this.defaultPosition; } public boolean fixedPosition() { return this.fixedPosition; } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\PackSelectionConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
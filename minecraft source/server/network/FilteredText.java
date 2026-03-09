/*    */ package net.minecraft.server.network;
/*    */ import net.minecraft.network.chat.FilterMask;
/*    */ 
/*    */ public final class FilteredText extends Record {
/*    */   private final String raw;
/*    */   private final FilterMask mask;
/*    */   
/*  8 */   public FilteredText(String raw, FilterMask mask) { this.raw = raw; this.mask = mask; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/network/FilteredText;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/server/network/FilteredText; } public String raw() { return this.raw; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/network/FilteredText;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/network/FilteredText; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/network/FilteredText;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/network/FilteredText;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public FilterMask mask() { return this.mask; }
/*  9 */   public static final FilteredText EMPTY = passThrough("");
/*    */ 
/*    */   
/* 12 */   public static FilteredText passThrough(String message) { return new FilteredText(message, FilterMask.PASS_THROUGH); }
/*    */ 
/*    */ 
/*    */   
/* 16 */   public static FilteredText fullyFiltered(String message) { return new FilteredText(message, FilterMask.FULLY_FILTERED); }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public String filtered() { return this.mask.apply(this.raw); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public String filteredOrEmpty() { return (String)Objects.requireNonNullElse(filtered(), ""); }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public boolean isFiltered() { return !this.mask.isEmpty(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\FilteredText.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
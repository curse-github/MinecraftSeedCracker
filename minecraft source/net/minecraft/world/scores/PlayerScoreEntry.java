/*    */ package net.minecraft.world.scores;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ 
/*    */ public final class PlayerScoreEntry extends Record {
/*    */   private final String owner;
/*    */   private final int value;
/*    */   private final Component display;
/*    */   private final NumberFormat numberFormatOverride;
/*    */   
/* 10 */   public PlayerScoreEntry(String owner, int value, Component display, NumberFormat numberFormatOverride) { this.owner = owner; this.value = value; this.display = display; this.numberFormatOverride = numberFormatOverride; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/PlayerScoreEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/scores/PlayerScoreEntry; } public String owner() { return this.owner; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/PlayerScoreEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/scores/PlayerScoreEntry; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/PlayerScoreEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/scores/PlayerScoreEntry;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public int value() { return this.value; } public Component display() { return this.display; } public NumberFormat numberFormatOverride() { return this.numberFormatOverride; }
/*    */   
/* 12 */   public boolean isHidden() { return this.owner.startsWith("#"); }
/*    */ 
/*    */   
/*    */   public Component ownerName() {
/* 16 */     if (this.display != null) {
/* 17 */       return this.display;
/*    */     }
/* 19 */     return Component.literal(owner());
/*    */   }
/*    */ 
/*    */   
/* 23 */   public MutableComponent formatValue(NumberFormat _default) { return ((NumberFormat)Objects.requireNonNullElse(this.numberFormatOverride, _default)).format(this.value); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\PlayerScoreEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
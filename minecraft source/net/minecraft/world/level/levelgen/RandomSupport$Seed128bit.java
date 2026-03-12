/*    */ package net.minecraft.world.level.levelgen;
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
/*    */ public final class Seed128bit
/*    */   extends Record
/*    */ {
/*    */   private final long seedLo;
/*    */   private final long seedHi;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #53	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #53	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #53	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 53 */   public Seed128bit(long seedLo, long seedHi) { this.seedLo = seedLo; this.seedHi = seedHi; } public long seedLo() { return this.seedLo; } public long seedHi() { return this.seedHi; }
/*    */   
/* 55 */   public Seed128bit xor(long lo, long hi) { return new Seed128bit(this.seedLo ^ lo, this.seedHi ^ hi); }
/*    */ 
/*    */ 
/*    */   
/* 59 */   public Seed128bit xor(Seed128bit other) { return xor(other.seedLo, other.seedHi); }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public Seed128bit mixed() { return new Seed128bit(RandomSupport.mixStafford13(this.seedLo), RandomSupport.mixStafford13(this.seedHi)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\RandomSupport$Seed128bit.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
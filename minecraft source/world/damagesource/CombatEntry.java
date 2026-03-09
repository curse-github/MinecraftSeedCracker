/*   */ package net.minecraft.world.damagesource;public final class CombatEntry extends Record { private final DamageSource source; private final float damage;
/*   */   private final FallLocation fallLocation;
/*   */   private final float fallDistance;
/*   */   
/* 5 */   public CombatEntry(DamageSource source, float damage, FallLocation fallLocation, float fallDistance) { this.source = source; this.damage = damage; this.fallLocation = fallLocation; this.fallDistance = fallDistance; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/damagesource/CombatEntry;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 5 */     //   0	7	0	this	Lnet/minecraft/world/damagesource/CombatEntry; } public DamageSource source() { return this.source; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/damagesource/CombatEntry;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/damagesource/CombatEntry; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/damagesource/CombatEntry;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/damagesource/CombatEntry;
/* 5 */     //   0	8	1	o	Ljava/lang/Object; } public float damage() { return this.damage; } public FallLocation fallLocation() { return this.fallLocation; } public float fallDistance() { return this.fallDistance; } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\damagesource\CombatEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
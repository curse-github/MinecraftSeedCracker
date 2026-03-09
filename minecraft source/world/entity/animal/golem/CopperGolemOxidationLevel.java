/*   */ package net.minecraft.world.entity.animal.golem;public final class CopperGolemOxidationLevel extends Record {
/*   */   private final SoundEvent spinHeadSound;
/*   */   private final SoundEvent hurtSound;
/*   */   private final SoundEvent deathSound;
/*   */   
/* 6 */   public CopperGolemOxidationLevel(SoundEvent spinHeadSound, SoundEvent hurtSound, SoundEvent deathSound, SoundEvent stepSound, Identifier texture, Identifier eyeTexture) { this.spinHeadSound = spinHeadSound; this.hurtSound = hurtSound; this.deathSound = deathSound; this.stepSound = stepSound; this.texture = texture; this.eyeTexture = eyeTexture; } private final SoundEvent stepSound; private final Identifier texture; private final Identifier eyeTexture; public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel;
/* 6 */     //   0	8	1	o	Ljava/lang/Object; } public SoundEvent spinHeadSound() { return this.spinHeadSound; } public SoundEvent hurtSound() { return this.hurtSound; } public SoundEvent deathSound() { return this.deathSound; } public SoundEvent stepSound() { return this.stepSound; } public Identifier texture() { return this.texture; } public Identifier eyeTexture() { return this.eyeTexture; }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\golem\CopperGolemOxidationLevel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
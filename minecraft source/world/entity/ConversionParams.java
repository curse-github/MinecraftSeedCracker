/*    */ package net.minecraft.world.entity;public final class ConversionParams extends Record { private final ConversionType type;
/*    */   private final boolean keepEquipment;
/*    */   private final boolean preserveCanPickUpLoot;
/*    */   private final PlayerTeam team;
/*    */   
/*  6 */   public ConversionParams(ConversionType type, boolean keepEquipment, boolean preserveCanPickUpLoot, PlayerTeam team) { this.type = type; this.keepEquipment = keepEquipment; this.preserveCanPickUpLoot = preserveCanPickUpLoot; this.team = team; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ConversionParams;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  6 */     //   0	7	0	this	Lnet/minecraft/world/entity/ConversionParams; } public ConversionType type() { return this.type; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ConversionParams;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ConversionParams; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ConversionParams;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ConversionParams;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public boolean keepEquipment() { return this.keepEquipment; } public boolean preserveCanPickUpLoot() { return this.preserveCanPickUpLoot; } public PlayerTeam team() { return this.team; }
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
/* 19 */   public static ConversionParams single(Mob mob, boolean keepEquipment, boolean preserveCanPickUpLoot) { return new ConversionParams(ConversionType.SINGLE, keepEquipment, preserveCanPickUpLoot, mob.getTeam()); }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface AfterConversion<T extends Mob> {
/*    */     void finalizeConversion(T param1T);
/*    */   } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ConversionParams.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
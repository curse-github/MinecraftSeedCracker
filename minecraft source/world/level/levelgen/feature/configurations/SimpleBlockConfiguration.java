/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ public final class SimpleBlockConfiguration extends Record implements FeatureConfiguration {
/*    */   private final BlockStateProvider toPlace;
/*    */   private final boolean scheduleTick;
/*    */   
/*  7 */   public SimpleBlockConfiguration(BlockStateProvider toPlace, boolean scheduleTick) { this.toPlace = toPlace; this.scheduleTick = scheduleTick; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleBlockConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleBlockConfiguration; } public BlockStateProvider toPlace() { return this.toPlace; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleBlockConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleBlockConfiguration; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleBlockConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleBlockConfiguration;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public boolean scheduleTick() { return this.scheduleTick; }
/*  8 */   public static final Codec<SimpleBlockConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(BlockStateProvider.CODEC
/*  9 */         .fieldOf("to_place").forGetter(()), Codec.BOOL
/* 10 */         .optionalFieldOf("schedule_tick", Boolean.valueOf(false)).forGetter(()))
/* 11 */       .apply(i, SimpleBlockConfiguration::new));
/*    */ 
/*    */   
/* 14 */   public SimpleBlockConfiguration(BlockStateProvider toPlace) { this(toPlace, false); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\SimpleBlockConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
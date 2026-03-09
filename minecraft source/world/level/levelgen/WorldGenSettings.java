/*    */ package net.minecraft.world.level.levelgen;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class WorldGenSettings extends Record {
/*    */   private final WorldOptions options;
/*    */   private final WorldDimensions dimensions;
/*    */   
/* 10 */   public WorldGenSettings(WorldOptions options, WorldDimensions dimensions) { this.options = options; this.dimensions = dimensions; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/WorldGenSettings;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/WorldGenSettings; } public WorldOptions options() { return this.options; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/WorldGenSettings;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/WorldGenSettings; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/WorldGenSettings;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/WorldGenSettings;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public WorldDimensions dimensions() { return this.dimensions; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final Codec<WorldGenSettings> CODEC = RecordCodecBuilder.create(i -> i.group(WorldOptions.CODEC
/* 15 */         .forGetter(WorldGenSettings::options), WorldDimensions.CODEC
/* 16 */         .forGetter(WorldGenSettings::dimensions))
/* 17 */       .apply(i, i.stable(WorldGenSettings::new)));
/*    */ 
/*    */   
/* 20 */   public static <T> DataResult<T> encode(DynamicOps<T> ops, WorldOptions options, WorldDimensions dimensions) { return CODEC.encodeStart(ops, new WorldGenSettings(options, dimensions)); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static <T> DataResult<T> encode(DynamicOps<T> ops, WorldOptions options, RegistryAccess registryAccess) { return encode(ops, options, new WorldDimensions(registryAccess.lookupOrThrow(Registries.LEVEL_STEM))); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\WorldGenSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.levelgen;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public final class NoiseSettings extends Record {
/*    */   private final int minY;
/*    */   private final int height;
/*    */   private final int noiseSizeHorizontal;
/*    */   private final int noiseSizeVertical;
/*    */   
/* 14 */   public NoiseSettings(int minY, int height, int noiseSizeHorizontal, int noiseSizeVertical) { this.minY = minY; this.height = height; this.noiseSizeHorizontal = noiseSizeHorizontal; this.noiseSizeVertical = noiseSizeVertical; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/NoiseSettings;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/NoiseSettings; } public int minY() { return this.minY; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/NoiseSettings;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/NoiseSettings; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/NoiseSettings;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/NoiseSettings;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public int height() { return this.height; } public int noiseSizeHorizontal() { return this.noiseSizeHorizontal; } public int noiseSizeVertical() { return this.noiseSizeVertical; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static final Codec<NoiseSettings> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 21 */         Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y).fieldOf("min_y").forGetter(NoiseSettings::minY), 
/* 22 */         Codec.intRange(0, DimensionType.Y_SIZE).fieldOf("height").forGetter(NoiseSettings::height), 
/* 23 */         Codec.intRange(1, 4).fieldOf("size_horizontal").forGetter(NoiseSettings::noiseSizeHorizontal), 
/* 24 */         Codec.intRange(1, 4).fieldOf("size_vertical").forGetter(NoiseSettings::noiseSizeVertical))
/* 25 */       .apply(i, NoiseSettings::new)).comapFlatMap(NoiseSettings::guardY, Function.identity());
/*    */   
/* 27 */   protected static final NoiseSettings OVERWORLD_NOISE_SETTINGS = create(-64, 384, 1, 2);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   protected static final NoiseSettings NETHER_NOISE_SETTINGS = create(0, 128, 1, 2);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   protected static final NoiseSettings END_NOISE_SETTINGS = create(0, 128, 2, 1);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   protected static final NoiseSettings CAVES_NOISE_SETTINGS = create(-64, 192, 1, 2);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   protected static final NoiseSettings FLOATING_ISLANDS_NOISE_SETTINGS = create(0, 256, 2, 1);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static DataResult<NoiseSettings> guardY(NoiseSettings dimensionType) {
/* 63 */     if (dimensionType.minY() + dimensionType.height() > DimensionType.MAX_Y + 1) {
/* 64 */       return DataResult.error(() -> "min_y + height cannot be higher than: " + DimensionType.MAX_Y + 1);
/*    */     }
/*    */     
/* 67 */     if (dimensionType.height() % 16 != 0) {
/* 68 */       return DataResult.error(() -> "height has to be a multiple of 16");
/*    */     }
/*    */     
/* 71 */     if (dimensionType.minY() % 16 != 0) {
/* 72 */       return DataResult.error(() -> "min_y has to be a multiple of 16");
/*    */     }
/*    */     
/* 75 */     return DataResult.success(dimensionType);
/*    */   }
/*    */   
/*    */   public static NoiseSettings create(int minY, int height, int noiseSizeHorizontal, int noiseSizeVertical) {
/* 79 */     NoiseSettings noiseSettings = new NoiseSettings(minY, height, noiseSizeHorizontal, noiseSizeVertical);
/*    */     
/* 81 */     guardY(noiseSettings).error().ifPresent(error -> {
/* 82 */           throw new IllegalStateException(error.message());
/*    */         });
/*    */     
/* 85 */     return noiseSettings;
/*    */   }
/*    */ 
/*    */   
/* 89 */   public int getCellHeight() { return QuartPos.toBlock(noiseSizeVertical()); }
/*    */ 
/*    */ 
/*    */   
/* 93 */   public int getCellWidth() { return QuartPos.toBlock(noiseSizeHorizontal()); }
/*    */ 
/*    */   
/*    */   public NoiseSettings clampToHeightAccessor(LevelHeightAccessor heightAccessor) {
/* 97 */     int newMinY = Math.max(this.minY, heightAccessor.getMinY());
/* 98 */     int newHeight = Math.min(this.minY + this.height, heightAccessor.getMaxY() + 1) - newMinY;
/* 99 */     return new NoiseSettings(newMinY, newHeight, this.noiseSizeHorizontal, this.noiseSizeVertical);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\NoiseSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
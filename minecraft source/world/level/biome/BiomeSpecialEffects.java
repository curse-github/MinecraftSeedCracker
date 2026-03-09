/*     */ package net.minecraft.world.level.biome;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class BiomeSpecialEffects extends Record {
/*     */   private final int waterColor;
/*     */   private final Optional<Integer> foliageColorOverride;
/*     */   
/*  11 */   public BiomeSpecialEffects(int waterColor, Optional<Integer> foliageColorOverride, Optional<Integer> dryFoliageColorOverride, Optional<Integer> grassColorOverride, GrassColorModifier grassColorModifier) { this.waterColor = waterColor; this.foliageColorOverride = foliageColorOverride; this.dryFoliageColorOverride = dryFoliageColorOverride; this.grassColorOverride = grassColorOverride; this.grassColorModifier = grassColorModifier; } private final Optional<Integer> dryFoliageColorOverride; private final Optional<Integer> grassColorOverride; private final GrassColorModifier grassColorModifier; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/BiomeSpecialEffects;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #11	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/biome/BiomeSpecialEffects; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/BiomeSpecialEffects;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #11	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/biome/BiomeSpecialEffects; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/BiomeSpecialEffects;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #11	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/biome/BiomeSpecialEffects;
/*  11 */     //   0	8	1	o	Ljava/lang/Object; } public int waterColor() { return this.waterColor; } public Optional<Integer> foliageColorOverride() { return this.foliageColorOverride; } public Optional<Integer> dryFoliageColorOverride() { return this.dryFoliageColorOverride; } public Optional<Integer> grassColorOverride() { return this.grassColorOverride; } public GrassColorModifier grassColorModifier() { return this.grassColorModifier; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  18 */   public static final Codec<BiomeSpecialEffects> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.STRING_RGB_COLOR
/*  19 */         .fieldOf("water_color").forGetter(BiomeSpecialEffects::waterColor), ExtraCodecs.STRING_RGB_COLOR
/*  20 */         .optionalFieldOf("foliage_color").forGetter(BiomeSpecialEffects::foliageColorOverride), ExtraCodecs.STRING_RGB_COLOR
/*  21 */         .optionalFieldOf("dry_foliage_color").forGetter(BiomeSpecialEffects::dryFoliageColorOverride), ExtraCodecs.STRING_RGB_COLOR
/*  22 */         .optionalFieldOf("grass_color").forGetter(BiomeSpecialEffects::grassColorOverride), GrassColorModifier.CODEC
/*  23 */         .optionalFieldOf("grass_color_modifier", GrassColorModifier.NONE).forGetter(BiomeSpecialEffects::grassColorModifier))
/*  24 */       .apply(i, BiomeSpecialEffects::new));
/*     */   
/*     */   public static class Builder {
/*  27 */     private OptionalInt waterColor = OptionalInt.empty();
/*  28 */     private Optional<Integer> foliageColorOverride = Optional.empty();
/*  29 */     private Optional<Integer> dryFoliageColorOverride = Optional.empty();
/*  30 */     private Optional<Integer> grassColorOverride = Optional.empty();
/*  31 */     private BiomeSpecialEffects.GrassColorModifier grassColorModifier = BiomeSpecialEffects.GrassColorModifier.NONE;
/*     */     
/*     */     public Builder waterColor(int waterColor) {
/*  34 */       this.waterColor = OptionalInt.of(waterColor);
/*  35 */       return this;
/*     */     }
/*     */     
/*     */     public Builder foliageColorOverride(int foliageColor) {
/*  39 */       this.foliageColorOverride = Optional.of(Integer.valueOf(foliageColor));
/*  40 */       return this;
/*     */     }
/*     */     
/*     */     public Builder dryFoliageColorOverride(int dryFoliageColor) {
/*  44 */       this.dryFoliageColorOverride = Optional.of(Integer.valueOf(dryFoliageColor));
/*  45 */       return this;
/*     */     }
/*     */     
/*     */     public Builder grassColorOverride(int grassColor) {
/*  49 */       this.grassColorOverride = Optional.of(Integer.valueOf(grassColor));
/*  50 */       return this;
/*     */     }
/*     */     
/*     */     public Builder grassColorModifier(BiomeSpecialEffects.GrassColorModifier grassModifier) {
/*  54 */       this.grassColorModifier = grassModifier;
/*  55 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeSpecialEffects build() {
/*  59 */       return new BiomeSpecialEffects(this.waterColor
/*  60 */           .orElseThrow(() -> new IllegalStateException("Missing 'water' color.")), this.foliageColorOverride, this.dryFoliageColorOverride, this.grassColorOverride, this.grassColorModifier);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final abstract enum GrassColorModifier
/*     */     implements StringRepresentable
/*     */   {
/*     */     NONE, DARK_FOREST, SWAMP;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final Codec<GrassColorModifier> CODEC;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static  {
/*     */       // Byte code:
/*     */       //   0: new net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier$1
/*     */       //   3: dup
/*     */       //   4: ldc 'NONE'
/*     */       //   6: iconst_0
/*     */       //   7: ldc 'none'
/*     */       //   9: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */       //   12: putstatic net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier.NONE : Lnet/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier;
/*     */       //   15: new net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier$2
/*     */       //   18: dup
/*     */       //   19: ldc 'DARK_FOREST'
/*     */       //   21: iconst_1
/*     */       //   22: ldc 'dark_forest'
/*     */       //   24: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */       //   27: putstatic net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier.DARK_FOREST : Lnet/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier;
/*     */       //   30: new net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier$3
/*     */       //   33: dup
/*     */       //   34: ldc 'SWAMP'
/*     */       //   36: iconst_2
/*     */       //   37: ldc 'swamp'
/*     */       //   39: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */       //   42: putstatic net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier.SWAMP : Lnet/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier;
/*     */       //   45: invokestatic $values : ()[Lnet/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier;
/*     */       //   48: putstatic net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier.$VALUES : [Lnet/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier;
/*     */       //   51: <illegal opcode> get : ()Ljava/util/function/Supplier;
/*     */       //   56: invokestatic fromEnum : (Ljava/util/function/Supplier;)Lnet/minecraft/util/StringRepresentable$EnumCodec;
/*     */       //   59: putstatic net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier.CODEC : Lcom/mojang/serialization/Codec;
/*     */       //   62: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       //   #76	-> 15
/*     */       //   #82	-> 30
/*     */       //   #69	-> 45
/*     */       //   #101	-> 51
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     GrassColorModifier(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     public String getSerializedName() { return this.name; }
/*     */     
/*     */     public abstract int modifyColor(double param1Double1, double param1Double2, int param1Int);
/*     */   }
/*     */   
/*     */   static enum null {
/*     */     public int modifyColor(double x, double z, int baseColor) { return baseColor; }
/*     */   }
/*     */   
/*     */   static enum null {
/*     */     public int modifyColor(double x, double z, int baseColor) { return (baseColor & 0xFEFEFE) + 2634762 >> 1; }
/*     */   }
/*     */   
/*     */   static enum null {
/*     */     public int modifyColor(double x, double z, int baseColor) {
/*     */       double groundValue = Biome.BIOME_INFO_NOISE.getValue(x * 0.0225D, z * 0.0225D, false);
/*     */       if (groundValue < -0.1D)
/*     */         return 5011004; 
/*     */       return 6975545;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\BiomeSpecialEffects.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
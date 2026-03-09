/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public static final abstract enum GrassColorModifier
/*     */   implements StringRepresentable
/*     */ {
/*     */   NONE, DARK_FOREST, SWAMP;
/*     */   private final String name;
/*     */   public static final Codec<GrassColorModifier> CODEC;
/*     */   
/*     */   static  {
/*     */     // Byte code:
/*     */     //   0: new net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier$1
/*     */     //   3: dup
/*     */     //   4: ldc 'NONE'
/*     */     //   6: iconst_0
/*     */     //   7: ldc 'none'
/*     */     //   9: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   12: putstatic net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier.NONE : Lnet/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier;
/*     */     //   15: new net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier$2
/*     */     //   18: dup
/*     */     //   19: ldc 'DARK_FOREST'
/*     */     //   21: iconst_1
/*     */     //   22: ldc 'dark_forest'
/*     */     //   24: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   27: putstatic net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier.DARK_FOREST : Lnet/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier;
/*     */     //   30: new net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier$3
/*     */     //   33: dup
/*     */     //   34: ldc 'SWAMP'
/*     */     //   36: iconst_2
/*     */     //   37: ldc 'swamp'
/*     */     //   39: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   42: putstatic net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier.SWAMP : Lnet/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier;
/*     */     //   45: invokestatic $values : ()[Lnet/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier;
/*     */     //   48: putstatic net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier.$VALUES : [Lnet/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier;
/*     */     //   51: <illegal opcode> get : ()Ljava/util/function/Supplier;
/*     */     //   56: invokestatic fromEnum : (Ljava/util/function/Supplier;)Lnet/minecraft/util/StringRepresentable$EnumCodec;
/*     */     //   59: putstatic net/minecraft/world/level/biome/BiomeSpecialEffects$GrassColorModifier.CODEC : Lcom/mojang/serialization/Codec;
/*     */     //   62: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #70	-> 0
/*     */     //   #76	-> 15
/*     */     //   #82	-> 30
/*     */     //   #69	-> 45
/*     */     //   #101	-> 51
/*     */   }
/*     */   
/*  98 */   GrassColorModifier(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public String getSerializedName() { return this.name; }
/*     */   
/*     */   public abstract int modifyColor(double paramDouble1, double paramDouble2, int paramInt);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\BiomeSpecialEffects$GrassColorModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
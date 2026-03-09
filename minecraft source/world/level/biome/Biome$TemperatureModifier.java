/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
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
/*     */ public static final abstract enum TemperatureModifier
/*     */   implements StringRepresentable
/*     */ {
/*     */   NONE, FROZEN;
/*     */   private final String name;
/*     */   public static final Codec<TemperatureModifier> CODEC;
/*     */   
/*     */   static  {
/*     */     // Byte code:
/*     */     //   0: new net/minecraft/world/level/biome/Biome$TemperatureModifier$1
/*     */     //   3: dup
/*     */     //   4: ldc 'NONE'
/*     */     //   6: iconst_0
/*     */     //   7: ldc 'none'
/*     */     //   9: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   12: putstatic net/minecraft/world/level/biome/Biome$TemperatureModifier.NONE : Lnet/minecraft/world/level/biome/Biome$TemperatureModifier;
/*     */     //   15: new net/minecraft/world/level/biome/Biome$TemperatureModifier$2
/*     */     //   18: dup
/*     */     //   19: ldc 'FROZEN'
/*     */     //   21: iconst_1
/*     */     //   22: ldc 'frozen'
/*     */     //   24: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   27: putstatic net/minecraft/world/level/biome/Biome$TemperatureModifier.FROZEN : Lnet/minecraft/world/level/biome/Biome$TemperatureModifier;
/*     */     //   30: invokestatic $values : ()[Lnet/minecraft/world/level/biome/Biome$TemperatureModifier;
/*     */     //   33: putstatic net/minecraft/world/level/biome/Biome$TemperatureModifier.$VALUES : [Lnet/minecraft/world/level/biome/Biome$TemperatureModifier;
/*     */     //   36: <illegal opcode> get : ()Ljava/util/function/Supplier;
/*     */     //   41: invokestatic fromEnum : (Ljava/util/function/Supplier;)Lnet/minecraft/util/StringRepresentable$EnumCodec;
/*     */     //   44: putstatic net/minecraft/world/level/biome/Biome$TemperatureModifier.CODEC : Lcom/mojang/serialization/Codec;
/*     */     //   47: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #90	-> 0
/*     */     //   #96	-> 15
/*     */     //   #89	-> 30
/*     */     //   #121	-> 36
/*     */   }
/*     */   
/* 118 */   TemperatureModifier(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public String getSerializedName() { return this.name; }
/*     */   
/*     */   public abstract float modifyTemperature(BlockPos paramBlockPos, float paramFloat);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\Biome$TemperatureModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
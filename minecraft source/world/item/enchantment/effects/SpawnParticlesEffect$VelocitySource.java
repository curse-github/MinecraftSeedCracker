/*     */ package net.minecraft.world.item.enchantment.effects;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.ConstantFloat;
/*     */ import net.minecraft.util.valueproviders.FloatProvider;
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
/*     */ public final class VelocitySource
/*     */   extends Record
/*     */ {
/*     */   private final float movementScale;
/*     */   private final FloatProvider base;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #95	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #95	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #95	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*  95 */   public VelocitySource(float movementScale, FloatProvider base) { this.movementScale = movementScale; this.base = base; } public float movementScale() { return this.movementScale; } public FloatProvider base() { return this.base; }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public static final MapCodec<VelocitySource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/* 100 */         .optionalFieldOf("movement_scale", Float.valueOf(0.0F)).forGetter(VelocitySource::movementScale), FloatProvider.CODEC
/* 101 */         .optionalFieldOf("base", ConstantFloat.ZERO).forGetter(VelocitySource::base))
/* 102 */       .apply(i, VelocitySource::new));
/*     */ 
/*     */   
/* 105 */   public double getVelocity(double movement, RandomSource random) { return movement * this.movementScale + this.base.sample(random); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\SpawnParticlesEffect$VelocitySource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
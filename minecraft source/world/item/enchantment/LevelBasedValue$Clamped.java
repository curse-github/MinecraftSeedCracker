/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.util.Mth;
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
/*     */ public final class Clamped
/*     */   extends Record
/*     */   implements LevelBasedValue
/*     */ {
/*     */   private final LevelBasedValue value;
/*     */   private final float min;
/*     */   private final float max;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #104	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #104	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #104	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 104 */   public Clamped(LevelBasedValue value, float min, float max) { this.value = value; this.min = min; this.max = max; } public LevelBasedValue value() { return this.value; } public float min() { return this.min; } public float max() { return this.max; }
/* 105 */   public static final MapCodec<Clamped> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LevelBasedValue.CODEC
/* 106 */         .fieldOf("value").forGetter(Clamped::value), Codec.FLOAT
/* 107 */         .fieldOf("min").forGetter(Clamped::min), Codec.FLOAT
/* 108 */         .fieldOf("max").forGetter(Clamped::max))
/* 109 */       .apply(i, Clamped::new)).validate(u -> {
/* 110 */         if (u.max <= u.min) {
/* 111 */           return DataResult.error(());
/*     */         }
/* 113 */         return DataResult.success(u);
/*     */       });
/*     */ 
/*     */ 
/*     */   
/* 118 */   public float calculate(int level) { return Mth.clamp(this.value.calculate(level), this.min, this.max); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public MapCodec<Clamped> codec() { return CODEC; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\LevelBasedValue$Clamped.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
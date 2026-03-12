/*     */ package net.minecraft.world.item.enchantment;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public interface LevelBasedValue {
/*  16 */   public static final Codec<LevelBasedValue> DISPATCH_CODEC = BuiltInRegistries.ENCHANTMENT_LEVEL_BASED_VALUE_TYPE.byNameCodec().dispatch(LevelBasedValue::codec, c -> c);
/*     */   
/*  18 */   public static final Codec<LevelBasedValue> CODEC = Codec.either(Constant.CODEC, DISPATCH_CODEC).xmap(either -> 
/*  19 */       (LevelBasedValue)either.map((), ()), levelBasedValue -> {
/*  20 */         Constant constant = (Constant)levelBasedValue; return (levelBasedValue instanceof Constant) ? Either.left(constant) : Either.right(levelBasedValue);
/*     */       });
/*     */   
/*     */   static MapCodec<? extends LevelBasedValue> bootstrap(Registry<MapCodec<? extends LevelBasedValue>> registry) {
/*  24 */     Registry.register(registry, "clamped", Clamped.CODEC);
/*  25 */     Registry.register(registry, "fraction", Fraction.CODEC);
/*  26 */     Registry.register(registry, "levels_squared", LevelsSquared.CODEC);
/*  27 */     Registry.register(registry, "linear", Linear.CODEC);
/*  28 */     Registry.register(registry, "exponent", Exponent.CODEC);
/*  29 */     return (MapCodec)Registry.register(registry, "lookup", Lookup.CODEC);
/*     */   }
/*     */ 
/*     */   
/*  33 */   static Constant constant(float value) { return new Constant(value); }
/*     */ 
/*     */ 
/*     */   
/*  37 */   static Linear perLevel(float base, float perLevelAboveFirst) { return new Linear(base, perLevelAboveFirst); }
/*     */ 
/*     */ 
/*     */   
/*  41 */   static Linear perLevel(float perLevel) { return perLevel(perLevel, perLevel); }
/*     */ 
/*     */ 
/*     */   
/*  45 */   static Lookup lookup(List<Float> values, LevelBasedValue fallback) { return new Lookup(values, fallback); }
/*     */   
/*     */   float calculate(int paramInt);
/*     */   
/*     */   MapCodec<? extends LevelBasedValue> codec();
/*     */   
/*     */   public static final class Constant extends Record implements LevelBasedValue { private final float value;
/*     */     
/*  53 */     public Constant(float value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Constant;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #53	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  53 */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Constant; } public float value() { return this.value; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Constant;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #53	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Constant; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Constant;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #53	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Constant;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/*  54 */     public static final Codec<Constant> CODEC = Codec.FLOAT.xmap(Constant::new, Constant::value);
/*  55 */     public static final MapCodec<Constant> TYPED_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/*  56 */           .fieldOf("value").forGetter(Constant::value))
/*  57 */         .apply(i, Constant::new));
/*     */ 
/*     */ 
/*     */     
/*  61 */     public float calculate(int level) { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     public MapCodec<Constant> codec() { return TYPED_CODEC; } }
/*     */   public static final class Lookup extends Record implements LevelBasedValue { private final List<Float> values;
/*     */     private final LevelBasedValue fallback;
/*     */     
/*  70 */     public Lookup(List<Float> values, LevelBasedValue fallback) { this.values = values; this.fallback = fallback; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;
/*  70 */       //   0	8	1	o	Ljava/lang/Object; } public List<Float> values() { return this.values; } public LevelBasedValue fallback() { return this.fallback; }
/*  71 */     public static final MapCodec<Lookup> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/*  72 */           .listOf().fieldOf("values").forGetter(Lookup::values), LevelBasedValue.CODEC
/*  73 */           .fieldOf("fallback").forGetter(Lookup::fallback))
/*  74 */         .apply(i, Lookup::new));
/*     */ 
/*     */ 
/*     */     
/*  78 */     public float calculate(int level) { return (level <= this.values.size()) ? ((Float)this.values.get(level - 1)).floatValue() : this.fallback.calculate(level); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     public MapCodec<Lookup> codec() { return CODEC; } }
/*     */   public static final class Linear extends Record implements LevelBasedValue { private final float base;
/*     */     private final float perLevelAboveFirst;
/*     */     
/*  87 */     public Linear(float base, float perLevelAboveFirst) { this.base = base; this.perLevelAboveFirst = perLevelAboveFirst; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear;
/*  87 */       //   0	8	1	o	Ljava/lang/Object; } public float base() { return this.base; } public float perLevelAboveFirst() { return this.perLevelAboveFirst; }
/*  88 */     public static final MapCodec<Linear> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/*  89 */           .fieldOf("base").forGetter(Linear::base), Codec.FLOAT
/*  90 */           .fieldOf("per_level_above_first").forGetter(Linear::perLevelAboveFirst))
/*  91 */         .apply(i, Linear::new));
/*     */ 
/*     */ 
/*     */     
/*  95 */     public float calculate(int level) { return this.base + this.perLevelAboveFirst * (level - 1); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     public MapCodec<Linear> codec() { return CODEC; } }
/*     */   public static final class Clamped extends Record implements LevelBasedValue { private final LevelBasedValue value; private final float min;
/*     */     private final float max;
/*     */     
/* 104 */     public Clamped(LevelBasedValue value, float min, float max) { this.value = value; this.min = min; this.max = max; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;
/* 104 */       //   0	8	1	o	Ljava/lang/Object; } public LevelBasedValue value() { return this.value; } public float min() { return this.min; } public float max() { return this.max; }
/* 105 */     public static final MapCodec<Clamped> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LevelBasedValue.CODEC
/* 106 */           .fieldOf("value").forGetter(Clamped::value), Codec.FLOAT
/* 107 */           .fieldOf("min").forGetter(Clamped::min), Codec.FLOAT
/* 108 */           .fieldOf("max").forGetter(Clamped::max))
/* 109 */         .apply(i, Clamped::new)).validate(u -> {
/* 110 */           if (u.max <= u.min) {
/* 111 */             return DataResult.error(());
/*     */           }
/* 113 */           return DataResult.success(u);
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 118 */     public float calculate(int level) { return Mth.clamp(this.value.calculate(level), this.min, this.max); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     public MapCodec<Clamped> codec() { return CODEC; } }
/*     */   public static final class Fraction extends Record implements LevelBasedValue { private final LevelBasedValue numerator;
/*     */     private final LevelBasedValue denominator;
/*     */     
/* 127 */     public Fraction(LevelBasedValue numerator, LevelBasedValue denominator) { this.numerator = numerator; this.denominator = denominator; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction;
/* 127 */       //   0	8	1	o	Ljava/lang/Object; } public LevelBasedValue numerator() { return this.numerator; } public LevelBasedValue denominator() { return this.denominator; }
/* 128 */     public static final MapCodec<Fraction> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LevelBasedValue.CODEC
/* 129 */           .fieldOf("numerator").forGetter(Fraction::numerator), LevelBasedValue.CODEC
/* 130 */           .fieldOf("denominator").forGetter(Fraction::denominator))
/* 131 */         .apply(i, Fraction::new));
/*     */ 
/*     */     
/*     */     public float calculate(int level) {
/* 135 */       float denominator = this.denominator.calculate(level);
/* 136 */       if (denominator == 0.0F) {
/* 137 */         return 0.0F;
/*     */       }
/* 139 */       return this.numerator.calculate(level) / denominator;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 144 */     public MapCodec<Fraction> codec() { return CODEC; } }
/*     */   public static final class Exponent extends Record implements LevelBasedValue { private final LevelBasedValue base;
/*     */     private final LevelBasedValue power;
/*     */     
/* 148 */     public Exponent(LevelBasedValue base, LevelBasedValue power) { this.base = base; this.power = power; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent;
/* 148 */       //   0	8	1	o	Ljava/lang/Object; } public LevelBasedValue base() { return this.base; } public LevelBasedValue power() { return this.power; }
/* 149 */     public static final MapCodec<Exponent> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LevelBasedValue.CODEC
/* 150 */           .fieldOf("base").forGetter(Exponent::base), LevelBasedValue.CODEC
/* 151 */           .fieldOf("power").forGetter(Exponent::power))
/* 152 */         .apply(i, Exponent::new));
/*     */ 
/*     */ 
/*     */     
/* 156 */     public float calculate(int level) { return (float)Math.pow(this.base.calculate(level), this.power.calculate(level)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     public MapCodec<Exponent> codec() { return CODEC; } }
/*     */   
/*     */   public static final class LevelsSquared extends Record implements LevelBasedValue {
/*     */     private final float added;
/*     */     
/* 166 */     public LevelsSquared(float added) { this.added = added; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared;
/* 166 */       //   0	8	1	o	Ljava/lang/Object; } public float added() { return this.added; }
/* 167 */     public static final MapCodec<LevelsSquared> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/* 168 */           .fieldOf("added").forGetter(LevelsSquared::added))
/* 169 */         .apply(i, LevelsSquared::new));
/*     */ 
/*     */ 
/*     */     
/* 173 */     public float calculate(int level) { return Mth.square(level) + this.added; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     public MapCodec<LevelsSquared> codec() { return CODEC; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\LevelBasedValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
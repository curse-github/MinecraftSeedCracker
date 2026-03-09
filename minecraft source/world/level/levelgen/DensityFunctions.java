/*      */ package net.minecraft.world.level.levelgen;
/*      */ import com.mojang.datafixers.kinds.App;
/*      */ import com.mojang.datafixers.util.Either;
/*      */ import com.mojang.datafixers.util.Function3;
/*      */ import com.mojang.datafixers.util.Function4;
/*      */ import com.mojang.datafixers.util.Function5;
/*      */ import com.mojang.datafixers.util.Function6;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*      */ import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
/*      */ import java.util.Arrays;
/*      */ import java.util.Optional;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Function;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.util.BoundedFloatFunction;
/*      */ import net.minecraft.util.CubicSpline;
/*      */ import net.minecraft.util.ExtraCodecs;
/*      */ import net.minecraft.util.KeyDispatchDataCodec;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.StringRepresentable;
/*      */ import net.minecraft.util.VisibleForDebug;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*      */ import net.minecraft.world.level.levelgen.synth.SimplexNoise;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public final class DensityFunctions {
/*   33 */   private static final Codec<DensityFunction> CODEC = BuiltInRegistries.DENSITY_FUNCTION_TYPE.byNameCodec().dispatch(function -> function.codec().codec(), Function.identity());
/*      */   
/*      */   protected static final double MAX_REASONABLE_NOISE_VALUE = 1000000.0D;
/*      */   
/*   37 */   private static final Codec<Double> NOISE_VALUE_CODEC = Codec.doubleRange(-1000000.0D, 1000000.0D);
/*      */   
/*   39 */   public static final Codec<DensityFunction> DIRECT_CODEC = Codec.either(NOISE_VALUE_CODEC, CODEC)
/*      */ 
/*      */     
/*   42 */     .xmap(either -> (DensityFunction)either.map(DensityFunctions::constant, Function.identity()), function -> {
/*   43 */         if (function instanceof Constant) { Constant constant = (Constant)function;
/*   44 */           return Either.left(Double.valueOf(constant.value())); }
/*      */         
/*   46 */         return Either.right(function);
/*      */       });
/*      */   
/*      */   public static MapCodec<? extends DensityFunction> bootstrap(Registry<MapCodec<? extends DensityFunction>> registry) {
/*   50 */     register(registry, "blend_alpha", BlendAlpha.CODEC);
/*   51 */     register(registry, "blend_offset", BlendOffset.CODEC);
/*   52 */     register(registry, "beardifier", BeardifierMarker.CODEC);
/*   53 */     register(registry, "old_blended_noise", BlendedNoise.CODEC);
/*   54 */     for (Marker.Type value : Marker.Type.values()) {
/*   55 */       register(registry, value.getSerializedName(), value.codec);
/*      */     }
/*   57 */     register(registry, "noise", Noise.CODEC);
/*   58 */     register(registry, "end_islands", EndIslandDensityFunction.CODEC);
/*   59 */     register(registry, "weird_scaled_sampler", WeirdScaledSampler.CODEC);
/*   60 */     register(registry, "shifted_noise", ShiftedNoise.CODEC);
/*   61 */     register(registry, "range_choice", RangeChoice.CODEC);
/*   62 */     register(registry, "shift_a", ShiftA.CODEC);
/*   63 */     register(registry, "shift_b", ShiftB.CODEC);
/*   64 */     register(registry, "shift", Shift.CODEC);
/*   65 */     register(registry, "blend_density", BlendDensity.CODEC);
/*   66 */     register(registry, "clamp", Clamp.CODEC);
/*   67 */     for (Mapped.Type value : Mapped.Type.values()) {
/*   68 */       register(registry, value.getSerializedName(), value.codec);
/*      */     }
/*   70 */     for (TwoArgumentSimpleFunction.Type value : TwoArgumentSimpleFunction.Type.values()) {
/*   71 */       register(registry, value.getSerializedName(), value.codec);
/*      */     }
/*   73 */     register(registry, "spline", Spline.CODEC);
/*   74 */     register(registry, "constant", Constant.CODEC);
/*   75 */     register(registry, "y_clamped_gradient", YClampedGradient.CODEC);
/*   76 */     return register(registry, "find_top_surface", FindTopSurface.CODEC);
/*      */   }
/*      */ 
/*      */   
/*   80 */   private static MapCodec<? extends DensityFunction> register(Registry<MapCodec<? extends DensityFunction>> registry, String name, KeyDispatchDataCodec<? extends DensityFunction> codec) { return (MapCodec)Registry.register(registry, name, codec.codec()); }
/*      */ 
/*      */ 
/*      */   
/*   84 */   private static <A, O> KeyDispatchDataCodec<O> singleArgumentCodec(Codec<A> argumentCodec, Function<A, O> constructor, Function<O, A> getter) { return KeyDispatchDataCodec.of(argumentCodec.fieldOf("argument").xmap(constructor, getter)); }
/*      */ 
/*      */ 
/*      */   
/*   88 */   private static <O> KeyDispatchDataCodec<O> singleFunctionArgumentCodec(Function<DensityFunction, O> constructor, Function<O, DensityFunction> getter) { return singleArgumentCodec(DensityFunction.HOLDER_HELPER_CODEC, constructor, getter); }
/*      */ 
/*      */ 
/*      */   
/*   92 */   private static <O> KeyDispatchDataCodec<O> doubleFunctionArgumentCodec(BiFunction<DensityFunction, DensityFunction, O> constructor, Function<O, DensityFunction> firstArgumentGetter, Function<O, DensityFunction> secondArgumentGetter) { return KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i.group(DensityFunction.HOLDER_HELPER_CODEC
/*   93 */             .fieldOf("argument1").forGetter(firstArgumentGetter), DensityFunction.HOLDER_HELPER_CODEC
/*   94 */             .fieldOf("argument2").forGetter(secondArgumentGetter))
/*   95 */           .apply(i, constructor))); }
/*      */ 
/*      */ 
/*      */   
/*   99 */   private static <O> KeyDispatchDataCodec<O> makeCodec(MapCodec<O> dataCodec) { return KeyDispatchDataCodec.of(dataCodec); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  106 */   public static DensityFunction interpolated(DensityFunction function) { return new Marker(Marker.Type.Interpolated, function); }
/*      */ 
/*      */ 
/*      */   
/*  110 */   public static DensityFunction flatCache(DensityFunction function) { return new Marker(Marker.Type.FlatCache, function); }
/*      */ 
/*      */ 
/*      */   
/*  114 */   public static DensityFunction cache2d(DensityFunction function) { return new Marker(Marker.Type.Cache2D, function); }
/*      */ 
/*      */ 
/*      */   
/*  118 */   public static DensityFunction cacheOnce(DensityFunction function) { return new Marker(Marker.Type.CacheOnce, function); }
/*      */ 
/*      */ 
/*      */   
/*  122 */   public static DensityFunction cacheAllInCell(DensityFunction function) { return new Marker(Marker.Type.CacheAllInCell, function); }
/*      */ 
/*      */ 
/*      */   
/*  126 */   public static DensityFunction mappedNoise(Holder<NormalNoise.NoiseParameters> noiseData, @Deprecated double xzScale, double yScale, double minTarget, double maxTarget) { return mapFromUnitTo(new Noise(new DensityFunction.NoiseHolder(noiseData), xzScale, yScale), minTarget, maxTarget); }
/*      */ 
/*      */ 
/*      */   
/*  130 */   public static DensityFunction mappedNoise(Holder<NormalNoise.NoiseParameters> noiseData, double yScale, double minTarget, double maxTarget) { return mappedNoise(noiseData, 1.0D, yScale, minTarget, maxTarget); }
/*      */ 
/*      */ 
/*      */   
/*  134 */   public static DensityFunction mappedNoise(Holder<NormalNoise.NoiseParameters> noiseData, double minTarget, double maxTarget) { return mappedNoise(noiseData, 1.0D, 1.0D, minTarget, maxTarget); }
/*      */ 
/*      */ 
/*      */   
/*  138 */   public static DensityFunction shiftedNoise2d(DensityFunction shiftX, DensityFunction shiftZ, double xzScale, Holder<NormalNoise.NoiseParameters> noiseData) { return new ShiftedNoise(shiftX, zero(), shiftZ, xzScale, 0.0D, new DensityFunction.NoiseHolder(noiseData)); }
/*      */ 
/*      */ 
/*      */   
/*  142 */   public static DensityFunction noise(Holder<NormalNoise.NoiseParameters> noiseData) { return noise(noiseData, 1.0D, 1.0D); }
/*      */ 
/*      */ 
/*      */   
/*  146 */   public static DensityFunction noise(Holder<NormalNoise.NoiseParameters> noiseData, double xzScale, double yScale) { return new Noise(new DensityFunction.NoiseHolder(noiseData), xzScale, yScale); }
/*      */ 
/*      */ 
/*      */   
/*  150 */   public static DensityFunction noise(Holder<NormalNoise.NoiseParameters> noiseData, double yScale) { return noise(noiseData, 1.0D, yScale); }
/*      */ 
/*      */ 
/*      */   
/*  154 */   public static DensityFunction rangeChoice(DensityFunction input, double minInclusive, double maxExclusive, DensityFunction whenInRange, DensityFunction whenOutOfRange) { return new RangeChoice(input, minInclusive, maxExclusive, whenInRange, whenOutOfRange); }
/*      */ 
/*      */ 
/*      */   
/*  158 */   public static DensityFunction shiftA(Holder<NormalNoise.NoiseParameters> noiseData) { return new ShiftA(new DensityFunction.NoiseHolder(noiseData)); }
/*      */ 
/*      */ 
/*      */   
/*  162 */   public static DensityFunction shiftB(Holder<NormalNoise.NoiseParameters> noiseData) { return new ShiftB(new DensityFunction.NoiseHolder(noiseData)); }
/*      */ 
/*      */ 
/*      */   
/*  166 */   public static DensityFunction shift(Holder<NormalNoise.NoiseParameters> noiseData) { return new Shift(new DensityFunction.NoiseHolder(noiseData)); }
/*      */ 
/*      */ 
/*      */   
/*  170 */   public static DensityFunction blendDensity(DensityFunction input) { return new BlendDensity(input); }
/*      */ 
/*      */ 
/*      */   
/*  174 */   public static DensityFunction endIslands(long seed) { return new EndIslandDensityFunction(seed); }
/*      */ 
/*      */ 
/*      */   
/*  178 */   public static DensityFunction weirdScaledSampler(DensityFunction input, Holder<NormalNoise.NoiseParameters> noiseData, WeirdScaledSampler.RarityValueMapper rarityValueMapper) { return new WeirdScaledSampler(input, new DensityFunction.NoiseHolder(noiseData), rarityValueMapper); }
/*      */ 
/*      */ 
/*      */   
/*  182 */   public static DensityFunction add(DensityFunction f1, DensityFunction f2) { return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.ADD, f1, f2); }
/*      */ 
/*      */ 
/*      */   
/*  186 */   public static DensityFunction mul(DensityFunction f1, DensityFunction f2) { return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.MUL, f1, f2); }
/*      */ 
/*      */ 
/*      */   
/*  190 */   public static DensityFunction min(DensityFunction f1, DensityFunction f2) { return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.MIN, f1, f2); }
/*      */ 
/*      */ 
/*      */   
/*  194 */   public static DensityFunction max(DensityFunction f1, DensityFunction f2) { return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.MAX, f1, f2); }
/*      */ 
/*      */ 
/*      */   
/*  198 */   public static DensityFunction spline(CubicSpline<Spline.Point, Spline.Coordinate> spline) { return new Spline(spline); }
/*      */ 
/*      */ 
/*      */   
/*  202 */   public static DensityFunction zero() { return Constant.ZERO; }
/*      */ 
/*      */ 
/*      */   
/*  206 */   public static DensityFunction constant(double value) { return new Constant(value); }
/*      */ 
/*      */ 
/*      */   
/*  210 */   public static DensityFunction yClampedGradient(int fromY, int toY, double fromValue, double toValue) { return new YClampedGradient(fromY, toY, fromValue, toValue); }
/*      */ 
/*      */ 
/*      */   
/*  214 */   public static DensityFunction map(DensityFunction function, Mapped.Type type) { return Mapped.create(type, function); }
/*      */ 
/*      */   
/*      */   private static DensityFunction mapFromUnitTo(DensityFunction function, double min, double max) {
/*  218 */     double middle = (min + max) * 0.5D;
/*  219 */     double factor = (max - min) * 0.5D;
/*      */     
/*  221 */     return add(constant(middle), mul(constant(factor), function));
/*      */   }
/*      */ 
/*      */   
/*  225 */   public static DensityFunction blendAlpha() { return BlendAlpha.INSTANCE; }
/*      */ 
/*      */ 
/*      */   
/*  229 */   public static DensityFunction blendOffset() { return BlendOffset.INSTANCE; }
/*      */ 
/*      */   
/*      */   public static DensityFunction lerp(DensityFunction alpha, DensityFunction first, DensityFunction second) {
/*  233 */     if (first instanceof Constant) { Constant constant = (Constant)first;
/*  234 */       return lerp(alpha, constant.value, second); }
/*      */     
/*  236 */     DensityFunction alphaCached = cacheOnce(alpha);
/*  237 */     DensityFunction oneMinusAlpha = add(mul(alphaCached, constant(-1.0D)), constant(1.0D));
/*  238 */     return add(mul(first, oneMinusAlpha), mul(second, alphaCached));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  243 */   public static DensityFunction lerp(DensityFunction factor, double first, DensityFunction second) { return add(mul(factor, add(second, constant(-first))), constant(first)); }
/*      */ 
/*      */ 
/*      */   
/*  247 */   public static DensityFunction findTopSurface(DensityFunction density, DensityFunction upperBound, int lowerBound, int stepSize) { return new FindTopSurface(density, upperBound, lowerBound, stepSize); }
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface TransformerWithContext
/*      */     extends DensityFunction
/*      */   {
/*      */     DensityFunction input();
/*      */ 
/*      */     
/*  257 */     default double compute(DensityFunction.FunctionContext context) { return transform(context, input().compute(context)); }
/*      */ 
/*      */ 
/*      */     
/*      */     default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  262 */       input().fillArray(output, contextProvider);
/*  263 */       for (int i = 0; i < output.length; i++) {
/*  264 */         output[i] = transform(contextProvider.forIndex(i), output[i]);
/*      */       }
/*      */     }
/*      */     
/*      */     double transform(DensityFunction.FunctionContext param1FunctionContext, double param1Double);
/*      */   }
/*      */   
/*      */   private static interface PureTransformer
/*      */     extends DensityFunction
/*      */   {
/*      */     DensityFunction input();
/*      */     
/*  276 */     default double compute(DensityFunction.FunctionContext context) { return transform(input().compute(context)); }
/*      */ 
/*      */ 
/*      */     
/*      */     default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  281 */       input().fillArray(output, contextProvider);
/*  282 */       for (int i = 0; i < output.length; i++)
/*  283 */         output[i] = transform(output[i]); 
/*      */     }
/*      */     
/*      */     double transform(double param1Double);
/*      */   }
/*      */   
/*      */   protected enum BlendAlpha
/*      */     implements DensityFunction.SimpleFunction {
/*  291 */     INSTANCE; public static final KeyDispatchDataCodec<DensityFunction> CODEC; static  {
/*  292 */       CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*      */     }
/*      */ 
/*      */     
/*  296 */     public double compute(DensityFunction.FunctionContext context) { return 1.0D; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  301 */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { Arrays.fill(output, 1.0D); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  306 */     public double minValue() { return 1.0D; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  311 */     public double maxValue() { return 1.0D; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  316 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; }
/*      */   }
/*      */   
/*      */   protected enum BlendOffset
/*      */     implements DensityFunction.SimpleFunction {
/*  321 */     INSTANCE; public static final KeyDispatchDataCodec<DensityFunction> CODEC; static  {
/*  322 */       CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*      */     }
/*      */ 
/*      */     
/*  326 */     public double compute(DensityFunction.FunctionContext context) { return 0.0D; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  331 */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { Arrays.fill(output, 0.0D); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  336 */     public double minValue() { return 0.0D; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  341 */     public double maxValue() { return 0.0D; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  346 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; }
/*      */   }
/*      */   
/*      */   public static interface BeardifierOrMarker
/*      */     extends DensityFunction.SimpleFunction {
/*  351 */     public static final KeyDispatchDataCodec<DensityFunction> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(DensityFunctions.BeardifierMarker.INSTANCE));
/*      */ 
/*      */ 
/*      */     
/*  355 */     default KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; }
/*      */   }
/*      */   
/*      */   protected enum BeardifierMarker
/*      */     implements BeardifierOrMarker {
/*  360 */     INSTANCE;
/*      */ 
/*      */ 
/*      */     
/*  364 */     public double compute(DensityFunction.FunctionContext context) { return 0.0D; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  369 */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { Arrays.fill(output, 0.0D); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  374 */     public double minValue() { return 0.0D; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  379 */     public double maxValue() { return 0.0D; } }
/*      */   @VisibleForDebug
/*      */   public static final class HolderHolder extends Record implements DensityFunction { private final Holder<DensityFunction> function;
/*      */     public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #388	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder; }
/*      */     
/*      */     public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #388	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder; }
/*      */     
/*      */     public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #388	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;
/*      */       //   0	8	1	o	Ljava/lang/Object; }
/*      */     
/*  388 */     public Holder<DensityFunction> function() { return this.function; }
/*  389 */     public HolderHolder(Holder<DensityFunction> function) { this.function = function; }
/*      */ 
/*      */     
/*  392 */     public double compute(DensityFunction.FunctionContext context) { return ((DensityFunction)this.function.value()).compute(context); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  397 */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { ((DensityFunction)this.function.value()).fillArray(output, contextProvider); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  403 */     public DensityFunction mapAll(DensityFunction.Visitor visitor) { return visitor.apply(new HolderHolder(new Holder.Direct(((DensityFunction)this.function.value()).mapAll(visitor)))); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  408 */     public double minValue() { return this.function.isBound() ? ((DensityFunction)this.function.value()).minValue() : Double.NEGATIVE_INFINITY; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  413 */     public double maxValue() { return this.function.isBound() ? ((DensityFunction)this.function.value()).maxValue() : Double.POSITIVE_INFINITY; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  419 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { throw new UnsupportedOperationException("Calling .codec() on HolderHolder"); } }
/*      */ 
/*      */ 
/*      */   
/*      */   public static interface MarkerOrMarked
/*      */     extends DensityFunction
/*      */   {
/*      */     DensityFunctions.Marker.Type type();
/*      */     
/*      */     DensityFunction wrapped();
/*      */     
/*  430 */     default KeyDispatchDataCodec<? extends DensityFunction> codec() { return (type()).codec; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  435 */     default DensityFunction mapAll(DensityFunction.Visitor visitor) { return visitor.apply(new DensityFunctions.Marker(type(), wrapped().mapAll(visitor))); } }
/*      */   protected static final class Marker extends Record implements MarkerOrMarked { private final Type type;
/*      */     private final DensityFunction wrapped;
/*      */     
/*  439 */     protected Marker(Type type, DensityFunction wrapped) { this.type = type; this.wrapped = wrapped; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #439	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #439	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #439	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;
/*  439 */       //   0	8	1	o	Ljava/lang/Object; } public Type type() { return this.type; } public DensityFunction wrapped() { return this.wrapped; }
/*      */     
/*  441 */     enum Type implements StringRepresentable { Interpolated("interpolated"),
/*  442 */       FlatCache("flat_cache"),
/*  443 */       Cache2D("cache_2d"),
/*  444 */       CacheOnce("cache_once"),
/*  445 */       CacheAllInCell("cache_all_in_cell");
/*      */       private final String name;
/*      */       private final KeyDispatchDataCodec<DensityFunctions.MarkerOrMarked> codec;
/*      */       
/*      */       Type(String name) {
/*  450 */         this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> new DensityFunctions.Marker(this, input), DensityFunctions.MarkerOrMarked::wrapped);
/*      */ 
/*      */         
/*  453 */         this.name = name;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  458 */       public String getSerializedName() { return this.name; } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  464 */     public double compute(DensityFunction.FunctionContext context) { return this.wrapped.compute(context); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  469 */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { this.wrapped.fillArray(output, contextProvider); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  474 */     public double minValue() { return this.wrapped.minValue(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  479 */     public double maxValue() { return this.wrapped.maxValue(); } }
/*      */    enum Type implements StringRepresentable { Interpolated("interpolated"), FlatCache("flat_cache"), Cache2D("cache_2d"), CacheOnce("cache_once"), CacheAllInCell("cache_all_in_cell"); private final String name; private final KeyDispatchDataCodec<DensityFunctions.MarkerOrMarked> codec; Type(String name) { this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> new DensityFunctions.Marker(this, input), DensityFunctions.MarkerOrMarked::wrapped);
/*      */       this.name = name; } public String getSerializedName() { return this.name; } } protected static final class Noise extends Record implements DensityFunction { private final DensityFunction.NoiseHolder noise; @Deprecated
/*      */     private final double xzScale; private final double yScale;
/*  483 */     protected Noise(DensityFunction.NoiseHolder noise, @Deprecated double xzScale, double yScale) { this.noise = noise; this.xzScale = xzScale; this.yScale = yScale; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #483	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #483	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #483	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;
/*  483 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction.NoiseHolder noise() { return this.noise; } @Deprecated public double xzScale() { return this.xzScale; } public double yScale() { return this.yScale; }
/*  484 */     public static final MapCodec<Noise> DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(DensityFunction.NoiseHolder.CODEC
/*  485 */           .fieldOf("noise").forGetter(Noise::noise), Codec.DOUBLE
/*  486 */           .fieldOf("xz_scale").forGetter(Noise::xzScale), Codec.DOUBLE
/*  487 */           .fieldOf("y_scale").forGetter(Noise::yScale))
/*  488 */         .apply(i, Noise::new));
/*      */     
/*  490 */     public static final KeyDispatchDataCodec<Noise> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */ 
/*      */     
/*  494 */     public double compute(DensityFunction.FunctionContext context) { return this.noise.getValue(context.blockX() * this.xzScale, context.blockY() * this.yScale, context.blockZ() * this.xzScale); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  499 */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { contextProvider.fillAllDirectly(output, this); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  504 */     public DensityFunction mapAll(DensityFunction.Visitor visitor) { return visitor.apply(new Noise(visitor.visitNoise(this.noise), this.xzScale, this.yScale)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  509 */     public double minValue() { return -maxValue(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  514 */     public double maxValue() { return this.noise.maxValue(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  519 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
/*      */ 
/*      */   
/*      */   protected static final class EndIslandDensityFunction
/*      */     implements DensityFunction.SimpleFunction
/*      */   {
/*  525 */     public static final KeyDispatchDataCodec<EndIslandDensityFunction> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(new EndIslandDensityFunction(0L)));
/*      */     
/*      */     private static final float ISLAND_THRESHOLD = -0.9F;
/*      */     private final SimplexNoise islandNoise;
/*      */     
/*      */     public EndIslandDensityFunction(long seed) {
/*  531 */       RandomSource islandRandom = new LegacyRandomSource(seed);
/*      */       
/*  533 */       islandRandom.consumeCount(17292);
/*  534 */       this.islandNoise = new SimplexNoise(islandRandom);
/*      */     }
/*      */     
/*      */     private static float getHeightValue(SimplexNoise islandNoise, int sectionX, int sectionZ) {
/*  538 */       int chunkX = sectionX / 2;
/*  539 */       int chunkZ = sectionZ / 2;
/*  540 */       int subSectionX = sectionX % 2;
/*  541 */       int subSectionZ = sectionZ % 2;
/*      */ 
/*      */       
/*  544 */       float doffs = 100.0F - Mth.sqrt((sectionX * sectionX + sectionZ * sectionZ)) * 8.0F;
/*  545 */       doffs = Mth.clamp(doffs, -100.0F, 80.0F);
/*      */ 
/*      */       
/*  548 */       for (int xo = -12; xo <= 12; xo++) {
/*  549 */         for (int zo = -12; zo <= 12; zo++) {
/*  550 */           long totalChunkX = (chunkX + xo);
/*  551 */           long totalChunkZ = (chunkZ + zo);
/*  552 */           if (totalChunkX * totalChunkX + totalChunkZ * totalChunkZ > 4096L && islandNoise.getValue(totalChunkX, totalChunkZ) < -0.8999999761581421D) {
/*  553 */             float islandSize = (Mth.abs((float)totalChunkX) * 3439.0F + Mth.abs((float)totalChunkZ) * 147.0F) % 13.0F + 9.0F;
/*  554 */             float xd = (subSectionX - xo * 2);
/*  555 */             float zd = (subSectionZ - zo * 2);
/*  556 */             float newDoffs = 100.0F - Mth.sqrt(xd * xd + zd * zd) * islandSize;
/*  557 */             newDoffs = Mth.clamp(newDoffs, -100.0F, 80.0F);
/*  558 */             doffs = Math.max(doffs, newDoffs);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  563 */       return doffs;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  568 */     public double compute(DensityFunction.FunctionContext context) { return (getHeightValue(this.islandNoise, context.blockX() / 8, context.blockZ() / 8) - 8.0D) / 128.0D; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  573 */     public double minValue() { return -0.84375D; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  578 */     public double maxValue() { return 0.5625D; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  583 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
/*      */   protected static final class WeirdScaledSampler extends Record implements TransformerWithContext { private final DensityFunction input; private final DensityFunction.NoiseHolder noise;
/*      */     private final RarityValueMapper rarityValueMapper;
/*      */     
/*  587 */     protected WeirdScaledSampler(DensityFunction input, DensityFunction.NoiseHolder noise, RarityValueMapper rarityValueMapper) { this.input = input; this.noise = noise; this.rarityValueMapper = rarityValueMapper; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$WeirdScaledSampler;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #587	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$WeirdScaledSampler; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$WeirdScaledSampler;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #587	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$WeirdScaledSampler; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$WeirdScaledSampler;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #587	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$WeirdScaledSampler;
/*  587 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction input() { return this.input; } public DensityFunction.NoiseHolder noise() { return this.noise; } public RarityValueMapper rarityValueMapper() { return this.rarityValueMapper; }
/*  588 */     private static final MapCodec<WeirdScaledSampler> DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(DensityFunction.HOLDER_HELPER_CODEC
/*  589 */           .fieldOf("input").forGetter(WeirdScaledSampler::input), DensityFunction.NoiseHolder.CODEC
/*  590 */           .fieldOf("noise").forGetter(WeirdScaledSampler::noise), RarityValueMapper.CODEC
/*  591 */           .fieldOf("rarity_value_mapper").forGetter(WeirdScaledSampler::rarityValueMapper))
/*  592 */         .apply(i, WeirdScaledSampler::new));
/*      */     
/*  594 */     public static final KeyDispatchDataCodec<WeirdScaledSampler> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */     
/*      */     public enum RarityValueMapper
/*      */       implements StringRepresentable
/*      */     {
/*  600 */       TYPE1("type_1", NoiseRouterData.QuantizedSpaghettiRarity::getSpaghettiRarity3D, 2.0D),
/*  601 */       TYPE2("type_2", NoiseRouterData.QuantizedSpaghettiRarity::getSphaghettiRarity2D, 3.0D); public static final Codec<RarityValueMapper> CODEC; private final String name; private final Double2DoubleFunction mapper; private final double maxRarity;
/*      */       
/*      */       static  {
/*  604 */         CODEC = StringRepresentable.fromEnum(RarityValueMapper::values);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       RarityValueMapper(String name, Double2DoubleFunction mapper, double maxRarity) {
/*  611 */         this.name = name;
/*  612 */         this.mapper = mapper;
/*  613 */         this.maxRarity = maxRarity;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  618 */       public String getSerializedName() { return this.name; }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public double transform(DensityFunction.FunctionContext context, double input) {
/*  624 */       double rarity = this.rarityValueMapper.mapper.get(input);
/*  625 */       return rarity * Math.abs(this.noise.getValue(context
/*  626 */             .blockX() / rarity, context
/*  627 */             .blockY() / rarity, context
/*  628 */             .blockZ() / rarity));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  634 */     public DensityFunction mapAll(DensityFunction.Visitor visitor) { return visitor.apply(new WeirdScaledSampler(this.input.mapAll(visitor), visitor.visitNoise(this.noise), this.rarityValueMapper)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  639 */     public double minValue() { return 0.0D; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  644 */     public double maxValue() { return this.rarityValueMapper.maxRarity * this.noise.maxValue(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  649 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
/*      */    public enum RarityValueMapper implements StringRepresentable { TYPE1("type_1", NoiseRouterData.QuantizedSpaghettiRarity::getSpaghettiRarity3D, 2.0D), TYPE2("type_2", NoiseRouterData.QuantizedSpaghettiRarity::getSphaghettiRarity2D, 3.0D); public static final Codec<RarityValueMapper> CODEC; private final String name; private final Double2DoubleFunction mapper; private final double maxRarity; static  { CODEC = StringRepresentable.fromEnum(RarityValueMapper::values); } RarityValueMapper(String name, Double2DoubleFunction mapper, double maxRarity) { this.name = name; this.mapper = mapper;
/*      */       this.maxRarity = maxRarity; } public String getSerializedName() { return this.name; } }
/*      */   protected static final class ShiftedNoise extends Record implements DensityFunction { private final DensityFunction shiftX; private final DensityFunction shiftY; private final DensityFunction shiftZ; private final double xzScale; private final double yScale; private final DensityFunction.NoiseHolder noise;
/*  653 */     protected ShiftedNoise(DensityFunction shiftX, DensityFunction shiftY, DensityFunction shiftZ, double xzScale, double yScale, DensityFunction.NoiseHolder noise) { this.shiftX = shiftX; this.shiftY = shiftY; this.shiftZ = shiftZ; this.xzScale = xzScale; this.yScale = yScale; this.noise = noise; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #653	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #653	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #653	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;
/*  653 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction shiftX() { return this.shiftX; } public DensityFunction shiftY() { return this.shiftY; } public DensityFunction shiftZ() { return this.shiftZ; } public double xzScale() { return this.xzScale; } public double yScale() { return this.yScale; } public DensityFunction.NoiseHolder noise() { return this.noise; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  661 */     private static final MapCodec<ShiftedNoise> DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(DensityFunction.HOLDER_HELPER_CODEC
/*  662 */           .fieldOf("shift_x").forGetter(ShiftedNoise::shiftX), DensityFunction.HOLDER_HELPER_CODEC
/*  663 */           .fieldOf("shift_y").forGetter(ShiftedNoise::shiftY), DensityFunction.HOLDER_HELPER_CODEC
/*  664 */           .fieldOf("shift_z").forGetter(ShiftedNoise::shiftZ), Codec.DOUBLE
/*  665 */           .fieldOf("xz_scale").forGetter(ShiftedNoise::xzScale), Codec.DOUBLE
/*  666 */           .fieldOf("y_scale").forGetter(ShiftedNoise::yScale), DensityFunction.NoiseHolder.CODEC
/*  667 */           .fieldOf("noise").forGetter(ShiftedNoise::noise))
/*  668 */         .apply(i, ShiftedNoise::new));
/*      */     
/*  670 */     public static final KeyDispatchDataCodec<ShiftedNoise> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  674 */       double x = context.blockX() * this.xzScale + this.shiftX.compute(context);
/*  675 */       double y = context.blockY() * this.yScale + this.shiftY.compute(context);
/*  676 */       double z = context.blockZ() * this.xzScale + this.shiftZ.compute(context);
/*  677 */       return this.noise.getValue(x, y, z);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  682 */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { contextProvider.fillAllDirectly(output, this); }
/*      */ 
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/*  687 */       return visitor.apply(new ShiftedNoise(this.shiftX
/*  688 */             .mapAll(visitor), this.shiftY
/*  689 */             .mapAll(visitor), this.shiftZ
/*  690 */             .mapAll(visitor), this.xzScale, this.yScale, visitor
/*      */ 
/*      */             
/*  693 */             .visitNoise(this.noise)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  699 */     public double minValue() { return -maxValue(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  704 */     public double maxValue() { return this.noise.maxValue(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  709 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
/*      */   private static final class RangeChoice extends Record implements DensityFunction { private final DensityFunction input; private final double minInclusive; private final double maxExclusive; private final DensityFunction whenInRange;
/*      */     private final DensityFunction whenOutOfRange;
/*      */     
/*  713 */     private RangeChoice(DensityFunction input, double minInclusive, double maxExclusive, DensityFunction whenInRange, DensityFunction whenOutOfRange) { this.input = input; this.minInclusive = minInclusive; this.maxExclusive = maxExclusive; this.whenInRange = whenInRange; this.whenOutOfRange = whenOutOfRange; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #713	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #713	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #713	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;
/*  713 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction input() { return this.input; } public double minInclusive() { return this.minInclusive; } public double maxExclusive() { return this.maxExclusive; } public DensityFunction whenInRange() { return this.whenInRange; } public DensityFunction whenOutOfRange() { return this.whenOutOfRange; }
/*  714 */     public static final MapCodec<RangeChoice> DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(DensityFunction.HOLDER_HELPER_CODEC
/*  715 */           .fieldOf("input").forGetter(RangeChoice::input), DensityFunctions.NOISE_VALUE_CODEC
/*  716 */           .fieldOf("min_inclusive").forGetter(RangeChoice::minInclusive), DensityFunctions.NOISE_VALUE_CODEC
/*  717 */           .fieldOf("max_exclusive").forGetter(RangeChoice::maxExclusive), DensityFunction.HOLDER_HELPER_CODEC
/*  718 */           .fieldOf("when_in_range").forGetter(RangeChoice::whenInRange), DensityFunction.HOLDER_HELPER_CODEC
/*  719 */           .fieldOf("when_out_of_range").forGetter(RangeChoice::whenOutOfRange))
/*  720 */         .apply(i, RangeChoice::new));
/*      */     
/*  722 */     public static final KeyDispatchDataCodec<RangeChoice> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  726 */       double inputValue = this.input.compute(context);
/*  727 */       if (inputValue >= this.minInclusive && inputValue < this.maxExclusive) {
/*  728 */         return this.whenInRange.compute(context);
/*      */       }
/*  730 */       return this.whenOutOfRange.compute(context);
/*      */     }
/*      */ 
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  735 */       this.input.fillArray(output, contextProvider);
/*  736 */       for (int i = 0; i < output.length; i++) {
/*  737 */         double v = output[i];
/*  738 */         if (v >= this.minInclusive && v < this.maxExclusive) {
/*  739 */           output[i] = this.whenInRange.compute(contextProvider.forIndex(i));
/*      */         } else {
/*  741 */           output[i] = this.whenOutOfRange.compute(contextProvider.forIndex(i));
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  748 */     public DensityFunction mapAll(DensityFunction.Visitor visitor) { return visitor.apply(new RangeChoice(this.input.mapAll(visitor), this.minInclusive, this.maxExclusive, this.whenInRange.mapAll(visitor), this.whenOutOfRange.mapAll(visitor))); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  753 */     public double minValue() { return Math.min(this.whenInRange.minValue(), this.whenOutOfRange.minValue()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  758 */     public double maxValue() { return Math.max(this.whenInRange.maxValue(), this.whenOutOfRange.maxValue()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  763 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
/*      */ 
/*      */ 
/*      */   
/*      */   static interface ShiftNoise
/*      */     extends DensityFunction
/*      */   {
/*      */     DensityFunction.NoiseHolder offsetNoise();
/*      */     
/*  772 */     default double minValue() { return -maxValue(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  777 */     default double maxValue() { return offsetNoise().maxValue() * 4.0D; }
/*      */ 
/*      */ 
/*      */     
/*  781 */     default double compute(double localX, double localY, double localZ) { return offsetNoise().getValue(localX * 0.25D, localY * 0.25D, localZ * 0.25D) * 4.0D; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  786 */     default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { contextProvider.fillAllDirectly(output, this); } }
/*      */   
/*      */   protected static final class ShiftA extends Record implements ShiftNoise { private final DensityFunction.NoiseHolder offsetNoise;
/*      */     
/*  790 */     protected ShiftA(DensityFunction.NoiseHolder offsetNoise) { this.offsetNoise = offsetNoise; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #790	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #790	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #790	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA;
/*  790 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction.NoiseHolder offsetNoise() { return this.offsetNoise; }
/*  791 */     private static final KeyDispatchDataCodec<ShiftA> CODEC = DensityFunctions.singleArgumentCodec(DensityFunction.NoiseHolder.CODEC, ShiftA::new, ShiftA::offsetNoise);
/*      */ 
/*      */ 
/*      */     
/*  795 */     public double compute(DensityFunction.FunctionContext context) { return compute(context.blockX(), 0.0D, context.blockZ()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  800 */     public DensityFunction mapAll(DensityFunction.Visitor visitor) { return visitor.apply(new ShiftA(visitor.visitNoise(this.offsetNoise))); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  805 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
/*      */   
/*      */   protected static final class ShiftB extends Record implements ShiftNoise { private final DensityFunction.NoiseHolder offsetNoise;
/*      */     
/*  809 */     protected ShiftB(DensityFunction.NoiseHolder offsetNoise) { this.offsetNoise = offsetNoise; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #809	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #809	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #809	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;
/*  809 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction.NoiseHolder offsetNoise() { return this.offsetNoise; }
/*  810 */     private static final KeyDispatchDataCodec<ShiftB> CODEC = DensityFunctions.singleArgumentCodec(DensityFunction.NoiseHolder.CODEC, ShiftB::new, ShiftB::offsetNoise);
/*      */ 
/*      */ 
/*      */     
/*  814 */     public double compute(DensityFunction.FunctionContext context) { return compute(context.blockZ(), context.blockX(), 0.0D); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  819 */     public DensityFunction mapAll(DensityFunction.Visitor visitor) { return visitor.apply(new ShiftB(visitor.visitNoise(this.offsetNoise))); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  824 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
/*      */   
/*      */   protected static final class Shift extends Record implements ShiftNoise { private final DensityFunction.NoiseHolder offsetNoise;
/*      */     
/*  828 */     protected Shift(DensityFunction.NoiseHolder offsetNoise) { this.offsetNoise = offsetNoise; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #828	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #828	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #828	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift;
/*  828 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction.NoiseHolder offsetNoise() { return this.offsetNoise; }
/*  829 */     private static final KeyDispatchDataCodec<Shift> CODEC = DensityFunctions.singleArgumentCodec(DensityFunction.NoiseHolder.CODEC, Shift::new, Shift::offsetNoise);
/*      */ 
/*      */ 
/*      */     
/*  833 */     public double compute(DensityFunction.FunctionContext context) { return compute(context.blockX(), context.blockY(), context.blockZ()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  838 */     public DensityFunction mapAll(DensityFunction.Visitor visitor) { return visitor.apply(new Shift(visitor.visitNoise(this.offsetNoise))); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  843 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
/*      */   
/*      */   private static final class BlendDensity extends Record implements TransformerWithContext { private final DensityFunction input;
/*      */     
/*  847 */     private BlendDensity(DensityFunction input) { this.input = input; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$BlendDensity;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #847	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$BlendDensity; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$BlendDensity;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #847	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$BlendDensity; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$BlendDensity;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #847	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$BlendDensity;
/*  847 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction input() { return this.input; }
/*  848 */     private static final KeyDispatchDataCodec<BlendDensity> CODEC = DensityFunctions.singleFunctionArgumentCodec(BlendDensity::new, BlendDensity::input);
/*      */ 
/*      */ 
/*      */     
/*  852 */     public double transform(DensityFunction.FunctionContext context, double input) { return context.getBlender().blendDensity(context, input); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  857 */     public DensityFunction mapAll(DensityFunction.Visitor visitor) { return visitor.apply(new BlendDensity(this.input.mapAll(visitor))); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  863 */     public double minValue() { return Double.NEGATIVE_INFINITY; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  869 */     public double maxValue() { return Double.POSITIVE_INFINITY; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  874 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
/*      */   protected static final class Clamp extends Record implements PureTransformer { private final DensityFunction input; private final double minValue;
/*      */     private final double maxValue;
/*      */     
/*  878 */     protected Clamp(DensityFunction input, double minValue, double maxValue) { this.input = input; this.minValue = minValue; this.maxValue = maxValue; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #878	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #878	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #878	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;
/*  878 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction input() { return this.input; } public double minValue() { return this.minValue; } public double maxValue() { return this.maxValue; }
/*  879 */     private static final MapCodec<Clamp> DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(DensityFunction.DIRECT_CODEC
/*  880 */           .fieldOf("input").forGetter(Clamp::input), DensityFunctions.NOISE_VALUE_CODEC
/*  881 */           .fieldOf("min").forGetter(Clamp::minValue), DensityFunctions.NOISE_VALUE_CODEC
/*  882 */           .fieldOf("max").forGetter(Clamp::maxValue))
/*  883 */         .apply(i, Clamp::new));
/*      */     
/*  885 */     public static final KeyDispatchDataCodec<Clamp> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */ 
/*      */     
/*  889 */     public double transform(double input) { return Mth.clamp(input, this.minValue, this.maxValue); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  894 */     public DensityFunction mapAll(DensityFunction.Visitor visitor) { return new Clamp(this.input.mapAll(visitor), this.minValue, this.maxValue); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  899 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
/*      */   protected static final class Mapped extends Record implements PureTransformer { private final Type type; private final DensityFunction input; private final double minValue;
/*      */     private final double maxValue;
/*      */     
/*  903 */     protected Mapped(Type type, DensityFunction input, double minValue, double maxValue) { this.type = type; this.input = input; this.minValue = minValue; this.maxValue = maxValue; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #903	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #903	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #903	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;
/*  903 */       //   0	8	1	o	Ljava/lang/Object; } public Type type() { return this.type; } public DensityFunction input() { return this.input; } public double minValue() { return this.minValue; } public double maxValue() { return this.maxValue; }
/*      */     public static Mapped create(Type type, DensityFunction input) {
/*  905 */       double minValue = input.minValue();
/*  906 */       double maxValue = input.maxValue();
/*  907 */       double minImage = transform(type, minValue);
/*  908 */       double maxImage = transform(type, maxValue);
/*  909 */       if (type == Type.INVERT) {
/*  910 */         if (minValue < 0.0D && maxValue > 0.0D) {
/*  911 */           return new Mapped(type, input, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
/*      */         }
/*      */         
/*  914 */         return new Mapped(type, input, maxImage, minImage);
/*      */       } 
/*  916 */       if (type == Type.ABS || type == Type.SQUARE)
/*      */       {
/*  918 */         return new Mapped(type, input, Math.max(0.0D, minValue), Math.max(minImage, maxImage));
/*      */       }
/*      */       
/*  921 */       return new Mapped(type, input, minImage, maxImage);
/*      */     }
/*      */     
/*      */     enum Type implements StringRepresentable {
/*  925 */       ABS("abs"),
/*  926 */       SQUARE("square"),
/*  927 */       CUBE("cube"),
/*  928 */       HALF_NEGATIVE("half_negative"),
/*  929 */       QUARTER_NEGATIVE("quarter_negative"),
/*  930 */       INVERT("invert"),
/*  931 */       SQUEEZE("squeeze");
/*      */       private final String name;
/*      */       private final KeyDispatchDataCodec<DensityFunctions.Mapped> codec;
/*      */       
/*      */       Type(String name) {
/*  936 */         this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> DensityFunctions.Mapped.create(this, input), DensityFunctions.Mapped::input);
/*      */ 
/*      */         
/*  939 */         this.name = name;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  944 */       public String getSerializedName() { return this.name; }
/*      */     }
/*      */ 
/*      */     
/*      */     private static double transform(Type type, double input) {
/*  949 */       switch (type.ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: case 3: return 
/*      */ 
/*      */ 
/*      */             
/*  953 */             (input > 0.0D) ? input : (input * 0.5D);
/*  954 */         case 4: return (input > 0.0D) ? input : (input * 0.25D);
/*      */         case 5:
/*      */         
/*      */         case 6:
/*  958 */           break; }  double c = Mth.clamp(input, -1.0D, 1.0D);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  966 */     public double transform(double input) { return transform(this.type, input); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  971 */     public Mapped mapAll(DensityFunction.Visitor visitor) { return create(this.type, this.input.mapAll(visitor)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  976 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return this.type.codec; } } enum Type implements StringRepresentable { ABS("abs"), SQUARE("square"), CUBE("cube"), HALF_NEGATIVE("half_negative"), QUARTER_NEGATIVE("quarter_negative"),
/*      */     INVERT("invert"),
/*      */     SQUEEZE("squeeze"); private final String name; private final KeyDispatchDataCodec<DensityFunctions.Mapped> codec; Type(String name) { this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> DensityFunctions.Mapped.create(this, input), DensityFunctions.Mapped::input);
/*      */       this.name = name; }
/*      */     public String getSerializedName() { return this.name; } }
/*  981 */   static interface TwoArgumentSimpleFunction extends DensityFunction { public static final Logger LOGGER = LogUtils.getLogger();
/*      */     
/*      */     static TwoArgumentSimpleFunction create(Type type, DensityFunction argument1, DensityFunction argument2) {
/*  984 */       double min1 = argument1.minValue();
/*  985 */       double min2 = argument2.minValue();
/*      */       
/*  987 */       double max1 = argument1.maxValue();
/*  988 */       double max2 = argument2.maxValue();
/*      */       
/*  990 */       if (type == Type.MIN || type == Type.MAX) {
/*  991 */         boolean firstAlwaysBiggerThanSecond = (min1 >= max2);
/*  992 */         boolean secondAlwaysBiggerThanFirst = (min2 >= max1);
/*  993 */         if (firstAlwaysBiggerThanSecond || secondAlwaysBiggerThanFirst) {
/*  994 */           LOGGER.warn("Creating a {} function between two non-overlapping inputs: {} and {}", new Object[] { type, argument1, argument2 });
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1004 */       switch (type.ordinal()) { default: throw new MatchException(null, null);
/*      */         case 0: 
/*      */         case 3: 
/*      */         case 2: 
/*      */         case 1:
/* 1009 */           if (min1 > 0.0D && min2 > 0.0D);
/*      */ 
/*      */           
/* 1012 */           if (max1 < 0.0D && max2 < 0.0D);
/*      */           break; }
/*      */       
/* 1015 */       double minValue = Math.min(min1 * max2, max1 * min2);
/*      */ 
/*      */ 
/*      */       
/* 1019 */       switch (type.ordinal()) { default: throw new MatchException(null, null);
/*      */         case 0: 
/*      */         case 3: 
/*      */         case 2: 
/*      */         case 1:
/* 1024 */           if (min1 > 0.0D && min2 > 0.0D);
/*      */ 
/*      */           
/* 1027 */           if (max1 < 0.0D && max2 < 0.0D);
/*      */           break; }
/*      */       
/* 1030 */       double maxValue = Math.max(min1 * min2, max1 * max2);
/*      */ 
/*      */ 
/*      */       
/* 1034 */       if (type == Type.MUL || type == Type.ADD) {
/* 1035 */         if (argument1 instanceof DensityFunctions.Constant) { DensityFunctions.Constant constant = (DensityFunctions.Constant)argument1;
/* 1036 */           return new DensityFunctions.MulOrAdd((type == Type.ADD) ? DensityFunctions.MulOrAdd.Type.ADD : DensityFunctions.MulOrAdd.Type.MUL, argument2, minValue, maxValue, constant.value); }
/*      */         
/* 1038 */         if (argument2 instanceof DensityFunctions.Constant) { DensityFunctions.Constant constant = (DensityFunctions.Constant)argument2;
/* 1039 */           return new DensityFunctions.MulOrAdd((type == Type.ADD) ? DensityFunctions.MulOrAdd.Type.ADD : DensityFunctions.MulOrAdd.Type.MUL, argument1, minValue, maxValue, constant.value); }
/*      */       
/*      */       } 
/*      */       
/* 1043 */       return new DensityFunctions.Ap2(type, argument1, argument2, minValue, maxValue);
/*      */     } Type type();
/*      */     DensityFunction argument1();
/*      */     DensityFunction argument2();
/* 1047 */     public enum Type implements StringRepresentable { ADD("add"),
/* 1048 */       MUL("mul"),
/* 1049 */       MIN("min"),
/* 1050 */       MAX("max"); private final KeyDispatchDataCodec<DensityFunctions.TwoArgumentSimpleFunction> codec; private final String name;
/*      */       
/*      */       Type(String name) {
/* 1053 */         this.codec = DensityFunctions.doubleFunctionArgumentCodec((argument1, argument2) -> 
/* 1054 */             DensityFunctions.TwoArgumentSimpleFunction.create(this, argument1, argument2), DensityFunctions.TwoArgumentSimpleFunction::argument1, DensityFunctions.TwoArgumentSimpleFunction::argument2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1062 */         this.name = name;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1067 */       public String getSerializedName() { return this.name; } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1079 */     default KeyDispatchDataCodec<? extends DensityFunction> codec() { return (type()).codec; } } public enum Type implements StringRepresentable { ADD("add"), MUL("mul"), MIN("min"),
/*      */     MAX("max"); private final KeyDispatchDataCodec<DensityFunctions.TwoArgumentSimpleFunction> codec; private final String name; Type(String name) { this.codec = DensityFunctions.doubleFunctionArgumentCodec((argument1, argument2) -> DensityFunctions.TwoArgumentSimpleFunction.create(this, argument1, argument2), DensityFunctions.TwoArgumentSimpleFunction::argument1, DensityFunctions.TwoArgumentSimpleFunction::argument2);
/*      */       this.name = name; } public String getSerializedName() { return this.name; } }
/*      */   private static final class MulOrAdd extends Record implements TwoArgumentSimpleFunction, PureTransformer { private final Type specificType; private final DensityFunction input; private final double minValue; private final double maxValue; private final double argument;
/* 1083 */     private MulOrAdd(Type specificType, DensityFunction input, double minValue, double maxValue, double argument) { this.specificType = specificType; this.input = input; this.minValue = minValue; this.maxValue = maxValue; this.argument = argument; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1083	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1083	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1083	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;
/* 1083 */       //   0	8	1	o	Ljava/lang/Object; } public Type specificType() { return this.specificType; } public DensityFunction input() { return this.input; } public double minValue() { return this.minValue; } public double maxValue() { return this.maxValue; } public double argument() { return this.argument; }
/*      */     
/* 1085 */     enum Type { MUL,
/* 1086 */       ADD; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1091 */     public DensityFunctions.TwoArgumentSimpleFunction.Type type() { return (this.specificType == Type.MUL) ? DensityFunctions.TwoArgumentSimpleFunction.Type.MUL : DensityFunctions.TwoArgumentSimpleFunction.Type.ADD; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1096 */     public DensityFunction argument1() { return DensityFunctions.constant(this.argument); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1101 */     public DensityFunction argument2() { return this.input; }
/*      */ 
/*      */ 
/*      */     
/*      */     public double transform(double input) {
/* 1106 */       switch (this.specificType.ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return 
/*      */         
/* 1108 */         input + this.argument;
/*      */     }
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/*      */       double maxValue, minValue;
/* 1114 */       DensityFunction function = this.input.mapAll(visitor);
/* 1115 */       double min = function.minValue();
/* 1116 */       double max = function.maxValue();
/*      */ 
/*      */       
/* 1119 */       if (this.specificType == Type.ADD) {
/* 1120 */         minValue = min + this.argument;
/* 1121 */         maxValue = max + this.argument;
/* 1122 */       } else if (this.argument >= 0.0D) {
/* 1123 */         minValue = min * this.argument;
/* 1124 */         maxValue = max * this.argument;
/*      */       } else {
/* 1126 */         minValue = max * this.argument;
/* 1127 */         maxValue = min * this.argument;
/*      */       } 
/* 1129 */       return new MulOrAdd(this.specificType, function, minValue, maxValue, this.argument);
/*      */     } }
/*      */   enum Type { MUL, ADD; }
/*      */   private static final class Ap2 extends Record implements TwoArgumentSimpleFunction { private final DensityFunctions.TwoArgumentSimpleFunction.Type type; private final DensityFunction argument1; private final DensityFunction argument2; private final double minValue; private final double maxValue;
/* 1133 */     private Ap2(DensityFunctions.TwoArgumentSimpleFunction.Type type, DensityFunction argument1, DensityFunction argument2, double minValue, double maxValue) { this.type = type; this.argument1 = argument1; this.argument2 = argument2; this.minValue = minValue; this.maxValue = maxValue; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1133	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1133	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1133	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;
/* 1133 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunctions.TwoArgumentSimpleFunction.Type type() { return this.type; } public DensityFunction argument1() { return this.argument1; } public DensityFunction argument2() { return this.argument2; }
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/* 1136 */       double v1 = this.argument1.compute(context);
/* 1137 */       switch (this.type.ordinal()) { default: throw new MatchException(null, null);case 0: case 1: return 
/*      */ 
/*      */             
/* 1140 */             (v1 == 0.0D) ? 
/* 1141 */             0.0D : (
/*      */             
/* 1143 */             v1 * this.argument2.compute(context));
/*      */         
/*      */         case 2:
/* 1146 */           return (v1 < this.argument2.minValue()) ? 
/* 1147 */             v1 : 
/*      */             
/* 1149 */             Math.min(v1, this.argument2.compute(context));
/*      */         case 3:
/*      */           break; }
/* 1152 */        return (v1 > this.argument2.maxValue()) ? 
/* 1153 */         v1 : 
/*      */         
/* 1155 */         Math.max(v1, this.argument2.compute(context));
/*      */     }
/*      */ 
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*      */       int i, i, i, i;
/*      */       double min, max, v2[];
/* 1162 */       this.argument1.fillArray(output, contextProvider);
/* 1163 */       switch (this.type.ordinal()) {
/*      */         case 0:
/* 1165 */           v2 = new double[output.length];
/* 1166 */           this.argument2.fillArray(v2, contextProvider);
/* 1167 */           for (i = 0; i < output.length; i++) {
/* 1168 */             output[i] = output[i] + v2[i];
/*      */           }
/*      */           break;
/*      */         case 1:
/* 1172 */           for (i = 0; i < output.length; i++) {
/* 1173 */             double v = output[i];
/* 1174 */             output[i] = (v == 0.0D) ? 0.0D : (v * this.argument2.compute(contextProvider.forIndex(i)));
/*      */           } 
/*      */           break;
/*      */         case 2:
/* 1178 */           min = this.argument2.minValue();
/* 1179 */           for (i = 0; i < output.length; i++) {
/* 1180 */             double v = output[i];
/* 1181 */             output[i] = (v < min) ? v : Math.min(v, this.argument2.compute(contextProvider.forIndex(i)));
/*      */           } 
/*      */           break;
/*      */         case 3:
/* 1185 */           max = this.argument2.maxValue();
/* 1186 */           for (i = 0; i < output.length; i++) {
/* 1187 */             double v = output[i];
/* 1188 */             output[i] = (v > max) ? v : Math.max(v, this.argument2.compute(contextProvider.forIndex(i)));
/*      */           } 
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1196 */     public DensityFunction mapAll(DensityFunction.Visitor visitor) { return visitor.apply(DensityFunctions.TwoArgumentSimpleFunction.create(this.type, this.argument1.mapAll(visitor), this.argument2.mapAll(visitor))); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1201 */     public double minValue() { return this.minValue; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1206 */     public double maxValue() { return this.maxValue; } }
/*      */   
/*      */   public static final class Spline extends Record implements DensityFunction { private final CubicSpline<Point, Coordinate> spline;
/*      */     
/* 1210 */     public Spline(CubicSpline<Point, Coordinate> spline) { this.spline = spline; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1210	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1210	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1210	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline;
/* 1210 */       //   0	8	1	o	Ljava/lang/Object; } public CubicSpline<Point, Coordinate> spline() { return this.spline; }
/* 1211 */     private static final Codec<CubicSpline<Point, Coordinate>> SPLINE_CODEC = CubicSpline.codec(Coordinate.CODEC);
/* 1212 */     private static final MapCodec<Spline> DATA_CODEC = SPLINE_CODEC.fieldOf("spline").xmap(Spline::new, Spline::spline);
/*      */     
/* 1214 */     public static final KeyDispatchDataCodec<Spline> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */ 
/*      */     
/* 1218 */     public double compute(DensityFunction.FunctionContext context) { return this.spline.apply(new Point(context)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1223 */     public double minValue() { return this.spline.minValue(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1228 */     public double maxValue() { return this.spline.maxValue(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1233 */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { contextProvider.fillAllDirectly(output, this); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1238 */     public DensityFunction mapAll(DensityFunction.Visitor visitor) { return visitor.apply(new Spline(this.spline.mapAll(c -> c.mapAll(visitor)))); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1243 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; }
/*      */     public static final class Coordinate extends Record implements BoundedFloatFunction<Point> { private final Holder<DensityFunction> function;
/*      */       
/* 1246 */       public Coordinate(Holder<DensityFunction> function) { this.function = function; } public final int hashCode() { // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;)I
/*      */         //   6: ireturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1246	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate; } public final boolean equals(Object o) { // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: aload_1
/*      */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;Ljava/lang/Object;)Z
/*      */         //   7: ireturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1246	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;
/* 1246 */         //   0	8	1	o	Ljava/lang/Object; } public Holder<DensityFunction> function() { return this.function; }
/* 1247 */       public static final Codec<Coordinate> CODEC = DensityFunction.CODEC.xmap(Coordinate::new, Coordinate::function);
/*      */ 
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1252 */         Optional<ResourceKey<DensityFunction>> key = this.function.unwrapKey();
/* 1253 */         if (key.isPresent()) {
/* 1254 */           ResourceKey<DensityFunction> name = (ResourceKey)key.get();
/* 1255 */           if (name == NoiseRouterData.CONTINENTS) {
/* 1256 */             return "continents";
/*      */           }
/* 1258 */           if (name == NoiseRouterData.EROSION) {
/* 1259 */             return "erosion";
/*      */           }
/* 1261 */           if (name == NoiseRouterData.RIDGES) {
/* 1262 */             return "weirdness";
/*      */           }
/* 1264 */           if (name == NoiseRouterData.RIDGES_FOLDED) {
/* 1265 */             return "ridges";
/*      */           }
/*      */         } 
/* 1268 */         return "Coordinate[" + String.valueOf(this.function) + "]";
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1273 */       public float apply(DensityFunctions.Spline.Point point) { return (float)((DensityFunction)this.function.value()).compute(point.context()); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1278 */       public float minValue() { return this.function.isBound() ? (float)((DensityFunction)this.function.value()).minValue() : Float.NEGATIVE_INFINITY; }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1283 */       public float maxValue() { return this.function.isBound() ? (float)((DensityFunction)this.function.value()).maxValue() : Float.POSITIVE_INFINITY; }
/*      */ 
/*      */ 
/*      */       
/* 1287 */       public Coordinate mapAll(DensityFunction.Visitor visitor) { return new Coordinate(new Holder.Direct(((DensityFunction)this.function.value()).mapAll(visitor))); } }
/*      */     
/*      */     public static final class Point extends Record { private final DensityFunction.FunctionContext context;
/*      */       
/* 1291 */       public Point(DensityFunction.FunctionContext context) { this.context = context; } public final String toString() { // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;)Ljava/lang/String;
/*      */         //   6: areturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1291	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point; } public final int hashCode() { // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;)I
/*      */         //   6: ireturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1291	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point; } public final boolean equals(Object o) { // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: aload_1
/*      */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;Ljava/lang/Object;)Z
/*      */         //   7: ireturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1291	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;
/* 1291 */         //   0	8	1	o	Ljava/lang/Object; } public DensityFunction.FunctionContext context() { return this.context; } } } public static final class Coordinate extends Record implements BoundedFloatFunction<Spline.Point> { private final Holder<DensityFunction> function; public Coordinate(Holder<DensityFunction> function) { this.function = function; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1246	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1246	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;
/* 1291 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<DensityFunction> function() { return this.function; } public static final Codec<Coordinate> CODEC = DensityFunction.CODEC.xmap(Coordinate::new, Coordinate::function); public String toString() { Optional<ResourceKey<DensityFunction>> key = this.function.unwrapKey(); if (key.isPresent()) { ResourceKey<DensityFunction> name = (ResourceKey)key.get(); if (name == NoiseRouterData.CONTINENTS) return "continents";  if (name == NoiseRouterData.EROSION) return "erosion";  if (name == NoiseRouterData.RIDGES) return "weirdness";  if (name == NoiseRouterData.RIDGES_FOLDED) return "ridges";  }  return "Coordinate[" + String.valueOf(this.function) + "]"; } public float apply(DensityFunctions.Spline.Point point) { return (float)((DensityFunction)this.function.value()).compute(point.context()); } public float minValue() { return this.function.isBound() ? (float)((DensityFunction)this.function.value()).minValue() : Float.NEGATIVE_INFINITY; } public float maxValue() { return this.function.isBound() ? (float)((DensityFunction)this.function.value()).maxValue() : Float.POSITIVE_INFINITY; } public Coordinate mapAll(DensityFunction.Visitor visitor) { return new Coordinate(new Holder.Direct(((DensityFunction)this.function.value()).mapAll(visitor))); } } public static final class Point extends Record { public Point(DensityFunction.FunctionContext context) { this.context = context; } private final DensityFunction.FunctionContext context; public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1291	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1291	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1291	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;
/* 1291 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction.FunctionContext context() { return this.context; } }
/*      */   private static final class Constant extends Record implements DensityFunction.SimpleFunction { private final double value;
/*      */     
/* 1294 */     private Constant(double value) { this.value = value; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1294	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1294	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1294	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;
/* 1294 */       //   0	8	1	o	Ljava/lang/Object; } public double value() { return this.value; }
/* 1295 */     private static final KeyDispatchDataCodec<Constant> CODEC = DensityFunctions.singleArgumentCodec(DensityFunctions.NOISE_VALUE_CODEC, Constant::new, Constant::value);
/* 1296 */     private static final Constant ZERO = new Constant(0.0D);
/*      */ 
/*      */ 
/*      */     
/* 1300 */     public double compute(DensityFunction.FunctionContext context) { return this.value; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1305 */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { Arrays.fill(output, this.value); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1310 */     public double minValue() { return this.value; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1315 */     public double maxValue() { return this.value; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1320 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
/*      */   private static final class YClampedGradient extends Record implements DensityFunction.SimpleFunction { private final int fromY; private final int toY; private final double fromValue;
/*      */     private final double toValue;
/*      */     
/* 1324 */     private YClampedGradient(int fromY, int toY, double fromValue, double toValue) { this.fromY = fromY; this.toY = toY; this.fromValue = fromValue; this.toValue = toValue; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1324	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1324	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1324	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;
/* 1324 */       //   0	8	1	o	Ljava/lang/Object; } public int fromY() { return this.fromY; } public int toY() { return this.toY; } public double fromValue() { return this.fromValue; } public double toValue() { return this.toValue; }
/* 1325 */     private static final MapCodec<YClampedGradient> DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 1326 */           Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("from_y").forGetter(YClampedGradient::fromY), 
/* 1327 */           Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("to_y").forGetter(YClampedGradient::toY), DensityFunctions.NOISE_VALUE_CODEC
/* 1328 */           .fieldOf("from_value").forGetter(YClampedGradient::fromValue), DensityFunctions.NOISE_VALUE_CODEC
/* 1329 */           .fieldOf("to_value").forGetter(YClampedGradient::toValue))
/* 1330 */         .apply(i, YClampedGradient::new));
/*      */     
/* 1332 */     public static final KeyDispatchDataCodec<YClampedGradient> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */ 
/*      */     
/* 1336 */     public double compute(DensityFunction.FunctionContext context) { return Mth.clampedMap(context.blockY(), this.fromY, this.toY, this.fromValue, this.toValue); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1341 */     public double minValue() { return Math.min(this.fromValue, this.toValue); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1346 */     public double maxValue() { return Math.max(this.fromValue, this.toValue); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1351 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
/*      */   private static final class FindTopSurface extends Record implements DensityFunction { private final DensityFunction density; private final DensityFunction upperBound; private final int lowerBound;
/*      */     private final int cellHeight;
/*      */     
/* 1355 */     private FindTopSurface(DensityFunction density, DensityFunction upperBound, int lowerBound, int cellHeight) { this.density = density; this.upperBound = upperBound; this.lowerBound = lowerBound; this.cellHeight = cellHeight; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1355	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1355	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1355	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;
/* 1355 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction density() { return this.density; } public DensityFunction upperBound() { return this.upperBound; } public int lowerBound() { return this.lowerBound; } public int cellHeight() { return this.cellHeight; }
/* 1356 */     private static final MapCodec<FindTopSurface> DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(DensityFunction.HOLDER_HELPER_CODEC
/* 1357 */           .fieldOf("density").forGetter(FindTopSurface::density), DensityFunction.HOLDER_HELPER_CODEC
/* 1358 */           .fieldOf("upper_bound").forGetter(FindTopSurface::upperBound), 
/* 1359 */           Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("lower_bound").forGetter(FindTopSurface::lowerBound), ExtraCodecs.POSITIVE_INT
/* 1360 */           .fieldOf("cell_height").forGetter(FindTopSurface::cellHeight))
/* 1361 */         .apply(i, FindTopSurface::new));
/*      */     
/* 1363 */     public static final KeyDispatchDataCodec<FindTopSurface> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/* 1367 */       int topY = Mth.floor(this.upperBound.compute(context) / this.cellHeight) * this.cellHeight;
/* 1368 */       if (topY <= this.lowerBound) {
/* 1369 */         return this.lowerBound;
/*      */       }
/*      */       
/* 1372 */       for (int blockY = topY; blockY >= this.lowerBound; blockY -= this.cellHeight) {
/* 1373 */         if (this.density.compute(new DensityFunction.SinglePointContext(context.blockX(), blockY, context.blockZ())) > 0.0D) {
/* 1374 */           return blockY;
/*      */         }
/*      */       } 
/* 1377 */       return this.lowerBound;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1382 */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { contextProvider.fillAllDirectly(output, this); }
/*      */ 
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/* 1387 */       return visitor.apply(new FindTopSurface(this.density
/* 1388 */             .mapAll(visitor), this.upperBound
/* 1389 */             .mapAll(visitor), this.lowerBound, this.cellHeight));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1397 */     public double minValue() { return this.lowerBound; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1402 */     public double maxValue() { return Math.max(this.lowerBound, this.upperBound.maxValue()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1407 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
/*      */ 
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\DensityFunctions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
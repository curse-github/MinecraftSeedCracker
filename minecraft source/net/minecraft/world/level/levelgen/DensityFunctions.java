package net.minecraft.world.level.levelgen;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;
import com.mojang.datafixers.util.Function6;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.BoundedFloatFunction;
import net.minecraft.util.CubicSpline;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import org.slf4j.Logger;

public final class DensityFunctions {
  private static final Codec<DensityFunction> CODEC = BuiltInRegistries.DENSITY_FUNCTION_TYPE.byNameCodec()
      .dispatch(function -> function.codec().codec(), Function.identity());
  protected static final double MAX_REASONABLE_NOISE_VALUE = 1000000.0D;
  private static final Codec<Double> NOISE_VALUE_CODEC = Codec.doubleRange(-1000000.0D, 1000000.0D);
  public static final Codec<DensityFunction> DIRECT_CODEC = Codec.either(NOISE_VALUE_CODEC, CODEC)
      .xmap(either -> (DensityFunction) either.map(DensityFunctions::constant, Function.identity()), function -> {
        if (function instanceof Constant) {
          Constant constant = (Constant) function;
          return Either.left(Double.valueOf(constant.value()));
        }
        return Either.right(function);
      });

  public static MapCodec<? extends DensityFunction> bootstrap(Registry<MapCodec<? extends DensityFunction>> registry) {
    register(registry, "blend_alpha", BlendAlpha.CODEC);
    register(registry, "blend_offset", BlendOffset.CODEC);
    register(registry, "beardifier", BeardifierMarker.CODEC);
    register(registry, "old_blended_noise", BlendedNoise.CODEC);
    for (Marker.Type value : Marker.Type.values()) {
      register(registry, value.getSerializedName(), value.codec);
    }
    register(registry, "noise", Noise.CODEC);
    register(registry, "end_islands", EndIslandDensityFunction.CODEC);
    register(registry, "weird_scaled_sampler", WeirdScaledSampler.CODEC);
    register(registry, "shifted_noise", ShiftedNoise.CODEC);
    register(registry, "range_choice", RangeChoice.CODEC);
    register(registry, "shift_a", ShiftA.CODEC);
    register(registry, "shift_b", ShiftB.CODEC);
    register(registry, "shift", Shift.CODEC);
    register(registry, "blend_density", BlendDensity.CODEC);
    register(registry, "clamp", Clamp.CODEC);
    for (Mapped.Type value : Mapped.Type.values()) {
      register(registry, value.getSerializedName(), value.codec);
    }
    for (TwoArgumentSimpleFunction.Type value : TwoArgumentSimpleFunction.Type.values()) {
      register(registry, value.getSerializedName(), value.codec);
    }
    register(registry, "spline", Spline.CODEC);
    register(registry, "constant", Constant.CODEC);
    register(registry, "y_clamped_gradient", YClampedGradient.CODEC);
    return register(registry, "find_top_surface", FindTopSurface.CODEC);
  }

  private static MapCodec<? extends DensityFunction> register(Registry<MapCodec<? extends DensityFunction>> registry,
      String name, KeyDispatchDataCodec<? extends DensityFunction> codec) {
    return (MapCodec) Registry.register(registry, name, codec.codec());
  }

  private static <A, O> KeyDispatchDataCodec<O> singleArgumentCodec(Codec<A> argumentCodec, Function<A, O> constructor,
      Function<O, A> getter) {
    return KeyDispatchDataCodec.of(argumentCodec.fieldOf("argument").xmap(constructor, getter));
  }

  private static <O> KeyDispatchDataCodec<O> singleFunctionArgumentCodec(Function<DensityFunction, O> constructor,
      Function<O, DensityFunction> getter) {
    return singleArgumentCodec(DensityFunction.HOLDER_HELPER_CODEC, constructor, getter);
  }

  private static <O> KeyDispatchDataCodec<O> doubleFunctionArgumentCodec(
      BiFunction<DensityFunction, DensityFunction, O> constructor, Function<O, DensityFunction> firstArgumentGetter,
      Function<O, DensityFunction> secondArgumentGetter) {
    return KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i
        .group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument1").forGetter(firstArgumentGetter),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument2").forGetter(secondArgumentGetter))
        .apply(i, constructor)));
  }

  private static <O> KeyDispatchDataCodec<O> makeCodec(MapCodec<O> dataCodec) {
    return KeyDispatchDataCodec.of(dataCodec);
  }

  public static DensityFunction interpolated(DensityFunction function) {
    return new Marker(Marker.Type.Interpolated, function);
  }

  public static DensityFunction flatCache(DensityFunction function) {
    return new Marker(Marker.Type.FlatCache, function);
  }

  public static DensityFunction cache2d(DensityFunction function) {
    return new Marker(Marker.Type.Cache2D, function);
  }

  public static DensityFunction cacheOnce(DensityFunction function) {
    return new Marker(Marker.Type.CacheOnce, function);
  }

  public static DensityFunction cacheAllInCell(DensityFunction function) {
    return new Marker(Marker.Type.CacheAllInCell, function);
  }

  public static DensityFunction mappedNoise(Holder<NormalNoise.NoiseParameters> noiseData, @Deprecated double xzScale,
      double yScale, double minTarget, double maxTarget) {
    return mapFromUnitTo(new Noise(new DensityFunction.NoiseHolder(noiseData), xzScale, yScale), minTarget, maxTarget);
  }

  public static DensityFunction mappedNoise(Holder<NormalNoise.NoiseParameters> noiseData, double yScale,
      double minTarget, double maxTarget) {
    return mappedNoise(noiseData, 1.0D, yScale, minTarget, maxTarget);
  }

  public static DensityFunction mappedNoise(Holder<NormalNoise.NoiseParameters> noiseData, double minTarget,
      double maxTarget) {
    return mappedNoise(noiseData, 1.0D, 1.0D, minTarget, maxTarget);
  }

  public static DensityFunction shiftedNoise2d(DensityFunction shiftX, DensityFunction shiftZ, double xzScale,
      Holder<NormalNoise.NoiseParameters> noiseData) {
    return new ShiftedNoise(shiftX, zero(), shiftZ, xzScale, 0.0D, new DensityFunction.NoiseHolder(noiseData));
  }

  public static DensityFunction noise(Holder<NormalNoise.NoiseParameters> noiseData) {
    return noise(noiseData, 1.0D, 1.0D);
  }

  public static DensityFunction noise(Holder<NormalNoise.NoiseParameters> noiseData, double xzScale, double yScale) {
    return new Noise(new DensityFunction.NoiseHolder(noiseData), xzScale, yScale);
  }

  public static DensityFunction noise(Holder<NormalNoise.NoiseParameters> noiseData, double yScale) {
    return noise(noiseData, 1.0D, yScale);
  }

  public static DensityFunction rangeChoice(DensityFunction input, double minInclusive, double maxExclusive,
      DensityFunction whenInRange, DensityFunction whenOutOfRange) {
    return new RangeChoice(input, minInclusive, maxExclusive, whenInRange, whenOutOfRange);
  }

  public static DensityFunction shiftA(Holder<NormalNoise.NoiseParameters> noiseData) {
    return new ShiftA(new DensityFunction.NoiseHolder(noiseData));
  }

  public static DensityFunction shiftB(Holder<NormalNoise.NoiseParameters> noiseData) {
    return new ShiftB(new DensityFunction.NoiseHolder(noiseData));
  }

  public static DensityFunction shift(Holder<NormalNoise.NoiseParameters> noiseData) {
    return new Shift(new DensityFunction.NoiseHolder(noiseData));
  }

  public static DensityFunction blendDensity(DensityFunction input) {
    return new BlendDensity(input);
  }

  public static DensityFunction endIslands(long seed) {
    return new EndIslandDensityFunction(seed);
  }

  public static DensityFunction weirdScaledSampler(DensityFunction input, Holder<NormalNoise.NoiseParameters> noiseData,
      WeirdScaledSampler.RarityValueMapper rarityValueMapper) {
    return new WeirdScaledSampler(input, new DensityFunction.NoiseHolder(noiseData), rarityValueMapper);
  }

  public static DensityFunction add(DensityFunction f1, DensityFunction f2) {
    return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.ADD, f1, f2);
  }

  public static DensityFunction mul(DensityFunction f1, DensityFunction f2) {
    return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.MUL, f1, f2);
  }

  public static DensityFunction min(DensityFunction f1, DensityFunction f2) {
    return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.MIN, f1, f2);
  }

  public static DensityFunction max(DensityFunction f1, DensityFunction f2) {
    return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.MAX, f1, f2);
  }

  public static DensityFunction spline(CubicSpline<Spline.Point, Spline.Coordinate> spline) {
    return new Spline(spline);
  }

  public static DensityFunction zero() {
    return Constant.ZERO;
  }

  public static DensityFunction constant(double value) {
    return new Constant(value);
  }

  public static DensityFunction yClampedGradient(int fromY, int toY, double fromValue, double toValue) {
    return new YClampedGradient(fromY, toY, fromValue, toValue);
  }

  public static DensityFunction map(DensityFunction function, Mapped.Type type) {
    return Mapped.create(type, function);
  }

  private static DensityFunction mapFromUnitTo(DensityFunction function, double min, double max) {
    double middle = (min + max) * 0.5D;
    double factor = (max - min) * 0.5D;
    return add(constant(middle), mul(constant(factor), function));
  }

  public static DensityFunction blendAlpha() {
    return BlendAlpha.INSTANCE;
  }

  public static DensityFunction blendOffset() {
    return BlendOffset.INSTANCE;
  }

  public static DensityFunction lerp(DensityFunction alpha, DensityFunction first, DensityFunction second) {
    if (first instanceof Constant) {
      Constant constant = (Constant) first;
      return lerp(alpha, constant.value, second);
    }
    DensityFunction alphaCached = cacheOnce(alpha);
    DensityFunction oneMinusAlpha = add(mul(alphaCached, constant(-1.0D)), constant(1.0D));
    return add(mul(first, oneMinusAlpha), mul(second, alphaCached));
  }

  public static DensityFunction lerp(DensityFunction factor, double first, DensityFunction second) {
    return add(mul(factor, add(second, constant(-first))), constant(first));
  }

  public static DensityFunction findTopSurface(DensityFunction density, DensityFunction upperBound, int lowerBound,
      int stepSize) {
    return new FindTopSurface(density, upperBound, lowerBound, stepSize);
  }

  private static interface TransformerWithContext extends DensityFunction {
    DensityFunction input();

    default double compute(DensityFunction.FunctionContext context) {
      return transform(context, input().compute(context));
    }

    default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
      input().fillArray(output, contextProvider);
      for (int i = 0; i < output.length; i++) {
        output[i] = transform(contextProvider.forIndex(i), output[i]);
      }
    }

    double transform(DensityFunction.FunctionContext param1FunctionContext, double param1Double);
  }

  private static interface PureTransformer extends DensityFunction {
    DensityFunction input();

    default double compute(DensityFunction.FunctionContext context) {
      return transform(input().compute(context));
    }

    default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
      input().fillArray(output, contextProvider);
      for (int i = 0; i < output.length; i++)
        output[i] = transform(output[i]);
    }

    double transform(double param1Double);
  }

  protected enum BlendAlpha implements DensityFunction.SimpleFunction {
    INSTANCE;

    public static final KeyDispatchDataCodec<DensityFunction> CODEC;
    static {
      CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
    }

    public double compute(DensityFunction.FunctionContext context) {
      return 1.0D;
    }

    public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
      Arrays.fill(output, 1.0D);
    }

    public double minValue() {
      return 1.0D;
    }

    public double maxValue() {
      return 1.0D;
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return CODEC;
    }
  }

  protected enum BlendOffset implements DensityFunction.SimpleFunction {
    INSTANCE;

    public static final KeyDispatchDataCodec<DensityFunction> CODEC;
    static {
      CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
    }

    public double compute(DensityFunction.FunctionContext context) {
      return 0.0D;
    }

    public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
      Arrays.fill(output, 0.0D);
    }

    public double minValue() {
      return 0.0D;
    }

    public double maxValue() {
      return 0.0D;
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return CODEC;
    }
  }

  public static interface BeardifierOrMarker extends DensityFunction.SimpleFunction {
    public static final KeyDispatchDataCodec<DensityFunction> CODEC = KeyDispatchDataCodec
        .of(MapCodec.unit(DensityFunctions.BeardifierMarker.INSTANCE));

    default KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return CODEC;
    }
  }

  protected enum BeardifierMarker implements BeardifierOrMarker {
    INSTANCE;

    public double compute(DensityFunction.FunctionContext context) {
      return 0.0D;
    }

    public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
      Arrays.fill(output, 0.0D);
    }

    public double minValue() {
      return 0.0D;
    }

    public double maxValue() {
      return 0.0D;
    }
  }

  @VisibleForDebug
  public static final class HolderHolder extends Record implements DensityFunction {
    private final Holder<DensityFunction> function;

    public final String toString() {

    public final int hashCode() {

    public final boolean equals(Object o) {

    public Holder<DensityFunction> function() {
      return this.function;
    }

    public HolderHolder(Holder<DensityFunction> function) {
      this.function = function;
    }

    public double compute(DensityFunction.FunctionContext context) {
      return ((DensityFunction) this.function.value()).compute(context);
    }

    public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
      ((DensityFunction) this.function.value()).fillArray(output, contextProvider);
    }

    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
      return visitor
          .apply(new HolderHolder(new Holder.Direct(((DensityFunction) this.function.value()).mapAll(visitor))));
    }

    public double minValue() {
      return this.function.isBound() ? ((DensityFunction) this.function.value()).minValue() : Double.NEGATIVE_INFINITY;
    }

    public double maxValue() {
      return this.function.isBound() ? ((DensityFunction) this.function.value()).maxValue() : Double.POSITIVE_INFINITY;
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
      throw new UnsupportedOperationException("Calling .codec() on HolderHolder");
    }
  }

  public static interface MarkerOrMarked extends DensityFunction {
    DensityFunctions.Marker.Type type();

    DensityFunction wrapped();

    default KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return (type()).codec;
    }

    default DensityFunction mapAll(DensityFunction.Visitor visitor) {
      return visitor.apply(new DensityFunctions.Marker(type(), wrapped().mapAll(visitor)));
    }
  }

  protected static final class Marker extends Record implements MarkerOrMarked {
    private final Type type;
    private final DensityFunction wrapped;

    protected Marker(Type type, DensityFunction wrapped) {
      this.type = type;
      this.wrapped = wrapped;
    }

    public final String toString() { 
     enum Type implements StringRepresentable { Interpolated("interpolated"),
       FlatCache("flat_cache"),
       Cache2D("cache_2d"),
       CacheOnce("cache_once"),
       CacheAllInCell("cache_all_in_cell");
       private final String name;
       private final KeyDispatchDataCodec<DensityFunctions.MarkerOrMarked> codec;
       Type(String name) {
         this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> new DensityFunctions.Marker(this, input), DensityFunctions.MarkerOrMarked::wrapped);
         this.name = name;
       }
       public String getSerializedName() { return this.name; } }

    public double compute(DensityFunction.FunctionContext context) {
      return this.wrapped.compute(context);
    }

    public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
      this.wrapped.fillArray(output, contextProvider);
    }

    public double minValue() {
      return this.wrapped.minValue();
    }

    public double maxValue() {
      return this.wrapped.maxValue();
    }
  }

  enum Type implements StringRepresentable {
    Interpolated("interpolated"), FlatCache("flat_cache"), Cache2D("cache_2d"), CacheOnce("cache_once"),
    CacheAllInCell("cache_all_in_cell");

    private final String name;
    private final KeyDispatchDataCodec<DensityFunctions.MarkerOrMarked> codec;

    Type(String name) {
      this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> new DensityFunctions.Marker(this, input),
          DensityFunctions.MarkerOrMarked::wrapped);
      this.name = name;
    }

    public String getSerializedName() {
      return this.name;
    }
  }

  protected static final class Noise extends Record implements DensityFunction {
    private final DensityFunction.NoiseHolder noise;
    @Deprecated
    private final double xzScale;
    private final double yScale;

    protected Noise(DensityFunction.NoiseHolder noise, @Deprecated double xzScale, double yScale) {
      this.noise = noise;
      this.xzScale = xzScale;
      this.yScale = yScale;
    }

    public final String toString() {

    public static final MapCodec<Noise> DATA_CODEC = RecordCodecBuilder
        .mapCodec(i -> i.group(DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter(Noise::noise),
            Codec.DOUBLE.fieldOf("xz_scale").forGetter(Noise::xzScale),
            Codec.DOUBLE.fieldOf("y_scale").forGetter(Noise::yScale)).apply(i, Noise::new));
    public static final KeyDispatchDataCodec<Noise> CODEC = DensityFunctions.makeCodec(DATA_CODEC);

    public double compute(DensityFunction.FunctionContext context) {
      return this.noise.getValue(context.blockX() * this.xzScale, context.blockY() * this.yScale,
          context.blockZ() * this.xzScale);
    }

    public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
      contextProvider.fillAllDirectly(output, this);
    }

    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
      return visitor.apply(new Noise(visitor.visitNoise(this.noise), this.xzScale, this.yScale));
    }

    public double minValue() {
      return -maxValue();
    }

    public double maxValue() {
      return this.noise.maxValue();
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return CODEC;
    }
  }

  protected static final class EndIslandDensityFunction implements DensityFunction.SimpleFunction {
    public static final KeyDispatchDataCodec<EndIslandDensityFunction> CODEC = KeyDispatchDataCodec
        .of(MapCodec.unit(new EndIslandDensityFunction(0L)));
    private static final float ISLAND_THRESHOLD = -0.9F;
    private final SimplexNoise islandNoise;

    public EndIslandDensityFunction(long seed) {
      RandomSource islandRandom = new LegacyRandomSource(seed);
      islandRandom.consumeCount(17292);
      this.islandNoise = new SimplexNoise(islandRandom);
    }

    private static float getHeightValue(SimplexNoise islandNoise, int sectionX, int sectionZ) {
      int chunkX = sectionX / 2;
      int chunkZ = sectionZ / 2;
      int subSectionX = sectionX % 2;
      int subSectionZ = sectionZ % 2;
      float doffs = 100.0F - Mth.sqrt((sectionX * sectionX + sectionZ * sectionZ)) * 8.0F;
      doffs = Mth.clamp(doffs, -100.0F, 80.0F);
      for (int xo = -12; xo <= 12; xo++) {
        for (int zo = -12; zo <= 12; zo++) {
          long totalChunkX = (chunkX + xo);
          long totalChunkZ = (chunkZ + zo);
          if (totalChunkX * totalChunkX + totalChunkZ * totalChunkZ > 4096L
              && islandNoise.getValue(totalChunkX, totalChunkZ) < -0.8999999761581421D) {
            float islandSize = (Mth.abs((float) totalChunkX) * 3439.0F + Mth.abs((float) totalChunkZ) * 147.0F) % 13.0F
                + 9.0F;
            float xd = (subSectionX - xo * 2);
            float zd = (subSectionZ - zo * 2);
            float newDoffs = 100.0F - Mth.sqrt(xd * xd + zd * zd) * islandSize;
            newDoffs = Mth.clamp(newDoffs, -100.0F, 80.0F);
            doffs = Math.max(doffs, newDoffs);
          }
        }
      }
      return doffs;
    }

    public double compute(DensityFunction.FunctionContext context) {
      return (getHeightValue(this.islandNoise, context.blockX() / 8, context.blockZ() / 8) - 8.0D) / 128.0D;
    }

    public double minValue() {
      return -0.84375D;
    }

    public double maxValue() {
      return 0.5625D;
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return CODEC;
    }
  }

  protected static final class WeirdScaledSampler extends Record implements TransformerWithContext {
    private final DensityFunction input;
    private final DensityFunction.NoiseHolder noise;
    private final RarityValueMapper rarityValueMapper;

    protected WeirdScaledSampler(DensityFunction input, DensityFunction.NoiseHolder noise,
        RarityValueMapper rarityValueMapper) {
      this.input = input;
      this.noise = noise;
      this.rarityValueMapper = rarityValueMapper;
    }

    public final String toString() {

    private static final MapCodec<WeirdScaledSampler> DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(WeirdScaledSampler::input),
            DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter(WeirdScaledSampler::noise),
            RarityValueMapper.CODEC.fieldOf("rarity_value_mapper").forGetter(WeirdScaledSampler::rarityValueMapper))
        .apply(i, WeirdScaledSampler::new));
    public static final KeyDispatchDataCodec<WeirdScaledSampler> CODEC = DensityFunctions.makeCodec(DATA_CODEC);

    public enum RarityValueMapper implements StringRepresentable {
      TYPE1("type_1", NoiseRouterData.QuantizedSpaghettiRarity::getSpaghettiRarity3D, 2.0D),
      TYPE2("type_2", NoiseRouterData.QuantizedSpaghettiRarity::getSphaghettiRarity2D, 3.0D);

      public static final Codec<RarityValueMapper> CODEC;
      private final String name;
      private final Double2DoubleFunction mapper;
      private final double maxRarity;
      static {
        CODEC = StringRepresentable.fromEnum(RarityValueMapper::values);
      }

      RarityValueMapper(String name, Double2DoubleFunction mapper, double maxRarity) {
        this.name = name;
        this.mapper = mapper;
        this.maxRarity = maxRarity;
      }

      public String getSerializedName() {
        return this.name;
      }
    }

    public double transform(DensityFunction.FunctionContext context, double input) {
      double rarity = this.rarityValueMapper.mapper.get(input);
      return rarity * Math
          .abs(this.noise.getValue(context.blockX() / rarity, context.blockY() / rarity, context.blockZ() / rarity));
    }

    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
      return visitor.apply(
          new WeirdScaledSampler(this.input.mapAll(visitor), visitor.visitNoise(this.noise), this.rarityValueMapper));
    }

    public double minValue() {
      return 0.0D;
    }

    public double maxValue() {
      return this.rarityValueMapper.maxRarity * this.noise.maxValue();
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return CODEC;
    }
  }

  public enum RarityValueMapper implements StringRepresentable {
    TYPE1("type_1", NoiseRouterData.QuantizedSpaghettiRarity::getSpaghettiRarity3D, 2.0D),
    TYPE2("type_2", NoiseRouterData.QuantizedSpaghettiRarity::getSphaghettiRarity2D, 3.0D);

    public static final Codec<RarityValueMapper> CODEC;
    private final String name;
    private final Double2DoubleFunction mapper;
    private final double maxRarity;
    static {
      CODEC = StringRepresentable.fromEnum(RarityValueMapper::values);
    }

    RarityValueMapper(String name, Double2DoubleFunction mapper, double maxRarity) {
      this.name = name;
      this.mapper = mapper;
      this.maxRarity = maxRarity;
    }

    public String getSerializedName() {
      return this.name;
    }
  }

  protected static final class ShiftedNoise extends Record implements DensityFunction {
    private final DensityFunction shiftX;
    private final DensityFunction shiftY;
    private final DensityFunction shiftZ;
    private final double xzScale;
    private final double yScale;
    private final DensityFunction.NoiseHolder noise;

    protected ShiftedNoise(DensityFunction shiftX, DensityFunction shiftY, DensityFunction shiftZ, double xzScale,
        double yScale, DensityFunction.NoiseHolder noise) {
      this.shiftX = shiftX;
      this.shiftY = shiftY;
      this.shiftZ = shiftZ;
      this.xzScale = xzScale;
      this.yScale = yScale;
      this.noise = noise;
    }

    public final String toString() {

    private static final MapCodec<ShiftedNoise> DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_x").forGetter(ShiftedNoise::shiftX),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_y").forGetter(ShiftedNoise::shiftY),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_z").forGetter(ShiftedNoise::shiftZ),
            Codec.DOUBLE.fieldOf("xz_scale").forGetter(ShiftedNoise::xzScale),
            Codec.DOUBLE.fieldOf("y_scale").forGetter(ShiftedNoise::yScale),
            DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter(ShiftedNoise::noise))
        .apply(i, ShiftedNoise::new));
    public static final KeyDispatchDataCodec<ShiftedNoise> CODEC = DensityFunctions.makeCodec(DATA_CODEC);

    public double compute(DensityFunction.FunctionContext context) {
      double x = context.blockX() * this.xzScale + this.shiftX.compute(context);
      double y = context.blockY() * this.yScale + this.shiftY.compute(context);
      double z = context.blockZ() * this.xzScale + this.shiftZ.compute(context);
      return this.noise.getValue(x, y, z);
    }

    public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
      contextProvider.fillAllDirectly(output, this);
    }

    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
      return visitor.apply(new ShiftedNoise(this.shiftX.mapAll(visitor), this.shiftY.mapAll(visitor),
          this.shiftZ.mapAll(visitor), this.xzScale, this.yScale, visitor.visitNoise(this.noise)));
    }

    public double minValue() {
      return -maxValue();
    }

    public double maxValue() {
      return this.noise.maxValue();
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return CODEC;
    }
  }

  private static final class RangeChoice extends Record implements DensityFunction {
    private final DensityFunction input;
    private final double minInclusive;
    private final double maxExclusive;
    private final DensityFunction whenInRange;
    private final DensityFunction whenOutOfRange;

    private RangeChoice(DensityFunction input, double minInclusive, double maxExclusive, DensityFunction whenInRange,
        DensityFunction whenOutOfRange) {
      this.input = input;
      this.minInclusive = minInclusive;
      this.maxExclusive = maxExclusive;
      this.whenInRange = whenInRange;
      this.whenOutOfRange = whenOutOfRange;
    }

    public final String toString() {

    public static final MapCodec<RangeChoice> DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(RangeChoice::input),
            DensityFunctions.NOISE_VALUE_CODEC.fieldOf("min_inclusive").forGetter(RangeChoice::minInclusive),
            DensityFunctions.NOISE_VALUE_CODEC.fieldOf("max_exclusive").forGetter(RangeChoice::maxExclusive),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("when_in_range").forGetter(RangeChoice::whenInRange),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("when_out_of_range").forGetter(RangeChoice::whenOutOfRange))
        .apply(i, RangeChoice::new));
    public static final KeyDispatchDataCodec<RangeChoice> CODEC = DensityFunctions.makeCodec(DATA_CODEC);

    public double compute(DensityFunction.FunctionContext context) {
      double inputValue = this.input.compute(context);
      if (inputValue >= this.minInclusive && inputValue < this.maxExclusive) {
        return this.whenInRange.compute(context);
      }
      return this.whenOutOfRange.compute(context);
    }

    public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
      this.input.fillArray(output, contextProvider);
      for (int i = 0; i < output.length; i++) {
        double v = output[i];
        if (v >= this.minInclusive && v < this.maxExclusive) {
          output[i] = this.whenInRange.compute(contextProvider.forIndex(i));
        } else {
          output[i] = this.whenOutOfRange.compute(contextProvider.forIndex(i));
        }
      }
    }

    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
      return visitor.apply(new RangeChoice(this.input.mapAll(visitor), this.minInclusive, this.maxExclusive,
          this.whenInRange.mapAll(visitor), this.whenOutOfRange.mapAll(visitor)));
    }

    public double minValue() {
      return Math.min(this.whenInRange.minValue(), this.whenOutOfRange.minValue());
    }

    public double maxValue() {
      return Math.max(this.whenInRange.maxValue(), this.whenOutOfRange.maxValue());
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return CODEC;
    }
  }

  static interface ShiftNoise extends DensityFunction {
    DensityFunction.NoiseHolder offsetNoise();

    default double minValue() {
      return -maxValue();
    }

    default double maxValue() {
      return offsetNoise().maxValue() * 4.0D;
    }

    default double compute(double localX, double localY, double localZ) {
      return offsetNoise().getValue(localX * 0.25D, localY * 0.25D, localZ * 0.25D) * 4.0D;
    }

    default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
      contextProvider.fillAllDirectly(output, this);
    }
  }

  protected static final class ShiftA extends Record implements ShiftNoise {
    private final DensityFunction.NoiseHolder offsetNoise;

    protected ShiftA(DensityFunction.NoiseHolder offsetNoise) {
      this.offsetNoise = offsetNoise;
    }

    public final String toString() {

    private static final KeyDispatchDataCodec<ShiftA> CODEC = DensityFunctions
        .singleArgumentCodec(DensityFunction.NoiseHolder.CODEC, ShiftA::new, ShiftA::offsetNoise);

    public double compute(DensityFunction.FunctionContext context) {
      return compute(context.blockX(), 0.0D, context.blockZ());
    }

    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
      return visitor.apply(new ShiftA(visitor.visitNoise(this.offsetNoise)));
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return CODEC;
    }
  }

  protected static final class ShiftB extends Record implements ShiftNoise {
    private final DensityFunction.NoiseHolder offsetNoise;

    protected ShiftB(DensityFunction.NoiseHolder offsetNoise) {
      this.offsetNoise = offsetNoise;
    }

    public final String toString() {

    private static final KeyDispatchDataCodec<ShiftB> CODEC = DensityFunctions
        .singleArgumentCodec(DensityFunction.NoiseHolder.CODEC, ShiftB::new, ShiftB::offsetNoise);

    public double compute(DensityFunction.FunctionContext context) {
      return compute(context.blockZ(), context.blockX(), 0.0D);
    }

    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
      return visitor.apply(new ShiftB(visitor.visitNoise(this.offsetNoise)));
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return CODEC;
    }
  }

  protected static final class Shift extends Record implements ShiftNoise {
    private final DensityFunction.NoiseHolder offsetNoise;

    protected Shift(DensityFunction.NoiseHolder offsetNoise) {
      this.offsetNoise = offsetNoise;
    }

    public final String toString() {

    private static final KeyDispatchDataCodec<Shift> CODEC = DensityFunctions
        .singleArgumentCodec(DensityFunction.NoiseHolder.CODEC, Shift::new, Shift::offsetNoise);

    public double compute(DensityFunction.FunctionContext context) {
      return compute(context.blockX(), context.blockY(), context.blockZ());
    }

    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
      return visitor.apply(new Shift(visitor.visitNoise(this.offsetNoise)));
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return CODEC;
    }
  }

  private static final class BlendDensity extends Record implements TransformerWithContext {
    private final DensityFunction input;

    private BlendDensity(DensityFunction input) {
      this.input = input;
    }

    public final String toString() {

    private static final KeyDispatchDataCodec<BlendDensity> CODEC = DensityFunctions
        .singleFunctionArgumentCodec(BlendDensity::new, BlendDensity::input);

    public double transform(DensityFunction.FunctionContext context, double input) {
      return context.getBlender().blendDensity(context, input);
    }

    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
      return visitor.apply(new BlendDensity(this.input.mapAll(visitor)));
    }

    public double minValue() {
      return Double.NEGATIVE_INFINITY;
    }

    public double maxValue() {
      return Double.POSITIVE_INFINITY;
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return CODEC;
    }
  }

  protected static final class Clamp extends Record implements PureTransformer {
    private final DensityFunction input;
    private final double minValue;
    private final double maxValue;

    protected Clamp(DensityFunction input, double minValue, double maxValue) {
      this.input = input;
      this.minValue = minValue;
      this.maxValue = maxValue;
    }

    public final String toString() {

    private static final MapCodec<Clamp> DATA_CODEC = RecordCodecBuilder
        .mapCodec(i -> i.group(DensityFunction.DIRECT_CODEC.fieldOf("input").forGetter(Clamp::input),
            DensityFunctions.NOISE_VALUE_CODEC.fieldOf("min").forGetter(Clamp::minValue),
            DensityFunctions.NOISE_VALUE_CODEC.fieldOf("max").forGetter(Clamp::maxValue)).apply(i, Clamp::new));
    public static final KeyDispatchDataCodec<Clamp> CODEC = DensityFunctions.makeCodec(DATA_CODEC);

    public double transform(double input) {
      return Mth.clamp(input, this.minValue, this.maxValue);
    }

    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
      return new Clamp(this.input.mapAll(visitor), this.minValue, this.maxValue);
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return CODEC;
    }
  }

  protected static final class Mapped extends Record implements PureTransformer {
    private final Type type;
    private final DensityFunction input;
    private final double minValue;
    private final double maxValue;

    protected Mapped(Type type, DensityFunction input, double minValue, double maxValue) {
      this.type = type;
      this.input = input;
      this.minValue = minValue;
      this.maxValue = maxValue;
    }

    public final String toString() {

    public static Mapped create(Type type, DensityFunction input) {
      double minValue = input.minValue();
      double maxValue = input.maxValue();
      double minImage = transform(type, minValue);
      double maxImage = transform(type, maxValue);
      if (type == Type.INVERT) {
        if (minValue < 0.0D && maxValue > 0.0D) {
          return new Mapped(type, input, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        return new Mapped(type, input, maxImage, minImage);
      }
      if (type == Type.ABS || type == Type.SQUARE) {
        return new Mapped(type, input, Math.max(0.0D, minValue), Math.max(minImage, maxImage));
      }
      return new Mapped(type, input, minImage, maxImage);
    }

    enum Type implements StringRepresentable {
      ABS("abs"), SQUARE("square"), CUBE("cube"), HALF_NEGATIVE("half_negative"), QUARTER_NEGATIVE("quarter_negative"),
      INVERT("invert"), SQUEEZE("squeeze");

      private final String name;
      private final KeyDispatchDataCodec<DensityFunctions.Mapped> codec;

      Type(String name) {
        this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> DensityFunctions.Mapped.create(this, input),
            DensityFunctions.Mapped::input);
        this.name = name;
      }

      public String getSerializedName() {
        return this.name;
      }
    }

    private static double transform(Type type, double input) {
      switch (type.ordinal()) {
      default:
        throw new MatchException(null, null);
      case 0:
      case 1:
      case 2:
      case 3:
        return (input > 0.0D) ? input : (input * 0.5D);
      case 4:
        return (input > 0.0D) ? input : (input * 0.25D);
      case 5:
      case 6:
        break;
      }
      double c = Mth.clamp(input, -1.0D, 1.0D);
    }

    public double transform(double input) {
      return transform(this.type, input);
    }

    public Mapped mapAll(DensityFunction.Visitor visitor) {
      return create(this.type, this.input.mapAll(visitor));
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return this.type.codec;
    }
  }

  enum Type implements StringRepresentable {
    ABS("abs"), SQUARE("square"), CUBE("cube"), HALF_NEGATIVE("half_negative"), QUARTER_NEGATIVE("quarter_negative"),
    INVERT("invert"), SQUEEZE("squeeze");

    private final String name;
    private final KeyDispatchDataCodec<DensityFunctions.Mapped> codec;

    Type(String name) {
      this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> DensityFunctions.Mapped.create(this, input),
          DensityFunctions.Mapped::input);
      this.name = name;
    }

    public String getSerializedName() {
      return this.name;
    }
  }

  static interface TwoArgumentSimpleFunction extends DensityFunction {
    public static final Logger LOGGER = LogUtils.getLogger();

    static TwoArgumentSimpleFunction create(Type type, DensityFunction argument1, DensityFunction argument2) {
      double min1 = argument1.minValue();
      double min2 = argument2.minValue();
      double max1 = argument1.maxValue();
      double max2 = argument2.maxValue();
      if (type == Type.MIN || type == Type.MAX) {
        boolean firstAlwaysBiggerThanSecond = (min1 >= max2);
        boolean secondAlwaysBiggerThanFirst = (min2 >= max1);
        if (firstAlwaysBiggerThanSecond || secondAlwaysBiggerThanFirst) {
          LOGGER.warn("Creating a {} function between two non-overlapping inputs: {} and {}",
              new Object[] { type, argument1, argument2 });
        }
      }
      switch (type.ordinal()) {
      default:
        throw new MatchException(null, null);
      case 0:
      case 3:
      case 2:
      case 1:
        if (min1 > 0.0D && min2 > 0.0D)
          ;
        if (max1 < 0.0D && max2 < 0.0D)
          ;
        break;
      }
      double minValue = Math.min(min1 * max2, max1 * min2);
      switch (type.ordinal()) {
      default:
        throw new MatchException(null, null);
      case 0:
      case 3:
      case 2:
      case 1:
        if (min1 > 0.0D && min2 > 0.0D)
          ;
        if (max1 < 0.0D && max2 < 0.0D)
          ;
        break;
      }
      double maxValue = Math.max(min1 * min2, max1 * max2);
      if (type == Type.MUL || type == Type.ADD) {
        if (argument1 instanceof DensityFunctions.Constant) {
          DensityFunctions.Constant constant = (DensityFunctions.Constant) argument1;
          return new DensityFunctions.MulOrAdd(
              (type == Type.ADD) ? DensityFunctions.MulOrAdd.Type.ADD : DensityFunctions.MulOrAdd.Type.MUL, argument2,
              minValue, maxValue, constant.value);
        }
        if (argument2 instanceof DensityFunctions.Constant) {
          DensityFunctions.Constant constant = (DensityFunctions.Constant) argument2;
          return new DensityFunctions.MulOrAdd(
              (type == Type.ADD) ? DensityFunctions.MulOrAdd.Type.ADD : DensityFunctions.MulOrAdd.Type.MUL, argument1,
              minValue, maxValue, constant.value);
        }
      }
      return new DensityFunctions.Ap2(type, argument1, argument2, minValue, maxValue);
    }

    Type type();

    DensityFunction argument1();

    DensityFunction argument2();

    public enum Type implements StringRepresentable {
      ADD("add"), MUL("mul"), MIN("min"), MAX("max");

      private final KeyDispatchDataCodec<DensityFunctions.TwoArgumentSimpleFunction> codec;
      private final String name;

      Type(String name) {
        this.codec = DensityFunctions.doubleFunctionArgumentCodec(
            (argument1, argument2) -> DensityFunctions.TwoArgumentSimpleFunction.create(this, argument1, argument2),
            DensityFunctions.TwoArgumentSimpleFunction::argument1,
            DensityFunctions.TwoArgumentSimpleFunction::argument2);
        this.name = name;
      }

      public String getSerializedName() {
        return this.name;
      }
    }

    default KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return (type()).codec;
    }
  }

  public enum Type implements StringRepresentable {
    ADD("add"), MUL("mul"), MIN("min"), MAX("max");

    private final KeyDispatchDataCodec<DensityFunctions.TwoArgumentSimpleFunction> codec;
    private final String name;

    Type(String name) {
      this.codec = DensityFunctions.doubleFunctionArgumentCodec(
          (argument1, argument2) -> DensityFunctions.TwoArgumentSimpleFunction.create(this, argument1, argument2),
          DensityFunctions.TwoArgumentSimpleFunction::argument1, DensityFunctions.TwoArgumentSimpleFunction::argument2);
      this.name = name;
    }

    public String getSerializedName() {
      return this.name;
    }
  }

  private static final class MulOrAdd extends Record implements TwoArgumentSimpleFunction, PureTransformer {
    private final Type specificType;
    private final DensityFunction input;
    private final double minValue;
    private final double maxValue;
    private final double argument;

    private MulOrAdd(Type specificType, DensityFunction input, double minValue, double maxValue, double argument) {
      this.specificType = specificType;
      this.input = input;
      this.minValue = minValue;
      this.maxValue = maxValue;
      this.argument = argument;
    }

    public final String toString() { 
     enum Type { MUL,
       ADD; }

    public DensityFunctions.TwoArgumentSimpleFunction.Type type() {
      return (this.specificType == Type.MUL) ? DensityFunctions.TwoArgumentSimpleFunction.Type.MUL
          : DensityFunctions.TwoArgumentSimpleFunction.Type.ADD;
    }

    public DensityFunction argument1() {
      return DensityFunctions.constant(this.argument);
    }

    public DensityFunction argument2() {
      return this.input;
    }

    public double transform(double input) {
      switch (this.specificType.ordinal()) {
      default:
        throw new MatchException(null, null);
      case 0:
      case 1:
        break;
      }
      return input + this.argument;
    }

    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
      double maxValue, minValue;
      DensityFunction function = this.input.mapAll(visitor);
      double min = function.minValue();
      double max = function.maxValue();
      if (this.specificType == Type.ADD) {
        minValue = min + this.argument;
        maxValue = max + this.argument;
      } else if (this.argument >= 0.0D) {
        minValue = min * this.argument;
        maxValue = max * this.argument;
      } else {
        minValue = max * this.argument;
        maxValue = min * this.argument;
      }
      return new MulOrAdd(this.specificType, function, minValue, maxValue, this.argument);
    }
  }

  enum Type {
    MUL, ADD;
  }

  private static final class Ap2 extends Record implements TwoArgumentSimpleFunction {
    private final DensityFunctions.TwoArgumentSimpleFunction.Type type;
    private final DensityFunction argument1;
    private final DensityFunction argument2;
    private final double minValue;
    private final double maxValue;

    private Ap2(DensityFunctions.TwoArgumentSimpleFunction.Type type, DensityFunction argument1,
        DensityFunction argument2, double minValue, double maxValue) {
      this.type = type;
      this.argument1 = argument1;
      this.argument2 = argument2;
      this.minValue = minValue;
      this.maxValue = maxValue;
    }

    public final String toString() {

    public double compute(DensityFunction.FunctionContext context) {
      double v1 = this.argument1.compute(context);
      switch (this.type.ordinal()) {
      default:
        throw new MatchException(null, null);
      case 0:
      case 1:
        return (v1 == 0.0D) ? 0.0D : (v1 * this.argument2.compute(context));
      case 2:
        return (v1 < this.argument2.minValue()) ? v1 : Math.min(v1, this.argument2.compute(context));
      case 3:
        break;
      }
      return (v1 > this.argument2.maxValue()) ? v1 : Math.max(v1, this.argument2.compute(context));
    }

    public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
      int i, i, i, i;
      double min, max, v2[];
      this.argument1.fillArray(output, contextProvider);
      switch (this.type.ordinal()) {
      case 0:
        v2 = new double[output.length];
        this.argument2.fillArray(v2, contextProvider);
        for (i = 0; i < output.length; i++) {
          output[i] = output[i] + v2[i];
        }
        break;
      case 1:
        for (i = 0; i < output.length; i++) {
          double v = output[i];
          output[i] = (v == 0.0D) ? 0.0D : (v * this.argument2.compute(contextProvider.forIndex(i)));
        }
        break;
      case 2:
        min = this.argument2.minValue();
        for (i = 0; i < output.length; i++) {
          double v = output[i];
          output[i] = (v < min) ? v : Math.min(v, this.argument2.compute(contextProvider.forIndex(i)));
        }
        break;
      case 3:
        max = this.argument2.maxValue();
        for (i = 0; i < output.length; i++) {
          double v = output[i];
          output[i] = (v > max) ? v : Math.max(v, this.argument2.compute(contextProvider.forIndex(i)));
        }
        break;
      }
    }

    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
      return visitor.apply(DensityFunctions.TwoArgumentSimpleFunction.create(this.type, this.argument1.mapAll(visitor),
          this.argument2.mapAll(visitor)));
    }

    public double minValue() {
      return this.minValue;
    }

    public double maxValue() {
      return this.maxValue;
    }
  }

  public static final class Spline extends Record implements DensityFunction {
    private final CubicSpline<Point, Coordinate> spline;

    public Spline(CubicSpline<Point, Coordinate> spline) {
      this.spline = spline;
    }

    public final String toString() {

    private static final Codec<CubicSpline<Point, Coordinate>> SPLINE_CODEC = CubicSpline.codec(Coordinate.CODEC);
    private static final MapCodec<Spline> DATA_CODEC = SPLINE_CODEC.fieldOf("spline").xmap(Spline::new, Spline::spline);
    public static final KeyDispatchDataCodec<Spline> CODEC = DensityFunctions.makeCodec(DATA_CODEC);

    public double compute(DensityFunction.FunctionContext context) {
      return this.spline.apply(new Point(context));
    }

    public double minValue() {
      return this.spline.minValue();
    }

    public double maxValue() {
      return this.spline.maxValue();
    }

    public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
      contextProvider.fillAllDirectly(output, this);
    }

    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
      return visitor.apply(new Spline(this.spline.mapAll(c -> c.mapAll(visitor))));
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
      return CODEC;
    }

    public static final class Coordinate extends Record implements BoundedFloatFunction<Point> {
      private final Holder<DensityFunction> function;

      public Coordinate(Holder<DensityFunction> function) {
        this.function = function;
      }

      public final int hashCode() {

      public static final Codec<Coordinate> CODEC = DensityFunction.CODEC.xmap(Coordinate::new, Coordinate::function);

      public String toString() {
        Optional<ResourceKey<DensityFunction>> key = this.function.unwrapKey();
        if (key.isPresent()) {
          ResourceKey<DensityFunction> name = (ResourceKey) key.get();
          if (name == NoiseRouterData.CONTINENTS) {
            return "continents";
          }
          if (name == NoiseRouterData.EROSION) {
            return "erosion";
          }
          if (name == NoiseRouterData.RIDGES) {
            return "weirdness";
          }
          if (name == NoiseRouterData.RIDGES_FOLDED) {
            return "ridges";
          }
        }
        return "Coordinate[" + String.valueOf(this.function) + "]";
      }

      public float apply(DensityFunctions.Spline.Point point) {
        return (float) ((DensityFunction) this.function.value()).compute(point.context());
      }

      public float minValue() {
        return this.function.isBound() ? (float) ((DensityFunction) this.function.value()).minValue()
            : Float.NEGATIVE_INFINITY;
      }

      public float maxValue() {
        return this.function.isBound() ? (float) ((DensityFunction) this.function.value()).maxValue()
            : Float.POSITIVE_INFINITY;
      }

      public Coordinate mapAll(DensityFunction.Visitor visitor) {
        return new Coordinate(new Holder.Direct(((DensityFunction) this.function.value()).mapAll(visitor)));
      }
    }

    public static final class Point extends Record {
      private final DensityFunction.FunctionContext context;

      public Point(DensityFunction.FunctionContext context) {
        this.context = context;
      }

      public final String toString() { 
   private static final class Constant extends Record implements DensityFunction.SimpleFunction { private final double value;
     private Constant(double value) { this.value = value; } public final String toString() { 
     private static final KeyDispatchDataCodec<Constant> CODEC = DensityFunctions.singleArgumentCodec(DensityFunctions.NOISE_VALUE_CODEC, Constant::new, Constant::value);
     private static final Constant ZERO = new Constant(0.0D);
     public double compute(DensityFunction.FunctionContext context) { return this.value; }
     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { Arrays.fill(output, this.value); }
     public double minValue() { return this.value; }
     public double maxValue() { return this.value; }
     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
   private static final class YClampedGradient extends Record implements DensityFunction.SimpleFunction { private final int fromY; private final int toY; private final double fromValue;
     private final double toValue;
     private YClampedGradient(int fromY, int toY, double fromValue, double toValue) { this.fromY = fromY; this.toY = toY; this.fromValue = fromValue; this.toValue = toValue; } public final String toString() { 
     private static final MapCodec<YClampedGradient> DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
           Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("from_y").forGetter(YClampedGradient::fromY), 
           Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("to_y").forGetter(YClampedGradient::toY), DensityFunctions.NOISE_VALUE_CODEC
           .fieldOf("from_value").forGetter(YClampedGradient::fromValue), DensityFunctions.NOISE_VALUE_CODEC
           .fieldOf("to_value").forGetter(YClampedGradient::toValue))
         .apply(i, YClampedGradient::new));
     public static final KeyDispatchDataCodec<YClampedGradient> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
     public double compute(DensityFunction.FunctionContext context) { 
      return Mth.clampedMap(context.blockY(), this.fromY, this.toY, this.fromValue, this.toValue);
    }
     public double minValue() { return Math.min(this.fromValue, this.toValue); }
     public double maxValue() { return Math.max(this.fromValue, this.toValue); }
     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
   private static final class FindTopSurface extends Record implements DensityFunction { private final DensityFunction density; private final DensityFunction upperBound; private final int lowerBound;
     private final int cellHeight;
     private FindTopSurface(DensityFunction density, DensityFunction upperBound, int lowerBound, int cellHeight) { this.density = density; this.upperBound = upperBound; this.lowerBound = lowerBound; this.cellHeight = cellHeight; } public final String toString() { 
     private static final MapCodec<FindTopSurface> DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(DensityFunction.HOLDER_HELPER_CODEC
           .fieldOf("density").forGetter(FindTopSurface::density), DensityFunction.HOLDER_HELPER_CODEC
           .fieldOf("upper_bound").forGetter(FindTopSurface::upperBound), 
           Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("lower_bound").forGetter(FindTopSurface::lowerBound), ExtraCodecs.POSITIVE_INT
           .fieldOf("cell_height").forGetter(FindTopSurface::cellHeight))
         .apply(i, FindTopSurface::new));
     public static final KeyDispatchDataCodec<FindTopSurface> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
     public double compute(DensityFunction.FunctionContext context) {
       int topY = Mth.floor(this.upperBound.compute(context) / this.cellHeight) * this.cellHeight;
       if (topY <= this.lowerBound) {
         return this.lowerBound;
       }
       for (int blockY = topY; blockY >= this.lowerBound; blockY -= this.cellHeight) {
         if (this.density.compute(new DensityFunction.SinglePointContext(context.blockX(), blockY, context.blockZ())) > 0.0D) {
           return blockY;
         }
       } 
       return this.lowerBound;
     }
     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { contextProvider.fillAllDirectly(output, this); }
     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
       return visitor.apply(new FindTopSurface(this.density
             .mapAll(visitor), this.upperBound
             .mapAll(visitor), this.lowerBound, this.cellHeight));
     }
     public double minValue() { return this.lowerBound; }
     public double maxValue() { return Math.max(this.lowerBound, this.upperBound.maxValue()); }
     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; } }
 }

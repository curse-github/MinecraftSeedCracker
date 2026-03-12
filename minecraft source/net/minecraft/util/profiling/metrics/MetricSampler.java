/*     */ package net.minecraft.util.profiling.metrics;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.DoubleSupplier;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MetricSampler
/*     */ {
/*     */   private final String name;
/*     */   private final MetricCategory category;
/*     */   private final DoubleSupplier sampler;
/*     */   private final ByteBuf ticks;
/*     */   private final ByteBuf values;
/*     */   private final Runnable beforeTick;
/*     */   final ThresholdTest thresholdTest;
/*     */   private double currentValue;
/*     */   
/*     */   protected MetricSampler(String name, MetricCategory category, DoubleSupplier sampler, Runnable beforeTick, ThresholdTest thresholdTest) {
/*  29 */     this.name = name;
/*  30 */     this.category = category;
/*  31 */     this.beforeTick = beforeTick;
/*  32 */     this.sampler = sampler;
/*  33 */     this.thresholdTest = thresholdTest;
/*  34 */     this.values = ByteBufAllocator.DEFAULT.buffer();
/*  35 */     this.ticks = ByteBufAllocator.DEFAULT.buffer();
/*  36 */     this.isRunning = true;
/*     */   }
/*     */ 
/*     */   
/*  40 */   public static MetricSampler create(String name, MetricCategory category, DoubleSupplier sampler) { return new MetricSampler(name, category, sampler, null, null); }
/*     */ 
/*     */ 
/*     */   
/*  44 */   public static <T> MetricSampler create(String metricName, MetricCategory category, T context, ToDoubleFunction<T> sampler) { return builder(metricName, category, sampler, context).build(); }
/*     */ 
/*     */   
/*     */   public static <T> MetricSamplerBuilder<T> builder(String metricName, MetricCategory category, ToDoubleFunction<T> sampler, T context) {
/*  48 */     if (sampler == null) {
/*  49 */       throw new IllegalStateException();
/*     */     }
/*  51 */     return new MetricSamplerBuilder(metricName, category, sampler, context);
/*     */   }
/*     */   
/*     */   public void onStartTick() {
/*  55 */     if (!this.isRunning) {
/*  56 */       throw new IllegalStateException("Not running");
/*     */     }
/*  58 */     if (this.beforeTick != null) {
/*  59 */       this.beforeTick.run();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEndTick(int currentTick) {
/*  70 */     verifyRunning();
/*  71 */     this.currentValue = this.sampler.getAsDouble();
/*  72 */     this.values.writeDouble(this.currentValue);
/*  73 */     this.ticks.writeInt(currentTick);
/*     */   }
/*     */   
/*     */   public void onFinished() {
/*  77 */     verifyRunning();
/*  78 */     this.values.release();
/*  79 */     this.ticks.release();
/*  80 */     this.isRunning = false;
/*     */   }
/*     */   
/*     */   private void verifyRunning() {
/*  84 */     if (!this.isRunning) {
/*  85 */       throw new IllegalStateException(String.format(Locale.ROOT, "Sampler for metric %s not started!", new Object[] { this.name }));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  90 */   DoubleSupplier getSampler() { return this.sampler; }
/*     */ 
/*     */ 
/*     */   
/*  94 */   public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/*  98 */   public MetricCategory getCategory() { return this.category; }
/*     */ 
/*     */   
/*     */   public SamplerResult result() {
/* 102 */     Int2DoubleOpenHashMap int2DoubleOpenHashMap = new Int2DoubleOpenHashMap();
/* 103 */     int firstTick = Integer.MIN_VALUE;
/* 104 */     int lastTick = Integer.MIN_VALUE;
/*     */     
/* 106 */     while (this.values.isReadable(8)) {
/* 107 */       int currentTick = this.ticks.readInt();
/* 108 */       if (firstTick == Integer.MIN_VALUE) {
/* 109 */         firstTick = currentTick;
/*     */       }
/* 111 */       int2DoubleOpenHashMap.put(currentTick, this.values.readDouble());
/* 112 */       lastTick = currentTick;
/*     */     } 
/* 114 */     return new SamplerResult(firstTick, lastTick, int2DoubleOpenHashMap);
/*     */   }
/*     */ 
/*     */   
/* 118 */   public boolean triggersThreshold() { return (this.thresholdTest != null && this.thresholdTest.test(this.currentValue)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 123 */     if (this == o) {
/* 124 */       return true;
/*     */     }
/* 126 */     if (o == null || getClass() != o.getClass()) {
/* 127 */       return false;
/*     */     }
/* 129 */     MetricSampler that = (MetricSampler)o;
/* 130 */     return (this.name.equals(that.name) && this.category.equals(that.category));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public int hashCode() { return this.name.hashCode(); }
/*     */ 
/*     */   
/*     */   public static class SamplerResult
/*     */   {
/*     */     private final Int2DoubleMap recording;
/*     */     
/*     */     private final int firstTick;
/*     */     
/*     */     private final int lastTick;
/*     */ 
/*     */     
/*     */     public SamplerResult(int firstTick, int lastTick, Int2DoubleMap recording) {
/* 148 */       this.firstTick = firstTick;
/* 149 */       this.lastTick = lastTick;
/* 150 */       this.recording = recording;
/*     */     }
/*     */ 
/*     */     
/* 154 */     public double valueAtTick(int tick) { return this.recording.get(tick); }
/*     */ 
/*     */ 
/*     */     
/* 158 */     public int getFirstTick() { return this.firstTick; }
/*     */ 
/*     */ 
/*     */     
/* 162 */     public int getLastTick() { return this.lastTick; } }
/*     */   
/*     */   public static class ValueIncreasedByPercentage implements ThresholdTest {
/*     */     private final float percentageIncreaseThreshold;
/*     */     
/*     */     public ValueIncreasedByPercentage(float percentageIncreaseThreshold) {
/* 168 */       this.previousValue = Double.MIN_VALUE;
/*     */ 
/*     */       
/* 171 */       this.percentageIncreaseThreshold = percentageIncreaseThreshold;
/*     */     }
/*     */     
/*     */     private double previousValue;
/*     */     
/*     */     public boolean test(double value) {
/*     */       boolean result;
/* 178 */       if (this.previousValue == Double.MIN_VALUE || value <= this.previousValue) {
/* 179 */         result = false;
/*     */       } else {
/* 181 */         result = ((value - this.previousValue) / this.previousValue >= this.percentageIncreaseThreshold);
/*     */       } 
/*     */       
/* 184 */       this.previousValue = value;
/* 185 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MetricSamplerBuilder<T>
/*     */     extends Object {
/*     */     private final String name;
/*     */     private final MetricCategory category;
/*     */     private final DoubleSupplier sampler;
/*     */     private final T context;
/*     */     private Runnable beforeTick;
/*     */     private MetricSampler.ThresholdTest thresholdTest;
/*     */     
/*     */     public MetricSamplerBuilder(String name, MetricCategory category, ToDoubleFunction<T> sampler, T context) {
/* 199 */       this.name = name;
/* 200 */       this.category = category;
/* 201 */       this.sampler = (() -> sampler.applyAsDouble(context));
/* 202 */       this.context = context;
/*     */     }
/*     */     
/*     */     public MetricSamplerBuilder<T> withBeforeTick(Consumer<T> beforeTick) {
/* 206 */       this.beforeTick = (() -> beforeTick.accept(this.context));
/* 207 */       return this;
/*     */     }
/*     */     
/*     */     public MetricSamplerBuilder<T> withThresholdAlert(MetricSampler.ThresholdTest thresholdTest) {
/* 211 */       this.thresholdTest = thresholdTest;
/* 212 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 216 */     public MetricSampler build() { return new MetricSampler(this.name, this.category, this.sampler, this.beforeTick, this.thresholdTest); }
/*     */   }
/*     */   
/*     */   public static interface ThresholdTest {
/*     */     boolean test(double param1Double);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\metrics\MetricSampler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
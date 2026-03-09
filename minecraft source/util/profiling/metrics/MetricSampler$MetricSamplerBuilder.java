/*     */ package net.minecraft.util.profiling.metrics;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.DoubleSupplier;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MetricSamplerBuilder<T>
/*     */   extends Object
/*     */ {
/*     */   private final String name;
/*     */   private final MetricCategory category;
/*     */   private final DoubleSupplier sampler;
/*     */   private final T context;
/*     */   private Runnable beforeTick;
/*     */   private MetricSampler.ThresholdTest thresholdTest;
/*     */   
/*     */   public MetricSamplerBuilder(String name, MetricCategory category, ToDoubleFunction<T> sampler, T context) {
/* 199 */     this.name = name;
/* 200 */     this.category = category;
/* 201 */     this.sampler = (() -> sampler.applyAsDouble(context));
/* 202 */     this.context = context;
/*     */   }
/*     */   
/*     */   public MetricSamplerBuilder<T> withBeforeTick(Consumer<T> beforeTick) {
/* 206 */     this.beforeTick = (() -> beforeTick.accept(this.context));
/* 207 */     return this;
/*     */   }
/*     */   
/*     */   public MetricSamplerBuilder<T> withThresholdAlert(MetricSampler.ThresholdTest thresholdTest) {
/* 211 */     this.thresholdTest = thresholdTest;
/* 212 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 216 */   public MetricSampler build() { return new MetricSampler(this.name, this.category, this.sampler, this.beforeTick, this.thresholdTest); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\metrics\MetricSampler$MetricSamplerBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
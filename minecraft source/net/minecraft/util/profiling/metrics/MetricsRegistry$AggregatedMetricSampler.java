/*    */ package net.minecraft.util.profiling.metrics;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class AggregatedMetricSampler
/*    */   extends MetricSampler
/*    */ {
/*    */   private final List<MetricSampler> delegates;
/*    */   
/*    */   private AggregatedMetricSampler(String name, List<MetricSampler> delegates) {
/* 51 */     super(name, ((MetricSampler)delegates.get(0)).getCategory(), () -> averageValueFromDelegates(delegates), () -> beforeTick(delegates), thresholdTest(delegates));
/* 52 */     this.delegates = delegates;
/*    */   }
/*    */   
/*    */   private static MetricSampler.ThresholdTest thresholdTest(List<MetricSampler> delegates) {
/* 56 */     return value -> delegates.stream().anyMatch(());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void beforeTick(List<MetricSampler> delegates) {
/* 65 */     for (MetricSampler delegate : delegates) {
/* 66 */       delegate.onStartTick();
/*    */     }
/*    */   }
/*    */   
/*    */   private static double averageValueFromDelegates(List<MetricSampler> delegates) {
/* 71 */     double aggregatedValue = 0.0D;
/*    */     
/* 73 */     for (MetricSampler delegate : delegates) {
/* 74 */       aggregatedValue += delegate.getSampler().getAsDouble();
/*    */     }
/*    */     
/* 77 */     return aggregatedValue / delegates.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 82 */     if (this == o) {
/* 83 */       return true;
/*    */     }
/* 85 */     if (o == null || getClass() != o.getClass()) {
/* 86 */       return false;
/*    */     }
/* 88 */     if (!super.equals(o)) {
/* 89 */       return false;
/*    */     }
/* 91 */     AggregatedMetricSampler that = (AggregatedMetricSampler)o;
/* 92 */     return this.delegates.equals(that.delegates);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 97 */   public int hashCode() { return Objects.hash(new Object[] { Integer.valueOf(super.hashCode()), this.delegates }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\metrics\MetricsRegistry$AggregatedMetricSampler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
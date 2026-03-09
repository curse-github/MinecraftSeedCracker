/*     */ package net.minecraft.util.profiling;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ProfilerFiller
/*     */ {
/*     */   public static final String ROOT = "root";
/*     */   
/*     */   void startTick();
/*     */   
/*     */   void endTick();
/*     */   
/*     */   void push(String paramString);
/*     */   
/*     */   void push(Supplier<String> paramSupplier);
/*     */   
/*     */   void pop();
/*     */   
/*     */   void popPush(String paramString);
/*     */   
/*     */   void popPush(Supplier<String> paramSupplier);
/*     */   
/*     */   default void addZoneText(String text) {}
/*     */   
/*     */   default void addZoneValue(long value) {}
/*     */   
/*     */   default void setZoneColor(int color) {}
/*     */   
/*     */   default Zone zone(String name) {
/*  34 */     push(name);
/*  35 */     return new Zone(this);
/*     */   }
/*     */   
/*     */   default Zone zone(Supplier<String> name) {
/*  39 */     push(name);
/*  40 */     return new Zone(this);
/*     */   }
/*     */ 
/*     */   
/*     */   void markForCharting(MetricCategory paramMetricCategory);
/*     */   
/*  46 */   default void incrementCounter(String name) { incrementCounter(name, 1); }
/*     */ 
/*     */   
/*     */   void incrementCounter(String paramString, int paramInt);
/*     */ 
/*     */   
/*  52 */   default void incrementCounter(Supplier<String> name) { incrementCounter(name, 1); }
/*     */ 
/*     */   
/*     */   void incrementCounter(Supplier<String> paramSupplier, int paramInt);
/*     */   
/*     */   static ProfilerFiller combine(ProfilerFiller first, ProfilerFiller second) {
/*  58 */     if (first == InactiveProfiler.INSTANCE) {
/*  59 */       return second;
/*     */     }
/*  61 */     if (second == InactiveProfiler.INSTANCE) {
/*  62 */       return first;
/*     */     }
/*  64 */     return new CombinedProfileFiller(first, second);
/*     */   }
/*     */   
/*     */   public static class CombinedProfileFiller implements ProfilerFiller {
/*     */     private final ProfilerFiller first;
/*     */     private final ProfilerFiller second;
/*     */     
/*     */     public CombinedProfileFiller(ProfilerFiller first, ProfilerFiller second) {
/*  72 */       this.first = first;
/*  73 */       this.second = second;
/*     */     }
/*     */ 
/*     */     
/*     */     public void startTick() {
/*  78 */       this.first.startTick();
/*  79 */       this.second.startTick();
/*     */     }
/*     */ 
/*     */     
/*     */     public void endTick() {
/*  84 */       this.first.endTick();
/*  85 */       this.second.endTick();
/*     */     }
/*     */ 
/*     */     
/*     */     public void push(String name) {
/*  90 */       this.first.push(name);
/*  91 */       this.second.push(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public void push(Supplier<String> name) {
/*  96 */       this.first.push(name);
/*  97 */       this.second.push(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public void markForCharting(MetricCategory category) {
/* 102 */       this.first.markForCharting(category);
/* 103 */       this.second.markForCharting(category);
/*     */     }
/*     */ 
/*     */     
/*     */     public void pop() {
/* 108 */       this.first.pop();
/* 109 */       this.second.pop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void popPush(String name) {
/* 114 */       this.first.popPush(name);
/* 115 */       this.second.popPush(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public void popPush(Supplier<String> name) {
/* 120 */       this.first.popPush(name);
/* 121 */       this.second.popPush(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public void incrementCounter(String name, int amount) {
/* 126 */       this.first.incrementCounter(name, amount);
/* 127 */       this.second.incrementCounter(name, amount);
/*     */     }
/*     */ 
/*     */     
/*     */     public void incrementCounter(Supplier<String> name, int amount) {
/* 132 */       this.first.incrementCounter(name, amount);
/* 133 */       this.second.incrementCounter(name, amount);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addZoneText(String text) {
/* 138 */       this.first.addZoneText(text);
/* 139 */       this.second.addZoneText(text);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addZoneValue(long value) {
/* 144 */       this.first.addZoneValue(value);
/* 145 */       this.second.addZoneValue(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setZoneColor(int color) {
/* 150 */       this.first.setZoneColor(color);
/* 151 */       this.second.setZoneColor(color);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\ProfilerFiller.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
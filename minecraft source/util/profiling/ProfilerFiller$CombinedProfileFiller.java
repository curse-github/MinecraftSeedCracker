/*     */ package net.minecraft.util.profiling;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CombinedProfileFiller
/*     */   implements ProfilerFiller
/*     */ {
/*     */   private final ProfilerFiller first;
/*     */   private final ProfilerFiller second;
/*     */   
/*     */   public CombinedProfileFiller(ProfilerFiller first, ProfilerFiller second) {
/*  72 */     this.first = first;
/*  73 */     this.second = second;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startTick() {
/*  78 */     this.first.startTick();
/*  79 */     this.second.startTick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void endTick() {
/*  84 */     this.first.endTick();
/*  85 */     this.second.endTick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(String name) {
/*  90 */     this.first.push(name);
/*  91 */     this.second.push(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(Supplier<String> name) {
/*  96 */     this.first.push(name);
/*  97 */     this.second.push(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void markForCharting(MetricCategory category) {
/* 102 */     this.first.markForCharting(category);
/* 103 */     this.second.markForCharting(category);
/*     */   }
/*     */ 
/*     */   
/*     */   public void pop() {
/* 108 */     this.first.pop();
/* 109 */     this.second.pop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void popPush(String name) {
/* 114 */     this.first.popPush(name);
/* 115 */     this.second.popPush(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void popPush(Supplier<String> name) {
/* 120 */     this.first.popPush(name);
/* 121 */     this.second.popPush(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void incrementCounter(String name, int amount) {
/* 126 */     this.first.incrementCounter(name, amount);
/* 127 */     this.second.incrementCounter(name, amount);
/*     */   }
/*     */ 
/*     */   
/*     */   public void incrementCounter(Supplier<String> name, int amount) {
/* 132 */     this.first.incrementCounter(name, amount);
/* 133 */     this.second.incrementCounter(name, amount);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addZoneText(String text) {
/* 138 */     this.first.addZoneText(text);
/* 139 */     this.second.addZoneText(text);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addZoneValue(long value) {
/* 144 */     this.first.addZoneValue(value);
/* 145 */     this.second.addZoneValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setZoneColor(int color) {
/* 150 */     this.first.setZoneColor(color);
/* 151 */     this.second.setZoneColor(color);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\ProfilerFiller$CombinedProfileFiller.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.util.profiling;
/*     */ 
/*     */ import com.mojang.jtracy.Plot;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import com.mojang.jtracy.Zone;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TracyZoneFiller implements ProfilerFiller {
/*  20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  22 */   private static final StackWalker STACK_WALKER = StackWalker.getInstance(Set.of(StackWalker.Option.RETAIN_CLASS_REFERENCE), 5);
/*     */   
/*  24 */   private final List<Zone> activeZones = new ArrayList();
/*  25 */   private final Map<String, PlotAndValue> plots = new HashMap();
/*     */ 
/*     */ 
/*     */   
/*  29 */   private final String name = Thread.currentThread().getName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startTick() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void endTick() {
/*  39 */     for (PlotAndValue plotAndValue : this.plots.values()) {
/*  40 */       plotAndValue.set(0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(String name) {
/*  46 */     String function = "";
/*  47 */     String file = "";
/*  48 */     int line = 0;
/*  49 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/*  50 */       Optional<StackWalker.StackFrame> result = (Optional)STACK_WALKER.walk(s -> s.filter(()).findFirst());
/*  51 */       if (result.isPresent()) {
/*  52 */         StackWalker.StackFrame frame = (StackWalker.StackFrame)result.get();
/*  53 */         function = frame.getMethodName();
/*  54 */         file = frame.getFileName();
/*  55 */         line = frame.getLineNumber();
/*     */       } 
/*     */     } 
/*  58 */     Zone zone = TracyClient.beginZone(name, function, file, line);
/*  59 */     this.activeZones.add(zone);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  64 */   public void push(Supplier<String> name) { push((String)name.get()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void pop() {
/*  69 */     if (this.activeZones.isEmpty()) {
/*  70 */       LOGGER.error("Tried to pop one too many times! Mismatched push() and pop()?");
/*     */       return;
/*     */     } 
/*  73 */     Zone zone = (Zone)this.activeZones.removeLast();
/*  74 */     zone.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void popPush(String name) {
/*  79 */     pop();
/*  80 */     push(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void popPush(Supplier<String> name) {
/*  85 */     pop();
/*  86 */     push((String)name.get());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void markForCharting(MetricCategory category) {}
/*     */ 
/*     */ 
/*     */   
/*  96 */   public void incrementCounter(String name, int amount) { ((PlotAndValue)this.plots.computeIfAbsent(name, s -> new PlotAndValue(this.name + " " + this.name))).add(amount); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public void incrementCounter(Supplier<String> name, int amount) { incrementCounter((String)name.get(), amount); }
/*     */ 
/*     */ 
/*     */   
/* 105 */   private Zone activeZone() { return (Zone)this.activeZones.getLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public void addZoneText(String text) { activeZone().addText(text); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public void addZoneValue(long value) { activeZone().addValue(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   public void setZoneColor(int color) { activeZone().setColor(color); }
/*     */   
/*     */   private static final class PlotAndValue
/*     */   {
/*     */     private final Plot plot;
/*     */     private int value;
/*     */     
/*     */     private PlotAndValue(String name) {
/* 128 */       this.plot = TracyClient.createPlot(name);
/* 129 */       this.value = 0;
/*     */     }
/*     */     
/*     */     void set(int value) {
/* 133 */       this.value = value;
/* 134 */       this.plot.setValue(value);
/*     */     }
/*     */ 
/*     */     
/* 138 */     void add(int amount) { set(this.value + amount); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\TracyZoneFiller.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
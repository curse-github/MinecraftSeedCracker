/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public interface InsideBlockEffectApplier
/*     */ {
/*  13 */   public static final InsideBlockEffectApplier NOOP = new InsideBlockEffectApplier()
/*     */     {
/*     */       public void apply(InsideBlockEffectType type) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void runBefore(InsideBlockEffectType type, Consumer<Entity> effect) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void runAfter(InsideBlockEffectType type, Consumer<Entity> effect) {}
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void apply(InsideBlockEffectType paramInsideBlockEffectType);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void runBefore(InsideBlockEffectType paramInsideBlockEffectType, Consumer<Entity> paramConsumer);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void runAfter(InsideBlockEffectType paramInsideBlockEffectType, Consumer<Entity> paramConsumer);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class StepBasedCollector
/*     */     implements InsideBlockEffectApplier
/*     */   {
/*  49 */     private static final InsideBlockEffectType[] APPLY_ORDER = InsideBlockEffectType.values();
/*     */     
/*     */     private static final int NO_STEP = -1;
/*  52 */     private final Set<InsideBlockEffectType> effectsInStep = EnumSet.noneOf(InsideBlockEffectType.class);
/*  53 */     private final Map<InsideBlockEffectType, List<Consumer<Entity>>> beforeEffectsInStep = Util.makeEnumMap(InsideBlockEffectType.class, type -> new ArrayList());
/*  54 */     private final Map<InsideBlockEffectType, List<Consumer<Entity>>> afterEffectsInStep = Util.makeEnumMap(InsideBlockEffectType.class, type -> new ArrayList());
/*     */     
/*  56 */     private final List<Consumer<Entity>> finalEffects = new ArrayList();
/*     */     
/*  58 */     private int lastStep = -1;
/*     */     
/*     */     public void advanceStep(int step) {
/*  61 */       if (this.lastStep != step) {
/*  62 */         this.lastStep = step;
/*  63 */         flushStep();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void applyAndClear(Entity entity) {
/*  68 */       flushStep();
/*  69 */       for (Consumer<Entity> effect : this.finalEffects) {
/*  70 */         if (!entity.isAlive()) {
/*     */           break;
/*     */         }
/*  73 */         effect.accept(entity);
/*     */       } 
/*  75 */       this.finalEffects.clear();
/*  76 */       this.lastStep = -1;
/*     */     }
/*     */     
/*     */     private void flushStep() {
/*  80 */       for (InsideBlockEffectType type : APPLY_ORDER) {
/*  81 */         List<Consumer<Entity>> beforeEffects = (List)this.beforeEffectsInStep.get(type);
/*  82 */         this.finalEffects.addAll(beforeEffects);
/*  83 */         beforeEffects.clear();
/*     */         
/*  85 */         if (this.effectsInStep.remove(type)) {
/*  86 */           this.finalEffects.add(type.effect());
/*     */         }
/*     */         
/*  89 */         List<Consumer<Entity>> afterEffects = (List)this.afterEffectsInStep.get(type);
/*  90 */         this.finalEffects.addAll(afterEffects);
/*  91 */         afterEffects.clear();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  97 */     public void apply(InsideBlockEffectType type) { this.effectsInStep.add(type); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     public void runBefore(InsideBlockEffectType type, Consumer<Entity> effect) { ((List)this.beforeEffectsInStep.get(type)).add(effect); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     public void runAfter(InsideBlockEffectType type, Consumer<Entity> effect) { ((List)this.afterEffectsInStep.get(type)).add(effect); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\InsideBlockEffectApplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
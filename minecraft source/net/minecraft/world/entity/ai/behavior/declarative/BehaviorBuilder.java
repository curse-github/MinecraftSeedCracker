/*     */ package net.minecraft.world.entity.ai.behavior.declarative;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.Const;
/*     */ import com.mojang.datafixers.kinds.IdF;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.kinds.OptionalBox;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.OneShot;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ public class BehaviorBuilder<E extends LivingEntity, M>
/*     */   extends Object
/*     */   implements App<BehaviorBuilder.Mu<E>, M> {
/*     */   private final TriggerWithResult<E, M> trigger;
/*     */   
/*     */   public static final class Mu<E extends LivingEntity> extends Object implements K1 {}
/*     */   
/*  32 */   public static <E extends LivingEntity, M> BehaviorBuilder<E, M> unbox(App<Mu<E>, M> box) { return (BehaviorBuilder)box; }
/*     */ 
/*     */ 
/*     */   
/*  36 */   public static <E extends LivingEntity> Instance<E> instance() { return new Instance(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends LivingEntity> OneShot<E> create(Function<Instance<E>, ? extends App<Mu<E>, Trigger<E>>> builder) {
/*  43 */     final TriggerWithResult<E, Trigger<E>> resolvedBuilder = get((App)builder.apply(instance()));
/*  44 */     return new OneShot<E>()
/*     */       {
/*     */         public boolean trigger(ServerLevel level, E body, long timestamp)
/*     */         {
/*  48 */           Trigger<E> trigger = (Trigger)resolvedBuilder.tryTrigger(level, body, timestamp);
/*  49 */           if (trigger == null) {
/*  50 */             return false;
/*     */           }
/*     */           
/*  53 */           return trigger.trigger(level, body, timestamp);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  58 */         public String debugString() { return "OneShot[" + resolvedBuilder.debugString() + "]"; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  63 */         public String toString() { return debugString(); }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends LivingEntity> OneShot<E> sequence(Trigger<? super E> first, Trigger<? super E> second) {
/*  72 */     return create(i -> i.group(i.ifTriggered(first)).apply(i, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public static <E extends LivingEntity> OneShot<E> triggerIf(Predicate<E> predicate, OneShot<? super E> behavior) { return sequence(triggerIf(predicate), behavior); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends LivingEntity> OneShot<E> triggerIf(Predicate<E> predicate) {
/*  82 */     return create(i -> i.point(()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E extends LivingEntity> OneShot<E> triggerIf(BiPredicate<ServerLevel, E> predicate) {
/*  87 */     return create(i -> i.point(()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  92 */   private static <E extends LivingEntity, M> TriggerWithResult<E, M> get(App<Mu<E>, M> box) { return (unbox(box)).trigger; }
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
/* 104 */   private BehaviorBuilder(TriggerWithResult<E, M> trigger) { this.trigger = trigger; }
/*     */ 
/*     */ 
/*     */   
/* 108 */   private static <E extends LivingEntity, M> BehaviorBuilder<E, M> create(TriggerWithResult<E, M> instanceFactory) { return new BehaviorBuilder(instanceFactory); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class PureMemory<E extends LivingEntity, F extends K1, Value>
/*     */     extends BehaviorBuilder<E, MemoryAccessor<F, Value>>
/*     */   {
/*     */     private PureMemory(final MemoryCondition<F, Value> condition)
/*     */     {
/* 118 */       super(new BehaviorBuilder.TriggerWithResult<E, MemoryAccessor<F, Value>>()
/*     */           {
/*     */             public MemoryAccessor<F, Value> tryTrigger(ServerLevel level, E body, long timestamp)
/*     */             {
/* 122 */               Brain<?> brain = body.getBrain();
/* 123 */               Optional<Value> value = brain.getMemoryInternal(condition.memory());
/* 124 */               if (value == null) {
/* 125 */                 return null;
/*     */               }
/* 127 */               return condition.createAccessor(brain, value);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 132 */             public String debugString() { return "M[" + String.valueOf(condition) + "]"; }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 137 */             public String toString() { return debugString(); } }); } } class null extends Object implements TriggerWithResult<E, MemoryAccessor<F, Value>> { public MemoryAccessor<F, Value> tryTrigger(ServerLevel level, E body, long timestamp) { Brain<?> brain = body.getBrain(); Optional<Value> value = brain.getMemoryInternal(condition.memory()); if (value == null) return null;  return condition.createAccessor(brain, value); } public String debugString() { return "M[" + String.valueOf(condition) + "]"; } public String toString() { return debugString(); } }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Constant<E extends LivingEntity, A>
/*     */     extends BehaviorBuilder<E, A>
/*     */   {
/*     */     private Constant(A a) {
/* 145 */       this(a, () -> "C[" + String.valueOf(a) + "]");
/*     */     }
/*     */     
/*     */     private Constant(final A a, final Supplier<String> debugString) {
/* 149 */       super(new BehaviorBuilder.TriggerWithResult<E, A>()
/*     */           {
/*     */             public A tryTrigger(ServerLevel level, E body, long timestamp) {
/* 152 */               return (A)a;
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 157 */             public String debugString() { return (String)debugString.get(); }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 162 */             public String toString() { return debugString(); } }); } } class null extends Object implements TriggerWithResult<E, A> { public A tryTrigger(ServerLevel level, E body, long timestamp) { return (A)a; } public String debugString() { return (String)debugString.get(); } public String toString() { return debugString(); } }
/*     */ 
/*     */   
/*     */   private static final class TriggerWrapper<E extends LivingEntity>
/*     */     extends BehaviorBuilder<E, Unit>
/*     */   {
/*     */     private TriggerWrapper(final Trigger<? super E> dependentTrigger)
/*     */     {
/* 170 */       super(new BehaviorBuilder.TriggerWithResult<E, Unit>()
/*     */           {
/*     */             public Unit tryTrigger(ServerLevel level, E body, long timestamp) {
/* 173 */               return dependentTrigger.trigger(level, body, timestamp) ? Unit.INSTANCE : null;
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 178 */             public String debugString() { return "T[" + String.valueOf(dependentTrigger) + "]"; } }); } } class null extends Object implements TriggerWithResult<E, Unit> { public Unit tryTrigger(ServerLevel level, E body, long timestamp) { return dependentTrigger.trigger(level, body, timestamp) ? Unit.INSTANCE : null; } public String debugString() { return "T[" + String.valueOf(dependentTrigger) + "]"; } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Instance<E extends LivingEntity>
/*     */     extends Object
/*     */     implements Applicative<Mu<E>, Instance.Mu<E>>
/*     */   {
/*     */     private static final class Mu<E extends LivingEntity>
/*     */       extends Object
/*     */       implements Applicative.Mu {}
/*     */ 
/*     */     
/* 192 */     public <Value> Optional<Value> tryGet(MemoryAccessor<OptionalBox.Mu, Value> box) { return OptionalBox.unbox(box.value()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     public <Value> Value get(MemoryAccessor<IdF.Mu, Value> box) { return (Value)IdF.get(box.value()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 202 */     public <Value> BehaviorBuilder<E, MemoryAccessor<OptionalBox.Mu, Value>> registered(MemoryModuleType<Value> memory) { return new BehaviorBuilder.PureMemory(new MemoryCondition.Registered(memory)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 207 */     public <Value> BehaviorBuilder<E, MemoryAccessor<IdF.Mu, Value>> present(MemoryModuleType<Value> memory) { return new BehaviorBuilder.PureMemory(new MemoryCondition.Present(memory)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     public <Value> BehaviorBuilder<E, MemoryAccessor<Const.Mu<Unit>, Value>> absent(MemoryModuleType<Value> memory) { return new BehaviorBuilder.PureMemory(new MemoryCondition.Absent(memory)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     public BehaviorBuilder<E, Unit> ifTriggered(Trigger<? super E> dependentTrigger) { return new BehaviorBuilder.TriggerWrapper(dependentTrigger); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 224 */     public <A> BehaviorBuilder<E, A> point(A a) { return new BehaviorBuilder.Constant(a); }
/*     */ 
/*     */ 
/*     */     
/* 228 */     public <A> BehaviorBuilder<E, A> point(Supplier<String> debugString, A a) { return new BehaviorBuilder.Constant(a, debugString); }
/*     */ 
/*     */ 
/*     */     
/*     */     public <A, R> Function<App<BehaviorBuilder.Mu<E>, A>, App<BehaviorBuilder.Mu<E>, R>> lift1(App<BehaviorBuilder.Mu<E>, Function<A, R>> function) {
/* 233 */       return a -> {
/* 234 */           final BehaviorBuilder.TriggerWithResult<E, A> aTrigger = BehaviorBuilder.get(a);
/* 235 */           final BehaviorBuilder.TriggerWithResult<E, Function<A, R>> fTrigger = BehaviorBuilder.get(function);
/*     */           
/* 237 */           return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this)
/*     */               {
/*     */                 public R tryTrigger(ServerLevel level, E body, long timestamp)
/*     */                 {
/* 241 */                   A ra = (A)aTrigger.tryTrigger(level, body, timestamp);
/* 242 */                   if (ra == null) {
/* 243 */                     return null;
/*     */                   }
/* 245 */                   Function<A, R> rf = (Function)fTrigger.tryTrigger(level, body, timestamp);
/* 246 */                   if (rf == null) {
/* 247 */                     return null;
/*     */                   }
/* 249 */                   return (R)rf.apply(ra);
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/* 254 */                 public String debugString() { return fTrigger.debugString() + " * " + fTrigger.debugString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 259 */                 public String toString() { return debugString(); }
/*     */               });
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T, R> BehaviorBuilder<E, R> map(final Function<? super T, ? extends R> func, App<BehaviorBuilder.Mu<E>, T> ts) {
/* 267 */       final BehaviorBuilder.TriggerWithResult<E, T> tTrigger = BehaviorBuilder.get(ts);
/* 268 */       return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this)
/*     */           {
/*     */             public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 271 */               T t = (T)tTrigger.tryTrigger(level, body, timestamp);
/* 272 */               if (t == null) {
/* 273 */                 return null;
/*     */               }
/* 275 */               return (R)func.apply(t);
/*     */             }
/*     */ 
/*     */             
/*     */             public String debugString() {
/* 280 */               return tTrigger.debugString() + ".map[" + tTrigger.debugString() + "]";
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 285 */             public String toString() { return debugString(); }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A, B, R> BehaviorBuilder<E, R> ap2(App<BehaviorBuilder.Mu<E>, BiFunction<A, B, R>> func, App<BehaviorBuilder.Mu<E>, A> a, App<BehaviorBuilder.Mu<E>, B> b) {
/* 294 */       final BehaviorBuilder.TriggerWithResult<E, A> aTrigger = BehaviorBuilder.get(a);
/* 295 */       final BehaviorBuilder.TriggerWithResult<E, B> bTrigger = BehaviorBuilder.get(b);
/* 296 */       final BehaviorBuilder.TriggerWithResult<E, BiFunction<A, B, R>> fTrigger = BehaviorBuilder.get(func);
/*     */       
/* 298 */       return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this)
/*     */           {
/*     */             public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 301 */               A ra = (A)aTrigger.tryTrigger(level, body, timestamp);
/* 302 */               if (ra == null) {
/* 303 */                 return null;
/*     */               }
/* 305 */               B rb = (B)bTrigger.tryTrigger(level, body, timestamp);
/* 306 */               if (rb == null) {
/* 307 */                 return null;
/*     */               }
/* 309 */               BiFunction<A, B, R> fr = (BiFunction)fTrigger.tryTrigger(level, body, timestamp);
/* 310 */               if (fr == null) {
/* 311 */                 return null;
/*     */               }
/* 313 */               return (R)fr.apply(ra, rb);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 318 */             public String debugString() { return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + aTrigger.debugString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 323 */             public String toString() { return debugString(); }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T1, T2, T3, R> BehaviorBuilder<E, R> ap3(App<BehaviorBuilder.Mu<E>, Function3<T1, T2, T3, R>> func, App<BehaviorBuilder.Mu<E>, T1> t1, App<BehaviorBuilder.Mu<E>, T2> t2, App<BehaviorBuilder.Mu<E>, T3> t3) {
/* 330 */       final BehaviorBuilder.TriggerWithResult<E, T1> t1Trigger = BehaviorBuilder.get(t1);
/* 331 */       final BehaviorBuilder.TriggerWithResult<E, T2> t2Trigger = BehaviorBuilder.get(t2);
/* 332 */       final BehaviorBuilder.TriggerWithResult<E, T3> t3Trigger = BehaviorBuilder.get(t3);
/* 333 */       final BehaviorBuilder.TriggerWithResult<E, Function3<T1, T2, T3, R>> fTrigger = BehaviorBuilder.get(func);
/*     */       
/* 335 */       return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this)
/*     */           {
/*     */             public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 338 */               T1 r1 = (T1)t1Trigger.tryTrigger(level, body, timestamp);
/* 339 */               if (r1 == null) {
/* 340 */                 return null;
/*     */               }
/* 342 */               T2 r2 = (T2)t2Trigger.tryTrigger(level, body, timestamp);
/* 343 */               if (r2 == null) {
/* 344 */                 return null;
/*     */               }
/* 346 */               T3 r3 = (T3)t3Trigger.tryTrigger(level, body, timestamp);
/* 347 */               if (r3 == null) {
/* 348 */                 return null;
/*     */               }
/* 350 */               Function3<T1, T2, T3, R> rf = (Function3)fTrigger.tryTrigger(level, body, timestamp);
/* 351 */               if (rf == null) {
/* 352 */                 return null;
/*     */               }
/* 354 */               return (R)rf.apply(r1, r2, r3);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 359 */             public String debugString() { return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + t1Trigger.debugString() + " * " + t2Trigger.debugString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 364 */             public String toString() { return debugString(); }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     public <T1, T2, T3, T4, R> BehaviorBuilder<E, R> ap4(App<BehaviorBuilder.Mu<E>, Function4<T1, T2, T3, T4, R>> func, App<BehaviorBuilder.Mu<E>, T1> t1, App<BehaviorBuilder.Mu<E>, T2> t2, App<BehaviorBuilder.Mu<E>, T3> t3, App<BehaviorBuilder.Mu<E>, T4> t4)
/*     */     {
/* 371 */       final BehaviorBuilder.TriggerWithResult<E, T1> t1Trigger = BehaviorBuilder.get(t1);
/* 372 */       final BehaviorBuilder.TriggerWithResult<E, T2> t2Trigger = BehaviorBuilder.get(t2);
/* 373 */       final BehaviorBuilder.TriggerWithResult<E, T3> t3Trigger = BehaviorBuilder.get(t3);
/* 374 */       final BehaviorBuilder.TriggerWithResult<E, T4> t4Trigger = BehaviorBuilder.get(t4);
/* 375 */       final BehaviorBuilder.TriggerWithResult<E, Function4<T1, T2, T3, T4, R>> fTrigger = BehaviorBuilder.get(func);
/*     */       
/* 377 */       return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this)
/*     */           {
/*     */             public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 380 */               T1 r1 = (T1)t1Trigger.tryTrigger(level, body, timestamp);
/* 381 */               if (r1 == null) {
/* 382 */                 return null;
/*     */               }
/* 384 */               T2 r2 = (T2)t2Trigger.tryTrigger(level, body, timestamp);
/* 385 */               if (r2 == null) {
/* 386 */                 return null;
/*     */               }
/* 388 */               T3 r3 = (T3)t3Trigger.tryTrigger(level, body, timestamp);
/* 389 */               if (r3 == null) {
/* 390 */                 return null;
/*     */               }
/* 392 */               T4 r4 = (T4)t4Trigger.tryTrigger(level, body, timestamp);
/* 393 */               if (r4 == null) {
/* 394 */                 return null;
/*     */               }
/* 396 */               Function4<T1, T2, T3, T4, R> rf = (Function4)fTrigger.tryTrigger(level, body, timestamp);
/* 397 */               if (rf == null) {
/* 398 */                 return null;
/*     */               }
/* 400 */               return (R)rf.apply(r1, r2, r3, r4);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 405 */             public String debugString() { return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + t1Trigger.debugString() + " * " + t2Trigger.debugString() + " * " + t3Trigger.debugString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 410 */             public String toString() { return debugString(); } }); } } private static final class Mu<E extends LivingEntity> extends Object implements Applicative.Mu {} class null extends Object implements TriggerWithResult<E, R> { null(BehaviorBuilder.Instance this$0) {} public R tryTrigger(ServerLevel level, E body, long timestamp) { A ra = (A)aTrigger.tryTrigger(level, body, timestamp); if (ra == null) return null;  Function<A, R> rf = (Function)fTrigger.tryTrigger(level, body, timestamp); if (rf == null) return null;  return (R)rf.apply(ra); } public String debugString() { return fTrigger.debugString() + " * " + fTrigger.debugString(); } public String toString() { return debugString(); } } class null extends Object implements TriggerWithResult<E, R> { null(BehaviorBuilder.Instance this$0) {} public R tryTrigger(ServerLevel level, E body, long timestamp) { T t = (T)tTrigger.tryTrigger(level, body, timestamp); if (t == null) return null;  return (R)func.apply(t); } public String debugString() { return tTrigger.debugString() + ".map[" + tTrigger.debugString() + "]"; } public String toString() { return debugString(); } } class null extends Object implements TriggerWithResult<E, R> { null(BehaviorBuilder.Instance this$0) {} public R tryTrigger(ServerLevel level, E body, long timestamp) { A ra = (A)aTrigger.tryTrigger(level, body, timestamp); if (ra == null) return null;  B rb = (B)bTrigger.tryTrigger(level, body, timestamp); if (rb == null) return null;  BiFunction<A, B, R> fr = (BiFunction)fTrigger.tryTrigger(level, body, timestamp); if (fr == null) return null;  return (R)fr.apply(ra, rb); } public String debugString() { return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + aTrigger.debugString(); } public String toString() { return debugString(); } } class null extends Object implements TriggerWithResult<E, R> { null(BehaviorBuilder.Instance this$0) {} public R tryTrigger(ServerLevel level, E body, long timestamp) { T1 r1 = (T1)t1Trigger.tryTrigger(level, body, timestamp); if (r1 == null) return null;  T2 r2 = (T2)t2Trigger.tryTrigger(level, body, timestamp); if (r2 == null) return null;  T3 r3 = (T3)t3Trigger.tryTrigger(level, body, timestamp); if (r3 == null) return null;  Function3<T1, T2, T3, R> rf = (Function3)fTrigger.tryTrigger(level, body, timestamp); if (rf == null) return null;  return (R)rf.apply(r1, r2, r3); } public String debugString() { return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + t1Trigger.debugString() + " * " + t2Trigger.debugString(); } public String toString() { return debugString(); } } class null extends Object implements TriggerWithResult<E, R> { public String toString() { return debugString(); }
/*     */     
/*     */     null(BehaviorBuilder.Instance this$0) {}
/*     */     
/*     */     public R tryTrigger(ServerLevel level, E body, long timestamp) {
/*     */       T1 r1 = (T1)t1Trigger.tryTrigger(level, body, timestamp);
/*     */       if (r1 == null)
/*     */         return null; 
/*     */       T2 r2 = (T2)t2Trigger.tryTrigger(level, body, timestamp);
/*     */       if (r2 == null)
/*     */         return null; 
/*     */       T3 r3 = (T3)t3Trigger.tryTrigger(level, body, timestamp);
/*     */       if (r3 == null)
/*     */         return null; 
/*     */       T4 r4 = (T4)t4Trigger.tryTrigger(level, body, timestamp);
/*     */       if (r4 == null)
/*     */         return null; 
/*     */       Function4<T1, T2, T3, T4, R> rf = (Function4)fTrigger.tryTrigger(level, body, timestamp);
/*     */       if (rf == null)
/*     */         return null; 
/*     */       return (R)rf.apply(r1, r2, r3, r4);
/*     */     }
/*     */     
/*     */     public String debugString() { return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + t1Trigger.debugString() + " * " + t2Trigger.debugString() + " * " + t3Trigger.debugString(); } }
/*     */ 
/*     */   
/*     */   private static interface TriggerWithResult<E extends LivingEntity, R> {
/*     */     R tryTrigger(ServerLevel param1ServerLevel, E param1E, long param1Long);
/*     */     
/*     */     String debugString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
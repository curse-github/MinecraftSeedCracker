/*     */ package net.minecraft.world.entity.ai.behavior.declarative;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.Const;
/*     */ import com.mojang.datafixers.kinds.IdF;
/*     */ import com.mojang.datafixers.kinds.OptionalBox;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
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
/*     */ public final class Instance<E extends LivingEntity>
/*     */   extends Object
/*     */   implements Applicative<BehaviorBuilder.Mu<E>, BehaviorBuilder.Instance.Mu<E>>
/*     */ {
/*     */   private static final class Mu<E extends LivingEntity>
/*     */     extends Object
/*     */     implements Applicative.Mu {}
/*     */   
/* 192 */   public <Value> Optional<Value> tryGet(MemoryAccessor<OptionalBox.Mu, Value> box) { return OptionalBox.unbox(box.value()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   public <Value> Value get(MemoryAccessor<IdF.Mu, Value> box) { return (Value)IdF.get(box.value()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   public <Value> BehaviorBuilder<E, MemoryAccessor<OptionalBox.Mu, Value>> registered(MemoryModuleType<Value> memory) { return new BehaviorBuilder.PureMemory(new MemoryCondition.Registered(memory)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   public <Value> BehaviorBuilder<E, MemoryAccessor<IdF.Mu, Value>> present(MemoryModuleType<Value> memory) { return new BehaviorBuilder.PureMemory(new MemoryCondition.Present(memory)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   public <Value> BehaviorBuilder<E, MemoryAccessor<Const.Mu<Unit>, Value>> absent(MemoryModuleType<Value> memory) { return new BehaviorBuilder.PureMemory(new MemoryCondition.Absent(memory)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   public BehaviorBuilder<E, Unit> ifTriggered(Trigger<? super E> dependentTrigger) { return new BehaviorBuilder.TriggerWrapper(dependentTrigger); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 224 */   public <A> BehaviorBuilder<E, A> point(A a) { return new BehaviorBuilder.Constant(a); }
/*     */ 
/*     */ 
/*     */   
/* 228 */   public <A> BehaviorBuilder<E, A> point(Supplier<String> debugString, A a) { return new BehaviorBuilder.Constant(a, debugString); }
/*     */ 
/*     */ 
/*     */   
/*     */   public <A, R> Function<App<BehaviorBuilder.Mu<E>, A>, App<BehaviorBuilder.Mu<E>, R>> lift1(App<BehaviorBuilder.Mu<E>, Function<A, R>> function) {
/* 233 */     return a -> {
/* 234 */         final BehaviorBuilder.TriggerWithResult<E, A> aTrigger = BehaviorBuilder.get(a);
/* 235 */         final BehaviorBuilder.TriggerWithResult<E, Function<A, R>> fTrigger = BehaviorBuilder.get(function);
/*     */         
/* 237 */         return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this)
/*     */             {
/*     */               public R tryTrigger(ServerLevel level, E body, long timestamp)
/*     */               {
/* 241 */                 A ra = (A)aTrigger.tryTrigger(level, body, timestamp);
/* 242 */                 if (ra == null) {
/* 243 */                   return null;
/*     */                 }
/* 245 */                 Function<A, R> rf = (Function)fTrigger.tryTrigger(level, body, timestamp);
/* 246 */                 if (rf == null) {
/* 247 */                   return null;
/*     */                 }
/* 249 */                 return (R)rf.apply(ra);
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 254 */               public String debugString() { return fTrigger.debugString() + " * " + fTrigger.debugString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 259 */               public String toString() { return debugString(); }
/*     */             });
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T, R> BehaviorBuilder<E, R> map(final Function<? super T, ? extends R> func, App<BehaviorBuilder.Mu<E>, T> ts) {
/* 267 */     final BehaviorBuilder.TriggerWithResult<E, T> tTrigger = BehaviorBuilder.get(ts);
/* 268 */     return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this)
/*     */         {
/*     */           public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 271 */             T t = (T)tTrigger.tryTrigger(level, body, timestamp);
/* 272 */             if (t == null) {
/* 273 */               return null;
/*     */             }
/* 275 */             return (R)func.apply(t);
/*     */           }
/*     */ 
/*     */           
/*     */           public String debugString() {
/* 280 */             return tTrigger.debugString() + ".map[" + tTrigger.debugString() + "]";
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 285 */           public String toString() { return debugString(); }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A, B, R> BehaviorBuilder<E, R> ap2(App<BehaviorBuilder.Mu<E>, BiFunction<A, B, R>> func, App<BehaviorBuilder.Mu<E>, A> a, App<BehaviorBuilder.Mu<E>, B> b) {
/* 294 */     final BehaviorBuilder.TriggerWithResult<E, A> aTrigger = BehaviorBuilder.get(a);
/* 295 */     final BehaviorBuilder.TriggerWithResult<E, B> bTrigger = BehaviorBuilder.get(b);
/* 296 */     final BehaviorBuilder.TriggerWithResult<E, BiFunction<A, B, R>> fTrigger = BehaviorBuilder.get(func);
/*     */     
/* 298 */     return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this)
/*     */         {
/*     */           public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 301 */             A ra = (A)aTrigger.tryTrigger(level, body, timestamp);
/* 302 */             if (ra == null) {
/* 303 */               return null;
/*     */             }
/* 305 */             B rb = (B)bTrigger.tryTrigger(level, body, timestamp);
/* 306 */             if (rb == null) {
/* 307 */               return null;
/*     */             }
/* 309 */             BiFunction<A, B, R> fr = (BiFunction)fTrigger.tryTrigger(level, body, timestamp);
/* 310 */             if (fr == null) {
/* 311 */               return null;
/*     */             }
/* 313 */             return (R)fr.apply(ra, rb);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 318 */           public String debugString() { return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + aTrigger.debugString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 323 */           public String toString() { return debugString(); }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T1, T2, T3, R> BehaviorBuilder<E, R> ap3(App<BehaviorBuilder.Mu<E>, Function3<T1, T2, T3, R>> func, App<BehaviorBuilder.Mu<E>, T1> t1, App<BehaviorBuilder.Mu<E>, T2> t2, App<BehaviorBuilder.Mu<E>, T3> t3) {
/* 330 */     final BehaviorBuilder.TriggerWithResult<E, T1> t1Trigger = BehaviorBuilder.get(t1);
/* 331 */     final BehaviorBuilder.TriggerWithResult<E, T2> t2Trigger = BehaviorBuilder.get(t2);
/* 332 */     final BehaviorBuilder.TriggerWithResult<E, T3> t3Trigger = BehaviorBuilder.get(t3);
/* 333 */     final BehaviorBuilder.TriggerWithResult<E, Function3<T1, T2, T3, R>> fTrigger = BehaviorBuilder.get(func);
/*     */     
/* 335 */     return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this)
/*     */         {
/*     */           public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 338 */             T1 r1 = (T1)t1Trigger.tryTrigger(level, body, timestamp);
/* 339 */             if (r1 == null) {
/* 340 */               return null;
/*     */             }
/* 342 */             T2 r2 = (T2)t2Trigger.tryTrigger(level, body, timestamp);
/* 343 */             if (r2 == null) {
/* 344 */               return null;
/*     */             }
/* 346 */             T3 r3 = (T3)t3Trigger.tryTrigger(level, body, timestamp);
/* 347 */             if (r3 == null) {
/* 348 */               return null;
/*     */             }
/* 350 */             Function3<T1, T2, T3, R> rf = (Function3)fTrigger.tryTrigger(level, body, timestamp);
/* 351 */             if (rf == null) {
/* 352 */               return null;
/*     */             }
/* 354 */             return (R)rf.apply(r1, r2, r3);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 359 */           public String debugString() { return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + t1Trigger.debugString() + " * " + t2Trigger.debugString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 364 */           public String toString() { return debugString(); }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T1, T2, T3, T4, R> BehaviorBuilder<E, R> ap4(App<BehaviorBuilder.Mu<E>, Function4<T1, T2, T3, T4, R>> func, App<BehaviorBuilder.Mu<E>, T1> t1, App<BehaviorBuilder.Mu<E>, T2> t2, App<BehaviorBuilder.Mu<E>, T3> t3, App<BehaviorBuilder.Mu<E>, T4> t4) {
/* 371 */     final BehaviorBuilder.TriggerWithResult<E, T1> t1Trigger = BehaviorBuilder.get(t1);
/* 372 */     final BehaviorBuilder.TriggerWithResult<E, T2> t2Trigger = BehaviorBuilder.get(t2);
/* 373 */     final BehaviorBuilder.TriggerWithResult<E, T3> t3Trigger = BehaviorBuilder.get(t3);
/* 374 */     final BehaviorBuilder.TriggerWithResult<E, T4> t4Trigger = BehaviorBuilder.get(t4);
/* 375 */     final BehaviorBuilder.TriggerWithResult<E, Function4<T1, T2, T3, T4, R>> fTrigger = BehaviorBuilder.get(func);
/*     */     
/* 377 */     return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this)
/*     */         {
/*     */           public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 380 */             T1 r1 = (T1)t1Trigger.tryTrigger(level, body, timestamp);
/* 381 */             if (r1 == null) {
/* 382 */               return null;
/*     */             }
/* 384 */             T2 r2 = (T2)t2Trigger.tryTrigger(level, body, timestamp);
/* 385 */             if (r2 == null) {
/* 386 */               return null;
/*     */             }
/* 388 */             T3 r3 = (T3)t3Trigger.tryTrigger(level, body, timestamp);
/* 389 */             if (r3 == null) {
/* 390 */               return null;
/*     */             }
/* 392 */             T4 r4 = (T4)t4Trigger.tryTrigger(level, body, timestamp);
/* 393 */             if (r4 == null) {
/* 394 */               return null;
/*     */             }
/* 396 */             Function4<T1, T2, T3, T4, R> rf = (Function4)fTrigger.tryTrigger(level, body, timestamp);
/* 397 */             if (rf == null) {
/* 398 */               return null;
/*     */             }
/* 400 */             return (R)rf.apply(r1, r2, r3, r4);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 405 */           public String debugString() { return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + t1Trigger.debugString() + " * " + t2Trigger.debugString() + " * " + t3Trigger.debugString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 410 */           public String toString() { return debugString(); }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$Instance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
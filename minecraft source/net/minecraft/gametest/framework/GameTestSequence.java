/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class GameTestSequence {
/*     */   private final GameTestInfo parent;
/*     */   private final List<GameTestEvent> events;
/*     */   private int lastTick;
/*     */   
/*     */   public class Condition {
/*     */     private static final int NOT_TRIGGERED = -1;
/*  17 */     private int triggerTime = -1;
/*     */     
/*     */     void trigger(int time) {
/*  20 */       if (this.triggerTime != -1) {
/*  21 */         throw new IllegalStateException("Condition already triggered at " + this.triggerTime);
/*     */       }
/*  23 */       this.triggerTime = time;
/*     */     }
/*     */     
/*     */     public void assertTriggeredThisTick() {
/*  27 */       int tick = GameTestSequence.this.parent.getTick();
/*  28 */       if (this.triggerTime != tick) {
/*  29 */         if (this.triggerTime == -1) {
/*  30 */           throw new GameTestAssertException(Component.translatable("test.error.sequence.condition_not_triggered"), tick);
/*     */         }
/*  32 */         throw new GameTestAssertException(Component.translatable("test.error.sequence.condition_already_triggered", new Object[] { Integer.valueOf(this.triggerTime) }), tick);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   GameTestSequence(GameTestInfo parent) {
/*  39 */     this.events = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */     
/*  43 */     this.parent = parent;
/*  44 */     this.lastTick = parent.getTick();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GameTestSequence thenWaitUntil(Runnable assertion) {
/*  51 */     this.events.add(GameTestEvent.create(assertion));
/*  52 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GameTestSequence thenWaitUntil(long expectedDelay, Runnable assertion) {
/*  59 */     this.events.add(GameTestEvent.create(expectedDelay, assertion));
/*  60 */     return this;
/*     */   }
/*     */   
/*     */   public GameTestSequence thenIdle(int delta) {
/*  64 */     return thenExecuteAfter(delta, () -> {
/*     */         
/*     */         });
/*     */   } public GameTestSequence thenExecute(Runnable assertion) {
/*  68 */     this.events.add(GameTestEvent.create(() -> executeWithoutFail(assertion)));
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public GameTestSequence thenExecuteAfter(int delta, Runnable after) {
/*  73 */     this.events.add(GameTestEvent.create(() -> {
/*  74 */             if (this.parent.getTick() < this.lastTick + delta) {
/*  75 */               throw new GameTestAssertException(Component.translatable("test.error.sequence.not_completed"), this.parent.getTick());
/*     */             }
/*  77 */             executeWithoutFail(after);
/*     */           }));
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public GameTestSequence thenExecuteFor(int delta, Runnable check) {
/*  83 */     this.events.add(GameTestEvent.create(() -> {
/*  84 */             if (this.parent.getTick() < this.lastTick + delta) {
/*  85 */               executeWithoutFail(check);
/*  86 */               throw new GameTestAssertException(Component.translatable("test.error.sequence.not_completed"), this.parent.getTick());
/*     */             } 
/*     */           }));
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   
/*  93 */   public void thenSucceed() { Objects.requireNonNull(this.parent); this.events.add(GameTestEvent.create(this.parent::succeed)); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public void thenFail(Supplier<GameTestException> e) { this.events.add(GameTestEvent.create(() -> this.parent.fail((GameTestException)e.get()))); }
/*     */ 
/*     */   
/*     */   public Condition thenTrigger() {
/* 101 */     Condition result = new Condition();
/* 102 */     this.events.add(GameTestEvent.create(() -> result.trigger(this.parent.getTick())));
/* 103 */     return result;
/*     */   }
/*     */   
/*     */   public void tickAndContinue(int tick) {
/*     */     try {
/* 108 */       tick(tick);
/* 109 */     } catch (GameTestAssertException gameTestAssertException) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public void tickAndFailIfNotComplete(int tick) {
/*     */     try {
/* 115 */       tick(tick);
/* 116 */     } catch (GameTestAssertException e) {
/* 117 */       this.parent.fail(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void executeWithoutFail(Runnable assertion) {
/*     */     try {
/* 123 */       assertion.run();
/* 124 */     } catch (GameTestAssertException e) {
/* 125 */       this.parent.fail(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tick(int tick) {
/* 130 */     Iterator<GameTestEvent> iterator = this.events.iterator();
/* 131 */     while (iterator.hasNext()) {
/* 132 */       GameTestEvent event = (GameTestEvent)iterator.next();
/* 133 */       event.assertion.run();
/* 134 */       iterator.remove();
/* 135 */       int delay = tick - this.lastTick;
/* 136 */       int prevTick = this.lastTick;
/* 137 */       this.lastTick = tick;
/* 138 */       if (event.expectedDelay != null && event.expectedDelay.longValue() != delay) {
/* 139 */         this.parent.fail(new GameTestAssertException(Component.translatable("test.error.sequence.invalid_tick", new Object[] { Long.valueOf(prevTick + event.expectedDelay.longValue()) }), tick));
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestSequence.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
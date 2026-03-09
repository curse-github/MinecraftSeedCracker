/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ 
/*     */ public class GateBehavior<E extends LivingEntity>
/*     */   extends Object
/*     */   implements BehaviorControl<E> {
/*     */   private final Map<MemoryModuleType<?>, MemoryStatus> entryCondition;
/*     */   private final Set<MemoryModuleType<?>> exitErasedMemories;
/*     */   private final OrderPolicy orderPolicy;
/*     */   private final RunningPolicy runningPolicy;
/*     */   private final ShufflingList<BehaviorControl<? super E>> behaviors;
/*     */   private Behavior.Status status;
/*     */   
/*     */   public GateBehavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, Set<MemoryModuleType<?>> exitErasedMemories, OrderPolicy orderPolicy, RunningPolicy runningPolicy, List<Pair<? extends BehaviorControl<? super E>, Integer>> behaviors) {
/*  27 */     this.behaviors = new ShufflingList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  37 */     this.status = Behavior.Status.STOPPED; this.entryCondition = entryCondition;
/*     */     this.exitErasedMemories = exitErasedMemories;
/*     */     this.orderPolicy = orderPolicy;
/*     */     this.runningPolicy = runningPolicy;
/*  41 */     behaviors.forEach(entry -> this.behaviors.add((BehaviorControl)entry.getFirst(), ((Integer)entry.getSecond()).intValue())); } public Behavior.Status getStatus() { return this.status; }
/*     */ 
/*     */   
/*     */   private boolean hasRequiredMemories(E body) {
/*  45 */     for (Map.Entry<MemoryModuleType<?>, MemoryStatus> entry : this.entryCondition.entrySet()) {
/*  46 */       MemoryModuleType<?> memoryType = (MemoryModuleType)entry.getKey();
/*  47 */       MemoryStatus requiredStatus = (MemoryStatus)entry.getValue();
/*  48 */       if (!body.getBrain().checkMemory(memoryType, requiredStatus)) {
/*  49 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  53 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean tryStart(ServerLevel level, E body, long timestamp) {
/*  58 */     if (hasRequiredMemories(body)) {
/*  59 */       this.status = Behavior.Status.RUNNING;
/*  60 */       this.orderPolicy.apply(this.behaviors);
/*  61 */       this.runningPolicy.apply(this.behaviors.stream(), level, body, timestamp);
/*  62 */       return true;
/*     */     } 
/*  64 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void tickOrStop(ServerLevel level, E body, long timestamp) {
/*  70 */     this.behaviors.stream()
/*  71 */       .filter(goal -> (goal.getStatus() == Behavior.Status.RUNNING))
/*  72 */       .forEach(goal -> goal.tickOrStop(level, body, timestamp));
/*     */ 
/*     */     
/*  75 */     if (this.behaviors.stream().noneMatch(g -> (g.getStatus() == Behavior.Status.RUNNING))) {
/*  76 */       doStop(level, body, timestamp);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void doStop(ServerLevel level, E body, long timestamp) {
/*  82 */     this.status = Behavior.Status.STOPPED;
/*     */     
/*  84 */     this.behaviors.stream()
/*  85 */       .filter(goal -> (goal.getStatus() == Behavior.Status.RUNNING))
/*  86 */       .forEach(goal -> goal.doStop(level, body, timestamp));
/*     */     
/*  88 */     Objects.requireNonNull(body.getBrain()); this.exitErasedMemories.forEach(body.getBrain()::eraseMemory);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public String debugString() { return getClass().getSimpleName(); }
/*     */   
/*     */   public enum OrderPolicy
/*     */   {
/*  97 */     ORDERED(t -> { 
/*  98 */       }), SHUFFLED(ShufflingList::shuffle);
/*     */ 
/*     */     
/*     */     private final Consumer<ShufflingList<?>> consumer;
/*     */ 
/*     */     
/* 104 */     OrderPolicy(Consumer<ShufflingList<?>> consumer) { this.consumer = consumer; }
/*     */ 
/*     */ 
/*     */     
/* 108 */     public void apply(ShufflingList<?> list) { this.consumer.accept(list); }
/*     */   }
/*     */   public final abstract enum RunningPolicy { RUN_ONE(ShufflingList::shuffle),
/*     */     TRY_ALL(ShufflingList::shuffle);
/*     */     public abstract <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> param1Stream, ServerLevel param1ServerLevel, E param1E, long param1Long);
/*     */     static  {
/*     */       // Byte code:
/*     */       //   0: new net/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy$1
/*     */       //   3: dup
/*     */       //   4: ldc 'RUN_ONE'
/*     */       //   6: iconst_0
/*     */       //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   10: putstatic net/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy.RUN_ONE : Lnet/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy;
/*     */       //   13: new net/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy$2
/*     */       //   16: dup
/*     */       //   17: ldc 'TRY_ALL'
/*     */       //   19: iconst_1
/*     */       //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   23: putstatic net/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy.TRY_ALL : Lnet/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy;
/*     */       //   26: invokestatic $values : ()[Lnet/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy;
/*     */       //   29: putstatic net/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy.$VALUES : [Lnet/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy;
/*     */       //   32: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #113	-> 0
/*     */       //   #122	-> 13
/*     */       //   #112	-> 26
/*     */     } }
/* 116 */   static enum null { public <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> behaviors, ServerLevel level, E body, long timestamp) { behaviors
/* 117 */         .filter(goal -> (goal.getStatus() == Behavior.Status.STOPPED))
/* 118 */         .filter(goal -> goal.tryStart(level, body, timestamp))
/* 119 */         .findFirst(); } }
/*     */ 
/*     */ 
/*     */   
/*     */   static enum null
/*     */   {
/* 125 */     public <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> behaviors, ServerLevel level, E body, long timestamp) { behaviors
/* 126 */         .filter(goal -> (goal.getStatus() == Behavior.Status.STOPPED))
/* 127 */         .forEach(goal -> goal.tryStart(level, body, timestamp)); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 139 */     Set<? extends BehaviorControl<? super E>> runningBehaviours = (Set)this.behaviors.stream().filter(goal -> (goal.getStatus() == Behavior.Status.RUNNING)).collect(Collectors.toSet());
/*     */     
/* 141 */     return "(" + getClass().getSimpleName() + "): " + String.valueOf(runningBehaviours);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\GateBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */